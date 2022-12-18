# Exchange rate API 💵

Exchange rate API, made with Javalin. Data is fetched from Bank.lv, and it stores the latest exchange rates in relation to EUR. Data is stored in a MariaDB database

## Instructions

1. Run a docker container, to start the MariaDB database ->

```
docker run --name currency-database -e MARIADB_USER=user -e MARIADB_PASSWORD=password -e MYSQL_ROOT_PASSWORD=rootpassword -e MARIADB_DATABASE=currency-database -p 3306:3306 -d mariadb:latest
```

**TO BE UPDATED...**

## Endpoints

-- /rates **[GET]** -> returns the latest rates in a JSON format

-- /rates/{*currency*} **[GET]** -> returns the historical values for the given currency. (ex. /rates/usd)


## JSON Example for /rates/{usd}

```json
[
    {
        "currency": "USD",
        "rate": 1.064900,
        "date": "2022-12-14"
    },
    {
        "currency": "USD",
        "rate": 1.062100,
        "date": "2022-12-15"
    },
    {
        "currency": "USD",
        "rate": 1.061900,
        "date": "2022-12-16"
    }
]
```
