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

First require it in your application

    (ns my-app.core
      (:require [rpas-cloud-sdk.core :as rpas]))
      
Or the REPL

    (require '[rpas-cloud-sdk.core :as rpas]')
    
Configure the SDK

Using simple token based authentication and returning responses as hash maps

    (rpas/config {:token "RPAS Cloud request-token" 
                  :api-verson "v1"
                  :response-format :clojure})
    

Using credential based authentication and returning responses as JSON

    (rpas/config {:username "RPAS Cloud username"
    			  :password "RPAS Cloud password" 
                  :api-verson "v1"
                  :response-format :json})

  