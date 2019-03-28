import Exceptions.DeniedAccessException;
import Exceptions.FileAccessException;
import Exceptions.InvalidNumberException;
import Exceptions.OutOfRangeException;

import java.io.File;
import java.io.FileNotFoundException;

public class CipherValidator {
    public static void validate(String[] args) throws InvalidNumberException, IllegalArgumentException, FileAccessException {
        if (args.length == 5) {
            //Radix
            try {
                int radix = Integer.parseInt(args[0]);
                if (!inRange(radix, 26, 26)){
                    throw new OutOfRangeException("Radix value not in correct range, should be 26");
                }
            } catch (NumberFormatException | OutOfRangeException e) {
                throw new InvalidNumberException("Radix value error, " + e.getMessage());
            }

            //Blocksize
            try {
                int blockSize = Integer.parseInt(args[1]);
                if (!inRange(blockSize, 3, 3)){
                    throw new OutOfRangeException("Blocksize value not in correct range, should be 3");
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

            }catch (FileNotFoundException | DeniedAccessException e){
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

            }catch (FileNotFoundException | DeniedAccessException e){
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

            }catch (FileNotFoundException | DeniedAccessException e){
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
}
