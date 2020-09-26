;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; DEPRECATED
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(ns practicalli.deprecated.design-journal-database-access
  (:require [next.jdbc :as jdbc]
            [next.jdbc.sql :as jdbc-sql]
            [next.jdbc.specs :as jdbc-spec]))

;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment


  ) ;; End of rich comment block


;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment


  ;; Database specification and connection
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  ;; Development environment
  ;; H2 in-memory database
  (def db-specification-dev {:dbtype "h2" :dbname "banking-on-clojure"})

  ;; Database connection
  #_(def data-source-dev (jdbc/get-datasource db-specification-dev))


  ;; Database schema
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  ;; define the schema to create tables
  ;; define a function to open a database connect, create all table schema as a transaction and then close the connection.


  #_(def schema-account-holders-table
      ["CREATE TABLE PUBLIC.ACCOUNT_HOLDERS(
        ACCOUNT_HOLDER_ID UUID(16) NOT NULL,
        FIRST_NAME VARCHAR(32),
        LAST_NAME VARCHAR(32),
        EMAIL_ADDRESS VARCHAR(32),
        RESIDENTIAL_ADDRESS VARCHAR(255),
        SOCIAL_SECURITY_NUMBER VARCHAR(32) "])

  (def schema-account-holders-table
    ["CREATE TABLE PUBLIC.ACCOUNT_HOLDERS(
        ACCOUNT_HOLDER_ID UUID(16) NOT NULL,
        FIRST_NAME VARCHAR(32),
        LAST_NAME VARCHAR(32),
        EMAIL_ADDRESS VARCHAR(32),
        RESIDENTIAL_ADDRESS VARCHAR(255),
        SOCIAL_SECURITY_NUMBER VARCHAR(32),
        CONSTRAINT CONSTRAINT_3 PRIMARY KEY (ACCOUNT_HOLDER_ID))"])

  (def schema-accounts-table
    )

  (def schema-transaction-history-table
    )



  (defn create-tables
    "Establish a connection to the data source and create all tables within a transaction.
  Close the database connection.
  Arguments:
  - table-schemas: a vector of sql statements, each creating a table"
    [table-schemas data-spec]

    (with-open [con (jdbc/get-connection data-spec)]
      (jdbc/with-transaction [tx con]
        (jdbc/execute! tx table-schemas ))))

  ;; When using connection pools, no need for the with-open, simply use jdbc/with-transaction with the connection pool
  ;; Simplifies the code



  ) ;; End of rich comment block

(comment

  ;; Create account_holders table
  (create-tables schema-account-holders-table
                 db-specification-dev)
  (create-tables schema-accounts-table
                 db-specification-dev)
  (create-tables schema-transaction-history-table
                 db-specification-dev)


  ;; Create all tables for the application
  (create-tables [schema-account-holders-table schema-accounts-table schema-transaction-history-table]
                 db-specification-dev)
  )

;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; Refactor the create-tables function to map the jdbc/execute! over each table-schema

  (defn create-tables
    "Establish a connection to the data source and create all tables within a transaction.
  Close the database connection.
  Arguments:
  - table-schemas: a vector of sql statements, each creating a table"
    [table-schemas data-spec]

    (with-open [con (jdbc/get-connection data-spec)]
      (jdbc/with-transaction [tx con]
        (map #(jdbc/execute! tx %) table-schemas ))))

  ;; Using clojure.core/map to iterate over the collection of statements makes the transaction close


  ;; Refactor to use doseq to avoid the transaction from closing.

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

  ) ;; End of rich comment block



(comment

  ;; Create all tables for the application
  (create-tables [schema-account-holders-table schema-accounts-table schema-transaction-history-table]
                 db-specification-dev)
  )


;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment
  ;; spec failures
  ;; Forgot to put the SQL statement string in a vector

  #_(def schema-account-holders-table
      "CREATE TABLE PUBLIC.ACCOUNT_HOLDERS(
        ACCOUNT_HOLDER_ID UUID(16) NOT NULL,
        FIRST_NAME VARCHAR(32),
        LAST_NAME VARCHAR(32),
        EMAIL_ADDRESS VARCHAR(32),
        RESIDENTIAL_ADDRESS VARCHAR(255),
        SOCIAL_SECURITY_NUMBER VARCHAR(32),
        CONSTRAINT CONSTRAINT_3 PRIMARY KEY (ACCOUNT_HOLDER_ID))")

  ;; Spec assertion failed.

  ;; Spec: #object[clojure.spec.alpha$regex_spec_impl$reify__2509 0x68649ba6 "clojure.spec.alpha$regex_spec_impl$reify__2509@68649ba6"]
  ;; Value: (#object[org.h2.jdbc.JdbcConnection 0xc059803 "conn46: url=jdbc:h2:./banking-on-clojure user="]
  ;;          "CREATE TABLE PUBLIC.ACCOUNT_HOLDERS(\n        ACCOUNT_HOLDER_ID UUID(16) NOT NULL,\n        FIRST_NAME VARCHAR(32),\n        LAST_NAME VARCHAR(32),\n        EMAIL_ADDRESS VARCHAR(32),\n        RESIDENTIAL_ADDRESS VARCHAR(255),\n        SOCIAL_SECURITY_NUMBER VARCHAR(32),\n        CONSTRAINT CONSTRAINT_3 PRIMARY KEY (ACCOUNT_HOLDER_ID))")

  ;; Problems:

  ;; val: "CREATE TABLE PUBLIC.ACCOUNT_HOLDERS(\n        ACCOUNT_HOLDER_ID UUID(16) NOT NULL,\n        FIRST_NAME VARCHAR(32),\n        LAST_NAME VARCHAR(32),\n        EMAIL_ADDRESS VARCHAR(32),\n        RESIDENTIAL_ADDRESS VARCHAR(255),\n        SOCIAL_SECURITY_NUMBER VARCHAR(32),\n        CONSTRAINT CONSTRAINT_3 PRIMARY KEY (ACCOUNT_HOLDER_ID))"
  ;; in: [1]
  ;; failed: vector?
  ;; spec: :next.jdbc.specs/sql-params
  ;; at: [:sql :sql-params :clojure.spec.alpha/pred]

  ;; val: "CREATE TABLE PUBLIC.ACCOUNT_HOLDERS(\n        ACCOUNT_HOLDER_ID UUID(16) NOT NULL,\n        FIRST_NAME VARCHAR(32),\n        LAST_NAME VARCHAR(32),\n        EMAIL_ADDRESS VARCHAR(32),\n        RESIDENTIAL_ADDRESS VARCHAR(255),\n        SOCIAL_SECURITY_NUMBER VARCHAR(32),\n        CONSTRAINT CONSTRAINT_3 PRIMARY KEY (ACCOUNT_HOLDER_ID))"
  ;; in: [1]
  ;; failed: nil?
  ;; at: [:sql :sql-params :clojure.spec.alpha/nil]






  #_(def create-table-account-holders
      "Create the account_holders table"
      (jdbc/execute!
        data-source-dev
        ["CREATE TABLE PUBLIC.ACCOUNT_HOLDERS(
        ACCOUNT_HOLDER_ID UUID(16) NOT NULL,
        FIRST_NAME VARCHAR(32),
        LAST_NAME VARCHAR(32),
        EMAIL_ADDRESS VARCHAR(32),
        RESIDENTIAL_ADDRESS VARCHAR(255),
        SOCIAL_SECURITY_NUMBER VARCHAR(32),
        CONSTRAINT CONSTRAINT_3 PRIMARY KEY (ACCOUNT_HOLDER_ID))
"]))

  ;; DBeaver DDL
  ;; -- PUBLIC.ACCOUNT_HOLDERS definition
  ;; -- Drop table
  ;; -- DROP TABLE PUBLIC.ACCOUNT_HOLDERS;

  ;; CREATE TABLE PUBLIC.ACCOUNT_HOLDERS(
  ;;                                     ACCOUNT_HOLDER_ID UUID(16) NOT NULL,
  ;;                                     FIRST_NAME VARCHAR(32),
  ;;                                     LAST_NAME VARCHAR(32),
  ;;                                     EMAIL_ADDRESS VARCHAR(32),
  ;;                                     RESIDENTIAL_ADDRESS VARCHAR(255),
  ;;                                     SOCIAL_SECURITY_NUMBER VARCHAR(32),
  ;;                                     CONSTRAINT CONSTRAINT_3 PRIMARY KEY (ACCOUNT_HOLDER_ID))
  ;; CREATE UNIQUE INDEX PRIMARY_KEY_3 ON PUBLIC.ACCOUNT_HOLDERS (ACCOUNT_HOLDER_ID);

  ;; Sources of errors
  ;; - missing commas gives `Syntax error in SQL statement`


  #_(def create-table-accounts
      "Create the accounts table"
      (jdbc/execute!
        data-source-dev
        ["create table accounts(
     account_id uuid,
     account_number int auto_increment primary key,
     account_holder_id uuid,
     account_sort_code varchar(6),
     account_name varchar(32),
     current_balance varchar(255),
     last_updated Date)"]))


  ;; -- PUBLIC.ACCOUNTS definition
  ;; -- Drop table
  ;; -- DROP TABLE PUBLIC.ACCOUNTS;
  ;; CREATE TABLE PUBLIC.ACCOUNTS
  ;; (
  ;;  ACCOUNT_ID UUID(16) NOT NULL,
  ;;  ACCOUNT_NUMBER INTEGER DEFAULT NEXT VALUE FOR "PUBLIC"."SYSTEM_SEQUENCE_C36F1FB0_EA6A_424C_87D3_99F451D8D1B6" NOT NULL AUTO_INCREMENT,
  ;;  ACCOUNT_SORT_CODE VARCHAR(6),
  ;;  ACCOUNT_NAME VARCHAR(32),
  ;;  CURRENT_BALANCE VARCHAR(255),
  ;;  LAST_UPDATED DATE,
  ;;  ACCOUNT_HOLDER_ID VARCHAR(100) NOT NULL,
  ;;  CONSTRAINT CONSTRAINT_A PRIMARY KEY (ACCOUNT_ID)
  ;;  );
  ;; CREATE INDEX ACCOUNTS_FK_INDEX_A ON PUBLIC.ACCOUNTS (ACCOUNT_HOLDER_ID);
  ;; CREATE UNIQUE INDEX PRIMARY_KEY_6 ON PUBLIC.ACCOUNTS (ACCOUNT_ID);

  ;; -- PUBLIC.ACCOUNTS foreign keys
  ;; ALTER TABLE PUBLIC.ACCOUNTS ADD CONSTRAINT ACCOUNTS_FK FOREIGN KEY (ACCOUNT_HOLDER_ID) REFERENCES PUBLIC.ACCOUNT_HOLDERS(ACCOUNT_HOLDER_ID) ON DELETE RESTRICT ON UPDATE RESTRICT;


  #_(def create-schema-transaction-history
      "Table to hold all transactions within the bank.
  Each transaction is associated with an account."
      (jdbc/execute!
        data-source-dev
        ["create table transaction_history(
     account_id uuid primary key,
     account_number int,
     account_sort_code varchar(6),
     account_name varchar(32),
     current_balance varchar(255),
     transaction_date Date)"]))

  ;; -- PUBLIC.TRANSACTION_HISTORY definition
  ;; -- Drop table
  ;; -- DROP TABLE PUBLIC.TRANSACTION_HISTORY;
  ;; CREATE TABLE PUBLIC.TRANSACTION_HISTORY
  ;; (
  ;;  ACCOUNT_ID UUID(16) NOT NULL,
  ;;  ACCOUNT_NUMBER INTEGER,
  ;;  ACCOUNT_SORT_CODE VARCHAR(6),
  ;;  ACCOUNT_NAME VARCHAR(32),
  ;;  CURRENT_BALANCE VARCHAR(255),
  ;;  TRANSACTION_DATE DATE,
  ;;  CONSTRAINT CONSTRAINT_9 PRIMARY KEY (ACCOUNT_ID)
  ;;  );
  ;; CREATE UNIQUE INDEX PRIMARY_KEY_9 ON PUBLIC.TRANSACTION_HISTORY (ACCOUNT_ID);

  ;; -- PUBLIC.TRANSACTION_HISTORY foreign keys
  ;; ALTER TABLE PUBLIC.TRANSACTION_HISTORY ADD CONSTRAINT TRANSACTION_HISTORY_FK FOREIGN KEY (ACCOUNT_ID)
  ;; REFERENCES PUBLIC.ACCOUNTS(ACCOUNT_ID) ON DELETE RESTRICT ON UPDATE RESTRICT;



  #_(defn show-schema
      [data-source table-name]
      (jdbc/execute! data-source [(str "show columns from " table-name)]))

  #_(defn drop-table
      [data-source table-name]
      (jdbc/execute! data-source [(str "drop table " table-name)]))

  ) ;; End of rich comment block


(comment  ;; Using Schema helpers
  ;; View application table schema
  (show-schema data-source-dev "PUBLIC.ACCOUNT_HOLDERS")
  (show-schema data-source-dev "PUBLIC.ACCOUNTS")
  (show-schema data-source-dev "PUBLIC.ACCOUNT_HOLDERS")

  ;; View database system schema
  (show-schema data-source-dev "information_schema.tables")


  ;; Remove tables from the database
  (drop-table data-source-dev "PUBLIC.ACCOUNT_HOLDERS")
  (drop-table data-source-dev "PUBLIC.ACCOUNTS")
  (drop-table data-source-dev "PUBLIC.ACCOUNT_HOLDERS")

  )


(comment

  ;; Since next.jdbc uses raw Java JDBC types, you can use with-open directly to reuse connections and ensure they are cleaned up correctly:

  (let [my-datasource (jdbc/get-datasource {:dbtype "..." :dbname "..." ...})]
    (with-open [connection (jdbc/get-connection my-datasource)]
      (jdbc/execute! connection [...])
      (reduce my-fn init-value (jdbc/plan connection [...]))
      (jdbc/execute! connection [...])))


  )

;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; Queries - Create
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  ;; DBeaver insert DDL
  ;; INSERT INTO PUBLIC.ACCOUNT_HOLDERS
  ;; (ACCOUNT_HOLDER_ID, FIRST_NAME, LAST_NAME, EMAIL_ADDRESS, RESIDENTIAL_ADDRESS, SOCIAL_SECURITY_NUMBER)
  ;; VALUES(?, '', '', '', '', '');


  #_(defn persist-account-holder
      "Persist a new account holder record"
      [account-holder-id]
      (jdbc/execute!
        data-source-dev
        [(str "insert into account_holders(account_holder_id,first_name,last_name,email_address,residential_address,social_security_number)
    values('" account-holder-id "', 'Jenny', 'Jetpack', 'jen@jetpack.org', '42 Meaning Lane, Altar IV', 'AB101112C' )")]) )

  (persist-account-holder (java.util.UUID/randomUUID))

  ;; Helper function to calculate the current balance
  #_(defn current-balance
      "Mock function, always returns 1000 as a string"
      [account_number date]
      "1000")


  #_(def new-account-default-balance
      "100")

  #_(defn persist-new-account
      "Persist a new account"
      [account-holder-id]
      (jdbc/execute!
        data-source-dev
        [(str
           "INSERT INTO PUBLIC.ACCOUNTS
         (ACCOUNT_SORT_CODE, ACCOUNT_NAME, CURRENT_BALANCE, LAST_UPDATED, ACCOUNT_HOLDER_ID)
       VALUES
         ('010101', 'current', '" new-account-default-balance "', '2020-09-03', '" account-holder-id "');")]))



  ;; Generated from DBeaver
  ;; -- Auto-generated SQL script #202009032248
  ;; "INSERT INTO PUBLIC.ACCOUNTS
  ;;    (ACCOUNT_ID,ACCOUNT_NUMBER,ACCOUNT_SORT_CODE,ACCOUNT_NAME,CURRENT_BALANCE,LAST_UPDATED,ACCOUNT_HOLDER_ID)
  ;;  VALUES
  ;;    ('6cce9257-8a3f-4efa-90cc-a7af81889860',1111111,'010201','current','100','2020-09-01','623dcc16-4726-4bbd-b785-ee00a7eff518');"


  #_(defn persist-new-account-hard-coded
      "Persist a new account"
      []
      (jdbc/execute!
        data-source-dev
        ["INSERT INTO PUBLIC.ACCOUNTS
   (ACCOUNT_ID,ACCOUNT_NUMBER,ACCOUNT_SORT_CODE,ACCOUNT_NAME,CURRENT_BALANCE,LAST_UPDATED,ACCOUNT_HOLDER_ID)
 VALUES
   ('b486aa93-bf4f-433d-b73c-5dc050bbd7e6',1111111,'010201','current','100','2020-09-01','623dcc16-4726-4bbd-b785-ee00a7eff518');"]))

  #_(persist-new-account-hard-coded)

  #_(defn persist-new-account-account-holder
      "Persist a new account"
      [account-holder-id]
      (jdbc/execute!
        data-source-dev
        [(str "INSERT INTO PUBLIC.ACCOUNTS
   (ACCOUNT_ID,ACCOUNT_NUMBER,ACCOUNT_SORT_CODE,ACCOUNT_NAME,CURRENT_BALANCE,LAST_UPDATED,ACCOUNT_HOLDER_ID)
 VALUES
   ('" account-holder-id "',1111111,'010201','current','100','2020-09-01','623dcc16-4726-4bbd-b785-ee00a7eff518');")]))

  (persist-new-account-account-holder (java.util.UUID/randomUUID))


  ;; Legacy date types not recommended
  ;; (java.util.Date.)
  ;; (java.sql.Date)
  ;; Calendar, GregorianCalendar, java.util.Date, java.sql.Date, java.sql.Timestamp

  ;; Recommended: use java.time from JSR 310: Date and Time API
  ;; https://jcp.org/en/jsr/detail?id=310
  ;; java-time-types-matrix.png
  ;; https://stackoverflow.com/questions/59482025/clojure-creating-a-date-time-object-for-the-jdbc
  ;; (java.time.LocalDate.)



  (defn persist-transaction
    "Persist a new transaction"
    [account-holder-id]
    (jdbc/execute!
      data-source-dev
      [(str "insert into accounts(account_holder_id,account_sort_code,account_name, current_balance)
    values('" account-holder-id "', '010101', 'current', '1000' )")]) )



  ;; Refactor to use specific next.jdbc functions for inserting records.
  ;; insert!

  ;; Given a table name (as a keyword) and a hash map of column names and values, this performs a single row insertion into the database:

  (sql/insert! ds :address {:name "A. Person" :email "albert@person.org"})
  ;; equivalent to
  (jdbc/execute-one! ds ["INSERT INTO address (name,email) VALUES (?,?)"
                         "A.Person" "albert@person.org"] {:return-keys true})



  (defn insert-new-account-holder
    "Persist a new account"
    [data-source account-holder-id]
    (jdbc/insert!
      data-source
      ["INSERT INTO PUBLIC.ACCOUNTS
        (ACCOUNT_ID,ACCOUNT_NUMBER,ACCOUNT_SORT_CODE,ACCOUNT_NAME,CURRENT_BALANCE,LAST_UPDATED,ACCOUNT_HOLDER_ID)
      VALUES (?,?,?,?,?,?,?)"
       account-holder-id 1111111 "010201","current","100","2020-09-01","623dcc16-4726-4bbd-b785-ee00a7eff518"

       (str )]))


  ;; insert-multi!

  ;; Given a table name (as a keyword), a vector of column names, and a vector of row value vectors, this performs a multi-row insertion into the database:

  (sql/insert-multi! ds :address
                     [:name :email]
                     [["Stella" "stella@artois.beer"]
                      ["Waldo" "waldo@lagunitas.beer"]
                      ["Aunt Sally" "sour@lagunitas.beer"]])
  ;; equivalent to
  (jdbc/execute! ds ["INSERT INTO address (name,email) VALUES (?,?), (?,?), (?,?)"
                     "Stella" "stella@artois.beer"
                     "Waldo" "waldo@lagunitas.beer"
                     "Aunt Sally" "sour@lagunitas.beer"] {:return-keys true})

  ;; Note: this expands to a single SQL statement with placeholders for every value being inserted -- for large sets of rows, this may exceed the limits on SQL string size and/or number of parameters for your JDBC driver or your database. Several databases have a limit of 1,000 parameter placeholders. Oracle does not support this form of multi-row insert, requiring a different syntax altogether.

  next.jdbc.prepare/execute-batch! for an alternative approach.



  ;; Queries - Read
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  (defn account-holders
    []
    (jdbc/execute!
      data-source-dev
      ["select * from account_holders"]))

  (account-holders)


  ;; Refactor to make a generic read


  (defn show-all-records
    [data-source table-name]
    (jdbc/execute!
      data-source
      [(str "select * from " table-name)]))

  (show-all-records data-source-dev "account_holders")
  (show-all-records data-source-dev "accounts")
  (show-all-records data-source-dev "transaction_history")

  (defn account-details
    "Given an account holder id,
  return  details of  all accounts"
    [account-holder-id]
    (jdbc/execute!
      data-source
      ["select * from accounts
      where account_holder = ?" account-holder-id]))





  ;; Batch processing
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;




  ;; REPL Driven Development
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


  ;; Refactor all the specific defs to a single helper function
  ;; but then is there any value in this, as its just the jdbc/execute! command with two arguments

  (defn execute-sql-query
    "Execute a given SQL query via the specified data source"
    [data-source sql-query]
    (jdbc/execute! data-source sql-query) )


  ) ;; End of rich comment block


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



  ;; (defn information-tables
  ;;   [data-source]
  ;;   (jdbc/execute!
  ;;     data-source
  ;;     ["select * from information_schema.tables"]))


  ;; (defn show-schema
  ;;   [table-name]
  ;;   (jdbc/execute!
  ;;     data-source
  ;;     [(str "show columns from " table-name)]))


  ;; (defn drop-table
  ;;   [table-name]
  ;;   (jdbc/execute!
  ;;     data-source
  ;;     [(str "drop table " table-name)]))


  (jdbc/execute!
    data-source-postgresql
    ["create table account-holders(
     id int,
     name varchar(32),
     email varchar(255))"])


  (jdbc/execute!
    data-source-postgresql
    ["CREATE TABLE IF NOT EXISTS public.account_holder (
      user_id serial PRIMARY KEY,
      username VARCHAR ( 50 ) UNIQUE NOT NULL,
      password VARCHAR ( 50 ) NOT NULL,
      email VARCHAR ( 255 ) UNIQUE NOT NULL,
      created_on TIMESTAMP NOT NULL,
      last_login TIMESTAMP"])


  (jdbc/execute!
    data-source-postgresql
    ["CREATE TABLE public.account_holder (username varchar(50) NOT NULL);"])



  CREATE TABLE accounts (
                         user_id serial PRIMARY KEY,
                         username VARCHAR ( 50 ) UNIQUE NOT NULL,
                         password VARCHAR ( 50 ) NOT NULL,
                         email VARCHAR ( 255 ) UNIQUE NOT NULL,
                         created_on TIMESTAMP NOT NULL,
                         last_login TIMESTAMP
                         );


  ;; Database schema designs - from the clojure.spec Specifications

  ;; Account holder
  ;; Each record contains all the information about a customer
  ;; account_holder_id : uuid
  ;;first-name
  ;;last-name
  ;; email-address
  ;; residential-address
  ;;social-security-id

  ;; Accounts schema
  ;; all accounts are contained in one table to keep a fairly flat schema design
  ;; last_updated represents when current_balance was last updated
  ;; This allows pending transactions to be identified
  ;; account_id : uuid
  ;; account_number: integer
  ;; account_type : string (current, savings, mortgage)
  ;; sort_code : sort-code
  ;; account_name :  string
  ;; account_holder_id : uuid
  ;; current_ballance : float
  ;; last_updated : date

  ;; account_history
  ;; all transactions are contained in one table to keep a flat schema
  ;; Totals in accounts_are calculated  from  the relevant transactions
  ;; and the accounts.last_updated date is updated
  ;; account_id : uuid
  ;; transaction_id : uuid
  ;; transaction_value: float



  ;; Using template functions - try HoneySQL instead
  ;; This form of substitution in the sql statements is not supported
  ;; (defn show-schema
  ;;   [table-name]
  ;;   (jdbc/execute!
  ;;     data-source
  ;;     ["show columns from ?" table-name]))

  ;; (show-schema "accounts")


  (defn drop-table
    [table-name]
    (jdbc/execute!
      data-source
      ["drop table ?" table-name]))



  ;; Refactor for multiple databases

  ;; (defn show-schema
  ;;   [data-source table-name]
  ;;   (jdbc/execute!
  ;;     data-source
  ;;     [(str "show columns from " table-name)]))


  ;; (show-schema data-source-jdbc-postgresql "public.account_holder")

  ;; (defn show-schema-postgres
  ;;   [table-name]
  ;;   (jdbc/execute!
  ;;     data-source-jdbc-postgresql
  ;;     [(str "show columns from " table-name)]))

  ;; (show-schema-postgres "public.account_holder")
  )


;; Instrument specifications

(comment

  (jdbc-spec/instrument))


;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; May included repetition of the above
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  ;; Using jdbc/execute!

  (defn persist-account-holder
    "Persist a new account holder record"
    [account-holder-id db-spec]
    (with-open [connection (jdbc/get-connection db-spec)]
      (jdbc/execute!
        connection
        [(str "insert into account_holders(
               account_holder_id,first_name,last_name,email_address,residential_address,social_security_number)
             values(
               '" account-holder-id "', 'Jenny', 'Jetpack', 'jen@jetpack.org', '42 Meaning Lane, Altar IV', 'AB101112C' )")])) )

  (persist-account-holder (java.util.UUID/randomUUID) db-specification-dev)


  ;; refactor sql statement into its own var

  (def account-holder-jenny
    [(str "insert into account_holders(account_holder_id,first_name,last_name,email_address,residential_address,social_security_number)
    values('" (java.util.UUID/randomUUID) "', 'Jenny', 'Jetpack', 'jen@jetpack.org', '42 Meaning Lane, Altar IV', 'AB101112C' )")])

  ;; Create a generic insert function
  #_(defn insert-record
      [db-spec sql-statement]
      (with-open [connection (jdbc/get-connection db-spec)]
        (jdbc/execute! connection sql-statement)))

  (insert-record  db-specification-dev  account-holder-jenny)


  ;; Using next.jdbc.sql functions
  ;; (jdbc-sql/insert! ds :address {:name "A. Person" :email "albert@person.org"})

  #_(defn add-account-holder
      [account-holder-id data-source]
      (jdbc-sql/insert!
        data-source
        :table-name {:column-name "value" ,,,}))

  (defn add-account-holder
    [account-holder-id db-spec]
    (with-open [connection (jdbc/get-connection db-spec)]
      (jdbc-sql/insert!
        connection
        :public.account_holders {:account_holder_id      account-holder-id
                                 :first_name             "Rachel"
                                 :last_name              "Rocketpack"
                                 :email_address          "rach@rocketpack.org"
                                 :residential_address    "1 Ultimate Question Lane, Altar IV"
                                 :social_security_number "BB104312D"})))

  (add-account-holder (java.util.UUID/randomUUID) db-specification-dev)


  ;; Generic insert function

  (defn insert-record
    [table record-data db-spec]
    (with-open [connection (jdbc/get-connection db-spec)]
      (jdbc-sql/insert! connection table record-data)))


  ;; The data to pass in looks familiar.  Its the same information as a qualified specification

  :public.account_holders {:account_holder_id      (java.util.UUID/randomUUID)
                           :first_name             "Rachel"
                           :last_name              "Rocketpack"
                           :email_address          "rach@rocketpack.org"
                           :residential_address    "1 Ultimate Question Lane, Altar IV"
                           :social_security_number "BB104312D"}

  ;; The specifications already defined can be used to generate mock data for the database.



  ;; Accounts
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;; Helper function to calculate the current balance
  (defn current-balance
    "Mock function, always returns 1000 as a string"
    [account_number date]
    "1000")


  (def new-account-default-balance
    "100")

  #_(defn persist-new-account
      "Persist a new account"
      [account-holder-id]
      (jdbc/execute!
        data-source-dev
        [(str
           "INSERT INTO PUBLIC.ACCOUNTS
         (ACCOUNT_SORT_CODE, ACCOUNT_NAME, CURRENT_BALANCE, LAST_UPDATED, ACCOUNT_HOLDER_ID)
       VALUES
         ('010101', 'current', '" new-account-default-balance "', '2020-09-03', '" account-holder-id "');")]))



  ;; Generated from DBeaver
  ;; -- Auto-generated SQL script #202009032248
  ;; "INSERT INTO PUBLIC.ACCOUNTS
  ;;    (ACCOUNT_ID,ACCOUNT_NUMBER,ACCOUNT_SORT_CODE,ACCOUNT_NAME,CURRENT_BALANCE,LAST_UPDATED,ACCOUNT_HOLDER_ID)
  ;;  VALUES
  ;;    ('6cce9257-8a3f-4efa-90cc-a7af81889860',1111111,'010201','current','100','2020-09-01','623dcc16-4726-4bbd-b785-ee00a7eff518');"

  ) ;; End of rich comment block



;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment


  ;; Repl experiments - to sort
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  (defn persist-new-account-hard-coded
    "Persist a new account"
    []
    (jdbc/execute!
      data-source-dev
      ["INSERT INTO PUBLIC.ACCOUNTS
   (ACCOUNT_ID,ACCOUNT_NUMBER,ACCOUNT_SORT_CODE,ACCOUNT_NAME,CURRENT_BALANCE,LAST_UPDATED,ACCOUNT_HOLDER_ID)
 VALUES
   ('b486aa93-bf4f-433d-b73c-5dc050bbd7e6',1111111,'010201','current','100','2020-09-01','623dcc16-4726-4bbd-b785-ee00a7eff518');"]))

  (persist-new-account-hard-coded)

  (defn persist-new-account-account-holder
    "Persist a new account"
    [account-holder-id]
    (jdbc/execute!
      data-source-dev
      [(str "INSERT INTO PUBLIC.ACCOUNTS
   (ACCOUNT_ID,ACCOUNT_NUMBER,ACCOUNT_SORT_CODE,ACCOUNT_NAME,CURRENT_BALANCE,LAST_UPDATED,ACCOUNT_HOLDER_ID)
 VALUES
   ('" account-holder-id "',1111111,'010201','current','100','2020-09-01','623dcc16-4726-4bbd-b785-ee00a7eff518');")]))

  (persist-new-account-account-holder (java.util.UUID/randomUUID))


  ;; Legacy date types not recommended
  ;; (java.util.Date.)
  ;; (java.sql.Date)
  ;; Calendar, GregorianCalendar, java.util.Date, java.sql.Date, java.sql.Timestamp

  ;; Recommended: use java.time from JSR 310: Date and Time API
  ;; https://jcp.org/en/jsr/detail?id=310
  ;; java-time-types-matrix.png
  ;; https://stackoverflow.com/questions/59482025/clojure-creating-a-date-time-object-for-the-jdbc
  ;; (java.time.LocalDate.)



  (defn persist-transaction
    "Persist a new transaction"
    [account-holder-id]
    (jdbc/execute!
      data-source-dev
      [(str "insert into accounts(account_holder_id,account_sort_code,account_name, current_balance)
    values('" account-holder-id "', '010101', 'current', '1000' )")]) )



  ;; Refactor to use specific next.jdbc functions for inserting records.
  ;; insert!

  ;; Given a table name (as a keyword) and a hash map of column names and values, this performs a single row insertion into the database:

  (sql/insert! ds :address {:name "A. Person" :email "albert@person.org"})
  ;; equivalent to
  (jdbc/execute-one! ds ["INSERT INTO address (name,email) VALUES (?,?)"
                         "A.Person" "albert@person.org"] {:return-keys true})



  (defn insert-new-account-holder
    "Persist a new account"
    [data-source account-holder-id]
    (jdbc/insert!
      data-source
      ["INSERT INTO PUBLIC.ACCOUNTS
        (ACCOUNT_ID,ACCOUNT_NUMBER,ACCOUNT_SORT_CODE,ACCOUNT_NAME,CURRENT_BALANCE,LAST_UPDATED,ACCOUNT_HOLDER_ID)
      VALUES (?,?,?,?,?,?,?)"
       account-holder-id 1111111 "010201","current","100","2020-09-01","623dcc16-4726-4bbd-b785-ee00a7eff518"]))




  ;; insert-multi!

  ;; Given a table name (as a keyword), a vector of column names, and a vector of row value vectors, this performs a multi-row insertion into the database:

  (sql/insert-multi! ds :address
                     [:name :email]
                     [["Stella" "stella@artois.beer"]
                      ["Waldo" "waldo@lagunitas.beer"]
                      ["Aunt Sally" "sour@lagunitas.beer"]])
  ;; equivalent to
  (jdbc/execute! ds ["INSERT INTO address (name,email) VALUES (?,?), (?,?), (?,?)"
                     "Stella" "stella@artois.beer"
                     "Waldo" "waldo@lagunitas.beer"
                     "Aunt Sally" "sour@lagunitas.beer"] {:return-keys true})

  ;; Note: this expands to a single SQL statement with placeholders for every value being inserted -- for large sets of rows, this may exceed the limits on SQL string size and/or number of parameters for your JDBC driver or your database. Several databases have a limit of 1,000 parameter placeholders. Oracle does not support this form of multi-row insert, requiring a different syntax altogether.

  next.jdbc.prepare/execute-batch! for an alternative approach.



  ;; Queries - Read
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  ;; DBeaver
  ;; SELECT ACCOUNT_HOLDER_ID, FIRST_NAME, LAST_NAME, EMAIL_ADDRESS, RESIDENTIAL_ADDRESS, SOCIAL_SECURITY_NUMBER
  ;; FROM PUBLIC.ACCOUNT_HOLDERS;

  (jdbc-sql/query
    (jdbc/get-connection db-specification-dev)
    ["SELECT * from ACCOUNT_HOLDERS"])

  (with-open [connection (jdbc/get-connection db-specification-dev)]
    (jdbc-sql/query
      connection
      ["SELECT * from ACCOUNT_HOLDERS"]))



  ;; Refactor to make a generic read

  (defn show-all-records
    [db-spec table-name]
    (with-open [connection (jdbc/get-connection db-spec)]
      (jdbc/execute!
        connection
        [(str "select * from " table-name)])))


  (show-all-records data-source-dev "PUBLIC.ACCOUNT_HOLDERS")
  (show-all-records data-source-dev "PUBLIC.ACCOUNTS")
  (show-all-records data-source-dev "PUBLIC.TRANSACTION_HISTORY")




  (defn account-details
    "Given an account holder id,
  return  details of  all accounts"
    [account-holder-id]
    (jdbc/execute!
      data-source
      ["select * from accounts
      where account_holder = ?" account-holder-id]))





  ;; Batch processing
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;




  ;; REPL Driven Development
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


  ;; Refactor all the specific defs to a single helper function
  ;; but then is there any value in this, as its just the jdbc/execute! command with two arguments

  (defn execute-sql-query
    "Execute a given SQL query via the specified data source"
    [data-source sql-query]
    (jdbc/execute! data-source sql-query) )


  ) ;; End of rich comment block



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



  ;; (defn information-tables
  ;;   [data-source]
  ;;   (jdbc/execute!
  ;;     data-source
  ;;     ["select * from information_schema.tables"]))


  ;; (defn show-schema
  ;;   [table-name]
  ;;   (jdbc/execute!
  ;;     data-source
  ;;     [(str "show columns from " table-name)]))


  ;; (defn drop-table
  ;;   [table-name]
  ;;   (jdbc/execute!
  ;;     data-source
  ;;     [(str "drop table " table-name)]))


  (jdbc/execute!
    data-source-postgresql
    ["create table account-holders(
     id int,
     name varchar(32),
     email varchar(255))"])


  (jdbc/execute!
    data-source-postgresql
    ["CREATE TABLE IF NOT EXISTS public.account_holder (
      user_id serial PRIMARY KEY,
      username VARCHAR ( 50 ) UNIQUE NOT NULL,
      password VARCHAR ( 50 ) NOT NULL,
      email VARCHAR ( 255 ) UNIQUE NOT NULL,
      created_on TIMESTAMP NOT NULL,
      last_login TIMESTAMP"])


  (jdbc/execute!
    data-source-postgresql
    ["CREATE TABLE public.account_holder (username varchar(50) NOT NULL);"])



  CREATE TABLE accounts (
                         user_id serial PRIMARY KEY,
                         username VARCHAR ( 50 ) UNIQUE NOT NULL,
                         password VARCHAR ( 50 ) NOT NULL,
                         email VARCHAR ( 255 ) UNIQUE NOT NULL,
                         created_on TIMESTAMP NOT NULL,
                         last_login TIMESTAMP
                         );



  )


;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  #_(create-table schema-transaction-history-table
                  db-specification-dev)

  ) ;; End of rich comment block
