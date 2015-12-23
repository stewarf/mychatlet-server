package com.sharak.mychatlet.server.managers;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.io.IOException;

import com.sharak.mychatlet.server.constants.ErrorConstants;
import com.sharak.mychatlet.server.constants.ServerConstants;
import com.sharak.mychatlet.server.db.ServerDataSource;
import com.sharak.mychatlet.server.exception.MyChatLetException;
import com.sharak.mychatlet.server.models.Server;

public class ServerManager {
    
    public static final String CONFIGFILE = "MyChatLetServer.ini";
    
    private static ServerDataSource serverDS;
    
    public static Server getConfigFromPropertiesFileToServer() throws MyChatLetException {
        Properties prop = new Properties();
        InputStream input = null;
        Server server = null;
        try {
            input = new FileInputStream(CONFIGFILE);
            prop.load(input);
            
            server = parsePropertiesToServer(prop);
            
        } catch(IOException e){
            throw new MyChatLetException(ErrorConstants.numError00001,ErrorConstants.msgError00001,e);
        } catch(Exception e){
            throw new MyChatLetException(ErrorConstants.numError00002,ErrorConstants.msgError00002,e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    throw new MyChatLetException(ErrorConstants.numError00003,ErrorConstants.msgError00003,e);
                }
            }
        }
        return server;
    }
    
    private static Server parsePropertiesToServer(Properties prop) {
        Server server = null;
        server = new Server();
        server.setHostname(prop.getProperty(ServerConstants.SERVER_HOSTNAME));
        server.setPuerto(Integer.parseInt(prop.getProperty(ServerConstants.SERVER_PUERTO)));
        server.setDbnombre(prop.getProperty(ServerConstants.SERVER_DBNOMBRE));
        server.setDbusuario(prop.getProperty(ServerConstants.SERVER_DBUSUARIO));
        server.setDbpassword(prop.getProperty(ServerConstants.SERVER_DBPASSWORD));
        return server;
    }
    
    public static Server getDefaultConfigToServer(){
        Server server = new Server();
        server.setHostname(ServerConstants.DEFAULT_SERVER_HOSTNAME);
        server.setPuerto(ServerConstants.DEFAULT_SERVER_PUERTO);
        server.setDbnombre(ServerConstants.DEFAULT_SERVER_DBNOMBRE);
        server.setDbusuario(ServerConstants.DEFAULT_SERVER_DBUSUARIO);
        server.setDbpassword(ServerConstants.DEFAULT_SERVER_DBPASSWORD);
        return server;
    }
    
    public static Server getOrCreateServer(Server server) throws MyChatLetException{
        serverDS = null;
        try {
            serverDS = new ServerDataSource(server);
            serverDS.open();
            server = serverDS.getServerWithId();
            serverDS.close();
        } catch(MyChatLetException e){
            throw e;
        }
        return server;
    }
    
}
