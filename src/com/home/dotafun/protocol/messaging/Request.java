package com.home.dotafun.protocol.messaging;

public class Request {
    private String from;
    private String message;
    private String command;
    private String[] arguments;

    public Request(String from, String message, String command, String[] arguments) {
        this.from = from;
        this.message = message;
        this.command = command;
        this.arguments = arguments;
    }

    public String getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }

    public String getCommand() {
        return command;
    }

    public String[] getArguments() {
        return arguments;
    }
}
