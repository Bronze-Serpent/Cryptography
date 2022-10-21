package utils;


class BlockCipherFunc
{

    public static long cyclicRightShiftLong(long value, int n)
    {
        return (value >>> n) | (value << 64 - n);
    }

    public static long cyclicLeftShiftLong(long value, int n)
    {
        return (value << n) | (value >>> 64 - n);
    }

    public static short cyclicRightShiftShort(short value, int n) { return (short) ((value >>> n) | (value << 16 - n )); }

    public static short cyclicLeftShiftShort(short value, int n) { return (short) ((value << n) | (value >>> 16 - n )); }
}
