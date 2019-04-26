package gov.cdc.nccdphp.esurveillance.rest.model;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Data;

@Data
public class ErrorReceipt {
    private ERROR_CODES code;

    private String timestamp;
    private String descritpion;
    private int status;
    private String path;
    private String exception;

    public ErrorReceipt() {
        this.timestamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT);
    }

    public ErrorReceipt(ERROR_CODES code, String descripion, int status, String path, String exception) {
        this();
        this.code = code;
        this.descritpion = descripion;
        this.status = status;
        this.path = path;
        this.exception = exception;
    }

}


