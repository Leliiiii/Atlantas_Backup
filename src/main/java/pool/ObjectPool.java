package pool;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

// Voglio solo gli oggetti di Poolable
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

    public void free(T obj) {
        if (inUse.remove(obj)) {
            obj.reset();
            available.add(obj);
        }
    }

    //TODO "FREE ALL" IN CASO

    public List<T> getInUse() {
        return new ArrayList<>(inUse);
    }
}
