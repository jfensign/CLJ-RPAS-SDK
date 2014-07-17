(ns webapp.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :refer [resource-response response]]
            [ring.middleware.json :as middleware]
            [rpas-cloud-sdk.core :as rpas]))

(rpas/config {:username "client_100_admin" 
              :password "13705754"
              :response-format :clojure
              :api-version "v1"})

  
(defn -ref-sdk-method
  [resource]
  (let [fun (symbol (clojure.string/join "-" ["get" resource]))
        name-sp (symbol "rpas-cloud-sdk.core")
        call (ns-resolve name-sp fun)]
    call))
  
(defroutes app-routes
  (GET "/sdk/:api" {query :query-params {api :api} :params} 
    (response 
      (let [call (-ref-sdk-method api)] 
        (call query))))
  (GET "/sdk/:api/:id" {query :query-params {api :api id :id} :params} 
    (response 
      (let [call (-ref-sdk-method api)] 
        (call id)))))
  
(def app
  (-> (handler/api app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)))
