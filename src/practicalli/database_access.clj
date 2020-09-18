(ns practicalli.database-access
  (:require [next.jdbc :as jdbc]
            [next.jdbc.sql :as jdbc-sql]
            [next.jdbc.specs :as jdbc-spec]))


;; Database specification and connection
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Development environment
;; H2 in-memory database
(def db-specification-dev {:dbtype "h2" :dbname "banking-on-clojure"})


;; Database schema
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; define the schema to create each table
;; constraints used to define explicitly named primary keys to aid debugging and maintenance
;; Using meaningful constraint names as they will appear in error messages and make issues easier to trace.
;; Also helps with the maintenance of the database overall.


(def schema-account-holders-table
  ["CREATE TABLE PUBLIC.ACCOUNT_HOLDERS(
     ACCOUNT_HOLDER_ID UUID DEFAULT RANDOM_UUID() NOT NULL,
     FIRST_NAME VARCHAR(32),
     LAST_NAME VARCHAR(32),
     EMAIL_ADDRESS VARCHAR(32) NOT NULL,
     RESIDENTIAL_ADDRESS VARCHAR(255),
     SOCIAL_SECURITY_NUMBER VARCHAR(32),
     CONSTRAINT ACCOUNT_HOLDERS_PK PRIMARY KEY (ACCOUNT_HOLDER_ID))"])

(def schema-accounts-table
  ["CREATE TABLE PUBLIC.ACCOUNTS(
     ACCOUNT_NUMBER INTEGER NOT NULL IDENTITY,
     ACCOUNT_SORT_CODE VARCHAR(6),
     ACCOUNT_NAME VARCHAR(32),
     CURRENT_BALANCE VARCHAR(255),
     LAST_UPDATED DATE,
     ACCOUNT_HOLDER_ID VARCHAR(100) NOT NULL,
     CONSTRAINT ACCOUNTS_PK PRIMARY KEY (ACCOUNT_NUMBER))"] )

(def schema-transaction-history-table
  ["CREATE TABLE PUBLIC.TRANSACTION_HISTORY(
     TRANSACTION_ID UUID DEFAULT RANDOM_UUID() NOT NULL,
     TRANSACTION_REFERENCE VARCHAR(32),
     TRANSACTION_DATE DATE,
     ACCOUNT_NUMBER INTEGER,
     CONSTRAINT TRANSACTION_HISTORY_PK PRIMARY KEY (TRANSACTION_ID))"])


;; Database schema - helper functions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; define a function to open a database connect, create all table schema as a transaction and then close the connection.
;; TODO: remove `with-open` when using a connection pool
;; pass pool connection to `jdbc/with-transaction`


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

;; could also use `clojure.core/run!` instead of `doseq`


(defn show-schema
  [db-spec table-name]
  (with-open [connection (jdbc/get-connection db-spec)]
    (jdbc/execute! connection [(str "SHOW COLUMNS FROM " table-name)])))


(defn drop-table
  [db-spec table-name]
  (with-open [connection (jdbc/get-connection db-spec)]
    (jdbc/execute! connection [(str "DROP TABLE " table-name)])))


(comment  ;; Managing Schemas

  (create-table schema-transaction-history-table
                db-specification-dev)

  ;; Create all tables in the development database
  (create-tables! [schema-account-holders-table schema-accounts-table schema-transaction-history-table]
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

  )


;; Create, Read, Update, Delete
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


;; Inserting multiple records

(defn create-records
  "Insert a single record into the database using a managed connection.
  Arguments:
  - table - name of database table to be affected
  - columns - vector of column names for which values are provided
  - record-data - Clojure vector representing a new records
  - db-spec - database specification to establish a connection"
  [db-spec table columns record-data]
  (with-open [connection (jdbc/get-connection db-spec)]
    (jdbc-sql/insert-multi!
      connection
      table
      columns
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


(defn update-record
  "Insert a single record into the database using a managed connection.
  Arguments:
  - table - name of database table to be affected
  - record-data - Clojure data representing a new record
  - db-spec - database specification to establish a connection
  - where-clause - column and value to identify a record to update"
  [db-spec table record-data where-clause]
  (with-open [connection (jdbc/get-connection db-spec)]
    (jdbc-sql/update!
      connection
      table
      record-data
      where-clause jdbc/snake-kebab-opts)))


(defn delete-record
  "Insert a single record into the database using a managed connection.
  Arguments:
  - table - name of database table to be affected
  - record-data - Clojure data representing a new record
  - db-spec - database specification to establish a connection"
  [db-spec table where-clause]
  (with-open [connection (jdbc/get-connection db-spec)]
    (jdbc-sql/delete! connection table where-clause)))



;; Business Logic
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn new-account-holder
  [customer-details]
  (create-record
    db-specification-dev
    :public.account_holders
    customer-details))


(comment

  (new-account-holder
    #:practicalli.specification-banking{:first_name             "Rachel"
                                        :last_name              "Requests"
                                        :email_address          "rach@requests.org"
                                        :residential_address    "1 Emotive Drive, Altar IV"
                                        :social_security_number "AB140123D"})
;; => #:account-holders{:account-holder-id #uuid "a7e7c9a3-b007-424f-8702-1c8908a8d8ba"}
  )


(comment

  ;; Create account holder
  (create-record db-specification-dev
                 "public.account_holders"
                 {:account_holder_id      (java.util.UUID/randomUUID)
                  :first_name             "Rachel"
                  :last_name              "Rocketpack"
                  :email_address          "rach@rocketpack.org"
                  :residential_address    "1 Ultimate Question Lane, Altar IV"
                  :social_security_number "BB104312D"})

  (read-record db-specification-dev ["select * from public.account_holders"])
  (read-record db-specification-dev ["select * from public.account_holders where first_name = ?" "Rachel"])
  (read-record db-specification-dev ["select * from public.account_holders where account_holder_id = ?" "f6d6c3ba-c5cc-49de-8c85-21904f8c5b4d"])

  (update-record db-specification-dev
                 :public.account_holders
                 {:email_address "rachel+update2@rockketpack.org"}
                 {:account_holder_id "f6d6c3ba-c5cc-49de-8c85-21904f8c5b4d"})
  ;; => #:next.jdbc{:update-count 1}

  (delete-record db-specification-dev :public.account_holders {:account_holder_id "f6d6c3ba-c5cc-49de-8c85-21904f8c5b4d"})
  ;; => #:next.jdbc{:update-count 1}

  (read-record db-specification-dev ["select * from public.account_holders where account_holder_id = ?" "f6d6c3ba-c5cc-49de-8c85-21904f8c5b4d"])
  ;; => [#:ACCOUNT_HOLDERS{:ACCOUNT_HOLDER_ID #uuid "f6d6c3ba-c5cc-49de-8c85-21904f8c5b4d", :FIRST_NAME "Rachel", :LAST_NAME "Rocketpack", :EMAIL_ADDRESS "rachel+update@rockketpack.org", :RESIDENTIAL_ADDRESS "1 Ultimate Question Lane, Altar IV", :SOCIAL_SECURITY_NUMBER "BB104312D"}]


  ;; Create account
  (create-record db-specification-dev
                 "public.accounts"
                 {:account_id        (java.util.UUID/randomUUID)
                  :account_number    "1234567890"
                  :account_sort_code "102010"
                  :account_name      "Current"
                  :current_balance   100
                  :last_updated      "2020-09-11"
                  :account_holder_id (java.util.UUID/randomUUID)})


  (read-record db-specification-dev ["select * from public.accounts where account_number = ?" "1234567890"])

  ;; Create transaction
  (create-record db-specification-dev
                 "public.transaction_history"
                 {:transaction_id        (java.util.UUID/randomUUID)
                  :transaction_reference "Salary"
                  :transaction_date      "2020-09-11"
                  :account_number        "1234567890"})

  (read-record db-specification-dev ["select * from public.transaction_history"])
  (read-record db-specification-dev ["select * from public.transaction_history where transaction_reference = ?" "Salary"])
  ;; => [#:TRANSACTION_HISTORY{:TRANSACTION_ID #uuid "8ac89cfc-6874-4ebe-9ee4-59b8c5e971ff", :TRANSACTION_REFERENCE "Salary", :TRANSACTION_DATE #inst "2020-09-10T23:00:00.000-00:00", :ACCOUNT_NUMBER 1234567890}]
  (read-record db-specification-dev ["select * from public.transaction_history where transaction_date = ?" "2020-09-11"])



  ;; Mock data from specifications
  ;; TODO:
  ;; - require banking specifications namespace
  ;; - call mock-data-* functions  mock-data-customer-details
  ;; (practicalli.specifications-banking/mock-data-customer-details)


  )


;; Instrument specifications
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Require the `next.jdbc.specs` library as jdbc-spec

(comment
  ;; Instrument all next.jdbc functions
  (jdbc-spec/instrument)

  ;; Remove instrumentation from all next.jdbc functions
  (jdbc-spec/unstrument)

  )
