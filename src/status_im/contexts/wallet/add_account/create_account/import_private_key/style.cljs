(ns status-im.contexts.wallet.add-account.create-account.import-private-key.style
  (:require [quo.foundations.colors :as colors]))

(def input
  {:margin-top         12
   :padding-horizontal 20})

(def indicator
  {:padding-horizontal 20
   :padding-vertical   8})

(def info-box
  {:margin-bottom 20})

(def page-top
  {:margin-top 2})

(def key-section
  {:margin-top         22
   :padding-horizontal 20})

(def section-label
  {:margin-bottom 8})

(defn public-address
  [state]
  (let [border-color (case state
                       :active-address   colors/success-50-opa-40
                       :inactive-address colors/warning-50-opa-40
                       colors/neutral-20)]
    {:border-width       1
     :border-color       border-color
     :border-style       :dashed
     :border-radius      16
     :padding-vertical   8
     :padding-horizontal 12}))
