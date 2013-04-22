package tests.ds.persistent;

import ds.persistent.Deque;
import ds.persistent.DequeKOT;
import ds.utility.Pair;

import java.util.*;

public class DequeTest<DequeType extends Deque<Integer>> {

    public static int NUM_OPERATIONS = 100000;

    enum Decision {
        PushFront, PopFront, PushBack, PopBack
    }
    interface DecisionMaker {
        Decision make(Decision previous);
    }

    class RandomDM implements DecisionMaker {
        public RandomDM() {
            rand = new Random();
        }
        public Decision make(Decision previous) {
            int r = rand.nextInt(4);
            return Decision.values()[r];
        }

        private Random rand;
    }

    class PushPopOneSideDM implements DecisionMaker {
        public Decision make(Decision previous) {
            int ord = previous.ordinal();
            ord = ord - (ord % 2);
            if (ord == previous.ordinal())
                ++ord;
            return Decision.values()[ord];
        }
    }

    class PushPopOppositeSideDM implements DecisionMaker {
        public Decision make(Decision previous) {
            int ord = previous.ordinal();
            ord = 3 - ord;
            return Decision.values()[ord];
        }
    }

    class PushDM implements DecisionMaker {
        public PushDM() {
            rand = new Random();
        }
        public Decision make(Decision previous) {
            int r = rand.nextInt(2);
            if (r == 0)
                return Decision.PushFront;
            else
                return Decision.PushBack;
        }
        private Random rand;
    }

    class PopDM implements DecisionMaker {
        public PopDM() {
            rand = new Random();
        }
        public Decision make(Decision previous) {
            int r = rand.nextInt(2);
            if (r == 0)
                return Decision.PopFront;
            else
                return Decision.PopBack;
        }
        private Random rand;
    }

    public DequeTest(Class<DequeType> clazz) {
        this.clazz = clazz;
        clean();
    }

    public Boolean TestPushPopFront() {
        System.out.println("Test push-pop Front");
        DecisionMaker dm = new PushPopOneSideDM();
        Decision decision = Decision.PushFront;
        Random rand = new Random();
        Boolean res = false;
        try {
            for (int oper = 0; oper < NUM_OPERATIONS; ++oper) {
                action(decision, rand.nextInt());
                decision = dm.make(decision);
            }

            res = validate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        clean();
        return res;
    }

    public Boolean TestPushPopBack() {
        System.out.println("Test push-pop Back");
        DecisionMaker dm = new PushPopOneSideDM();
        Decision decision = Decision.PushBack;
        Random rand = new Random();
        Boolean res = false;
        try {
            for (int oper = 0; oper < NUM_OPERATIONS; ++oper) {
                action(decision, rand.nextInt());
                decision = dm.make(decision);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        res = validate();
        clean();
        return res;
    }

    public Boolean TestPushFrontPopBack() {
        System.out.println("Test push Front - pop Back");
        DecisionMaker dm = new PushPopOppositeSideDM();
        Decision decision = Decision.PushFront;
        Random rand = new Random();
        Boolean res = false;
        try {
            for (int oper = 0; oper < NUM_OPERATIONS; ++oper) {
                action(decision, rand.nextInt());
                decision = dm.make(decision);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        res = validate();
        clean();
        return res;
    }

    public Boolean TestPushBackPopFront() {
        System.out.println("Test push Back - pop Front");
        DecisionMaker dm = new PushPopOppositeSideDM();
        Decision decision = Decision.PushBack;
        Random rand = new Random();
        Boolean res = false;
        try {
            for (int oper = 0; oper < NUM_OPERATIONS; ++oper) {
                action(decision, rand.nextInt());
                decision = dm.make(decision);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        res = validate();
        clean();
        return res;
    }

    public Boolean TestRandomPushPop() {
        System.out.println("Test Random push-pop");
        DecisionMaker dm = new RandomDM();
        Decision decision = Decision.PushFront;
        Random rand = new Random();
        Boolean res = false;
        try {
            for (int oper = 0; oper < NUM_OPERATIONS; ++oper) {
                action(decision, rand.nextInt());
                decision = dm.make(decision);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        res = validate();
        clean();
        return res;
    }

    public Boolean TestFillEmpty() {
        System.out.println("Test Filling and Emptying");
        DecisionMaker dm1 = new PushDM();
        DecisionMaker dm2 = new PopDM();
        Decision decision = Decision.PushFront;
        Random rand = new Random();
        Boolean res = false;
        int div = 1000;
        try {
            for (int j = 0; j < div; ++j) {
                for (int oper = 0; oper < NUM_OPERATIONS / div; ++oper) {
                    action(decision, rand.nextInt());
                    decision = dm1.make(decision);
                }
                for (int oper = 0; oper < NUM_OPERATIONS / div; ++oper) {
                    action(decision, rand.nextInt());
                    decision = dm2.make(decision);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        res = validate();
        clean();
        return res;
    }

    public Boolean validate() {
        int count = 0;
        System.out.println("Validating versions");
        while (!etalon.isEmpty() && !tested.isEmpty()) {
//            System.out.print(count + ", ");
            if (!validate(etalon.pollFirst(), tested.pollFirst())) {
                System.out.println("Test FAULT\n");
                return false;
            }
            ++count;
        }
        System.out.println("Test OK\n");
        return true;
    }

    private void action(Decision decision, Integer value) throws Exception {
        if (decision == Decision.PushFront)
            pushFront(value);
        if (decision == Decision.PopFront)
            popFront();
        if (decision == Decision.PushBack)
            pushBack(value);
        if (decision == Decision.PopBack)
            popBack();
    }

    private void pushFront(Integer value) {
        tested.addLast(tested.peekLast().pushFront(value));

        ArrayDeque<Integer> et = etalon.peekLast().clone();
        et.addFirst(value);
        etalon.addLast(et);
    }

    private void pushBack(Integer value) {
        tested.addLast(tested.peekLast().pushBack(value));

        ArrayDeque<Integer> et = etalon.peekLast().clone();
        et.addLast(value);
        etalon.addLast(et);
    }

    private void popFront() throws Exception {
        Boolean caught_exception = false;
        try {
            Pair<Integer, Deque<Integer>> pair = tested.peekLast().popFront();
            tested.addLast(pair.second);
        }
        catch (NoSuchElementException ex) {
            caught_exception = true;
        }

        ArrayDeque<Integer> et = etalon.peekLast().clone();
        if (et.pollFirst() == null)
            caught_exception = ! caught_exception;
        else
            etalon.addLast(et);

        if (caught_exception)
            throw new Exception("One deque threw exception, another didn't");
    }

    private void popBack() throws Exception {
        Boolean caught_exception = false;
        try {
            Pair<Integer, Deque<Integer>> pair = tested.peekLast().popBack();
            tested.addLast(pair.second);
        }
        catch (NoSuchElementException ex) {
            caught_exception = true;
        }

        ArrayDeque<Integer> et = etalon.peekLast().clone();
        if (et.pollLast() == null)
            caught_exception = ! caught_exception;
        else
            etalon.addLast(et);

        if (caught_exception)
            throw new Exception("One deque threw exception, another didn't");
    }

    private Boolean validate(ArrayDeque<Integer> etal, Deque<Integer> test) {
        while (!etal.isEmpty()) {
            try {
                Integer et_value = etal.pollFirst();
                Pair<Integer, Deque<Integer>> pair = test.popFront();
                if (et_value != pair.first) {
                    System.out.println("Elements aren't equal: " + et_value + ", " + pair.first);
                    return false;
                }
                test = pair.second;
            }
            catch (NoSuchElementException ex) {
                System.out.println("Tested queue has less elements");
                return false;
            }
        }
        if (!test.empty()) {
            System.out.println("Tested queue still has elements");
            return false;
        }
        return true;
    }

    private void clean() {
        etalon = new LinkedList<ArrayDeque<Integer>>();
        tested = new LinkedList<Deque<Integer>>();

        etalon.addLast(new ArrayDeque<Integer>());
        try {
            tested.addLast(clazz.newInstance());
        }
        catch (InstantiationException ex) {
            System.out.println("Can't create instance of " + clazz + ": " + ex.getMessage());
        }
        catch (IllegalAccessException ex) {
            System.out.println("Can't create instance of " + clazz + ": " + ex.getMessage());
        }
    }

    private LinkedList<ArrayDeque<Integer>> etalon;
    private LinkedList<Deque<Integer>>      tested;
    private Class<DequeType> clazz;
}
