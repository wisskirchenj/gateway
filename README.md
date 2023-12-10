# Cluster-Gateway

Spring Cloud gateway MVC application, that implements a login and register server as Open-ID connect hub to web-services in
the cloud. Spring security's Oauth2-client is used for the OpenId-Login (OIDC) and Spring Webflux is
used to serve a register endpoint (see below).

## Technology / External Libraries

- Java 21
- Spring Cloud Gateway Mvc 4.1.0
- Spring Boot 3.2 with Oauth-client
- Spring AOT native image on GraalVM
- Gradle 8.5

## Program description

Native image Cloud Gateway app based also on Spring-Oauth2Client for OIDC login on authorization_code workflow
and token relay, that is supposed to run in our kubernetes cluster to gateway incoming requests to their destination
services. 

After logging in once, the user is able to consume all connected service routes via Single-Sign-On. Also
a scope consent page is displayed to a user after login.

As Oauth2-provider we use the new Spring
Oauth2-AuthorizationServer, that also does the user registry and persistent User management. 

## Gateway routes so far

> **recipe-service** under `/recipe`, that is token-relayed and prefixed by `"/api"`.
>
> **greeting-service** under `/hello` that is just some hello-world service greeting the user by her principal.
>
> more coming up soon

To register a new user, the Webflux served endpoint 

> POST /api/register (**unauthenticated**)
> ```
> {
>  "email": "user@test.com",
>  "password": "password"
> }

is used.
While this must currently be done vie `curl`, `http`(ie) or Postman, we will soon provide a Thymeleaf
based register form.

## Project status

Project started on 26.06.23

## Progress

26.06.23 Initial setup

01.07.23 Minimal working gateway and Oauth2-client setup, that serves two routes:

02.07.23 First functional complete version, where user registration is persisted by this Gateway app (rudimentary 
frontend still to come up), while the connected (by redirect-uri) Spring authorization server uses
this info via the Postgres-service to authenticate OIDC-login requests.

09.12.23 Migrated project to use new 2023.0.0 Spring Cloud Gateway Mvc together with Spring Boot 3.2.
Migration effort mainly was 
- rewriting the GatewayConfig, 
- adapt the WebSecurityConfig from Webflux to Mvc and 
- replacing the `WebTestClient` with `MockMvc` in the integration tests.

Faced problems were some CGLIB inconsistency with Spring framework 6.1, that prevented the use of @MockBean in
test-AOT as well as some proxy generation in processAOT steps. Also I ended up disabling Spring Boot Devtools
as they prevented stable application startup.