public class Matrix {
    static final int SIZE = 4; // Size of the matrices (4x4)
    static int[][] matA = new int[SIZE][SIZE];
    static int[][] matB = new int[SIZE][SIZE];
    static int[][] matC = new int[SIZE][SIZE];

    // Thread class to calculate one element in the resulting matrix
    static class CellMultiplier extends Thread {
        private int row, col;

        // Constructor to initialize the thread with specific cell location
        public CellMultiplier(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void run() {
            int prod = 0;
            for (int k = 0; k < SIZE; k++) {
                prod += matA[row][k] * matB[k][col];
            }
            matC[row][col] = prod; // Store the computed value in matC
        }
    }

    public static void main(String[] args) {
        // Initialize matA and matB with some values
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                matA[i][j] = i + j; // Example initialization
                matB[i][j] = i * j; // Example initialization
            }
        }

        // Array to hold threads
        Thread[][] threads = new Thread[SIZE][SIZE];

        // Create and start threads for each cell in the resulting matrix
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                threads[i][j] = new CellMultiplier(i, j);
                threads[i][j].start(); // Start the thread
            }
        }

        // Wait for all threads to finish
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                try {
                    threads[i][j].join(); // Wait for the thread to complete
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // Print the resulting matrix matC
        System.out.println("Result matrix:");
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(matC[i][j] + " ");
            }
            System.out.println();
        }
    }
}

