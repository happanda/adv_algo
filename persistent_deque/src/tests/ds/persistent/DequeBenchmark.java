package tests.ds.persistent;

import ds.persistent.Deque;

import java.util.Random;


public class DequeBenchmark<DequeType extends Deque<Integer>> {

    public DequeBenchmark(Class<DequeType> clazz) {
        totalTime = 0;
        System.out.println("Benchmark for " + clazz);
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

    interface Meter {
        public String name();
        public void prepare();
        public void action();
    }

    public void BenchFillEmptyWorstCase(final int numOperations) {
        measure(new Meter() {
            public String name() {
                return "FillEmptyWorstCase";
            }
            public void prepare() {
			    deques = new Object[numOperations];
            }
            public void action() {
			    Random rand = new Random();
                for (int i = 0; i < numOperations; ++i) {
                    deque = (DequeType)deque.pushFront(i);
					deques[i] = deque;
                }
                for (int i = 0; i < numOperations; ++i) {
                    deques[i] = (DequeType)((DequeType)deques[i]).pushFront(rand.nextInt());
					deques[i] = (DequeType)((DequeType)deques[i]).popBack().second;
                }
            }
			private Object[] deques;
        });
    }

    public void BenchAllRandom(final int numOperations) {
        measure(new Meter() {
            public String name() {
                return "AllRandom";
            }
            public void prepare() {
                Random rand = new Random();
                opers = new Boolean[numOperations];
                for (int i = 0; i < numOperations; ++i) {
                    opers[i] = rand.nextBoolean();
                }
            }
            public void action() {
                for (int i = 0; i < numOperations; ++i) {
                    deque = opers[i] ? (DequeType)deque.pushFront(i) : (DequeType)deque.pushBack(i);
                }
                for (int i = 0; i < numOperations; ++i) {
                    deque = opers[i] ? (DequeType)deque.popBack().second : (DequeType)deque.popFront().second;
                }
            }
            private Boolean[] opers;
        });
    }

    private void measure(Meter meter) {
        meter.prepare();
        System.out.println(meter.name());
        for (int i = 0; i < NUM_REPEATS; ++i) {
            start();
            meter.action();
            stop();
        }
        System.out.println("Total Time: " + (totalTime / 1000000) + " ms (" + totalTime + " ns)");
    }

    private void start() {
        startTime = System.nanoTime();
    }

    private void stop() {
        endTime = System.nanoTime();
        long duration = (endTime - startTime);
        totalTime += duration;
        System.out.println("Time: " + (duration / 1000000) + " ms (" + duration + " ns)");
    }

    private long startTime;
    private long endTime;
    private long totalTime;

    private Class<DequeType> clazz;
    private DequeType        deque;

    private int NUM_REPEATS = 1;
}
