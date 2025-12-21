Запуск: ```docker-compose``` -> ```UserServiceApplication``` и ```NotificationServiceApplication```.

Письма ловятся mailhog: http://localhost:8025/

UserService API: 

  Создание пользователя: POST - ```/api/users```
    тело: 
```json
    {  
        "name": "Test Name",  
        "email": "email@example.com",  
        "age": 25  
    }
```

  Список всез пользователей: GET - ```/api/users```

  Пользователь по ID: ```/api/users/{id}```

  Редактирование пользователя: ```/api/users/{id}```
    тело: 
```json
    {
        "name": "Updated Name",
        "email": "new@example.com",
        "age": 36
    }
```

  Удаление пользователя: ```/api/users/{id}```
