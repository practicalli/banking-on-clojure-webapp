;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Request handlers
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(ns practicalli.request-handler
  (:require
   ;; Web Application
   [ring.util.response :refer [response]]
   [hiccup.core :refer [html]]
   [hiccup.page :refer [html5 include-js include-css]]
   [hiccup.element :refer [link-to]]

   ;; Data access
   [practicalli.hanlder-helpers :as helper]))


;; Markup Generators
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn bank-account-media-object
  [account-details]
  [:article {:class "media"}
   [:figure {:class "media-left"}
    [:p {:class "image is-64x64"}
     [:img {:src "https://raw.githubusercontent.com/jr0cket/developer-guides/master/clojure/clojure-bank-coin.png"}]]]
   [:div {:class "media-content"}
    [:div {:class "content"}
     [:h3 {:class "subtitle"}
      (str (:account-type account-details) " : &lambda;" (:account-value account-details))]]

    [:div {:class "field is-grouped"}
     [:div {:class "control"}
      [:div {:class "tags has-addons"}
       [:span {:class "tag"} "Account number"]
       [:span {:class "tag is-success is-light"} (:account-number account-details)]]]

     [:div {:class "tags has-addons"}
      [:span {:class "tag"} "Sort Code"]
      [:span {:class "tag is-success is-light"} (:account-sort-code account-details)]]]]

   [:div {:class "media-right"}
    (link-to {:class "button is-primary"} "/transfer" "Transfer")
    (link-to {:class "button is-info"} "/payment" "Payment")]])



;; HTML Pages
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn welcome-page
  [request]
  (response
    (html5
      {:lang "en"}
      [:head
       (include-css "https://cdn.jsdelivr.net/npm/bulma@0.9.0/css/bulma.min.css")]
      [:body
       [:section {:class "hero is-info"}
        [:div {:class "hero-body"}
         [:div {:class "container"}
          [:h1 {:class "title"} "Banking on Clojure"]
          [:p {:class "subtitle"}
           "Making your money immutable"]]]]

       [:section {:class "section"}
        [:div {:class "container"}
         (link-to {:class "button is-primary"} "/accounts"    "Login")
         (link-to {:class "button is-danger"}  "/register" "Register")
         [:p {:class "content"}
          "Manage your money without unexpected side-effects using a simple made easy banking service"]
         [:img {:src "https://raw.githubusercontent.com/jr0cket/developer-guides/master/clojure/clojure-piggy-bank.png"}]]]
       ])))


(defn register-customer
  [request]
  (response
    (html
      [:div
       [:h1 "Banking on Clojure"]
       [:p "New account holder" ]
       (helper/new-customer
         #:customer{:legal-name             "Terry Able"
                    :email_address          "terry@able.org"
                    :residential_address    "1 Hard Code Drive, Altar IV"
                    :social_security_number "xx104312D"
                    :preferred_name         "Terri"})

       [:img {:src "https://raw.githubusercontent.com/jr0cket/developer-guides/master/clojure/clojure-piggy-bank.png"}]])))


(defn accounts-overview-page
  "Overview of each bank account owned by the current customer.

  Using Bulma media object style
  https://bulma.io/documentation/layout/media-object/

  Request hash-map is not currently used"

  [request]
  (response
    (html5
      {:lang "en"}
      [:head
       (include-css "https://cdn.jsdelivr.net/npm/bulma@0.9.0/css/bulma.min.css")]
      [:body
       [:section {:class "hero is-info"}
        [:div {:class "hero-body"}
         [:div {:class "container"}
          [:h1 {:class "title"} "Banking on Clojure"]
          [:p {:class "subtitle"}
           "Making your money immutable"]]]]

       [:section {:class "section"}
        (bank-account-media-object {:account-type  "Current Account" :account-number    "123456789"
                                    :account-value "1,234"           :account-sort-code "01-02-01"})

        (bank-account-media-object {:account-type  "Savings Account" :account-number    "123454321"
                                    :account-value "2,000"           :account-sort-code "01-02-01"})

        (bank-account-media-object {:account-type  "Tax Free Savings Account" :account-number    "123454321"
                                    :account-value "20,000"                   :account-sort-code "01-02-01"})

        (bank-account-media-object {:account-type  "Mortgage Account" :account-number    "98r9e8r79wr87e9232"
                                    :account-value "354,000"          :account-sort-code "01-02-01"})

        ]])))


(defn account-history
  [request]
  (response
    (html5
      {:lang "en"}
      [:head
       (include-css "https://cdn.jsdelivr.net/npm/bulma@0.9.0/css/bulma.min.css")]
      [:body
       [:section {:class "hero is-info"}
        [:div {:class "hero-body"}
         [:div {:class "container"}
          [:h1 {:class "title"} "Banking on Clojure"]
          [:p {:class "subtitle"}
           "Making your money immutable"]]]]

       [:section {:class "section"}
        [:div {:class "container"}
         [:h1 {:class "title"}
          "Account History"]
         [:p {:class "content"}
          "Manage your money without unexpected side-effects using a simple made easy banking service"]
         [:img {:src "https://raw.githubusercontent.com/jr0cket/developer-guides/master/clojure/clojure-piggy-bank.png"}]]]
       ])))


;; TODO: Add form to transfer money from one account to another
(defn money-transfer
  [request]
  (response
    (html5
      {:lang "en"}
      [:head
       (include-css "https://cdn.jsdelivr.net/npm/bulma@0.9.0/css/bulma.min.css")]
      [:body
       [:section {:class "hero is-info"}
        [:div {:class "hero-body"}
         [:div {:class "container"}
          [:h1 {:class "title"} "Banking on Clojure"]
          [:p {:class "subtitle"}
           "Making your money immutable"]]]]

       [:section {:class "section"}
        [:div {:class "container"}
         [:h1 {:class "title"}
          "Transfer Dashboard"]
         [:p {:class "content"}
          "Manage your money without unexpected side-effects using a simple made easy banking service"]
         [:img {:src "https://raw.githubusercontent.com/jr0cket/developer-guides/master/clojure/clojure-piggy-bank.png"}]]]
       ])))


;; TODO: Add form to transfer money from one account to another
(defn money-payment
  [request]
  (response
    (html5
      {:lang "en"}
      [:head
       (include-css "https://cdn.jsdelivr.net/npm/bulma@0.9.0/css/bulma.min.css")]
      [:body
       [:section {:class "hero is-info"}
        [:div {:class "hero-body"}
         [:div {:class "container"}
          [:h1 {:class "title"} "Banking on Clojure"]
          [:p {:class "subtitle"}
           "Making your money immutable"]]]]

       [:section {:class "section"}
        [:div {:class "container"}
         [:h1 {:class "title"}
          "Payment Dashboard"]
         [:p {:class "content"}
          "Manage your money without unexpected side-effects using a simple made easy banking service"]
         [:img {:src "https://raw.githubusercontent.com/jr0cket/developer-guides/master/clojure/clojure-piggy-bank.png"}]]]
       ])))




(comment

  ;; Test handler function with empty request
  (welcome-page {})

  (html5 {})
  ;; => "<!DOCTYPE html>\n<html></html>"

  (html5
    {:lang "en"}
    [:head
     (include-js "myscript.js")
     (include-css "mystyle.css")]
    [:body
     [:div
      [:h1 {:class "info"} "Hiccup"]]])
  ;; => "<!DOCTYPE html>\n<html lang=\"en\"><head><script src=\"myscript.js\" type=\"text/javascript\"></script><link href=\"mystyle.css\" rel=\"stylesheet\" type=\"text/css\"></head><body><div><h1 class=\"info\">Hiccup</h1></div></body></html>"



  )


;; RDD
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(comment

  ;; Simple handler

  (defn welcome-page
    [request]
    (response (html [:div
                     [:h1 "Banking on Clojure"]
                     [:img {:src "https://raw.githubusercontent.com/jr0cket/developer-guides/master/clojure/clojure-piggy-bank.png"}]])))



  ;; Initial design of accounts page

  (defn account-dashboard
    [request]
    (html5
      {:lang "en"}
      [:head
       (include-css "https://cdn.jsdelivr.net/npm/bulma@0.9.0/css/bulma.min.css")]
      [:body
       [:section {:class "hero is-info"}
        [:div {:class "hero-body"}
         [:div {:class "container"}
          [:h1 {:class "title"} "Banking on Clojure"]
          [:p {:class "subtitle"}
           "Making your money immutable"]]]]

       [:section {:class "section"}
        [:div {:class "container"}
         [:h1 {:class "title"}
          "Account Dashboard"]
         [:p {:class "content"}
          "Manage your money without unexpected side-effects using a simple made easy banking service"]

         [:div {:class "box"}
          [:h1 {:class "title"}
           "Current Account"]
          [:p {:class "content"}
           "Manage your money without unexpected side-effects using a simple made easy banking service"]]

         [:div {:class "box"}
          [:h1 {:class "title"}
           "Savings Account"]
          [:p {:class "content"}
           "Manage your money without unexpected side-effects using a simple made easy banking service"]]

         [:div {:class "box"}
          [:h1 {:class "title"}
           "Tax Free Savings Account"]
          [:p {:class "content"}
           "Manage your money without unexpected side-effects using a simple made easy banking service"]]

         [:div {:class "box"}
          [:h1 {:class "title"}
           "Credit Card Account"]
          [:p {:class "content"}
           "Manage your money without unexpected side-effects using a simple made easy banking service"]]

         [:div {:class "box"}
          [:h1 {:class "title"}
           "Mortgage"]
          [:p {:class "content"}
           "Manage your money without unexpected side-effects using a simple made easy banking service"]]]
        ]]))


  ;; Abstracting hiccup with functions
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  (defn unordered-list [items]
    [:ul
     (for [i items]
       [:li i])])

  ;; Many lines of code can now be reduced to a single line

  ;; [:div
  ;;  (unordered-list ["collection" "of" "list" "items"])]


  (defn bank-account-media-object
    [account-details]
    [:article {:class "media"}
     [:figure {:class "media-left"}
      [:p {:class "image is-64x64"}
       [:img {:src "https://raw.githubusercontent.com/jr0cket/developer-guides/master/clojure/clojure-bank-coin.png"}]]]
     [:div {:class "media-content"}
      [:div {:class "content"}
       [:h3 {:class "subtitle"}
        "Current Account : &lambda;" (:account-value account-details)]
       [:p "Account number: " {:account-number account-details} " Sort code: " {:account-sort-code account-details}]]]
     [:div {:class "media-right"}
      (link-to {:class "button is-primary"} "/transfer" "Transfer")
      (link-to {:class "button is-info"} "/payment" "Payment")]])

  #_(bank-account-media-object
      {:account-type      "Current Account"
       :account-number    "123456789"
       :account-value     "i1,000,000"
       :account-sort-code "01-02-01"})
;; => [:article {:class "media"} [:figure {:class "media-left"} [:p {:class "image is-64x64"} [:img {:src "https://raw.githubusercontent.com/jr0cket/developer-guides/master/clojure/clojure-bank-coin.png"}]]] [:div {:class "media-content"} [:div {:class "content"} [:h3 {:class "subtitle"} "Current Account : &lambda;" "i1,000,000"] [:p "Account number: " {:account-number {:account-type "Current Account", :account-number "123456789", :account-value "i1,000,000", :account-sort-code "01-02-01"}} " Sort code: " {:account-sort-code {:account-type "Current Account", :account-number "123456789", :account-value "i1,000,000", :account-sort-code "01-02-01"}}]]] [:div {:class "media-right"} [:a {:href #object[java.net.URI 0x3516357f "/transfer"], :class "button is-primary"} ("Transfer")] [:a {:href #object[java.net.URI 0x4cf62d27 "/payment"], :class "button is-info"} ("Payment")]]]




  )  ;; End of Rich Comment Block
