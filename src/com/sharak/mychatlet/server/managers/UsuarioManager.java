package com.sharak.mychatlet.server.managers;

import com.sharak.mychatlet.server.constants.ErrorConstants;
import com.sharak.mychatlet.server.exception.MyChatLetException;
import com.sharak.mychatlet.server.models.Server;
import com.sharak.mychatlet.server.models.Usuario;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class UsuarioManager {
    
    public static Usuario getUsuario(Server server, Usuario usuario) throws MyChatLetException {
        int oLang = 0;
        
        String sqlQuery;
        Statement stm;
        ResultSet rls;
        
        try {
            String promptPassword = usuario.getPassWord();
            
            stm = server.getDbconexion().createStatement();
            sqlQuery = "SELECT COUNT(id_usuario) as contador FROM cmlt_usuarios WHERE nickname='"+usuario.getNickName()+"' ";
            rls = stm.executeQuery(sqlQuery);
            if(rls.next()) {
                oLang = rls.getInt("contador");
            }
            
            rls = null;
            stm = null;
            
            if(oLang >= 1) {
                usuario = getUsuarioWithId(server,usuario);
                if(!promptPassword.equalsIgnoreCase(usuario.getPassWord())){
                    usuario.setId(0);
                }
            } else if(oLang == 0) {
                stm = server.getDbconexion().createStatement();
                sqlQuery="INSERT INTO cmlt_usuarios (nickname, password) VALUES ('"+usuario.getNickName()+"','"+usuario.getPassWord()+"') ";
                stm.executeUpdate(sqlQuery);
                usuario = getUsuarioWithId(server,usuario);
            }
            
        } catch(MyChatLetException e){
            throw e;
        } catch(Exception e){
            throw new MyChatLetException(ErrorConstants.numError00015,ErrorConstants.msgError00015,e);
        }
        return usuario;
    }
    
    private static Usuario getUsuarioWithId(Server server, Usuario usuario) throws MyChatLetException {
        try {
            Statement stm = server.getDbconexion().createStatement();
            String sSQL = "SELECT cmlt_usuarios.id_usuario,cmlt_usuarios.nickname,cmlt_usuarios.password FROM cmlt_usuarios WHERE cmlt_usuarios.nickname='"+usuario.getNickName()+"' and cmlt_usuarios.password='"+usuario.getPassWord()+"' ";
            ResultSet rls = stm.executeQuery(sSQL);

            if(rls.next()) {
                usuario.setId(rls.getInt("id_usuario"));
                usuario.setPassWord(rls.getString("password"));
                usuario.setStartDate(new Date());
            }
        } catch(Exception e){
            throw new MyChatLetException(ErrorConstants.numError00016,ErrorConstants.msgError00016,e);
        }
        return usuario;
    }
    
    public static void activar(Server server, Usuario usuario) throws MyChatLetException {
        String sqlQuery;
        Statement stm;
        
        try {
            
            stm = server.getDbconexion().createStatement();
            
            String oSQL="INSERT INTO cmlt_usuarios_log(id_usuario, id_servidor, fecha) VALUES("+usuario.getId()+","+server.getId()+", NOW())";
            
            String uSQL="INSERT INTO cmlt_usuarios_online(id_usuario, id_servidor, status) VALUES("+usuario.getId()+","+server.getId()+", 1)";
            
            stm.executeUpdate(oSQL);
            stm.executeUpdate(uSQL);
            
        } catch(Exception e){
            throw new MyChatLetException(ErrorConstants.numError00017,ErrorConstants.msgError00017,e);
        }
    }
    
}
