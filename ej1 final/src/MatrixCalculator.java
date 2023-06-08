import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MatrixCalculator {
    private int[][] matrixA;
    private int[][] matrixC;
    private int[][] matrixB;

    public MatrixCalculator(String matrixAFile, String matrixCFile) throws IOException {
        matrixA = readMatrixFromFile(matrixAFile);
        matrixC = readMatrixFromFile(matrixCFile);
        matrixB = new int[matrixA.length][matrixA[0].length];
    }

    // Read matrix from a text file
    private int[][] readMatrixFromFile(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        int numRows = 0;
        int numCols = 0;

        // Determine matrix dimensions
        while ((line = reader.readLine()) != null) {
            String[] elements = line.trim().split("\\s+");
            numCols = elements.length;
            numRows++;
        }
        reader.close();

        // Create matrix
        int[][] matrix = new int[numRows][numCols];

        // Read matrix values from file
        reader = new BufferedReader(new FileReader(fileName));
        int row = 0;
        while ((line = reader.readLine()) != null) {
            String[] elements = line.trim().split("\\s+");
            for (int col = 0; col < numCols; col++) {
                matrix[row][col] = Integer.parseInt(elements[col]);
            }
            row++;
        }
        reader.close();

        return matrix;
    }

    // Calculate matrix B: B = C - A
    public void calculateMatrixB() {
        if (matrixA.length != matrixC.length || matrixA[0].length != matrixC[0].length) {
            throw new IllegalArgumentException("Matrices A and C must have the same dimensions.");
        }

        int numRows = matrixA.length;
        int numCols = matrixA[0].length;

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                matrixB[i][j] = matrixC[i][j] - matrixA[i][j];
            }
        }
    }

    // Get matrix B
    public int[][] getMatrixB() {
        return matrixB;
    }

    // Example JUnit test for matrix calculation
    public static void main(String[] args) {
        try {
            MatrixCalculator calculator = new MatrixCalculator("matrixA.txt", "matrixC.txt");
            calculator.calculateMatrixB();
            int[][] matrixB = calculator.getMatrixB();

            // Print matrix B
            for (int i = 0; i < matrixB.length; i++) {
                for (int j = 0; j < matrixB[0].length; j++) {
                    System.out.print(matrixB[i][j] + " ");
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
