import Exceptions.FileAccessException;
import Exceptions.InvalidNumberException;
import org.jscience.mathematics.number.Float64;
import org.jscience.mathematics.vector.Float64Matrix;
import org.jscience.mathematics.vector.Float64Vector;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

@SuppressWarnings("Duplicates")
public class HillDecipher {
    public static void main(String[] args) {
        System.out.println("Welcome to the HillDecipher");

        try {
            DecipherValidator.validate(args);
        } catch (InvalidNumberException | FileAccessException | IllegalArgumentException e) {
            System.out.println("Runtime error! " + e.getMessage());
        }

        final int radix = Integer.parseInt(args[0]);
        final int blockSize = Integer.parseInt(args[1]);
        final File keyFile = new File(args[2]);
        final File plainText = new File(args[3]);
        final File cipherText = new File(args[4]);

        decrypt(keyFile, cipherText, plainText);
    }

    public static void decrypt(File keyFile, File source, File dest){
        Object[] key = readFromFile(keyFile);
        int[][] keyMatrix = createMatrix(key);
        int[][] invertedMatrix = invertMatrix(keyMatrix);

        printMatrix(invertedMatrix, "Inverted matrix");

        Object[] numericArr = readFromFile(source);
        int[][] cipherText = createReverseMatrix(numericArr);
        printMatrix(cipherText, "cipherText");
        int[][] plainNumeric = multiplyMatrices(invertedMatrix, cipherText);
        printMatrix(plainNumeric, "plainNumeric");

        writeToFile(plainNumeric, dest.toPath().toString());
    }

    private static int[][] invertMatrix(int[][] matrix){
        Float64Vector vectors[] = new Float64Vector[matrix.length];
        double[][] result = new double[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                result[i][j] = (double) matrix[i][j];
            }
            vectors[i] = Float64Vector.valueOf(result[i]);
        }
/*        double[] c0 = {16, 0, 17};
        double[] c1 = {25, 8, 6};
        double[] c2 = {2, 9, 7};
        Float64Vector column0 = Float64Vector.valueOf(c0);
        Float64Vector column1 = Float64Vector.valueOf(c1);
        Float64Vector column2 = Float64Vector.valueOf(c2);*/

        Float64Matrix keyMatrix = Float64Matrix.valueOf(vectors);


        long det = Math.floorMod(keyMatrix.determinant().longValue(), 26);
        int detInv = modInverse((int) det, 26);
        Float64Matrix adjMatrix = matrixMod(keyMatrix.adjoint(), 26);
        double a = (double) detInv;
        Float64 f = Float64.valueOf(a);
        Float64Matrix m = matrixMod(adjMatrix.times(f),26);

        int[][] returnMatrix = new int[matrix.length][matrix.length];

        for (int i = 0; i < matrix.length; i++){
            for (int j = 0; j < matrix.length; j++){
                returnMatrix[i][j] = (int) m.get(i, j).doubleValue();
            }
        }
        return returnMatrix;
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
    private static int[][] createReverseMatrix(Object[] arr){
        int k = 0;
        int row = 3;
        int col = arr.length / row;
        int[][] result = new int[row][col];
        for (int i = 0; i < col; i++){
            for (int j = 0; j < row; j++){
                if (k >= 0 && k < arr.length) {
                    result[j][i] = (Integer) arr[k++];
                }else{
                    result[j][i] = 0;
                }
            }
        }
        System.out.println();
        return result;
    }
    public static int[][] createMatrix(Object[] arr){
        int k = 0;
        int row = 3;
        int col = arr.length / row;
        int[][] result = new int[row][col];
        for (int i = 0; i < row; i++){
            for (int j = 0; j < col; j++){
                if (k >= 0 && k < arr.length) {
                    result[i][j] = (Integer) arr[k++];
                }else{
                    result[i][j] = 0;
                }
            }
        }
        return result;
    }
    public static Object[] readFromFile(File source){
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

        Object[] arr = list.toArray();
        return arr;
    }
    public static void writeToFile(int[][] source, String dest){
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(dest));
            for (int i = 0; i < source[0].length; i++){
                for (int j = 0; j < source.length; j++){
                    if (i >= 0 && i <= source[0].length && j >= 0 && j <= source.length) {
                        out.write(source[j][i] % 26 + " ");
                    }
                }
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[][] multiplyMatrices(int[][] first, int[][] second){
        int[][] product = new int[first.length][second[0].length];
        int row1 = first.length;
        int col1 = first[0].length;
        int col2 = second[0].length;


        for(int i = 0; i < row1; i++) {
            for (int j = 0; j < col2; j++) {
                for (int k = 0; k < col1; k++) {
                    product[i][j] += first[i][k] * second[k][j];
                    product[i][j] = product[i][j] % 26;
                }
            }
        }
        return product;
    }
    public static void printMatrix(int[][] matrix, String name){
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
