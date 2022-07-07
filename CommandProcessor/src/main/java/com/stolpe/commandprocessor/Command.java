package com.stolpe.commandprocessor;

import java.util.ArrayList;
import java.util.List;

public class Command {
    private String value = null;
    private String command = null;
    private List<String> logs = new ArrayList<>();
    private List<String> errors = new ArrayList<>();
    private String port = null;

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
