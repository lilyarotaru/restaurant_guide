[Проект TopJava-2](https://javaops.ru/view/topjava2)
===============================

#### Разбор решения [выпускного проекта TopJava](https://github.com/JavaOPs/topjava/blob/master/graduation.md)
- Spring Boot 2.5, Lombok, H2, Swagger/OpenAPI 3.0, Caffeine Cache
- Исходный код взят из миграции TopJava на Spring Boot (без еды)
- На основе этого репозитория на курсе будет выполняться выпускной проект "Голосование за рестораны"

#### Рефакторинг кода TopJava:
- в нашем приложении теперь только REST контроллеры, не надо добавлять `Rest` в имя
- заменил префикс `/rest` в URLs на `/api` 
- каждый контроллер занимается своими CRUD, переименовал `Admin[Rest]Controller` в `AdminUserController`
- исключил `AppConfig.h2Server` из тестов, он там не нужен
- удалил проверки `ValidationUtil.checkNotFound`. Есть готовый метод `JpaRepository.getById`, который бросает `EntityNotFoundException`. 
Добавил его обработку в `GlobalExceptionHandler`.
- сделал общий метод `BaseRepository.deleteExisted`
- TODO: кэшируйте только наиболее часто запрашиваемые данные