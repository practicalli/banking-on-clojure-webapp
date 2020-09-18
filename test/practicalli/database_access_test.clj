(ns practicalli.database-access-test
  (:require
   ;; Unit testing
   [clojure.test :refer [deftest is testing]]

   ;; Clojure Specifications
   [clojure.spec.alpha :as spec]
   [clojure.spec.gen.alpha :as spec-gen]
   [practicalli.specifications-banking]

   ;; System under test
   [practicalli.database-access :as SUT])
  )


(deftest new-account-holder-test
  (testing "Registered account holder is valid specification"
    (is (map? (SUT/new-account-holder
                (spec-gen/generate
                  (spec/gen :practicalli.specifications-banking/customer-details)))))))



(comment



  ;; Mock data from specifications
  ;; - require banking specifications namespace
  ;; - call mock-data-* functions  mock-data-customer-details
  (practicalli.specifications-banking/mock-data-customer-details)
  (practicalli.specifications-banking/mock-data-account-holder)


  (SUT/create-record
    SUT/db-specification-dev
    :public.account_holders
    (practicalli.specifications-banking/mock-data-account-holder))





  (SUT/create-record SUT/db-specification-dev
                     :public.account_holders
                     {:account_holder_id      (java.util.UUID/randomUUID)
                      :first_name             "Rachel"
                      :last_name              "Rocketpack"
                      :email_address          "rach@rocketpack.org"
                      :residential_address    "1 Ultimate Question Lane, Altar IV"
                      :social_security_number "BB104312D"})

  )
