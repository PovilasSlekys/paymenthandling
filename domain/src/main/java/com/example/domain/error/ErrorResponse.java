package com.example.domain.error;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@JsonPropertyOrder({"code", "messages"})
public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = 4303096619052955617L;
    private final String code;
    private final List<String> messages;

    public ErrorResponse(String code, Throwable throwable) {
        this.messages = new LinkedList<>();
        this.code = code;
        this.setStandardInformation(throwable);
    }

    @JsonCreator
    public ErrorResponse(@JsonProperty("code") String code, @JsonProperty("message") List<String> messages) {
        this.messages = new LinkedList<>();
        this.code = code;
        messages.forEach(this::addMessage);
    }

    @JsonProperty("code")
    public String getCode() {return this.code;}

    @JsonProperty("messages")
    public List<String> getMessages() {return this.messages;}

    @JsonIgnore
    private void setStandardInformation(Throwable exception) {
        this.addMessage("exceptionThrownBy",
                exception.getStackTrace()[0].getClassName() + "." + exception.getStackTrace()[0].getMethodName());
        this.addMessage("exceptionClass", exception.getClass().getName());
        if (exception.getMessage() != null) {
            this.addMessage("exceptionMessage", exception.getMessage());
        }
    }

    @JsonIgnore
    private void addMessage(String dataName, String dataValue) {
        this.messages.add(dataName + " [" + dataValue + "]");
    }

    @JsonIgnore
    private void addMessage(String errorMessage) {
        this.messages.add(errorMessage);
    }
}
