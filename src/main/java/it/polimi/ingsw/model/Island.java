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
        for (Student s: this.students) {
            for (Type_Student t : input) {
                if (s.type().equals(t)) {
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
            previousTower = this.tower;
            this.tower=n_tower.get(0);
            hasChanged = true;
        } else{
            this.tower=n_tower.get(0);
            hasChanged=true;
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
            throw new Exception("There are currently no Towers here");
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
        String island;
        int[] counter = countStudents();
        island =    "   ___________\n"+
                    "  /  ";

        if (noEntry)
            island +=   "\u001B[31;1m"+"DIVIETO"+"\u001B[0m";
        else
            island +=   "       ";

        island +=   "  \\\n"+
                    " /    "+new Student(Type_Student.DRAGON).toString()+"x"+counter[0];

        if (counter[0] < 10)
            island += " ";

        island +=   "   \\\n"+
                    "/"+new Student(Type_Student.FAIRY).toString()+"x"+counter[1];

        if (counter[1] < 10)
            island += " ";

        if (tower != null)
            island +=   tower.getColor().toString()+"|_|"+new Student(Type_Student.GNOME)+"x"+counter[2];
        else
            island +=   "   "+new Student(Type_Student.GNOME)+"x"+counter[2];

        if (counter[2] < 10)
            island += " ";

        island +=   "\\\n"+
                    "\\      ";

        if (tower != null)
            island += tower.getColor().toString()+"/_\\"+"\u001B[0m";
        else
            island += "   ";

        island +=   "      /\n"+
                    " \\ "+new Student(Type_Student.UNICORN)+"x"+counter[3];

        if (counter[3] < 10)
            island += " ";

        island +=   ""+new Student(Type_Student.FROG)+"x"+counter[4];

        if (counter[4] < 10)
            island += " ";

        island +=   "/\n"+
                    "  \\___________/";

        return island;
    }

    private int[] countStudents() {
        int[] counter = new int[5];

        for (int i=0; i<5; i++) {
            counter[i] = 0;
        }

        for (Student student : students) {
            switch (student.type()) {
                case DRAGON -> counter[0]++;
                case FAIRY -> counter[1]++;
                case GNOME -> counter[2]++;
                case UNICORN -> counter[3]++;
                case FROG -> counter[4]++;
            }
        }

        return counter;
    }

    @Override
    public boolean hasChanged() {
        return hasChanged;
    }

    @Override
    public ArrayList<Tower> getPreviousTowers() throws Exception {
        ArrayList<Tower> previousTowers;

        if (previousTower == null || !hasChanged) {
            throw new Exception ("There haven't been changes");
        }
        previousTowers = new ArrayList<>();
        previousTowers.add(previousTower);
        hasChanged = false;
        return previousTowers;
    }
}
