import ds.persistent.Deque;
import ds.persistent.PersistentDeque;
import ds.utility.Pair;
import ds.persistent.DequeKOT;
import tests.ds.persistent.DequeBenchmark;
import tests.ds.persistent.DequeTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Vector;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Not enough arguments (use '-t' or '-i' or '-b'");
            System.exit(1);
        }
        if (args[0].equals("-t"))
            RunTests();
        if (args[0].equals("-i"))
            Interact();
        if (args[0].equals("-b")) {
            if (args.length < 4) {
                System.out.println("Provide tested deque ('k' for DequeKOT or 'p' " +
                        "or PersistentDeque), kind of test ('r' for AllRandom or 'f' for FillEmpty)" +
                        " and number of operations");
                System.exit(1);
            }

            try {
                int numOperations = Integer.parseInt(args[3]);
                Benchmark("k".equals(args[1]), "r".equals(args[2]), numOperations);
            }
            catch (NumberFormatException ex) {
                System.out.println("Can't parse number of operations!!");
                System.exit(1);
            }
        }
    }

    private static void Interact() {
        Vector<Deque<String>> deques = new Vector<Deque<String>>();
        deques.add(new DequeKOT<String>());
        int curVersion = 0;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println("\nAvailable commands (spaces inside value prohibited):");
            System.out.println("+f <value> - push string value at the beginning");
            System.out.println("-f <value> - pop string value at the beginning");
            System.out.println("+b - push string value at the end");
            System.out.println("-b - pop string value at the end");
            System.out.println("p <N> - print contents of version number N");
            System.out.println("Current version: " + curVersion);
            System.out.println("Ctrl + C - exit");
            try {
                String line = br.readLine();
                String[] tokens = line.split("\\s+");
                if (tokens.length > 2) {
                    System.out.println("Provided input is incorrect");
                    continue;
                }
                if (tokens[0].equals("+f")) {
                    if (tokens.length < 2) {
                        System.out.println("Not enough arguments");
                        continue;
                    }
                    Deque<String> deque = deques.lastElement();
                    deques.add(deque.pushFront(tokens[1]));
                    ++curVersion;
                }
                else if (tokens[0].equals("-f")) {
                    try {
                        Deque<String> deque = deques.lastElement();
                        Pair<String, Deque<String>> pair = deque.popFront();
                        System.out.println("Poped " + pair.first);
                        deques.add(pair.second);
                        ++curVersion;
                    }
                    catch (NoSuchElementException ex) {
                        System.out.println("Deque is empty already");
                    }
                }
                else if (tokens[0].equals("+b")) {
                    if (tokens.length < 2) {
                        System.out.println("Not enough arguments");
                        continue;
                    }
                    Deque<String> deque = deques.lastElement();
                    deques.add(deque.pushBack(tokens[1]));
                    ++curVersion;
                }
                else if (tokens[0].equals("-b")) {
                    try {
                        Deque<String> deque = deques.lastElement();
                        Pair<String, Deque<String>> pair = deque.popBack();
                        System.out.println("Poped " + pair.first);
                        deques.add(pair.second);
                        ++curVersion;
                    }
                    catch (NoSuchElementException ex) {
                        System.out.println("Deque is empty already");
                    }
                }
                else if (tokens[0].equals("p")) {
                    if (tokens.length < 2) {
                        System.out.println("Not enough arguments");
                        continue;
                    }
                    try {
                        int version = Integer.parseInt(tokens[1]);
                        if (version < 0 || version >= deques.size()) {
                            System.out.println("Version number must be between 0 and " + (deques.size() - 1));
                            continue;
                        }
                        Deque<String> d_cont = deques.elementAt(version);
                        System.out.println("Contents of deque version " + version + ":");
                        while (!d_cont.empty()) {
                            Pair<String, Deque<String>> pair = d_cont.popFront();
                            System.out.println(pair.first);
                            d_cont = pair.second;
                        }
                    }
                    catch (NumberFormatException ex) {
                        System.out.println("Can't parse version number");
                    }
                }
                else {
                    System.out.println("Unknown command: " + tokens[0]);
                    continue;
                }
            }
            catch (IOException ex) {
                System.out.println("Exception occured: " + ex.getMessage());
            }

        }
    }

    private static void RunTests() {
        System.out.println("Testing PersistentDeque");
        DequeTest dt = new DequeTest(PersistentDeque.class);
        dt.TestPushPopFront();
        dt.TestPushPopBack();
        dt.TestPushFrontPopBack();
        dt.TestPushBackPopFront();
        dt.TestFillEmpty();
        dt.TestRandomPushPop();

        System.out.println("\nTesting DequeKOT");
        dt = new DequeTest(DequeKOT.class);
        dt.TestPushPopFront();
        dt.TestPushPopBack();
        dt.TestPushFrontPopBack();
        dt.TestPushBackPopFront();
        dt.TestFillEmpty();
        dt.TestRandomPushPop();
    }

    private static void Benchmark(Boolean kot, Boolean random, int numOperations) {
        if (kot) {
            DequeBenchmark<DequeKOT<Integer>> db = new DequeBenchmark(DequeKOT.class);
            if (random) {
                db.BenchAllRandom(numOperations);
            }
            else {
                db.BenchFillEmptyWorstCase(numOperations);
            }
        }
        else {
            DequeBenchmark<PersistentDeque<Integer>> db = new DequeBenchmark(PersistentDeque.class);
            if (random) {
                db.BenchAllRandom(numOperations);
            }
            else {
                db.BenchFillEmptyWorstCase(numOperations);
            }
        }
    }
}

