import java.util.*;

public class PageFault {

    // Function to calculate page faults using FIFO algorithm
    public static int fifoPageFaults(List<Integer> referenceString, int numFrames) {
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> pages = new HashSet<>();
        int pageFaults = 0;

        for (int page : referenceString) {
            // If the page is not in memory
            if (!pages.contains(page)) {
                pageFaults++;
                if (queue.size() == numFrames) {
                    int oldPage = queue.poll();
                    pages.remove(oldPage);
                }
                queue.add(page);
                pages.add(page);
            }
        }
        return pageFaults;
    }

    // Function to calculate page faults using LRU algorithm
    public static int lruPageFaults(List<Integer> referenceString, int numFrames) {
        Map<Integer, Integer> pageMap = new HashMap<>();
        int pageFaults = 0;

        for (int i = 0; i < referenceString.size(); i++) {
            int page = referenceString.get(i);
            // If the page is not in memory
            if (!pageMap.containsKey(page)) {
                if (pageMap.size() == numFrames) {
                    // Find the least recently used page
                    int lruTime = Integer.MAX_VALUE, lruPage = -1;
                    for (Map.Entry<Integer, Integer> entry : pageMap.entrySet()) {
                        if (entry.getValue() < lruTime) {
                            lruTime = entry.getValue();
                            lruPage = entry.getKey();
                        }
                    }
                    pageMap.remove(lruPage);
                }
                pageFaults++;
            }
            // Update the last access time of the page
            pageMap.put(page, i);
        }
        return pageFaults;
    }

    // Function to calculate page faults using Optimal algorithm
    public static int optimalPageFaults(List<Integer> referenceString, int numFrames) {
        Set<Integer> pages = new HashSet<>();
        int pageFaults = 0;

        for (int i = 0; i < referenceString.size(); i++) {
            int page = referenceString.get(i);
            // If the page is not in memory
            if (!pages.contains(page)) {
                if (pages.size() == numFrames) {
                    // Find the page to replace
                    int farthest = i, pageToReplace = -1;
                    boolean found = false;

                    for (int p : pages) {
                        int j;
                        for (j = i + 1; j < referenceString.size(); j++) {
                            if (referenceString.get(j) == p) break;
                        }
                        if (j == referenceString.size()) {
                            pageToReplace = p;
                            found = true;
                            break;
                        }
                        if (j > farthest) {
                            farthest = j;
                            pageToReplace = p;
                        }
                    }

                    pages.remove(pageToReplace);
                }
                pages.add(page);
                pageFaults++;
            }
        }
        return pageFaults;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of frames: ");
        int numFrames = scanner.nextInt();

        System.out.print("Enter the number of pages in the reference string: ");
        int n = scanner.nextInt();

        List<Integer> referenceString = new ArrayList<>();
        System.out.println("Enter the reference string:");
        for (int i = 0; i < n; i++) {
            referenceString.add(scanner.nextInt());
        }

        // Calculate and display the number of page faults for each algorithm
        System.out.println("FIFO Page Faults: " + fifoPageFaults(referenceString, numFrames));
        System.out.println("LRU Page Faults: " + lruPageFaults(referenceString, numFrames));
        System.out.println("Optimal Page Faults: " + optimalPageFaults(referenceString, numFrames));

        scanner.close();
    }
}
