(ns practicalli.handler-helpers-test
  (:require
   ;; Unit testing
   [clojure.test :refer [deftest is testing use-fixtures]]

   ;; Specifications and Generative testing
   [clojure.spec.alpha :as spec]
   [clojure.spec.gen.alpha :as spec-gen]
   [practicalli.data.specs]

   ;; System under test
   [practicalli.hanlder-helpers :as SUT]))

;; TODO fixtures to setup database...
;; once: create / clean the database by dropping tables then creating tables
;; each: where we are using pre-existing data - drop-create-insert ??

(defn database-reset-fixture
  "Setup: drop all tables, creates new tables
   Teardown: drop all tables
  SQL schema code has if clauses to avoid errors running SQL code.
  Arguments:
  test-function - a function to run a specific test"
  [test-function]
  (SUT/create-database)
  (test-function)
  (SUT/delete-database))

(use-fixtures :each database-reset-fixture)


;; DONE: add test selector

(deftest ^:database new-customer-test
  (testing "New customer generative testing"
    (is (spec/valid?
          :customer/id
          (:customer/id (SUT/new-customer
                          (spec-gen/generate (spec/gen :customer/unregistered))))))))


(deftest ^:kaocha/skip-test new-transaction-test
  (testing "New customer generative testing"
    (is (spec/valid?
          :customer/id
          (:customer/id (SUT/new-customer
                          (spec-gen/generate (spec/gen :customer/unregistered))))))))



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
