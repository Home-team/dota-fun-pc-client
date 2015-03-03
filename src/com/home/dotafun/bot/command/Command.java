package com.home.dotafun.bot.command;

import com.home.dotafun.bot.exception.BotException;
import com.home.dotafun.protocol.messaging.Request;
import com.home.dotafun.protocol.messaging.Response;

import java.io.IOException;

public interface Command {

    public void execute(Request request, Response response) throws BotException, IOException;
}
