;; Copyright © 2017 StreamBright LLC and contributors

;; See LICENSE

(defproject alavita "0.1.0"
  :description "HDFS disk usage reporting"
  :url "https://github.com/StreamBright/alavita"
  :license {  :name "MIT"
              :url  "https://mit-license.org/"  }
  :dependencies [
    [org.clojure/clojure                "1.8.0"           ]
    [org.clojure/tools.cli              "0.3.5"           ]
    [org.clojure/tools.logging          "0.4.0"           ]
    [org.slf4j/slf4j-log4j12            "1.7.12"          ]
    [log4j/log4j                        "1.2.17"          ]
    [org.apache.hadoop/hadoop-hdfs      "2.6.0-cdh5.8.5"  ]
    [org.jdbi/jdbi3-core                "3.0.0"           ]
    [org.xerial/sqlite-jdbc             "3.21.0"          ]
    [org.rapidoid/rapidoid-http-server  "5.5.3"
      :exclusions [
        org.rapidoid/rapidoid-sql
        org.rapidoid/rapidoid-watch
        org.rapidoid/rapidoid-http-client
        org.rapidoid/hibernate-entitymanager
      ]
    ]
  ]
  :repositories {"cloudera" {  :name "cloudera"
                          :url "https://repository.cloudera.com/content/repositories/releases/" }

                 "maven" {     :name "maven"
                          :url "https://repo.maven.apache.org/maven2/" } }
  :exclusions [
    javax.mail/mail
    javax.jms/jms
    com.sun.jdmk/jmxtools
    com.sun.jmx/jmxri
    jline/jline
  ]
  :profiles {:uberjar {:aot :all}}
  :jvm-opts [
    "-Xms128m" "-Xmx256m"
    "-server" "-XX:+UseConcMarkSweepGC"
    "-XX:+TieredCompilation" "-XX:+AggressiveOpts"
    ;"-Dcom.sun.management.jmxremote"
    ;"-Dcom.sun.management.jmxremote.port=8888"
    ;"-Dcom.sun.management.jmxremote.local.only=false"
    ;"-Dcom.sun.management.jmxremote.authenticate=false"
    ;"-Dcom.sun.management.jmxremote.ssl=false"
    ;"-XX:+UnlockCommercialFeatures" "-XX:+FlightRecorder"
    ;"-XX:StartFlightRecording=duration=60s,filename=myrecording.jfr"
    ;"-Xprof" "-Xrunhprof"
  ]
  :repl-options {:init-ns alavita.core}
  :main alavita.core)
