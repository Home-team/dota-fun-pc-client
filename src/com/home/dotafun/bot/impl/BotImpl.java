package com.home.dotafun.bot.impl;

import com.home.dotafun.bot.Bot;
import com.home.dotafun.bot.command.Command;
import com.home.dotafun.bot.exception.BotException;
import com.home.dotafun.protocol.messaging.Request;
import com.home.dotafun.protocol.messaging.Response;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BotImpl implements Bot {

    private ConcurrentMap<String, Command> commands;

    public BotImpl() {
        commands = new ConcurrentHashMap<String, Command>();
    }

    @Override
    public void addCommand(String name, Command command) {
        commands.putIfAbsent(name, command);
    }

    @Override
    public void addCommand(Map<String, Command> commands) {
        this.commands.putAll(commands);
    }

    @Override
    public void removeCommand(String name) {
        commands.remove(name);
    }

    @Override
    public void clearCommands() {
        commands.clear();
    }

    @Override
    public void executeCommad(Request request, Response response) throws BotException, IOException {
        if(commands.containsKey(request.getCommand())) {
            commands.get(request.getCommand()).execute(request, response);
        } else {
            response.writeMessage(request.getCommand() + ": command not found");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BotImpl)) return false;

        BotImpl bot = (BotImpl) o;
        return !(commands != null ? !commands.equals(bot.commands) : bot.commands != null);
    }

    @Override
    public int hashCode() {
        return commands != null ? commands.hashCode() : 0;
    }
}
