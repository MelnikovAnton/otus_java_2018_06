### ДЗ-10: myORM
Создайте в базе таблицу с полями: <br />id bigint(20) NOT NULL auto_increment <br />name varchar(255)<br />age int(3)<br /><br />Создайте абстрактный класс DataSet. Поместите long id в DataSet. <br />Добавьте класс UserDataSet (с полями, которые соответствуют таблице) унаследуйте его от DataSet. <br /><br />Напишите Executor, который сохраняет наследников DataSet в базу и читает их из базы по id и классу.  <br /><br />&lt;T extends DataSet&gt; void save(T user){…}<br />&lt;T extends DataSet&gt; T load(long id, Class&lt;T&gt; clazz){…}

### Решение:
```
mvn clean package
java -jar ./target/HW10.jar
```
        
