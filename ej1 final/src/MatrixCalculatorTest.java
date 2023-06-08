import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class MatrixCalculatorTest {
    @Test
    public void testMatrixCalculation() {
        try {
            MatrixCalculator calculator = new MatrixCalculator("matrixA.txt", "matrixC.txt");
            calculator.calculateMatrixB();
            int[][] matrixB = calculator.getMatrixB();

            // Test matrix B dimensions
            Assertions.assertEquals(3, matrixB.length);
            Assertions.assertEquals(3, matrixB[0].length);

            // Test matrix B values
            Assertions.assertEquals(2, matrixB[0][0]);
            Assertions.assertEquals(3, matrixB[0][1]);
            Assertions.assertEquals(0, matrixB[0][2]);
            Assertions.assertEquals(4, matrixB[1][0]);
            Assertions.assertEquals(-1, matrixB[1][1]);
            Assertions.assertEquals(2, matrixB[1][2]);
            Assertions.assertEquals(1, matrixB[2][0]);
            Assertions.assertEquals(-3, matrixB[2][1]);
            Assertions.assertEquals(5, matrixB[2][2]);
        } catch (IOException e) {
            Assertions.fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testInvalidMatrixDimensions() {
        try {
            MatrixCalculator calculator = new MatrixCalculator("matrixA_invalid.txt", "matrixC_invalid.txt");
            Assertions.assertThrows(IllegalArgumentException.class, calculator::calculateMatrixB);
        } catch (IOException e) {
            Assertions.fail("IOException occurred: " + e.getMessage());
        }
    }
}

