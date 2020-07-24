(ns practicalli.specifications-banking
  (:require [clojure.spec.alpha :as spec]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test]

            [clojure.string]

            [practicalli.specifications]
            [practicalli.database-access :as SUT]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Specifications for banking on clojure application
;;
;; Author: practicalli
;;
;; Description:
;; Data and function specifications using clojure.spec.alpha
;; and instrumentation helper functions
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;; Banking data specifications
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(spec/def ::first-name string?)
(spec/def ::last-name string?)
(spec/def ::email-address string?)

;; residential address values
(spec/def ::house-name-number (spec/or :string string?
                                       :number int?))
(spec/def ::street-name string?)
(spec/def ::post-code string?)
(spec/def ::county string?)

;; countries of the world as a set,
;; containing a string for each country
;; defined in the practicalli.specifications namespace
(spec/def ::country :practicalli.specifications/countries-of-the-world)

(spec/def ::residential-address (spec/keys :req [::house-name-number ::street-name ::post-code]
                                           :opt [::county ::country]))

(spec/def ::social-security-id-uk string?)
(spec/def ::social-security-id-usa string?)

(spec/def ::social-security-id (spec/or ::social-security-id-uk
                                        ::social-security-id-usa))

;; composite customer details specification
(spec/def ::customer-details
  (spec/keys
    :req [::first-name ::last-name ::email-address ::residential-address ::social-security-id]))


;; Account holder values
(spec/def ::account-id uuid?)

;; Account holder - composite specification
(spec/def ::account-holder
  (spec/keys
    :req [::account-id
          ::first-name
          ::last-name
          ::email-address
          ::residential-address
          ::social-security-id]))


;; Generating data from specifications
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Test data specifications by generating sample data from those specifications.
;; Identifies specifications that may require custom generators.
;; If individual specifications do not generate consistent data,
;; then incorrect results may occur during function specification checking.

(comment

  (spec-gen/sample (spec/gen ::first-name))
  (spec-gen/sample (spec/gen ::last-name))
  (spec-gen/sample (spec/gen ::email-address))
  (spec-gen/sample (spec/gen ::house-name-number))
  (spec-gen/sample (spec/gen ::street-name))
  (spec-gen/sample (spec/gen ::post-code))
  (spec-gen/sample (spec/gen ::county))
  (spec-gen/sample (spec/gen ::country))
  (spec-gen/sample (spec/gen ::residential-address))
  (spec-gen/sample (spec/gen ::social-security-id-uk))
  (spec-gen/sample (spec/gen ::social-security-id-usa))
  (spec-gen/sample (spec/gen ::social-security-id))
  (spec-gen/sample (spec/gen ::customer-details))
  (spec-gen/sample (spec/gen ::account-holder))

  )



;; Banking function specifications
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(spec/fdef SUT/register-account-holder
  :args (spec/cat :customer ::customer-details)
  :ret ::account-holder)


;; Banking function instrumentation
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(spec-test/instrument `SUT/register-account-holder)


;; test the instrumentation of the function
;; use bad data, should return spec error
(comment

  (SUT/register-account-holder {})

  ;; Use specs to generate test data, should evaluate correctly
  (SUT/register-account-holder
    (spec-gen/generate
      (spec/gen ::customer-details)))


  (spec-test/unstrument `SUT/register-account-holder)

  )

;; spec check
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Generative testing from specifications
;; Generate 1000 data points from the arguments to a function spec
;; Use that data to check the evaluation result against the return specification

(comment

  (spec-test/check `SUT/register-account-holder)

  )
