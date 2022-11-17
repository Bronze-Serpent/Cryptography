import utils.Cryptanalysis;
import utils.Mover;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import static utils.Cryptanalysis.*;
import static utils.Cryptography.*;
import static utils.Cryptography.calcHash;


public class Application
{

    public static void main(String[] args)
    {
        char[] alphabet = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

        int mesSize = 14;
        long numOfPairs = 6_100_000; // 10^(-6)% :)

        /*
        Cryptanalysis.BirthdayAttackRez birthdayAttackRez = null;
        int numOfIterations = 0;
        while (birthdayAttackRez == null)
        {
            birthdayAttackRez = birthdayAttack(alphabet, mesSize, numOfPairs,
                    m -> calcHash(m, 54321));
            numOfIterations++;
        }
         */

        System.out.println("Хэш функция");
        System.out.println("Значение общего хэшкода: " + calcHash("rybwwjpsbawqfj", 54321));
        System.out.println("Сообщение 1: " + "rybwwjpsbawqfj");
        System.out.println("Сообщение 2: " + "nrpwdihkfreqmq");
        System.out.println("Количество итераций: " + "17");

        BigInteger oKeyN = new BigInteger("889577666850907");
        BigInteger oKeyE = new BigInteger("13971");
        String cipherText = "403013074606912545180648978557219641194372024501606729868202878976557455422";

        BigInteger sKey = calcSKeyRSA(oKeyN, oKeyE);
        List<Character> message = parseMessageRSA(decryptRSA(parseCipherTextRSA(cipherText, oKeyN), sKey, oKeyN));

        System.out.println("\nRSA");
        System.out.print("Расшифрованное сообщение: ");
        message.forEach(System.out::print);

    }


    private static List<Character> parseMessageRSA(List<BigInteger> message)
    {
        List<Character> symbolsASCII = new LinkedList<>();

        for (BigInteger mes : message)
        {
            int startIndex = 0;

            while(true)
            {
                int mesLength = mes.toString().length();

                if ( mesLength < startIndex + 2)
                {
                    if (mesLength == startIndex)
                        break;

                    symbolsASCII.add((char) Integer.parseInt(mes.toString().substring(startIndex, mesLength)));
                    break;
                }
                else
                {
                    symbolsASCII.add((char) Integer.parseInt(mes.toString().substring(startIndex, startIndex + 2)));
                    startIndex += 2;
                }
            }
        }

        return symbolsASCII;
    }
}
