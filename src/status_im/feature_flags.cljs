(ns status-im.feature-flags
  (:require
    [clojure.string :as string]
    [react-native.config :as config]
    [reagent.core :as reagent]))

(defn- enabled-in-env?
  [k]
  (= "1" (config/get-config k)))

(defonce ^:private feature-flags-config
  (reagent/atom
   {::wallet.import-private-key        true ;(enabled-in-env? :FLAG_IMPORT_PRIVATE_KEY_ENABLED)
    ::wallet.network-filter            (enabled-in-env? :FLAG_NETWORK_FILTER_ENABLED)
    ::community.edit-account-selection (enabled-in-env? :FLAG_EDIT_ACCOUNT_SELECTION_ENABLED)}))

(defn feature-flags [] @feature-flags-config)

(def feature-flags-categories
  (set (map
        (fn [k]
          (first (string/split (str (name k)) ".")))
        (keys @feature-flags-config))))

(defn enabled?
  [flag]
  (get (feature-flags) flag))

(defn toggle
  [flag]
  (swap! feature-flags-config update flag not))

(defn alert
  [flag action]
  (if (enabled? flag)
    (action)
    (js/alert (str flag " is currently feature flagged off"))))
