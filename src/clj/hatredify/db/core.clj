(ns hatredify.db.core
  (:require
    [clj-time.jdbc]
    [conman.core :as conman]
    [mount.core :refer [defstate]]
    [hatredify.config :refer [env]]))

(defstate ^:dynamic *db*
  :start
  (when (env :database-url)
    (conman/connect! {:jdbc-url (env :database-url)}))
  :stop
  (when *db*
    (conman/disconnect! *db*)))

(when *db*
  (conman/bind-connection *db* "sql/queries.sql"))
