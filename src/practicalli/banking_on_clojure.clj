(ns practicalli.banking-on-clojure
  (:gen-class)
  (:require [org.httpkit.server :as app-server]
            [compojure.core :refer [defroutes GET POST]]
            [practicalli.request-handler :as handler]
            [practicalli.specifications-banking]))


;; Request Routing
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defroutes app
  (GET "/"         [] handler/welcome-page)
  (GET "/accounts" [] handler/accounts-overview-page)
  (GET "/account"  [] handler/account-history)
  (GET "/transfer" [] handler/money-transfer)
  (GET "/payment"  [] handler/money-payment)

  (GET "/register" [] handler/register-account-holder) )



;; System
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Reference to application server instance for stopping/restarting
(defonce app-server-instance (atom nil))


(defn app-server-start
  "Start the application server and log the time of start."

  [http-port]
  (println (str (java.util.Date.)
                " INFO: Starting server on port: " http-port))
  (reset! app-server-instance
          (app-server/run-server #'app {:port http-port})))


(defn app-server-stop
  "Gracefully shutdown the server, waiting 100ms.  Log the time of shutdown"
  []
  (when-not (nil? @app-server-instance)
    (@app-server-instance :timeout 100)
    (reset! app-server-instance nil)
    (println (str (java.util.Date.)
                  " INFO: Application server shutting down..."))))


(defn app-server-restart
  "Convenience function to stop and start the application server"

  [http-port]
  (app-server-stop)
  (app-server-start http-port))


(defn -main
  "Select a value for the http port the app-server will listen to
  and call app-server-start

  The http port is either an argument passed to the function,
  an operating system environment variable or a default value."

  [& [http-port]]
  (let [http-port (Integer. (or http-port (System/getenv "PORT") "8888"))]
    (app-server-start http-port)))


;; REPL driven development helpers
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(comment

  ;; Start application server - via `-main` or `app-server-start`
  (-main)
  (app-server-start 8888)

  ;; Stop / restart application server
  (app-server-stop)
  (app-server-restart 8888)

  ;; Get PORT environment variable from Operating System
  (System/getenv "PORT")

  ;; Get all environment variables
  ;; use a data inspector to view environment-variables name
  (def environment-variables
    (System/getenv))

  ;; Check values set in the default system properties
  (def system-properties
    (System/getProperties))

  )
