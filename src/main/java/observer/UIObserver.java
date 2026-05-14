package observer;

public class UIObserver implements Observer {
    private final Runnable updateAction;

    public UIObserver(Runnable updateAction) {
        this.updateAction = updateAction;
    }

    @Override
    public void update(String event, Object data) {
        if (updateAction != null) {
            updateAction.run();
        }
    }
}
