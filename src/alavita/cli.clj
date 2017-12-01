
(ns ^{  :doc "alavita :: cli"
      :author "Istvan Szukacs"  }
  alavita.cli
  (:require
    [clojure.tools.cli      :as   cli   ]
    [clojure.tools.logging  :as   log   ]
    [clojure.edn            :as   edn   ]
    [clojure.reflect        :as   rfl   ]

    )
  (:import
    [java.io                                File BufferedReader                   ]
    [clojure.lang                           PersistentArrayMap PersistentList     ]
    )
  (:gen-class))

(defn all-methods [obj]
  (->> obj rfl/reflect
    :members
    (filter :return-type)
    (map :name)
    sort
    (map #(str "." %) )
    distinct
    println))

(defn read-file
  "Returns {:ok string } or {:error...}"
  ^PersistentArrayMap [^File file]
  (try
    (cond
      (.isFile file)
      {:ok (slurp file) }
      :else
      (throw (Exception. "Input is not a file")))
    (catch Exception e
      {:error "Exception" :fn "read-file" :exception (.getMessage e) })))

(defn parse-edn-string
  "Returns {:ok {} } or {:error...}"
  ^PersistentArrayMap [^String s]
  (try
    {:ok (edn/read-string s)}
    (catch Exception e
      {:error "Exception" :fn "parse-config" :exception (.getMessage e)})))


(defn read-config
  "Reads the configuration file (app.edn) and returns the config as a hashmap"
  ^PersistentArrayMap [^String path]
  (let
    [ file-string (read-file (File. path)) ]
    (cond
      (contains? file-string :ok)
      ;if the file read is successful the content is sent to parse-edn-string
      ;that can return either and {:ok ...} or {:error ...}
      (parse-edn-string (file-string :ok))
      :else
      ;keeping the original error and let it fall through
      file-string)))

(defn exit
  ([^Long n]
   (log/info "init :: stop")
   (System/exit n))
  ([^Long n ^String msg]
   (log/info msg)
   (log/info "init :: stop")
   (System/exit n)))

(defn process-config
  "Processing config with error handling"
  [file]
  ; Handle help and error conditions
  (let [config (read-config file)]
    (cond
      (or (empty? config) (:error config))
      (exit 1 (str "Config cannot be read or parsed..." "\n" config))
      :else
      config)))

(def cli-options
  ;; An option with a required argument
  [
   ["-c" "--config CONFIG" "Config file name"
    :default "conf/app.edn"]
   ["-e" "--env ENV" "Environment (dev or prod)"
    :default "dev"]
   ["-h" "--help"]
   ])

(defn process-cli
  "Processing the cli arguments and options"
  [args cli-options]
  (let [
        cli-options-parsed (cli/parse-opts args cli-options)
        {:keys [options arguments errors summary]} cli-options-parsed
        ]
    (cond
      (:help options)
      (do
        (log/info (str "Help: \n" summary))
        (exit 0))
      errors
      (do
        (log/error errors)
        (exit 1))
      :else
      cli-options-parsed)))
