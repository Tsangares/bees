package attributes;

import javafx.beans.property.ReadOnlyLongWrapper;

/**
 * Created by William on Dec-12-2014.
 */
/*time: 13:01*/
public class TimeManager {
    public final static int secondRate = 1;
    public static String longMonth = "longMonth";
    public static String shortMonth = "shortMonth";
    public ReadOnlyLongWrapper year = new ReadOnlyLongWrapper(0);
    public ReadOnlyLongWrapper month = new ReadOnlyLongWrapper(0);
    public ReadOnlyLongWrapper day = new ReadOnlyLongWrapper(0);
    public ReadOnlyLongWrapper hour = new ReadOnlyLongWrapper(0);
    public ReadOnlyLongWrapper min = new ReadOnlyLongWrapper(0);
    public ReadOnlyLongWrapper second = new ReadOnlyLongWrapper(0);
    public TimeManager(){
    }
    public TimeManager(long input){
        update(input);
    }
    public void update(long frame){
        second.set(frame/ secondRate);
        min.set(second.get()/60);
        hour.set(min.get()/60);
        day.set(hour.get()/24);
        month.set(day.get()/30 + 4);
        year.set(month.get()/12 + 1926);
    }
    public String findTime(){
        return  String.format("%02d",hour.get()%12) + ":" + String.format("%02d",min.get()%60) + (hour.get()%24>12?" pm":" am");
    }
    public static String findMonth(long number){
        switch ((int)(number)) {
            case 0:
                return "Jan";
            case 1:
                return "Feb";
            case 2:
                return "Mar";
            case 3:
                return "Apr";
            case 4:
                return "May";
            case 5:
                return "Jun";
            case 6:
                return "Jul";
            case 7:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Nov";
            case 11:
                return "Dec";
        }
        return "Month";
    }
}
