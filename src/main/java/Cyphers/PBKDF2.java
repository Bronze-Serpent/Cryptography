package Cyphers;

import java.util.Arrays;
import java.util.function.ToLongBiFunction;

public class PBKDF2 implements KeyDiversifier
{
    /**
     * Number of rands (passes) of encryption
     */
    private final int numOfIter;


    public PBKDF2(int numOfIter)
    {
        this.numOfIter = numOfIter;
    }


    @Override
    public long[] diversify(ToLongBiFunction<String, Long> hashFunk, String password, long salt, int dkLen)
    {
        int numOfBlocks = (int) Math.ceil(dkLen / 64.0);
        int lastBlockLen = dkLen - (numOfBlocks - 1) * 64;

        long[] blocks = new long[numOfBlocks];

        for (int num = 0; num < numOfBlocks; num++)
            blocks[num] = createBlock(hashFunk, password, salt ^ num, numOfIter);

        blocks[numOfBlocks - 1] = blocks[numOfBlocks - 1] << (64 - lastBlockLen) >>> (64 - lastBlockLen);

        return  blocks;
    }


    private static long createBlock(ToLongBiFunction<String, Long> hashFunk, String password, long salt, int numOfIter)
    {
        long[] parentBlocs = new long[numOfIter];

        for (int num = 0; num < numOfIter; num++)
        {
            if (num == 0)
                parentBlocs[num] = hashFunk.applyAsLong(password, salt);

            else
                parentBlocs[num] = hashFunk.applyAsLong(password, parentBlocs[num - 1]);
        }

        return Arrays.stream(parentBlocs)
                .reduce(0, (a, b) -> a ^ b);
    }

}
