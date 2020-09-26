;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Schema definition for relational databases
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(ns practicalli.data.schema
  (:require [next.jdbc :as jdbc]))


;; Database tables
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def schema-customer
  ["create table if not exists public.customer (

     id                     uuid         default random_uuid() not null,
     legal_name             varchar(32)  not null,
     email_address          varchar(32)  not null,
     residential_address    varchar(255) not null,
     social_security_number varchar(32)  not null,
     preferred_name         varchar(32),

     constraint customer_pk primary key (id))"])


(def schema-account
  ["create table if not exists public.account (

     number          integer not null identity,
     sort_code       varchar(6) not null,
     name            varchar(32) not null,
     balance         varchar(255) not null,
     balance_updated date not null,
     customer_id     varchar(100) not null,

     constraint account_pk primary key (number))"] )


(def schema-transaction
  ["create table if not exists public.transaction (

     id             uuid default random_uuid() not null,
     date           date not null,
     account_to     integer not null,
     account_from   integer,
     reference      varchar(32),

     constraint transaction_pk primary key (id))"])




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
  "Show the columns and types that define the table schema
   Arguments:
  - table-schemas: a vector of sql statements, each creating a table
  - data-spec: next.jdbc database specification"
  [db-spec table-name]
  (with-open [connection (jdbc/get-connection db-spec)]
    (jdbc/execute! connection [(str "show columns from " table-name)])))


(defn drop-table
  "Drop table in database if it exists
  Arguments:
  - next.jdbc database specification
  - string of table name to drop from database
  Returns:
  - next.jdbc hash-map of result, 1=table dropped, 0=table does not exist"
  [db-spec table-name]
  (with-open [connection (jdbc/get-connection db-spec)]
    (jdbc/execute! connection [(str "drop table if exists " table-name)])))



(comment ;; Managing Schema - development

  ;; Required database specifications before calling helper functions
  (require '[practicalli.data.connection
             :refer [db-spec-dev db-spec-staging db-spec]])

  ;; Create all tables in the development database
  (create-tables! [schema-customer schema-account schema-transaction]
                  db-spec-dev)


  ;; View application table schema in development database
  (show-schema db-spec-dev "public.customer")
  (show-schema db-spec-dev "public.account")
  (show-schema db-spec-dev "public.transaction")

  ;; View database system schema in development database
  (show-schema db-spec-dev "information_schema.tables")

  ;; Remove tables from the development database
  (drop-table db-spec-dev "public.customer")
  (drop-table db-spec-dev "public.account")
  (drop-table db-spec-dev "public.transaction")

  ) ;; End of rich comment block


(comment ;; Managing Schema - staging

  ;; Create all tables in the development database
  (create-tables! [schema-customer schema-account schema-transaction]
                  db-spec-staging)

  ;; View application table schema in development database
  (show-schema db-spec-staging "public.customer")
  (show-schema db-spec-staging "public.account")
  (show-schema db-spec-staging "public.transaction")

  ;; View database system schema in development database
  (show-schema db-spec-staging "information_schema.tables")

  ;; Remove tables from the development database
  (drop-table db-spec-staging "public.customer")
  (drop-table db-spec-staging "public.account")
  (drop-table db-spec-staging "public.transaction")

  ) ;; End of rich comment block


(comment ;; Managing Schema - production

  ;; Create all tables in the development database
  (create-tables! [schema-customer schema-account schema-transaction]
                  db-spec)

  ;; View application table schema in development database
  (show-schema db-spec "public.customer")
  (show-schema db-spec "public.account")
  (show-schema db-spec "public.transaction")

  ;; View database system schema in development database
  (show-schema db-spec "information_schema.tables")

  ;; Remove tables from the development database
  (drop-table db-spec "public.customer")
  (drop-table db-spec "public.account")
  (drop-table db-spec "public.transaction")

  ) ;; End of rich comment block
