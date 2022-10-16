package utils;

import static utils.BlockCipherFunc.*;
import static utils.DataWork.*;


public class CryptoUtils
{

    private static final int ROUND_QUANTITY = 12;


    private CryptoUtils()
    {
        throw new AssertionError("This class is not meant to be instantiated");
    }


    /**
     * Decrypts a message using the CBC algorithm (Materials/CBC.png). To encrypt one block,
     * using an algorithm designed to decrypt the Feistil chain
     * from the picture "Feistel network.jpeg"
     *
     * @param encryptedMessage message encrypted by the Feistil chain from the picture "Feistel network.jpeg"
     * @param secretKey key to be used for decryption
     * @return decrypted message
     */
    public static long[] decryptMessage(long[] encryptedMessage, long secretKey, long initVec)
    {
        long[] message = encryptedMessage.clone();

        for (int round = ROUND_QUANTITY; round >= 1; round--)
        {
            for (int i = encryptedMessage.length - 1; i > -1 ; i--)
            {
                message[i] = decryptOneBlock(message[i], roundKey(secretKey, round));
                if (i == 0)
                    message[i] = message[i] ^ initVec;
                else
                    message[i] = message[i] ^ message[i - 1];
            }
        }
        return message;
    }


    /**
     * Encrypts a message using the CBC algorithm (Materials/CBC.png). To encrypt one block,
     * using the Feistel network shown in the picture "Materials/Feistel network.jpeg"
     *
     * @param message original message
     * @param secretKey key to be used for encryption
     * @return encrypted message
     */
    public static long[] encryptMessage(long[] message, long secretKey, long initVec)
    {
        long[] encryptedMessage = message.clone();

        for (int round = 1; round <= ROUND_QUANTITY; round++)
        {
            for (int i = 0; i < message.length; i++)
            {
                if (i == 0)
                    encryptedMessage[i] = encryptedMessage[i] ^ initVec;
                else
                    encryptedMessage[i] = encryptedMessage[i] ^ encryptedMessage[i - 1];
                encryptedMessage[i] = encryptOneBlock(encryptedMessage[i], roundKey(secretKey, round));
            }
        }
        return encryptedMessage;
    }


    private static long decryptOneBlock(long block, short roundKey)
    {
        short[] encSubblocks = bitsFromLongToShortArr(block);
        short[] subblocks = new short[4];

        subblocks[3] = encSubblocks[0];
        subblocks[1] = (short) (encSubblocks[3] ^ roundKey);
        subblocks[2] = (short) (encSubblocks[1] ^ encSubblocks[3]);
        subblocks[0] = (short) (encSubblocks[2] ^ f2(subblocks[2], subblocks[3]));

        return bitsFromShortArrToLong(subblocks);
    }


    private static long encryptOneBlock(long block, short roundKey)
    {
        short[] subblocks = bitsFromLongToShortArr(block);
        short[] encSubblocks = new short[4];

        encSubblocks[0] = subblocks[3];
        encSubblocks[3] = (short) (subblocks[1] ^ roundKey);
        encSubblocks[1] = (short) (subblocks[2] ^ encSubblocks[3]);
        encSubblocks[2] = (short) (subblocks[0] ^ f2(subblocks[2], subblocks[3]));

        return bitsFromShortArrToLong(encSubblocks);
    }


    private static short f2(short val1, short val2)
    {
        return cyclicRightShiftShort((short) (val1 ^ val2), 6);
    }


    private static short roundKey(long secretKey, int round)
    {
        long modifiedSecretKey = cyclicLeftShiftLong(secretKey, round * 3);

        return (short) (modifiedSecretKey >>> 24);
    }



}
