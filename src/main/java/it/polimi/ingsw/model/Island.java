package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Island implements Land {  //METTERE A POSTO
    private final ArrayList<Student> students;
    private final short islandID;
    private Tower tower;
    private boolean noEntry;

    public Island (short id){
        islandID = id;
        tower = null;
        noEntry = false;
        students=null;
    }

    public short getID() {
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
    public short getInfluence(Student input) {
        short i=0;
        for (Student s: this.students)
            if(input.getType()==s.getType()){
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
    public Archipelago uniteIslands(ArrayList<Land> others) throws Exception {
        for(Land i:others) {
            if (i.getTower().getColor() != this.tower.getColor()) {
                throw new Exception("Wrong Color of Towers");
            }
        }
        ArrayList<Island> arr=new ArrayList<>();
        arr.add(this);
        for(Land i: others){
            arr.addAll(i.getIslands());
        }
        return new Archipelago(arr);
    }

    public ArrayList<Island> getIslands(){
        ArrayList<Island> me=new ArrayList<>();
        me.add(this);
        return me;
    }

    @Override
    public ArrayList<Tower> getAllTowers() {
        return null;
    }

    @Override
    public Island getHead() {
        return this;
    }

    @Override
    public short size() {
        return 0;
    }

    @Override
    public Colors getTowerColor() {
        return null;
    }


    public void setNoEntry(boolean noEntry) throws DuplicateValueException{  //vedi bene cosa deve fare
        if (noEntry == this.noEntry){
            throw new DuplicateValueException("A No Entry tile has already been set on this island");
        }
        this.noEntry = noEntry;
    }
}
