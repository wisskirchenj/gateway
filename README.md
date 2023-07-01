# Cluster-Gateway

Spring Cloud gateway application, that implements a login and register server as Open-ID connect hub to web-services in
the cloud.

## Technology / External Libraries

- Java 20
- Spring Cloud Gateway 4.0.6
- Spring Boot 3.1.1 Oauth-client
- Spring AOT native image on GraalVM
- Gradle 8.1.1

## Program description

Native image Cloud Gateway app based also on Spring-Oauth2Client for OIDC login on authorization_code workflow
and token relay, that is supposed to run in our kubernetes cluster to gateway incoming requests to their destination
services. 

After logging in once, the user is able to consume all connected service routes via Single-Sign-On. Also
a scope consent page is displayed to a user after login.

As Oauth2-provider we use the new Spring
Oauth2-AuthorizationServer, that also does the user registry and persistent User management. 

## Project status

Project started on 26.06.23

## Progress

26.06.23 Initial setup

01.07.23 Minimal working gateway and Oauth2-client setup, that serves two routes:

> **recipe-service** under `/recipe`, that is token-relayed and prefixed by `"/api"`.
>
> **greeting-service** under `/hello` that is just some hello-world service greeting the user by her principal.
>
> more coming up soon