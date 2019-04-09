# Тестовое REST-приложение 

## Сборка
Проект использует инструмент сборки [Gradle](https://gradle.org/). 
Сборка выполняется средствами IDE или из консоли: 
```
./gradlew build 
```
При сборке выполнятся автоматическое тестирование, настройки подключения к БД для тестов находятся в 
`application-integrationtest.properties`.

> Внимание! Настройки по умолчанию БД для тестов предполагают пересоздание БД при запуске тестов. Таким образом, 
возможна потеря данных при запуске на продуктовой базе

Команда для сборки без выполнения тестов:
```
./gradlew build -x test
``` 

## Запуск

Приложение собирается как исполняемый jar, т.е. запуск осуществляется командой
```
java -jar build/libs/requests-0.0.1-SNAPSHOT.jar
```
Настройки подключения к БД расположены в `application.properties`.
Пример настроек для [PostgreSQL](https://www.postgresql.org/):
```properties
spring.datasource.url=jdbc:postgresql://localhost/postgres
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver
``` 
Необходимые таблицы создаются автоматически при старте приложения. 

## Примеры запросов для утилиты curl

* Отправить заявку по услуге в ведомство:
```
curl -d @request.json -H "Content-Type: application/json" http://localhost:8080/requests
```
где файл `request.json` вида:
```json
{
  "person": {
    "surname": "Иванов",
    "name": "Иван",
    "patronymic": "Иванович",
    "passport": {
      "series": "01-01",
      "number": "123456"
    },
    "documents": [
      {
        "data": "dGVzdCBiYXNlNjQgc3RyaW5n"
      }
    ],
    "serviceName": "Имя услуги",
    "date": "31/12/2018"
  }
}
```
* Получить все заявки:
```
 curl -v http://localhost:8080/requests
```
* Получить заявки на указанную дату:
```
curl -v http://localhost:8080/requests?date=31/12/2018
```
* Получить заявки по указанной услуге, отсортированные по убыванию даты:
```
curl -v "http://localhost:8080/requests?serviceName=service&order_by=-date"
```
* Получить необработанные заявки, отсортированные по фио:
```
curl -v "http://localhost:8080/requests?status=new&order_by=person.surname,person.name,person.patronymic"
```
* Получить детализацию по заявке с id = n:
```
curl -v "http://localhost:8080/requests/n"
```
* Исполнить заявку c id = n:
```
curl -v --request PATCH "http://localhost:8080/requests/n?status=processed"
```