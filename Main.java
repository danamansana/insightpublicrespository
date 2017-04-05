package com.company;
import org.omg.PortableInterceptor.ACTIVE;

import java.io.IOException;
import java.util.HashMap;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;

public class Main {

    public HashMap<String, data> commands;
    public HashMap<String, data> times;
    public HashMap<String, data> users;
    public conveyor ActiveHours;
    public basicbin topHourList;
    public ArrayList<String> blocklog;
    public HashMap<String, blockhelper> possibleblocks;
    public dataheap sortcommands;
    public dataheap sorttimes;
    public dataheap sortusers;
    public hourheap sorthours;
    public Hour althour;
    public Calendar altcal;
    public dataheap althoursort;
    public boolean fin;
    public Main(){
        commands = new HashMap();
        times = new HashMap();
        users = new HashMap();
        blocklog = new ArrayList();
        ActiveHours = new conveyor();
        topHourList = new basicbin();
        possibleblocks = new HashMap();
        sortcommands = new dataheap();
        sorttimes = new dataheap();
        sortusers = new dataheap();
        sorthours = new hourheap();
        fin = false;
        althour = null;
        altcal = null;
        althoursort = new dataheap();

    }

    public void reportresults() throws IOException{
        FileWriter commandresults = new FileWriter("log_output/resources.txt");
        int i = 0;
        while(!sortcommands.nextleaf.equals("1") && i<10){
            String line = sortcommands.top().publish()+"\n";
            commandresults.write(line);
            i = i+1;
        }
        commandresults.close();
        /**FileWriter timesresults  = new FileWriter("log_output/hours.txt");
        i = 0;
        while(!sorthours.nextleaf.equals("1") && i<10){
            String line = sorthours.top().publish()+"\n";
            timesresults.write(line);
            i = i+1;
        }
        timesresults.close();*/
        /**FileWriter hoursresults = new FileWriter("log_output/hours.txt");
        i=0;
        dataheap hourhelper = sorthours.sortbyusage();
        while(!hourhelper.nextleaf.equals("1") && i<10){
            String line = hourhelper.top().publish()+"\n";
            hoursresults.write(line);
            i = i+1;
        }
        hoursresults.close();*/
        FileWriter hoursresults = new FileWriter("log_output/hours.txt");
        i = 0;
        while(i<topHourList.bin.length){
            String line = topHourList.bin[i].publish()+"\n";
            hoursresults.write(line);
            i = i+1;
        }
        hoursresults.close();
        /*FileWriter althoursresults = new FileWriter("log_output/althours.txt");
        i = 0;
        while(!althoursort.nextleaf.equals("1") && i<10){
            String line = althoursort.top().publish()+"\n";
            althoursresults.write(line);
            i = i+1;
        }
        althoursresults.close();*/
        FileWriter userresults = new FileWriter("log_output/hosts.txt");
        i= 0;
        while(!sortusers.nextleaf.equals("1")&&i<10){
            String line = sortusers.top().publish()+"\n";
            userresults.write(line);
            i = i+1;
        }
        userresults.close();
        FileWriter blocked = new FileWriter("log_output/blocked.txt");
        i= 0;
        while(i<blocklog.size()){
            blocked.write(blocklog.get(i)+"\n");
            i = i+1;
        }
        blocked.close();

    }

    public void readlog() throws IOException{
        FileReader read = null;
        try{
            read = new FileReader("log.txt");
            BufferedReader buffread  = new BufferedReader(read);
            int n  = 0;
            String line;
            //while(n<start && (line = buffread.readLine()) != null){
            //    n = n+1;
            //}
            while ((line = buffread.readLine()) != null){
                //line = buffread.readLine();
                parser p = new parser(line);
                //S = "";
                incorporateline(p);

                /**while(ActiveHours.during()){
                    topHourList.ins(ActiveHours.currentposition, ActiveHours.conv);
                    ActiveHours.incpos();
                }*/
                /*if(n%250000 == 0){
                    ActiveHours = new conveyor(ActiveHours.tops);
                }*/
                System.out.println(n);
                System.out.println(p.time);
                n = n+1;
            }
            if (buffread.readLine() == null){ fin = true;}
            ActiveHours.clearconv();
            topHourList = ActiveHours.tops;
            //topHours(ActiveHours.currentposition);
            //clear(ActiveHours, sorthours);
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
        }

    }
    public void incorporateline(parser nextline){
        //data t = nextline.timedatum();
        data c = nextline.commanddatum();
        data u = nextline.userdatum();
        updateblocks(possibleblocks,nextline);
        //incorporatedata(t,times, sorttimes);
        incorporatedata(c,commands, sortcommands);
        incorporatedata(u, users, sortusers);
        updateHours(nextline);
        //altupdateHours(nextline);
    }

    public void updateHours(parser nextline){
        Hour h  = new Hour(nextline);
        ActiveHours.add(h);
    }

    public void altupdateHours(parser nextline){
        Hour h = new Hour(nextline);
        if(althour == null){
            althour = h;
            altcal = h.increment(Calendar.HOUR, 1);
        }
        else{
            if(altcal.after(h.time)){
                althour.usage =+1;
            }
            else{
                System.out.println(Integer.toString(altcal.get(Calendar.HOUR))+":"+Integer.toString(altcal.get(Calendar.MINUTE))+":"+Integer.toString(altcal.get(Calendar.SECOND)));
                althoursort.insert(althour);
                althour = h;
                altcal = h.increment(Calendar.HOUR, 1);
            }
        }
    }

    public void topHours(int start){
        int i = start;
        while(i<ActiveHours.conv.size()){
            topHourList.ins(i, ActiveHours.conv);
            System.out.println(i);
            i = i+1;
        }
    }



    /**public void updateHours(parser nextline){
        blockhelper nexttime = new blockhelper(nextline);
        nexttime.blockedtill = nexttime.increment(nexttime.countfrom, Calendar.HOUR, 1);
        if(ActiveHours.size() != 0) {
            blockhelper lasttime = ActiveHours.get(ActiveHours.size() - 1);
            if (lasttime.hourname.equals(nexttime.hourname)) {
                lasttime.usage = lasttime.usage +1;
                lasttime.pings = lasttime.pings+1;
                A = A+1;
            } else {
                nexttime.usage = lasttime.usage + 1;
                A = A+1;
                while ((ActiveHours.size() != 0) && (ActiveHours.get(0).blockedtill.before(nexttime.countfrom))) {
                    S = S+ ", " + ActiveHours.get(0).blockedtill.get(Calendar.HOUR) + " is before " + nexttime.countfrom.get(Calendar.HOUR);
                    nexttime.usage = nexttime.usage - ActiveHours.get(0).pings;
                    A = A- ActiveHours.get(0).pings;
                    ActiveHours.get(0).rename();
                    sorthours.boundedinsert(ActiveHours.get(0));
                    ActiveHours.remove(0);
                }
                if(ActiveHours.size() != 0){
                    nexttime.hourbeginname = ActiveHours.get(0).hourname;}
                ActiveHours.add(nexttime);
            }
        } else{
            ActiveHours.add(nexttime);
            A = A+1;
        }

    }*/

    public void clear(ArrayList<blockhelper> from , boundeddataheap into ){
        while(from.size() != 0){
            from.get(0).rename();
            into.boundedinsert(from.get(0));
            from.remove(0);
        }
    }

    public void updateblocks(HashMap<String, blockhelper> updatelog, parser info){
        blockhelper inf = new blockhelper(info);
        if(!updatelog.containsKey(info.name) && info.response.equals("401")){
            updatelog.put(info.name, inf);
        }
        if(updatelog.containsKey(info.name) && !info.response.equals("401")){
            blockhelper compareto  = updatelog.get(info.name);
            if(!inf.countfrom.before(compareto.blockedtill)){
                updatelog.remove(info.name);
            }
        }
        if(updatelog.containsKey(info.name) && info.response.equals("401")){
            blockhelper compareto  = updatelog.get(info.name);
            if((compareto.pings+1)<3){
                compareto.pings = compareto.pings+1;
            }
            if(!((compareto.pings+1)<3)){
                compareto.pings = compareto.pings+1;
                compareto.blockedtill = compareto.increment(inf.countfrom, Calendar.MINUTE, 5);
                blocklog.add(info.originalline);
            }
        }

    }

    /**public void incorporateuser(parser nextline){
        user u = new user(nextline.name, nextline.name, 1);
        if(nextline.response.equals("401")){
            u.addtime(nextline.time);
        }
        if(users.containsKey(u.name)){
            user previousu = (user)users.get(u.name);
            if(!nextline.response.equals("401")){
                previousu.clearblocks(nextline.time);
            }
            else{
                if(previousu.needsblock(nextline.time)){
                    String toadd = nextline.originalline;
                    blocklog.add(toadd);}
                    previousu.addtime(nextline.time);
                }
            }
         incorporatedata(u, users, sortusers);
        }*/



    public void incorporatedata(data contains, HashMap<String, data> container, dataheap sortcontainer){
        if(hasdata(container, contains)){
            updatedata(container.get(contains.name), sortcontainer);
        }
        else{
            adddata(container, contains, sortcontainer);
        }
    }

    public void updatedata(data contains, dataheap sortcontainer){
        sortcontainer.updateposition(contains.address);
    }

    public void adddata(HashMap<String, data> container, data contains, dataheap sortcontainer){
        container.put(contains.name, contains);
        sortcontainer.insert(contains);
    }


    public boolean hasdata(HashMap<String, data> container, data contains){
        return container.containsKey(contains.name);
    }



    public static void main(String[] args) throws IOException {
        Main m  = new Main();
        m.readlog();
        /*int i = 0;
        while(m.fin == false){
        m.readlog(i, i+1000000);
        i = i+ 250000;
        m.ActiveHours = new conveyor(m.topHourList);
        }*/
        m.reportresults();
        //m.sortcommands.state();
        //m.sorttimes.state();

	// write your code here
    }
}
