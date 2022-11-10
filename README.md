# ConsulTestProject
firs commit

Project need to consul server


In this project I'm demonstrating you how to use Hashicorp's Consul as a discovery and configuration server with Spring Cloud Consul and other Spring Cloud projects for building microservice-based architecture.

$ docker run -d --name consul-test-1 -p 8500:8500 -e CONSUL_BIND_INTERFACE=eth0 consul

$ docker run -d --name consul-agent-2 -e CONSUL_BIND_INTERFACE=eth0 -p 8501:8500 consul agent -dev -join=172.17.0.2

$ docker run -d --name consul-agent-3 -e CONSUL_BIND_INTERFACE=eth0 -p 8502:8500 consul agent -dev -join=172.17.0.2


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
