# Wishlist (Lista de Desejos)

Objetivo do projeto é demostrar o uso do Spring, Webflux e MongoDB.

Link para o Swagger: http://localhost:8088/swagger 

## Build
Para realizar o build é necessario obter o JDK 17 e Gradle 8+

Tasks disponiveis:

| Task                    | Descrição                                    |
|-------------------------|----------------------------------------------|   
| gradew build            | Realiza o build completo da aplicação        |
| gradle test             | Executa os testes unitários da aplicação     |
| gradle jacocoTestReport | Sumariza o relatório de cobertura dos testes |
| gradle bootRun          | Executa a aplicação                          |


### Esse serviço atende os seguintes requisitos:
- Adicionar um produto na Wishlist do cliente;
- Remover um produto da Wishlist do cliente;
- Consultar todos os produtos da Wishlist do cliente;
- Consultar se um determinado produto está na Wishlist do cliente;


## Execução Local
Para rodar a aplicação de forma local basta executar comando:

``gradlew bootRun``

## Executar no Docker
Executar o comando abaixo, após realizar o **build** da aplicação.

``docker-compose up`` ou ``docker-compose up --build --force-recreate`` para forçar sempre um novo build.

---
# Pagina Inicial das Aplicações no Docker

Para facilitar o acesso as aplicações basta acessar o endereço: http://localhost:3333/ 

Ele possui os links para acesso a todas as aplicações.


## MongoDB

O Acesso ao mongo db esta disponivel atraves do host **127.0.0.1** porta **27017**

Ou utilizar o Mongo Express disponivel no endereço: http://127.0.0.1:8081

## Grafana e Prometheus

Para acessar dashboard basta acessar o endereço abaixo: http://127.0.0.1:3000 e o Prometheus  http://127.0.0.1:9090 


## Observações e Considerações

1. ID's do produto e do cliente deixei como NUMÉRICO, supondo que já existia em algum outro sistema ou base de dados legada.
2. Nos testes decidi utilizar o block, não achei interessante e pratico realizar os testes de forma sincrona.
3. As métricas são **apenas exemplo**, demostrando que podemos combinar o actuator e o prometheus para gerar novos dados.
4. Durante os testes unitários prefiro evitar o uso de mock's para realmente validar a execução dos serviços
5. Componentes e Ferramentas utilizadas:

| <!-- -->                      | <!-- -->                     |
|-------------------------------|------------------------------|
| IntelliJ IDEA                 | Docker Desktop               |
| Java Eclipse Temurin™ 17.0.11 | Spring Boot                  |
| MongoDB                       | Spring WebFlux               |
| Spring Actuator               | Grafana                      |
| Prometheus                    | Nginx (Report Coverage Site) |

<font color=orange>** Trabalhei pouco com webflux, mas acredito que consegui atigir o objetivo proposto.</font>
