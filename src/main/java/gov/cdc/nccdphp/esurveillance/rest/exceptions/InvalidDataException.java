package gov.cdc.nccdphp.esurveillance.rest.exceptions;

public class InvalidDataException extends Exception {
	
	public InvalidDataException(){
		
	}
	
	public InvalidDataException(String message){
		super(message);
	}
}
