package com.home.dotafun.bot.command.impl;

import com.home.dotafun.bot.command.Command;
import com.home.dotafun.main.Main;
import com.home.dotafun.protocol.messaging.Request;
import com.home.dotafun.protocol.messaging.Response;

public class ExitCommand implements Command {

    @Override
    public void execute(Request request, Response response) {
        Main.resume();
    }
}
