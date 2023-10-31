package de.henrik.gui;

import de.henrik.algorithm.ProgressEvent;

import java.util.EventListener;

public interface ProgressListener extends EventListener {
    public void progressChanged(ProgressEvent e);
}
