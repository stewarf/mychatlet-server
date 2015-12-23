package com.sharak.mychatlet.server.exception;

import com.sharak.mychatlet.server.constants.ErrorConstants;

public class MyChatLetException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    private int numeroError;
    private String mensajeError;

    public int getNumeroError() {
        return numeroError;
    }

    public void setNumeroError(int numeroError) {
        this.numeroError = numeroError;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }
    
    public MyChatLetException(){
        numeroError = ErrorConstants.numError00000;
        mensajeError = ErrorConstants.msgError00000;
    }
    
    public MyChatLetException(String mensaje){
        numeroError = ErrorConstants.numError00000;
        mensajeError = mensaje;
    }
    
    /**
     * Manejo de error
     * @param numero ErrorConstants.numErrorNNNNN
     * @param mensaje ErrorConstants.msgErrorNNNNN
     */
    public MyChatLetException(int numero, String mensaje) {
        numeroError = numero;
        mensajeError = numero + " " + mensaje;
    }
    
    /**
     * Manejo de error con excepcion
     * @param numero ErrorConstants.numErrorNNNNN
     * @param mensaje ErrorConstants.msgErrorNNNNN
     * @param excepcion Objeto Exception
     */
    public MyChatLetException(int numero, String mensaje, Exception excepcion) {
        numeroError = numero;
        mensajeError = mensaje + " " + excepcion.getMessage();
    }
    
}
