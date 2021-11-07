# semester_project_3_team
1) Реализация системы входа в систему


2) БД человек/группа/баллы


3) Реализовать функционала учителя

	3.1) Список учеников
	
	3.2) Список лаб (возможность загрузки новых лаб)
	
	3.3) Возможность шарить лабы с учениками

	3.4) Создание новых лаб (название, условие, дедлайн, группы которым доступно)

	3.5) Система дедлайнов

	3.6) Возможность шарить лабы только определенным группам (и ограничение по баллам)

	3.7) Возможность перейти на страницу ученика


4) Функционал ученика

	4.1) Список доступных лаб

	4.2) Реализация возможности перехода к ним

	4.3) Перелопатить функционал учителя под ученика



5) Странички загруженных лаб
	
	5.1) Учитель видит табличку, кто сдал, и видит ссылку на загруженный файл



# Схема нашей "БД":

У нас будет ~~две БД~~ два txt файла

Первый будет хранить в себе логин, пароль, номер группы и ФИО человека

	Логин и пароль для всех выдан отрядом программистов заранее
	
	Одна строчка - один человек
 
	Разделение данных с помощью символа ';' (чтобы txt можно было открыть с помощью excel)
 
	Разделение строк с помощью символа переноса строки
 
	В конце БД строчка с символом '^'
 
 
Второй будет хранить в себе данные о лабораторных работах

	Порядковый номер лр, название лр, дедлайн, номера групп, предмет, баллы, путь к файлу с заданием, путь к папке с ответами
	
	Номера групп перечислены с разделяющим символом '['
 
	Одна строчка - одна лр
 
	Разделение данных с помощью символа ';' (чтобы txt можно было открыть с помощью excel)
 
	Разделение строк с помощью символа переноса строки
 
	В конце БД строчка с символом '^'
 

# Мы работаем, честное слово
