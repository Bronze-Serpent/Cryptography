package utils;

import java.io.*;


public class Data
{


    private Data()
    {
        throw new AssertionError("This class is not meant to be instantiated");
    }


    /**
     * Reads a file byte by byte into an array of byte
     *
     * @param fileName the name of the file from which the bytes will be read
     * @return array of bytes read from the file
     * @throws IOException if no file with that name was found
     *
     */
    public static byte[] readBitByteByteFromFile(String fileName) throws IOException
    {
        byte[] fileByBytesArr;

        try(FileInputStream inputStream = new FileInputStream(fileName))
        {
            fileByBytesArr = new byte[inputStream.available()];
            int bufferSize = 64_000;

            if (inputStream.available() < bufferSize)
                bufferSize = inputStream.available();
            inputStream.read(fileByBytesArr, 0, bufferSize);
        }
        return fileByBytesArr;
    }


    /**
     * Writes the given byte array in the file
     *
     * @param byteArr array of bytes to be written to the file
     * @param fileName the name of the file where the bytes will be written to
     * @throws IOException if no file with that name was found
     *
     */
    public static void writeByteToFile(byte[] byteArr, String fileName) throws IOException
    {
        File outputFile = new File(fileName);
        try (FileOutputStream outputStream = new FileOutputStream(outputFile))
        {
            outputStream.write(byteArr);
        }
    }

}
