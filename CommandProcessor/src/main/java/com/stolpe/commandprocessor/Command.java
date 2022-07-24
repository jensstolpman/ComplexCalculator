package com.stolpe.commandprocessor;

import java.util.ArrayList;
import java.util.List;

public class Command {
    private String value = "0";
    private String command = "";
    private List<String> logs = new ArrayList<>();
    private List<String> errors = new ArrayList<>();

    public List<String> getStack() {
        return stack;
    }

    public void setStack(List<String> stack) {
        this.stack = stack;
    }

    private List<String> stack = new ArrayList<>();

    private int newEnter = 1;

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

    public int getNewEnter() {
        return newEnter;
    }

    public void setNewEnter(int isEnter) {
        this.newEnter = isEnter;
    }
}
