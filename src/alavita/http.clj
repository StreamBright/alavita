;; Copyright 2017 StreamBright LLC and contributors

;; See LICENSE

(ns ^{  :doc "alavita :: http"
        :author "Istvan Szukacs"      }
  alavita.http
  (:require
    [alavita.http-handlers      :as handlers    ]
    [clojure.tools.logging      :as log         ] )
  (:import
    [org.rapidoid.http    Req ReqHandler  ]
    [org.rapidoid.config  Conf            ]
    [org.rapidoid.setup   On OnRoute      ] ) )

(defn set-http-params!
  []
  (do
   (.set Conf/HTTP "serverName" "lofasz")
   (.set Conf/HTTP "maxPipeline" 128)))

(defn as-function
  ^ReqHandler [f]
  (reify ReqHandler
    (execute [this ^Object req] (f req))))

(defn get-route
  ^OnRoute [^String path]
  (On/get path))

(defn set-managed!
  ^OnRoute [^OnRoute route managed?]
  (.managed route managed?))

(defn start
  []
  (let [route-echo  (get-route "/echo")
        _           (set-managed! route-echo true)
        route-hi    (get-route "/hi")
        _           (set-managed! route-hi true)
        ]
    (set-http-params!)
    (.json route-echo (as-function handlers/jsonhandler))
    (.plain route-hi handlers/plainhandler)))
