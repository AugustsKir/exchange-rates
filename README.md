# Exchange rate API 💵

Exchange rate API, made with Javalin. Data is fetched from Bank.lv, and it stores the latest exchange rates in relation to EUR. Data is stored in a MariaDB database

## Instructions

1. Clone the project to your local computer

2. Create a docker network

```
docker network create exchange_rate_network
```

3. Run a docker container to start the MariaDB database

```
docker run --name currency-database --network=exchange_rate_network -e MARIADB_USER=user -e MARIADB_PASSWORD=password -e MYSQL_ROOT_PASSWORD=rootpassword -e MARIADB_DATABASE=currency-database -p 3306:3306 -d mariadb:latest
```

4. Open the terminal in the project folder and run the following commands ->
- First off, to build the docker image.
```
docker build -t exchange-rate-app .
```
 - The command below loads the data to the database.
```
docker run --network=exchange_rate_network --name exchange_rate_load_data exchange-rate-app "load_data"
```

- The command below starts Javalin, and enables the endpoints
```
docker run -d --network=exchange_rate_network -p 8080:8080 --name exchange_rate_endpoints exchange-rate-app "enable_endpoints"
```
- The command below clears the database from any data. 

```
docker run --network=exchange_rate_network --name exchange_rate_clear_data exchange-rate-app "clear_data"
```
5. Send a HTTP GET request with the given endpoints below
```
http://0.0.0.0:8080/rates/aud
```

## Endpoints

**Dislaimer | Endpoints will be available on 0.0.0.0:8080**

- /rates **[GET]** -> returns the latest rates in a JSON format

- /rates/{*currency*} **[GET]** -> returns the historical values for the given currency. (ex. /rates/usd)


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
