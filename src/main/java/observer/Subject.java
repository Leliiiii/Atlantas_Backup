package observer;

import java.util.ArrayList;
import java.util.List;

public class Subject {
    private final List<Observer> observers = new ArrayList<>();

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String eventName, Object eventData) {

        // Avvisa tutti gli observer
        for (int i = 0; i < observers.size(); i++) {
            observers.get(i).update(eventName, eventData);
        }
    }
}
