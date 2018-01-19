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

(defn create-table
  [db table-name]
  (try
    (dao/execute db
      (str  "CREATE TABLE "
            table-name
            "(id INTEGER PRIMARY KEY, "
            "path TEXT, entry TEXT, "
            "is_file INTEGER, size INTEGER)"))
      {:ok (str "Table " table-name " has been successfully created")}
    (catch Exception e
      {:err "Error while creating table" :msg (.getMessage e)})))

(defn insert-into-table
  [db query data]
  (let [ex (partial dao/execute db query)]
    (apply ex data)))

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
        table-maybe-created                         (cond
                                                      (:ok (create-table db table-name))
                                                        (log/info (str "Table " table-name " has been successfully created"))
                                                      :else
                                                        (log/error (str "Could not create table " table-name)))

        ;SQLiteException [SQLITE_ERROR] SQL error or missing database (table entries already exists)
        ;org.sqlite.core.DB.newSQLException (DB.java:909)

        insert-query                                (str "INSERT INTO "
                                                         table-name
                                                         "(path, entry, is_file, size) VALUES (?, ?, ?, ?)")

        ]
    ; main entry point for execution
    (log/info (str ":ok" " env " env))

    (doseq [f (files "/etc/")]
      (let [file-name       (get-file-name f)
            split-file-name (split-file-path file-name)]
        (insert-into-table db insert-query  (list (first split-file-name) (second split-file-name) (is-file? f) (get-file-size f)))))

    (log/info "init :: stop"))
    (System/exit 0))

