package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Archipelago implements Land {
    private final ArrayList<Island> group=new ArrayList<>();
    private short size;
    private final Island head;
    private Colors color;

    public Archipelago(ArrayList<Island> group){  //non so se manca qualcosa

        this.group.addAll(group);
        size = (short) this.group.size();
        head = this.group.get(0);
        color=this.group.get(0).getTower().getColor();
    }

    @Override
    public  Tower getTower() {   //grea get color tower e get all towers
        Tower t=this.head.getTower();
        return t;
    }

    @Override
    public ArrayList<Student> getStudents() {
        ArrayList<Student> t= new ArrayList<>();
        for(Island i: group)
        {
            t.addAll(i.getStudents());
        }
        return t;
    }

    @Override
    public short getID() {
        return head.getID();
    }

    @Override
    public void addStudent(Student s) {
        head.addStudent(s);
    }

    @Override
    public short getInfluence(Student input) {
        short influence=0;
        for(Island i: group){
            influence= (short) (influence+i.getInfluence(input));
        }
        return influence;
    }

    @Override
    public boolean isThereNoEntry() {
        return head.isThereNoEntry();
    }

    @Override
    public void setNoEntry(boolean noEntry) throws DuplicateValueException {
        if (noEntry == this.isThereNoEntry()){
            throw new DuplicateValueException("A No Entry tile has already been set on this island");
        }
        for(Island i: group){
            i.setNoEntry(noEntry);
        }
    }

    @Override
    public Tower changeTower(Tower n_tower) {
        for(Island i : group){
            i.changeTower(n_tower);
        }
        return null;
    }

    @Override
    public Archipelago uniteIslands(ArrayList<Land> others) throws Exception {
        for(Land i:others) {
            if (i.getTower().getColor() != this.color) {
                throw new Exception("Wrong Color of Towers");
            }
        }
        for(Land i: others){
            group.addAll(i.getIslands());
        }
        return this; //ritorna me stesso
    }

    @Override
    public ArrayList<Island> getIslands() {
        return group;
    }

    @Override
    public ArrayList<Tower> getAllTowers() {
        ArrayList<Tower> t= new ArrayList<>();
        for(Island i : group)
        {
            t.add(i.getTower());
        }
        return t;
    }

    public short size(){
        return (short) group.size();
    }

    @Override
    public Colors getTowerColor() {
        return head.getTowerColor();
    }

    public Island getHead() {
        return head;
    }

    //public void mergeGroups(ArrayList<Island> group){
    //    short size = (short) group.size();
    //    for (Island i : group){
    //        this.group.add(i);
    //        group.remove(i);
    //        this.size++;
    //    }
    //}
}
