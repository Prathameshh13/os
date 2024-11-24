import java.util.*;

public class DiskSchedueling {

    // SSTF (Shortest Seek Time First) Algorithm
    public static int sstf(int[] requests, int head) {
        int totalSeek = 0;
        List<Integer> queue = new ArrayList<>();
        for (int req : requests) queue.add(req);

        while (!queue.isEmpty()) {
            // Find the nearest request
            int closest = Integer.MAX_VALUE, closestIdx = -1;
            for (int i = 0; i < queue.size(); i++) {
                int distance = Math.abs(queue.get(i) - head);
                if (distance < closest) {
                    closest = distance;
                    closestIdx = i;
                }
            }

            // Move the head to the closest request
            totalSeek += closest;
            head = queue.remove(closestIdx);
        }
        return totalSeek;
    }

    // SCAN Algorithm
    public static int scan(int[] requests, int head, int direction, int diskSize) {
        int totalSeek = 0;
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();

        // Separate requests into left and right of the head
        for (int req : requests) {
            if (req < head) left.add(req);
            else right.add(req);
        }

        // Sort the requests
        Collections.sort(left);
        Collections.sort(right);

        // If moving towards the spindle (lower values)
        if (direction == -1) {
            for (int i = left.size() - 1; i >= 0; i--) {
                totalSeek += Math.abs(head - left.get(i));
                head = left.get(i);
            }
            // Then move to the other end
            if (!right.isEmpty()) {
                totalSeek += Math.abs(head - 0);
                head = 0;
                for (int r : right) {
                    totalSeek += Math.abs(head - r);
                    head = r;
                }
            }
        } else { // Moving towards the larger values
            for (int r : right) {
                totalSeek += Math.abs(head - r);
                head = r;
            }
            // Then move to the other end
            if (!left.isEmpty()) {
                totalSeek += Math.abs(head - (diskSize - 1));
                head = diskSize - 1;
                for (int i = left.size() - 1; i >= 0; i--) {
                    totalSeek += Math.abs(head - left.get(i));
                    head = left.get(i);
                }
            }
        }

        return totalSeek;
    }

    // C-LOOK Algorithm
    public static int cLook(int[] requests, int head) {
        int totalSeek = 0;
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();

        // Separate requests into left and right of the head
        for (int req : requests) {
            if (req < head) left.add(req);
            else right.add(req);
        }

        // Sort the requests
        Collections.sort(left);
        Collections.sort(right);

        // Process requests to the right
        for (int r : right) {
            totalSeek += Math.abs(head - r);
            head = r;
        }
        // Jump to the lowest request and process remaining
        if (!left.isEmpty()) {
            totalSeek += Math.abs(head - left.get(0));
            head = left.get(0);
            for (int l : left) {
                totalSeek += Math.abs(head - l);
                head = l;
            }
        }

        return totalSeek;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of disk requests: ");
        int n = scanner.nextInt();

        int[] requests = new int[n];
        System.out.println("Enter the disk requests:");
        for (int i = 0; i < n; i++) {
            requests[i] = scanner.nextInt();
        }

        System.out.print("Enter the initial head position: ");
        int head = scanner.nextInt();

        System.out.print("Enter the disk size (for SCAN): ");
        int diskSize = scanner.nextInt();

        System.out.print("Enter the direction (1 for high, -1 for low): ");
        int direction = scanner.nextInt();

        System.out.println("SSTF Total Seek Operations: " + sstf(requests, head));
        System.out.println("SCAN Total Seek Operations: " + scan(requests, head, direction, diskSize));
        System.out.println("C-LOOK Total Seek Operations: " + cLook(requests, head));

        scanner.close();
    }
}
