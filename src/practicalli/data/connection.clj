;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Database specifications for relational databases
;;
;; - DONE: next.jdbc database specifications
;; - TODO: connection pool
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(ns practicalli.data.connection)


;; H2 database - embedded relational db
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Evironment: development
(def db-spec-dev
  "H2 development database specification for next.jdbc"
  {:dbtype "h2" :dbname "banking-on-clojure"})


;; Heroku Postgres database - PostgreSQL as a service
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def db-spec-staging
  "PostgreSQL Staging database specification for next.jdbc"
  (System/getenv "JDBC_DATABASE_URL_STAGING"))

(def db-spec
  "PostgreSQL production database specification for next.jdbc"
  (System/getenv "JDBC_DATABASE_URL"))


;; Create environment variables using Heroku CLI tool
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Run an instance of the app (container) and send the command `echo $JDBC_DATABASE_URL`
;; heroku run echo \$JDBC_DATABASE_URL --app banking-on-clojure-staging

;; This returns the correct JDBC connection string
;; jdbc:postgresql://<hostname>:port/<database-name>?user=<username>&password=<password>&sslmode=require

;; This jdbc connection string is generated from the DATABASE_URL config var that is added to the heroku app when a database is provisioned.

;; The Heroku build scripts automatically create a JDBC_DATABASE_URL value from the DATABASE_URL, so it is reasonable to expect the




;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  (:require '[next.jdbc :as jdbc])

  (defn information-tables
    [data-source]
    (jdbc/execute! data-source ["select * from information_schema.tables"]))

  (information-tables db-spec-dev)
  (information-tables db-spec-staging)
  (information-tables db-spec)

  ) ;; End of rich comment block
