(ns quo2.components.list-items.saved-address.component-spec
  (:require [test-helpers.component :as h]
            [quo2.components.list-items.saved-address.view :as saved-address]
            [quo2.foundations.colors :as colors]))

(h/describe "List items: saved address"
  (h/test "default render"
    (h/render [saved-address/view])
    (h/is-truthy (h/query-by-label-text :container)))

  (h/test "on-press-in changes state to :pressed"
    (h/render [saved-address/view])
    (h/fire-event :on-press-in (h/get-by-label-text :container))
    (h/wait-for #(h/has-style (h/query-by-label-text :container)
                              {:backgroundColor (colors/custom-color :blue 50 5)})))

  (h/test "on-press-in changes state to :pressed with blur? enabled"
    (h/render [saved-address/view {:blur? true}])
    (h/fire-event :on-press-in (h/get-by-label-text :container))
    (h/wait-for #(h/has-style (h/query-by-label-text :container)
                              {:backgroundColor colors/white-opa-5})))

  (h/test "on-press-out changes state to :active"
    (h/render [saved-address/view])
    (h/fire-event :on-press-in (h/get-by-label-text :container))
    (h/fire-event :on-press-out (h/get-by-label-text :container))
    (h/wait-for #(h/has-style (h/query-by-label-text :container)
                              {:backgroundColor (colors/custom-color :blue 50 10)})))

  (h/test "on-press-out changes state to :active with blur? enabled"
    (h/render [saved-address/view {:blur? true}])
    (h/fire-event :on-press-in (h/get-by-label-text :container))
    (h/fire-event :on-press-out (h/get-by-label-text :container))
    (h/wait-for #(h/has-style (h/query-by-label-text :container)
                              {:backgroundColor colors/white-opa-10})))

  (h/test "on-press-out calls on-press"
    (let [on-press (h/mock-fn)]
      (h/render [saved-address/view {:on-press on-press}])
      (h/fire-event :on-press-in (h/get-by-label-text :container))
      (h/fire-event :on-press-out (h/get-by-label-text :container))
      (h/was-called on-press)))

  (h/test "renders options button if type :action"
    (let [on-options-press (h/mock-fn)]
      (h/render [saved-address/view
                 {:type             :action
                  :on-options-press on-options-press}])
      (h/is-truthy (h/query-by-label-text :options-button))
      (h/fire-event :on-press (h/get-by-label-text :options-button))
      (h/was-called on-options-press))))
