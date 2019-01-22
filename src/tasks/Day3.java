package tasks;

import common.Utils;
import java.util.ArrayList;

public class Day3
{
    // Simple class to represent a rectangle
    class Rect
    {
        int x,y,width,height;

        Rect(int x, int y, int width, int height)
        {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public void print()
        {
            System.out.println("X:" + x + ", Y:" + y + ", Width:" + width + ", Height:" + height);
        }
    }

    ArrayList<String> input;

    public Day3()
    {
        input = Utils.readFile("res/day3.txt");

        part1();
    }

    private void part1()
    {
        ArrayList<Rect> rects = new ArrayList<>();

        // For each retangle
        for (String line : input)
        {
            // Do some horrible things to turn them into...
            String cleanedUp = line.split(" @ ")[1];
            cleanedUp = cleanedUp.replace(":","");

            String xy = cleanedUp.split(" ")[0];
            String widthHeight = cleanedUp.split(" ")[1];

            int x = Integer.parseInt(xy.split(",")[0]);
            int y = Integer.parseInt(xy.split(",")[1]);
            int width = Integer.parseInt(widthHeight.split("x")[0]);
            int height = Integer.parseInt(widthHeight.split("x")[1]);

            // Rectangles!
            Rect rect = new Rect(x,y,width,height);
            rects.add(rect);
        }

        // Matches for a single x,y point
        int numMatches;
        // Number of times a single x,y point matched more than once
        int masterCount = 0;

        int largestX = 0;
        int largestY = 0;

        for (Rect rect : rects)
        {
            if (rect.x + rect.width > largestX) {
                largestX = rect.x + rect.width;
            }

            if (rect.y + rect.height > largestY) {
                largestY = rect.y + rect.height;
            }
        }

        System.out.println("Map size: " + largestX + " x " + largestY);

        for (int x=largestX; x>=0; x--)
        {
            for (int y=largestY; y>=0; y--)
            {
                numMatches = 0;

                // Loop through every rectangle
                for (Rect rect : rects)
                {
                    // And record if it falls within the point
                    if ((x >= rect.x && x <= rect.x + rect.width) && (y >= rect.y && y <= rect.y + rect.height))
                    {
                        numMatches++;

                        System.out.println("Rectangle {X:" + rect.x + ", Y:" + rect.y + ", W:"
                                + rect.width + ", H:" + rect.height +
                                "} contains {X:" + x + ", Y:" + y + "} (" + numMatches + ")");
                    }
                }

                // Record for any matches which overlap two rectangles
                if (numMatches >= 2)
                {
                    masterCount++;
                }
            }
        }

        System.out.println("Total count: " + masterCount);
    }
}
