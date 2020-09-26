(ns practicalli.data.access
  (:require
   [next.jdbc       :as jdbc]
   [next.jdbc.sql   :as jdbc-sql]))


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
