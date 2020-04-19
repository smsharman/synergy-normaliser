(ns {{name}}.core
  (:require [uswitch.lambada.core :refer [deflambdafn]]
            [clojure.data.json :as json]
            [clojure.java.io :as io]
            [cognitect.aws.client.api :as aws]
            [synergy-specs.events :as synspec]
            [clojure.spec.alpha :as s]
            [synergy-events-stdlib.core :as stdlib]
            [taoensso.timbre :as timbre
             :refer [log trace debug info warn error fatal report
                     logf tracef debugf infof warnf errorf fatalf reportf
                     spy get-env]])
  (:gen-class))

(def sns (aws/client {:api :sns}))

(def ssm (aws/client {:api :ssm}))

(def snsArnPrefix (atom ""))

(def eventStoreTopic (atom ""))

;; This topic is the topic to which normalised messages should be delivered
(def deliveryTopic "")

;; Event specific processing - take in a map which is the extracted input event (regardless of source)
;; and emit a map which is a non-namespaced Synergy standard event. This will be checked and dispatched to the
;; output topic
(defn generate-new-event [input-map]
  input-map)

(defn process-event [event-content event-type]
  (if (empty? @snsArnPrefix)
    (stdlib/set-up-topic-table snsArnPrefix eventStoreTopic ssm))
  (let [jevent (json/read-str event-content :key-fn keyword)
        tevent (generate-new-event jevent)
        wevent (synspec/wrap-std-event tevent)]
    (info "JSON transformed event : " jevent)
    (info "Transformed event : " tevent)
    (info "Synergy namespaced event : " wevent)
    (if (true? (get (stdlib/validate-message wevent) :status))
      (stdlib/send-to-topic deliveryTopic tevent @snsArnPrefix sns)
      (info "VALIDATION FAILED"))))

(defn handle-event [event]
  (info "Raw event: " (print-str event))
  (let [deduced-type (stdlib/check-event-type event)
        event-content (stdlib/get-event-data event deduced-type)]
  (process-event event-content deduced-type)))


(deflambdafn {{name}}.core.Route
             [in out ctx]
             "Takes a JSON event in standard Synergy Event form from the Message field, convert to map and send to routing function"
             (let [event (json/read (io/reader in) :key-fn keyword)
                   res (handle-event event)]
               (with-open [w (io/writer out)]
                 (json/write res w))))
