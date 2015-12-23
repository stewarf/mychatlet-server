package com.sharak.mychatlet.server.threads;

import com.sharak.mychatlet.server.constants.ErrorConstants;
import com.sharak.mychatlet.server.exception.MyChatLetException;
import com.sharak.mychatlet.server.managers.ServerManager;
import com.sharak.mychatlet.server.managers.UsuarioManager;
import com.sharak.mychatlet.server.models.Server;
import com.sharak.mychatlet.server.models.Usuario;
import com.sharak.mychatlet.server.utils.Encriptacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ThreadServer extends Thread {
    
    private static final String TAG = ThreadServer.class.getSimpleName();
    
    private Server server;
    private Socket clientSocket;
    private Usuario usuario;
    private static List UserOnLine= new ArrayList();
    private BufferedReader entrada;
    private PrintWriter salida;
    private String mensaje;
    private String fecha;
    private String[] command;
    
    public ThreadServer(Server server,Socket clientSocket) throws IOException {
        this.server = server;
        this.clientSocket=clientSocket;
        activateCommunication();
    }
    
    private void activateCommunication() throws IOException {
        entrada= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        salida=new PrintWriter(clientSocket.getOutputStream(),true);
    }
    
    @Override
    public void run() {
        mensaje = "";
        usuario = new Usuario();
        usuario.setId(0);
        try {
            
            do {
                loginModule();
                usuario = UsuarioManager.getUsuario(server,usuario);
                if(usuario.getId() == 0){
                    salida.println("\nMensaje del Servidor "+server.getId()+": El nick no existe o tu password es erroneo.\nVuelve a intentarlo\n*** END MSG\n");
                } else {
                    addConexion(this);
                }
            } while(usuario.getId() == 0);
            
            UsuarioManager.activar(server,usuario);
            salida.println("\nBienvenido al servidor de chat "+server.getId()+"\n");
            
            getLastConversations();
            
            while((mensaje=entrada.readLine())!=null) {
                command = mensaje.split(" ");
                if(command[0].equals("/C")){
                    getOnlineUserList();
                } else if(command[0].equals("/H")){
                    if(command.length == 2){
                        //conversaciones conforme a la fecha especificada
                        fecha=command[1];
                        getConversationsByDate(fecha);
                    } else {
                        writeUser(this,"Mensaje del Servidor: Comando requiere argumentos para procesar\nUso correcto: /H <fecha> \nFormato para la fecha: YYYY-mm-dd\n");
                    }
                } else if(command[0].equals("/T")){
                    getLoginDateTimeClient();
                } else if(command[0].equals("/L")){
                    getConnectionHistoricClient();
                } else if (command[0].equals("/E")) {
                    salida.println("Usuario desconectado");
                    break;
                } else if(command[0].equals("/PM")){
                    if(command.length >= 3){
                        String destinatario_nick = command[1];
                        StringBuffer mensaje = new StringBuffer();
                        for (int x=2;x<command.length;x++){
                           mensaje =mensaje.append(command[x]+" ");
                        }
                        sendPrivateMsgToClient(destinatario_nick,mensaje.toString().trim());
                    } else {
                        writeUser(this,"Mensaje del Servidor: Comando requiere argumentos para procesar\nUso correcto: /PM <Nickname> <Mensaje>\n");
                    }
                } else {
                    try {
                        Statement stm = server.getDbconexion().createStatement();
                        String iSQL = "INSERT INTO cmlt_charlas(id_usuario, id_servidor, fecha, mensaje) VALUES("+usuario.getId()+","+server.getId()+", NOW(), '"+mensaje+"')";
                        stm.executeUpdate(iSQL);
                        writeAll(usuario.getNickName()+" dice: "+mensaje);
                    } catch(SQLException sql){}
                }
            }
            
        } catch(IOException ioe) {
            
        } catch(MyChatLetException exception){
            System.out.println("["+TAG+"] Error #"+exception.getNumeroError()+":");
            System.out.println(exception.getMensajeError());
            System.out.println("--------------");
        } catch(Exception exception){
            System.out.println("ERROR NO CONTROLADO: ");
            exception.printStackTrace();
        } finally {
            deleteConexion(this);
            clear();
        }
        
    }
    
    private void loginModule() throws MyChatLetException {
        String nickname = "";
        String password = "";
        try {
            salida.println("Nickname: ");
            nickname=(entrada.readLine().trim());
            
            if((nickname.equalsIgnoreCase(""))||(nickname == null)){
                Random rn = new Random();
                int range = 9999 - 1000 + 1;
                int randomNum =  rn.nextInt(range) + 1000;
                nickname = "User"+randomNum;
            }
            
            salida.println("Password: ");
            password=(entrada.readLine().trim());
            
            usuario.setNickName(nickname);
            usuario.setPassWord(password);
        } catch(IOException e) {
            throw new MyChatLetException(ErrorConstants.numError00013,ErrorConstants.msgError00013,e);
        } catch(Exception e){
            throw new MyChatLetException(ErrorConstants.numError00014,ErrorConstants.msgError00014,e);
        }
    }
    
    private static synchronized void addConexion(ThreadServer threadServidor) {
        UserOnLine.add(threadServidor);
    }
    
    private synchronized void writeAll(String txt) {
        Iterator it=UserOnLine.iterator();
        while(it.hasNext()) {
            ThreadServer tmp=(ThreadServer)it.next();
            if(!(tmp.equals(this))) {
                writeUser(tmp,txt);
            }
        }
    }
    
    private synchronized void writeUser(ThreadServer threadServidor, String txt) {
        (threadServidor.salida).println(txt);
    }
    
    private static synchronized void deleteConexion(ThreadServer threadServidor) {
        UserOnLine.remove(threadServidor);
    }
    
    public void clear() {
        if(entrada!=null) {
            try {
                entrada.close();
            } catch(IOException i){}
            entrada=null;
        }
        if(salida!=null) {
            salida.close();
            salida=null;
        }
        if(clientSocket!=null) {
            try {
                clientSocket.close();
            }
            catch(IOException io){}
            clientSocket=null;
        }
    }
    
    public void getLastConversations(){
        try {
            Statement stm = server.getDbconexion().createStatement();
            String sSQL = "SELECT us.nickname, date_format(ch.fecha,'%d %b %Y %H:%i') as fecha_formateada, ch.mensaje FROM cmlt_charlas ch LEFT JOIN cmlt_usuarios us ON us.id_usuario=ch.id_usuario WHERE ch.id_server="+server.getId()+" ORDER BY ch.fecha DESC LIMIT  0, 10";
            ResultSet rls = stm.executeQuery(sSQL);

            writeUser(this,"*** Ultimas Conversaciones Recientes");
            while(rls.next()) {
                writeUser(this,"["+rls.getString("fecha_formateada")+"] "+rls.getString("nickname")+" dice: "+rls.getString("mensaje"));
            }
            writeUser(this,"*** END MSG\n");
        }
        catch(SQLException sqle) {}
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////
    
    public void getOnlineUserList(){
        Iterator it=UserOnLine.iterator();
        writeUser(this,"*** Lista de Usuarios Activos");
        while(it.hasNext()){
            ThreadServer tmp=(ThreadServer)it.next();
            writeUser(this,tmp.usuario.getNickName());
        }
        writeUser(this,"*** END LIST\n");
    }
    
    public void getLoginDateTimeClient(){
        writeUser(this,"Fecha de inicio sesion actual: "+usuario.getStartDate().toString()+"\n");
    }
    
    public void getConversationsByDate(String fecha){
        try {
            Statement stm = server.getDbconexion().createStatement();
            String sSQL = "SELECT us.nickname, date_format(ch.fecha,'%d %b %Y %H:%i') as fecha_formateada, ch.mensaje FROM cmlt_charlas ch LEFT JOIN cmlt_usuarios us ON us.id_usuario=tbcharlas.id_usuario WHERE ch.id_servidor="+server.getId()+" AND ch.fecha BETWEEN '"+fecha+" 00:00:00' AND '"+fecha+" 23:59:59' ORDER BY ch.fecha DESC LIMIT  0, 50";
            ResultSet rls = stm.executeQuery(sSQL);

            writeUser(this,"*** Conversaciones de la fecha: "+fecha);
            while(rls.next()){
                writeUser(this,"["+rls.getString("fecha_formateada")+"] "+rls.getString("nickname")+" dice: "+rls.getString("mensaje"));
            }
            writeUser(this,"*** END MSG\n");
        } catch(SQLException sqle) {}
    }
    
    public void getConnectionHistoricClient(){
        String sSQL="";
        try {
            Statement stm = server.getDbconexion().createStatement();
            sSQL += "SELECT date_format(uslg.fecha,'%d %b %Y %H:%i') as fecha_formateada, us.nickname ";
            sSQL += "FROM  cmlt_usuarios_log uslg ";
            sSQL += "LEFT JOIN cmlt_usuarios us ON us.idUsr=uslg.idUsr "; 
            sSQL += "WHERE uslg.id_servidor="+server.getId()+" AND uslg.id_usuario="+usuario.getId()+" ";
            sSQL += "ORDER BY uslg.fecha ";
            ResultSet rls = stm.executeQuery(sSQL);

            writeUser(this,"*** Historico de Conexiones");
            while(rls.next()) {
                writeUser(this,"["+rls.getString("fecha_formateada")+"] "+rls.getString("nickname")+" inicio sesion.");
            }
            writeUser(this,"*** END MSG\n");
        } catch(SQLException sqle) {}
    }
    
    public void sendPrivateMsgToClient(String nickname,String txt) {
        Iterator it=UserOnLine.iterator();
        while(it.hasNext()) {
            ThreadServer tmp=(ThreadServer)it.next();
            if(tmp.usuario.getNickName().equals(nickname)){
                writeUser(tmp,"Mensaje Privado de "+usuario.getNickName()+": "+txt);
            }
        }
    }
    
}
