package tests.ds.persistent;

import ds.persistent.DequeKOT;
import ds.utility.Pair;

import java.util.*;

public class DequeTest {

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

    public DequeTest() {
        etalon = new LinkedList<ArrayDeque<Integer>>();
        tested = new LinkedList<DequeKOT<Integer>>();

        etalon.addLast(new ArrayDeque<Integer>());
        tested.addLast(new DequeKOT<Integer>());
    }

    public Boolean TestPushPopFront() throws Exception {
        System.out.println("Test push-pop Front");
        DecisionMaker dm = new PushPopOneSideDM();
        Decision decision = Decision.PushFront;
        Random rand = new Random();
        for (int oper = 0; oper < NUM_OPERATIONS; ++oper) {
            action(decision, rand.nextInt());
            decision = dm.make(decision);
        }

        return validate();
    }

    public Boolean TestPushPopBack() throws Exception{
        System.out.println("Test push-pop Back");
        DecisionMaker dm = new PushPopOneSideDM();
        Decision decision = Decision.PushBack;
        Random rand = new Random();
        for (int oper = 0; oper < NUM_OPERATIONS; ++oper) {
            action(decision, rand.nextInt());
            decision = dm.make(decision);
        }

        return validate();
    }

    public Boolean TestPushFrontPopBack() throws Exception {
        System.out.println("Test push Front - pop Back");
        DecisionMaker dm = new PushPopOppositeSideDM();
        Decision decision = Decision.PushFront;
        Random rand = new Random();
        for (int oper = 0; oper < NUM_OPERATIONS; ++oper) {
            action(decision, rand.nextInt());
            decision = dm.make(decision);
        }

        return validate();
    }

    public Boolean TestPushBackPopFront() throws Exception {
        System.out.println("Test push Back - pop Front");
        DecisionMaker dm = new PushPopOppositeSideDM();
        Decision decision = Decision.PushBack;
        Random rand = new Random();
        for (int oper = 0; oper < NUM_OPERATIONS; ++oper) {
            action(decision, rand.nextInt());
            decision = dm.make(decision);
        }

        return validate();
    }

    public Boolean TestRandomPushPop() throws Exception {
        System.out.println("Test Random push-pop");
        DecisionMaker dm = new RandomDM();
        Decision decision = Decision.PushFront;
        Random rand = new Random();
        for (int oper = 0; oper < NUM_OPERATIONS; ++oper) {
            action(decision, rand.nextInt());
            decision = dm.make(decision);
        }

        return validate();
    }

    public Boolean TestFillEmpty() throws Exception {
        System.out.println("Test Filling and Emptying");
        DecisionMaker dm1 = new PushDM();
        DecisionMaker dm2 = new PopDM();
        Decision decision = Decision.PushFront;
        Random rand = new Random();
        int div = 1000;
        int pushed = 0;
        for (int j = 0; j < div; ++j) {
            for (int oper = 0; oper < NUM_OPERATIONS / div; ++oper) {
                action(decision, pushed);
                decision = dm1.make(decision);
            }
            for (int oper = 0; oper < NUM_OPERATIONS / div; ++oper) {
                action(decision, pushed++);
                decision = dm2.make(decision);
            }
        }

        return validate();
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
            Pair<Integer, DequeKOT<Integer>> pair = tested.peekLast().popFront();
            tested.addLast(pair.second);
        }
        catch (NoSuchElementException ex) {
            caught_exception = true;
        }

        ArrayDeque<Integer> et = etalon.peekLast().clone();
        if (et.pollFirst() == null)
            caught_exception = ! caught_exception;
        etalon.addLast(et);

        if (caught_exception)
            throw new Exception("One deque threw exception, another didn't");
    }

    private void popBack() throws Exception {
        Boolean caught_exception = false;
        try {
            Pair<Integer, DequeKOT<Integer>> pair = tested.peekLast().popBack();
            tested.addLast(pair.second);
        }
        catch (NoSuchElementException ex) {
            caught_exception = true;
        }

        ArrayDeque<Integer> et = etalon.peekLast().clone();
        if (et.pollLast() == null)
            caught_exception = ! caught_exception;
        etalon.addLast(et);

        if (caught_exception)
            throw new Exception("One deque threw exception, another didn't");
    }

    private Boolean validate(ArrayDeque<Integer> etal, DequeKOT<Integer> test) {
        while (!etal.isEmpty()) {
            try {
                Integer et_value = etal.pollFirst();
                Pair<Integer, DequeKOT<Integer>> pair = test.popFront();
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

    private LinkedList<ArrayDeque<Integer>> etalon;
    private LinkedList<DequeKOT<Integer>>   tested;
}
