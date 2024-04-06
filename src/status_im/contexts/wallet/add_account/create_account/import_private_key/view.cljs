(ns status-im.contexts.wallet.add-account.create-account.import-private-key.view
  (:require
    [clojure.string :as string]
    [quo.core :as quo]
    [react-native.clipboard :as clipboard]
    [react-native.core :as rn]
    [status-im.common.floating-button-page.view :as floating-button-page]
    [status-im.contexts.wallet.add-account.create-account.import-private-key.style :as style]
    [utils.address :as utils]
    [utils.i18n :as i18n]
    [utils.re-frame :as rf]))

(defn address-input [{:keys [input-value set-input-value error?]}]
  (let [on-paste (rn/use-callback
                                (fn []
                                  (clipboard/get-string
                                   (fn [clipboard]
                                     (when-not (empty? clipboard)
                                       (set-input-value clipboard))))))]
    [quo/input
     {:accessibility-label :add-address-to-watch
      :placeholder         (i18n/label :t/enter-private-key-placeholder)
      :container-style     {:margin-top 12
                            :padding-horizontal 20}
      :label               (i18n/label :t/private-key)
      :type           :password
      :error? error?
      :return-key-type     :done
      :on-change-text      set-input-value
      :button              (when (string/blank? input-value)
                             {:on-press on-paste
                              :text     (i18n/label :t/paste)})
      :value               input-value
      }]))

(defn view []
  (let [customization-color (rf/sub [:profile/customization-color])
        [input-value set-input-value] (rn/use-state "")]
    [rn/view {:flex 1}
     [floating-button-page/view
      {:customization-color    customization-color
       :header                 [quo/page-nav
                                {:background :white
                                 :type       :no-title

                                 :icon-name  :i/close
                                 :on-press   #(rf/dispatch [:navigate-back])}]
       :footer                 [:<>
                                [quo/information-box
                                 {:type :default
                                  :icon :i/info
                                  :style {:margin-bottom 20}}
                                 (i18n/label :t/import-private-key-info)]
                                [quo/button
                                 {:customization-color customization-color
                                  :disabled?           (empty? input-value)
                                  :on-press            #()}
                                 (i18n/label :t/continue)]]}
      [quo/page-top
       {:container-style  {:margin-top 2}
        :title            (i18n/label :t/import-private-key)
        :description      :text
        :description-text  (i18n/label :t/enter-private-key)}]
      [address-input
       {:input-value input-value
        :set-input-value set-input-value
        :error? false}]]]))
