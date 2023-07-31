package Cyphers;

import static Cyphers.FeistelCypher.*;

public class FeistelCBCCypher implements Cypher
{

    /**
     * Number of rands (passes) of encryption
     */
    private final int NUM_OF_ITER;

    /**
     * Initialization vector
     */
    private final long INIT_VEC;



    private FeistelCBCCypher(long initVec, int numOfIter)
    {
        this.INIT_VEC = initVec;
        this.NUM_OF_ITER = numOfIter;
    }


    /**
     * Decrypts a message using the CBC algorithm (Materials/CBC.png). To encrypt one block,
     * using an algorithm designed to decrypt the Feistil chain
     * from the picture "Materials/Feistel network.jpeg"
     */
    public long[] decryptMsg(long[] encryptedMessage, long secretKey)
    {
        long[] message = encryptedMessage.clone();

        for (int round = NUM_OF_ITER; round >= 1; round--)
        {
            short roundKey = calcRoundKey(secretKey, round);

            for (int i = encryptedMessage.length - 1; i > -1 ; i--)
            {
                message[i] = decryptOneBlock(message[i], roundKey);
                if (i == 0)
                    message[i] = message[i] ^ INIT_VEC;
                else
                    message[i] = message[i] ^ message[i - 1];
            }
        }
        return message;
    }


    /**
     * Encrypts a message using the CBC algorithm (Materials/CBC.png). To encrypt one block,
     * using the Feistel network shown in the picture "Materials/Feistel network.jpeg"
     */
    public long[] encryptMsg(long[] message, long secretKey)
    {
        long[] encryptedMessage = message.clone();

        for (int round = 1; round <= NUM_OF_ITER; round++)
        {
            short roundKey = calcRoundKey(secretKey, round);

            for (int i = 0; i < message.length; i++)
            {
                if (i == 0)
                    encryptedMessage[i] = encryptedMessage[i] ^ INIT_VEC;
                else
                    encryptedMessage[i] = encryptedMessage[i] ^ encryptedMessage[i - 1];
                encryptedMessage[i] = encryptOneBlock(encryptedMessage[i], roundKey);
            }
        }
        return encryptedMessage;
    }
}
