### ДЗ-16:  MessageServer</div>
Cревер из ДЗ-15 разделить на три приложения:<br />    • MessageServer<br />    • Frontend<br />    • DBServer<br />Запускать Frontend и DBServer из MessageServer.<br />Сделать MessageServer сокет-сервером,  Frontend и DBServer клиентами.<br />Пересылать сообщения с Frontend на DBService через MessageServer. <br />Запустить приложение с двумя фронтендами (на разных портах)* и двумя датабазными серверами.<br /><br />* если у вас запуск веб приложения в контейнере, то MessageServer может копировать root.war в контейнеры при старте
                