# Система управления задачами

Система управления задачами (Task Management System) с использованием Java. Система обеспечивает создание, редактирование, удаление и просмотр задач.

### Требования

- Java 17
- Maven
- Docker

В качестве БД используется PostgreSQL.
Приложение реализовано с использованием framework Spring Boot.

Для запуска приложения необходимы следующие компоненты:
- файл "docker-compose.yml" (корень проекта)
- файл "dockerfile" (корень проекта)
- директория "\dockerDatabase" со всем содержимым
- файл \target\TaskManager-0.0.1-SNAPSHOT.jar

### Запуск приложения

Для запуска приложения перейти в корень проекта и выполнить команду "docker-compose up".
Приложение доступно по адресу http://localhost:8080

Swagger доступен по адресу http://localhost:8080/swagger-ui/index.html

Для запуска приложения из Intellij IDEA запустить TaskManagerApplication.


### Конфигурация

В качестве БД используется PostgreSQL.
При запуске приложения производится подключение к БД PostgreSQL в Docker.

При запуске приложения из Intellij IDEA при необходимости изменить параметры подключения к БД в файлах application.properties (spring.datasource.url, spring.datasource.username, spring.datasource.password)
Параметры подключения к БД для тестов указаны в файле application-test.properties.

### Таблицы базы данных
create table user_table(\
id int primary key generated by default as identity,\
username varchar not null unique,\
mail varchar not null unique,\
password varchar not null\
);

create table task(\
id int primary key generated by default as identity,\
title varchar,\
description varchar,\
state varchar,\
priority varchar,\
owner_id int references user_table (id),\
executor_id int references user_table (id)\
);

create table comment(\
id int primary key generated by default as identity,\
text varchar,\
user_id int references user_table(id),\
task_id int references task(id)\
);

Для выполнения тестов тестовые таблицы в БД создаются атоматически.