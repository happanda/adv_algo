package ds.persistent;

import ds.utility.Pair;

import java.util.NoSuchElementException;

// Kaplan-Okasaki-Trajan deque
public class DequeKOT<T> implements Deque<T> {
    public DequeKOT() {
    }

    public Boolean empty() {
        return head1 == null && head2 == null && head3 == null
                && tail1 == null && tail2 == null && tail3 == null && child == null;
    }

    public DequeKOT<T> pushFront(T value) {
        if (!full(DequeEnd.HEAD))
            return new DequeKOT<T>(value, head1, head2, child, tail1, tail2, tail3);

        if (child == null)
            child = new DequeKOT<Pair<T, T>>();

        Pair<T, T> pair = new Pair<T, T>(head2, head3);

        return new DequeKOT<T>(value, head1, null, child.pushFront(pair), tail1, tail2, tail3);
    }

    public Pair<T, Deque<T>> popFront() throws NoSuchElementException {
        if (!empty(DequeEnd.HEAD)) {
            return new Pair<T, Deque<T>>(head1, new DequeKOT<T>(head2, head3, null, child, tail1, tail2, tail3));
        }
        if (child != null && !child.empty())
        {
            Pair<Pair<T, T>, Deque<Pair<T, T>>> res = child.popFront();
            DequeKOT<T> d = new DequeKOT<T>(res.first.second, null, null,
                    (DequeKOT<Pair<T, T>>)res.second,
                    tail1, tail2, tail3);
            return new Pair<T, Deque<T>>(res.first.first, d);
        }

        if (tail1 != null)
            return new Pair<T, Deque<T>>(tail1, new DequeKOT<T>(null, null, null, null, null, tail2, tail3));
        if (tail2 != null)
            return new Pair<T, Deque<T>>(tail2, new DequeKOT<T>(null, null, null, null, null, null, tail3));
        if (tail3 != null)
            return new Pair<T, Deque<T>>(tail3, new DequeKOT<T>());

        throw new NoSuchElementException("Pop from empty deque");
    }

    public DequeKOT<T> pushBack(T value) {
        if (!full(DequeEnd.TAIL))
            return new DequeKOT<T>(head1, head2, head3, child, tail2, tail3, value);

        if (child == null)
            child = new DequeKOT<Pair<T, T>>();

        Pair<T, T> pair = new Pair<T, T>(tail1, tail2);

        return new DequeKOT<T>(head1, head2, head3, child.pushBack(pair), null, tail3, value);
    }

    public Pair<T, Deque<T>> popBack() throws NoSuchElementException {
        if (!empty(DequeEnd.TAIL)) {
            return new Pair<T, Deque<T>>(tail3, new DequeKOT<T>(head1, head2, head3, child, null, tail1, tail2));
        }
        if (child != null && !child.empty())
        {
            Pair<Pair<T, T>, Deque<Pair<T, T>>> res = child.popBack();
            DequeKOT<T> d = new DequeKOT<T>(head1, head2, head3,
                    (DequeKOT<Pair<T, T>>)res.second,
                    null, null, res.first.first);
            return new Pair<T, Deque<T>>(res.first.second, d);
        }

        if (head3 != null)
            return new Pair<T, Deque<T>>(head3, new DequeKOT<T>(head1, head2, null, null, null, null, null));
        if (head2 != null)
            return new Pair<T, Deque<T>>(head2, new DequeKOT<T>(head1, null, null, null, null, null, null));
        if (head1 != null)
            return new Pair<T, Deque<T>>(head1, new DequeKOT<T>());

        throw new NoSuchElementException("Pop from empty deque");
    }

    enum DequeEnd {
        HEAD, TAIL
    }

    private Boolean empty(DequeEnd end) {
        if (end == DequeEnd.HEAD)
            return head1 == null && head2 == null && head3 == null;
        if (end == DequeEnd.TAIL)
            return tail3 == null && tail2 == null && tail1 == null;
        return false;
    }

    private Boolean full(DequeEnd end) {
        if (end == DequeEnd.HEAD)
            return head3 != null && head2 != null && head1 != null;
        if (end == DequeEnd.TAIL)
            return tail1 != null && tail2 != null && tail3 != null;
        return false;
    }

    private DequeKOT(T head1, T head2, T head3, DequeKOT<Pair<T, T>> child,
                     T tail1, T tail2, T tail3) {
        this.head1 = head1;
        this.head2 = head2;
        this.head3 = head3;
        this.tail1 = tail1;
        this.tail2 = tail2;
        this.tail3 = tail3;
        if (child != null && !child.empty())
            this.child = child;
        else
            this.child = null;
    }

//    private void start() {
//        startTime = System.nanoTime();
//    }
//
//    private void stop() {
//        endTime = System.nanoTime();
//        duration += (endTime - startTime);
//    }
//
//    private long startTime;
//    private long endTime;
//    private long duration;

    private T head1;
    private T head2;
    private T head3;
    private T tail1;
    private T tail2;
    private T tail3;
    private DequeKOT<Pair<T, T>> child;

}
