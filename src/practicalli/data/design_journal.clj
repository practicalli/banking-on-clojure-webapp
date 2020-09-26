;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Design Journal
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(ns practicalli.data.design-journal
  (:require [clojure.spec.alpha :as spec]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as jdbc-sql]
            [next.jdbc.specs :as jdbc-spec]))

;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; Database specification
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  ;; Create a new database to experiment with
  (def db-specification-dev {:dbtype "h2" :dbname "banking-redux"})


  ;; Schema definition
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  (def schema-account-holders-table
    ["CREATE TABLE IF NOT EXISTS PUBLIC.ACCOUNT_HOLDERS(
     ID UUID DEFAULT RANDOM_UUID() NOT NULL,
     LEGAL_NAME VARCHAR(32) NOT NULL,
     EMAIL_ADDRESS VARCHAR(32) NOT NULL,
     RESIDENTIAL_ADDRESS VARCHAR(255) NOT NULL,
     SOCIAL_SECURITY_NUMBER VARCHAR(32) NOT NULL,
     PREFERRED_NAME VARCHAR(32),
     CONSTRAINT ACCOUNT_HOLDERS_PK PRIMARY KEY (ID))"])

  ;; CRUD Helpers
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  (defn create-record
    "Insert a single record into the database using a managed connection.
  Arguments:
  - table - name of database table to be affected
  - record-data - Clojure data representing a new record
  - db-spec - database specification to establish a connection"
    [db-spec table record-data]
    (with-open [connection (jdbc/get-connection db-spec)]
      (jdbc-sql/insert!
        connection
        table
        record-data
        jdbc/snake-kebab-opts)))

  (defn read-record
    "Insert a single record into the database using a managed connection.
  Arguments:
  - table - name of database table to be affected
  - record-data - Clojure data representing a new record
  - db-spec - database specification to establish a connection"
    [db-spec sql-query]
    (with-open [connection (jdbc/get-connection db-spec)]
      (jdbc-sql/query connection sql-query)))


  ;; Abstraction
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  (defn register-customer
    [customer-details]
    (create-record
      db-specification-dev
      :public.account_holders
      customer-details))

  (defn customer-details
    [customer-id]
    (read-record
      db-specification-dev
      ["select * from public.account_holders where id = ?" customer-id])
    )

  ) ;; End of rich comment block

(comment

  (register-customer
    (spec-gen/generate (spec/gen :customer/unregistered)))

  ;; Register a customer and use the returned uuid to get that customers details
  (customer-details
    (:account-holders/id (register-customer
                           (spec-gen/generate (spec/gen :customer/unregistered)))))
  ;; => [#:ACCOUNT_HOLDERS{:ID #uuid "a752d1eb-42f4-412f-90e7-f8f185f6761b", :LEGAL_NAME "MV0lFDATSZ7zL7Cl8q0", :EMAIL_ADDRESS "o61HRbJU4MQg2l2veGB2U84nR2", :RESIDENTIAL_ADDRESS "EAnhE1NKSX1L06yjIxj271R2", :SOCIAL_SECURITY_NUMBER "VAt0Jg", :PREFERRED_NAME nil}]


  #_(new-account-holder
      (practicalli.specifications-banking/mock-data-customer-details))
  ;; => #:account-holders{:account-holder-id #uuid "036ecad3-138d-4467-b161-56cbcb9730aa"}

  #_(new-account-holder
      #:practicalli.specification-banking{:first_name             "Rachel"
                                          :last_name              "Requests"
                                          :email_address          "rach@requests.org"
                                          :residential_address    "1 Emotive Drive, Altar IV"
                                          :social_security_number "AB140123D"})
  ;; => #:account-holders{:account-holder-id #uuid "a7e7c9a3-b007-424f-8702-1c8908a8d8ba"}
  )


;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment


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
      :req [:customer/legal-name
            :customer/email-address
            :customer/residential-address
            :customer/social-security-number]
      :opt [:customer/preferred-name]))


  ;; This design seems so obvious now, but its had me going round in circles trying to optomise for weeks.


  (spec-gen/generate (spec/gen :customer/unregistered))
  ;; => #:customer{:legal-name "7hl4PT89AO3Pe04958YBWxWH0m6tnG", :email-address "iz83P60EtVM9lMX6zg6", :residential-address "FJ7Mh6nNJviX", :social-security-number "9bYAS85axW42KnOPcPjMtkg06qb4Tr"}
  (spec-gen/generate (spec/gen :customer/registered))
  ;; => #:customer{:preferred-name "S8i45tGAgaO60uPVW6q48Emg1", :legal-name "FFsv7pCavtC5V9qD52wO91i9Y", :email-address "6Wl3O11i3L66q800f3JcgkQ7414V0", :residential-address "vzl93YDnD74Zh5", :social-security-number "120J"}


  ) ;; End of rich comment block



;; Rich comment Block with redefined functions and other vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; Create a new database to experiment with
  (def db-specification-dev {:dbtype "h2" :dbname "banking-redux"})


  ;; initial design
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  (def schema-account-holders-table
    ["CREATE TABLE IF NOT EXISTS PUBLIC.ACCOUNT_HOLDERS(
     ACCOUNT_HOLDER_ID UUID DEFAULT RANDOM_UUID() NOT NULL,
     FIRST_NAME VARCHAR(32),
     LAST_NAME VARCHAR(32),
     EMAIL_ADDRESS VARCHAR(32) NOT NULL,
     RESIDENTIAL_ADDRESS VARCHAR(255),
     SOCIAL_SECURITY_NUMBER VARCHAR(32),
     CONSTRAINT ACCOUNT_HOLDERS_PK PRIMARY KEY (ACCOUNT_HOLDER_ID))"])


  (def schema-account-holders-table
    ["CREATE TABLE IF NOT EXISTS PUBLIC.ACCOUNT_HOLDERS(
     ID UUID DEFAULT RANDOM_UUID() NOT NULL,
     LEGAL_NAME VARCHAR(32) NOT NULL,
     EMAIL_ADDRESS VARCHAR(32) NOT NULL,
     RESIDENTIAL_ADDRESS VARCHAR(255) NOT NULL,
     SOCIAL_SECURITY_NUMBER VARCHAR(32) NOT NULL,
     PREFERRED_NAME VARCHAR(32),
     CONSTRAINT ACCOUNT_HOLDERS_PK PRIMARY KEY (ID))"])

  (defn create-tables!
    "Establish a connection to the data source and create all tables within a transaction.
  Close the database connection.
  Arguments:
  - table-schemas: a vector of sql statements, each creating a table
  - data-spec: next.jdbc database specification"
    [table-schemas data-spec]

    (with-open [connection (jdbc/get-connection data-spec)]
      (jdbc/with-transaction [transaction connection]
        (doseq [sql-statement table-schemas]
          (jdbc/execute! transaction sql-statement) ))))
  (defn show-schema
    [db-spec table-name]
    (with-open [connection (jdbc/get-connection db-spec)]
      (jdbc/execute! connection [(str "SHOW COLUMNS FROM " table-name)])))
  (defn drop-table
    [db-spec table-name]
    (with-open [connection (jdbc/get-connection db-spec)]
      (jdbc/execute! connection [(str "DROP TABLE " table-name)])))

  ;; Create all tables in the development database
  (create-tables! [schema-account-holders-table]
                  db-specification-dev)

  #_(create-tables!
      [schema-account-holders-table
       schema-accounts-table
       schema-transaction-history-table]
      db-specification-dev)

  ;; View application table schema in development database
  (show-schema db-specification-dev "PUBLIC.ACCOUNT_HOLDERS")
  (show-schema db-specification-dev "PUBLIC.ACCOUNTS")
  (show-schema db-specification-dev "PUBLIC.TRANSACTION_HISTORY")

  ;; View database system schema in development database
  (show-schema db-specification-dev "INFORMATION_SCHEMA.TABLES")

  ;; Remove tables from the development database
  (drop-table db-specification-dev "PUBLIC.ACCOUNT_HOLDERS")
  (drop-table db-specification-dev "PUBLIC.ACCOUNTS")
  (drop-table db-specification-dev "PUBLIC.TRANSACTION_HISTORY")



  ;; The table names in the h2 database are relatively simple,
  ;; it does not seem that they can be qualified in the same way clojure.spec keywords are
  ;; so when returning a qualfied result, the table name is used and not the catalog(?)
  ;; The table PUBLIC.ACCOUNT will return a result namespaced :ACCOUNT{,,,}


  ;; => [#:ACCOUNT_HOLDERS{:ACCOUNT_HOLDER_ID #uuid "f6d6c3ba-c5cc-49de-8c85-21904f8c5b4d", :FIRST_NAME "Rachel", :LAST_NAME "Rocketpack", :EMAIL_ADDRESS "rachel+update@rockketpack.org", :RESIDENTIAL_ADDRESS "1 Ultimate Question Lane, Altar IV", :SOCIAL_SECURITY_NUMBER "BB104312D"}]


;; Specifications
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  (spec/def ::id uuid?)
  (spec/def ::legal-name string?)
  (spec/def ::email-address string?)
  (spec/def ::residential-address string?)
  (spec/def ::social-security-number string?)
  (spec/def ::preferred-name string?)

  (spec/def ::account-holder
    (spec/keys
      :req [::id ::legal-name ::email-address ::residential-address ::social-security-number]
      :opt [::preferred-name]))


  (spec-gen/generate (spec/gen ::account-holder))


  (spec/def ::customer
    (spec/keys
      :req [::id ::legal-name ::email-address ::residential-address ::social-security-number]
      :opt [::preferred-name]))


  (spec-gen/generate (spec/gen ::account-holder))
  ;; => #:practicalli.data.relational{:id #uuid "bce879ad-3b1c-4e87-948c-4e559474901d", :legal-name "asGT3s8lJi01z45d9a3lG5V36lh13", :email-address "zoW8L41tIV498f5Pi", :residential-address "w3e0y", :social-security-number "4F34X8"}



  (spec/def :customer/details
    (spec/keys
      :req [::legal-name ::email-address ::residential-address ::social-security-number]
      :opt [::preferred-name]))


  (spec-gen/generate (spec/gen :customer/details))
;; => #:practicalli.data.relational{:id #uuid "64561b61-7f33-4700-80d4-de9c8e4c9ced", :legal-name "gx5DbXIoOA8a06u837", :email-address "2M6691M9886U7sI", :residential-address "6c23GWul3Fk1nl1807H42UEHgUqt6", :social-security-number "DOAiLgjYcSHI"}


  ;; Using autoresolve spec in a test, then define use a specific namespace
  ;; or use an alias that is the same (but aliases cannot all be the same)






  ) ;; End of Rich comment block


;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; Discarded
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  ;; Account holder values
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  (spec/def :account-holder/id uuid?)
  (spec/def :account-holder/legal-name string?)
  (spec/def :account-holder/email-address string?)
  (spec/def :account-holder/residential-address string?)
  (spec/def :account-holder/social-security-number string?)
  (spec/def :account-holder/preferred-name string?)

  (spec/describe :account-holder/id)

  ;; Account holder - composite specification
  (spec/def :account-holder/details
    (spec/keys
      :req [:account-holder/id
            :account-holder/legal-name
            :account-holder/email-address
            :account-holder/residential-address
            :account-holder/social-security-number]
      :opt [:account-holder/preferred-name]))


  (spec-gen/generate (spec/gen :account-holder/details))

  (spec-gen/sample (spec/gen ::account-holder))


  ;; refactored specification
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  (spec/def :customer/legal-name string?)
  (spec/def :customer/email-address string?)
  (spec/def :customer/residential-address string?)
  (spec/def :customer/social-security-number string?)
  (spec/def :customer/preferred-name string?)


  ;; Account holder - composite specification
  (spec/def :customer/details
    (spec/keys
      :req [:customer/legal-name
            :customer/email-address
            :customer/residential-address
            :customer/social-security-number]
      :opt [:customer/preferred-name]))


  (spec-gen/generate (spec/gen :customer/details))
;; => #:customer{:preferred-name "dx3Aui1X8978XO0D", :legal-name "t68kpUHIYDyT6lHljXQqysPO", :email-address "fcK5rJhcO7m", :residential-address "P", :social-security-number "2dDxJe78QrGt3v4S0Tw"}
  ;; => #:customer{:preferred-name "C9i8257Gp05o9a", :legal-name "VTVE7Rd53W2s53", :email-address "i3Muq", :residential-address "X3MQXIkp959q8C62O95swf529Qt5q8", :social-security-number "K4ufLlJh25c"}

  (spec-gen/sample (spec/gen ::customer))


  ;; Why not just give them the same name
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Specific customer details

  (spec/def :customer/id uuid?)
  (spec/def :customer/legal-name string?)
  (spec/def :customer/email-address string?)
  (spec/def :customer/residential-address string?)
  (spec/def :customer/social-security-number string?)
  (spec/def :customer/preferred-name string?)

  ;; Details to send to the database
  (spec/def :customer/unregistered
    (spec/keys
      :req [:customer/legal-name
            :customer/email-address
            :customer/residential-address
            :customer/social-security-number]
      :opt [:customer/preferred-name]))


  ;; Details recieved from the database
  (spec/def :customer/registered
    (spec/keys
      :req [:customer/legal-name
            :customer/email-address
            :customer/residential-address
            :customer/social-security-number]
      :opt [:customer/preferred-name]))


  ;; This seems so obvious now, but its had me going round in circles trying to optomise for weeks.


  (spec-gen/generate (spec/gen :customer/unregistered))
  ;; => #:customer{:legal-name "7hl4PT89AO3Pe04958YBWxWH0m6tnG", :email-address "iz83P60EtVM9lMX6zg6", :residential-address "FJ7Mh6nNJviX", :social-security-number "9bYAS85axW42KnOPcPjMtkg06qb4Tr"}
  (spec-gen/generate (spec/gen :customer/registered))
;; => #:customer{:preferred-name "S8i45tGAgaO60uPVW6q48Emg1", :legal-name "FFsv7pCavtC5V9qD52wO91i9Y", :email-address "6Wl3O11i3L66q800f3JcgkQ7414V0", :residential-address "vzl93YDnD74Zh5", :social-security-number "120J"}


  ) ;; End of rich comment block
