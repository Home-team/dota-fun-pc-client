package com.home.dotafun.protocol.messaging.handler;

import com.home.dotafun.protocol.messaging.Request;
import com.home.dotafun.protocol.messaging.Response;

public interface MessageHandler {

    public void processMessage(Request request, Response response);
}
