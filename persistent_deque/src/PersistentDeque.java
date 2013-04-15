import java.lang.Exception;

class PersistentDeque<T> {
    public PersistentDeque(){
    }

    public Boolean empty() {
        return head == null && tail == null && child == null;
    }

    public PersistentDeque<T> push(T value) {
        if (empty())
            return new PersistentDeque<T>(value, null, null);
        if (head == null)
            return new PersistentDeque<T>(value, child, tail);
        if (child == null)
            child = new PersistentDeque<Pair<T, T>>();
        return new PersistentDeque<T>(null, child.push(new Pair<T, T>(value, head)), tail);
    }

    public Pair<T, PersistentDeque<T>> pop() throws Exception {
        if (empty())
            throw new Exception("Pop from empty deque");
        if (head != null)
            return new Pair<T, PersistentDeque<T>>(head, new PersistentDeque<T>(null, child, tail));
        if (child != null)
        {
            Pair<Pair<T, T>, PersistentDeque<Pair<T, T>>> res = child.pop();
            return new Pair<T, PersistentDeque<T>>(res.first.first, new PersistentDeque<T>(res.first.second, res.second, tail));
        }
        return new Pair<T, PersistentDeque<T>>(tail, new PersistentDeque<T>());
    }

    private PersistentDeque(T head, PersistentDeque<Pair<T, T>> child, T tail) {
        this.head  = head;
        this.child = child;
        this.tail  = tail;
    }
    
    private T head;
    private T tail;
    private PersistentDeque<Pair<T, T>> child;
    
};
