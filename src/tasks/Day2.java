package tasks;

import common.Utils;

import java.util.*;

public class Day2
{
    // Contains all strings from our input
    ArrayList<String> input;
    // The number of times two letters have been seen in total
    int twoCount = 0;
    // The number of times three letters have been seen in total
    int threeCount = 0;

    public Day2()
    {
        input = Utils.readFile("res/day2.txt");

        //part1();
        part2();
    }

    public void part2()
    {
        // For each box id
        for (String line : input)
        {
            char[] lineArr = line.toCharArray();

            // Loop through every other box id
            for (String comparingLine : input)
            {
                // Don't compare the same box Id
                if (line.equals(comparingLine)){
                    continue;
                }

                char[] comparingLineArr = comparingLine.toCharArray();

                int diffCount = 0;
                char differingChar = ' ';

                // Compare each char line by line
                for (int i=0; i<comparingLineArr.length; i++) {
                    if (comparingLineArr[i] != lineArr[i]){
                        diffCount++;
                        // Keep a rolling update of the latest differing char (will only be used for the match)
                        differingChar = comparingLineArr[i];
                    }
                }

                // Display the matching ID with the char to be replaced
                if (diffCount == 1) {
                    System.out.println(line + " (" + differingChar + ")");
                }
            }
        }
    }

    public void part1()
    {
        // For each box
        for (String line : input)
        {
            // Create a map to store the number of times a character has been seen
            HashMap<Character, Integer> seenMap = new HashMap<>();

            for (int i=0; i<line.length(); i++)
            {
                char currChar = line.charAt(i);

                // Update the map
                seenMap.put(currChar, seenMap.containsKey(currChar) ? seenMap.get(currChar)+1 : 1);
            }

            boolean twoMatch = false;
            boolean threeMatch = false;

            // For every value in the map
            for (Map.Entry<Character,Integer> m : seenMap.entrySet())
            {
                // We're only interested in values which will create our checksum
                if (m.getValue() != 2 && m.getValue() != 3) continue;

                // Update the counts accordingly
                if (!twoMatch && m.getValue() == 2) {
                    twoCount++;
                    twoMatch = true;
                }
                if (!threeMatch && m.getValue() == 3) {
                    threeCount++;
                    threeMatch = true;
                }
            }

            System.out.println();
        }

        // And return the checksum
        System.out.println(twoCount * threeCount);
    }
}
