***MyChatLet Server***

**Configuración**
Disponer de la siguiente información y definirlo en el archivo de configuración *MyChatLetServer.ini*

- Hostname (localhost)
- Puerto
- Nombre de la base
- Usuario con acceso a la base de datos
- Password


**Ejecución**

Opción 1:

*java -jar mychatlet-server.jar*

Opción 2: usando el archivo por lotes

*mychatlet-server-run.bat*


**Comandos del chat soportados en el servidor**

*/C*: Desplegar la lista de clientes activos en la instancia correspondiente

*/H date*: Despliega el historial de pláticas generado según la fecha ingresada

*/T*: Despliega hora y fecha en que inicio sesión el cliente

*/L*: Despliega el historial de conexiones del cliente en cuestión

*/E*: Termina la sesión del cliente actual.

*/PM user private-text*: Envía un mensaje privado al cliente mencionado
