package com.sharak.mychatlet.server;

import com.sharak.mychatlet.server.constants.ErrorConstants;
import com.sharak.mychatlet.server.db.ServerDataSource;
import com.sharak.mychatlet.server.exception.MyChatLetException;
import com.sharak.mychatlet.server.managers.ServerManager;
import com.sharak.mychatlet.server.models.Server;
import com.sharak.mychatlet.server.threads.ThreadServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MyChatLetServer {
    
    private static final String TAG = MyChatLetServer.class.getSimpleName();
    private static final String VERSION = "1.0";
    
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private Server server;
    
    public static void main(String[] args) {
        try {
            
            new MyChatLetServer();
            
        } catch(MyChatLetException exception){
            System.out.println("Error #"+exception.getNumeroError()+":");
            System.out.println(exception.getMensajeError());
            System.out.println("--------------");
        } catch(Exception exception){
            System.out.println("ERROR NO CONTROLADO: ");
            exception.printStackTrace();
        }
    }
    
    public MyChatLetServer() throws MyChatLetException {
        banner();
        
        try {
            server = ServerManager.getConfigFromPropertiesFileToServer();
        } catch(MyChatLetException exception){
            server = ServerManager.getDefaultConfigToServer();
        }
        
        try {
            server = ServerManager.getOrCreateServer(server);
        } catch(MyChatLetException exception){
            throw exception;
        }
        
        try {
            serverSocket = new ServerSocket(server.getPuerto());
            System.out.println("Server conectado");
        } catch(Exception e) {
            System.out.println("Server desconectado");
        }
        
        clientSocket = null;
        while(true) {
            try {
                clientSocket=serverSocket.accept();
                try {
                    new ThreadServer(server,clientSocket).start();
                } catch(IOException e1) {
                    if(clientSocket != null) {
                        try {
                            clientSocket.close();
                        } catch(IOException e2){
                            throw new MyChatLetException(ErrorConstants.numError00011,
                                    ErrorConstants.msgError00011,e2);
                        }
                    }
                }
            } catch(SecurityException exception){
                if(serverSocket!=null){
                    try {
                        serverSocket.close();
                    } catch(IOException e4){
                        throw new MyChatLetException(ErrorConstants.numError00012,
                                ErrorConstants.msgError00012,e4);
                    }
                }
            } catch(MyChatLetException exception){
                throw exception;
            } catch(Exception exception){
                throw new MyChatLetException(ErrorConstants.numError00010,
                        ErrorConstants.msgError00010,exception);
            }
        }
    }
    
    private void banner(){
        StringBuilder contenido = new StringBuilder(1000);
        contenido.append("MyChatLet Server version "+VERSION).append('\n')
                .append("----------------------------------------");
        System.out.println(contenido.toString());
    }
    
}
