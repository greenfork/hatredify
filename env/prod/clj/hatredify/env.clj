(ns hatredify.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[hatredify started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[hatredify has shut down successfully]=-"))
   :middleware identity})
