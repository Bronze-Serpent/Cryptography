package utils;

import containers.OptionsCont;

import java.io.*;


public class Data
{

    private static final String OPTION_POINTER = "-";
    private static final String OPTION_DELIMITER = ":\\s*";


    private Data()
    {
        throw new AssertionError("This class is not meant to be instantiated");
    }


    /**
     * Reads options from a file.
     * Option requirements:
     * 1.Option name must start with OPTION_POINTER
     * 2.The option value must be separated from the name using OPTION_DELIMITER
     * 3.Each option must have only one value
     *
     * @param fileName the name of the file from which the options will be read
     * @return container containing read options
     * @throws IOException if no file with that name was found
     * @throws RuntimeException if option has more than 1 value
     */
    public static OptionsCont readOptionsFromFile(String fileName) throws IOException
    {
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName)))
        {
            OptionsCont optionsCont = new OptionsCont();
            String line;

            while ((line = reader.readLine()) != null)
            {
                line = line.trim();

                if (line.startsWith(OPTION_POINTER))
                {
                    String[] lineAsArr = line.split(OPTION_DELIMITER);
                    if (lineAsArr.length == 1)
                        optionsCont.addOption(lineAsArr[0].substring(1), null);
                    else
                        if (lineAsArr.length == 2)
                            optionsCont.addOption(lineAsArr[0].substring(1), lineAsArr[1]);
                        else
                            throw new RuntimeException("Error reading parameter" + lineAsArr[0] +
                                    "a parameter can have only 1 value");
                }
            }

            return optionsCont;
        }
    }


    /**
     * Reads a file byte by byte into an array of byte
     *
     * @param fileName the name of the file from which the bytes will be read
     * @return array of bytes read from the file
     * @throws IOException if no file with that name was found
     *
     */
    public static byte[] readBitByteByteFromFile(String fileName) throws IOException
    {
        byte[] fileByBytesArr;

        try(FileInputStream inputStream = new FileInputStream(fileName))
        {
            fileByBytesArr = new byte[inputStream.available()];
            int bufferSize = 64_000;

            if (inputStream.available() < bufferSize)
                bufferSize = inputStream.available();
            inputStream.read(fileByBytesArr, 0, bufferSize);
        }
        return fileByBytesArr;
    }


    /**
     * Writes the given byte array in the file
     *
     * @param byteArr array of bytes to be written to the file
     * @param fileName the name of the file where the bytes will be written to
     * @throws IOException if no file with that name was found
     *
     */
    public static void writeByteToFile(byte[] byteArr, String fileName) throws IOException
    {
        File outputFile = new File(fileName);
        try (FileOutputStream outputStream = new FileOutputStream(outputFile))
        {
            outputStream.write(byteArr);
        }
    }

}
