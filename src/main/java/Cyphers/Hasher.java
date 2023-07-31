package Cyphers;

public interface Hasher
{

    /**
     *  Calculates a hash of 64 bits from a message of any length.
     *
     * @param message from it will calculate the hash
     * @param initVec used to get the hash from the first block in the message
     * @return message hash
     */
    long calcHash(String message, long initVec);
}
