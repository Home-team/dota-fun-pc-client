package com.home.dotafun.bot.command.impl;

import com.home.dotafun.bot.command.Command;
import com.home.dotafun.global_variables.DF;
import com.home.dotafun.protocol.messaging.Request;
import com.home.dotafun.protocol.messaging.Response;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShowDesktopCommand implements Command {

    @Override
    public void execute(Request request, Response response) throws IOException {
        List<Integer> keys = new ArrayList<Integer>();
        keys.add(KeyEvent.VK_WINDOWS);
        keys.add(KeyEvent.VK_D);
        DF.dfRobot.clickKeysShortcut(keys);
        response.writeMessage("success!");
    }
}
