import org.jscience.mathematics.vector.Float64Matrix;
import org.jscience.mathematics.vector.Float64Vector;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


public class HillKeys {
    public static void main(String[] args) {
        System.out.println("Welcome to the HillKeys");
        int radix, blockSize;
        radix = blockSize = 0;
        File keyFile = null;
        try {
            if (args.length > 0 && args.length <= 5) {
                radix = Integer.parseInt(args[0]);
                if (radix != 26) {
                    System.err.println("radix has to be 26");
                    System.exit(1);
                }
                blockSize = Integer.parseInt(args[1]);
                if (blockSize != 3) {
                    System.err.println("blocksize must be 3");
                    System.exit(1);
                }
                keyFile = new File(args[2]);
            } else {
                throw new NumberFormatException("error");
            }
        } catch (NumberFormatException e) {
            System.err.println("Incorrect argument format, usage should be <int blocksize> <int radix> <Key file name> <Text file name> <Ciphertext file name>");
            System.exit(1);
        }

        double[][] matrix = generateKey(radix, blockSize);
        File output = new File("AssignmentIncludes/key3-26.txt");

        System.out.println("matrix");
        printMatrix(matrix);
        writeMatrix(matrix, output);
    }
    public static void writeMatrix(double[][] matrix, File dest){
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(dest));
            for (int i = 0; i < matrix[0].length; i++){
                for (int j = 0; j < matrix.length; j++){
                    if (i >= 0 && i <= matrix[0].length && j >= 0 && j <= matrix.length) {
                        out.write((int) matrix[j][i] % 26 + " ");
                    }
                }
                out.newLine();
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double[][] generateKey(int radix, int blocksize) {
        //TODO Triple-check that the keys generated actually works.
        int[] sequence = new int[blocksize * blocksize];
        double[][] matrix = new double[blocksize][blocksize];
        int k = 0;

        Random rand = new Random();
        for (int i = 0; i < blocksize * blocksize; i++) {
            sequence[i] = rand.nextInt(radix);
        }
        for (int i = 0; i < blocksize; i++){
            for (int j = 0; j < blocksize; j++){
                matrix[i][j] = sequence[k++];
            }
        }
        Float64Vector column0 = Float64Vector.valueOf(matrix[0]);
        Float64Vector column1 = Float64Vector.valueOf(matrix[1]);
        Float64Vector column2 = Float64Vector.valueOf(matrix[2]);
        Float64Matrix key = Float64Matrix.valueOf(column0, column1, column2).transpose();

        if (!hasInverse(key, radix, blocksize)) {
            generateKey(radix, blocksize);
        }

        return matrix;
    }

    private static boolean hasInverse(Float64Matrix key, int radix, int blocksize) {
        double[][] matrix = new double[blocksize][blocksize];
        int k = 0;
        if (!key.determinant().equals(0)){
            System.out.println("invertible");
            return true;
        }else{
            System.out.println("not invertible");
            return false;
        }
    }
    public static void printMatrix(double[][] matrix){
        for (int i = 0; i < matrix.length; i++){
            for (int j = 0; j < matrix[i].length; j++){
                if (i >= 0 && i < matrix.length && j >= 0 && j < matrix[i].length) {
                    System.out.print(matrix[i][j] + " ");
                }
            }
            System.out.println();
        }
    }
}