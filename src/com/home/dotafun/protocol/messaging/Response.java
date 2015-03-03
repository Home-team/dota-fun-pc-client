package com.home.dotafun.protocol.messaging;

import java.io.IOException;

public class Response {
    private String to;
    private MessagingProtocol messagingProtocol;

    public Response(MessagingProtocol messagingProtocol) {
        this.messagingProtocol = messagingProtocol;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    public void writeMessage(String message) throws IOException {
        messagingProtocol.sendMessage(to, message);
    }
}
