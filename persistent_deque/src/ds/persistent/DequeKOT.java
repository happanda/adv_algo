package ds.persistent;

import ds.utility.Buffer;
import ds.utility.Pair;

import java.util.NoSuchElementException;

// Kaplan-Okasaki-Trajan deque
public class DequeKOT<T> {
    public DequeKOT() {
        head = new Buffer<T>();
        tail = new Buffer<T>();
    }

    public Boolean empty() {
        return head.empty() && tail.empty() && child == null;
    }

    public DequeKOT<T> pushFront(T value) {
        if (!overflowed(head))
            return new DequeKOT<T>(new Buffer<T>(value, head), child, tail);

        if (child == null)
            child = new DequeKOT<Pair<T, T>>();

        Buffer<T> newHead = head.clone();
        T pairSecond = newHead.popLast();
        T pairFirst  = newHead.popLast();
        Pair<T, T> pair = new Pair<T, T>(pairFirst, pairSecond);

        return new DequeKOT<T>(new Buffer<T>(value, newHead), child.pushFront(pair), tail);
    }

    public Pair<T, DequeKOT<T>> popFront() throws NoSuchElementException {
        if (empty())
            throw new NoSuchElementException("Pop from empty deque");
        if (!empty(head)) {
            Buffer<T> newHead = head.clone();
            T retValue = newHead.popFirst();

            return new Pair<T, DequeKOT<T>>(retValue, new DequeKOT<T>(newHead, child, tail));
        }
        if (child != null)
        {
            Pair<Pair<T, T>, DequeKOT<Pair<T, T>>> res = child.popFront();
            DequeKOT<T> d = new DequeKOT<T>(new Buffer<T>(res.first.second), res.second, tail);
            return new Pair<T, DequeKOT<T>>(res.first.first, d);
        }

        Buffer<T> newTail = tail.clone();
        T retValue = newTail.popFirst();
        return new Pair<T, DequeKOT<T>>(retValue, new DequeKOT<T>(head, null, newTail));
    }

    public DequeKOT<T> pushBack(T value) {
        if (!overflowed(tail))
            return new DequeKOT<T>(head, child, new Buffer<T>(tail, value));

        if (child == null)
            child = new DequeKOT<Pair<T, T>>();

        Buffer<T> newTail = tail.clone();
        T pairFirst  = newTail.popFirst();
        T pairSecond = newTail.popFirst();
        Pair<T, T> pair = new Pair<T, T>(pairFirst, pairSecond);

        return new DequeKOT<T>(head, child.pushBack(pair), new Buffer<T>(newTail, value));
    }

    public Pair<T, DequeKOT<T>> popBack() throws NoSuchElementException {
        if (empty())
            throw new NoSuchElementException("Pop from empty deque");
        if (!empty(tail)) {
            Buffer<T> newTail = tail.clone();
            T retValue = newTail.popLast();

            return new Pair<T, DequeKOT<T>>(retValue, new DequeKOT<T>(head, child, newTail));
        }
        if (child != null)
        {
            Pair<Pair<T, T>, DequeKOT<Pair<T, T>>> res = child.popBack();
            DequeKOT<T> d = new DequeKOT<T>(head, res.second, new Buffer<T>(res.first.first));
            return new Pair<T, DequeKOT<T>>(res.first.second, d);
        }

        Buffer<T> newHead = head.clone();
        T retValue = newHead.popFirst();
        return new Pair<T, DequeKOT<T>>(retValue, new DequeKOT<T>(newHead, null, tail));
    }

    private Boolean empty(Buffer<T> buff) {
        return buff.empty();
    }

    private Boolean overflowed(Buffer<T> buff) {
        return buff.size() >= SIZE;
    }

    private DequeKOT(Buffer<T> head, DequeKOT<Pair<T, T>> child, Buffer<T> tail) {
        this.head  = head;
        if (child != null && !child.empty())
            this.child = child;
        else
            this.child = null;
        this.tail  = tail;
    }

    private Buffer<T> head;
    private Buffer<T> tail;
    private DequeKOT<Pair<T, T>> child;

    private static int SIZE = 3;
}
