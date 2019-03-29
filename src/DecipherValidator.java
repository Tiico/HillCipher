import Exceptions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DecipherValidator {
    public static void validate(String[] args) throws InvalidNumberException, IllegalArgumentException, FileAccessException {
        if (args.length == 5) {
            //Radix
            try {
                int radix = Integer.parseInt(args[0]);
                if (!inRange(radix, 1, Integer.MAX_VALUE)){
                    throw new OutOfRangeException("Radix value not in correct range, should be non-zero and positive.");
                }
            } catch (NumberFormatException | OutOfRangeException e) {
                throw new InvalidNumberException("Radix value error, " + e.getMessage());
            }

            //Blocksize
            try {
                int blockSize = Integer.parseInt(args[1]);
                if (!inRange(blockSize, 3, 3)){
                    throw new OutOfRangeException("Blocksize value not in correct range, should be non-zero and positive.");
                }
            }catch (NumberFormatException | OutOfRangeException e){
                throw new InvalidNumberException("Blocksize value error, " + e.getMessage());
            }
            //Keyfile
            try {
                File key = new File(args[2]);
                if (!key.exists()){
                    throw new FileNotFoundException("file was not found");
                }
                if (!key.canRead()){
                    throw new DeniedAccessException("Unable to access file properly");
                }
                validateFile(key);

            }catch (FileNotFoundException | DeniedAccessException | InvalidFileFormatException e){
                throw new FileAccessException("Unable to handle file: \"" + args[2] + "\", " + e.getMessage());
            }

            //PlainFile
            try {
                File plainFile = new File(args[3]);
                if (!plainFile.exists()){
                    throw new FileNotFoundException("file was not found");
                }
                if (!plainFile.canRead()){
                    throw new DeniedAccessException("Unable to access file properly");
                }
                validateFile(plainFile);

            }catch (FileNotFoundException | DeniedAccessException | InvalidFileFormatException e){
                throw new FileAccessException("Unable to handle file: \"" + args[3] + "\", " + e.getMessage());
            }
            //CipherFile
            try {
                File cipherFile = new File(args[4]);
                if (!cipherFile.exists()){
                    throw new FileNotFoundException("file was not found");
                }
                if (!cipherFile.canWrite()){
                    throw new DeniedAccessException("Unable to access file properly");
                }
                validateFile(cipherFile);

            }catch (FileNotFoundException | DeniedAccessException | InvalidFileFormatException e){
                throw new FileAccessException("Unable to handle file: \"" + args[4] + "\", " + e.getMessage());
            }
        }else{
            throw new IllegalArgumentException("Incorrect amount of arguments, should be between 1 and 5");
        }
    }

    private static boolean inRange(int input, int start, int end){
        if (input <= end && input >= start)
            return true;

        return false;
    }
    private static void validateFile(File source) throws InvalidFileFormatException {
        Scanner sc = null;
        try {
            sc = new Scanner(source);
            while(sc.hasNextInt()){
                Integer.parseInt(sc.next());
            }
        } catch (FileNotFoundException | NumberFormatException e) {
            throw new InvalidFileFormatException("Invalid format in file \"" + source.toPath().toString() + "\", should be all integers");
        }

    }
}
