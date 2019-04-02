import org.jscience.mathematics.number.Float64;
import org.jscience.mathematics.vector.Float64Matrix;
import org.jscience.mathematics.vector.Float64Vector;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

@SuppressWarnings("Duplicates")
public class HillDecipher {
    static int radix;
    static int blockSize;

    public static void main(String[] args) {
        System.out.println("Welcome to the HillDecipher");

        try {
            DecipherValidator.validate(args);
        } catch (InvalidNumberException | FileAccessException | IllegalArgumentException e) {
            System.out.println("Runtime error! " + e.getMessage());
        }

        radix = Integer.parseInt(args[0]);
        blockSize = Integer.parseInt(args[1]);
        final File keyFile = new File(args[2]);
        final File plainText = new File(args[3]);
        final File cipherText = new File(args[4]);

        decrypt(keyFile, cipherText, plainText);
    }

    public static void decrypt(File keyFile, File source, File dest){
        int[] key = readFromFile(keyFile);
        double[][] keyMatrix = createMatrix(key);
        printMatrix(keyMatrix, "keymatrix");
        double[][] invertedMatrix = invertMatrix(keyMatrix);
        System.out.println("invertedMatrix");
        System.out.println(invertedMatrix);

        int[] numericArr = readFromFile(source);
        double[][] cipherText = createReverseMatrix(numericArr);
        printMatrix(cipherText, "ciphertext");

        Float64Matrix inverted = Float64Matrix.valueOf(invertedMatrix);
        Float64Matrix cipher = Float64Matrix.valueOf(cipherText);

        Float64Matrix plainNumeric = multiplyMatrices(inverted, cipher);
        System.out.println(plainNumeric);

        writeToFile(plainNumeric, dest.toPath().toString());
    }

    private static double[][] invertMatrix(double[][] matrix){
        Float64Matrix keyMatrix = Float64Matrix.valueOf(matrix);
        System.out.println(keyMatrix);


        long det = Math.floorMod(keyMatrix.determinant().longValue(), radix);
        int detInv = modInverse((int) det, radix);
        System.out.println("Det: " + det);
        System.out.println("Det inv: " + detInv);
        Float64Matrix adjMatrix = matrixMod(keyMatrix.adjoint(), radix);
        double a = (double) detInv;
        Float64 f = Float64.valueOf(a);
        Float64Matrix m = matrixMod(adjMatrix.times(f),radix);

        double[][] returnMatrix = new double[matrix.length][matrix.length];

        for (int i = 0; i < matrix.length; i++){
            for (int j = 0; j < matrix.length; j++){
                returnMatrix[i][j] = m.get(i, j).doubleValue();
            }
        }
        return returnMatrix;
    }

    public static int modulo(int x, int m){
        return Math.floorMod((Math.floorMod(x, m) + m), m);
    }

    // A naive method to find modulo
    private static int modInverse(int a, int m){
        a = a % m;
        for (int x = 1; x < m; x++)
            if ((a * x) % m == 1)
                return x;
        return 1;
    }
    private static Float64Matrix matrixMod(Float64Matrix matrix, int mod){
        Float64Vector[] vectors = new Float64Vector[matrix.getNumberOfRows()];
        double[][] temp = new double[matrix.getNumberOfRows()][matrix.getNumberOfColumns()];

        for (int i = 0; i < matrix.getNumberOfRows(); i++){
            for (int j = 0; j < matrix.getNumberOfColumns(); j++){
                 temp[i][j] = Math.floorMod(Math.round(matrix.get(i, j).doubleValue()), mod);
            }
            vectors[i] = Float64Vector.valueOf(temp[i]);
        }
        Float64Matrix m = Float64Matrix.valueOf(vectors);
        return m;
    }
    private static double[][] createReverseMatrix(int[] arr){
        int k = 0;
        int row = blockSize;
        int col = arr.length / row;
        double[][] result = new double[row][col];
        for (int i = 0; i < col; i++){
            for (int j = 0; j < row; j++){
                if (k >= 0 && k < arr.length) {
                    result[j][i] = (double) arr[k++];
                }else{
                    result[j][i] = 0;
                }
            }
        }
        System.out.println();
        return result;
    }
    public static double[][] createMatrix(int[] arr){
        int k = 0;
        int row = blockSize;
        int col = arr.length / row;
        double[][] result = new double[row][col];
        for (int i = 0; i < row; i++){
            for (int j = 0; j < col; j++){
                if (k >= 0 && k < arr.length) {
                    result[i][j] = (double) arr[k++];
                }else{
                    result[i][j] = 0;
                }
            }
        }
        return result;
    }
    public static int[] readFromFile(File source){
        Scanner sc = null;
        try {
            sc = new Scanner(source);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<Integer> list = new ArrayList<Integer>();
        while(sc.hasNextInt()){
            list.add(sc.nextInt());
        }

        int[] arr = new int[list.size()];
        for (int i = 0; i < list.size(); i++){
            arr[i] = list.get(i);
        }
        return arr;
    }
    public static void writeToFile(Float64Matrix source, String dest){
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(dest));
            for (int i = 0; i < source.getNumberOfColumns(); i++){
                for (int j = 0; j < source.getNumberOfRows(); j++){
                    out.write((int) source.get(j, i).doubleValue() % radix + " ");
                }
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Float64Matrix multiplyMatrices(Float64Matrix first, Float64Matrix second){
        System.out.println(first);
        System.out.println();
        System.out.println(second);
        Float64Matrix temp = first.times(second);
        double[][] arr = new double[temp.getNumberOfRows()][temp.getNumberOfColumns()];

        for (int i = 0; i < temp.getNumberOfRows(); i++){
            for (int j = 0; j < temp.getNumberOfColumns(); j++){
                arr[i][j] = modulo((int) temp.get(i, j).doubleValue(), radix);
            }
        }

        Float64Matrix result = Float64Matrix.valueOf(arr);

        return result;

    }
    public static void printMatrix(double[][] matrix, String name){
        System.out.println("*====" + name + "====*");
        for (int i = 0; i < matrix.length; i++){
            for (int j = 0; j < matrix[i].length; j++){
                if (i >= 0 && i < matrix.length && j >= 0 && j < matrix[i].length) {
                    System.out.print(matrix[i][j] + " ");
                }
            }
            System.out.println();
        }
        System.out.println("*==============*");
    }
}
