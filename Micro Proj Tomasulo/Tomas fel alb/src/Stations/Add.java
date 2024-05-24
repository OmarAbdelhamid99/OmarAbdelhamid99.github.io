package Stations;

import java.util.ArrayList;
import java.util.Scanner;

public class Add {
    public int size;

    public static class AddStation {
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

        public AddStation(int x) {
            name = "A" + x;
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
            return "AddStation [" +
                    "Name = " + name + "|| busy = " + busy + "|| operation = "
                    + operation + "|| Vj = " + Vj + "|| Vk = " + Vk + "|| Qj = " + Qj + "|| Qk = " + Qk + "||address = "
                    + address + "|| ExecutionTime = " + time + ']';
        }
    }

    public ArrayList<AddStation> AddRS = new ArrayList<>();

    public Add() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please Enter AddStation size: ");
        size = scanner.nextInt();

        for (int i = 1; i <= size; i++) {
            AddRS.add(new AddStation(i));
        }
    }

    public void print() {
        for (int i = 0; i < AddRS.size(); i++) {
            System.out.println(AddRS.get(i));
        }
    }

    public static void main(String[] args) {
        Add addStation = new Add();
        addStation.print();
    }
}
