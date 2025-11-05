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

## local
```bash
# Ejecución por Tags
mvn test -Dkarate.options="--tags @smoke"

# Ejecución por Tags Windows
mvn test --% -Dkarate.options="--tags @TokenTest"

# Ejecución por entornos
mvn test -Dkarate.options="--tags @TokenTest" -Dkarate.env="certificacion"

## Ejecución desde GitHub Actions

El workflow se llama "Karate API Tests" y está configurado para ejecutarse con `workflow_dispatch`. El input principal es `test_tag` y se usa así para filtrar escenarios por tag.

Cómo ejecutar desde la interfaz web:
1. Ve a la pestaña "Actions" en tu repositorio.
2. Selecciona el workflow "Karate API Tests".
3. Haz clic en "Run workflow".
4. En el campo `test_tag` introduce el tag del escenario (ej: `@smokePostOrder`).
5. Ejecuta el workflow.

Qué hace internamente:
- El workflow ejecuta:
  mvn test -Dkarate.options="--tags <valor_de_test_tag>"
- Los reportes se suben como artifact en `target/karate-reports/` y se pueden descargar desde la ejecución del workflow.

Ejemplos:
- Ejecutar tag `@smokePostOrder`:
  - test_tag = @smokePostOrder
- Si en el futuro se habilitan inputs adicionales (nombre de escenario u opciones libres), esos valores se combinarán con `--tags` y se pasarán a `-Dkarate.options`.

Notas importantes:
- Actualmente el workflow pide `test_tag` en el formulario (required). Si prefieres hacerlo opcional, edita `.github/workflows/karate-tests.yml` para cambiar `required: true` a `required: false`.
- No necesitas credenciales locales para que Actions ejecute el workflow; todo se ejecuta en runners de GitHub.
- Si el nombre del escenario contiene espacios se deberá cuidar el quoting, pero para tags normales no es necesario.
