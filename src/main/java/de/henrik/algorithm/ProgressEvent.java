package de.henrik.algorithm;

public class ProgressEvent {
    private final float newProgress;

    public ProgressEvent(float newProgress) {
        this.newProgress = newProgress;
    }

    public float getProgress() {
        return newProgress;
    }
}
