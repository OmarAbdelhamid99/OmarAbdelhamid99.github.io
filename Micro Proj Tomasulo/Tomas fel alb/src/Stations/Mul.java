package Stations;

import java.util.ArrayList;
import java.util.Scanner;

public class Mul {
    int size;

    public static class MulStation {
        public int i;
        public int time;
        public int busy;
        public String name;
        public String operation;
        public String Vj;
        public String Vk;
        public String Qj;
        public String Qk;
        public String address;

        public MulStation(int x) {
            name = "M" + x;
            busy = 0;
        }

        public void Reset() {
            busy = 0;
            operation = null;
            Vj = null;
            Vk = null;
            Qj = null;
            Qk = null;
            address = null;
        }

        @Override
        public String toString() {
            return "MulStation [" +
                    "Name = " + name + "|| busy = " + busy + "|| operation = "
                    + operation + "|| Vj = " + Vj + "|| Vk = " + Vk + "|| Qj = " + Qj + "|| Qk = " + Qk + "||address = "
                    + address + "|| ExecutionTime = " + time + ']';
        }
    }

    public ArrayList<MulStation> MulRS = new ArrayList<>();

    public Mul() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please Enter MulStation size: ");
        size = scanner.nextInt();

        for (int i = 1; i <= size; i++) {
            MulRS.add(new MulStation(i));
        }
    }

    public void print() {
        for (int i = 0; i < MulRS.size(); i++) {
            System.out.println(MulRS.get(i));
        }
    }

    public static void main(String[] args) {
        Mul mulStation = new Mul();
        mulStation.print();
    }
}
