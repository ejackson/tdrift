(ns config.migrate-config
  (:require [clojure.contrib.sql :as sql]))

(defonce DB {:classname "com.mysql.jdbc.Driver"
             :subprotocol "mysql"
             :subname "//mysql-server:3306/bookkeeper"
             :user "bookkeeper"
             :password "bookkeeper"
             :auto-commit true
             :fetch-size  500})

(defn db-version []
  (sql/with-connection DB
    (sql/with-query-results res 
      ["select version from schema_migrations limit 1"]
      (:version (first res)))))

(defn update-db-version [version]
  (sql/with-connection DB
    (sql/insert-values :schema_migrations [:version] [version])))

(defn migrate-config []
  {:init (fn [_])
   :directory "db/migrations"
   :current-version db-version
   :update-version update-db-version})