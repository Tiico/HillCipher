import Exceptions.FileAccessException;
import Exceptions.InvalidNumberException;
import org.jscience.mathematics.number.LargeInteger;
import org.jscience.mathematics.number.Real;
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

        try {
            KeysValidator.validate(args);
        } catch (InvalidNumberException | FileAccessException | IllegalArgumentException e) {
            System.out.println("Runtime error! " + e.getMessage());
        }

        final int radix = Integer.parseInt(args[0]);
        final int blockSize = Integer.parseInt(args[1]);
        final File keyOutput = new File(args[2]);


        double[][] matrix;

        do {
            matrix = generateKey(radix, blockSize);
            printMatrix(matrix);
        }while (!hasInverse(matrix, radix));

        System.out.println("matrix");
        printMatrix(matrix);
        writeMatrix(matrix, keyOutput, radix);
    }
    public static void writeMatrix(double[][] matrix, File dest, int radix){
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(dest));
            for (int i = 0; i < matrix[0].length; i++){
                for (int j = 0; j < matrix.length; j++){
                    if (i >= 0 && i <= matrix[0].length && j >= 0 && j <= matrix.length) {
                        out.write(((int) matrix[i][j] + " "));
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
        double[][] matrix = new double[blocksize][blocksize];

        Random rand = new Random();
        for (int i = 0; i < blocksize; i++){
            for (int j = 0; j < blocksize; j++){
                matrix[i][j] = rand.nextInt(radix);
            }
        }
        return matrix;
    }

    private static boolean hasInverse(double[][] matrix, int radix) {
        Float64Matrix key = Float64Matrix.valueOf(matrix);
        int keyDet = modulo((int) Math.round((key.determinant().doubleValue())), radix);
        if (modInverse(keyDet, radix) > 0 && keyDet != 0 ){
            System.out.println("invertible");
            return true;
        }else{
            System.out.println("not invertible");
            return false;
        }
    }
    public static int modulo(int x, int m){
        return Math.floorMod((Math.floorMod(x, m) + m), m);
    }
    // A naive method to find modulo
    private static int modInverse(int a, int m){
        a = modulo(a, m);
        for (int x = 1; x < m; x++)
            if (modulo((a * x), m) == 1)
                return x;
        return -1;
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
