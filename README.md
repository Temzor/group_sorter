# Group Sorter

Java-приложение, которое обрабатывает текстовый файл для группировки данных и предоставляет статистику о сформированных группах.

## Содержание

- Введение
- Возможности
- Начало работы
    - Предварительные требования
    - Сборка проекта
    - Запуск приложения
- Использование
    - Формат входного файла
    - Пример
- Производительность
- Структура проекта
- Зависимости
- Контакты

## Введение

Group Sorter — это Java-приложение, предназначенное для чтения больших текстовых файлов, обработки каждой строки, группировки данных на основе определенных критериев и предоставления статистики о сформированных группах. Оно оптимизировано для производительности и может эффективно обрабатывать большие наборы данных.

## Возможности

- Эффективная обработка больших текстовых файлов.
- Группировка данных на основе реализованной логики.
- Предоставление информации о времени выполнения и статистике групп.
- Поддержка сжатых входных файлов (формат .gz).

## Начало работы

### Предварительные требования

- Java Development Kit (JDK) 17: Убедитесь, что установлена JDK 17 и задана переменная окружения JAVA_HOME.
- Apache Maven 3.8 или выше: Maven используется для сборки и управления зависимостями проекта.

### Сборка проекта

1. Клонируйте репозиторий

   - git clone https://github.com/Temzor/group_sorter
   - cd group_sorter


2. Соберите с помощью Maven

   Выполните следующую команду в корневой директории проекта для сборки проекта и создания исполняемого JAR-файла:

   mvn clean package


Эта команда сгенерирует JAR-файл со всеми включенными зависимостями:

      target/group_sorter-1.0-SNAPSHOT-jar-with-dependencies.jar


### Запуск приложения

Чтобы запустить приложение, используйте команду java -jar, указав путь к исполняемому JAR-файлу и входному текстовому файлу:

- java -jar target/group_sorter-1.0-SNAPSHOT-jar-with-dependencies.jar inputfile.txt


Если ваш входной файл сжат в формате .gz, вы можете указать его напрямую:

- java -jar target/group_sorter-1.0-SNAPSHOT-jar-with-dependencies.jar inputfile.txt.gz


## Использование

### Формат входного файла

Приложение ожидает текстовый файл, где каждая строка представляет элемент для обработки. Конкретный формат каждой строки зависит от реализованной логики в классе LineProcessor.

Примечание: Пожалуйста, убедитесь, что ваш входной файл соответствует ожидаемому формату, чтобы избежать ошибок при обработке.

### Пример

1. Подготовьте ваш входной файл

   Создайте текстовый файл data.txt со следующим примерным содержимым:

   111

   222

   1112

   222

   1113

   333


2. Запустите приложение

   java -jar target/group_sorter-1.0-SNAPSHOT-jar-with-dependencies.jar data.txt


3. Ожидаемый вывод

   Время выполнения программы: 123 мс
   Количество групп с более чем одним элементом: 2


Это означает, что программа обработала файл за 123 миллисекунды и обнаружила 2 группы с более чем одним элементом (в данном случае яблоко и банан).

## Производительность

Приложение измеряет и выводит время выполнения, что помогает анализировать производительность с разными размерами входных файлов.

## Структура проекта


```
group_sorter/
├─── pom.xml
├─── README.md
└───src
    ├───main
    │   ├───java
    │      └───io
    │          └───sorter
    │              │   Main.java
    │              │
    │              ├───mapper
    │              │       DisjointSet.java
    │              │
    │              ├───processor
    │              │       LineProcessor.java
    │              │
    │              ├───service
    │              │       CustomFileReader.java
    │              │       GroupBuilder.java
    │              │       LineParser.java
    │              │       ResultWriter.java
    │              │
    │              └───serviceimpl
    │                      CustomFileReaderImpl.java
    │                      GroupBuilderImpl.java
    │                      LineParserImpl.java
    │                      ResultWriterImpl.java
    └───resources
```

- Main.java: Точка входа приложения;
- FileReaderImpl считывает файл.- LineParserImpl парсит строки;
- GroupBuilderImpl строит группы;
- ResultWriterImpl записывает результат;
- DisjointSet реализует структуру данных для объединения множеств.

## Зависимости

Проект использует следующие зависимости, которыми управляет Maven:
- Генерация отчетов maven-surefire отключена;
- Библиотека автотестирования при сборке org.junit.jupiter;
- Apache Commons Compress: Для обработки сжатых входных файлов;
- Другие стандартные библиотеки: По мере необходимости для логики приложения.

Все зависимости указаны в файле pom.xml и включены в исполняемый JAR-файл, собираемый с помощью Maven.

## Контакты

По всем вопросам или предложениям обращайтесь:

- Имя: Дмитрий К.
- Email: shymsidecity@ya.ru
- Tg: dmitriiglass
- GitHub: temzor (https://github.com/Temzor)

---

Спасибо за использование Group Sorter! Ваш вклад и предложения приветствуются.