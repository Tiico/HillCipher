import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class HillCipher {

    public static void main(String[] args) {
        System.out.println("Welcome to the HillCipher bitch");
        int radix, blockSize;
        File keyFile, plainText, cipherText;
        keyFile = plainText = cipherText = null;
            try {
                if (args.length > 0 && args.length <= 5) {
                    radix = Integer.parseInt(args[0]);
                    if (radix != 3){
                        System.err.println("radix has to be 3");
                        System.exit(1);
                    }
                    blockSize = Integer.parseInt(args[1]);
                    if (blockSize != 26){
                        System.err.println("blocksize must be 26");
                        System.exit(1);
                    }
                    keyFile = new File(args[2]);
                    plainText = new File(args[3]);
                    cipherText = new File(args[4]);
                    if (!keyFile.isFile() || !plainText.isFile()){
                        System.err.println("A file specified does not exits");
                        System.exit(1);
                    }
                }else{
                    throw new NumberFormatException("error");
                }
            } catch (NumberFormatException e) {
                System.err.println("Incorrect argument format, usage should be <Integer> <Integer> <Key file name> <Text file name> <Ciphertext file name>");
                System.exit(1);
            }
            File tempPlainNumber = new File("AssignmentIncludes/tempPlainNumber.txt");

        try {
            String params = "--coding=alpha" + " " + plainText + " " + tempPlainNumber;
            Runtime.getRuntime().exec("python AssignmentIncludes/hillencode" + " " + params);
        } catch (IOException e) {
            e.printStackTrace();
        }
        encrypt(keyFile, cipherText, tempPlainNumber);
        decrypt(cipherText, plainText);
    }

    private static void encrypt(File key, File cipherText, File plainNumber){
        Object[] arr = readFromFile(plainNumber);
        int[][] plainNumbers = createReverseMatrix(arr, 3, 4);

        Object[] keyList = readFromFile(key);
        int[][] keyMatrix = createMatrix(keyList, 3, 3);

        int[][] product = multiplyMatrices(keyMatrix, plainNumbers , 3, 3, 4);
        printMatrix(product);

        File tempCipherNumber = new File("AssignmentIncludes/tempCipherNumber.txt");
        writeToFile(product, tempCipherNumber.toPath().toString());

        try {
            String params = "--coding=alpha" + " " + tempCipherNumber + " " + cipherText;
            Process p = Runtime.getRuntime().exec("python AssignmentIncludes/hilldecode" + " " + params);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void decrypt(File source, File dest){
        File invKey = new File("AssignmentIncludes/invkey3-26.txt");
        Object[] key = readFromFile(invKey);
        int[][] invKeyMatrix = createMatrix(key, 3, 3);
        printMatrix(invKeyMatrix);

        //Needs to convert source from alpha to numeric, should add own function to use throughout project.
        System.out.println(source.toPath().toString());
        Object[] arr = readFromFile(source);
        int[][] cipherText = createMatrix(arr, 3, 4);
        printMatrix(cipherText);
    }
    private static int[][] multiplyMatrices(int[][] first, int[][] second, int row1, int col1, int col2){
        int[][] product = new int[row1][col2];
        for(int i = 0; i < row1; i++) {
            for (int j = 0; j < col2; j++) {
                for (int k = 0; k < col1; k++) {
                    product[i][j] += first[i][k] * second[k][j];
                }
            }
        }
        return product;
    }
    private static int[][] createReverseMatrix(Object[] arr, int row, int col){
        int k = 0;
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
    private static int[][] createMatrix(Object[] arr, int row, int col){
        int k = 0;
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
    private static Object[] readFromFile(File source){
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
    private static void writeToFile(int[][] source, String dest){
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
    private static void printMatrix(int[][] matrix){
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
