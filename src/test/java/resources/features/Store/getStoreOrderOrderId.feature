@retoOka
Feature: Validar el servicio Get Store Order por OrderId

    Background:
        * configure ssl = true
        * url BaseUrl
        * header accept = Accept
        * header Content-Type = Accept

        * def responseOrder = read('classpath:resources/response/storeInventory.response.json')
        * def crearOrder = callonce read('classpath:resources/features/Store/postStoreOrder.feature@smokePostOrder')
        * def orderId = crearOrder.response.id
        * print "orderId: " + orderId
        * karate.pause(6000)

    @smokeGetStoreOrderOrderId
    Scenario: Validar el servicio Get Store Order por OrderId
        * karate.pause(6000)
        Given path '/order', orderId
        When method Get
        Then status 200
   
    @getStoreOrderOrderId404
    Scenario: Validar el servicio Get Store Order por OrderId con response 404
        Given path 'https://petstore.swagger.io/v2/store/orderInvalid'
        When method Get
        Then status 404
    
    @getStoreOrderValidateResponseSchema
    Scenario: Validar el servicio Get Store Order por OrderId y validar esquema de respuesta
        * karate.pause(6000)
        Given path '/order', orderId
        When method Get
        Then status 200
        And match response == responseOrder
