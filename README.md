Project "Restaurant voting"
===============================

#### 
It is implementation of REST API using Hibernate/Spring/SpringMVC without frontend.
Voting system for deciding where to have lunch.

- 2 types of users: admin and regular users
- Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
- Menu changes each day (admins do the updates)
- Users can vote on which restaurant they want to have lunch at
- Only one vote counted per user
- If user votes again the same day:
  -   If it is before 11:00 we assume that he changed his mind.
  -   If it is after 11:00 then it is too late, vote can't be changed
- Each restaurant provides a new menu each day.

-------------------------------------------------------------
- Stack: [JDK 17](http://jdk.java.net/17/), Spring Boot 2.5, Lombok, H2, Caffeine Cache, Swagger/OpenAPI 3.0
- Run: `mvn spring-boot:run` in root directory.
-----------------------------------------------------
### [REST API documentation](http://localhost:8080/swagger-ui.html)  
Credentials:
```
User:  user@yandex.ru / password
Admin: admin@gmail.com / admin
```

#### curl samples
> For windows use `Git Bash` 

#### get All Users
`curl -s http://localhost:8080/api/admin/users --user admin@gmail.com:admin`

#### get User 1
`curl -s http://localhost:8080/api/admin/users/1 --user admin@gmail.com:admin`

#### register User
`curl -s -i -X POST -d '{"name":"New User","email":"test@mail.ru","password":"test-password"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/api/profile`

#### get Profile
`curl -s http://localhost:8080/api/profile --user test@mail.ru:test-password`

#### get All Restaurants
`curl -s http://localhost:8080/api/restaurants --user user@yandex.ru:password`

#### get Restaurant 1
`curl -s http://localhost:8080/api/restaurants/1 --user user@yandex.ru:password`

#### vote for the restaurant
`curl -X POST http://localhost:8080/api/restaurants/2/votes --user admin@gmail.com:admin`

#### get Restaurant not found
`curl -s -v http://localhost:8080/api/restaurants/100 --user user@yandex.ru:password`

#### delete Restaurant
`curl -s -X DELETE http://localhost:8080/api/admin/restaurants/2 --user admin@gmail.com:admin`

#### create Restaurant
`curl -s -X POST -d '{"name":"New Restaurant"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/api/admin/restaurants --user admin@gmail.com:admin`

#### update Restaurant
`curl -s -X PUT -d '{"name":"Updated Sakhalin"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/api/admin/restaurants/1 --user admin@gmail.com:admin`

#### create invalid Restaurant
`curl -s -X POST -d '{}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/api/admin/restaurants --user admin@gmail.com:admin`

#### get All Dishes for restaurant 1
`curl -s -v http://localhost:8080/api/admin/restaurants/1/dishes --user admin@gmail.com:admin`

#### update Dish 3 for restaurant 1
`curl -s -X PUT -d '{"name":"Updated Dish","price":999}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/api/admin/restaurants/1/dishes/3 --user admin@gmail.com:admin`

#### delete Dish 2 belongs to restaurant 1
`curl -s -X DELETE http://localhost:8080/api/admin/restaurants/1/dishes/2 --user admin@gmail.com:admin`

#### delete Dish 5 doesn’t belong to restaurant 1
`curl -s -X DELETE http://localhost:8080/api/admin/restaurants/1/dishes/5 --user admin@gmail.com:admin`

#### create Dish for restaurant 1
`curl -s -X POST -d '{"name":"New Dish","price":888}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/api/admin/restaurants/1/dishes --user admin@gmail.com:admin`

#### create duplicated Dish
`curl -s -X POST -d '{"name":"Sakhalin fish soup","price":888}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/api/admin/restaurants/1/dishes --user admin@gmail.com:admin`
