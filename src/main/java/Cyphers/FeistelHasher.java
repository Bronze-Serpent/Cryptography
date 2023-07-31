package Cyphers;


import utils.Mover;


public class FeistelHasher implements Hasher
{

    /**
     *  For calculating used algorithm shown in the picture "Materials/Hash function.jpeg".
     *  The encoder is based on the Feistel Chain used by Class FeistelCypher.
     */
    public long calcHash(String message, long initVec)
    {
        long[] messageAsArr = Mover.bitsFromByteArrToLongArr(message.getBytes());
        long prevBlockHash = initVec;

        for (long block : messageAsArr)
        {
            long encryptedBlock = FeistelCypher.encryptOneBlock(block, FeistelCypher.calcRoundKey(prevBlockHash, 1));
            prevBlockHash = encryptedBlock ^ prevBlockHash ^ block;
        }

        return prevBlockHash;
    }
}
