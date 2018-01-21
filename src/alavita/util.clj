;; Copyright 2017 StreamBright LLC and contributors

;; See LICENSE

(ns ^{	:doc "alavita :: util"
				:author "Istvan Szukacs"			}
	alavita.util
	(:import
		[java.util	UUID							]
		[java.text	SimpleDateFormat	]
		[java.time	Instant						]
		[java.util	Date							] )
	)

(defn get-utc-string-of-now
	"Return a string of UTC now"
	[]
	(.format
		(SimpleDateFormat. "yyyy_MM_dd_HH_mm")
		(Date/from (Instant/now))))

(defn uuid
	"Returns a new java.util.UUID as string"
	[]
	(str (UUID/randomUUID)))

(defn exit
	[n]
	(System/exit n))
