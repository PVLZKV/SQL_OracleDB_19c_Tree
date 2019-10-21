# SQL_OracleDB_19c_Tree

Cсылки для скачивания доп материалов:
Oracle Database 19c - https://www.oracle.com/database/technologies/oracle-database-software-downloads.html
JDBC Driver - https://www.oracle.com/database/technologies/appdev/jdbc-ucp-19c-downloads.html

Перед выполнением задания:
1) На ПК установлена Oracle Database 19c
2) Создан Common user: c##user, password : password
3) Добален драйвер для базы данных - ojdbc10.jar
4) Установлено подключение к БД
4) Создана таблица и заполнена исходными данными

Описание к выполнению задания:
Дана таблица с данными:

Id 	ParentId 	Name
1 	0 	debian
2 	1 	ubuntu
3 	2 	kubuntu
4 	2 	lubuntu
5 	2 	linux mint
6 	0 	slackware
7 	6 	slax
8 	7 	wolvix
9 	7 	slampp
10 	7 	dnalinux
11 	6 	suse
12 	11 	linkat
13 	11 	opensuse
14 	0 	redhat
15 	14 	fedora core
16 	15 	sailfish os
17 	15 	fedora

Чать - Task 2 / Part 1.0 (output as a tree)

Задание: 
Необходимо вывести иерархический список следующего вида:  

debian            
    ubuntu        
        kubuntu   
        lubuntu   
        linux mint
slackware         
    slax          
        wolvix    
        slampp    
        dnalinux  
    suse          
        linkat    
        opensuse  
redhat            
    fedora core   
        sailfish os
        fedora

Решение:        
Связи между строками реализованы используя рекурсивные запросы Oracle SQL - START WITH и CONNECT BY с оператором PRIOR
Древовидный вывод реализован фунцией LPAD (' ', 2*(LEVEL-1)) || Name

Чать - Task 2 / Part 2.0 (sort by '%a%', output as a tree)

Задание:
Отсортировать по элементам. Если в группе\подгруппе нет элементов, то не выводить группу\подгруппу.
Например, значение фильтра: «а». В результате должны получить список.
Решение должно работать для любого наполнения таблицы и любого фильтра.

Решение:
Реализовано сляинем рузультатов двух запросов SELECT, ипользуя оператор UNIONN.
Первый SELECT возвращает множество рекурсивный связей от строк, чьё имя содержит букву "а", к корню.
Второй SELECT возвращает множество рекурсивных связей от каждой строки к корню. 
Связи строятся с условием - либо у дочерней, либо у родительской строки есть буква "а".
Древовидный вывод реализован фунцией LPAD (' ', 2*(LEVEL-1)) || Name.