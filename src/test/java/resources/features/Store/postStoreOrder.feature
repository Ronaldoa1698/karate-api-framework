@retoOka
Feature: Validar el servicio Get Store Inventory

    Background:
        * configure ssl = true
        * url BaseUrl
        * path '/order'
        * header accept = Accept
        * header Content-Type = Accept

        * def requestOrder = read('classpath:resources/model/order/orderRequest.model.json')
    
    @smokePostOrder
    Scenario: Validar el servicio Post crear orden response 200
        Given request requestOrder 
        When method Post 
        Then status 200
        * karate.pause(6000)
    
    @PostOrder200
    Scenario Outline: Validar el servicio Post crear orden con response 200
        Given request <requestBody>
        When method Post
        Then status 200
        Examples:
          | requestBody                                                                 |
          | { "petId": 1, "quantity": 2, "shipDate": "2025-11-05T03:18:02.466Z", "status": "placed", "complete": true } |
          | { "petId": 3, "quantity": 5, "shipDate": "2024-06-15T10:30:00.000Z", "status": "approved", "complete": false } |
          | { "petId": 7, "quantity": 1, "shipDate": "2024-12-20T15:45:30.123Z", "status": "delivered", "complete": true } |

    @validatePostOrderResponse400
    Scenario: Validar el servicio Post crear orden con response 400
        Given request 
        When method Post
        Then status 400

    @validatePostOrderResponse404
    Scenario: Validar el servicio Post crear orden con response 404
        Given path 'https://petstore.swagger.io/v2/store/orderInvalid'
        When method Post
        Then status 404
    
    @validatePostOrderResponse500
    Scenario: Validar el servicio Post crear orden con response 500
        Given request { "petId": "-1.2", "quantity": 2, "shipDate": "2025-11-05T03:18:02.466+0000", "status": "prueba","complete": true }
        When method Post
        Then status 500
