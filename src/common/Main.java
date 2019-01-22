package common;

import tasks.Day2;
import tasks.Day4;

public class Main
{
    static Class target = Day4.class;

    public static void main(String[] args)
    {
        long startTime = System.currentTimeMillis();

        try {
            target.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;

        System.err.println("\nExecution time: " + elapsedTime + "ms");
    }

}
