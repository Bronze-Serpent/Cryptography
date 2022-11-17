package utils;

import java.math.BigInteger;


public class MathFunc
{

    public MathFunc() { throw new AssertionError("This class is not meant to be instantiated"); }


    /**
     * Factorization of the transmitted public key (intended for factorization by 2 only).
     * @param oKeyN part of the public key by which the module is taken
     * @return An array of length 2. Consists of two factors of the passed number.
     */
    public static BigInteger[] factorizationRSA(BigInteger oKeyN)
    {
        for(BigInteger elem = BigInteger.TWO, increment = BigInteger.ONE; elem.compareTo(oKeyN) < 0; elem = elem.add(increment))
            if (oKeyN.mod(elem).compareTo(BigInteger.ZERO) == 0)
                return new BigInteger[] {oKeyN.divide(elem), elem};

        return new BigInteger[] {oKeyN, BigInteger.ONE};
    }


    /**
     * Calculates the Euler function from the formula (p - 1) * (q -1)
     *
     * @return Euler function result
     */
    public static BigInteger eulerFunk(BigInteger p, BigInteger q)
    {
        p = p.subtract(BigInteger.ONE);
        q = q.subtract(BigInteger.ONE);

        return p.multiply(q);
    }
}
