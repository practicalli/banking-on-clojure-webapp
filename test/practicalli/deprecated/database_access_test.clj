;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; DEPRECATED
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(ns practicalli.deprecated.database-access-test
  (:require
   ;; Unit testing
   [clojure.test :refer [deftest is testing]]

   ;; Clojure Specifications
   [clojure.spec.alpha :as spec]
   [clojure.spec.test.alpha :as spec-test]
   [clojure.spec.gen.alpha :as spec-gen]
   [practicalli.specifications-banking]

   ;; System under test
   [practicalli.database-access :as SUT])
  )

;;
#_(deftest new-account-holder-test
    (testing "Registered account holder is valid specification"
      (is (spec/valid? :practicalli.specifications-banking/account-holder-id
                       (SUT/new-account-holder
                         (spec-gen/generate
                           (spec/gen :practicalli.specifications-banking/customer-details)))))))


#_(deftest new-account-holder-test
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


  (SUT/create-record SUT/db-specification-dev
                     :public.account_holders
                     {:account_holder_id      (java.util.UUID/randomUUID)
                      :first_name             "Rachel"
                      :last_name              "Rocketpack"
                      :email_address          "rach@rocketpack.org"
                      :residential_address    "1 Ultimate Question Lane, Altar IV"
                      :social_security_number "BB104312D"})


  (SUT/create-record
    SUT/db-specification-dev
    :public.account_holders
    (practicalli.specifications-banking/mock-data-account-holder))


  (SUT/new-account-holder (practicalli.specifications-banking/mock-data-account-holder))

  )


;; Data access function specification
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; qualified namespaces for specifications and databases are different,
;; causing function specification to fail

#_(spec/fdef practicalli.database-access/new-account-holder
    :args (spec/cat :customer :practicalli.specifications-banking/customer-details)
    :ret :practicalli.specifications-banking/account-holder-id)


;; Spec generating Names too long for database

#_(spec/fdef practicalli.database-access/new-account-holder
    :args (spec/cat :customer :practicalli.specifications-banking/customer-details)
    :ret map?)


;; Data access function instrumentation
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; test the instrumentation of the function
;; use bad data, should return spec error
(comment

  (spec-test/instrument `SUT/new-account-holder)

  (spec-test/unstrument `SUT/new-account-holder)

  )


;; spec check
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Generative testing from specifications
;; Generate 1000 data points from the arguments to a function spec
;; Use that data to check the evaluation result against the return specification

(comment

  ;; Failing as next.jdbc returns account-holder as the qualified namespace
  (spec-test/check `SUT/new-account-holder)

  )
