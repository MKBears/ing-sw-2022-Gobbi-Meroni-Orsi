package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Island implements Land {

    private ArrayList<Student> students;
    private final short islandID;
    private Tower tower;
    private boolean noEntry;
    private ArrayList<Island>archi;

    public Island (short id){
        islandID = id;
        tower = null;
        noEntry = false;
        if(!archi.isEmpty()){
            archi.clear();
        }
    }

    public short getID() {
        return islandID;
    }

    @Override
    public void addStudents(Student s) {  //implementazione sbagliata: uno per volta sempre?
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
        for (Student s:students)
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
    public Archipelago uniteIsland(ArrayList<Land> altre) throws Exception{
        short a=0;
        for(Land i:altre) {
            if (i.getTower().getColor() != this.tower.getColor()) {
                throw new Exception("Wrong Color of Towers");
            }
        }
        ArrayList<Land> arr=new ArrayList<>();
        arr.add(this);
        arr.addAll(altre);
        Archipelago union= new Archipelago(arr);
        return union;
    }

    public ArrayList<Island> getIslands(){
        ArrayList<Island> me=new ArrayList<>();
        me.add(this);
        return me;
    }


    public void setNoEntry(boolean noEntry) throws DuplicateValueException{  //vedi bene cosa deve fare
        if (noEntry == this.noEntry){
            throw new DuplicateValueException("A No Entry tile has already been set on this island");
        }
        this.noEntry = noEntry;
    }
}
