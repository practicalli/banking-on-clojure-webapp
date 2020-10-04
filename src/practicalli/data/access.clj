(ns practicalli.data.access
  (:require
   [next.jdbc       :as jdbc]
   [next.jdbc.sql   :as jdbc-sql]

   [practicalli.data.schema :as schema]))


;; Helper functions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn ^:private execute-transaction
  "Execute SQL statements within a transaction, for a given database source"
  [sql-statements data-spec]
  (with-open [connection (jdbc/get-connection data-spec)]
    (jdbc/with-transaction [transaction connection]
      (doseq [sql-statement sql-statements]
        (jdbc/execute-one! transaction sql-statement)))))


;; Schema management
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn create-tables
  "Delete all database tables"
  [db-spec]
  (execute-transaction
    [schema/customer-drop schema/account-drop schema/transaction-drop] db-spec)
  (execute-transaction
    [schema/customer-create schema/account-create schema/transaction-create] db-spec))

(defn delete-tables
  "Reset all database tables"
  [db-spec]
  (execute-transaction
    [schema/customer-drop schema/account-drop schema/transaction-drop] db-spec))

(defn show-table
  [db-spec sql-statement]
  (with-open [connection (jdbc/get-connection db-spec)]
    (jdbc/execute! connection sql-statement)))


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
      where-clause
      jdbc/snake-kebab-opts)))


(defn delete-record
  "Insert a single record into the database using a managed connection.
  Arguments:
  - table - name of database table to be affected
  - record-data - Clojure data representing a new record
  - db-spec - database specification to establish a connection"
  [db-spec table where-clause]
  (with-open [connection (jdbc/get-connection db-spec)]
    (jdbc-sql/delete! connection table where-clause)))





;; Instrument next.jdbc specifications
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(comment

  ;; Require the next.jdbc.specs namespace before instrumenting functions
  (require '[next.jdbc.specs :as jdbc-spec])

  ;; Instrument all next.jdbc functions
  (jdbc-spec/instrument)

  ;; Remove instrumentation from all next.jdbc functions
  (jdbc-spec/unstrument)

  )
