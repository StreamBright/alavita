;; Copyright 2017 StreamBright LLC and contributors

;; See LICENSE

(ns ^{  :doc "alavita :: disk-usage"
        :author "Istvan Szukacs"      }
  alavita.disk-usage
  (:require
    [clojure.java.io          :as     io      ]
    [clojure.string           :as     strg    ]
    )
  (:import
    [java.io    File              ] ) )

(defn files
  [path]
  (file-seq (io/file path)))

(defn get-file-name
  [^File file]
  (.getAbsolutePath file))

(defn get-file-names
  [files]
  (map get-file-name files))

(defn get-file-size
  [^File file]
  (.length file))

(defn is-file?
  [^File file]
  (if (.isFile file) 1 0))

(defn split-file-path
  [file-name]
  (let [splt (strg/split file-name #"(?<=/)")]
    [(apply str (drop-last splt)) (last splt)]))

