import Exceptions.FileAccessException;
import Exceptions.InvalidNumberException;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class HillCipher {

    public static void main(String[] args) {
        System.out.println("Welcome to the hillcipher");
        try {
            CipherValidator.validate(args);
        } catch (InvalidNumberException | IllegalArgumentException | FileAccessException e) {
            System.out.println("Runtime error! " + e.getMessage());
            System.exit(1);
        }
        final int radix = Integer.parseInt(args[0]);
        final int blockSize = Integer.parseInt(args[1]);
        final File keyFile = new File(args[2]);
        final File plainText = new File(args[3]);
        final File cipherText = new File(args[4]);

        encrypt(keyFile, plainText, cipherText);
    }
    public static void encrypt(File key, File source, File dest){
        Object[] arr = readFromFile(source);
        int[][] plainNumbers = createReverseMatrix(arr);
        printMatrix(plainNumbers, "plainNumbers");

        Object[] keyList = readFromFile(key);
        int[][] keyMatrix = createMatrix(keyList);
        printMatrix(keyMatrix, "keyMatrix");

        int[][] product = multiplyMatrices(keyMatrix, plainNumbers);

        printMatrix(product, "product");

        writeToFile(product, dest.toPath().toString());
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
    public static int[][] createReverseMatrix(Object[] arr){
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
