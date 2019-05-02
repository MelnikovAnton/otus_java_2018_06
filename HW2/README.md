### ДЗ 02. Измерение памяти
Написать стенд для определения размера объекта. Определить размер пустой строки и пустых контейнеров. Определить рост размера контейнера от количества элементов в нем.<br /><br /><br />Например:<br />Object — 8 bytes,<br />Empty String — 40 bytes<br />Array — from 12 bytes

### Решение:
```
mvn clean install

java -javaagent:./target/HW2.jar -jar ./target/HW2.jar
```
Значения для ArrayList у метода reflection меньше реального, так как внутри ArrayList-а находится массив Object[],
то есть Reflection посчитает элементы массива равными 16.

Судя по всему Instrumentation не лезет в глубь полей и поэтому у всех коллекций возвращает размер объекта без учета размера массива.
Аналогичная ситуация и со String. Не учтен byte[].

Метод ArrayGetSize не подходит для определения размера строк определенных через "".
Так как такие строки хранятся в String Pool и не меняют объем памяти.