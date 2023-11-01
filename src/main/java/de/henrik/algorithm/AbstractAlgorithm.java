package de.henrik.algorithm;

import de.henrik.gui.ProgressListener;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractAlgorithm implements Runnable, Algorithm {

    protected static boolean verbose = false;
    protected static boolean oneStep = false;
    protected static boolean pause = false;
    protected static boolean slow = false;
    protected static boolean cancel = false;
    protected static int SLOW_TIME = 100;
    private static AbstractAlgorithm algorithm = null;
    protected final List<Runnable> onFinishListener = new ArrayList<>();

    protected final List<ProgressListener> progressListeners = new ArrayList<>();

    protected final Long seed;

    protected AbstractAlgorithm(long seed) {
        this.seed = seed;
    }

    public void addProgressListener(ProgressListener l) {
        progressListeners.add(l);
    }

    protected abstract void fireStateChanged();


    public static void step() {
        AbstractAlgorithm.pause = false;
        AbstractAlgorithm.oneStep = true;
        if (algorithm != null) {
            algorithm.stepInstance();
        }
    }

    public static void cancel() {
        if (algorithm != null) {
            AbstractAlgorithm.cancel = true;
        }
    }


    protected synchronized void stepInstance() {
        if (algorithm != null) algorithm.notify();
    }

    public static void resume() {
        AbstractAlgorithm.pause = false;
        if (algorithm != null) {
            algorithm.resumeInstance();
        }
    }

    protected synchronized void resumeInstance() {
        if (algorithm != null) algorithm.notify();
    }

    public static void pause() {
        AbstractAlgorithm.pause = true;
    }

    public static synchronized void setSlow(boolean slow) {
        AbstractAlgorithm.slow = slow;
    }

    public static void setVerbose(boolean verbose) {
        AbstractAlgorithm.verbose = verbose;
    }

    public static void setSpeed(int value) {
        AbstractAlgorithm.SLOW_TIME = value;
    }

    protected abstract void runAlgorithm();

    @Override
    public void run() {
        if (algorithm == null) {
            AbstractAlgorithm.algorithm = this;
            System.out.println("Starting Algorithm with seed " + seed + " slow " + slow + " verbose " + verbose);
            var time = System.currentTimeMillis();
            runAlgorithm();
            System.out.println("Algorithm finished in " + (System.currentTimeMillis() - time) + "ms");
            callFinishListener();
            AbstractAlgorithm.algorithm = null;
        }
    }

    public static boolean isRunning() {
        return algorithm != null;
    }

    protected void checkPause() {
        synchronized (this) {
            if (cancel) {
                AbstractAlgorithm.cancel = false;
                AbstractAlgorithm.algorithm = null;
                callFinishListener();
                throw new RuntimeException("Algorithm canceled");
            }
            while (pause && !oneStep) {
                try {
                    wait(100);
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (oneStep) {
                oneStep = false;
                pause = true;
            }
            if (slow) try {
                wait(SLOW_TIME);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void onFinish(Runnable r) {
        onFinishListener.add(r);
    }

    private void callFinishListener() {
        for (var r : onFinishListener) {
            r.run();
        }
    }
}

