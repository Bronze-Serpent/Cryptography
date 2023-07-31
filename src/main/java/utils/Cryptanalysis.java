package utils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.function.ToLongFunction;

import static utils.MathFunc.*;


public class Cryptanalysis
{

    public Cryptanalysis() { throw new AssertionError("This class is not meant to be instantiated"); }

    /**
     * Computes the RSA secret key (d) using the public key (e, n) in formula m^(e * d) mod N = m.
     *
     * @param oKeyN part of the public key by which the module is taken (N)
     * @param oKeyE part of the public key by which the exponent is calculated (e)
     * @return the secret key (d)
     */
    public static BigInteger calcSKeyRSA(BigInteger oKeyN, BigInteger oKeyE)
    {
        BigInteger[] multipliers = factorizationRSA(oKeyN);
        BigInteger elerFunkRez = eulerFunk(multipliers[0], multipliers[1]);

        return pickUpTheKey(elerFunkRez, oKeyE);
    }


    private static BigInteger pickUpTheKey(BigInteger elerFunkRez, BigInteger e)
    {
        BigInteger k = BigInteger.ZERO;

        while (true)
        {
            BigInteger numerator = elerFunkRez.multiply(k).add(BigInteger.ONE);
            if (numerator.mod(e).compareTo(BigInteger.ZERO) == 0)
                return numerator.divide(e);

            k = k.add(BigInteger.ONE);
        }
    }


    /**
     * Performs a birthday attack on the passed hash function
     *
     * @param alphabet characters that make up the generated message
     * @param mesSize generated message size
     * @param numOfPairs number of computed hash values
     * @param hashFunk hash function for which the attack is made
     * @return attack result
     */
    public static BirthdayAttackRez birthdayAttack(char[] alphabet, int mesSize, long numOfPairs, ToLongFunction<String> hashFunk)
    {
        Map<Long, String> mHPairs = new HashMap<>();

        for (int i = 0; i < numOfPairs; i++)
        {
            String randomMes = RndMessager.getMessage(alphabet, mesSize);
            long mesHash = hashFunk.applyAsLong(randomMes);

            if (mHPairs.containsKey(mesHash) && !mHPairs.get(mesHash).equals(randomMes))
                return new BirthdayAttackRez(mesHash, randomMes, mHPairs.get(mesHash));
            else
                mHPairs.put(mesHash, randomMes);
        }

        return null;
    }


    /**
     * Container, for the result of the birthday attack
     */
    public static class BirthdayAttackRez
    {
        final long hash;
        final String message1;
        final String message2;

        public BirthdayAttackRez(long hash, String message1, String message2)
        {
            this.hash = hash;
            this.message1 = message1;
            this.message2 = message2;
        }


        public long getHash() { return hash; }

        public String getMessage1() { return message1; }

        public String getMessage2() { return message2; }
    }
}
