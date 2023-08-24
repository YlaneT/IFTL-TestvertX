package com.example;

import java.util.Observable;
import java.util.Observer;

public class ObserverShutdown implements Observer {
    
    public void observe (Observable o) {
        o.addObserver(this);
    }
    
    @Override
    public void update (Observable o, Object arg) {
        if (((ObservableShutdown) o).isShutdownCalled()) {
            Main.closeVertx();
        }
    }
}
