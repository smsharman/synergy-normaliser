(ns leiningen.new.synergy-normaliser
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files]]
            [leiningen.core.main :as main]))

(def render (renderer "synergy-normaliser"))

(defn synergy-normaliser
  "Generates Synergy Normaliser template"
  [name]
  (let [data {:name name
              :sanitized (name-to-path name)}]
    (main/info "Generating fresh 'lein new' synergy-normaliser project.")
    (->files data
             ["README.md" (render "README.md" data)]
             [".gitignore" (render ".gitignore" data)]
             ["src/{{sanitized}}/core.clj" (render "core.clj" data)]
             ["resources/deployFunction.sh" (render "deployFunction.sh" data)]
             ["resources/updateFunction.sh" (render "updateFunction.sh" data)]
             ["test/{{sanitized}}/core_test.clj" (render "test.clj" data)]
             ["project.clj" (render "project.clj" data)])))
