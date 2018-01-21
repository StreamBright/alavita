;; Copyright 2017 StreamBright LLC and contributors

;; See LICENSE

(ns ^{  :doc "alavita :: core"
        :author "Istvan Szukacs"      }
  alavita.core
  (:require
    [alavita.cli              :as     cli     ]
    [alavita.util             :as     utl     ]
    [alavita.dao              :as     dao     ]
    [alavita.disk-usage       :as     du      ]
    [alavita.http             :as     http    ]
    [clojure.tools.logging    :as     log     ]
    [clojure.java.io          :as     io      ]
    [clojure.string           :as     strg    ]
    )
  (:import
    )
  (:gen-class))

(defn create-disk-usage-table
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
        table-name                                  (str "entries_" (utl/get-utc-string-of-now))
        table-maybe-created                         (cond
                                                      (:ok (create-disk-usage-table db table-name))
                                                        (log/info (str "Table " table-name " has been successfully created"))
                                                      :else
                                                        (do
                                                          (log/error (str "Could not create table " table-name))
                                                          (utl/exit 1)
                                                          ))

        ;SQLiteException [SQLITE_ERROR] SQL error or missing database (table entries already exists)
        ;org.sqlite.core.DB.newSQLException (DB.java:909)

        insert-query                                (str "INSERT INTO "
                                                         table-name
                                                         "(path, entry, is_file, size) VALUES (?, ?, ?, ?)")

        ]
    ; main entry point for execution
    (log/info (str ":ok" " env " env))

    (doseq [f (du/files "/etc/")]
      (let [file-name       (du/get-file-name f)
            split-file-name (du/split-file-path file-name)]
        (dao/insert-into-table
          db
          insert-query
          (list (first split-file-name) (second split-file-name) (du/is-file? f) (du/get-file-size f)))))
    (http/start)
    (Thread/sleep 500000)
    (log/info "init :: stop"))
    (System/exit 0))

