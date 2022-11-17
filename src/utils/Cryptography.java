package utils;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import static utils.BlockCipherFunc.*;
import static utils.Mover.*;


public class Cryptography
{

    private static final int DEFAULT_ROUND_QUANTITY = 12;


    private Cryptography()
    {
        throw new AssertionError("This class is not meant to be instantiated");
    }

    /**
     * Splits the ciphertext from a string into numbers less than n
     *
     * @param ciphertext ciphertext :)
     * @param n the number of the module by which is taken
     * @return Message splitted into numbers
     */
    public static List<BigInteger> parseCipherTextRSA(String ciphertext, BigInteger n)
    {
        List<BigInteger>  receivedElem = new LinkedList<>();
        int nLength = n.toString().length();

        for (int startIndex = 0 ; ;)
        {
            if (ciphertext.length() < startIndex + nLength)
            {
                if (ciphertext.length() == startIndex)
                    return receivedElem;

                receivedElem.add(new BigInteger(ciphertext.substring(startIndex)));
                return receivedElem;
            }
            else
            {
                BigInteger elem = new BigInteger(ciphertext.substring(startIndex, startIndex + nLength));

                if (elem.compareTo(n) < 0)
                {
                    receivedElem.add(elem);
                    startIndex += nLength;
                }
                else
                {
                    receivedElem.add(new BigInteger(ciphertext.substring(startIndex, startIndex + nLength - 1)));
                    startIndex += nLength - 1;
                }
            }
        }
    }


    /**
     * Decrypts the message using the formula с^d mod N = m
     *
     * @param message   the message is represented as a set of bits interpreted as an BigInteger.
     *                The message is broken by an BigInteger so that each element is less than n
     * @param sKey  the secret key
     * @param oKeyN part of the public key by which the module is taken
     * @return decrypted message represented by bits interpreted as list of BigInteger
     */
    public static List<BigInteger> decryptRSA(List<BigInteger> message, final BigInteger sKey, final BigInteger oKeyN)
    {
        return message.stream()
                .map(m -> m.modPow(sKey, oKeyN))
                .toList();
    }


    /**
     *  Calculates a hash of 64 bits from a message of any length according
     *  to the algorithm shown in the picture "Materials/Hash function.jpeg"
     *
     * @param message from it will calculate the hash
     * @param initVec used to get the hash from the first block in the message
     * @return message hash
     */
    public static long calcHash(String message, long initVec)
    {
        long[] messageAsArr = Mover.bitsFromByteArrToLongArr(message.getBytes());
        long prevBlockHash = initVec;

        for (long block : messageAsArr)
        {
            long encryptedBlock = encryptOneBlock(block, calcRoundKey(prevBlockHash, 1));
            prevBlockHash = encryptedBlock ^ prevBlockHash ^ block;
        }

        return prevBlockHash;
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
