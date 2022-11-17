package utils;


class Reformer
{

    private Reformer()
    {
        throw new AssertionError("This class is not meant to be instantiated");
    }


    /**
     * Creates a new array equivalent to the original, but without elements at the end equal to 0
     *
     * @param message an array from which elements equal to 0 will be removed from the end
     * @return an array equivalent to the original, but without elements at the end equal to 0
     */
    public static byte[] removeEmptyByteFromTheEnd(byte[] message)
    {
        int emptyByteCounter = 0;

        for (int i = message.length - 1; i > -1; i--)
        {
            if (message[i] == 0)
                emptyByteCounter++;
            else
                break;
        }

        byte[] messageWithoutAdded = new byte[message.length - emptyByteCounter];
        System.arraycopy(message, 0, messageWithoutAdded, 0, message.length - emptyByteCounter);

        return messageWithoutAdded;
    }
}
