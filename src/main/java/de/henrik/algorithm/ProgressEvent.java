package de.henrik.algorithm;

public class ProgressEvent {
    float newProgress;

    public ProgressEvent(float newProgress) {
        this.newProgress = newProgress;
    }

    public float getProgress() {
        return newProgress;
    }
}
