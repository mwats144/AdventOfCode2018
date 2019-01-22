package common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Stack;

public class Utils
{
    public static ArrayList<String> readFile(String filePath)
    {
        ArrayList<String> input = new ArrayList<>();

        try
        {
            BufferedReader bf = new BufferedReader(new FileReader(filePath));
            String line = bf.readLine();
            while (line != null)
            {
                input.add(line);
                line = bf.readLine();
            }

            return input;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }
}
