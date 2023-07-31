package Cyphers;

public interface Cypher
{

    /**
     * Decrypts the message.
     *
     * @param encryptedMsg message encrypted by the Feistil chain from the picture "Materials/Feistel network.jpeg"
     * @param secretKey key to be used for decryption
     * @return decrypted message
     */
    long[] decryptMsg(long[] encryptedMsg, long secretKey);


    /**
     * Encrypts the message.
     *
     * @param msg original message
     * @param secretKey key to be used for encryption
     * @return encrypted message
     */
    long[] encryptMsg(long[] msg, long secretKey);

}
