
public class Main {
    public static void main(String[] args) {
        try
        {
            PersistentDeque<Integer> deque = new PersistentDeque<Integer>();
            final int count = 12;
            for (int i = 0; i < count; ++i) {
                deque = deque.push(i);
            }

            for (int i = 0; i < count; ++i) {
                Pair<Integer, PersistentDeque<Integer>> p = deque.pop();
                System.out.println(p.first);
                deque = p.second;
            }
        }
        catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
}
