package it.polimi.ingsw.model;

import java.util.ArrayList;

public interface Land {
    void addStudent(Student s);

    public Tower getTower();
    public ArrayList<Student> getStudents();
    public int getID();
    public void addStudents(Student s);  //allo start
    public int getInfluence(Student input);
    public boolean isThereNoEntry();

    ArrayList<Tower> getAllTowers();

    Island getHead();

    int size();

    Colors getTowerColor() throws Exception;

    public void setNoEntry(boolean noEntry) throws Exception;
    public Tower changeTower(Tower n_tower);
    public Archipelago uniteIsland(ArrayList<Land> altra)throws Exception;

    Archipelago uniteIslands(ArrayList<Land> others) throws Exception;

    public ArrayList<Island> getIslands();
}