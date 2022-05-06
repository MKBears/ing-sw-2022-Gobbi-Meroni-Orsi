package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * One of the two implementation of Land
 */
public class Archipelago implements Land {
    private final ArrayList<Island> group=new ArrayList<>();
    private int size;
    private final Island head;
    private Colors color;

    //Sarebbe meglio non scrivere nello specifico tutte le istruzioni del metodo, basta solo descrivere
    // il funzionamento molto ad altro livello
    /**
     * Constructor: group==ArrayList of Islands, size=group.size(), head=group.get(0), color=towercolor
     * @param group ArrayList of Islands
     */
    public Archipelago(ArrayList<Island> group){  //non so se manca qualcosa
        this.group.addAll(group);
        size = (int) this.group.size();
        head = this.group.get(0);
        color=this.group.get(0).getTower().getColor();
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
    public int getInfluence(Type_Student input) {
        int influence=0;
        for(Island i: group){
            influence= (int) (influence+i.getInfluence(input));
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
     * @throws Exception
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
    public void changeTower(Tower n_tower) {
        for(Island i : group){
            i.changeTower(n_tower);
        }
        try {
            color = head.getTowerColor();
        }
        catch(Exception e){};
        return;
    }

    /**
     *
     * @param other Land with this Archipelago will be united
     * @return Archipelago (myself)
     * @throws Exception
     */
    @Override
    public Archipelago uniteIslands(Land other) throws Exception {
        if (other.getTower().getColor() != this.color) {
            throw new Exception("Wrong Color of Towers"); //
        }
        group.addAll(other.getIslands());
        this.size= (int) group.size();
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
        return t;
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

    //public void mergeGroups(ArrayList<Island> group){
    //    int size = (int) group.size();
    //    for (Island i : group){
    //        this.group.add(i);
    //        group.remove(i);
    //        this.size++;
    //    }
    //}


    @Override
    public String toString() {
        String a="Arcipelago di "+size+" isole{ \n";
        for (Island e:group) {
            a=a+e.toString()+'\n';
        }
        a=a+"}\n";
        return a;
    }
}
