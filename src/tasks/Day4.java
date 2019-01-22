package tasks;

import common.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Day4
{
    ArrayList<String> input;
    ArrayList<LogEntry> entries = new ArrayList<>();
    HashMap<Integer,Guard> guardMap = new HashMap<>();

    // Class to store an entry, consisting of a timestamp and a message
    class LogEntry implements Comparable<LogEntry>
    {
        private Date date;
        private String details;

        public LogEntry(Date date, String details)
        {
            this.date = date;
            this.details = details;
        }

        public String getMessage()
        {
            return details;
        }

        public Date getDate(){
            return date;
        }

        // Allows us to sort the log
        @Override
        public int compareTo(LogEntry entry){
            return date.compareTo(entry.getDate());
        }
    }

    class Guard
    {
        // Stores the time slept (value) for each minute (key)
        HashMap<Integer,Integer> minuteToSleepMap = new HashMap<>();

        int sleptMinutes = 0;
        int id;

        public Guard(int id) {
            this.id = id;
        }

        public int getTotalSleptMinutes()
        {
            return sleptMinutes;
        }

        public int getMostSleptMinute()
        {
            return Collections.max(minuteToSleepMap.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
        }

        public void addSleptMinutes(int startMinute, int endMinute, int numMinutesSlept)
        {
            for (int i=startMinute; i<endMinute; i++)
            {
                minuteToSleepMap.put(i, minuteToSleepMap.containsKey(i)? minuteToSleepMap.get(i) + numMinutesSlept : numMinutesSlept);
            }
            sleptMinutes += numMinutesSlept;
        }

        public int getId()
        {
            return id;
        }

        public HashMap<Integer,Integer> getSleepMap()
        {
            return minuteToSleepMap;
        }
    }

    // Returns the guard ID from a log message
    private int idFromGuardString(String line)
    {
        if (!line.contains("Guard")) {
            throw new IllegalArgumentException();
        }

        String idStr = line.replace("Guard #", "")
          .replace(" begins shift", "")
            .replace(" ends shift", "");

        int id = Integer.parseInt(idStr);

        return id;
    }

    public Day4() throws Exception
    {
        input = Utils.readFile("res/day4.txt");

        // Create log entries
        for (String line : input)
        {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dateStr = line.substring(1,17);
            Date date = format.parse(dateStr);

            entries.add(new LogEntry(date, line.substring(19)));

            if (line.contains("Guard"))
            {
                int id = idFromGuardString(line.substring(19));
                guardMap.put(id, new Guard(id));
            }
        }

        // Sort the log chronologically
        Collections.sort(entries);

        // Store the current guard id the log is referencing
        Guard currentGuard = null;
        // And the last minute slept
        int sleepingMinute = 0;

        for (LogEntry entry : entries)
        {
            // GUARD ID
            if (entry.getMessage().contains("Guard"))
            {
                currentGuard = guardMap.get(idFromGuardString(entry.getMessage()));
            }

            // FALLING ASLEEP
            else if (entry.getMessage().contains("falls asleep"))
            {
                sleepingMinute = entry.getDate().getMinutes();
            }

            // WAKING UP
            else if (entry.getMessage().contains("wakes up"))
            {
                currentGuard.addSleptMinutes(sleepingMinute, entry.getDate().getMinutes(), entry.getDate().getMinutes());
            }
        }

        HashMap<Guard, Integer> mostSleptMinuteMap = new HashMap<>();

        // Work out our sleepiest guard
        Guard sleepiestGuard = null;
        for (Map.Entry<Integer, Guard> map : guardMap.entrySet())
        {
            if (sleepiestGuard == null)
            {
                sleepiestGuard = map.getValue();
            }
            else
            {
                if (map.getValue().getTotalSleptMinutes() > sleepiestGuard.getTotalSleptMinutes())
                {
                    sleepiestGuard = map.getValue();
                }
            }
        }

        // And print the result
        System.out.println("Result: " + sleepiestGuard.id * sleepiestGuard.getMostSleptMinute());

        /**
         * PART 2
         */

        // A map of each minute, containing a list of guards and how many times they slept on that minute
        HashMap<Integer, HashMap<Guard, Integer>> sleepMap = new HashMap<>();

        for (int i=0; i<59; i++)
        {
            sleepMap.put(i, new HashMap<>());

            // Find the guard who slept most on this minute, and the number of times they slept
            for (Map.Entry<Integer, Guard> map : guardMap.entrySet())
            {
                HashMap currMap = sleepMap.get(i);
                currMap.put(map.getValue(), map.getValue().getSleepMap().get(i) == null ? 0 : map.getValue().getSleepMap().get(i));
            }
        }

        // For each minute, let's work out the guard that slept most on that minute, and how much they slept
        for (Map.Entry<Integer, HashMap<Guard, Integer>> map : sleepMap.entrySet())
        {
            int minute = map.getKey();
            HashMap<Guard,Integer> guardMap = map.getValue();

            int biggestSleep = 0;
            int guardId = -1;

            for (Map.Entry<Guard, Integer> map2 : guardMap.entrySet())
            {
                if (map2.getValue() != null && map2.getValue() > biggestSleep) {
                    guardId = map2.getKey().getId();
                    biggestSleep = map2.getValue();
                }
            }

            // And print out the result
            System.out.println("Minute: " + minute + " -> " + guardId + " " + biggestSleep);

            // Eyeballing the result, bit cheaty... (748 * 33)
        }

    }
}
