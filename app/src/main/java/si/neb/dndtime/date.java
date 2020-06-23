package si.neb.dndtime;

import java.text.DecimalFormat;

/**
 * Created by Ben on 10 Jun.
 */

public class date {
    public final int daysInYear = 365;
    public final String[] monthNames = {
      "Frostthaw","Sunrise","Feast of Pelor","Rainfall","Planting","Feast of Avandra","High Sun","Summertide","Feast of Midsummer","Sunset","Harvesttide","Feast of Sehanine","Frosttide","Feast of Midwinter"
    };
    public final int[] monthLengths = {40,40,1,40,40,1,40,40,1,40,40,1,40,1};
    public final int[] equinoxes = {79,172,266,355};

    public final int[] colors = {0xff08002d,0xffff6100,0xff00c4ff,0xffa63392,0xff08002d};



    long days;
    int minutes;
    public date (long days)
    {
        this.days = days;
        this.minutes = 0;
    }
    public date (int year, int month, int day)
    {
        if (day<1||month<1||month>monthLengths.length||day>monthLengths[month-1]){throw new IllegalArgumentException("The input date is invalid.");}
        this.days = 365*(year-1);
        for (int i = 0; i<month-1;i++)
        {
            this.days += monthLengths[i];
        }
        this.days += day;
        this.minutes = 0;
    }
    public date (int year, int month, int day, int hour, int minute)
    {
        if (day<1||month<1||month>monthLengths.length||day>monthLengths[month-1]||minute>=60||hour>=24){throw new IllegalArgumentException("The input date is invalid.");}
        this.days = 365*(year-1);
        for (int i = 0; i<month-1;i++)
        {
            this.days += monthLengths[i];
        }
        this.days += day;
        this.minutes = minute;
        this.minutes+=(60*hour);
    }
    public void addDays(int d)
    {
        this.days += d;
    }
    public void addMinutes(int m)
    {
        days += m/1440;
        minutes+=m%1440;
        if (minutes >=1440)
        {
            days++;
            minutes -= 1440;
        }
    }

    public int getHours()
    {
        return minutes/60;
    }
    public int getMinutes()
    {
        return minutes%60;
    }
    public int getDay() {
        int day = getDayOfYear();
        int i = 0;
        while (day>monthLengths[i])
        {
            day-=monthLengths[i];
            i++;
        }
        return day;
    }
    public int getMonth()
    {
        int day = getDayOfYear();
        int i = 0;
        while (day>monthLengths[i])
        {
            day-=monthLengths[i];
            i++;
        }
        return i+1;
    }
    public int getDayOfYear()
    {
        return (int)(1+((days-1)%365));
    }
    public int getYear()
    {
        return (int)(1+((days-1)/365));
    }
    public String toString()
    {

        String minuteString = new DecimalFormat("00").format(getMinutes());
        String hourString = "";
        String post = "";
        if (getHours() == 0) {
            hourString = "12";
            post = "AM";
        }
        else if (getHours() < 12)
        {
            hourString = Integer.toString(getHours());
            post = "AM";
        }
        else if (getHours() == 12)
        {
            hourString = "12";
            post = "PM";
        }
        else
        {
            hourString = ((Integer)(getHours()-12)).toString();
            post = "PM";
        }


        String timeLine = hourString+":"+minuteString+" "+post;
        String dateLine;

        if (monthLengths[getMonth()-1]==1)
        {
            dateLine = monthNames[getMonth()-1] + ", " + getYear();
        }
        else
        {
           dateLine = getDay() + " " + monthNames[getMonth()-1] + ", " + getYear();
        }
        return timeLine + "\n" + dateLine;
    }

    public int getColor()
    {
        int base = (int)colorDayDecimal();
        if (base==4){base--;}
        return blendColors(colors[base],colors[base+1],colorDayDecimal()-base);
    }

    public int getSunriseTime()
    {
        return 840-getSunriseOffset();
    }
    public int getSunsetTime()
    {
        return 840+getSunriseOffset();
    }
    public double colorDayDecimal()
    {
        if (minutes < getSunriseTime()-120)
        {
            return 0;
        }
        else if (minutes <= getSunriseTime())
        {
            return  ((minutes - getSunriseTime() + 120) / 120.0);
        }
        else if (minutes <= getSunriseTime()+120)
        {
            return 1+((minutes-getSunriseTime())/120.0);
        }
        else if (minutes < getSunsetTime()-120)
        {
            return 2;
        }
        else if (minutes <= getSunsetTime())
        {
            return 2+((minutes - getSunsetTime()+120)/120.0);
        }
        else if (minutes <= getSunsetTime()+120)
        {
            return 3+((minutes-getSunsetTime())/120.0);
        }
        else
        {
            return 0;
        }
    }

    public int getSunriseOffset()
    {
        int daysToSummer;
        if (getDayOfYear()>=equinoxes[1]&&getDayOfYear()<=equinoxes[3])
        {
            daysToSummer = getDayOfYear()-equinoxes[1];
        }
        else if (getDayOfYear()>equinoxes[3])
        {
            daysToSummer = (365-getDayOfYear())+equinoxes[1];
        }
        else
        {
            daysToSummer = equinoxes[1]-getDayOfYear();
        }
        return 450-(int)(180*(daysToSummer/187.0));
    }

    public int blendColors(int starting, int ending, double blend)
    {
        starting -= 0xff000000;
        ending -= 0xff000000;
        int sb = starting%256;
        int sg = ((starting-sb)/256)%256;
        int sr = ((starting-sb)/(256*256))-(sg/256);

        int eb = ending%256;
        int eg = ((ending-eb)/256)%256;
        int er = ((ending-eb)/(256*256))-(eg/256);

        int newb = sb + (int)(blend*(eb-sb));
        int newg = sg + (int)(blend*(eg-sg));
        int newr = sr + (int)(blend*(er-sr));
        return 0xFF000000+(256*256*newr)+(256*newg)+newb;
    }

}
//5628 Sunset 24, 20:00

//equinoxes:
//
//
// 39 Sunrise Vernal
// 31 Frostide Winter
// 23 Sunset Autumnal
// 10 High Sun

//midnight 08002d
//sunrise ff6100
//noon 00c4ff
