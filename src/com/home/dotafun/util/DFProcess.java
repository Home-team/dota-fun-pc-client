package com.home.dotafun.util;

import com.home.dotafun.protocol.messaging.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DFProcess {
    private Process process;
    private volatile boolean isAlive;
    private String encoding;

    public DFProcess(String command) throws IOException {
        encoding = "cp866";
        Runtime runtime = Runtime.getRuntime();
        process = runtime.exec(command);
        isAlive = true;
        new LifeCycleThread();
    }

    public DFProcess(String command, String encoding) throws IOException {
        this.encoding = encoding;
        Runtime runtime = Runtime.getRuntime();
        process = runtime.exec(command);
        isAlive = true;
        new LifeCycleThread();
    }

    public void wrireStdin(String request) throws IOException {
        process.getOutputStream().write(request.getBytes(encoding));
        process.getOutputStream().flush();
    }

    public void readStdout(Response response) {
        new StdoutThread(response);
    }

    public void readStderr(Response response) {
        new StderrThread(response);
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void destroy() {
        process.destroy();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DFProcess dfProcess = (DFProcess) o;

        if (isAlive != dfProcess.isAlive) return false;
        if (encoding != null ? !encoding.equals(dfProcess.encoding) : dfProcess.encoding != null) return false;
        if (process != null ? !process.equals(dfProcess.process) : dfProcess.process != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = process != null ? process.hashCode() : 0;
        result = 31 * result + (isAlive ? 1 : 0);
        result = 31 * result + (encoding != null ? encoding.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DFProcess{" +
                "process=" + process +
                ", isAlive=" + isAlive +
                ", encoding='" + encoding + '\'' +
                '}';
    }

    private class StderrThread implements Runnable {
        private Response response;

        private StderrThread(Response response) {
            this.response = response;
            Thread thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();
        }

        @Override
        public void run() {
            String line = null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream(), encoding))) {
                while ((line = reader.readLine()) != null) {
                    response.writeMessage(line);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class StdoutThread implements Runnable {
        private Response response;

        private StdoutThread(Response response) {
            this.response = response;
            Thread thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();
        }

        @Override
        public void run() {
            String line = null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), encoding))) {
                while ((line = reader.readLine()) != null) {
                    response.writeMessage(line);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class LifeCycleThread implements Runnable {

        private LifeCycleThread() {
            Thread thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();
        }

        @Override
        public void run() {
            try {
                process.waitFor();
                process.getErrorStream().close();
                process.getOutputStream().close();
                isAlive = false;
            } catch (InterruptedException | IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
