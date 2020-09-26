(ns practicalli.handler-helpers-test
  (:require
   ;; Unit testing
   [clojure.test :refer [deftest is testing]]

   ;; Specifications and Generative testing
   [clojure.spec.alpha :as spec]
   [clojure.spec.gen.alpha :as spec-gen]
   [practicalli.data.specs]

   ;; System under test
   [practicalli.hanlder-helpers :as SUT]))

;; TODO setup database...


(deftest new-customer-test
  (is (spec/valid? :customer/id
                   (:id (SUT/new-customer
                          (spec-gen/generate (spec/gen :customer/unregistered))))))
  )



;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment


  (SUT/new-customer
    (spec-gen/generate (spec/gen :customer/unregistered)))

  ;; Register a customer and use the returned uuid to get that customers details
  (SUT/customer-account-overview
    (:customer/id (SUT/new-customer
                    (spec-gen/generate (spec/gen :customer/unregistered)))))


  ;; Register a customer and use the returned uuid to get that customers details
  #_(SUT/customer-details
      (:account-holders/id (SUT/new-customer
                             (spec-gen/generate (spec/gen :customer/unregistered)))))


  ) ;; End of rich comment block
