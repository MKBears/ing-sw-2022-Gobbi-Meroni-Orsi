package it.polimi.ingsw.model;

import java.util.ArrayList;

public interface Land {
    public Tower getTower();
    public ArrayList<Student> getStudents();
    public short getID();
    public void addStudents(Student s);  //allo start
    public short getInfluence(Student input);
    public boolean isThereNoEntry();
    public void setNoEntry(boolean noEntry) throws DuplicateValueException;
    public Tower changeTower(Tower n_tower);
    public Archipelago uniteIsland(ArrayList<Land> altra)throws Exception;
    public ArrayList<Island> getIslands();
}