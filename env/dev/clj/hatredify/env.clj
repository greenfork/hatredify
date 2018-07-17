(ns hatredify.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [hatredify.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[hatredify started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[hatredify has shut down successfully]=-"))
   :middleware wrap-dev})
