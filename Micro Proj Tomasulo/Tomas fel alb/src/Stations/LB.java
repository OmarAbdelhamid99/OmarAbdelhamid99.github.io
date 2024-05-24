package Stations;

import java.util.ArrayList;
import java.util.Scanner;

public class LB {
    int size;

    public static class loadbuffer {
        public int i;
        public int time;
        public int busy;
        public String name;
        public String address;

        public loadbuffer(int x) {
            name = "L" + x;
            busy = 0;
        }

        public void Reset() {
            busy = 0;
            address = null;
        }

        @Override
        public String toString() {
            return "LoadBuffer [" + "name = " + name + "|| busy = " + busy + "|| address = "
                    + address + "|| ExecutionTime = " + time + ']';
        }
    }

    public ArrayList<loadbuffer> lb = new ArrayList<>();

    public LB() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please Enter LoadBuffer size: ");
        size = scanner.nextInt();

        for (int i = 1; i <= size; i++) {
            lb.add(new loadbuffer(i));
        }
    }

    public void print() {
        for (int i = 0; i < lb.size(); i++) {
            System.out.println(lb.get(i));
        }
    }

    public static void main(String[] args) {
        LB loadBuffer = new LB();
        loadBuffer.print();
    }
}
