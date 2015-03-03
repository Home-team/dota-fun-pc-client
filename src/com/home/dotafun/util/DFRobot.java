package com.home.dotafun.util;

import java.awt.*;
import java.util.List;

public class DFRobot {
    private Robot robot;
    private Thread holderKeyThread;
    private Thread blockerCursorThread;

    public DFRobot() throws AWTException {
        robot = new Robot();
        holderKeyThread = null;
        blockerCursorThread = null;
    }

    private void BresenhamLineAlgorithm(Point point1, Point point2, Robot robot) {
        int x = point1.x;
        int y = point1.y;
        int dx = Math.abs(point2.x - point1.x), dy = Math.abs(point2.y - point1.y);
        int sx = (point2.x - point1.x) > 0 ? 1 : ((point2.x - point1.x) == 0 ? 0 : -1);
        int sy = (point2.y - point1.y) > 0 ? 1 : ((point2.y - point1.y) == 0 ? 0 : -1);
        int tx = -1;
        int ty = -1;
        if (dx >= dy) {
            tx = sx;
            ty = 0;
        } else {
            int z = dx;
            dx = dy;
            dy = z;
            tx = 0;
            ty = sy;
        }
        int scount = 2 * dy;
        int count = scount - dx;
        int dcount = count - dx;
        for (; ; ) {
            dx -= 1;
            if (dx < -1) break;
            robot.mouseMove(x, y);
            if (count >= 0) {
                x += sx;
                y += sy;
                count += dcount;
            } else {
                x += tx;
                y += ty;
                count += scount;
            }
        }
    }

    public Point getCursorPosition() {
        return MouseInfo.getPointerInfo().getLocation();
    }

    public void setCursorPosition(Point point) {
        robot.mouseMove(point.x, point.y);
    }

    public void moveCursorToPosition(Point pointToMove) {
        BresenhamLineAlgorithm(getCursorPosition(), pointToMove, robot);
    }

    public void blockCursor() {
        if (blockerCursorThread != null) {
            return;
        }
        blockerCursorThread = new Thread(new BlockerCursorRunnable());
        blockerCursorThread.setDaemon(true);
        blockerCursorThread.start();
    }

    public void unblockCursor() {
        if (blockerCursorThread == null) {
            throw new NullPointerException();
        }
        while (blockerCursorThread.isAlive()) {
            blockerCursorThread.interrupt();
        }
        blockerCursorThread = null;
    }

    public void clickKey(int key) {
        robot.keyPress(key);
        robot.keyRelease(key);
    }

    public void clickKeys(List<Integer> keys) {
        for (Integer key : keys) {
            robot.keyPress(key);
            robot.keyRelease(key);
        }
    }

    public void clickKeysShortcut(List<Integer> keys) {
        for (Integer key : keys) {
            robot.keyPress(key);
        }
        for (Integer key : keys) {
            robot.keyRelease(key);
        }
    }

    public void unholdKeys() {
        if (holderKeyThread == null) {
            throw new NullPointerException();
        }
        while (holderKeyThread.isAlive()) {
            holderKeyThread.interrupt();
        }
        holderKeyThread = null;
    }

    public void holdKeys(List<Integer> keys) {
        if (holderKeyThread != null) {
            return;
        }
        holderKeyThread = new Thread(new HolderKeyRunnable(keys));
        holderKeyThread.setDaemon(true);
        holderKeyThread.start();
    }

    public void holdKey(int key) {
        if (holderKeyThread != null) {
            return;
        }
        holderKeyThread = new Thread(new HolderKeyRunnable(key));
        holderKeyThread.setDaemon(true);
        holderKeyThread.start();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DFRobot dfRobot = (DFRobot) o;

        if (holderKeyThread != null ? !holderKeyThread.equals(dfRobot.holderKeyThread) : dfRobot.holderKeyThread != null)
            return false;
        if (robot != null ? !robot.equals(dfRobot.robot) : dfRobot.robot != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = robot != null ? robot.hashCode() : 0;
        result = 31 * result + (holderKeyThread != null ? holderKeyThread.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DFRobot{" +
                "robot=" + robot +
                ", holderKeyThread=" + holderKeyThread +
                '}';
    }

    private class BlockerCursorRunnable implements Runnable {

        @Override
        public void run() {
            Point point = getCursorPosition();
            while (!Thread.currentThread().isInterrupted()) {
                setCursorPosition(point);
            }
        }
    }

    private class HolderKeyRunnable implements Runnable {
        int key;
        List<Integer> keys;

        private HolderKeyRunnable(List<Integer> keys) {
            this.key = -1;
            this.keys = keys;
        }

        private HolderKeyRunnable(int key) {
            this.key = key;
            this.keys = null;
        }

        @Override
        public void run() {
            if (keys == null) {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        robot.keyPress(key);
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                } finally {
                    robot.keyRelease(key);
                }
            } else {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        for (Integer key : keys) {
                            robot.keyPress(key);
                        }
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                } finally {
                    for (Integer key : keys) {
                        robot.keyRelease(key);
                    }
                }
            }
        }
    }
}