package tasks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Day1
{
    int outcome = 0;
    Set<Integer> counts = new HashSet<>();
    Stack<String> input;

    public Day1()
    {
        input = readFile("res/day1.txt");

        part1();
        part2();
    }

    private void part1()
    {
        // For each line
        while (! input.isEmpty())
        {
            String element = input.pop();

            // Strip the sign off
            String sign = element.substring(0,1);
            String value = element.substring(1);

            // Add or remove the value depending on the sign
            outcome += sign.equals("+") ? Integer.parseInt(value) : -Integer.parseInt(value);
        }

        System.out.println(outcome);
    }

    public void part2()
    {
        int idx = 0;
        // Keep cycling through the input
        while (true)
        {
            String element = input.get(idx);
            String sign = element.substring(0,1);
            String value = element.substring(1);

            // Add or remove the value depending on the sign
            outcome += sign.equals("+") ? Integer.parseInt(value) : -Integer.parseInt(value);

            // If the duplicate is found
            if (counts.contains(outcome))
            {
                System.err.println(outcome);
                break;
            }

            counts.add(outcome);

            // Cycles the stack when it reaches the end
            idx++;
            if (idx > input.size()-1)
            {
                idx = 0;
            }
        }
    }

    public static Stack<String> readFile(String filePath)
    {
        Stack<String> input = new Stack<>();

        try
        {
            BufferedReader bf = new BufferedReader(new FileReader(filePath));
            String line = bf.readLine();
            while (line != null)
            {
                input.push(line);
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
