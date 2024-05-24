package Controller;

import java.util.ArrayList;
import java.util.Scanner;

import Stations.Add;
import Stations.LB;
import Stations.Mul;
import Stations.RegisterFile;
import Stations.SB;

public class Tomasulo {
    static RegisterFile registerFile = new RegisterFile();
    static Add addStation = new Add();
    static Mul mulStation = new Mul();
    static LB loadBuffer = new LB();
    static SB storeBuffer = new SB();

    // Initializing all components.

    static boolean branchHandled = false; // To handle the branch instruction.
    static int branchTarget = 0;

    public static class station {
        public String station;
        public int I1;
        public int I2;

        public station(String station, int I1, int I2) {
            this.station = station;
            this.I1 = I1;
            this.I2 = I2;
        }
    }

    public static class Instruction {
        public String Instruction;
        public String i;
        public String j;
        public String k;
        public String Issue;
        public String Execute;
        public String WriteBack;
        public String branchTarget;
        public boolean isBNEZ;

        public Instruction(String inst, String A, String B, String C) {
            this.Instruction = inst;
            this.i = A;
            this.j = B;
            this.k = C;
        }

        public Instruction(String inst, String A, String B, String C, String target) {
            this(inst, A, B, C);
            this.branchTarget = target;
            this.isBNEZ = true;
        }

        @Override
        public String toString() {
            return "[Instruction= " + Instruction + "|| i= " + i + "|| j= " + j + "|| k= " + k
                    + "|| Issue= " + Issue + "|| Execute= " + Execute + "|| WriteBack= " + WriteBack + ']';
        }
    }

    static int cyclesAdd;
    static int cyclesSub;
    static int cyclesMul;
    static int cyclesDiv;
    static int cyclesLD;
    static int cyclesSD;

    public static void Latency() {

        // Latency() is used to take the latency of each instruction from the
        // user.

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please Enter Add latency: ");
        cyclesAdd = scanner.nextInt();
        System.out.println("Please Enter Sub Latency: ");
        cyclesSub = scanner.nextInt();
        System.out.println("Please Enter Mul Latency: ");
        cyclesMul = scanner.nextInt();
        System.out.println("Please Enter Div Latency: ");
        cyclesDiv = scanner.nextInt();
        System.out.println("Please Enter LD Latency: ");
        cyclesLD = scanner.nextInt();
        System.out.println("Please Enter SD Latency: ");
        cyclesSD = scanner.nextInt();
    }

    static station Issue = null;
    static int clock = 1;

    static ArrayList<Instruction> instructions = new ArrayList<>();
    static ArrayList<station> StartExecute = new ArrayList<>();
    static ArrayList<station> FinishExecute = new ArrayList<>();
    static ArrayList<station> WriteBack = new ArrayList<>();

    static boolean addFull = false;
    static boolean mulFull = false;
    static boolean memoryFull = false;
    static boolean writeFull = false;

    static int InstructionComplete = 0;

    public static void issue() {
        boolean issued = false;

        for (int i = 0; i < instructions.size(); i++) {
            if (!branchHandled && instructions.get(i).Instruction.equals("BNEZ")) {
                instructions.get(i).Issue = Integer.toString(clock);
                Issue = new station("BNEZ", i, -1);
                branchHandled = true;
                issued = true;
            } else if (instructions.get(i).Issue == null && !branchHandled) {
                if (instructions.get(i).Instruction.equals("ADD.D")
                        || instructions.get(i).Instruction.equals("SUB.D")) {
                    for (int j = 0; j < addStation.AddRS.size(); j++) {
                        Add.AddStation NewInstruction = addStation.AddRS.get(j);
                        if (NewInstruction.busy == 0) {
                            instructions.get(i).Issue = Integer.toString(clock);
                            NewInstruction.i = i;
                            NewInstruction.busy = 1;
                            NewInstruction.operation = instructions.get(i).Instruction;
                            if (registerFile.regs.get(Integer.parseInt(instructions.get(i).j.substring(1))).Qi
                                    .equals("0"))
                                NewInstruction.Vj = "R[" + instructions.get(i).j + "]";
                            else
                                NewInstruction.Qj = registerFile.regs
                                        .get(Integer.parseInt(instructions.get(i).j.substring(1))).Qi;
                            if (registerFile.regs.get(Integer.parseInt(instructions.get(i).k.substring(1))).Qi
                                    .equals("0"))
                                NewInstruction.Vk = "R[" + instructions.get(i).k + "]";
                            else
                                NewInstruction.Qk = registerFile.regs
                                        .get(Integer.parseInt(instructions.get(i).k.substring(1))).Qi;
                            registerFile.regs
                                    .get(Integer.parseInt(instructions.get(i).i.substring(1))).Qi = NewInstruction.name;
                            if (NewInstruction.Vj != null && NewInstruction.Vk != null) {
                                if (NewInstruction.operation.equals("ADD.D")) {
                                    NewInstruction.time = cyclesAdd;
                                } else {
                                    NewInstruction.time = cyclesSub;
                                }
                                Issue = new station("ADD", i, Integer.parseInt(NewInstruction.name.substring(1)) - 1);
                            }
                            issued = true;
                            break;
                        }
                    }
                }
                if (instructions.get(i).Instruction.equals("MUL.D")
                        || instructions.get(i).Instruction.equals("DIV.D")) {
                    for (int j = 0; j < mulStation.MulRS.size(); j++) {
                        Mul.MulStation NewInstruction = mulStation.MulRS.get(j);
                        if (NewInstruction.busy == 0) {
                            instructions.get(i).Issue = Integer.toString(clock);
                            NewInstruction.i = i;
                            NewInstruction.busy = 1;
                            NewInstruction.operation = instructions.get(i).Instruction;
                            if (registerFile.regs.get(Integer.parseInt(instructions.get(i).j.substring(1))).Qi
                                    .equals("0"))
                                NewInstruction.Vj = "R[" + instructions.get(i).j + "]";
                            else
                                NewInstruction.Qj = registerFile.regs
                                        .get(Integer.parseInt(instructions.get(i).j.substring(1))).Qi;
                            if (registerFile.regs.get(Integer.parseInt(instructions.get(i).k.substring(1))).Qi
                                    .equals("0"))
                                NewInstruction.Vk = "R[" + instructions.get(i).k + "]";
                            else
                                NewInstruction.Qk = registerFile.regs
                                        .get(Integer.parseInt(instructions.get(i).k.substring(1))).Qi;
                            registerFile.regs
                                    .get(Integer.parseInt(instructions.get(i).i.substring(1))).Qi = NewInstruction.name;
                            if (NewInstruction.Vj != null && NewInstruction.Vk != null) {
                                if (NewInstruction.operation.equals("MUL.D")) {
                                    NewInstruction.time = cyclesMul;
                                } else {
                                    NewInstruction.time = cyclesDiv;
                                }
                                Issue = new station("MUL", i, Integer.parseInt(NewInstruction.name.substring(1)) - 1);
                            }
                            issued = true;
                            break;
                        }
                    }
                }
                if (instructions.get(i).Instruction.equals("L.D")) {
                    for (int j = 0; j < loadBuffer.lb.size(); j++) {
                        LB.loadbuffer NewInstruction = loadBuffer.lb.get(j);
                        if (NewInstruction.busy == 0) {
                            instructions.get(i).Issue = Integer.toString(clock);
                            NewInstruction.i = i;
                            NewInstruction.busy = 1;
                            NewInstruction.time = cyclesLD;
                            NewInstruction.address = instructions.get(i).j + "+" + "R[" + instructions.get(i).k + "]";
                            Issue = new station("LD", i, Integer.parseInt(NewInstruction.name.substring(1)) - 1);
                            registerFile.regs
                                    .get(Integer.parseInt(instructions.get(i).i.substring(1))).Qi = NewInstruction.name;
                            issued = true;
                            break;
                        }
                    }
                }
                if (instructions.get(i).Instruction.equals("S.D")) {
                    for (int j = 0; j < storeBuffer.sb.size(); j++) {
                        SB.storebuffer NewInstruction = storeBuffer.sb.get(j);
                        if (NewInstruction.busy == 0) {
                            instructions.get(i).Issue = Integer.toString(clock);
                            NewInstruction.i = i;
                            NewInstruction.busy = 1;
                            NewInstruction.time = cyclesSD;
                            NewInstruction.address = instructions.get(i).j + "+" + "R[" + instructions.get(i).k + "]";
                            Issue = new station("SD", i, Integer.parseInt(NewInstruction.name.substring(1)) - 1);
                            registerFile.regs
                                    .get(Integer.parseInt(instructions.get(i).i.substring(1))).Qi = NewInstruction.name;
                            issued = true;
                            break;
                        }
                    }
                }
            }
            if (issued)
                break;
        }
    }

    public static void execute() {
        ArrayList<station> Done = new ArrayList<>();
        int CyclesBnez = 1;
        for (int i = 0; i < StartExecute.size(); i++) {
            station station = StartExecute.get(i);
            if (Issue != null) {
                if (Issue.station.equals("BNEZ")) {
                    if (CyclesBnez == 0) {

                        if (branchTarget >= 0 && branchTarget < instructions.size()) {
                            clock = branchTarget;
                        } else {
                            System.out.println("Invalid branch target!");
                            break;
                        }
                    } else {
                        CyclesBnez--;
                    }
                }
            }
            // }
            // }
            // branchTarget = Issue.I1;
            // branchHandled = false;
            // } else {
            // Issue = null;
            // branchHandled = false;
            // }
            // }
            // }
            if (station.station.equals("ADD")) {
                int cyclesOP;
                if (addStation.AddRS.get(station.I2).operation.equals("ADD.D")) {
                    cyclesOP = cyclesAdd;
                } else {
                    cyclesOP = cyclesSub;
                }
                boolean firstExecute = false;
                if (!addFull && addStation.AddRS.get(station.I2).time == cyclesOP) {
                    instructions.get(station.I1).Execute = clock + "-";
                    addFull = true;
                    firstExecute = true;
                }
                if (firstExecute || addStation.AddRS.get(station.I2).time != cyclesOP) {
                    addStation.AddRS.get(station.I2).time--;
                }
                if (addStation.AddRS.get(station.I2).time == 0) {
                    instructions.get(station.I1).Execute += clock;
                    Done.add(station);
                    FinishExecute.add(station);
                }
            }
            if (station.station.equals("MUL")) {
                int cyclesOP1;
                if (mulStation.MulRS.get(station.I2).operation.equals("MUL.D")) {
                    cyclesOP1 = cyclesMul;
                } else {
                    cyclesOP1 = cyclesDiv;
                }
                boolean firstExecute1 = false;
                if (!mulFull && mulStation.MulRS.get(station.I2).time == cyclesOP1) {
                    instructions.get(station.I1).Execute = clock + "-";
                    mulFull = true;
                    firstExecute1 = true;
                }
                if (firstExecute1 || mulStation.MulRS.get(station.I2).time != cyclesOP1)
                    mulStation.MulRS.get(station.I2).time--;
                if (mulStation.MulRS.get(station.I2).time == 0) {
                    instructions.get(station.I1).Execute += clock;
                    Done.add(station);
                    FinishExecute.add(station);
                }
            }

            if (station.station.equals("LD")) {
                boolean firstExecute2 = false;
                if (!memoryFull && loadBuffer.lb.get(station.I2).time == cyclesLD) {
                    instructions.get(station.I1).Execute = clock + "-";
                    memoryFull = true;
                    firstExecute2 = true;
                }
                if (firstExecute2 || loadBuffer.lb.get(station.I2).time != cyclesLD)
                    loadBuffer.lb.get(station.I2).time--;
                if (loadBuffer.lb.get(station.I2).time == 0) {
                    instructions.get(station.I1).Execute += clock;
                    Done.add(station);
                    FinishExecute.add(station);
                }
            }
            if (station.station.equals("SD")) {
                boolean firstExecute3 = false;
                if (!memoryFull && storeBuffer.sb.get(station.I2).time == cyclesSD) {
                    instructions.get(station.I1).Execute = clock + "-";
                    memoryFull = true;
                    firstExecute3 = true;
                }
                if (firstExecute3 || storeBuffer.sb.get(station.I2).time != cyclesSD)
                    storeBuffer.sb.get(station.I2).time--;
                if (storeBuffer.sb.get(station.I2).time == 0) {
                    instructions.get(station.I1).Execute += clock;
                    storeBuffer.sb.get(station.I2).Reset();
                    Done.add(station);
                    InstructionComplete++;
                }
            }
        }
        StartExecute.removeAll(Done);
    }

    public static void write() {
        ArrayList<station> Done = new ArrayList<>();
        for (int i = 0; i < WriteBack.size(); i++) {
            station station = WriteBack.get(i);
            String NewInstructionName = "";
            instructions.get(station.I1).WriteBack = Integer.toString(clock);
            if (Issue != null && Issue.station.equals("BNEZ")) {
                branchTarget = Issue.I1;
                branchHandled = false;
            }
            if (station.station.equals("ADD")) {
                NewInstructionName = addStation.AddRS.get(station.I2).name;
                addStation.AddRS.get(station.I2).Reset();
            } else if (station.station.equals("MUL")) {
                NewInstructionName = mulStation.MulRS.get(station.I2).name;
                mulStation.MulRS.get(station.I2).Reset();
            } else if (station.station.equals("LD")) {
                NewInstructionName = loadBuffer.lb.get(station.I2).name;
                loadBuffer.lb.get(station.I2).Reset();
            }
            for (int j = 0; j < registerFile.regs.size(); j++) {
                RegisterFile.Register register = registerFile.regs.get(j);
                if (register.Qi.equals(NewInstructionName))
                    register.Qi = "0";
            }
            for (int j = 0; j < addStation.AddRS.size(); j++) {
                Add.AddStation NewInstruction = addStation.AddRS.get(j);
                boolean edited = false;
                if (NewInstruction.Qj != null && NewInstruction.Qj.equals(NewInstructionName)) {
                    NewInstruction.Vj = "new value";
                    edited = true;
                }
                if (NewInstruction.Qk != null && NewInstruction.Qk.equals(NewInstructionName)) {
                    NewInstruction.Qk = null;
                    NewInstruction.Vk = "new value";
                    edited = true;
                }
                if (edited && NewInstruction.Vj != null && NewInstruction.Vk != null) {
                    if (NewInstruction.operation.equals("ADD.D"))
                        NewInstruction.time = cyclesAdd;
                    else
                        NewInstruction.time = cyclesSub;
                    StartExecute.add(new station("ADD", NewInstruction.i,
                            Integer.parseInt(NewInstruction.name.substring(1)) - 1));
                }

                // if (instructions.get(i).Instruction.equals("BNEZ")) {
                // if (!branchHandled) {
                // instructions.get(i).Issue = Integer.toString(clock);
                // Issue = new station("BNEZ", i, -1);
                // branchHandled = true;
                // issued = true;
                // }
                // }
                // }
                // }

                // if (!issued)

            }
            for (int j = 0; j < mulStation.MulRS.size(); j++) {
                Mul.MulStation NewInstruction = mulStation.MulRS.get(j);
                boolean edited = false;
                if (NewInstruction.Qj != null && NewInstruction.Qj.equals(NewInstructionName)) {
                    NewInstruction.Qj = null;
                    NewInstruction.Vj = "new value";
                    edited = true;
                }
                if (NewInstruction.Qk != null && NewInstruction.Qk.equals(NewInstructionName)) {
                    NewInstruction.Qk = null;
                    NewInstruction.Vk = "new value";
                    edited = true;
                }
                if (edited && NewInstruction.Vj != null && NewInstruction.Vk != null) {
                    if ("MUL.D".equals(NewInstruction.operation))
                        NewInstruction.time = cyclesMul;
                    else
                        NewInstruction.time = cyclesDiv;
                    StartExecute.add(new station("MUL", NewInstruction.i,
                            Integer.parseInt(NewInstruction.name.substring(1)) - 1));
                }
            }
            writeFull = true;
            Done.add(station);
            InstructionComplete++;
            break;
        }
        WriteBack.removeAll(Done);

    }

    public static void clear() {

        // Clears the status flags and finishes execution list.

        addFull = false;
        mulFull = false;
        memoryFull = false;
        writeFull = false;

        FinishExecute.clear();
    }

    public static void program() {
        issue();
        execute();
        write();
        Display();
    }

    public static void print() {

        // A method to print each instruction out of the arraylist.

        for (int i = 0; i < instructions.size(); i++) {
            Instruction instruction = instructions.get(i);
            System.out.println(instruction);
        }
    }

    public static void Display() {

        // The Display of the program in CLI

        System.out.println("-------------Clock " + clock + "-------------");
        System.out.println(" ");
        print();
        System.out.println(" ");
        addStation.print();
        System.out.println(" ");
        mulStation.print();
        System.out.println(" ");
        loadBuffer.print();
        System.out.println(" ");
        storeBuffer.print();
        System.out.println(" ");
        registerFile.print();
        System.out.println(" ");
    }

    public static void Tomas() {

        // main method to executes the Tomasulo algorithm until all instructions are
        // complete.

        while (InstructionComplete < instructions.size()) {
            if (Issue != null) {
                StartExecute.add(Issue);
                Issue = null;
            }
            WriteBack.addAll(FinishExecute);
            clear();
            program();
            clock++;
        }
    }

    public static void main(String[] args) {

        Latency();

        instructions.add(new Instruction("L.D", "F6", "32", "R2"));
        instructions.add(new Instruction("MUL.D", "F0", "F2", "F4"));
        instructions.add(new Instruction("SUB.D", "F8", "F6", "F2"));
        instructions.add(new Instruction("L.D", "F2", "44", "R3"));
        instructions.add(new Instruction("DIV.D", "F10", "F0", "F6"));
        instructions.add(new Instruction("ADD.D", "F6", "F8", "F2"));

        // instructions.add(new Instruction("L.D", "F6", "32", "R2"));
        // instructions.add(new Instruction("L.D", "F2", "44", "R3"));
        // instructions.add(new Instruction("MUL.D", "F0", "F2", "F4"));
        // instructions.add(new Instruction("SUB.D", "F8", "F6", "F2"));
        // instructions.add(new Instruction("DIV.D", "F10", "F0", "F6"));
        // instructions.add(new Instruction("BNEZ", "F6", "0", "0")); // Branch taken
        // instructions.add(new Instruction("ADD.D", "F6", "F8", "F2"));

        // instructions.add(new Instruction("L.D", "F6", "32", "R2"));
        // instructions.add(new Instruction("L.D", "F2", "44", "R3"));
        // instructions.add(new Instruction("BNEZ", "R3", "", "2")); // Branch not taken
        // instructions.add(new Instruction("MUL.D", "F0", "F2", "F4"));
        // instructions.add(new Instruction("SUB.D", "F8", "F6", "F2"));
        // instructions.add(new Instruction("DIV.D", "F10", "F0", "F6"));
        // instructions.add(new Instruction("ADD.D", "F6", "F8", "F2"));

        Tomas();
    }
}
