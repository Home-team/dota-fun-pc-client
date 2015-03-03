package com.home.dotafun.bot.command.impl;

import com.home.dotafun.bot.command.Command;
import com.home.dotafun.global_variables.DF;
import com.home.dotafun.protocol.messaging.Request;
import com.home.dotafun.protocol.messaging.Response;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BugsKeyboardCommand implements Command {

    private String getUsage() {
        return "Usage: bugs_keyboard [argument]\n" +
                "Arguments:\n" +
                "   on - enable bugs\n" +
                "   off - disable bugs";
    }

    @Override
    public void execute(Request request, Response response) throws IOException {
        String[] args = request.getArguments();
        if (args == null) {
            response.writeMessage(getUsage());
            return;
        }
        switch (args[0]) {
            case "on":
                List<Integer> keys = new ArrayList<Integer>();
                keys.add(KeyEvent.VK_ALT);
                keys.add(KeyEvent.VK_SHIFT);
                keys.add(KeyEvent.VK_CAPS_LOCK);
                keys.add(KeyEvent.VK_TAB);
                DF.dfRobot.holdKeys(keys);
                response.writeMessage("success!");
                break;
            case "off":
                System.out.println("off");
                DF.dfRobot.unholdKeys();
                response.writeMessage("success!");
                break;
            default:
                response.writeMessage(getUsage());
        }
    }
}
