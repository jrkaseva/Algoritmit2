package Codes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class Inheritance {
    /**
     * Person class
     */
    public static class Person implements Comparable<Person>{
        private int id;
        private String name;
        private int parent1_id;
        private int parent2_id;
        private boolean isDead = false;

        // Default constructor
        public Person(){id = 0; name = "no_name"; parent1_id = 0; parent2_id = 0;}
        
        // Constructor with info
        public Person(int id, String name, int parent1_id, int parent2_id) {
            this.id = id;
            this.name = name;
            this.parent1_id = parent1_id;
            this.parent2_id = parent2_id;
        }

        public void isDead(){
            isDead = true;
        }

        @Override
        public int compareTo(Person p) {
            return this.id - p.id;
        }
    }
    static final Scanner SC = new Scanner(System.in);

    public static void main(String[] args){
        while (inherit()){}
    }

    public static boolean inherit(){
        int dead = SC.nextInt();
        if(dead == 0) return false;
        int inheritance = SC.nextInt();
        HashMap<Integer, Person> people = new HashMap<>();
        while(true){
            boolean isDead = false;
            int id = SC.nextInt();
            if (id == 0) break;
            if (id < 0){
                isDead = true;
                id = -id;
            }
            String name = SC.next().replace("\n", " ").trim();
            int parent1 = SC.nextInt();
            int parent2 = SC.nextInt();
            Person add = new Person(id, name, parent1, parent2);
            if (isDead) add.isDead();
            people.put(id, add);
        }
        if (!hasLiveHeir(people, people.get(dead))){
            System.out.println("Valtio saa " + inheritance + "\nJakamatta jää 0");
            return true;
        }
        HashMap<Person, Integer> stash = new HashMap<>();
        heritDown(dead, inheritance, people, stash);
        print(stash);
        return true;
    }

    public static void heritDown(int dead, int inheritance, HashMap<Integer, Person> people, HashMap<Person, Integer> stash){
        int count = 0;
        ArrayList<Integer> no_heirs = new ArrayList<>();
        for (int i : people.keySet()){
            Person p = people.get(i);
            if (p.parent1_id == dead || p.parent2_id == dead){
                if (p.isDead){
                    if (hasLiveHeir(people, p)) count++;
                    else no_heirs.add(p.id);
                }
                else count++;
            }
        }
        int shares = inheritance / count;
        if (stash.isEmpty()) stash.put(null, inheritance % count);
        else stash.put(null, stash.get(null) + (inheritance % count));
        putToStash(people, no_heirs, dead, shares, stash);
    }
    public static void putToStash(HashMap<Integer, Person> people, ArrayList<Integer> no_heirs, int id_dead, int shares, HashMap<Person, Integer> stash){
        for (int i : people.keySet()){
            Person p = people.get(i);
            if (no_heirs.contains(p.id)){
                continue;
            }
            if (p.parent1_id == id_dead || p.parent2_id == id_dead){
                if (p.isDead){
                    heritDown(p.id, shares, people, stash);
                }
                else stash.put(p, shares);
            }
        }
    }
    public static boolean hasLiveHeir(HashMap<Integer, Person> people, Person dead){
        for (int i : people.keySet()){
            Person p = people.get(i);
            if(p.parent1_id == dead.id || p.parent2_id == dead.id){
                if (p.isDead){
                    if(hasLiveHeir(people, p)) return true;
                }
                else return true;
            }
        }
        return false;
    }

    public static void print(HashMap<Person, Integer> stash){
        ArrayList<Person> people = new ArrayList<>();
        for (Person p : stash.keySet()){
            if (p == null) continue;
            people.add(p);
        }
        Collections.sort(people);
        for (Person p : people) {
            System.out.println(String.format("%s saa %d", p.name, stash.get(p)));
        }
        System.out.println("Jakamatta jää " + stash.get(null));
    }
}