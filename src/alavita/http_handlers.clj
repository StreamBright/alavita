;; Copyright 2017 StreamBright LLC and contributors

;; See LICENSE

(ns ^{  :doc "alavita :: echo-http-handler"
        :author "Istvan Szukacs"      }
  alavita.http-handlers
  (:import
    [org.rapidoid.http  Req ]
    [org.rapidoid.u     U   ] )
  (:gen-class
    :methods
     [
      ^:static [jsonhandler [org.rapidoid.http.Req] java.util.Map]
      ^:static [plainhandler [] java.lang.String]
      ]))

(defn jsonhandler
  [^Req req]
  (U/map (.headers req)))

(defn plainhandler
  []
  "{\"ok\": \"ok\"}")
