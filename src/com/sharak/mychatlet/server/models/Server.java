package com.sharak.mychatlet.server.models;

import java.sql.Connection;

public class Server {
    
    private int id;
    private int puerto;
    private String hostname;
    private String dbnombre;
    private String dbusuario;
    private String dbpassword;
    private Connection dbconexion;

    public Connection getDbconexion() {
        return dbconexion;
    }

    public void setDbconexion(Connection dbconexion) {
        this.dbconexion = dbconexion;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getDbnombre() {
        return dbnombre;
    }

    public void setDbnombre(String dbnombre) {
        this.dbnombre = dbnombre;
    }

    public String getDbusuario() {
        return dbusuario;
    }

    public void setDbusuario(String dbusuario) {
        this.dbusuario = dbusuario;
    }

    public String getDbpassword() {
        return dbpassword;
    }

    public void setDbpassword(String dbpassword) {
        this.dbpassword = dbpassword;
    }
    
}
