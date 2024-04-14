(ns status-im.contexts.wallet.add-account.create-account.import-private-key.view
  (:require
    [clojure.string :as string]
    [quo.core :as quo]
    [react-native.clipboard :as clipboard]
    [react-native.core :as rn]
    [status-im.common.floating-button-page.view :as floating-button-page]
    [status-im.contexts.wallet.add-account.create-account.import-private-key.style :as style]
    [status-im.contexts.wallet.common.validation :as v]
    [utils.debounce :as debounce]
    [utils.i18n :as i18n]
    [utils.re-frame :as rf]))

(defn address-input
  [{:keys [input-value set-input-value set-flow-state error?]}]
  (let [check-address (rn/use-callback
                       (debounce/debounce (fn [v]
                                            (if (empty? v)
                                              (set-flow-state nil)
                                              ;; TODO check for validation
                                              (if-not (v/private-key? v)
                                                (set-flow-state :invalid-private-key)
                                                ;; TODO add real requests
                                                (do
                                                  (set-flow-state :scanning)
                                                  (js/setTimeout
                                                   set-flow-state
                                                   400
                                                   (rand-nth [:active-address :inactive-address]))))))
                                          500))
        on-change     (rn/use-callback
                       (fn [v]
                         (set-input-value v)
                         (check-address v)))

        on-paste      (rn/use-callback
                       (fn []
                         (clipboard/get-string
                          (fn [clipboard]
                            (when-not (empty? clipboard)
                              (on-change clipboard))))))]
    [quo/input
     {:accessibility-label :add-address-to-watch
      :placeholder         (i18n/label :t/enter-private-key-placeholder)
      :container-style     style/input
      :label               (i18n/label :t/private-key)
      :type                :password
      :error?              error?
      :return-key-type     :done
      :on-change-text      on-change
      :button              (when (string/blank? input-value)
                             {:on-press on-paste
                              :text     (i18n/label :t/paste)})
      :value               input-value}]))

(defn activity-indicator
  [state]
  (let [{:keys [message] :as props}
        (case state
          :scanning
          {:type    :info
           :icon    :i/scanning
           :message :t/scanning-for-activity}

          :inactive-address
          {:type    :warning
           :icon    :i/info
           :message :t/this-account-has-no-activity}

          :active-address
          {:type    :success
           :icon    :i/done
           :message :t/this-address-has-activity}

          :invalid-private-key
          {:type    :error
           :icon    :i/info
           :message :t/invalid-private-key}

          nil)]

    (when props
      [quo/info-message
       (assoc props
              :size  :default
              :style style/indicator)
       (i18n/label message)])))

(defn view
  []
  (let [customization-color           (rf/sub [:profile/customization-color])
        [input-value set-input-value] (rn/use-state "")
        [flow-state set-flow-state]   (rn/use-state)
        error?                        (= :invalid-private-key flow-state)]
    [rn/view {:flex 1}
     [floating-button-page/view
      {:customization-color customization-color
       :header              [quo/page-nav
                             {:background :white
                              :type       :no-title
                              :icon-name  :i/close
                              :on-press   #(rf/dispatch [:navigate-back])}]
       :footer              [:<>
                             (when-not (#{:active-address :inactive-address} flow-state)
                               [quo/information-box
                                {:type  :default
                                 :icon  :i/info
                                 :style style/info-box}
                                (i18n/label :t/import-private-key-info)])
                             [quo/button
                              {:customization-color customization-color
                               :disabled?           (or (empty? input-value) error?)
                               :on-press            #()}
                              (i18n/label :t/continue)]]}
      [quo/page-top
       {:container-style  style/page-top
        :title            (i18n/label :t/import-private-key)
        :description      :text
        :description-text (i18n/label :t/enter-private-key)}]
      [address-input
       {:input-value     input-value
        :set-input-value set-input-value
        :set-flow-state  set-flow-state
        :error?          error?}]
      (when (#{:scanning :active-address :inactive-address} flow-state)
        [rn/view {:style style/key-section}
         [quo/section-label
          {:section         (i18n/label :t/private-key-public-address)
           :container-style style/section-label}]
         ;; TODO Get real address
         [rn/view {:style (style/public-address flow-state)}
          [quo/text "0xd8dA6BF26964aF9D7eEd9e03E53415D37aA96045"]]])
      (when (seq input-value)
        [activity-indicator flow-state])]]))
