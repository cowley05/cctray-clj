(ns clj-cctray.reader
  "Functions for reading urls."
  (:require [clj-http.client :as client]
            [clojure.string :refer [lower-case]]))

(def ^:dynamic http-client-insecure? "Should we check SSL certificates?" true)
(def ^:dynamic http-client-timeout "How long to wait in seconds before timing out http calls." 30)

(defn http-client-options [] {:insecure? http-client-insecure?
                              :timeout   (* http-client-timeout 1000)
                              :headers   {"Accept" "application/xml"}})

(defn- http-get [url]
  (:body (client/get url (http-client-options))))

(defn http-get->String [url]
  (let [raw-response (http-get url)]
    (java.io.ByteArrayInputStream. (.getBytes raw-response))))

(defn decide-reader [url]
  (if (.startsWith (lower-case url) "http")
    http-get->String
    identity))

(defn xml-reader
  "Returns an xml reader to use based on the format of the given url."
  [url]
  ((decide-reader url) url))
