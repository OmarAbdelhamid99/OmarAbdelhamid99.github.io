package Stations;

import java.util.ArrayList;

public class RegisterFile {

    public static class Register {
        public String name;
        public String Qi;

        public Register(int x) {
            name = "F" + x;
            Qi = "0";
        }

        @Override
        public String toString() {
            return "Register[" + "name = " + name + " ||" + " Qi = " + Qi + ']';
        }
    }

    public ArrayList<Register> regs = new ArrayList<>();

    public RegisterFile() {
        for (int i = 0; i < 32; i++) {
            regs.add(new Register(i));
        }
    }

    public void print() {
        for (int i = 0; i < regs.size(); i++) {
            System.out.println(regs.get(i));
        }
    }

    public static void main(String[] args) {
        RegisterFile registerFile = new RegisterFile();
        registerFile.print();
    }
}
