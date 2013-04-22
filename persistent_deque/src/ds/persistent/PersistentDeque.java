package ds.persistent;

import ds.utility.Pair;
import java.util.NoSuchElementException;

public class PersistentDeque<T> implements Deque<T> {
    public PersistentDeque() {
    }

    public Boolean empty() {
        return head == null && tail == null && child == null;
    }

    public PersistentDeque<T> pushFront(T value) {
        if (empty())
            return new PersistentDeque<T>(value, null, null);
        if (head == null)
            return new PersistentDeque<T>(value, child, tail);
        if (child == null)
            child = new PersistentDeque<Pair<T, T>>();
        return new PersistentDeque<T>(null, child.pushFront(new Pair<T, T>(value, head)), tail);
    }

    public Pair<T, Deque<T>> popFront() throws NoSuchElementException {
        if (empty())
            throw new NoSuchElementException("Pop from empty deque");
        if (head != null)
            return new Pair<T, Deque<T>>(head, new PersistentDeque<T>(null, child, tail));
        if (child != null && !child.empty())
        {
            Pair<Pair<T, T>, Deque<Pair<T, T>>> res = child.popFront();
            PersistentDeque<T> d = new PersistentDeque<T>(res.first.second, (PersistentDeque<Pair<T, T>>)res.second, tail);

            return new Pair<T, Deque<T>>(res.first.first, d);
        }
        return new Pair<T, Deque<T>>(tail, new PersistentDeque<T>());
    }

    public PersistentDeque<T> pushBack(T value) {
        if (empty())
            return new PersistentDeque<T>(null, null, value);
        if (tail == null)
            return new PersistentDeque<T>(head, child, value);
        if (child == null)
            child = new PersistentDeque<Pair<T, T>>();
        return new PersistentDeque<T>(head, child.pushBack(new Pair<T, T>(tail, value)), null);
    }

    public Pair<T, Deque<T>> popBack() throws NoSuchElementException {
        if (empty())
            throw new NoSuchElementException("Pop from empty deque");
        if (tail != null)
            return new Pair<T, Deque<T>>(tail, new PersistentDeque<T>(head, child, null));
        if (child != null && !child.empty())
        {
            Pair<Pair<T, T>, Deque<Pair<T, T>>> res = child.popBack();
            PersistentDeque<T> d = new PersistentDeque<T>(head, (PersistentDeque<Pair<T, T>>)res.second, res.first.first);

            return new Pair<T, Deque<T>>(res.first.second, d);
        }
        return new Pair<T, Deque<T>>(head, new PersistentDeque<T>());
    }

    private PersistentDeque(T head, PersistentDeque<Pair<T, T>> child, T tail) {
        this.head  = head;
        if (child != null && !child.empty())
            this.child = child;
        else
            this.child = null;
        this.tail  = tail;
    }

    private T head;
    private T tail;
    private PersistentDeque<Pair<T, T>> child;

};
