import containers.*;
import utils.*;

import static utils.Algorithm.FEISTEL_CHAIN;
import static utils.Mover.*;
import static utils.Data.*;
import static utils.Crypto.*;
import static utils.Purpose.*;
import static utils.Reformer.*;

import java.io.*;


public class Application
{

    private static final String OPTIONS_FILENAME = "options.txt";


    public static void main(String[] args) throws IOException
    {
        ArgsCont argsCont = ArgParser.parseCmdArgs(args);
        OptionsCont optsCont = Data.readOptionsFromFile(OPTIONS_FILENAME);

        ArgParser.checkOptionsForAlg(optsCont, argsCont.getAlgorithm());

        long key = Long.parseLong(optsCont.get("key"));
        long initVec = 0;
        int rQ = 0;
        if (optsCont.contains("rQ"))
            rQ = Integer.parseInt(optsCont.get("rQ"));
        if (argsCont.getAlgorithm() != FEISTEL_CHAIN)
            initVec = Long.parseLong(optsCont.get("initVec"));

        byte[] messageAsByteArr = readBitByteByteFromFile(argsCont.getInputFile());
        long[] messageAsLongArr = bitsFromByteArrToLongArr(messageAsByteArr);

        byte[] processedMessage;
        if (argsCont.getWorkingMode() == ENCRYPT)
        {
            long[] encryptedMessage = new long[0];

            switch (argsCont.getAlgorithm())
            {
                case FEISTEL_CHAIN:
                        if (optsCont.contains("rQ"))
                            encryptedMessage = encryptMessageFN(messageAsLongArr, key, rQ);
                        else
                            encryptedMessage = encryptMessageFN(messageAsLongArr, key);
                        break;

                case CBC:
                    if (optsCont.contains("rQ"))
                        encryptedMessage = encryptMessageCBC(messageAsLongArr, key, initVec, rQ);
                    else
                        encryptedMessage = encryptMessageCBC(messageAsLongArr, key, initVec);
                    break;
            }
            processedMessage = bitsFromLongArrToByteArr(encryptedMessage);
        }
        else
        {
            long[] decryptedMes = new long[0];

            switch (argsCont.getAlgorithm())
            {
                case FEISTEL_CHAIN:
                    if (optsCont.contains("rQ"))
                        decryptedMes = decryptMessageFN(messageAsLongArr, key, rQ);
                    else
                        decryptedMes = decryptMessageFN(messageAsLongArr, key);
                    break;

                case CBC:
                    if (optsCont.contains("rQ"))
                        decryptedMes = decryptMessageCBC(messageAsLongArr, key, initVec, rQ);
                    else
                        decryptedMes = decryptMessageCBC(messageAsLongArr, key, initVec);
                    break;
            }
            processedMessage = removeEmptyByteFromTheEnd(bitsFromLongArrToByteArr(decryptedMes));
        }

        writeByteToFile(processedMessage, argsCont.getOutputFile());
    }

}
