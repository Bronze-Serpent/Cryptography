package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;


public class DataWork
{
    private DataWork()
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


    /**
     * Writes the bits from the passed long to an array. The bits are written in the same order as in the long variable,
     * i.e. the first 16 bits from long will correspond to the first element in the array, and so on.
     * If there are not enough significant bits in the long to fill all the elements of the array,
     * the rest of the elements will be filled with 0
     *
     * @param l this variable is perceived only as a storage of bits in memory
     * @return an array consisting of always 4 elements, where each element is the corresponding part of the passed long
     */
    public static short[] bitsFromLongToShortArr(long l)
    {
        short[] resultArr = new short[4];
        for (int i = 0; i < 4; i++)
        {
            resultArr[i] = (short) l;
            l = l >>> 16;
        }
        return resultArr;
    }


    /**
     * Rewrites bits from short array to long. Long corresponds to 4 elements
     * of the short array. Does not change the order of bytes in the array.
     *
     * @throws IllegalArgumentException shortArr.length > 4 because one long = four short = 8 byte
     * @param shortArr this array is perceived only as a storage of bits in memory
     * @return the same bits as were in the passed array, but written to the long
     */
    public static long bitsFromShortArrToLong(short[] shortArr)
    {
        if (shortArr.length > 4)
            throw new IllegalArgumentException("Переданный массив не может содержать больше 4 элементов");

        long result = 0L;

        for (int i = 0; i < shortArr.length; i++)
        {
            result = result | (shortArr[shortArr.length - 1 - i] & 0xffff);

            if (i != shortArr.length - 1)
                result = result << 16;
        }
        return result;
    }


    /**
     *  Rewrites bits from byte array to long array. One element of the long array corresponds to 8 elements
     *  of the byte array. Does not change the order of bytes in the array.
     *
     * @param byteArr this array is perceived only as a storage of bits in memory
     * @return the same bits as were in the passed array, but written to the long array
     */
    public static long[] bitsFromByteArrToLongArr(byte[] byteArr)
    {
        long[] longArr;
        if (byteArr.length % 8 == 0)
            longArr = new long[byteArr.length / 8];
        else
            longArr = new long[byteArr.length / 8 + 1];

        int startIdx = 0;
        int endIdx = Math.min(byteArr.length, 8);

        for (int i = 0; i < longArr.length; i++)
        {
            byte[] subArrayToOneLong = Arrays.copyOfRange(byteArr, startIdx, endIdx);
            longArr[i] = bitsFromByteArrToLong(subArrayToOneLong);

            startIdx += 8;
            endIdx = Math.min(byteArr.length, endIdx + 8);
        }
        return longArr;
    }


    /**
     *  Rewrites bits from long array to byte array. One element of the long array corresponds to 8 elements
     *  of the byte array. Does not change the order of bytes in the array.
     *
     * @param longArr this array is perceived only as a storage of bits in memory
     * @return the same bits as were in the passed array, but written to the byte array
     */
    public static byte[] bitsFromLongArrToByteArr(long[] longArr)
    {
        byte[] byteArr = new byte [longArr.length * 8];

        for (int i = 0; i < longArr.length; i++)
        {
            byte[] subArrayFromOneLong = bitsFromLongToByteArr(longArr[i]);
            System.arraycopy(subArrayFromOneLong, 0, byteArr, i * 8, 8);
        }
        return byteArr;
    }


    /**
     * Writes the bits from the passed array to a long variable. The bits are written in the same order as in the array,
     *  i.e. the first 8 bits from long will correspond to the first element in the array, and so on.
     *
     * @throws IllegalArgumentException if the number of elements of the passed array exceeds 8;
     * only 8 bytes can be written to a long, and 1 byte contains 1 byte.
     * @param byteArr this array is perceived only as a storage of bits in memory
     * @return  the same bits as were in the passed array, but written to the long
     */
    public static long bitsFromByteArrToLong(byte[] byteArr)
    {
        if (byteArr.length > 8)
            throw new IllegalArgumentException("Переданный массив не может содержать больше 8 элементов");

        if (byteArr.length == 0)
            return 0;

        long result = Byte.toUnsignedLong((byte) 0);

        for (int i = 0; i < byteArr.length; i++)
        {
            result = result | Byte.toUnsignedLong(byteArr[byteArr.length - 1 - i]);

            if (i != byteArr.length - 1)
                result = result << 8;
        }
        return result;
    }


    /**
     * Writes the bits from the passed long to an array. The bits are written in the same order as in the long variable,
     * i.e. the first 8 bits from long will correspond to the first element in the array, and so on.
     * If there are not enough significant bits in the long to fill all the elements of the array,
     * the rest of the elements will be filled with 0
     *
     * @param l this long is perceived only as a storage of bits in memory
     * @return the same bits as were in the passed long, but written to the byte array
     */
    public static byte[] bitsFromLongToByteArr(long l)
    {
        byte[] resultArr = new byte[8];

        for (int i = 0; i < 8; i++)
        {
            resultArr[i] = (byte) l;
            l = l >>> 8;
        }
        return resultArr;
    }


    /**
     * Creates a new array equivalent to the original, but without elements at the end equal to 0
     *
     * @param message an array from which elements equal to 0 will be removed from the end
     * @return an array equivalent to the original, but without elements at the end equal to 0
     */
    public static byte[] removeEmptyByteFromTheEnd(byte[] message)
    {
        int emptyByteCounter = 0;

        for (int i = message.length - 1; i > -1; i--)
        {
            if (message[i] == 0)
                emptyByteCounter++;
            else
                break;
        }

        byte[] messageWithoutAdded = new byte[message.length - emptyByteCounter];
        System.arraycopy(message, 0, messageWithoutAdded, 0, message.length - emptyByteCounter);

        return messageWithoutAdded;
    }
}
