;; Copyright 2017 StreamBright LLC and contributors

;; See LICENSE

(ns ^{  :doc "alavita :: core"
        :author "Istvan Szukacs"      }
  alavita.dao
  (:require
    [clojure.tools.logging    :as     log     ]
    [clojure.java.io          :as     io      ]
  )
  (:import
    [org.jdbi.v3.core           Jdbi Handle ]
    [org.jdbi.v3.core.statement Query       ]
  ))

(defn create
  (^Jdbi [^String url]
    (Jdbi/create url))
  (^Jdbi [^String url ^String username ^String password]
    (Jdbi/create url username password)))

(defn open
  ^Handle [^Jdbi jdbi]
  (.open jdbi))

(defn create-query
  ^Query [^Handle h ^String s]
  (.createQuery h s))

(defn execute
  [^Handle h, ^String sql, & args]
  (.execute h sql (into-array Object args)))



