package it.polimi.ingsw.model;

import java.util.ArrayList;

public interface Land {
    Tower getTower();
    ArrayList<Student> getStudents();
    short getID();
    void addStudent(Student s);  //allo start
    short getInfluence(Student input);
    boolean isThereNoEntry();
    void setNoEntry(boolean noEntry) throws DuplicateValueException;
    Tower changeTower(Tower n_tower);
    Archipelago uniteIslands(ArrayList<Land> altra)throws Exception;
    ArrayList<Island> getIslands();
    ArrayList<Tower> getAllTowers();
    Island getHead();
    short size();
    Colors getTowerColor();
}