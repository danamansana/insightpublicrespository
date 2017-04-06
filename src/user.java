package com.company;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.company.data;

/**
 * Created by danielappel on 4/2/17.
 */
import java.util.Calendar;
public class user extends data {
    public Calendar[] times;
    public Calendar blockeduntil;

    public user(String name, String address, int usage) {
        super(name, address, usage);
        times = new Calendar[3];
        blockeduntil = Calendar.getInstance();
        blockeduntil.set(1000, 1, 1);

    }

    public boolean needsblock(String nexttime) {
        Calendar nt = cal(nexttime);
        if (nt.before(blockeduntil)) {
            return true;
        }
        if (times[2] == null) {
            return false;
        }
        Calendar check = increment(times[0], Calendar.SECOND, 20);
        return nt.before(check);


    }

    public void clearblocks(String time) {
        Calendar t = cal(time);
        if (!t.before(blockeduntil)) {
            times[0] = null;
            times[1] = null;
            times[2] = null;
        }
    }

    public void addtime(String newtime) {
        Calendar nt = cal(newtime);
        if (times[0] == null) {
            times[0] = nt;
        } else if (times[1] == null) {
            times[1] = nt;
        } else if (times[2] == null) {
            times[2] = nt;
        } else {
            if (nt.before(increment(times[0], Calendar.SECOND, 20)) && !nt.before(blockeduntil)) {
                blockeduntil = increment(times[0], Calendar.MINUTE, 5);

            }
            times[0] = times[1];
            times[1] = times[2];
            times[2] = nt;
        }
    }

    public Calendar increment(Calendar c, int field, int amount) {
        Calendar inc = Calendar.getInstance();
        inc.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE),
                c.get(Calendar.HOUR), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
        inc.add(field, amount);
        return inc;

    }

    public Calendar cal(String s) {
        Calendar c = Calendar.getInstance();
        Pattern p = Pattern.compile("(.*?)/(.*?)/(.*?):(.*?):(.*?):(.*?)\\s(.*?)?");
        Matcher m = p.matcher(s);
        m.find();
        int day = Integer.parseInt(m.group(1));
        int month = monthnumber(m.group(2));
        int year = Integer.parseInt(m.group(3));
        int hour = Integer.parseInt(m.group(4));
        int min = Integer.parseInt(m.group(5));
        int sec = Integer.parseInt(m.group(6));
        c.set(year, month, day, hour, min, sec);
        return c;
    }

    public Integer monthnumber(String m) {
        if (m.substring(0, 2).toLowerCase().equals("ja")) {
            return 1;
        }
        if (m.substring(0, 1).toLowerCase().equals("f")) {
            return 2;
        }
        if (m.substring(0, 3).toLowerCase().equals("mar")) {
            return 3;
        }
        if (m.substring(0, 2).toLowerCase().equals("ap")) {
            return 4;
        }
        if (m.substring(0, 3).toLowerCase().equals("may")) {
            return 5;
        }
        if (m.substring(0, 3).toLowerCase().equals("jun")) {
            return 6;
        }
        if (m.substring(0, 3).toLowerCase().equals("jul")) {
            return 7;
        }
        if (m.substring(0, 2).toLowerCase().equals("au")) {
            return 8;
        }
        if (m.substring(0, 1).toLowerCase().equals("s")) {
            return 9;
        }
        if (m.substring(0, 1).toLowerCase().equals("o")) {
            return 10;
        }
        if (m.substring(0, 1).toLowerCase().equals("n")) {
            return 11;
        }
        return 12;
    }
}
