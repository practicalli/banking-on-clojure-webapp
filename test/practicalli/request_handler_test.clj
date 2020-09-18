(ns practicalli.request-handler-test
  (:require
   ;; Unit testing
   [clojure.test :refer [deftest is testing]]
   [ring.mock.request :as mock]

   ;; Clojure Spec and Generative testing
   [clojure.spec.alpha :as spec]
   [clojure.spec.gen.alpha :as spec-gen]
   [clojure.spec.test.alpha :as spec-test]

   ;; System under test
   [practicalli.request-handler :as SUT]))


(deftest welcome-page-test
  (testing "Testing elements on the welcome page"
    (is (= 200
           (:status (SUT/welcome-page (mock/request :get "/")))))))

(deftest accounts-overview-page-test
  (testing "Testing elements on the accounts overview page"
    (is (= 200
           (:status (SUT/accounts-overview-page (mock/request :get "/accounts")))))))

(deftest account-history-test
  (testing "Testing elements on the account history page"
    (is (= 200
           (:status (SUT/account-history (mock/request :get "/account")))))))

(deftest money-transfer-test
  (testing "Testing elements on the money transfer page"
    (is (= 200
           (:status (SUT/money-transfer (mock/request :get "/money-transfer")))))))

(deftest money-payment-test
  (testing "Testing elements on the money payment page"
    (is (= 200
           (:status (SUT/money-payment(mock/request :get "/money-payment")))))))


(deftest register-account-holder-test
  (testing "Testing elements on the register account holder page"
    (is (= 200
           (:status (SUT/account-history (mock/request :get "/register-account-holder")))))
    #_(is (valid? ))))
