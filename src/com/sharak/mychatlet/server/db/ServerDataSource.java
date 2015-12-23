package com.sharak.mychatlet.server.db;

import com.sharak.mychatlet.server.constants.ErrorConstants;
import com.sharak.mychatlet.server.models.Server;
import com.sharak.mychatlet.server.exception.MyChatLetException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ServerDataSource {
    
    ServerDBHelper dbhelper;
    Connection db;
    
    public ServerDataSource(Server server) throws MyChatLetException {
        dbhelper = new ServerDBHelper(server);
    }
    
    public void open() throws MyChatLetException {
        try {
            db = dbhelper.getConnection();
        } catch(Exception e){
            throw new MyChatLetException(ErrorConstants.numError00007,ErrorConstants.msgError00007,e);
        }
    }
    
    public void close(){
        db = null;
    }
    
    public Server getServerWithId() throws MyChatLetException {
        Server server = dbhelper.getServer();
        int oLang = 0;
        
        String sqlQuery;
        Statement stm;
        ResultSet rls;
        
        try {
            stm = db.createStatement();
            sqlQuery = "SELECT COUNT(id_servidor) FROM cmlt_servidores WHERE hostname='"+server.getHostname()+"' AND puerto="+server.getPuerto()+" ";
            rls = stm.executeQuery(sqlQuery);
            if(rls.next()) {
                oLang = rls.getInt(1);
            }
            
            rls = null;
            stm = null;
            
            if(oLang >= 1) {
                server = getServerId(server);
            } else if(oLang == 0) {
                stm = db.createStatement();
                sqlQuery="INSERT INTO cmlt_servidores (hostname, puerto) VALUES ('"+server.getHostname()+"',"+server.getPuerto()+") ";
                stm.executeUpdate(sqlQuery);
                server = getServerId(server);
            }
            
        } catch(MyChatLetException e){
            throw e;
        } catch(Exception e){
            throw new MyChatLetException(ErrorConstants.numError00008,ErrorConstants.msgError00008,e);
        } finally {
            server.setDbconexion(db);
            dbhelper.setServer(server);
        }
        
        return server;
    }
    
    public Server getServerId(Server server) throws MyChatLetException {
        String sqlQuery;
        Statement stm;
        ResultSet rls;
        try {
            stm = db.createStatement();
            sqlQuery = "SELECT id_servidor FROM cmlt_servidores WHERE hostname='"+server.getHostname()+"' AND puerto="+server.getPuerto()+" ";
            rls = stm.executeQuery(sqlQuery);
            if(rls.next()) {
                server.setId(rls.getInt(1));
            }
        } catch(Exception e){
            throw new MyChatLetException(ErrorConstants.numError00009,ErrorConstants.msgError00009,e);
        }
        return server;
    }
    
}
