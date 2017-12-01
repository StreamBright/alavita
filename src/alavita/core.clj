;; Copyright 2017 StreamBright LLC and contributors

;; See LICENSE

(ns ^{  :doc "alavita :: core"
        :author "Istvan Szukacs"      }
  alavita.core
  (:require
    [alavita.cli              :as     cli     ]
    [alavita.dao              :as     dao     ]
    [clojure.tools.logging    :as     log     ]
    [clojure.java.io          :as     io      ]
    [clojure.string           :as     strg    ]
    )
  (:import
    [java.io    File              ]
    [java.text  SimpleDateFormat  ]
    [java.time  Instant           ]
    [java.util  Date              ] )
  (:gen-class))

(defn get-utc-string
  []
  (.format
    (SimpleDateFormat. "yyyy_MM_dd_HH_mm")
    (Date/from (Instant/now))))

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

(defn -main
  [& args]
  (let [
        ;CLI & Config
        cli-options-parsed                          (cli/process-cli args cli/cli-options)
        {:keys [options arguments errors summary]}  cli-options-parsed
        config                                      (cli/process-config (:config options))
        env                                         (keyword (:env options))
        db                                          (dao/open ;todo move to config
                                                      (dao/create "jdbc:sqlite:/tmp/data.db"))
        table-name                                  (str "entries_" (get-utc-string))
        create-table                                (try
                                                      (dao/execute db
                                                        (str "CREATE TABLE "
                                                           table-name
                                                           "(id INTEGER PRIMARY KEY, "
                                                           "path TEXT, entry TEXT, "
                                                           "is_file INTEGER, size INTEGER)"))
                                                      (catch Exception e (log/error
                                                        "caught exception: "
                                                        (.getMessage e))))
        ;SQLiteException [SQLITE_ERROR] SQL error or missing database (table entries already exists)  org.sqlite.core.DB.newSQLException (DB.java:909)

        insert-query                                (str "INSERT INTO "
                                                         table-name
                                                         "(path, entry, is_file, size) VALUES (?, ?, ?, ?)")

        ]
    ; main entry point for execution
    (log/info (str ":ok" " env " env))

    (doseq [f (files "/tmp/test")]
      (dao/execute db insert-query (get-file-name f) (is-file? f) (get-file-size f)))

    (log/info "init :: stop"))
    (System/exit 0))

