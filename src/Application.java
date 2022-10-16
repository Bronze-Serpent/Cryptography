import static utils.CryptoUtils.*;
import static utils.DataWork.*;
import java.io.*;

public class Application
{

    public static void main(String[] args) throws IOException
    {
        InputArgs inputArgs = parseCmdArgs(args);
        byte[] messageAsByteArr = readBitByteByteFromFile(inputArgs.getInputFile());
        long[] messageAsLongArr = bitsFromByteArrToLongArr(messageAsByteArr);

        byte[] processedMessage;
        if (inputArgs.getWorkingMode().equals("encrypt"))
        {
            long[] encryptedMessage = encryptMessage(messageAsLongArr, inputArgs.getKey(), inputArgs.getInitVec());

            processedMessage = bitsFromLongArrToByteArr(encryptedMessage);
        }
        else
        {
            byte[] decryptedMes = bitsFromLongArrToByteArr(
                    decryptMessage(messageAsLongArr, inputArgs.getKey(), inputArgs.getInitVec()));

            processedMessage = removeEmptyByteFromTheEnd(decryptedMes);
        }

        writeByteToFile(processedMessage, inputArgs.getOutputFile());
    }


    private static InputArgs parseCmdArgs(String[] args)
    {
        if (args.length != 5)
        {
            String argsAsLine = "";
            for (String arg : args)
                argsAsLine += arg;

            throw new RuntimeException("The program cannot be run with these arguments: " + argsAsLine +
                    " Required arguments: input_file output_file key working_mode initialization_vector");
        }

        if (!(args[3].equals("encrypt") || args[3].equals("decrypt")))
            throw new RuntimeException("The program cannot be run with this working mode: " + args[3] +
                    ". Valid operating modes: \"encrypt\" and \"decrypt\"");

        try
        {
            long key = Long.parseLong(args[2]);

            try
            {
                long initVec = Long.parseLong(args[4]);
                return new InputArgs(args[0], args[1], key, args[3], initVec);
            }
            // this is bad, I think
            catch (NumberFormatException e)
            {
                throw new RuntimeException("The program cannot be run with initVec: " + args[4] +
                        " initVec must be a number valid for long");
            }
        }
        // this is bad, I think
        catch (NumberFormatException e)
        {
            throw new RuntimeException("The program cannot be run with key: " + args[2] +
                    " Key must be a number valid for long");
        }
    }


    private static void test() throws IOException
    {
        for(byte b : readBitByteByteFromFile("text.txt"))
            System.out.print(b + " ");

        System.out.println();

        for(byte b : readBitByteByteFromFile("output.txt"))
            System.out.print(b + " ");
    }


    private static void test2()
    {
        long l_1 = -1245435;
        long l_2 = 65535;
        long result_1 = l_1 & l_2;
        short k = (short) result_1;

        System.out.println(result_1);
        System.out.println(k);
        System.out.println();

        long long1 = 1245435;
        long long2 = 65535;
        long result_2 = long1 & long2;
        short k_2 = (short) result_2;

        System.out.println(result_2);
        System.out.println(k_2);
        System.out.println();

        long l_3 = -1245435;
        long result_3 = Math.abs(l_3);
        short k_3 = (short) result_3;

        System.out.println(l_3);
        System.out.println(k_3);
    }


    private static void test3()
    {
        long result = 0;
        byte b1 = -114;
        byte b2 = -95;

        result = Byte.toUnsignedLong(b1);
        System.out.println(result);

        result = result << 8;
        System.out.println(result);

        result = result | Byte.toUnsignedLong(b2);
        System.out.println(result);


        byte receivedB2 = (byte) result;
        System.out.println(receivedB2);

        result = result >>> 8;
        System.out.println(result);

        byte receivedB1 = (byte) result;
        System.out.println(receivedB1);
    }


    private static void test4()
    {
        long result = 0;
        short s1 = -32768;
        short s2 = 32767;

        result = s1 & 0xffff;
        System.out.println(result);

        result = result << 16;
        System.out.println(result);

        result = result | s2 & 0xffff;
        System.out.println(result);


        short receivedS2 = (short) result;
        System.out.println(receivedS2);

        result = result >>> 16;
        System.out.println(result);

        short receivedS1 = (short) result;
        System.out.println(receivedS1);
    }
}
