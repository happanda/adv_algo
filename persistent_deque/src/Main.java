import ds.utility.Pair;
import ds.persistent.DequeKOT;

public class Main {
    public static void main(String[] args) {
        try
        {
            DequeKOT<Integer> deque = new DequeKOT<Integer>();
            final int count = 12;
            for (int i = 0; i < count; ++i) {
                deque = deque.pushFront(i);
            }

            for (int i = 0; i < count; ++i) {
                Pair<Integer, DequeKOT<Integer>> p = deque.popFront();
                System.out.println(p.first);
                deque = p.second;
            }

            for (int i = 0; i < count; ++i) {
                deque = deque.pushBack(i);
            }

            for (int i = 0; i < count; ++i) {
                Pair<Integer, DequeKOT<Integer>> p = deque.popFront();
                System.out.println(p.first);
                deque = p.second;
            }
        }
        catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
}
