package tests.ds.persistent;

import ds.persistent.DequeKOT;
import ds.persistent.PersistentDeque;


public class DequeBenchmark {

    public DequeBenchmark() {
        persDeque = new PersistentDeque<Integer>();
        dequeKot  = new DequeKOT<Integer>();
    }

    public void BenchPushPopOneSide() {
    }

    private PersistentDeque<Integer> persDeque;
    private DequeKOT<Integer>        dequeKot;
}
