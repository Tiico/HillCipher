import org.jscience.mathematics.vector.Float64Matrix;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class HillCipher {
    static int radix;
    static int blockSize;

    public static void main(String[] args) {
        System.out.println("Welcome to the hillcipher");
        try {
            CipherValidator.validate(args);
        } catch (InvalidNumberException | IllegalArgumentException | FileAccessException e) {
            System.out.println("Runtime error! " + e.getMessage());
            System.exit(1);
        }
        radix = Integer.parseInt(args[0]);
        blockSize = Integer.parseInt(args[1]);
        final File keyFile = new File(args[2]);
        final File plainText = new File(args[3]);
        final File cipherText = new File(args[4]);

        encrypt(keyFile, plainText, cipherText);
    }
    public static void encrypt(File key, File source, File dest){
        int[] arr = readFromFile(source);
        double[][] plainNumbers = createReverseMatrix(arr);
        printMatrix(plainNumbers, "plainNumbers");

        int[] keyList = readFromFile(key);
        double[][] keyMatrix = createMatrix(keyList);
        printMatrix(keyMatrix, "keyMatrix");

        Float64Matrix p = Float64Matrix.valueOf(plainNumbers);
        Float64Matrix k = Float64Matrix.valueOf(keyMatrix);
        Float64Matrix product = multiplyMatrices(k, p);


        System.out.println(product);

        writeToFile(product, dest.toPath().toString());
    }
    public static Float64Matrix multiplyMatrices(Float64Matrix first, Float64Matrix second){
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
    public static double[][] createReverseMatrix(int[] arr){
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
    public static int modulo(int x, int m){
        return Math.floorMod((Math.floorMod(x, m) + m), m);
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
