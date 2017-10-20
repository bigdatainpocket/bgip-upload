package com.bgip.exception;

import javax.ws.rs.core.Response.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BgipException extends Exception {

	private static final long serialVersionUID = 1L;
	private  String errorCode = null;
	private Status status = null;

	private static final Logger LOGGER = LoggerFactory.getLogger(BgipException.class);


    public BgipException(String errorCode, String message){
    	super(message);
    	this.errorCode = errorCode;
        }
    public BgipException(String errorCode, String message, Status status){
    	super(message);
    	this.errorCode = errorCode;
    	this.status = status;
    	
        }

    public BgipException(String errorCode){
    	super(errorCode);
        }
    
    public BgipException(String code, Throwable cause){
    	super(code, cause);
        }

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}




	
    
    
    
    
}
