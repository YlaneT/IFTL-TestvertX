package com.example;

import java.util.Observable;

public class ObservableShutdown extends Observable {
    private boolean shutdownCalled = false;
    
    public void setShutdownCalled (boolean shutdownCalled) {
        synchronized (this) {
            this.shutdownCalled = shutdownCalled;
        }
        setChanged();
        notifyObservers();
    }
    
    public boolean isShutdownCalled () {
        return shutdownCalled;
    }
}
