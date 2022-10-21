package containers;


import java.util.HashMap;
import java.util.Map;


public class OptionsCont
{

    Map<String, String> optionVal = new HashMap<>();

    public void addOption(String oName, String value) { optionVal.put(oName, value); }

    public String get(String oName) { return optionVal.get(oName); }

    public boolean contains(String oName) { return optionVal.containsKey(oName); }
}
