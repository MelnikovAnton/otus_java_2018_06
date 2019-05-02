### ДЗ 05. Измерение активности GC
Написать приложение, которое следит за сборками мусора и пишет в лог количество сборок каждого типа (young, old) и время которое ушло на сборки в минуту.<br />Добиться OutOfMemory в этом приложении через медленное подтекание по памяти (например добавлять элементы в List и удалять только половину).<br />Настроить приложение (можно добавлять Thread.sleep(...)) так чтобы оно падало с OOM примерно через 5 минут после начала работы.<br />Собрать статистику (количество сборок, время на сборрки) по разным типам GC.

### Решение:
<p class="p2">Для получения информации о работе GC используется com.sun.manageme.GarbageCollectionNotificationInfo.</p>
<p class="p2">С помощью ключа:</p>
<p class="p2">-Xlog:gc:./logs/gc_pid_%p.log</p>
<p class="p2">Я сохраняю лог работы GC в директорию ./logs</p>
<p class="p3"><br></p>
<p class="p2">Для управления утечкой памяти можно использовать MBeans.</p>
<p class="p3"><br></p>
<p class="p2">Для Запуска можно использовать скрипт start.sh или start.cmd (в зависимости от ОС).</p>
<p class="p2">Скрипт собирает приложение и последовательно запускает приложение с ключами:</p>
<p class="p2">-XX:+UseParallelGC -XX:-UseParallelOldGC</p>
<p class="p2">-XX:+UseSerialGC</p>
<p class="p2">-XX:+UseParallelGC</p>
<p class="p2">-XX:+UseConcMarkSweepGC</p>
<p class="p2">-XX:+UseG1GC</p>
<p class="p3"><br></p>
<p class="p2">В моем случае скрипт выполнялся 27 мин. Лучший результат показал G1. Раньше всех «упал» ParallelGC.</p>
<p class="p1"><br></p>
<p class="p1"><br></p>
<table cellspacing="0" cellpadding="0" class="t1">
  <tbody>
    <tr>
      <td rowspan="2" valign="top" class="td1">
        <p class="p2"><span class="s1"><b>VM key</b></span></p>
      </td>
      <td rowspan="2" valign="top" class="td2">
        <p class="p2"><span class="s1"><b>GC Name</b></span></p>
      </td>
      <td rowspan="2" valign="top" class="td3">
        <p class="p2"><span class="s1"><b>Time Before OOM</b></span></p>
      </td>
      <td rowspan="2" valign="top" class="td4">
        <p class="p2"><span class="s1"><b>Count</b></span></p>
      </td>
      <td colspan="2" valign="top" class="td5">
        <p class="p2"><span class="s1"><b>Time</b></span></p>
      </td>
      <td colspan="2" valign="top" class="td6">
        <p class="p2"><span class="s1"><b>Memory</b></span></p>
      </td>
      <td colspan="2" valign="top" class="td7">
        <p class="p2"><span class="s1"><b>Operations</b></span></p>
      </td>
    </tr>
    <tr>
      <td valign="top" class="td8">
        <p class="p2"><span class="s1"><b><span class="Apple-converted-space"> </span>Total (ms)</b></span></p>
      </td>
      <td valign="top" class="td9">
        <p class="p2"><span class="s1"><b>AVG (ms)</b></span></p>
      </td>
      <td valign="top" class="td10">
        <p class="p2"><span class="s1"><b>Total (Mb)</b></span></p>
      </td>
      <td valign="top" class="td11">
        <p class="p2"><span class="s1"><b>AVG (Mb)</b></span></p>
      </td>
      <td valign="top" class="td11">
        <p class="p2"><span class="s1"><b>Added</b></span></p>
      </td>
      <td valign="top" class="td12">
        <p class="p2"><span class="s1"><b>Deleted</b></span></p>
      </td>
    </tr>
    <tr>
      <td rowspan="2" valign="top" class="td13">
        <p class="p3"><span class="s1"><b>-XX:+UseParallelGC -XX:-UseParallelOldGC</b></span></p>
        <p class="p1"><br></p>
      </td>
      <td valign="top" class="td14">
        <p class="p3"><span class="s1"><b>PS MarkSweep</b></span></p>
      </td>
      <td rowspan="2" valign="middle" class="td15">
        <p class="p4"><span class="s1">00:04:44</span></p>
      </td>
      <td valign="middle" class="td16">
        <p class="p2"><span class="s1">32</span></p>
      </td>
      <td valign="middle" class="td17">
        <p class="p2"><span class="s1">5384</span></p>
      </td>
      <td valign="middle" class="td18">
        <p class="p2"><span class="s1">168</span></p>
      </td>
      <td valign="middle" class="td19">
        <p class="p2"><span class="s1">368</span></p>
      </td>
      <td valign="middle" class="td20">
        <p class="p2"><span class="s1">12</span></p>
      </td>
      <td rowspan="2" valign="middle" class="td21">
        <p class="p4"><span class="s1">22321</span></p>
      </td>
      <td rowspan="2" valign="middle" class="td22">
        <p class="p4"><span class="s1">11187</span></p>
      </td>
    </tr>
    <tr>
      <td valign="top" class="td23">
        <p class="p3"><span class="s1"><b>PS Scavenge</b></span></p>
      </td>
      <td valign="middle" class="td24">
        <p class="p2"><span class="s1">4</span></p>
      </td>
      <td valign="middle" class="td25">
        <p class="p2"><span class="s1">159</span></p>
      </td>
      <td valign="middle" class="td26">
        <p class="p2"><span class="s1">40</span></p>
      </td>
      <td valign="middle" class="td27">
        <p class="p2"><span class="s1">99</span></p>
      </td>
      <td valign="middle" class="td28">
        <p class="p2"><span class="s1">25</span></p>
      </td>
    </tr>
    <tr>
      <td rowspan="2" valign="top" class="td29">
        <p class="p3"><span class="s1"><b>-XX:+UseSerialGC</b></span></p>
        <p class="p1"><br></p>
      </td>
      <td valign="top" class="td30">
        <p class="p3"><span class="s1"><b>MarkSweepCompact</b></span></p>
      </td>
      <td rowspan="2" valign="middle" class="td31">
        <p class="p4"><span class="s1">00:04:56</span></p>
      </td>
      <td valign="middle" class="td32">
        <p class="p2"><span class="s1">25</span></p>
      </td>
      <td valign="middle" class="td33">
        <p class="p2"><span class="s1">4610</span></p>
      </td>
      <td valign="middle" class="td34">
        <p class="p2"><span class="s1">184</span></p>
      </td>
      <td valign="middle" class="td35">
        <p class="p2"><span class="s1">372</span></p>
      </td>
      <td valign="middle" class="td36">
        <p class="p2"><span class="s1">15</span></p>
      </td>
      <td rowspan="2" valign="middle" class="td37">
        <p class="p4"><span class="s1">23591</span></p>
      </td>
      <td rowspan="2" valign="middle" class="td38">
        <p class="p4"><span class="s1">11799</span></p>
      </td>
    </tr>
    <tr>
      <td valign="top" class="td39">
        <p class="p5"><span class="s1"><b>Copy</b></span></p>
      </td>
      <td valign="middle" class="td40">
        <p class="p2"><span class="s1">5</span></p>
      </td>
      <td valign="middle" class="td41">
        <p class="p2"><span class="s1">254</span></p>
      </td>
      <td valign="middle" class="td42">
        <p class="p2"><span class="s1">51</span></p>
      </td>
      <td valign="middle" class="td43">
        <p class="p2"><span class="s1">118</span></p>
      </td>
      <td valign="middle" class="td44">
        <p class="p2"><span class="s1">23</span></p>
      </td>
    </tr>
    <tr>
      <td rowspan="2" valign="top" class="td45">
        <p class="p3"><span class="s1"><b>+UseParallelGC</b></span></p>
      </td>
      <td valign="top" class="td30">
        <p class="p3"><span class="s1"><b>PS MarkSweep</b></span></p>
      </td>
      <td rowspan="2" valign="middle" class="td46">
        <p class="p4"><span class="s1">00:04:37</span></p>
      </td>
      <td valign="middle" class="td32">
        <p class="p2"><span class="s1">16</span></p>
      </td>
      <td valign="middle" class="td33">
        <p class="p2"><span class="s1">2336</span></p>
      </td>
      <td valign="middle" class="td34">
        <p class="p2"><span class="s1">146</span></p>
      </td>
      <td valign="middle" class="td35">
        <p class="p2"><span class="s1">368</span></p>
      </td>
      <td valign="middle" class="td36">
        <p class="p2"><span class="s1">23</span></p>
      </td>
      <td rowspan="2" valign="middle" class="td47">
        <p class="p4"><span class="s1">22321</span></p>
      </td>
      <td rowspan="2" valign="middle" class="td48">
        <p class="p4"><span class="s1">11183</span></p>
      </td>
    </tr>
    <tr>
      <td valign="top" class="td49">
        <p class="p3"><span class="s1"><b>PS Scavenge<span class="Apple-converted-space"> </span></b></span></p>
      </td>
      <td valign="middle" class="td50">
        <p class="p2"><span class="s1">4</span></p>
      </td>
      <td valign="middle" class="td51">
        <p class="p2"><span class="s1">170</span></p>
      </td>
      <td valign="middle" class="td52">
        <p class="p2"><span class="s1">42</span></p>
      </td>
      <td valign="middle" class="td53">
        <p class="p2"><span class="s1">99</span></p>
      </td>
      <td valign="middle" class="td54">
        <p class="p2"><span class="s1">25</span></p>
      </td>
    </tr>
    <tr>
      <td rowspan="2" valign="top" class="td55">
        <p class="p3"><span class="s1"><b>+UseConcMarkSweepGC</b></span></p>
      </td>
      <td valign="top" class="td30">
        <p class="p3"><span class="s1"><b>ConcurrentMarkSweep</b></span></p>
      </td>
      <td rowspan="2" valign="middle" class="td56">
        <p class="p4"><span class="s1">00:05:01</span></p>
      </td>
      <td valign="middle" class="td32">
        <p class="p2"><span class="s1">77</span></p>
      </td>
      <td valign="middle" class="td33">
        <p class="p2"><span class="s1">64707</span></p>
      </td>
      <td valign="middle" class="td34">
        <p class="p2"><span class="s1">840</span></p>
      </td>
      <td valign="middle" class="td35">
        <p class="p2"><span class="s1">146</span></p>
      </td>
      <td valign="middle" class="td36">
        <p class="p2"><span class="s1">2</span></p>
      </td>
      <td rowspan="2" valign="middle" class="td57">
        <p class="p4"><span class="s1">23592</span></p>
      </td>
      <td rowspan="2" valign="middle" class="td58">
        <p class="p4"><span class="s1">11805</span></p>
      </td>
    </tr>
    <tr>
      <td valign="top" class="td30">
        <p class="p3"><span class="s1"><b>ParNew</b></span></p>
      </td>
      <td valign="middle" class="td32">
        <p class="p2"><span class="s1">13</span></p>
      </td>
      <td valign="middle" class="td33">
        <p class="p2"><span class="s1">159</span></p>
      </td>
      <td valign="middle" class="td34">
        <p class="p2"><span class="s1">12</span></p>
      </td>
      <td valign="middle" class="td35">
        <p class="p2"><span class="s1">138</span></p>
      </td>
      <td valign="middle" class="td36">
        <p class="p2"><span class="s1">10</span></p>
      </td>
    </tr>
    <tr>
      <td rowspan="2" valign="top" class="td45">
        <p class="p3"><span class="s1"><b>+UseG1GC</b></span></p>
      </td>
      <td valign="top" class="td49">
        <p class="p3"><span class="s1"><b>G1 Young Generation</b></span></p>
      </td>
      <td rowspan="2" valign="middle" class="td46">
        <p class="p4"><span class="s1">00:05:02</span></p>
      </td>
      <td valign="middle" class="td50">
        <p class="p2"><span class="s1">34</span></p>
      </td>
      <td valign="middle" class="td51">
        <p class="p2"><span class="s1">378</span></p>
      </td>
      <td valign="middle" class="td52">
        <p class="p2"><span class="s1">11</span></p>
      </td>
      <td valign="middle" class="td53">
        <p class="p2"><span class="s1">97</span></p>
      </td>
      <td valign="middle" class="td54">
        <p class="p2"><span class="s1">3</span></p>
      </td>
      <td rowspan="2" valign="middle" class="td47">
        <p class="p4"><span class="s1">23805</span></p>
      </td>
      <td rowspan="2" valign="middle" class="td48">
        <p class="p4"><span class="s1">11904</span></p>
      </td>
    </tr>
    <tr>
      <td valign="top" class="td30">
        <p class="p3"><span class="s1"><b>G1 Old Generation</b></span></p>
      </td>
      <td valign="middle" class="td32">
        <p class="p2"><span class="s1">9</span></p>
      </td>
      <td valign="middle" class="td33">
        <p class="p2"><span class="s1">1316</span></p>
      </td>
      <td valign="middle" class="td34">
        <p class="p2"><span class="s1">146</span></p>
      </td>
      <td valign="middle" class="td35">
        <p class="p2"><span class="s1">100</span></p>
      </td>
      <td valign="middle" class="td36">
        <p class="p2"><span class="s1">11</span></p>
      </td>
    </tr>
  </tbody>
</table>
<p class="p9"><br></p>
