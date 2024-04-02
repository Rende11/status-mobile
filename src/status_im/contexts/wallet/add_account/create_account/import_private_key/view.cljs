(ns status-im.contexts.wallet.add-account.create-account.import-private-key.view
  (:require
    [clojure.string :as string]
    [quo.core :as quo]
    [react-native.core :as rn]
    [status-im.common.floating-button-page.view :as floating-button-page]
    [status-im.contexts.wallet.add-account.create-account.import-private-key.style :as style]
    [utils.address :as utils]
    [utils.i18n :as i18n]
    [utils.re-frame :as rf]))

(defn address-input []
(let [ [input-value set-input-value] (rn/use-state "")]
[quo/input
      {:accessibility-label :add-address-to-watch
       :placeholder         "Enter your private key";(i18n/label :t/address-placeholder)
       :container-style     {} ;style/input
       :label               "Private key";(i18n/label :t/eth-or-ens)
       :type           :password
        :error? true
       :return-key-type     :done
       :on-change-text      set-input-value
       :button              (when (string/blank? input-value)
                              {:on-press #(js/alert "ads")
                               :text     (i18n/label :t/paste)})
      ;;  :value               input-value
       }]))

(defn view []
   (let [customization-color (rf/sub [:profile/customization-color])]
[rn/view {:flex 1}
[floating-button-page/view
     {:customization-color    customization-color
      :header-container-style {:margin-top 8}
      :header                 [quo/page-nav
                               {:background :white
                                :type       :no-title
                                 
                                :icon-name  :i/close
                                :on-press   #(rf/dispatch [:navigate-back])}]
      :footer                 [quo/button
                               {:customization-color customization-color
                                :disabled?           false
                                :on-press            #()}
                               (i18n/label :t/continue)]}
                               
    [rn/view {:flex 1
      :border-width 1
    }
            [quo/page-top
           {:container-style  {}
            :title            "Import Private Key";(i18n/label :t/add-address)
            :description      :text
            :description-text "Enter the private key of an address";(i18n/label :t/enter-eth)
            }]
                [rn/view {
                  :flex 1
                  :justify-content :space-between
                  :border-width 1
                  :border-color :red
                  :padding-horizontal 20
    }
       [address-input
           {}]
           [quo/information-box
            {:type :default
             :icon :i/info
             :style {
               :justify-self :flex-end
               :align-self :flex-end}
             }
            "New addresses cannot be derived from an account imported from a private key. Import using a seed phrase if you wish to derive addresses."]
                ]
    ]
                               ]]))