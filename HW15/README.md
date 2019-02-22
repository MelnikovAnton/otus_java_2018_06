По пунктам 1а-б у меня проблем не возникло но на всякий случай
 добавил зависимости и методы.
 вот моя конфигурация томката:

<table border="1" cellspacing="0" cellpadding="3">
<tbody><tr>
 <td colspan="8" class="title">Информация о сервере</td>
</tr>
<tr>
 <td class="header-center"><small>Версия Tomcat</small></td>
 <td class="header-center"><small>Версия JVM</small></td>
 <td class="header-center"><small>Поставщик JVM</small></td>
 <td class="header-center"><small>ОС</small></td>
 <td class="header-center"><small>Версия ОС</small></td>
 <td class="header-center"><small>Архитектура ОС</small></td>
 <td class="header-center"><small>Имя хоста</small></td>
 <td class="header-center"><small>IP Адрес</small></td>
</tr>
<tr>
 <td class="row-center"><small>Apache Tomcat/9.0.14</small></td>
 <td class="row-center"><small>10.0.1+10</small></td>
 <td class="row-center"><small>"Oracle Corporation"</small></td>
 <td class="row-center"><small>Mac OS X</small></td>
 <td class="row-center"><small>10.14.3</small></td>
 <td class="row-center"><small>x86_64</small></td>
 <td class="row-center"><small>MBP-Anton</small></td>
 <td class="row-center"><small>192.168.1.144</small></td>
</tr>
</tbody></table>

<br/>
1в: Поправил, хотя наверное коряво.
<br/>
2: Добавил catch (Exception e) и на всякий случай мониторинг состояния потоков в MessageSystem.
<br/>
3:  Сделал MsgToDB абстрактным и добавил новые класы для сообщений.