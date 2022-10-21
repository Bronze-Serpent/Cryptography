package containers;

import utils.Algorithm;
import utils.Purpose;


public class ArgsCont
{

    private final String inputFile;

    private final String outputFile;

    private final Purpose purpose;

    private final Algorithm algorithm;


    public ArgsCont(String inputFile, String outputFile, Purpose purpose, Algorithm algorithm)
    {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.purpose = purpose;
        this.algorithm = algorithm;
    }

    public String getInputFile() { return inputFile; }

    public String getOutputFile() { return outputFile; }

    public Purpose getWorkingMode() { return purpose; }

    public Algorithm getAlgorithm() {return algorithm;}
}