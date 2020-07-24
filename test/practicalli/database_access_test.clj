(ns practicalli.database-access-test
  (:require [clojure.spec.alpha :as spec]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.test :refer [deftest is testing]]
            [practicalli.database-access :as SUT]))

(deftest register-account-holder-test
  (testing "Registered account holder is valid specification"
    (is (spec/valid? :practicalli.specifications-banking/account-holder
                     (SUT/register-account-holder
                       (spec-gen/generate
                         (spec/gen :practicalli.specifications-banking/customer-details))))))
  )
