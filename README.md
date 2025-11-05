# Guía de Uso del Framework Karate

Este proyecto utiliza el framework Karate para pruebas de API. El proyecto está estructurado en Java, JavaScript y Maven.

## Estructura del Proyecto

La estructura del proyecto es la siguiente:

- `src/test/java/karate-config.js`: Este archivo contiene la configuración para las pruebas de Karate.
- `src/test/java/karate/runner`: Este directorio contiene los archivos de ejecución de las pruebas.
- `src/test/java/karate/utils`: Este directorio contiene los archivos de utilidades para las pruebas.
- `src/test/java/resources/features`: Este directorio contiene los archivos de características que definen los escenarios de prueba.
- `src/test/java/resources/request`: Este directorio contiene los archivos de archivos json para realizar mock de data.

## Configuración de entornos

  En el archivo `karate-config.js` se encuentran las configuraciones de los entornos de integración, certificación y producción.

  ```javascript
  function fn() {
      var env = karate.env; 
      if (!env) {
          env = 'integracion'; 
      }

      if (env == 'integracion') {
          config.tokenUrl = 'https://servicewapdigitalcrt0100.azurewebsites.net',
     
      }
      
      return config;
  }
  ```

## Ejecución de Pruebas

Los escenarios de prueba se definen en los archivos `.feature` en el directorio `src/test/java/resources/features`. Cada escenario está etiquetado con una etiqueta única para su fácil identificación y ejecución.

Por ejemplo, para ejecutar el escenario de prueba etiquetado con `@TokenTest`, use el siguiente comando:

Para Windows/Linux/Mac:

```bash
# Ejecución por Tags
mvn test -Dkarate.options="--tags @smoke"

# Ejecución por Tags Windows
mvn test --% -Dkarate.options="--tags @TokenTest"

# Ejecución por entornos
mvn test -Dkarate.options="--tags @TokenTest" -Dkarate.env="certificacion"
