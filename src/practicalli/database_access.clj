(ns practicalli.database-access
  (:require [next.jdbc :as jdbc]))


;; Database specification and connection
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Development environment
;; H2 in-memory database
(def db-specification-dev {:dbtype "h2" :dbname "banking-on-clojure"})


;; Database schema
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; define the schema to create tables
;; define a function to open a database connect, create all table schema as a transaction and then close the connection.


(def schema-account-holders-table
  ["CREATE TABLE PUBLIC.ACCOUNT_HOLDERS(
     ACCOUNT_HOLDER_ID UUID(16) NOT NULL,
     FIRST_NAME VARCHAR(32),
     LAST_NAME VARCHAR(32),
     EMAIL_ADDRESS VARCHAR(32),
     RESIDENTIAL_ADDRESS VARCHAR(255),
     SOCIAL_SECURITY_NUMBER VARCHAR(32),
     CONSTRAINT ACCOUNT_HOLDERS_PK PRIMARY KEY (ACCOUNT_HOLDER_ID))"])

(def schema-accounts-table
  ["CREATE TABLE PUBLIC.ACCOUNTS(
     ACCOUNT_ID UUID(16) NOT NULL,
     ACCOUNT_NUMBER INTEGER NOT NULL AUTO_INCREMENT,
     ACCOUNT_SORT_CODE VARCHAR(6),
     ACCOUNT_NAME VARCHAR(32),
     CURRENT_BALANCE VARCHAR(255),
     LAST_UPDATED DATE,
     ACCOUNT_HOLDER_ID VARCHAR(100) NOT NULL,
     CONSTRAINT ACCOUNTS_PK PRIMARY KEY (ACCOUNT_ID))"] )

(def schema-transaction-history-table
  ["CREATE TABLE PUBLIC.TRANSACTION_HISTORY(
     TRANSACTION_ID UUID(16) NOT NULL,
     TRANSACTION_REFERENCE VARCHAR(32),
     TRANSACTION_DATE DATE,
     ACCOUNT_NUMBER INTEGER,
     CONSTRAINT TRANSACTION_HISTORY_PK PRIMARY KEY (TRANSACTION_ID))"])


;; Using meaningful constraint names as they will appear in error messages and make issues easier to trace.
;; Also helps with the maintenance of the database overall.



;; Database schema - helper functions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TODO: remove `with-open` when using a connection pool
;; pass pool connection to `jdbc/with-transaction`


(defn create-tables
  "Establish a connection to the data source and create all tables within a transaction.
  Close the database connection.
  Arguments:
  - table-schemas: a vector of sql statements, each creating a table"
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
  (with-open [connection (jdbc/get-datasource db-spec)]
    (jdbc/execute! connection [(str "DROP TABLE " table-name)])))


(comment  ;; Managing Schemas

  (create-table schema-transaction-history-table
                db-specification-dev)

  ;; Create all tables in the development database
  (create-tables [schema-account-holders-table schema-accounts-table schema-transaction-history-table]
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



;; REPL Driven Development
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(comment

  ;; Create a table called contacts
  ;; Creates .mv.db file if not already present

  (jdbc/execute!
    data-source
    ["create table account-holders(
     id int auto_increment primary key,
     name varchar(32),
     email varchar(255))"])

  ;;=> [#:next.jdbc{:update-count 0}]


  ;; Add a contact to the database

  (jdbc/execute!
    data-source
    ["insert into contacts(name,email)
    values('Jenny Jetpack','jenny@jetpack.org')"])
  ;; => [#:next.jdbc{:update-count 1}]


  ;; Select all contacts from the database

  (jdbc/execute!
    data-source
    ["select * from contacts"])
  ;; => [#:CONTACTS{:ID 1, :NAME "Jenny Jetpack", :EMAIL "jenny@jetpack.org"}]


  ;; Delete all contacts by deleting the table
  (jdbc/execute!
    data-source
    ["drop table contacts"])

  )
