### ДЗ-11: Hibernate ORM
На основе предыдущего ДЗ (myORM):<br />1. Оформить решение в виде DBService (interface DBService, class DBServiceImpl, UsersDAO, UsersDataSet, Executor)<br />2. Не меняя интерфейс DBSerivice сделать DBServiceHibernateImpl на Hibernate.<br />3. Добавить в UsersDataSet поля:<br />адресс  (OneToOne) <br />class AddressDataSet{<br />	private String street;<br />}<br />и телефон* (OneToMany)<br />class PhoneDataSet{<br />	private String number;<br />}<br />Добавить соответствущие датасеты и DAO. <br /><br />4.** Поддержать работу из пункта (3) в myORM

### Решение:
```
mvn clean package
java -jar ./target/HW11.jar
```
      
