import java.util.*;

class Process {
    int id, arrivalTime, burstTime, priority;
    int finishTime, waitingTime, turnaroundTime, remainingTime;

    Process(int id, int arrivalTime, int burstTime, int priority) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime; // Used for preemptive algorithms
    }
}

public class SchedulingAlgorithms {

    // FCFS Scheduling
    public static void fcfs(List<Process> processes) {
        int currentTime = 0;
        for (Process p : processes) {
            if (currentTime < p.arrivalTime) currentTime = p.arrivalTime;
            p.finishTime = currentTime + p.burstTime;
            p.turnaroundTime = p.finishTime - p.arrivalTime;
            p.waitingTime = p.turnaroundTime - p.burstTime;
            currentTime += p.burstTime;
        }
    }

    // SJF Non-Preemptive Scheduling
    public static void sjfNonPreemptive(List<Process> processes) {
        int currentTime = 0;
        List<Process> readyQueue = new ArrayList<>();
        List<Process> result = new ArrayList<>();
        while (result.size() < processes.size()) {
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && !readyQueue.contains(p) && !result.contains(p)) {
                    readyQueue.add(p);
                }
            }
            if (!readyQueue.isEmpty()) {
                readyQueue.sort(Comparator.comparingInt(p -> p.burstTime));
                Process shortest = readyQueue.remove(0);
                shortest.finishTime = currentTime + shortest.burstTime;
                shortest.turnaroundTime = shortest.finishTime - shortest.arrivalTime;
                shortest.waitingTime = shortest.turnaroundTime - shortest.burstTime;
                currentTime += shortest.burstTime;
                result.add(shortest);
            } else {
                currentTime++;
            }
        }
    }

    // SJF Preemptive Scheduling
    public static void sjfPreemptive(List<Process> processes) {
        int currentTime = 0;
        int completed = 0;
        Process current = null;
        while (completed < processes.size()) {
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && p.remainingTime > 0) {
                    if (current == null || p.remainingTime < current.remainingTime) {
                        current = p;
                    }
                }
            }
            if (current != null) {
                current.remainingTime--;
                currentTime++;
                if (current.remainingTime == 0) {
                    completed++;
                    current.finishTime = currentTime;
                    current.turnaroundTime = current.finishTime - current.arrivalTime;
                    current.waitingTime = current.turnaroundTime - current.burstTime;
                    current = null;
                }
            } else {
                currentTime++;
            }
        }
    }

    // Priority Scheduling (Non-Preemptive)
    public static void priorityNonPreemptive(List<Process> processes) {
        int currentTime = 0;
        List<Process> readyQueue = new ArrayList<>();
        List<Process> result = new ArrayList<>();
        while (result.size() < processes.size()) {
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && !readyQueue.contains(p) && !result.contains(p)) {
                    readyQueue.add(p);
                }
            }
            if (!readyQueue.isEmpty()) {
                readyQueue.sort(Comparator.comparingInt(p -> p.priority));
                Process highestPriority = readyQueue.remove(0);
                highestPriority.finishTime = currentTime + highestPriority.burstTime;
                highestPriority.turnaroundTime = highestPriority.finishTime - highestPriority.arrivalTime;
                highestPriority.waitingTime = highestPriority.turnaroundTime - highestPriority.burstTime;
                currentTime += highestPriority.burstTime;
                result.add(highestPriority);
            } else {
                currentTime++;
            }
        }
    }

    // Round Robin Scheduling
    public static void roundRobin(List<Process> processes, int timeQuantum) {
        int currentTime = 0;
        Queue<Process> readyQueue = new LinkedList<>();
        for (Process p : processes) {
            if (p.arrivalTime == 0) readyQueue.add(p);
        }
        while (!readyQueue.isEmpty()) {
            Process current = readyQueue.poll();
            int executeTime = Math.min(timeQuantum, current.remainingTime);
            current.remainingTime -= executeTime;
            currentTime += executeTime;

            for (Process p : processes) {
                if (p.arrivalTime > currentTime - executeTime && p.arrivalTime <= currentTime && !readyQueue.contains(p)) {
                    readyQueue.add(p);
                }
            }
            if (current.remainingTime > 0) {
                readyQueue.add(current);
            } else {
                current.finishTime = currentTime;
                current.turnaroundTime = current.finishTime - current.arrivalTime;
                current.waitingTime = current.turnaroundTime - current.burstTime;
            }
        }
    }

    public static void displayResults(List<Process> processes) {
        System.out.println("ID\tArrival\tBurst\tPriority\tFinish\tTurnaround\tWaiting");
        for (Process p : processes) {
            System.out.printf("%d\t%d\t%d\t%d\t\t%d\t%d\t\t%d\n", p.id, p.arrivalTime, p.burstTime, p.priority, p.finishTime, p.turnaroundTime, p.waitingTime);
        }
    }

    public static void main(String[] args) {
        // Predefined input
        List<Process> processes = Arrays.asList(
                new Process(1, 0, 7, 2),
                new Process(2, 2, 4, 1),
                new Process(3, 4, 1, 3),
                new Process(4, 5, 3, 2)
        );

        // Clone processes for independent scheduling results
        List<Process> fcfsProcesses = new ArrayList<>();
        List<Process> sjfNonPreemptiveProcesses = new ArrayList<>();
        List<Process> sjfPreemptiveProcesses = new ArrayList<>();
        List<Process> priorityProcesses = new ArrayList<>();
        List<Process> rrProcesses = new ArrayList<>();

        for (Process p : processes) {
            fcfsProcesses.add(new Process(p.id, p.arrivalTime, p.burstTime, p.priority));
            sjfNonPreemptiveProcesses.add(new Process(p.id, p.arrivalTime, p.burstTime, p.priority));
            sjfPreemptiveProcesses.add(new Process(p.id, p.arrivalTime, p.burstTime, p.priority));
            priorityProcesses.add(new Process(p.id, p.arrivalTime, p.burstTime, p.priority));
            rrProcesses.add(new Process(p.id, p.arrivalTime, p.burstTime, p.priority));
        }

        System.out.println("\nFCFS Scheduling:");
        fcfs(fcfsProcesses);
        displayResults(fcfsProcesses);

        System.out.println("\nSJF Non-Preemptive Scheduling:");
        sjfNonPreemptive(sjfNonPreemptiveProcesses);
        displayResults(sjfNonPreemptiveProcesses);

        System.out.println("\nSJF Preemptive Scheduling:");
        sjfPreemptive(sjfPreemptiveProcesses);
        displayResults(sjfPreemptiveProcesses);

        System.out.println("\nPriority Non-Preemptive Scheduling:");
        priorityNonPreemptive(priorityProcesses);
        displayResults(priorityProcesses);

        int timeQuantum = 2; // Predefined time quantum
        System.out.println("\nRound Robin Scheduling (Time Quantum = " + timeQuantum + "):");
        roundRobin(rrProcesses, timeQuantum);
        displayResults(rrProcesses);
    }
}
