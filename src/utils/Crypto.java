package utils;

import static utils.BlockCipherFunc.*;
import static utils.Mover.*;


public class Crypto
{

    private static final int DEFAULT_ROUND_QUANTITY = 12;


    private Crypto()
    {
        throw new AssertionError("This class is not meant to be instantiated");
    }


    /**
     * Decrypts the message to decrypt one block using an algorithm designed to decrypt the Feistil chain
     * from the picture "Materials/Feistel network.jpeg"
     *
     * @param encryptedMessage message encrypted by the Feistil chain from the picture "Materials/Feistel network.jpeg"
     * @param secretKey key to be used for decryption
     * @param rQ number of encryption rounds
     * @return decrypted message
     */
    public static long[] decryptMessageFN(long[] encryptedMessage, long secretKey, int rQ)
    {
        long[] message = encryptedMessage.clone();

        for (int round = rQ; round >= 1; round--)
        {
            short roundKey = calcRoundKey(secretKey, round);

            for (int i = encryptedMessage.length - 1; i > -1 ; i--)
                message[i] = decryptOneBlock(message[i], roundKey);
        }
        return message;
    }


    public static long[] decryptMessageFN(long[] encryptedMessage, long secretKey)
    {
        return decryptMessageFN(encryptedMessage, secretKey, DEFAULT_ROUND_QUANTITY);
    }


    /**
     * Encrypts the message using the Feistel network to encrypt one block shown
     * in the picture "Materials/Feistel network.jpeg"
     *
     * @param message original message
     * @param secretKey key to be used for encryption
     * @param rQ number of encryption rounds
     * @return encrypted message
     */
    public static long[] encryptMessageFN(long[] message, long secretKey, int rQ)
    {
        long[] encryptedMessage = message.clone();

        for (int round = 1; round <= rQ; round++)
        {
            short roundKey = calcRoundKey(secretKey, round);

            for (int i = 0; i < message.length; i++)
                encryptedMessage[i] = encryptOneBlock(encryptedMessage[i], roundKey);
        }
        return encryptedMessage;
    }


    public static long[] encryptMessageFN(long[] message, long secretKey)
    {
        return encryptMessageFN(message, secretKey, DEFAULT_ROUND_QUANTITY);
    }


    /**
     * Decrypts a message using the CBC algorithm (Materials/CBC.png). To encrypt one block,
     * using an algorithm designed to decrypt the Feistil chain
     * from the picture "Materials/Feistel network.jpeg"
     *
     * @param encryptedMessage message encrypted by the CBC algorithm, using Feistil chain
     * from the picture "Feistel network.jpeg"
     * @param secretKey key to be used for decryption
     * @param initVec initialization vector to be used for decryption
     * @return decrypted message
     */
    public static long[] decryptMessageCBC(long[] encryptedMessage, long secretKey, long initVec, int rq)
    {
        long[] message = encryptedMessage.clone();

        for (int round = rq; round >= 1; round--)
        {
            short roundKey = calcRoundKey(secretKey, round);

            for (int i = encryptedMessage.length - 1; i > -1 ; i--)
            {
                message[i] = decryptOneBlock(message[i], roundKey);
                if (i == 0)
                    message[i] = message[i] ^ initVec;
                else
                    message[i] = message[i] ^ message[i - 1];
            }
        }
        return message;
    }


    public static long[] decryptMessageCBC(long[] encryptedMessage, long secretKey, long initVec)
    {
        return decryptMessageCBC(encryptedMessage, secretKey, initVec, DEFAULT_ROUND_QUANTITY);
    }


    /**
     * Encrypts a message using the CBC algorithm (Materials/CBC.png). To encrypt one block,
     * using the Feistel network shown in the picture "Materials/Feistel network.jpeg"
     *
     * @param message original message
     * @param secretKey key to be used for encryption
     * @param initVec initialization vector to be used for decryption
     * @return encrypted message
     */
    public static long[] encryptMessageCBC(long[] message, long secretKey, long initVec, int rQ)
    {
        long[] encryptedMessage = message.clone();

        for (int round = 1; round <= rQ; round++)
        {
            short roundKey = calcRoundKey(secretKey, round);

            for (int i = 0; i < message.length; i++)
            {
                if (i == 0)
                    encryptedMessage[i] = encryptedMessage[i] ^ initVec;
                else
                    encryptedMessage[i] = encryptedMessage[i] ^ encryptedMessage[i - 1];
                encryptedMessage[i] = encryptOneBlock(encryptedMessage[i], roundKey);
            }
        }
        return encryptedMessage;
    }


    public static long[] encryptMessageCBC(long[] message, long secretKey, long initVec)
    {
        return encryptMessageCBC(message, secretKey, initVec, DEFAULT_ROUND_QUANTITY);
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


    private static short calcRoundKey(long secretKey, int round)
    {
        long modifiedSecretKey = cyclicLeftShiftLong(secretKey, round * 3);

        return (short) (modifiedSecretKey >>> 24);
    }

}
