package tests.ds.persistent;

import ds.persistent.Deque;
import ds.persistent.DequeKOT;
import ds.persistent.PersistentDeque;


public class DequeBenchmark<DequeType extends Deque<Integer>> {

    public DequeBenchmark(Class<DequeType> clazz) {
        this.clazz = clazz;
        try {
            deque = clazz.newInstance();
        }
        catch (InstantiationException ex) {
            System.out.println("Can't create instance of " + clazz + ": " + ex.getMessage());
        }
        catch (IllegalAccessException ex) {
            System.out.println("Can't create instance of " + clazz + ": " + ex.getMessage());
        }
    }

    public void BenchPushPopOneSide() {
        measure(new Meter() {
            public String name() {
                return "PushPopOneSide";
            }
            public void action() {
                for (int i = 0; i < NUM_OPERATIONS; ++i) {
                    deque = (DequeType)deque.pushFront(i);
                    deque = (DequeType)deque.popFront().second;
                }
            }
        });
    }

    public void BenchPushPopOppositeSide() {
        measure(new Meter() {
            public String name() {
                return "PushPopOneSide";
            }
            public void action() {
                for (int i = 0; i < NUM_OPERATIONS; ++i) {
                    deque = (DequeType)deque.pushFront(i);
                    deque = (DequeType)deque.popBack().second;
                }
            }
        });
    }

    public void BenchFillEmpty() {
        measure(new Meter() {
            public String name() {
                return "PushPopOneSide";
            }
            public void action() {
                for (int i = 0; i < NUM_OPERATIONS; ++i) {
                    if (i % 2 == 0)
                        deque = (DequeType)deque.pushFront(i);
                    else
                        deque = (DequeType)deque.pushBack(i);
                }
            }
        });
    }

    interface Meter {
        public String name();
        public void action();
    }

    private void measure(Meter meter) {
        System.out.println(meter.name());
        start();
        meter.action();
        stop();
    }

    private void start() {
        startTime = System.nanoTime();
    }

    private void stop() {
        endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Time: " + (duration / 1000000) + " ms (" + duration + " ns)");
    }

    private long startTime;
    private long endTime;

    private Class<DequeType> clazz;
    private DequeType        deque;

    private int NUM_OPERATIONS = 90000000;
}
