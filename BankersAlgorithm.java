import java.util.Scanner;

public class BankersAlgorithm {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input number of processes and resources
        System.out.print("Enter the number of processes: ");
        int numProcesses = sc.nextInt();

        System.out.print("Enter the number of resources: ");
        int numResources = sc.nextInt();

        // Input Allocation matrix
        int[][] allocation = new int[numProcesses][numResources];
        System.out.println("Enter the Allocation matrix:");
        for (int i = 0; i < numProcesses; i++) {
            for (int j = 0; j < numResources; j++) {
                allocation[i][j] = sc.nextInt();
            }
        }

        // Input Max matrix
        int[][] max = new int[numProcesses][numResources];
        System.out.println("Enter the Max matrix:");
        for (int i = 0; i < numProcesses; i++) {
            for (int j = 0; j < numResources; j++) {
                max[i][j] = sc.nextInt();
            }
        }

        // Input Available resources
        int[] available = new int[numResources];
        System.out.println("Enter the Available resources:");
        for (int i = 0; i < numResources; i++) {
            available[i] = sc.nextInt();
        }

        // Calculate Need matrix
        int[][] need = new int[numProcesses][numResources];
        for (int i = 0; i < numProcesses; i++) {
            for (int j = 0; j < numResources; j++) {
                need[i][j] = max[i][j] - allocation[i][j];
            }
        }

        // Display the Need matrix
        System.out.println("Need matrix:");
        for (int i = 0; i < numProcesses; i++) {
            for (int j = 0; j < numResources; j++) {
                System.out.print(need[i][j] + " ");
            }
            System.out.println();
        }

        // Safety algorithm
        boolean[] finished = new boolean[numProcesses];
        int[] safeSequence = new int[numProcesses];
        int count = 0;

        while (count < numProcesses) {
            boolean found = false;

            for (int i = 0; i < numProcesses; i++) {
                if (!finished[i]) {
                    boolean canExecute = true;
                    for (int j = 0; j < numResources; j++) {
                        if (need[i][j] > available[j]) {
                            canExecute = false;
                            break;
                        }
                    }

                    if (canExecute) {
                        // If process i can execute, add its allocated resources to available
                        for (int j = 0; j < numResources; j++) {
                            available[j] += allocation[i][j];
                        }
                        safeSequence[count++] = i;
                        finished[i] = true;
                        found = true;
                        System.out.println("Process " + i + " is executed.");
                    }
                }
            }

            if (!found) {
                System.out.println("The system is in an unsafe state!");
                return;
            }
        }

        // Display safe sequence
        System.out.println("The system is in a safe state.");
        System.out.print("Safe sequence: ");
        for (int i = 0; i < numProcesses; i++) {
            System.out.print("P" + safeSequence[i] + " ");
        }
    }
}
