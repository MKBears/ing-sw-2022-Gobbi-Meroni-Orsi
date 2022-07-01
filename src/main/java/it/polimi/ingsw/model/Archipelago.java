package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * One of the two implementation of Land
 */
public class Archipelago implements Land, Serializable {
    private final ArrayList<Island> group=new ArrayList<>();
    private int size;
    private final Island head;
    private Colors color;
    private boolean hasChanged;
    private final ArrayList<Tower> previousTowers;

    /**
     * Constructor: group==ArrayList of Islands, size=group.size(), head=group.get(0), color=towercolor
     * @param group ArrayList of Islands
     */
    public Archipelago(ArrayList<Island> group){
        this.group.addAll(group);
        size = this.group.size();
        head = this.group.get(0);
        color=this.group.get(0).getTower().getColor();
        previousTowers = new ArrayList<>();
        hasChanged=true;
    }

    /**
     *
     * @return the tower that governs the Archipelago
     */
    @Override
    public  Tower getTower() {
        return this.head.getTower();
    }

    /**
     *
     * @return ArrayList with all the students of the Archipelago
     */
    @Override
    public ArrayList<Student> getStudents() {
        ArrayList<Student> t= new ArrayList<>();
        for(Island i: group)
        {
            t.addAll(i.getStudents());
        }
        return t;
    }

    /**
     *
     * @return the id of the Archipelago
     */
    @Override
    public int getID() {
        return head.getID();
    }

    /**
     *
     * @param s adds the Student in the Archipelago
     */
    @Override
    public void addStudent(Student s) {
        head.addStudent(s);
    }

    /**
     *
     * @param input the type of the Student of you want to know the influence
     * @return aj integer: the influence
     */
    @Override
    public int getInfluence(ArrayList<Type_Student> input) {
        int influence=0;
        for(Island i: group){
            influence += i.getInfluence(input);
        }
        return influence;
    }

    /**
     *
     * @return boolean: the state of "noEntry"
     */
    @Override
    public boolean isThereNoEntry() {
        return head.isThereNoEntry();
    }

    /**
     *
     * @param noEntry changes the state if "noEntry"
     * @throws Exception if an entry tile has already been set on this archipelago
     */
    @Override
    public void setNoEntry(boolean noEntry) throws Exception {
        if (noEntry == this.isThereNoEntry()){
            throw new Exception("A No Entry tile has already been set on this island");
        }
        for(Island i: group){
            i.setNoEntry(noEntry);
        }
    }

    /**
     *
     * @param n_tower change the towers on the Archipelago and returns the old towers on their board
     */
    @Override
    public void changeTower(ArrayList<Tower> n_tower) throws Exception {
        if (this.size == n_tower.size()) {
            ArrayList<Tower> single = new ArrayList<>();
            previousTowers.clear();

            for (Island i : group) {
                single.add(n_tower.remove(0));
                previousTowers.add(i.getTower());
                i.changeTower(single);
                single.clear();
            }
            color = head.getTowerColor();
            hasChanged = true;
        }
        else{
            throw new Exception("Error in towers");  ////
        }
    }

    /**
     *
     * @param other Land with this Archipelago will be united
     * @return Archipelago (myself)
     * @throws Exception if some island's tower's color is different from the others
     */
    @Override
    public Archipelago uniteIslands(Land other) throws Exception {
        if (other.getTower().getColor() != this.color) {
            throw new Exception("Wrong Color of Towers");
        }
        group.addAll(other.getIslands());
        this.size= group.size();
        return this; //ritorna me stesso
    }

    /**
     *
     * @return ArrayList of all the Islands that compose the Archipelago
     */
    @Override
    public ArrayList<Island> getIslands() {
        return group;
    }

    /**
     *
     * @return ArrayList of all the Towers that compose the Archipelago
     */
    @Override
    public ArrayList<Tower> getAllTowers() {
        ArrayList<Tower> t= new ArrayList<>();
        for(Island i : group)
        {
            t.add(i.getTower());
        }
        return (ArrayList<Tower>) t.clone();
    }

    /**
     *
     * @return number of Islands that composes the archipelago
     */
    public int size(){
        return size;
    }

    /**
     *
     * @return the color of the towers of the Archipelago
     */
    @Override
    public Colors getTowerColor() {
        return color;
    }

    /**
     *
     * @return the Island used as head for the Archipelago (the main island)
     */
    public Island getHead() {
        return head;
    }

    /**
     * Tells you if the towers have changed since tha last time you checked
     * @return true if towers have changed
     */
    @Override
    public boolean hasChanged() {
        return hasChanged;
    }

    /**
     *
     * @return the previous towers after a new player controls the archipelago
     */
    public ArrayList<Tower> getPreviousTowers(){
        if (previousTowers == null || !hasChanged) {
            return null;
        }
        hasChanged = false;
        return previousTowers;
    }
}
