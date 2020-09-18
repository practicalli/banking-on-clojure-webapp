(ns practicalli.specifications-banking
  (:require
   ;; Clojure Specifications
   [clojure.spec.alpha :as spec]
   [clojure.spec.gen.alpha :as spec-gen]
   [clojure.spec.test.alpha :as spec-test]
   [practicalli.specifications]

   ;; Helper namespaces
   [clojure.string]))


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

#_(spec/def ::residential-address (spec/keys :req [::house-name-number ::street-name ::post-code]
                                             :opt [::county ::country]))

;; Create a simpler specification for residential address
(spec/def ::residential-address string?)


(spec/def ::social-security-id-uk string?)
(spec/def ::social-security-id-usa string?)

#_(spec/def ::social-security-id (spec/or ::social-security-id-uk
                                          ::social-security-id-usa))

;; Create a simpler specification for social security number
(spec/def ::social-security-number string?)

;; composite customer details specification
(spec/def ::customer-details
  (spec/keys
    :req [::first-name ::last-name ::email-address ::residential-address ::social-security-number]))


;; Account holder values
(spec/def ::account-holder-id uuid?)

;; Account holder - composite specification
(spec/def ::account-holder
  (spec/keys
    :req [::account-holder-id
          ::first-name
          ::last-name
          ::email-address
          ::residential-address
          ::social-security-number]))


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


  (spec-gen/generate (spec/gen ::customer-details))

  )





;; Mock data generators
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Functions to generate mock data from specifications

(defn mock-data-customer-details
  []
  (spec-gen/generate (spec/gen ::customer-details)))

(defn mock-data-account-holder
  []
  (spec-gen/generate (spec/gen ::account-holder)))
