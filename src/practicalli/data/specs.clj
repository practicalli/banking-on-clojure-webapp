(ns practicalli.data.specs
  (:require
   [clojure.spec.alpha     :as spec]
   [clojure.spec.gen.alpha :as spec-gen]))



;; Customer detail specifications

(spec/def :customer/id uuid?)
(spec/def :customer/legal-name string?)
(spec/def :customer/email-address string?)
(spec/def :customer/residential-address string?)
(spec/def :customer/social-security-number string?)
(spec/def :customer/preferred-name string?)

;; Data to send to the database
(spec/def :customer/unregistered
  (spec/keys
    :req [:customer/legal-name
          :customer/email-address
          :customer/residential-address
          :customer/social-security-number]
    :opt [:customer/preferred-name]))


;; Data received from the database
(spec/def :customer/registered
  (spec/keys
    :req [:customer/id
          :customer/legal-name
          :customer/email-address
          :customer/residential-address
          :customer/social-security-number]
    :opt [:customer/preferred-name]))





;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  (spec-gen/generate (spec/gen :customer/unregistered))
  ;; => #:customer{:legal-name "7hl4PT89AO3Pe04958YBWxWH0m6tnG", :email-address "iz83P60EtVM9lMX6zg6", :residential-address "FJ7Mh6nNJviX", :social-security-number "9bYAS85axW42KnOPcPjMtkg06qb4Tr"}
  (spec-gen/generate (spec/gen :customer/registered))
  ;; => #:customer{:preferred-name "S8i45tGAgaO60uPVW6q48Emg1", :legal-name "FFsv7pCavtC5V9qD52wO91i9Y", :email-address "6Wl3O11i3L66q800f3JcgkQ7414V0", :residential-address "vzl93YDnD74Zh5", :social-security-number "120J"}


  (spec-gen/generate (spec/gen :customer/id))

  (spec/valid? :customer/id
               (spec-gen/generate (spec/gen :customer/id)))

  (spec/valid? :customer/id
               #:customer{:id #uuid "323ecc53-d676-4b7a-bea0-a4b3ca075c2c"})
  ;; => false

  (spec/valid? :customer/id
               (:id #:customer{:id #uuid "323ecc53-d676-4b7a-bea0-a4b3ca075c2c"}))


  ) ;; End of rich comment block



;; I am using the following schema with a H2 database, which generates a uuid.
;; (def schema-customer
;;   ["create table if not exists public.customer (

;;      id                     uuid         default random_uuid() not null,
;;      legal_name             varchar(32)  not null,
;;      email_address          varchar(32)  not null,
;;      residential_address    varchar(255) not null,
;;      social_security_number varchar(32)  not null,
;;      preferred_name         varchar(32),

;;      constraint customer_pk primary key (id))"])
;; This uuid value is returned from a call to next.jdbc.sql/insert! which used the following spec to generate the new record.
;; (spec/def :customer/unregistered
;;   (spec/keys
;;     :req [:customer/legal-name
;;           :customer/email-address
;;           :customer/residential-address
;;           :customer/social-security-number]
;;     :opt [:customer/preferred-name]))
;; The update! function returns values in the form: #:customer{:id #uuid "323ecc53-d676-4b7a-bea0-a4b3ca075c2c"}
;; I check the result from insert! against the :customer/id specification:
;; (spec/def :customer/id uuid?)
;; Doing so in a unit test, that test is failing suggesting the returned value from insert! is not conforming to the clojure.spec specification.
;; expected: (spec/valid?
;;             :customer/id
;;             (SUT/new-customer
;;               (spec-gen/generate (spec/gen :customer/unregistered))))

;; actual: (not
;;           (spec/valid?
;;             :customer/id
;;             #:customer{:id #uuid "323ecc53-d676-4b7a-bea0-a4b3ca075c2c"}))
;; Not sure where I have gone wrong here... any suggestions?
