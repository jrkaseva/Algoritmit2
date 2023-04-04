package Codes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;
/**
 * TIEA211 Algoritmit 2 -course from Jyväskylä University (K2023).
 * Author: Joakim Kaseva
 * Last modified: 29.3.2023
 */
public class PerinnonjakoCopy2{
    static final Scanner SC = new Scanner(System.in);
    public static void main(String[] args){
        final String END = "0";
        String line = "";
        StringBuilder text = new StringBuilder();
        try {
            line = SC.nextLine();
            if (line.equals(END)) System.exit(0);
            while(true){
                if (line.equals(END)){
                    inherit(text.toString());
                    text = new StringBuilder();
                    line = SC.nextLine();
                    if (line.equals(END)) break;
                    continue;
                }
                if (line.length() > 4 && line.substring(line.length() - 5).equals("0 0 0")){
                    inherit(line);
                    break;
                }
                text.append(line + "\n");
                line = SC.nextLine();
            }
        } catch (NoSuchElementException e){
            inherit(text.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Virhe syötteen lukemisessa");
        }
    }
    public static void inherit(String text){
        text = text.replace("\n", " ");
        String[] input = text.split(" ");
        int count = 0;
        boolean first_row = false;
        StringBuilder temp = new StringBuilder();
        String firstRow = "";
        ArrayList<String> people = new ArrayList<>();
        for(int i = 0; i < input.length; i++){
            if (input[i] == "") continue;
            temp.append(input[i]);
            count++;
            if(!first_row){
                if(count == 2){
                    first_row = true;
                    count = 0;
                    firstRow = temp.toString();
                    temp = new StringBuilder();
                }
            }
            if(count == 4){
                count = 0;
                people.add(temp.toString());
                temp = new StringBuilder();
            }
            else temp.append(" ");

        }
        String[] temp2 = firstRow.split(" ");
        String id_dead = "-" + temp2[0];
        int inheritance = Integer.parseInt(temp2[1]);
        HashMap<String, Integer> stash = new HashMap<>();
        if (!hasLiveHeir(people, id_dead)){
            System.out.println("Valtio saa " + inheritance + "\nJakamatta jää 0");
            return;
        }
        calc(people, id_dead, inheritance, stash);
        print(stash);
    }
    public static void calc(ArrayList<String> people, String id_dead, int inheritance, HashMap<String, Integer> stash){
        int count = 0;
        ArrayList<String> no_heirs = new ArrayList<>();
        for (String s : people){
            String[] data = s.split(" ");
            if (("-" + data[2]).equals(id_dead) || ("-" + data[3]).equals(id_dead)){
                if (data[0].startsWith("-")){
                    if (hasLiveHeir(people, data[0])) count++;
                    else no_heirs.add(data[0]);
                }
                else count++;
            }
        }
        int shares = inheritance / count;
        if (stash.isEmpty()) stash.put(Character.MAX_VALUE + "", inheritance % count);
        else stash.put(Character.MAX_VALUE + "", stash.get(Character.MAX_VALUE + "") + (inheritance % count));
        putToStash(people, no_heirs, id_dead, shares, stash);
    }
    public static void putToStash(ArrayList<String> people, ArrayList<String> no_heirs, String id_dead, int shares, HashMap<String, Integer> stash){
        for (String s : people){
            String[] data = s.split(" ");
            if (no_heirs.contains(data[0])){
                continue;
            }
            if (("-" + data[2]).equals(id_dead) || ("-" + data[3]).equals(id_dead)){
                if (data[0].startsWith("-")){
                    calc(people, data[0], shares, stash);
                }
                else stash.put(data[0] + " " + data[1], shares);
            }
        }
    }
    public static boolean hasLiveHeir(ArrayList<String> people, String id_dead){
        boolean found_heir = false;
        for (String s : people){
            String[] data = s.split(" ");
            if (("-" + data[2]).equals(id_dead) || ("-" + data[3]).equals(id_dead)){
                if (data[0].startsWith("-")) {
                    found_heir = hasLiveHeir(people, data[0]);
                    if (found_heir) return found_heir;
                }
                else return true;
            }
        }
        return found_heir;
    }
    public static void print(HashMap<String, Integer> stash){
        HashMap<Integer, String> temp = new HashMap<>();
        ArrayList<Integer> ids = new ArrayList<>();
        for (String s : stash.keySet()){
            if (s.startsWith(Character.MAX_VALUE + "")) continue;
            temp.put(Integer.parseInt(s.substring(0, s.indexOf(" "))), s.substring(s.indexOf(" ") + 1));
            ids.add(Integer.parseInt(s.substring(0, s.indexOf(" "))));
        }
        Collections.sort(ids);
        for (int id : ids) {
            int money = stash.get(id + " " + temp.get(id));
            System.out.println(String.format("%s saa %d", temp.get(id), money));
        }
        System.out.println("Jakamatta jää " + stash.get(Character.MAX_VALUE + ""));
    }
}