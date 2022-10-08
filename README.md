# CurrencyApp
<<<<<<< HEAD
Часть 1
  Используя Clean Architecture, создать Kotlin модуль, который будет описывать работу валютной системы, сценарием использования которой является предоставление курса валют (Структура объекта курса валют: Название, Текущий курс, Изменение курса за 24 часа)

Часть 2
  Написать  Android реализацию этой системы с отображением списка курсов валют на UI используя паттерн MVVM (Jetpack). Если изменение курса положительное, то элемент изменения курса в списке должен иметь зеленый цвет, в противном случае красный.

Exchange rate API: https://exchangeratesapi.io/ (или любой другой бесплатный API)
=======
Create new project or use existing one with the Room db

Add ViewModel, LiveData and Navigation libraries (Hilt is an optional bonus)
Use existing project as an example: https://github.com/Yazon2006/KeepSimple2

Check if the viewModel survive screen rotation

Use liveData to receive updates from Room

Make HTTP request and save the result in DB which trigger LiveData which trigger UI updates
>>>>>>> localDb
