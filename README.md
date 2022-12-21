#  Дипломный проект профессии "Тестировщик"
___
### Дипломный проект представляет собой комплексное автоматизированное тестирования веб-сервиса по покупке тура.
___

## Документация проекта:
[1.Задание к диплому](https://github.com/AlinaNabi/Diplom/blob/main/Documents/Zadanie.md)  
[2.План автоматизации](https://github.com/AlinaNabi/Diplom/blob/main/README.md)  
[3.Отчет по итогам тестирования]()  
[4.Отчет по итогам автоматизации]()  
  
На локальном компьютере заранее должны быть установлены IntelliJ IDEA и Docker


## Запуск SUT, авто-тестов и генерация отчетов

1. Запустить Docker Desktop
2. Открыть проект в IntelliJ IDEA
3. В терминале IDEA запустить контейнеры:  
   `docker-compose up -d`
4. Запустить приложение:
* Для MySQL:  
  `java -jar ./artifacts/aqa-shop.jar --spring.datasource.url=jdbc:mysql://localhost:3306/app`
* Для PostgreSQL:  
  `java -jar ./artifacts/aqa-shop.jar --spring.datasource.url=jdbc:postgresql://localhost:5432/app`
5. Открыть второй терминал
6. В новой вкладке терминала в IDEA запустить тесты:
* Для MySQL:  
  `./gradlew clean test "-Ddb.url=jdbc:mysql://localhost:3306/app"`
* Для PostgreSQL:  
  `./gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"`
7. Создать отчёт Allure  
   `.\gradlew allureServe`
8. Для завершения работы allureServe выполнить команду
   `Ctrl+C`
9. Перейти в первый терминал
10. Остановить приложение
    `Ctrl+C`
11. Для остановки работы контейнеров выполнить команду  
    `docker-compose down`

    

