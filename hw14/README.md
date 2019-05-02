### ДЗ-14: WAR
Собрать war для приложения из ДЗ-12. <br />Создавать кэш и DBService как Spring beans, передавать (inject) их в сервлеты. <br />Запустить веб приложение во внешнем веб сервере.

###  Решение:
```
mvn clean package
cp ./target/hw14.war $CATALINA_HOME/webapps/
```
