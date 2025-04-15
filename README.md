# название бд Subscribers
# Таблицы создадутся при первом запуске программы
# чтобы заполнить таблицу тестовых Subscribers - POST запрос на /fillSubscribers
# POST на generate/random/ - создать случайное кол-во записей
# POST на /deleteCdr - удаляет все записи из CDR таблицы
## Учитывается хронологическая генерация
## Звонки которые происходят в полночь делятся на две разные записи
## Генерируются записи происходящие в один момент времени
## Отслеживаются занятые звонками абоненты, чтобы не возникло две разные записи, где один абонент участвует в них одновременно
## После генерирования 10-и записей они отправляются в другой сервис (пока закоменчена сама отправка, вызывается соответствующий метод который выводит информацию об отправляемых записях в терминал) и продолжается генерация
