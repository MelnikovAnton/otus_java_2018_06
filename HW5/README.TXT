Для создания утечки я заполняю Map 5 kB значениями и ключами которые создаются разными ClassLoader.
Ключи ставлю равными null чтобы значения можно было собрать GC. Ключи созданные CustomClassLoder не доступны для GC.

Для сохранения результата я пытаюсь обрабатывать OutOfMemoryError, хотя я понимаю что в реальной ситуации это скорее всего не поможет.

Для получения информации о работе GC используется com.sun.manageme.GarbageCollectionNotificationInfo.

Для управления утечкой памяти можно использовать MBeans.

Для Запуска можно использовать скрипт start.sh или start.cmd (в зависимости от ОС).
Скрипт собирает приложение и последовательно запускает приложение с ключами:
-XX:+UseParallelGC -XX:-UseParallelOldGC
-XX:+UseSerialGC
-XX:+UseParallelGC
-XX:+UseConcMarkSweepGC
-XX:+UseG1GC

В моем случае скрипт выполнялся 37 мин.
Результат работы:

1523+UseParallelGC--UseParallelOldGC
time before OOME: 00:09:53

GC: PS MarkSweep
Count: 904
Time: 220121 ms.
avgTime: 243,496681 ms.
Total Memory Collected: 25425,071152 Mb
avg Memory: 28,125079 Mb
==================================================
GC: PS Scavenge
Count: 307
Time: 1076 ms.
avgTime: 3,504886 ms.
Total Memory Collected: 49908,284470 Mb
avg Memory: 162,567702 Mb
==================================================

1553+UseSerialGC
time before OOME: 00:06:15

GC: Copy
Count: 221
Time: 1824 ms.
avgTime: 8,253394 ms.
Total Memory Collected: 29686,870247 Mb
avg Memory: 134,329730 Mb
==================================================
GC: MarkSweepCompact
Count: 26
Time: 6340 ms.
avgTime: 243,846154 ms.
Total Memory Collected: 1187,807030 Mb
avg Memory: 45,684886 Mb
==================================================

1558+UseParallelGC
time before OOME: 00:07:38

GC: PS MarkSweep
Count: 857
Time: 81785 ms.
avgTime: 95,431739 ms.
Total Memory Collected: 25607,422798 Mb
avg Memory: 29,880307 Mb
==================================================
GC: PS Scavenge
Count: 309
Time: 1140 ms.
avgTime: 3,689320 ms.
Total Memory Collected: 50254,331688 Mb
avg Memory: 162,635378 Mb
==================================================

1574+UseConcMarkSweepGC
time before OOME: 00:06:22

GC: ParNew
Count: 326
Time: 1897 ms.
avgTime: 5,819018 ms.
Total Memory Collected: 42368,945518 Mb
avg Memory: 129,966091 Mb
==================================================
GC: ConcurrentMarkSweep
Count: 103
Time: 34864 ms.
avgTime: 338,485437 ms.
Total Memory Collected: -1407,916031 Mb
avg Memory: -13,669088 Mb
==================================================

1583+UseG1GC
time before OOME: 00:06:29

GC: G1 Young Generation
Count: 274
Time: 3044 ms.
avgTime: 11,109489 ms.
Total Memory Collected: 17330,368881 Mb
avg Memory: 63,249521 Mb
==================================================
GC: G1 Old Generation
Count: 68
Time: 7962 ms.
avgTime: 117,088235 ms.
Total Memory Collected: 398,613045 Mb
avg Memory: 5,861957 Mb
==================================================

