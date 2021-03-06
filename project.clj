(defproject hatredify "1.0.0"

  :description "Web-application which changes chunks of text to more evil form."
  :url "https://github.com/greenfork/hatredify"

  :dependencies [[clj-time "0.14.4"]
                 [com.h2database/h2 "1.4.196"]
                 [compojure "1.6.1"]
                 [conman "0.8.2"]
                 [cprop "0.1.11"]
                 [funcool/struct "1.3.0"]
                 [luminus-immutant "0.2.4"]
                 [luminus-migrations "0.5.1"]
                 [luminus-nrepl "0.1.4"]
                 [luminus/ring-ttl-session "0.3.2"]
                 [markdown-clj "1.0.2"]
                 [metosin/muuntaja "0.5.0"]
                 [metosin/ring-http-response "0.9.0"]
                 [mount "0.1.12"]
                 [org.clojure/clojure "1.9.0"]
                 [org.clojure/tools.cli "0.3.7"]
                 [org.clojure/tools.logging "0.4.1"]
                 [org.webjars.bower/tether "1.4.4"]
                 [org.webjars/bootstrap "4.1.1"]
                 [org.webjars/font-awesome "5.1.0"]
                 [org.webjars/jquery "3.3.1-1"]
                 [org.webjars/webjars-locator "0.34"]
                 [ring-webjars "0.2.0"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-defaults "0.3.2"]
                 [selmer "1.11.8"]
                 [clj-wordnet "0.3.0"]]

  ;; Includes custom build of clj-wordnet library
  :repositories [["local"
                  ~(str (.toURI (java.io.File. "resources/libs")))]]

  :min-lein-version "2.0.0"

  :source-paths ["src/clj"]
  :test-paths ["test/clj"]
  :resource-paths ["resources"]
  :target-path "target/%s/"
  :main ^:skip-aot hatredify.core

  :plugins [[lein-immutant "2.1.0"]]

  :profiles
  {:uberjar {:omit-source true
             :aot :all
             :uberjar-name "hatredify.jar"
             :source-paths ["env/prod/clj"]
             :resource-paths ["env/prod/resources"]
             :uberjar-exclusions [#"resources/data"]}

   :dev           [:project/dev :profiles/dev]
   :test          [:project/dev :project/test :profiles/test]

   :project/dev  {:jvm-opts ["-Dconf=dev-config.edn"]
                  :dependencies [[expound "0.7.1"]
                                 [pjstadig/humane-test-output "0.8.3"]
                                 [prone "1.6.0"]
                                 [ring/ring-devel "1.6.3"]
                                 [ring/ring-mock "0.3.2"]
                                 [kerodon "0.9.0"]
                                 [criterium "0.4.4"]]
                  :plugins      [[com.jakemccrary/lein-test-refresh "0.23.0"]
                                 [lein-exec "0.3.7"]]
                  :source-paths ["env/dev/clj" "benchmarking"]
                  :resource-paths ["env/dev/resources"]
                  :repl-options {:init-ns user}
                  :injections [(require 'pjstadig.humane-test-output)
                               (pjstadig.humane-test-output/activate!)]
                  :aliases {"benchmark" ["exec" "-p" "benchmarking/core.clj"]}}
   :project/test {:jvm-opts ["-Dconf=test-config.edn"]
                  :resource-paths ["env/test/resources"]}
   :profiles/dev {}
   :profiles/test {}})
