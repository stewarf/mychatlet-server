***MyChatLet Server***

**Configuraci�n**
Disponer de la siguiente informaci�n y definirlo en el archivo de configuraci�n *MyChatLetServer.ini*

- Hostname (localhost)
- Puerto
- Nombre de la base
- Usuario con acceso a la base de datos
- Password


**Ejecuci�n**

Opci�n 1:

*java -jar mychatlet-server.jar*

Opci�n 2: usando el archivo por lotes

*mychatlet-server-run.bat*


**Comandos del chat soportados en el servidor**

*/C*: Desplegar la lista de clientes activos en la instancia correspondiente

*/H date*: Despliega el historial de pl�ticas generado seg�n la fecha ingresada

*/T*: Despliega hora y fecha en que inicio sesi�n el cliente

*/L*: Despliega el historial de conexiones del cliente en cuesti�n

*/E*: Termina la sesi�n del cliente actual.

*/PM user private-text*: Env�a un mensaje privado al cliente mencionado
