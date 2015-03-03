package com.home.dotafun.bot;

import com.home.dotafun.bot.command.Command;
import com.home.dotafun.bot.exception.BotException;
import com.home.dotafun.protocol.messaging.Request;
import com.home.dotafun.protocol.messaging.Response;

import java.io.IOException;
import java.util.Map;

public interface Bot {

    public void addCommand(String name, Command command);

    public void addCommand(Map<String, Command> commands);

    public void removeCommand(String name);

    public void clearCommands();

    public void executeCommad(Request request, Response response) throws BotException, IOException;

}
