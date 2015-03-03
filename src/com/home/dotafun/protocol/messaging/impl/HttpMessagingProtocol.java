package com.home.dotafun.protocol.messaging.impl;

import com.home.dotafun.protocol.messaging.handler.MessageHandler;
import com.home.dotafun.protocol.messaging.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class HttpMessagingProtocol implements MessagingProtocol {
    private Map<String, String> requests;
    private String server;
    private String id;

    public HttpMessagingProtocol(String server, String id) {
        this.server = server;
        this.id = id;
        requests = new HashMap<String, String>();
        requests.put("send_message", "sendmsg");
        requests.put("registration", "registration");
        requests.put("get_message", "getmsg");
    }

    public HttpMessagingProtocol(Map<String, String> requests, String server, String id) {
        this.requests = requests;
        this.server = server;
        this.id = id;
    }

    private String sendGetRequest(String urlStr) throws IOException {
        HttpURLConnection connection = null;
        URL url = new URL(urlStr);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = bufferedReader.readLine()) != null) {
            response.append(inputLine);
        }
        bufferedReader.close();
        return response.toString();
    }

    @Override
    public void sendMessage(String to, String msg) throws IOException {
        if (to == null || msg == null) {
            throw new NullPointerException();
        }
        StringBuffer request = new StringBuffer();
        request.append(server).append('/').append(requests.get("send_message")).append("?to=")
                .append(to).append("?id=").append(id).append("&msg=").append(msg);
        sendGetRequest(request.toString());
    }

    @Override
    public void addMessageHandler(MessageHandler messageHandler) {
        new InterviewThread(messageHandler);
    }

    public String registration(String uid) throws Exception {
        StringBuffer request = new StringBuffer();
        request.append(server).append('/').append(requests.get("registration")).append("?uid=").append(uid);
        return sendGetRequest(request.toString());
    }

    public Map<String, String> getRequests() {
        return requests;
    }

    public String getServer() {
        return server;
    }

    public String getId() {
        return id;
    }

    public void setRequests(Map<String, String> requests) {
        this.requests = requests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HttpMessagingProtocol)) return false;

        HttpMessagingProtocol httpProtocol = (HttpMessagingProtocol) o;

        if (id != null ? !id.equals(httpProtocol.id) : httpProtocol.id != null) return false;
        if (requests != null ? !requests.equals(httpProtocol.requests) : httpProtocol.requests != null) return false;
        if (server != null ? !server.equals(httpProtocol.server) : httpProtocol.server != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = requests != null ? requests.hashCode() : 0;
        result = 31 * result + (server != null ? server.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Http{");
        sb.append("requests=").append(requests);
        sb.append(", server='").append(server).append('\'');
        sb.append(", id='").append(id).append('\'');
        sb.append('}');
        return sb.toString();
    }

    private class InterviewThread implements Runnable {
        private MessageHandler messageHandler;
        private ExecutorService executorService;

        private InterviewThread(MessageHandler messageHandler) {
            this.executorService = Executors.newCachedThreadPool(new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setDaemon(true);
                    return thread;
                }
            });
            this.messageHandler = messageHandler;
            Thread thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();
        }

        @Override
        public void run() {
            String url = server + '/' + requests.get("get_message") + "?id=" + id;
            while (true) {
                try {
                    final String responseFromServer = sendGetRequest(url);
                    if (!"".equals(responseFromServer) && responseFromServer != null) {
                        executorService.submit(new Runnable() {
                            @Override
                            public void run() {
                                Request request = createRequest(responseFromServer);
                                Response response = createResponse();
                                response.setTo(request.getFrom());
                                messageHandler.processMessage(request, response);
                            }
                        });
                    }
                    Thread.sleep(300);
                } catch (IOException | InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        private Request createRequest(String responseFromServer) {
            if (responseFromServer == null) {
                throw new NullPointerException();
            }
            String[] strArray = null;
            strArray = responseFromServer.split(";", 2);
            String from = strArray[0];
            String message = strArray[1];

            strArray = message.trim().toLowerCase().split(" ", 2);
            String command = null;
            String[] args = null;
            if (strArray.length > 0) {
                command = strArray[0];
            }
            if (strArray.length > 1) {
                args = strArray[1].split(" ");
            }
            return new Request(from, message, command, args);
        }

        private Response createResponse() {
            return new Response(HttpMessagingProtocol.this);
        }
    }
}
