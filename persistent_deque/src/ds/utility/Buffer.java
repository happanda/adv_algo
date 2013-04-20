package ds.utility;

import java.util.ArrayDeque;

public class Buffer<T> {
    public Buffer() {
        data = new ArrayDeque<T>();
    }

    public Buffer(T value) {
        data = new ArrayDeque<T>();
        data.addFirst(value);
    }

    public Buffer(final T addFirst, final Buffer<T> copy) {
        data = copy.data.clone();
        data.addFirst(addFirst);
    }

    public Buffer(final Buffer<T> copy, final T addLast) {
        data = copy.data.clone();
        data.addLast(addLast);
    }

    public T popFirst() {
        return data.pollFirst();
    }

    public T popLast() {
        return data.pollLast();
    }

    public Boolean empty() {
        return data.isEmpty();
    }

    public int size() {
        return data.size();
    }

    public Buffer<T> clone() {
        Buffer<T> buf = new Buffer<T>();
        buf.data = data.clone();
        return buf;
    }

    private ArrayDeque<T> data;
}
