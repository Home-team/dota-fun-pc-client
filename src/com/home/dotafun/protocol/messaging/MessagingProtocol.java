package com.home.dotafun.protocol.messaging;

import com.home.dotafun.protocol.messaging.handler.MessageHandler;
import com.home.dotafun.protocol.Protocol;

import java.io.IOException;

public interface MessagingProtocol extends Protocol {

    public void sendMessage(String username, String msg) throws IOException;

    public void addMessageHandler(final MessageHandler messageHandler);
}
