;; Copyright 2017 StreamBright LLC and contributors

;; See LICENSE

(ns ^{	:doc "alavita :: echo-http-handler"
				:author "Istvan Szukacs"			}
	alavita.http-handlers
	(:import
		[org.rapidoid.http	Req ReqHandler	]
		[org.rapidoid.u			U								] )
	(:gen-class
		:methods
		 [
			^:static [json-handler [org.rapidoid.http.Req] java.util.Map]
			^:static [plain-handler [] java.lang.String]
			]))

(defn create-request-handler
	[h]
	(reify ReqHandler
		(execute
			[this ^Req req]
				(h req))))

(reify ReqHandler
 (execute [this, ^Req req]
	 ;; here, access req's fields to write the body or headers
	 ))

(defn json-handler
	[^Req req]
	"{\"ok\": \"ok\"}")
	(U/map (.headers req)))


(defn plain-handler
	[]
	"{\"ok\": \"ok\"}")

