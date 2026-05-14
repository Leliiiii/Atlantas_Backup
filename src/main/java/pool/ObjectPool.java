package pool;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ObjectPool<T extends Poolable> {
    private final List<T> available;
    private final List<T> inUse;
    private final Supplier<T> factory;
    private final int maxSize;

    public ObjectPool(Supplier<T> factory, int initialSize, int maxSize) {
        this.factory = factory;
        this.maxSize = maxSize;
        this.available = new ArrayList<>();
        this.inUse = new ArrayList<>();

        for (int i = 0; i < initialSize; i++) {
            available.add(factory.get());
        }
    }

    public T obtain() {
        T obj;
        if (!available.isEmpty()) {
            obj = available.remove(available.size() - 1);
        } else if (inUse.size() < maxSize) {
            obj = factory.get();
        } else {
            return null;
        }
        obj.reset();
        inUse.add(obj);
        return obj;
    }

    public void free(T obj) {
        if (inUse.remove(obj)) {
            obj.reset();
            available.add(obj);
        }
    }

    public void freeAll() {
        for (T obj : new ArrayList<>(inUse)) {
            free(obj);
        }
    }

    public List<T> getInUse() {
        return new ArrayList<>(inUse);
    }
}
