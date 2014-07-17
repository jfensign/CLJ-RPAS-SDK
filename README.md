CLJ-RPAS-SDK
============

A simple RPAS REST Client for the JVM implemented in Clojure.



<h3>Installation</h3>
<p><code>rpas-cloud-sdk</code> is available on <a href="https://clojars.org/rpas-cloud-sdk">Clojars</a></p>

Artifacts are [released to
Clojars](https://clojars.org/jfensign/rpas-cloud-sdk). If you are using
Maven, add the following repository definition to your `pom.xml`:

``` xml
<repository>
  <id>clojars.org</id>
  <url>http://clojars.org/repo</url>
</repository>
```


With Lein

    [rpas-cloud-sdk "0.1.0-SNAPSHOT"]

With Maven

    <dependency>
      <groupId>rpas-cloud-sdk</groupId>
      <artifactId>rpas-cloud-sdk</artifactId>
      <version>0.1.0-SNAPSHOT</version>
    </dependency>


With Gradle

    compile "rpas-cloud-sdk:rpas-cloud-sdk:0.1.0-SNAPSHOT"
    
    
    
<h3>Usage</h3>

<b>First require it in your application</b>

    (ns my-app.core
      (:require [rpas-cloud-sdk.core :as rpas]))
      
<b>Or the REPL</b>

    (require '[rpas-cloud-sdk.core :as rpas]')
    
<b>Configure the SDK</b>

Using simple token based authentication and returning responses as hash maps

    (rpas/config {:token "RPAS Cloud request-token" 
                  :api-verson "v1"
                  :response-format :clojure})
    

Using credential based authentication and returning responses as JSON

    (rpas/config {:username "RPAS Cloud username"
            :password "RPAS Cloud password" 
                  :api-verson "v1"
                  :response-format :json})

<b>Use the SDK to easily access RPAS resources</b>

Listing resources

    ;basic list
    (rpas/get-rated-items)
    ----------------------
    ;with query parameters
    (rpas/get-rated-items {:q "datapoint1:datapoint2value,datapoint2:datapoint2value"
                           :expand "*"
                           :offset 20
                           :rows 80})

Selecting resources

    ;basic select
    (rpas/get-rated-items "RatedItemID")
    ------------------------------------
    ;with a querystring
    (rpas/get-rated-items "RatedItemID" {:q "datapoint1:datapoint2value,datapoint2:datapoint2value"
                                         :expand "*"
                                         :offset 20
                                         :rows 80})


<h3>Example Web Application</h3>

    ;include libraries
    (ns app.handler
      (:require [compojure.core :refer :all]
                [compojure.handler :as handler]
                [compojure.route :as route]
                [ring.util.response :refer [resource-response response]]
                [ring.middleware.json :as middleware]
                [rpas-cloud-sdk.core :as rpas]))
    ;configure the SDK
    (rpas/config {:username "RPAS USERNAME" 
                  :password "RPAS PASSWORD"
                  :response-format :clojure
                  :api-version "v1"})
                  
    (defn -ref-sdk-method
      "take the resource parameter and call the appropriate function (i.e. the resource 'taxonomies' maps to get-taxonomies'"
      [resource]
      (let [fun (symbol (clojure.string/join "-" ["get" resource]))
            name-sp (symbol "rpas-cloud-sdk.core")
            call (ns-resolve name-sp fun)]
        call))
        
    ;define routes
    (defroutes app-routes
    ;Returns listed resources
      (GET "/sdk/:api" {query :query-params {api :api} :params} 
        (response 
            (let [call (-ref-sdk-method api)]
              ;call rpas sdk function and apply query
              (call query))))
        ;Returns resources selected by ID
      (GET "/sdk/:api/:id" {query :query-params {api :api id :id} :params} 
        (response 
            (let [call (-ref-sdk-method api)]
              ;call rpas sdk function and supply as an argument
              (call id)))))
              
    ;initialize your app and define your middleware stack
    (def app
      (-> (handler/api app-routes)
          (middleware/wrap-json-body)
          (middleware/wrap-json-response)))
