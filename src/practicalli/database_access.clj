(ns practicalli.database-access
  (:require [next.jdbc :as jdbc]))


;; Database specification and connection
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; H2 in-memory database
(def db-specification {:dbtype "h2" :dbname "banking-on-clojure"})

;; Database connection
(def data-source (jdbc/get-datasource db-specification))


(defn register-account-holder
  [customer-details]
  (assoc customer-details
         :practicalli.specifications-banking/account-id
         (java.util.UUID/randomUUID)))



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
