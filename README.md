# Пример приложения по курсу [Разработка Enterprise приложений без боли и сожалений](https://howto.stringconcat.com)
## Архитектура
Приложение разделено по ограниченым контекстам на микросервисы. 
В ветке [part_1](https://github.com/stringconcat/ddd_practice/tree/part_1) можно увидеть как на начальном этапе приложение было модульным монолитом.

Каждый микросервис реалзиован по принципу чистой архитектуры (Clean Architecture), 
бизнес-логика реализована паттернами Domain-Driven Design'а (Aggregates, Entities, Value Object, Domain Services).
За структурой проекта следит [ArchUnit](https://www.archunit.org/).

На данный момент СУБД используется только для одного агрегата, 
все остальное есть InMemory-реализация хранилищ.

Также мы постарались реализовать принцип кричащей архитектуры (Screaming Architecture). 
Структура проекта отражает назначение проекта и функциональность, 
тоже самое касается пакетов и даже отдельных [файлов](shop/usecase/src/main/kotlin/com/stringconcat/ddd/shop/usecase/cart)

Решения о важных архитектурных решениях содержатся в [Architecture Decision Records](/tools/adr)

## Инфраструктура
### Continuous integration
Основная идея - разработчику должно быть максимально удобно 
работать за локальным компьютером. Поэтому все необходимые действия с проектом
можно совершить нажатием одной "кнопки", которые находятся в [tools/scripts](tools/scripts). 
Таким образом в полной мере реализуем концепцию CI.
В качестве предохранителя используется [GitHubActions](https://github.com/stringconcat/ddd_practice/actions).
В случае падения сборки в артефакте reports есть вся необходимая информация для диагностики.
Для пуша можно установить [pre-push hook](tools/scripts/installHook.sh),
который будет прогонять всю сборку целиком перед заливкой в репозиторий.

### Сборка
Помимо простой компиляции и подготовки образа,
в проекте используются дополнительные инструменты, результат работы которых также влияет на успех или неуспех сборки.

* Статанализатор [detekt](https://detekt.dev/)
* Линтер - [ktlint](https://ktlint.github.io/), (как плагин к detekt)
* Контроль тестового покрытия [JaCoCo](https://github.com/jacoco/jacoco)
* Поиск уязвимых зависимотей [OWASP DependencyCheck](https://jeremylong.github.io/DependencyCheck/)
* Поиск новых версий для зависимостей []()
* Контроль отсутствия предупреждений компилятора

Зависимости перечислены в [ProjectDependencies.kt](buildSrc/src/main/kotlin/ProjectDependencies.kt)

### Запуск
"Кнопками" из [tools/scripts](tools/scripts) можно разворачивать и 
останавливать кластер на рабочей машине разработчка 
без необходимости занимать стенды. 

docker-compose -v

Docker Compose version v2.23.3
Версия докер компоузера должна быть свежей иначе на ubuntu 18, например, не взлетит
https://github.com/mishin/ddd_practice

И будет писать “Define and run multi-container applications with Docker.”

Ну и памяти на сервере не менее 16Gb

## Тесты
Пирамида тестов, реализованая в проекте
* Юнит-тесты
* Интеграционные тесты
  * Тестирование репозиториев
  * Тестирование клиентов к внешним сервисам
* Контрактные тесты [Pact.io](https://pact.io)
* Компонентные тесты
* Сквозные тесты [kbdd](https://github.com/ru-fix/kbdd/) + [Allure](https://docs.qameta.io/allure/)
* Тесты структуры проекта [ArchUnit](https://www.archunit.org/)
* Мутационные тесты [PiTest](https://pitest.org/)
* Нагрузочные тесты [Gatling](https://gatling.io/)

## Взаимодействие с внешним миром
В примере реализованы примеры
* Асинхронного взаимодействия между микросервисами через [RabbitMQ](https://rabbitmq.com)
* REST уровеня HATEOAS - [Spring HATEOAS](https://spring.io/projects/spring-hateoas)
* Использования паттернов устойчивости ([на примере библиотеки](https://resilience4j.readme.io/))
* Централизованой обработки ошибок
* Валидации ввода моделью предметной области
* Взаимодействия через Telnet ([библиотека ktelnet, скопирована в репозиторий с исправлением нескольких ошибок](https://github.com/teverett/ktelnet))

## Event storming
[Miro board](https://miro.com/app/board/o9J_lR8tnlI=/)

## Мониторинг
In Progress

## Конфигурация
In Progress

## Безопасность
In Progress

Ставьте звезды и приходите к нам в [Телеграм-канал](https://t.me/stringconcat)
