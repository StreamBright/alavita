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
      ^:static [json-handler [org.rapidoid.http.Req] java.util.Map]
      ^:static [plain-handler [] java.lang.String]
      ]))

(defn json-handler
  [^Req req]
  (U/map (.headers req)))

(defn plain-handler
  []
  "{\"ok\": \"ok\"}")

