;; Copyright 2017 StreamBright LLC and contributors

;; See LICENSE

(ns ^{  :doc "alavita :: http"
        :author "Istvan Szukacs"      }
  alavita.http
  (:require
    [alavita.http-handlers      :as handlers    ]
    [clojure.tools.logging      :as log         ] )
  (:import
    [org.rapidoid.config  Conf        ]
    [org.rapidoid.setup   On OnRoute  ] ) )

(defn set-http-params!
  []
  (do
   (.set Conf/HTTP "serverName" "lofasz")
   (.set Conf/HTTP "maxPipeline" 128)))

(defn ^java.util.function.Function as-function [f]
  (reify java.util.function.Function
    (apply [this arg] (f arg))))

(defmacro jfn [& args]
  `(as-function (fn ~@args)))

(defn get-route
  ^OnRoute [^String path]
  (On/get path))

(defn set-managed!
  ^OnRoute [^OnRoute route managed?]
  (.managed route managed?))

(defn start
  []
  (let [route-echo  (get-route "/echo")
        _           (set-managed! route-echo false)
        route-hi    (get-route "/hi")
        _           (set-managed! route-hi false)
        ]
    (set-http-params!)
    (.json route-echo #(handlers/json-handler %))
    (.plain route-hi handlers/plain-handler)))
