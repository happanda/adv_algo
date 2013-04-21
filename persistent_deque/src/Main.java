import ds.utility.Pair;
import ds.persistent.DequeKOT;
import tests.ds.persistent.DequeTest;

public class Main {
    public static void main(String[] args) {
        RunTests();
    }

    private static void RunTests() {
        DequeTest dt = new DequeTest();
        dt.TestPushPopFront();
        dt.TestPushPopBack();
        dt.TestPushFrontPopBack();
        dt.TestPushBackPopFront();
        dt.TestFillEmpty();
        dt.TestRandomPushPop();
    }
}

