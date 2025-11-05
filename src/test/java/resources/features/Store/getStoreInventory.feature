@retoOka
Feature: Validar el servicio Get Store Inventory

    Background:
        * configure ssl = true
        * url BaseUrl
        * path '/inventory'
        * header accept = Accept

    @smokeGetInventory
    Scenario: Validar el servicio Get Store Inventory
        Given method get
        Then status 200
    
    @validateResponse404
    Scenario: Validar el servicio Get Store Inventory con response 404
        Given path '/inventario'
        And method get
        Then status 404