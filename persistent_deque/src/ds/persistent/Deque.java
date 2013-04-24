package ds.persistent;

import ds.utility.Pair;

import java.util.NoSuchElementException;


public interface Deque<T> {
    /// TODO: is it possible to use Deque<T, Derived extends Deque<T, Derived>>
    /// and dynamically create Deques in runtime?

    Boolean empty();

    Deque<T> pushFront(T value);
    Deque<T> pushBack (T value);

    Pair<T, Deque<T>> popFront() throws NoSuchElementException;
    Pair<T, Deque<T>> popBack () throws NoSuchElementException;
}
