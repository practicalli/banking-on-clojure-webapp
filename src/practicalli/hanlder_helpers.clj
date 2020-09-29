(ns practicalli.hanlder-helpers
  (:require
   [practicalli.data.access
    :refer [create-record update-record read-record delete-record
            create-tables delete-tables]]

   [practicalli.data.connection :as connection]))


;; Business Logic - helpers for request handlers
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Database schemas
(defn create-database [] (create-tables connection/db-spec-dev))
(defn delete-database [] (delete-tables connection/db-spec-dev))


;; Create

(defn new-customer
  [customer-details]
  (create-record connection/db-spec-dev :public.customer customer-details))

(defn new-account
  [account-details]
  (create-record connection/db-spec-dev :public.account account-details))

(defn new-transaction
  [transaction-details]
  (create-record connection/db-spec-dev :public.transaction transaction-details))


;; Update

(defn update-customer
  [updated-values customer-id]
  (update-record connection/db-spec-dev :public.customer updated-values customer-id))

(defn update-account
  [updated-values account-id]
  (update-record connection/db-spec-dev :public.account updated-values account-id))

(defn update-transaction
  [updated-values transaction-id]
  (update-record connection/db-spec-dev :public.transaction updated-values transaction-id))


;; Read

(defn customer-account-overview
  [customer-id]
  (read-record connection/db-spec-dev
               ["select * from public.customer where id = ?" customer-id]))

(defn account-details
  [account-id]
  (read-record connection/db-spec-dev ["select * from public.account where id = ?" account-id]))

(defn transaction-history
  [transaction-id]
  (read-record connection/db-spec-dev ["select * from public.transaction where id = ?" transaction-id]))



;; Delete

(defn delete-customer
  [customer-id]
  (delete-record connection/db-spec-dev :public.customer customer-id))

(defn delete-account
  [account-id]
  (delete-record connection/db-spec-dev :public.account account-id))

(defn delete-transaction
  [transaction-id]
  (delete-record connection/db-spec-dev :public.transaction transaction-id))



;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; Create or delete the database tables from practicalli.data.schema
  (create-database)
  (delete-database)


  ) ;; End of rich comment block
