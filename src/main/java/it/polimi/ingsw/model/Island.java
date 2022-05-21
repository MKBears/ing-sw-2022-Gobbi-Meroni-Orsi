package it.polimi.ingsw.model;

import java.util.ArrayList;

/**
 * One of the two implementation of Land
 */
public class Island implements Land {
    private final ArrayList<Student> students;
    private final int islandID;
    private Tower tower;
    private boolean noEntry;

    /**
     * Constructor: tower null, noEntry false, students empty
     * @param id unique index of the island
     */
    public Island (int id){
        islandID = id;
        tower = null;
        noEntry = false;
        students=new ArrayList<>();
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
        for (Student s: this.students) {
            for (Type_Student t : input) {
                if (s.getType().equals(t)) {
                    i++;
                }
            }
        }
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
            this.tower=n_tower.get(0);
        }
        else{
            this.tower=n_tower.get(0);
        }
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
        return t;
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
     * @return 1
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
            return tower.getColor(); //
        }
        else
            throw new Exception("There is currently no Towers here");
    }

    /**
     *
     * @param noEntry change te state of "noEntry"
     * @throws Exception
     */
    public void setNoEntry(boolean noEntry) throws Exception{  //vedi bene cosa deve fare
        if (noEntry == this.noEntry){
            throw new Exception("A No Entry tile has already been set on this island");
        }
        this.noEntry = noEntry;
    }

    @Override
    public String toString() {
        String a= "isola " + islandID +
                " con studenti: " + students;
        if(tower!=null)
            a=a+" e " + tower;
        else a=a+" non ha torri";
        if (noEntry==false){
            return a;
        }
        else
            return a+" entrata chiusa";
    }
}
