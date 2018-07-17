(ns hatredify.routes.home
  (:require [hatredify.layout :as layout]
            [hatredify.db.core :as db]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [hatredify.lib.core :refer [hatredify-text]]))

(defn home-page []
  (layout/render
   "home.html"
   {:default-chunk
    "It is a good day, sir!\nIt's so warm outside!"}))

(defn change-text [data]
  (layout/render "home.html"
                 {:hatredified-chunk
                  (hatredify-text (get-in data [:params :text-chunk]))
                  :initial-chunk
                  (get-in data [:params :text-chunk])}))

(defroutes home-routes
  (GET "/" [] (home-page))
  (POST "/" request (change-text request)))
