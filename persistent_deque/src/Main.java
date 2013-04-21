import ds.utility.Pair;
import ds.persistent.DequeKOT;
import tests.ds.persistent.DequeTest;

public class Main {
    public static void main(String[] args) {
        try
        {
            DequeTest dt = new DequeTest();
            dt.TestPushPopFront();
            dt = new DequeTest();
            dt.TestPushPopBack();
            dt = new DequeTest();
            dt.TestPushFrontPopBack();
            dt = new DequeTest();
            dt.TestPushBackPopFront();
            dt = new DequeTest();
            dt.TestFillEmpty();
            dt = new DequeTest();
            dt.TestRandomPushPop();
        }
        catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
}
