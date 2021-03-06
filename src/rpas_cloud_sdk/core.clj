(ns rpas-cloud-sdk.core
  (:gen-class)
  (:require [clj-http.client :as http]
            [clojure.data.json :as json]))

(def rpas-request-headers (atom {}))
(def simple-access-token (atom ""))
(def global-options (atom {}))
(def base-uri "http://54.214.50.90")

(def resources {:data-sources "data_sources"
                :taxonomies "taxonomies"
                :research-types "research_types"
                :rated-items "rated_items"
                :disclosures "disclosures"
                :documents "documents"
                :lists "lists"
                :products "products"
                :workflows "workflows"
                :roles "roles"
                :users "users"})

(defn http-request-options
  ([]
   {:headers @rpas-request-headers}))

(defn response
  [http-response]
  (let [format (@global-options :response-format)]
    (if (or 
      (not format) 
      (= :clojure format))
        (json/read-str (http-response :body) 
                       :keyword-fn keyword)
      (if (= :json format)
          (http-response :body)
          nil))))

;Should be expanded. alert *base-uri* with appropriate version path once domain is purchased
(defn config
  [opts]
  (reset! global-options opts)
  (if 
    (@global-options :token)
  (swap! rpas-request-headers conj {"x-its-rpas" (@global-options :token)
                                      "x-rpas-api-version" (@global-options :api-version)})
  (if 
      (and 
        (@global-options :username) 
        (@global-options :password))
      (let [{:keys [username password]} @global-options
            request-token (((response (http/post 
              (clojure.string/join "/" [base-uri "authenticate"])
              {:basic-auth [username password]})) "Auth") "RequestToken")]
        (swap! rpas-request-headers conj {"x-its-rpas" request-token
                                          "x-rpas-api-version" (@global-options :api-version)}))
      (throw (Exception. "Invalid RPAS Cloud Credentials")))))

(defn -compose-request-uri
  ([resource-key & params]
   (clojure.string/join "/" [base-uri (resources resource-key) 
                             (or (apply str params) "")])))

(defn -set-query [query] (or query {}))

(defn compose-request
  ([http-method resource-key opts]
   ((symbol "clj-http.client" (str http-method))
    (-compose-request-uri (keyword resource-key (or (opts :uri-params) "")))
    {:headers (or (conj @rpas-request-headers (opts :headers)))})))

(defn get-data-sources
  ([]
   (response (http/get (-compose-request-uri :data-sources) (http-request-options))))
  ([id]
   (response (http/get (-compose-request-uri :data-sources id) (http-request-options))))
  ([id query]
   (response (http/get (-compose-request-uri :data-sources id)
             {:headers @rpas-request-headers
              :query-params (-set-query query)}))))

(defn get-rated-items
  ([]
   (response (http/get (-compose-request-uri :rated-items) (http-request-options))))
  ([query]
   (response
     (if (string? query)
       (http/get (-compose-request-uri :rated-items query) 
                 {:headers @rpas-request-headers})
     
       (http/get (-compose-request-uri :rated-items) 
                 {:headers @rpas-request-headers
                  :query-params (-set-query query)}))))
  ([id query]
   (response (http/get (-compose-request-uri :rated-items id)
             {:headers @rpas-request-headers
              :query-params (-set-query query)}))))

(defn get-research-types
  ([]
   (response (http/get (-compose-request-uri :research-types) 
             {:headers @rpas-request-headers})))
  ([id]
   (if (string? id)
     (response (http/get (-compose-request-uri :research-types id)
             {:headers @rpas-request-headers}))
     (response (http/get (-compose-request-uri :research-types)
             {:headers @rpas-request-headers
              :query-params (-set-query id)}))))
  ([id query]
   (response (http/get (-compose-request-uri :research-types id)
             {:headers @rpas-request-headers
              :query-params (-set-query query)}))))

(defn get-taxonomies
  ([]
   (response (http/get (-compose-request-uri :taxonomies) 
             {:headers @rpas-request-headers})))
  ([id]
   (if (string? id)
     (response (http/get (-compose-request-uri :taxonomies id)
             {:headers @rpas-request-headers}))
     (response (http/get (-compose-request-uri :taxonomies)
             {:headers @rpas-request-headers
              :query-params (-set-query id)}))))
  ([id query]
   (response (http/get (-compose-request-uri :taxonomies id)
             {:headers @rpas-request-headers
              :query-params (-set-query query)}))))

(defn get-workflows
  ([]
   (response (http/get (-compose-request-uri :workflows)
             {:headers @rpas-request-headers})))
  ([id]
    (if (string? id)
     (response (http/get (-compose-request-uri :workflows id)
             {:headers @rpas-request-headers}))
     (response (http/get (-compose-request-uri :workflows)
             {:headers @rpas-request-headers
              :query-params (-set-query id)}))))
  ([id query]
   (response (http/get (-compose-request-uri :workflows id)
             {:headers @rpas-request-headers
              :query-params (-set-query query)}))))

(defn get-lists
  ([]
   (response (http/get (-compose-request-uri :lists)
             {:headers @rpas-request-headers})))
  ([id]
   (if (string? id)
     (response (http/get (-compose-request-uri :lists id)
             {:headers @rpas-request-headers}))
     (response (http/get (-compose-request-uri :lists)
             {:headers @rpas-request-headers
              :query-params (-set-query id)}))))
  ([id query]
   (response (http/get (-compose-request-uri :lists id)
             {:headers @rpas-request-headers
              :query-params (-set-query query)}))))

(defn get-products
  ([]
   (response (http/get (-compose-request-uri :products)
             {:headers @rpas-request-headers})))
  ([id]
   (if (string? id)
     (response (http/get (-compose-request-uri :products id)
             {:headers @rpas-request-headers}))
     (response (http/get (-compose-request-uri :products)
             {:headers @rpas-request-headers
              :query-params (-set-query id)}))))
  ([id query]
   (response (http/get (-compose-request-uri :products id)
             {:headers @rpas-request-headers
              :query-params (-set-query query)}))))

(defn get-users
  ([]
   (response (http/get (-compose-request-uri :users)
             {:headers @rpas-request-headers})))
  ([id]
   (if (string? id)
     (response (http/get (-compose-request-uri :users id)
             {:headers @rpas-request-headers}))
     (response (http/get (-compose-request-uri :users)
             {:headers @rpas-request-headers
              :query-params (-set-query id)}))))
  ([id query]
   (response (http/get (-compose-request-uri :users id)
             {:headers @rpas-request-headers
              :query-params (-set-query query)}))))

(defn get-roles
  ([]
   (response (http/get (-compose-request-uri :roles)
             {:headers @rpas-request-headers})))
  ([id]
   (if (string? id)
     (response (http/get (-compose-request-uri :roles id)
             {:headers @rpas-request-headers}))
     (response (http/get (-compose-request-uri :roles)
             {:headers @rpas-request-headers
              :query-params (-set-query id)}))))
  ([id query]
   (response (http/get (-compose-request-uri :roles id)
             {:headers @rpas-request-headers
              :query-params (-set-query query)}))))

(defn get-disclosures
  ([]
   (response (http/get (-compose-request-uri :disclosures)
             {:headers @rpas-request-headers})))
  ([id]
   (if (string? id)
     (response (http/get (-compose-request-uri :disclosures id)
             {:headers @rpas-request-headers}))
     (response (http/get (-compose-request-uri :disclosures)
             {:headers @rpas-request-headers
              :query-params (-set-query id)}))))
  ([id query]
   (response (http/get (-compose-request-uri :disclosures id)
             {:headers @rpas-request-headers
              :query-params (-set-query query)}))))

(defn get-documents
  ([]
   (response (http/get (-compose-request-uri :documents) 
             {:headers @rpas-request-headers})))
  ([id]
   (if (string? id)
     (response (http/get (-compose-request-uri :documents id)
             {:headers @rpas-request-headers}))
     (response (http/get (-compose-request-uri :documents)
             {:headers @rpas-request-headers
              :query-params (-set-query id)}))))
  ([id query]
   (response (http/get (-compose-request-uri :documents id)
             {:headers @rpas-request-headers
              :query-params (-set-query query)}))))

(defn -main
  [& args]
  (comment "Quick test. Run \"lein run\"")
  (config {:username "client_100_admin"
           :password "13705754"
           :api-version "v1"})
  (println @global-options)
  (->> (get-rated-items "537b62e6edff55ab0f9dd7c6" {:q "something:else"}) (println)))