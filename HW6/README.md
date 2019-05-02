### ДЗ-06: my cache engine
Напишите свой cache engine с soft references

### Решение:
```
mvn clean test
```
Для того чтобы не допустить утечку памяти использован _SoftReference_.
***
Для проверки работы кэша я использую тесты с разнями типами кэша и размером 200 элементов.
Я записываю на диск файл с 1000 элементами. Далее 10000 раз читаю данные 
если элемента нет в кэше записываю его в кэш.
***
В результате выполнения теста лучшие результаты показал _eternalCache_.
Никакого приемущества не дало использование _lifeCache_ в моем примере.
<br/>
MainTest.lifeCache          4m 43s <br/>
MainTest.minHitCache        5m 01s<br/>
MainTest.eternalCache       3m 43s<br/>
MainTest.idleCache          3m 58s<br/>
MainTest.noCache            4m 36s
