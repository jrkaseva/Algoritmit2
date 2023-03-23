package Codes;

import java.util.ArrayList;
import java.util.Collections;
/**
 * TIEA211 Algoritmit 2 -course from Jyväskylä University (K2023).
 * Author: Joakim Kaseva
 * Last modified: 23.3.2023
 */
public class Perinnonjako{
    /**
     * Person class
     */
    public static class Person implements Comparable{
        protected int id;
        protected String name;
        protected int parent1_id;
        protected int parent2_id;

        // Default constructor
        public Person(){id = 0; name = "no_name"; parent1_id = 0; parent2_id = 0;}
        
        // Constructor with info
        public Person(int id, String name, int parent1_id, int parent2_id) {
            this.id = id;
            this.name = name;
            this.parent1_id = parent1_id;
            this.parent2_id = parent2_id;
        }

        @Override
        public int compareTo(Object arg0) {
            if(arg0 instanceof Person){
                Person p = (Person) arg0;
                int compare_id = p.id;
                return this.id - compare_id;
            }
            throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
        }
    }
    
    /**
     * Main method
     * @param args not in use
     */
    public static void main(String[] args){
        String text = "4 7825349\n-1 Klaara 0 0\n-2 Vihtori 0 0\n3 Amalia 0 2\n-4 Bernard 2 1\n5 Cecilia 1 2\n-6 Erika 2 0\n7 Ferdinand 6 0\n8 Daniel 0 1\n0\n0";
        ArrayList<Person> people = getPeople(text);
        String firstRow = text.substring(0, text.indexOf("\n"));
        String[] temp = firstRow.split(" ");
        int id_of_inheritance = Integer.parseInt(temp[0]) * (-1);
        Person who_died = new Person();
        for(Person p : people){
            if (p.id == id_of_inheritance) {who_died = p; break;}
        }
        int amount_of_inheritance = Integer.parseInt(temp[1]);
        ArrayList<Person> to_inherit = new ArrayList<Person>();
        if(!periAlas(who_died, people, to_inherit)) periYlempaa(who_died, people, to_inherit);
        printInheritance(amount_of_inheritance, to_inherit);
    }

    /**
     * Turns a string into a list of Person-objects. Doesn't take into account the first row of the string
     * @param string to get Person-objects from
     * @return list of Person-objects
     */
    public static ArrayList<Person> getPeople(String string){
        ArrayList<Person> people = new ArrayList<Person>();
        String[] rows = string.split("\n");
        try {
            for (int i = 1; i < rows.length; i++){
                String[] info = rows[i].split(" ");
                if(info.length > 1){
                    int id = Integer.parseInt(info[0]);
                    String name = info[1];
                    int parent1_id = Integer.parseInt(info[2]);
                    int parent2_id = Integer.parseInt(info[3]);
                    people.add(new Person(id, name, parent1_id, parent2_id));
                }
            }
            return people;
        } catch (Exception e) {
            System.out.println("Virhe syötteessä");
            people.add(null);
            return people;
        }
    }

    /**
     * Searches for an heir down the lineage
     * @param who_died Person, who's descendants are being searched for
     * @param people family tree
     * @param to_inherit list of found live heirs
     * @return true if an heir exists
     */
    public static boolean periAlas(Person who_died, ArrayList<Person> people, ArrayList<Person> to_inherit){
        boolean found_heir = false;
        for (int i = 0; i < people.size(); i++){
            // Current person form list
            Person curr = people.get(i);
            if (curr.parent1_id == Math.abs(who_died.id)||curr.parent2_id == Math.abs(who_died.id)){
                // Current person is child of dead person
                if (curr.id < 0){ // Current person is dead
                    found_heir = periAlas(curr, people, to_inherit);
                }
                else { // Current person can inherit
                    found_heir = true;
                    to_inherit.add(curr);
                }
            }
        }
        return found_heir;
    }
    
    /**
     * Searches for an heir down the lineage. If no heirs found, return a Person-object named Government
     * @param who_died Person, who's descendants are being searched for
     * @param people family tree
     * @param to_inherit list of found live heirs
     */
    private static void periYlempaa(Person who_died, ArrayList<Person> people, ArrayList<Person> to_inherit) {
        boolean found_heir = false;
        for(int i = 0; i < people.size(); i++){
            // Current person from list
            Person curr = people.get(i);
            if (who_died.parent1_id == Math.abs(curr.id) || Math.abs(curr.id) == who_died.parent2_id){
                // Current person is parent of dead person
                if(curr.id < 0){
                    // Current person is dead
                    found_heir = periAlas(curr, people, to_inherit);
                }
                else {
                    to_inherit.add(curr);
                    found_heir = true;
                }
            }
        }
        // No heirs
        if (!found_heir) to_inherit.add(new Person(0, "Valtio", 0, 0));
    }

    /**
     * Prints the amount of money each heir gets, and the money that isn't split
     * @param amount_of_inheritance in total to be split
     * @param to_inherit live heirs that can inherit. Duplicate heirs gain that much more inheritance
     */
    public static void printInheritance(int amount_of_inheritance, ArrayList<Person> to_inherit) {
        int shares = to_inherit.size();
        Collections.sort(to_inherit);
        int amount_per_person = amount_of_inheritance / shares;
        for(int i = 0; i < to_inherit.size(); i++){
            Person p = to_inherit.get(i);
            int count = 1;
            for(int j = to_inherit.size() - 1; j > i; j--){
                if (to_inherit.get(j) == p){
                    count++;
                    to_inherit.remove(j);
                }
            }
            System.out.println(String.format("%s saa %d", p.name, amount_per_person * count));
        }
        if (!(amount_of_inheritance % shares == 0)) System.out.println("Jakamatta jää " + amount_of_inheritance % shares);
    }
}