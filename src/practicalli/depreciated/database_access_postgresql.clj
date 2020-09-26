;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; DEPRECATED
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(ns practicalli.deprecated.database-access-postgresql
  (:require [next.jdbc :as jdbc]))


;; Staging environment
;; Postgresql in-memory database

#_(def data-source-jdbc-postgresql
    (jdbc/get-datasource (System/getenv "JDBC_DATABASE_URL")) )


;; Refactor schema helper functions to be pure

;; Drop this function and use show-schema instead
;; - simplifies code too

#_(defn information-tables
    [data-source]
    (jdbc/execute!
      data-source
      ["select * from information_schema.tables"]))


#_(defn show-schema
    [data-source table-name]
    (jdbc/execute!
      data-source
      [(str "show columns from " table-name)]))


#_(defn drop-table
    [data-source table-name]
    (jdbc/execute!
      data-source
      [(str "drop table " table-name)]))

(comment  ;; Using Schema helpers
  ;; View application table schema
  (show-schema data-source-jdbc-postgresql "account_holders")
  (show-schema data-source-jdbc-postgresql "accounts")
  (show-schema data-source-jdbc-postgresql "account_history")

  (show-schema data-source-jdbc-postgresql "information_schema.tables")
  )


(comment  ;; Design journal

  ;; ;; TODO: Check the DATABASE_URL works with next.jdbc
  ;; ;; Not working
  (System/getenv "JDBC_DATABASE_CONNECTION")
  (System/getenv "JDBC_DATABASE_USERNAME")
  (System/getenv "JDBC_DATABASE_PASSWORD")


  (def db-specification-postgresql
    {:jdbcUrl  (System/getenv "JDBC_DATABASE_URL")
     :user     (System/getenv "JDBC_DATABASE_USERNAME")
     :password (System/getenv "JDBC_DATABASE_PASSWORD")})

  (def data-source-jdbc-postgresql
    (jdbc/get-datasource (System/getenv "JDBC_DATABASE_URL")) )

  #_(def data-source-postgresql
      (jdbc/get-datasource db-specification-postgresql))


  ;; Using Heroku CLI tool
  ;; Run an instance of the app (container) and send the command `echo $JDBC_DATABASE_URL`
  ;; heroku run echo \$JDBC_DATABASE_URL --app banking-on-clojure-staging

  ;; This returns the correct JDBC connection string
  ;; jdbc:postgresql://<hostname>:port/<database-name>?user=<username>&password=<password>&sslmode=require

  ;; This jdbc connection string is generated from the DATABASE_URL config var that is added to the heroku app when a database is provisioned.

  ;; (def data-source-postgres (jdbc/get-datasource (str "jdbc:" (System/getenv "JDBC_DATABASE_URL"))))


  #_(def db-specification-postgres-manual
      {:dbtype "h2" :dbname "banking-on-clojure"})


  ;; (information-tables data-source-postgres)



  ;; (def db-specification-postgresql
  ;;   {:jdbcUrl  (System/getenv "JDBC_DATABASE_URL")
  ;;    :user     (System/getenv "JDBC_DATABASE_USERNAME")
  ;;    :password (System/getenv "JDBC_DATABASE_PASSWORD")})


  ;; Database connection
  ;; (def data-source (jdbc/get-datasource db-specification))

  ;; Live environment
  )
