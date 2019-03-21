import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HillCipher {

    public static void main(String[] args) {
        System.out.println("Welcome to the HillCipher bitch");
        int radix, blockSize;
        radix = blockSize = 0;
        File keyFile, plainFile, cipherFile;
        keyFile = plainFile = cipherFile = null;
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
                    plainFile = new File(args[3]);
                    cipherFile = new File(args[4]);
                    if (!keyFile.isFile() || !plainFile.isFile()){
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

        try {
            String params = "--coding=alpha" + " " + plainFile + " " + "AssignmentIncludes/cipherTest.txt";
            Process p = Runtime.getRuntime().exec("python AssignmentIncludes/hillencode" + " " + params);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Encrypt(keyFile, cipherFile);
    }

    private static void Encrypt(File key, File cipher){
        int[][] cipherNumbers = null;
        try {
            Scanner sc = new Scanner(cipher);
            ArrayList<Integer> cipherlist = new ArrayList<Integer>();
            while(sc.hasNextInt()){
                cipherlist.add(sc.nextInt());
            }

            Object[] arr = cipherlist.toArray();
            cipherNumbers = convertToMatrix(arr, 4, 3);
            System.out.println("CipherNumbers");
            printMatrix(cipherNumbers);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner sc = null;
        try {
            sc = new Scanner(key);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<Integer> keylist = new ArrayList<Integer>();
        while (sc.hasNextInt()){
            keylist.add(sc.nextInt());
        }
        int[][] keyMatrix = convertToMatrix(keylist.toArray(), 3, 3);
        System.out.println("Printing keymatrix");
        printMatrix(keyMatrix);

        int[][] product = multiplyMatrices(cipherNumbers, keyMatrix , 4, 3, 3);
        System.out.println("Printing product matrix");
        printMatrix(product);

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
    private static int[][] convertToMatrix(Object[] arr, int row, int col){
        int k = 0;
        int[][] result = new int[row][col];
        for (int i = 0; i < row; i++){
            for (int j = 0; j < col; j++){
                if (k >= 0 && k < arr.length) {
                    result[i][j] = (Integer) arr[k++];
                    System.out.print(result[i][j] + " ");
                }else{
                    result[i][j] = 0;
                }
            }
        }
        System.out.println();
        return result;
    }
    private static void printMatrix(int[][] matrix){
        System.out.println("PRINTING MATRIX");
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 3; j++){
                if (i >= 0 && i < matrix.length) {
                    System.out.print(matrix[i][j] + " ");
                }
            }
            System.out.println();
        }
    }
}
