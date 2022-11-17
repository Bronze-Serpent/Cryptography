package utils;


import java.util.concurrent.ThreadLocalRandom;

class RandomMessage
{
    /**
     * Generates a message of size mesSize, consisting of characters from alphabet
     *
     * @param alphabet characters that make up the generated message
     * @param mesSize generated message size
     * @return random message
     */
    public static String getMessage(char[] alphabet, int mesSize)
    {
        char[] message = new char[mesSize];

        for (int i = 0; i < mesSize; i++)
            message[i] = alphabet[ThreadLocalRandom.current().nextInt(alphabet.length)];

        return new String(message);
    }
}
