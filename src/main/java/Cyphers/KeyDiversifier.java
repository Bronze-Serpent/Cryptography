package Cyphers;


import java.util.function.ToLongBiFunction;

public interface KeyDiversifier
{
    /**
     *  Method for key diversification based on password strings
     *
     * @param hashFunk hash function that takes a string and returns a number
     * @param password password string
     * @param salt Used to complicate the definition of the preimage of the hash function
     * @param dkLen Return key length
     * @return key, length dkLen
     */
    long[] diversify(ToLongBiFunction<String, Long> hashFunk, String password, long salt, int dkLen);
}
