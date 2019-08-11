# Description 

This is a car advert demo REST API. It is developed in Play Framework with Scala programming language.
Used Slick with Mysql database. Data is represented in Json format.


# REST API 

1 - <b>List all car adverts :</b> 
  - The car adverts are listed and by default are sorted by id.
```bash
curl -i -X GET http://localhost:9000/
```

  - You can choose by which filed you want to sort the listed car adverts.
```bash
curl -i -X GET http://localhost:9000/listcars/"name of the field"
```

2 - <b>Get car advert by ID :</b>
  - You can show an car advert by using the ID of the car advert
  ```bash
curl -i -X GET http://localhost:9000/getcar/"the ID number of car advert"
```

3- <b>Add car advert :</b>
  ```bash
curl -i -X POST ...
```

4- <b>Modify car advert :</b>
  ```bash
curl -i -X POST ...
```

# Improvements

