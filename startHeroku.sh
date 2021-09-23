#!/bin/bash
if [ "$#" -eq 1 ]; then
    APP_NAME="$1"
    heroku create $APP_NAME --region eu
    heroku git:remote --app $APP_NAME
    heroku addons:create heroku-postgresql --as DATABASE --app $APP_NAME
    heroku config:set MAVEN_CUSTOM_OPTS="-Pprod,heroku -DskipTests" --app $APP_NAME
    heroku buildpacks:add heroku/java --app $APP_NAME
    DATABASE_URL=$(heroku config:get DATABASE_URL --remote heroku --app "$APP_NAME")
    DATABASE_USER=$(echo "$DATABASE_URL" | awk -v FS="(://|:)" '{print $2}')
    DATABASE_PASSWORD=$(echo "$DATABASE_URL" | awk -v FS="(/)" '{print $4}')
    echo "DATABASE_URL = ${DATABASE_URL}"
    echo "DATABASE_USER = ${DATABASE_USER}"
    echo "DATABASE_PASSWORD = ${DATABASE_PASSWORD=}"
    heroku config:set JDBC_DATABASE_URL=$DATABASE_URL --remote heroku --app $APP_NAME
    heroku config:set JDBC_DATABASE_USERNAME=$DATABASE_USER --remote heroku --app $APP_NAME
    heroku config:set JDBC_DATABASE_PASSWORD=$DATABASE_PASSWORD --remote heroku --app $APP_NAME
    sed "s/<MYAPPNAME>/$APP_NAME/g" ./heroku_files/application-heroku.yml > ./src/main/resources/config/application-heroku.yml
else
    echo "Usage: ./heroku_setup.sh <my_app_name>"
fi
