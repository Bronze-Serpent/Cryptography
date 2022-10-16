

public class InputArgs
{

    private final String inputFile;
    private final String outputFile;
    private final String workingMode;
    private final long key;
    private final long initVec;


    public InputArgs(String inputFile, String outputFile, long key, String workingMode, long initVec)
    {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.key = key;
        this.workingMode = workingMode;
        this.initVec = initVec;
    }

    public long getInitVec() {return initVec;}

    public long getKey() { return key; }

    public String getInputFile() { return inputFile; }

    public String getOutputFile() { return outputFile; }

    public String getWorkingMode() { return workingMode; }

}