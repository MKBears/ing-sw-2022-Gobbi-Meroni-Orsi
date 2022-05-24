package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Interface for Archipelagos and Islands
 */
public interface Land extends Serializable {
    /**
     *
     * @return the tower that governs the land
     */
    Tower getTower();
    /**
     *
     * @return ArrayList that contains all the students that are in the land
     */
    ArrayList<Student> getStudents();
    /**
     *
     * @return the id of the land
     */
    int getID();
    /**
     *
     * @param s adds a student to the land
     */
    void addStudent(Student s);  //allo start
    /**
     *
     * @param input the type of the Student that you want to know the influence
     * @return an integer: the influence
     */
    int getInfluence(ArrayList<Type_Student> input);
    /**
     *
     * @return boolean: "noEntry" state
     */
    boolean isThereNoEntry();
    /**
     *
     * @param noEntry changes te state of "noEntry"
     * @throws Exception
     */
    void setNoEntry(boolean noEntry) throws Exception;
    /**
     *
     * @param n_tower changes the tower on the land and puts them in their owner's board
     */
    void changeTower(ArrayList<Tower> n_tower) throws Exception;
    /**
     *
     * @param other Land that will be united with this land
     * @return Archipelago type
     * @throws Exception
     */
    Archipelago uniteIslands(Land other)throws Exception;
    /**
     *
     * @return ArrayList with all the island of this land
     */
    ArrayList<Island> getIslands();
    /**
     *
     * @return ArrayList with the tower of the land
     */
    ArrayList<Tower> getAllTowers();
    /**
     *
     * @return the head island
     */
    Island getHead();
    /**
     *
     * @return the size of the land
     */
    int size();
    /**
     *
     * @return the color of the towers (if it contains a tower)
     * @throws Exception
     */
    Colors getTowerColor() throws Exception;

    /**
     *
     * @return true if the towers built on it have changed since the last check
     */
    boolean hasChanged();

    /**
     *
     * @return the towers built on this land before it has been conquered
     */
    ArrayList<Tower> getPreviousTowers() throws Exception;
}