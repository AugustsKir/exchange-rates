# Exchange rate API 💵

Exchange rate API, made with Javalin. Data is fetched from Bank.lv, and it stores the latest exchange rates in relation to EUR. Data is stored in a MariaDB database

## Instructions

1. Clone the project to your local computer

2. Run a docker container, to start the MariaDB database ->

```
docker run --name currency-database -e MARIADB_USER=user -e MARIADB_PASSWORD=password -e MYSQL_ROOT_PASSWORD=rootpassword -e MARIADB_DATABASE=currency-database -p 3306:3306 -d mariadb:latest
```

3. Open the terminal in the project folder and run the following commands ->
 - The command below loads the data to the database.
```
mvn exec:java -Dexec.arguments="load_data"
```

- The command below starts Javalin, and enables the endpoints | To stop the app, press CTRL+C in the terminal.
```
mvn exec:java -Dexec.arguments="enable_endpoints"
```
- The command below clears the database from any data. 

```
mvn exec:java -Dexec.arguments="clear_data"
```

***DISCLAIMER | To use these commands, you have to have maven installed and configured on your machine.***


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

