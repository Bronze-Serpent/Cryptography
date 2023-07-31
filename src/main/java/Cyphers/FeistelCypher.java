package Cyphers;

import static utils.BlockCipherFunc.cyclicLeftShiftLong;
import static utils.BlockCipherFunc.cyclicRightShiftShort;
import static utils.Mover.bitsFromLongToShortArr;
import static utils.Mover.bitsFromShortArrToLong;


public class FeistelCypher implements Cypher
{

    /**
     * Number of rands (passes) of encryption
     */
    private final int NUM_OF_ITER;


    private FeistelCypher(int numOfIter)
    {
        this.NUM_OF_ITER = numOfIter;
    }


    /**
     * Decrypts the message to decrypt one block using an algorithm designed to decrypt the Feistil chain
     * from the picture "Materials/Feistel network.jpeg"
     */
    public long[] decryptMsg(long[] encryptedMessage, long secretKey)
    {
        long[] message = encryptedMessage.clone();

        for (int round = NUM_OF_ITER; round >= 1; round--)
        {
            short roundKey = calcRoundKey(secretKey, round);

            for (int i = encryptedMessage.length - 1; i > -1 ; i--)
                message[i] = decryptOneBlock(message[i], roundKey);
        }
        return message;
    }


    /**
     * Encrypts the message using the Feistel network to encrypt one block shown
     * in the picture "Materials/Feistel network.jpeg"
     */
    public long[] encryptMsg(long[] message, long secretKey)
    {
        long[] encryptedMessage = message.clone();

        for (int round = 1; round <= NUM_OF_ITER; round++)
        {
            short roundKey = calcRoundKey(secretKey, round);

            for (int i = 0; i < message.length; i++)
                encryptedMessage[i] = encryptOneBlock(encryptedMessage[i], roundKey);
        }
        return encryptedMessage;
    }


    static long decryptOneBlock(long block, short roundKey)
    {
        short[] encSubblocks = bitsFromLongToShortArr(block);
        short[] subblocks = new short[4];

        subblocks[3] = encSubblocks[0];
        subblocks[1] = (short) (encSubblocks[3] ^ roundKey);
        subblocks[2] = (short) (encSubblocks[1] ^ encSubblocks[3]);
        subblocks[0] = (short) (encSubblocks[2] ^ f2(subblocks[2], subblocks[3]));

        return bitsFromShortArrToLong(subblocks);
    }


    static long encryptOneBlock(long block, short roundKey)
    {
        short[] subblocks = bitsFromLongToShortArr(block);
        short[] encSubblocks = new short[4];

        encSubblocks[0] = subblocks[3];
        encSubblocks[3] = (short) (subblocks[1] ^ roundKey);
        encSubblocks[1] = (short) (subblocks[2] ^ encSubblocks[3]);
        encSubblocks[2] = (short) (subblocks[0] ^ f2(subblocks[2], subblocks[3]));

        return bitsFromShortArrToLong(encSubblocks);
    }


    static short f2(short val1, short val2)
    {
        return cyclicRightShiftShort((short) (val1 ^ val2), 6);
    }


    static short calcRoundKey(long secretKey, int round)
    {
        long modifiedSecretKey = cyclicLeftShiftLong(secretKey, round * 3);

        return (short) (modifiedSecretKey >>> 24);
    }
}
