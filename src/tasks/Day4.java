package tasks;

import common.Utils;
import sun.rmi.runtime.Log;

import java.text.DateFormat;
import java.text.ParseException;
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

    public Day4() throws ParseException, InterruptedException
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
    }
}
