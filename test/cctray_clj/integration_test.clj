(ns cctray-clj.integration-test
  (:require [cctray-clj.parser :as subject]
            [midje.sweet :refer :all]
            [clojure.java.io :as io]))

(def test-data-url "resources/test_data.xml")

(fact "required test_data.xml file exists"
      (.exists (io/as-file test-data-url)) => true)

(fact "will turn xml to map"
      (first (:content (subject/to-map test-data-url)))
      => (contains {:attrs {:name            "success-sleeping-project :: stage1 :: job1"
                            :activity        "Sleeping"
                            :lastBuildStatus "Success"
                            :lastBuildLabel  "8"
                            :lastBuildTime   "2005-09-28T10:30:34.6362160+01:00"
                            :nextBuildTime   "2005-10-04T14:31:52.4509248+01:00"
                            :webUrl          "http://some-url"}}))

(fact "will create list of projects"
      (first (subject/get-projects test-data-url))
      => {:raw-name        "success-sleeping-project"
          :name            "success sleeping project"
          :stage           "stage1"
          :job             "job1"
          :activity        "Sleeping"
          :prognosis       "healthy"
          :lastBuildStatus "Success"
          :lastBuildLabel  "8"
          :lastBuildTime   "2005-09-28T10:30:34.6362160+01:00"
          :nextBuildTime   "2005-10-04T14:31:52.4509248+01:00"
          :webUrl          "http://some-url"})

