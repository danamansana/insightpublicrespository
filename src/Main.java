package com.company;
import org.omg.PortableInterceptor.ACTIVE;

import java.io.*;
import java.util.HashMap;
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
        //copy("log_output/resources.txt", "insight_testsuite/tests/test_features/log_output/resources.txt");
        //copy("log_output/resources.txt", "insight_testsuite/your-own-test/log_output/resources.txt");


        FileWriter hoursresults = new FileWriter("log_output/hours.txt");
        i = 0;
        while(i<topHourList.bin.length){
            String line = topHourList.bin[i].publish()+"\n";
            hoursresults.write(line);
            i = i+1;
        }
        hoursresults.close();
        //copy("log_output/hours.txt", "insight_testsuite/tests/test_features/log_output/hours.txt");
        //copy("log_output/hours.txt", "insight_testsuite/your-own-test/log_output/hours.txt");

        FileWriter userresults = new FileWriter("log_output/hosts.txt");
        i= 0;
        while(!sortusers.nextleaf.equals("1")&&i<10){
            String line = sortusers.top().publish()+"\n";
            userresults.write(line);
            i = i+1;
        }
        userresults.close();
        //copy("log_output/hosts.txt", "insight_testsuite/tests/test_features/log_output/hosts.txt");
        //copy("log_output/hosts.txt", "insight_testsuite/your-own-test/log_output/hosts.txt");

        FileWriter blocked = new FileWriter("log_output/blocked.txt");
        i= 0;
        while(i<blocklog.size()){
            blocked.write(blocklog.get(i)+"\n");
            i = i+1;
        }
        blocked.close();
        //copy("log_output/blocked.txt", "insight_testsuite/tests/test_features/log_output/blocked.txt");
        //copy("log_output/blocked.txt", "insight_testsuite/your-own-test/log_output/blocked.txt");


    }

    public void readlog() throws IOException{
        FileReader read = null;
        try{
            read = new FileReader("log_input/log.txt");
            BufferedReader buffread  = new BufferedReader(read);
            int n  = 0;
            String line;

            while ((line = buffread.readLine()) != null){

                parser p = new parser(line);

                incorporateline(p);



                //System.out.println(n);
                //System.out.println(p.time);
                n = n+1;
            }
            if (buffread.readLine() == null){ fin = true;}
            ActiveHours.clearconv();
            topHourList = ActiveHours.tops;

        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
        }

        //copy("log_input/log.txt", "insight_testsuite/temp/log_input/log.txt");
        //copy("log_input/log.txt", "insight_testsuite/your-own-test/log_input/log.txt");


    }
    public void incorporateline(parser nextline){

        data c = nextline.commanddatum();
        data u = nextline.userdatum();
        updateblocks(possibleblocks,nextline);

        incorporatedata(c,commands, sortcommands);
        incorporatedata(u, users, sortusers);
        updateHours(nextline);

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

  public void copy(String from, String to) throws IOException {
      FileReader reader = new FileReader(from);
      BufferedReader buffread = new BufferedReader(reader);
      FileWriter writer = new FileWriter(to);
      String line;
      while ((line = buffread.readLine()) != null) {
          writer.write(line+"\n");
      }
      reader.close();
      buffread.close();
      writer.close();

      }





    public static void main(String[] args) throws IOException {
        Main m  = new Main();
        m.readlog();

        m.reportresults();

    }
}
