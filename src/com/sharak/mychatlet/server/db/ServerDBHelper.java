package com.sharak.mychatlet.server.db;

import com.sharak.mychatlet.server.constants.ErrorConstants;
import com.sharak.mychatlet.server.exception.MyChatLetException;
import com.sharak.mychatlet.server.models.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ServerDBHelper {
    
    static String CLASS_NAME = "com.mysql.jdbc.Driver";  
    static String PATH_DB = "jdbc:mysql://";
    
    private String urlConexion;
    private Connection conexion = null;
    private Server server;
    
    public ServerDBHelper(Server server) throws MyChatLetException {
        try {
            this.server = server;
            this.urlConexion = PATH_DB+server.getHostname()+"/"+server.getDbnombre();
            if (this.conexion != null) this.conexion = null;
            
            Class.forName(CLASS_NAME).newInstance();
            this.conexion = (Connection) DriverManager.getConnection(this.urlConexion,server.getDbusuario(),server.getDbpassword());
            
        } catch(SQLException e) {
            throw new MyChatLetException(ErrorConstants.numError00004,ErrorConstants.msgError00004,e);
        } catch (ClassNotFoundException e) {
            throw new MyChatLetException(ErrorConstants.numError00005,ErrorConstants.msgError00005,e);
        } catch(Exception e) {
            throw new MyChatLetException(ErrorConstants.numError00006,ErrorConstants.msgError00006,e);
        }
    }
    
    public Server getServer(){
        return this.server;
    }
    
    public void setServer(Server server){
        this.server = server;
    }
    
    public String getUrl(){
        return this.urlConexion;
    }
    
    public void setUrl(String url) {
        this.urlConexion= url;
    }
    
    public Connection getConnection() {
        return this.conexion;
    }
    
    public void closeConnection() {
        this.conexion = null;
    }
    
}
