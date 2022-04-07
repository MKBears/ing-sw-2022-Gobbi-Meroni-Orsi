package it.polimi.ingsw.model;

import java.util.ArrayList;

public interface Land {
    public Tower getTower();
    public ArrayList<Student> getStudents();
    public short getID();
    public void addStudent(Student s);  //allo start
    public short getInfluence(Student input);
    public boolean isThereNoEntry();
    public void setNoEntry(boolean noEntry) throws DuplicateValueException;
    public Tower changeTower(Tower n_tower);
    public Archipelago uniteIslands(ArrayList<Land> altra)throws Exception;
    public ArrayList<Island> getIslands();
    public ArrayList<Tower> getAllTowers();
    public Island getHead();
    public short size();
    public Colors getTowerColor();
}