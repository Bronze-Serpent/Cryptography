package utils;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;


public class RSACryptography
{

    private RSACryptography()
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

}
