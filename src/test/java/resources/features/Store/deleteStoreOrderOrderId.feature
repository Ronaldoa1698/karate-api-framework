@retoOka
Feature: Validar el servicio Delete Store Order por OrderId

    Background:
        * configure ssl = true
        * url BaseUrl
        * header accept = Accept
        * header Content-Type = Accept

        * def crearOrder = callonce read('classpath:resources/features/Store/postStoreOrder.feature@smokePostOrder')
        * def orderId = crearOrder.response.id
        * karate.pause(6000)

    @smokeDeleteStoreOrderOrderId
    Scenario: Validar el servicio Delete Store Order por OrderId
        * karate.pause(8000)
        
        Given path '/order', orderId
        When method Delete
        Then status 200 || 404
        
        * karate.pause(8000)
        Given path '/order', orderId
        When method Get
        Then status 404
        * print "aca2" + orderId

    @deleteStoreOrderOrderId404
    Scenario: Validar el servicio Delete Store Order por OrderId con response 404
        Given path 'https://petstore.swagger.io/v2/store/orderInvalid'
        When method Delete
        Then status 404
    