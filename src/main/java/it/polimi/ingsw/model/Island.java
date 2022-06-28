package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * One of the two implementation of Land
 */
public class Island implements Land, Serializable {
    private final ArrayList<Student> students;
    private final int islandID;
    private Tower tower;
    private boolean noEntry;
    private boolean hasChanged;
    private Tower previousTower;

    /**
     * Constructor: tower null, noEntry false, students empty
     * @param id unique index of the island
     */
    public Island (int id){
        islandID = id;
        tower = null;
        previousTower = null;
        noEntry = false;
        students=new ArrayList<>();
        hasChanged=false;
    }

    /**
     *
     * @return the id of the island
     */
    public int getID() {
        return islandID;
    }

    /**
     *
     * @param s adds a student to the island
     */
    @Override
    public void addStudent(Student s) {
        students.add(s);
    }


    /**
     *
     * @return the tower that governs the island
     */
    @Override
    public Tower getTower() {
        return tower;
    }


    /**
     *
     * @return ArryList that contains all the students that are in the island
     */
    public ArrayList<Student> getStudents(){
        return students;
    }

    /**
     *
     * @param input the type of the Student that you want to know the influence
     * @return an integer: the influence
     */
    @Override
    public int getInfluence(ArrayList<Type_Student> input) {
        int i=0;
        if (!students.isEmpty())
            for (Student s: this.students)
                for (Type_Student t : input)
                    if (s.type().equals(t))
                        i++;
        return i;
    }

    /**
     *
     * @return boolean: "noEntry" state
     */
    public boolean isThereNoEntry(){
        return noEntry;
    }


    /**
     *
     * @param n_tower changes the tower on the island and puts them in their owner's board
     */
    @Override
    public void changeTower(ArrayList<Tower> n_tower) {
        if(this.tower!=null){
            this.tower.getBoard().returnTower(this.tower);
            previousTower = this.tower;
        }
        this.tower=n_tower.get(0);
        hasChanged = true;
    }

    /**
     *
     * @param other Land with this island will be united
     * @return Archipelago type
     * @throws Exception
     */
    @Override
    public Archipelago uniteIslands(Land other) throws Exception {
        if (other.getTower().getColor() != this.tower.getColor()) {
            throw new Exception("Wrong Color of Towers");
        }
        ArrayList<Island> arr=new ArrayList<>();
        arr.add(this);
        arr.addAll(other.getIslands());
        hasChanged=true;
        return new Archipelago(arr);
    }

    /**
     *
     * @return ArrayList with all the island of this land
     */
    public ArrayList<Island> getIslands(){
        ArrayList<Island> me=new ArrayList<>();
        me.add(this);
        return me;
    }

    /**
     *
     * @return ArrayList with the tower of the island (size==1)
     */
    @Override
    public ArrayList<Tower> getAllTowers() {
        ArrayList<Tower>t=new ArrayList<>();
        t.add(tower);
        return (ArrayList<Tower>) t.clone();
    }

    /**
     *
     * @return this island
     */
    @Override
    public Island getHead() {
        return this;
    }

    /**
     *
     * @return the size of the island (it is always 1)
     */
    @Override
    public int size() {
        return 1;
    }

    /**
     *
     * @return the color of the tower (if it contains a tower)
     * @throws Exception
     */
    @Override
    public Colors getTowerColor() throws Exception{
        if (tower != null) {
            return tower.getColor();
        }
        else
            throw new Exception("Non ci sono ancora torri su quest'isola");
    }

    /**
     *
     * @param noEntry change te state of "noEntry"
     * @throws Exception
     */
    public void setNoEntry(boolean noEntry) throws Exception{
        if (noEntry == this.noEntry){
            throw new Exception("C'é già una tessera divieto su quest'isola, sceglierne un'altra");
        }
        this.noEntry = noEntry;
    }

    @Override
    public boolean hasChanged() {
        return hasChanged;
    }

    @Override
    public ArrayList<Tower> getPreviousTowers() {
        ArrayList<Tower> previousTowers;

        if (previousTower == null || !hasChanged) {
            return null;
        }
        previousTowers = new ArrayList<>();
        previousTowers.add(previousTower);
        hasChanged = false;
        return previousTowers;
    }
}
