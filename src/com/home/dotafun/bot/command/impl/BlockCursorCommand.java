package com.home.dotafun.bot.command.impl;

import com.home.dotafun.bot.command.Command;
import com.home.dotafun.global_variables.DF;
import com.home.dotafun.protocol.messaging.Request;
import com.home.dotafun.protocol.messaging.Response;

import java.io.IOException;

public class BlockCursorCommand implements Command {

    private String getUsage() {
        return "Usage: block_cursor [argument]\n" +
                "Arguments:\n" +
                "   on - enable block\n" +
                "   off - disable block";
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
                DF.dfRobot.blockCursor();
                response.writeMessage("success!");
                break;
            case "off":
                DF.dfRobot.unblockCursor();
                response.writeMessage("success!");
                break;
            default:
                response.writeMessage(getUsage());
        }
    }
}
