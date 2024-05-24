package Stations;

import java.util.ArrayList;
import java.util.Scanner;

public class SB {
    int size;

    public static class storebuffer {
        public int i;
        public int time;
        public int busy;
        public String name;
        public String address;

        public storebuffer(int number) {
            name = "S" + number;
            busy = 0;
        }

        public void Reset() {
            busy = 0;
            address = null;
        }

        @Override
        public String toString() {
            return "StoreBuffer [" + "name = " + name + "|| busy = " + busy +
                    "|| address = " + address + "|| ExecutionTime = " + time + ']';
        }
    }

    public ArrayList<storebuffer> sb = new ArrayList<>();

    public SB() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please Enter StoreBuffer size: ");
        size = scanner.nextInt();

        for (int i = 1; i <= size; i++) {
            sb.add(new storebuffer(i));
        }
    }

    public void print() {
        for (int i = 0; i < sb.size(); i++) {
            System.out.println(sb.get(i));
        }
    }

    public static void main(String[] args) {
        SB storeBuffer = new SB();
        storeBuffer.print();
    }
}
