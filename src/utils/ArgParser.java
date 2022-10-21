package utils;

import containers.*;

import static utils.Algorithm.FEISTEL_CHAIN;


public class ArgParser
{

    private ArgParser() {throw new AssertionError("This class is not meant to be instantiated");}


    public static ArgsCont parseCmdArgs(String[] args)
    {
        if (args.length != 4)
        {
            String argsAsLine = "";
            for (String arg : args)
                argsAsLine += arg;

            throw new RuntimeException("The program cannot be run with these arguments: " + argsAsLine +
                    " Required arguments: input_file output_file working_mode algorithm");
        }

        if (!(args[2].equals("encrypt") || args[2].equals("decrypt")))
            throw new RuntimeException("The program cannot be run with this working mode: " + args[3] +
                    ". Valid operating modes: \"encrypt\" and \"decrypt\"");

        try
        {
            Algorithm algm = Algorithm.valueOf(args[3].toUpperCase());

            try
            {
                Purpose wm = Purpose.valueOf(args[2].toUpperCase());

                return new ArgsCont(args[0], args[1], wm, algm);
            }
            catch (IllegalArgumentException e)
            {
                throw new RuntimeException("The program cannot be run with this working mode: " + args[3] +
                        ". Valid operating modes: \"encrypt\" and \"decrypt\"");
            }
        }
        catch (IllegalArgumentException e)
        {
            throw new RuntimeException("The program cannot be run with this algorithm: " + args[3] +
                    ". Valid algorithms: \"Feistel_chain\", \"CBC\", \"Hash\"");
        }
    }


    public static void checkOptionsForAlg(OptionsCont optionsCont, Algorithm alg)
    {
        checkKey(optionsCont);

        if (alg != FEISTEL_CHAIN)
            checkInitVec(optionsCont);

        if (optionsCont.contains("rQ"))
            checkRQ(optionsCont);
    }


    private static void checkKey(OptionsCont optionsCont)
    {
        try { Long.parseLong(optionsCont.get("key")); }
        catch (NumberFormatException e)
        {
            throw new RuntimeException("The program cannot be run with key: " + optionsCont.get("key") +
                    ". Key must be a number valid for long");
        }
    }


    private static void checkInitVec(OptionsCont optionsCont)
    {
        try { Long.parseLong(optionsCont.get("initVec")); }
        catch (NumberFormatException e)
        {
            throw new RuntimeException("The program cannot be run with initVec: " + optionsCont.get("initVec") +
                    ". initVec must be a number valid for long");
        }
    }


    private static void checkRQ(OptionsCont optionsCont)
    {
        try
        {
            int rQ = Integer.parseInt(optionsCont.get("rQ"));
            if (rQ < 0)
                throw new RuntimeException("The program cannot be run with rQ: " + optionsCont.get("rQ") +
                        ". rQ must be a positive number valid for int");
        }
        catch (NumberFormatException e)
        {
            throw new RuntimeException("The program cannot be run with rQ: " + optionsCont.get("rQ") +
                    ". rQ must be a positive number valid for int");
        }
    }
}
