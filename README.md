# ConsulTestProject
firs commit

Project need to consul server


In this project I'm demonstrating you how to use Hashicorp's Consul as a discovery and configuration server with Spring Cloud Consul and other Spring Cloud projects for building microservice-based architecture.

example consul server config file

config/student-service::dev/data

server:
  port: 9977

spring:
  datasource:
    driverClassName: org.h2.Driver
    hikari:
      connection-timeout: '20000 '
      pool-name: school-service-pool-name
      idle-timeout: '10000 '
      maximum-pool-size: '10 '
      max-lifetime: '1000 '
      auto-commit: 'true '
      minimum-idle: '10 '
    password: ''
    username: sa
    url: jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE
  h2:
    console:
      settings:
        trace: 'false'
        web-allow-others: 'false'
      path: /h2-console
      enabled: 'true'
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: 'true'
    database-platform: org.hibernate.dialect.H2Dialect
    

auth:
  app:
    jwtSecretKey: SnijderVurduGolOdu!!00161654
    jwtExpirationMs: '86400001'
