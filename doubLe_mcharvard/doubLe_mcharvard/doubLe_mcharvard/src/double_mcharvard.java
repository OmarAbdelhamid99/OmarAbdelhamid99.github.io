import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class double_mcharvard {

    int pc = 0;
    int[] instructionMemory = new int[1024];
    int[] dataMemory = new int[2048];
    int[] gprs = new int[64];
    int[] statusRegister = new int[8];
    boolean branchOccurred = false;

    public static void main(String[] args) {

        double_mcharvard ca = new double_mcharvard();
        ca.init(14); //specified number of instructions so it doesn't run forever

    }

    public void init(int numberOfInstructions){
        textFileToArray("doubLe_mcharvard/instructions.txt");
        Pipeline(numberOfInstructions);
    }

    public static String parseAssemblyCode(String assemblyCode) {
        // Split the assembly code into different parts
        String[] parts = assemblyCode.split("\\s*,\\s*|\\s+");

        String opcode = parts[0]; // The first part is the opcode

        // Transform the opcode into binary
        String binaryOpcode = getBinaryOpcode(opcode);

        // Generate binary code based on the opcode
        StringBuilder binaryCode = new StringBuilder(binaryOpcode);

        for (int i = 1; i < parts.length; i++) {
            String operand = parts[i];

            // Transform the operand into binary
            String binaryOperand = getBinaryOperand(operand);

            // Append the binary operand to the binary code
            binaryCode.append(binaryOperand);
        }

        return binaryCode.toString();
    }

    public void textFileToArray(String filePath) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int i = 0;
        for (String s : lines) {
            instructionMemory[i] = Integer.parseInt(parseAssemblyCode(s), 2);
            i++;
        }

    }


    public static String getBinaryOpcode(String opcode) {
        opcode = opcode.toUpperCase();
        switch (opcode) {
            case "ADD":
                return "0000";
            case "SUB":
                return "0001";
            case "MUL":
                return "0010";
            case "LDI":
                return "0011";
            case "BEQZ":
                return "0100";
            case "AND":
                return "0101";
            case "OR":
                return "0110";
            case "JR":
                return "0111";
            case "SLC":
                return "1000";
            case "SRC":
                return "1001";
            case "LB":
                return "1010";
            case "SB":
                return "1011";
            // Add more opcode cases as needed
            default:
                throw new IllegalArgumentException("Unknown opcode: " + opcode);
        }
    }

    public static String getBinaryOperand(String operand) {
        // Transform the operand into binary representation
        // Implement the logic to convert the operand into binary based on your specific requirements

        // Dummy implementation: assuming operand is a register

        operand = operand.toUpperCase();

        String operandInDecimal = "";
        if (operand.charAt(0) == 'R')
            operandInDecimal = operand.substring(1);
        else
            operandInDecimal = operand;

        String operandInBinary = Integer.toBinaryString(Integer.parseInt(operandInDecimal));

        if (operandInBinary.length() > 6) {
            throw new IllegalArgumentException("Operand too big.");
        }

        while (operandInBinary.length() < 6) {
            operandInBinary = "0".concat(operandInBinary);
        }

        return operandInBinary;
    }

    public int fetch() {
        if (pc < 0 || pc >= instructionMemory.length) {
            throw new IllegalArgumentException("Invalid program counter");
        }

        return instructionMemory[pc];
    }

    public int[] decode(int instruction) {
        String instructionString = Integer.toBinaryString(instruction);
        while(instructionString.length()<16){
            instructionString = "0".concat(instructionString);
        }
        String opcode = instructionString.substring(0, 4);
        String operand1 = instructionString.substring(4, 10);
        String operand2 = instructionString.substring(10, 16);
        int opcode_ = Integer.parseInt(opcode, 2);
        int operand1_ = Integer.parseInt(operand1, 2);
        int operand2_ = Integer.parseInt(operand2, 2);
        return new int[]{opcode_,operand1_,operand2_};
    }

    public static int bitCount(int a){
        return Integer.toBinaryString(a).length();
    }

    public void execute(int opcode, int operand1, int operand2) {
        switch (opcode) {
            case 0b0000: //add
                // carry flag
                int sum = gprs[operand1] + gprs[operand2];
                if (bitCount(sum) > 8) {
                    statusRegister[4] = 1;
                    gprs[operand1] = Integer.parseInt(Integer.toBinaryString(sum).substring(1), 2);
                } else {
                    statusRegister[4] = 0;
                    gprs[operand1] = sum;
                }

                // Overflow check (v)
                if (((gprs[operand1] ^ gprs[operand2]) & 0x80) == 0 && ((gprs[operand1] ^ sum) & 0x80) != 0) {
                    statusRegister[3] = 1; // Set overflow flag (V) to 1
                } else {
                    statusRegister[3] = 0; // Set overflow flag (V) to 0
                }

                // Negative flag (N) check
                if (gprs[operand1] < 0) {
                    statusRegister[2] = 1; // Set negative flag (N) to 1
                } else {
                    statusRegister[2] = 0; // Set negative flag (N) to 0
                }
                // Sign flag (S = N XOR V)
                statusRegister[1] = statusRegister[2] ^ statusRegister[3];

                // Zero flag (Z) check
                if (gprs[operand1] == 0) {
                    statusRegister[0] = 1; // Set zero flag (Z) to 1
                } else {
                    statusRegister[0] = 0; // Set zero flag (Z) to 0
                }
                System.out.println("R"+operand1+"="+gprs[operand1]);
                System.out.println("C="+statusRegister[4]);
                System.out.println("V="+statusRegister[3]);
                System.out.println("N="+statusRegister[2]);
                System.out.println("S="+statusRegister[1]);
                System.out.println("Z="+statusRegister[0]);
                break;
            case 0b0001: //sub

                // Perform subtraction operation
                int difference = gprs[operand1] - gprs[operand2];
                gprs[operand1] = difference;


                if (((gprs[operand1] ^ gprs[operand2]) & 0x80) != 0 && ((gprs[operand1] ^ difference) & 0x80) != 0) {
                    statusRegister[3] = 1; // Set overflow flag (V) to 1
                } else {
                    statusRegister[3] = 0; // Set overflow flag (V) to 0
                }

                // Negative flag (N) check
                if (gprs[operand1] < 0) {
                    statusRegister[2] = 1; // Set negative flag (N) to 1
                } else {
                    statusRegister[2] = 0; // Set negative flag (N) to 0
                }
                // Sign flag (S = N XOR V)
                statusRegister[1] = statusRegister[2] ^ statusRegister[3];

                // Zero flag (Z) check
                if (gprs[operand1] == 0) {
                    statusRegister[0] = 1; // Set zero flag (Z) to 1
                } else {
                    statusRegister[0] = 0; // Set zero flag (Z) to 0
                }
                System.out.println("R"+operand1+"="+gprs[operand1]);
                System.out.println("V="+statusRegister[3]);
                System.out.println("N="+statusRegister[2]);
                System.out.println("S="+statusRegister[1]);
                System.out.println("Z="+statusRegister[0]);
                break;

            case 0b0010:// mul
                gprs[operand1] = gprs[operand1] * gprs[operand2];
                if(gprs[operand1]>255){
                    throw new IllegalArgumentException("Multiplication result too big");
                }
                // Negative flag (N) check
                if (gprs[operand1] < 0) {
                    statusRegister[2] = 1; // Set negative flag (N) to 1
                } else {
                    statusRegister[2] = 0; // Set negative flag (N) to 0
                }

                // Zero flag (Z) check
                if (gprs[operand1] == 0) {
                    statusRegister[0] = 1; // Set zero flag (Z) to 1
                } else {
                    statusRegister[0] = 0; // Set zero flag (Z) to 0
                }
                System.out.println("R"+operand1+"="+gprs[operand1]);
                System.out.println("N="+statusRegister[2]);
                System.out.println("Z="+statusRegister[0]);
                break;

            case 0b0011:// ldi
                gprs[operand1] = operand2;
                System.out.println("R"+operand1+"="+gprs[operand1]);
                break;
            case 0b0100:// beqz
                if (gprs[operand1] == 0) {
                    pc = pc + 1 + operand2;
                    branchOccurred = true;
                }
                System.out.println("PC="+pc);
                break;
            case 0b0101://and 
                gprs[operand1] = gprs[operand1] & gprs[operand2];
                // Negative flag (N) check
                if (gprs[operand1] < 0) {
                    statusRegister[2] = 1; // Set negative flag (N) to 1
                } else {
                    statusRegister[2] = 0; // Set negative flag (N) to 0
                }

                // Zero flag (Z) check
                if (gprs[operand1] == 0) {
                    statusRegister[0] = 1; // Set zero flag (Z) to 1
                } else {
                    statusRegister[0] = 0; // Set zero flag (Z) to 0
                }
                System.out.println("R"+operand1+"="+gprs[operand1]);
                System.out.println("N="+statusRegister[2]);
                System.out.println("Z="+statusRegister[0]);
                break;

            case 0b0110://or
                gprs[operand1] = gprs[operand1] | gprs[operand2];
                // Negative flag (N) check
                if (gprs[operand1] < 0) {
                    statusRegister[2] = 1; // Set negative flag (N) to 1
                } else {
                    statusRegister[2] = 0; // Set negative flag (N) to 0
                }
                // Zero flag (Z) check
                if (gprs[operand1] == 0) {
                    statusRegister[0] = 1; // Set zero flag (Z) to 1
                } else {
                    statusRegister[0] = 0; // Set zero flag (Z) to 0
                }
                System.out.println("R"+operand1+"="+gprs[operand1]);
                System.out.println("N="+statusRegister[2]);
                System.out.println("Z="+statusRegister[0]);
                break;
            case 0b0111:// jr
                pc = Integer.parseInt(toBinaryString(gprs[operand1],8).concat(toBinaryString(gprs[operand2],8)), 2);
                System.out.println("PC="+pc);
                break;
            case 0b1000:// slc
                gprs[operand1] = gprs[operand1] << operand2 | gprs[operand1] >>> 8 - operand2;
                // Negative flag (N) check
                if (gprs[operand1] < 0) {
                    statusRegister[2] = 1; // Set negative flag (N) to 1
                } else {
                    statusRegister[2] = 0; // Set negative flag (N) to 0
                }

                // Zero flag (Z) check
                if (gprs[operand1] == 0) {
                    statusRegister[0] = 1; // Set zero flag (Z) to 1
                } else {
                    statusRegister[0] = 0; // Set zero flag (Z) to 0
                }
                System.out.println("R"+operand1+"="+gprs[operand1]);
                System.out.println("N="+statusRegister[2]);
                System.out.println("Z="+statusRegister[0]);
                break;
            case 0b1001:// src
                gprs[operand1] = gprs[operand1] >>> operand2 | gprs[operand1] << 8 - operand2;
                // Negative flag (N) check
                if (gprs[operand1] < 0) {
                    statusRegister[2] = 1; // Set negative flag (N) to 1
                } else {
                    statusRegister[2] = 0; // Set negative flag (N) to 0
                }

                // Zero flag (Z) check
                if (gprs[operand1] == 0) {
                    statusRegister[0] = 1; // Set zero flag (Z) to 1
                } else {
                    statusRegister[0] = 0; // Set zero flag (Z) to 0
                }
                System.out.println("R"+operand1+"="+gprs[operand1]);
                System.out.println("N="+statusRegister[2]);
                System.out.println("Z="+statusRegister[0]);
                break;
            case 0b1010:// lb
                gprs[operand1] = dataMemory[operand2];
                System.out.println("R"+operand1+"="+gprs[operand1]);
                break;
            case 0b1011:// sb
                dataMemory[operand2] = gprs[operand1];
                System.out.println("MEM["+operand2+"]="+dataMemory[operand2]);
                break;
            // Add more opcode cases as needed
            default:
                throw new IllegalArgumentException("Unknown opcode: " + opcode);
        }
    }

    public void Pipeline(int totalInstructions) {

        int pipelineSize = 3; // Number of instructions running in parallel
        int clockCycles = 3 + ((totalInstructions - 1) * 1); // Number of clock cycles

        // Create an array to hold the instructions
        String[] instructions = new String[totalInstructions];
        for (int i = 0; i < totalInstructions; i++) {
            instructions[i] = "Instruction " + (i + 1);
        }

        // Execute the pipeline
        int cycle = 1;
        int fetchedInstruction = 0;
        int oldFetchedInstruction = 0;
        int[] decodedInstruction = new int[3];
        int[] oldDecodedInstruction = new int[3];
        for (int i = 0; i < clockCycles; i++) {
            for (int j = 0; j < pipelineSize; j++) {
                int instructionIndex = i - j;
                if (instructionIndex >= 0 && instructionIndex < totalInstructions) {
                    System.out.print("** Cycle " + cycle + " " + instructions[instructionIndex] + " ");
                    if(j==0) {
                        System.out.println("IF");
                        oldFetchedInstruction = fetchedInstruction;
                        fetchedInstruction = fetch();
                        System.out.println("Instruction " + toBinaryString(fetchedInstruction) + " was fetched");
                        pc++;
                    } else if (j==1) {
                        System.out.println("ID");
                        oldDecodedInstruction = decodedInstruction;
                        System.out.println("Instruction \"" + toBinaryString(oldFetchedInstruction) + "\" is getting decoded");
                        decodedInstruction = decode(oldFetchedInstruction);
                    } else {
                        System.out.println("EX");

                        System.out.println("Opcode: " + oldDecodedInstruction[0] + " Operand1: " + oldDecodedInstruction[1] + " Operand2: " + oldDecodedInstruction[2]);
                        System.out.println("Above instruction is getting executed");
                        execute(oldDecodedInstruction[0], oldDecodedInstruction[1], oldDecodedInstruction[2]);

                        //check for control hazard
                        if ((oldDecodedInstruction[0] == 4 && branchOccurred) || oldDecodedInstruction[0] == 7) {
                            fetchedInstruction = fetch();
                            pc++;

                            oldFetchedInstruction = fetchedInstruction;
                            fetchedInstruction = fetch();
                            pc++;

                            oldDecodedInstruction = decodedInstruction;
                            decodedInstruction = decode(oldFetchedInstruction);
                            branchOccurred = false;
                        }

                    }
                }
            }
            cycle++;
            System.out.println();
        }
        System.out.println("General purpose registers:");
        System.out.print("[");
        for(int i: gprs)
            System.out.print(i + ", ");
        System.out.println("]");
        System.out.println("Status registers:");
        System.out.println("C="+statusRegister[4]);
        System.out.println("V="+statusRegister[3]);
        System.out.println("N="+statusRegister[2]);
        System.out.println("S="+statusRegister[1]);
        System.out.println("Z="+statusRegister[0]);
        System.out.println("Data memory:");
        System.out.print("[");
        for(int i: dataMemory)
            System.out.print(i + ", ");
        System.out.println("]");
        System.out.println("Instruction memory:");
        System.out.print("[");
        for(int i: instructionMemory)
            System.out.print(i + ", ");
        System.out.println("]");

    }

    public static String toBinaryString(int i){
        String s = Integer.toBinaryString(i);
        while(s.length()<16){
            s = "0".concat(s);
        }
        return s;
    }

    public static String toBinaryString(int i, int bitSize){
        String s = Integer.toBinaryString(i);
        while(s.length()<bitSize){
            s = "0".concat(s);
        }
        return s;
    }

}

