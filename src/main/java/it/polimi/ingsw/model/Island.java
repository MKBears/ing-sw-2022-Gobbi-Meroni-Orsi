package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Island implements Land {  //METTERE A POSTO
    private final ArrayList<Student> students;
    private final int islandID;
    private Tower tower;
    private boolean noEntry;

    public Island (int id){
        islandID = id;
        tower = null;
        noEntry = false;
        students=new ArrayList<>();
    }

    public int getID() {
        return islandID;
    }

    @Override
    public void addStudent(Student s) {
        students.add(s);
    }


    @Override
    public Tower getTower() {
        return tower;
    }


    public ArrayList<Student> getStudents(){
        return students;
    }

    @Override
    public int getInfluence(Type_Student input) {
        int i=0;
        for (Student s: this.students)
            if(input==s.getType()){
                i++;
        }
        return i;
    }

    public boolean isThereNoEntry(){
        return noEntry;
    }


    @Override
    public Tower changeTower(Tower n_tower) {
            Tower old=this.tower;
            this.tower=n_tower;
            return old;
    }

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

    public ArrayList<Island> getIslands(){
        ArrayList<Island> me=new ArrayList<>();
        me.add(this);
        return me;
    }

    @Override
    public ArrayList<Tower> getAllTowers() {
        ArrayList<Tower>t=new ArrayList<>();
        t.add(tower);
        return t;
    }

    @Override
    public Island getHead() {
        return this;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public Colors getTowerColor() throws Exception{
        if (tower != null) {
            return tower.getColor();
        }
        else
            throw new Exception("There is currently no Towers here");
    }


    public void setNoEntry(boolean noEntry) throws DuplicateValueException{  //vedi bene cosa deve fare
        if (noEntry == this.noEntry){
            throw new DuplicateValueException("A No Entry tile has already been set on this island");
        }
        this.noEntry = noEntry;
    }
}
