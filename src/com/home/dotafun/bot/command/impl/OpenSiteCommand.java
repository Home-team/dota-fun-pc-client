package com.home.dotafun.bot.command.impl;

import com.home.dotafun.bot.command.Command;
import com.home.dotafun.protocol.messaging.Request;
import com.home.dotafun.protocol.messaging.Response;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpenSiteCommand implements Command {

    private String getUsage() {
        return "Usage: open_site [argument]\n" +
                "Arguments:\n" +
                "   any valid url";
    }

    @Override
    public void execute(Request request, Response response) throws IOException {
        String[] args = request.getArguments();
        if (args == null) {
            response.writeMessage(getUsage());
            return;
        }
        String regexp = "^(https?|ftp)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(args[0]);
        if (matcher.matches()) {
            try {
                Desktop.getDesktop().browse(new URI(args[0]));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            response.writeMessage("success!");
        } else {
            response.writeMessage("invalid url");
        }
    }
}
