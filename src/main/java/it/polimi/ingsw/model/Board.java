package it.polimi.ingsw.model;

import java.util.ArrayList;

/**
 * The class representing the board each player controls in an ordinary match.
 * It contains all the towers, has an entrance to put students from a cloud and a table for each type of student.
 */
public class Board {
    private final ArrayList<Student> dragons;
    private final ArrayList<Student> unicorns;
    private final ArrayList<Student> fairies;
    private final ArrayList<Student> gnomes;
    private final ArrayList<Student> frogs;
    private ArrayList<Tower> towers;
    private ArrayList<Student> entrance;

    /**
     *
     * @param towersNum 8: 2 players;
     *                  6: 3 players;
     *                  0 or 8: 4 players
     * @param color the color of the towers
     */
    public Board(int towersNum, Colors color){
        dragons = new ArrayList<>();
        unicorns = new ArrayList<>();
        fairies = new ArrayList<>();
        gnomes = new ArrayList<>();
        frogs = new ArrayList<>();
        entrance=new ArrayList<>();
        initializeTowers(towersNum, color);
    }

    /**
     * Creates the correct number of towers with the specified color
     * @param towersNum 8: 2 players;
     *                  6: 3 players;
     *                  0 or 8: 4 players
     * @param color the color of the towers
     */
    private void initializeTowers(int towersNum, Colors color){
        Tower temp;
        towers=new ArrayList<>(towersNum);

        for (int i=0; i< towersNum; i++){
            temp = new Tower(color, this);
            towers.add(temp);
        }
    }

    /**
     * To use before the start of the match to initialize the entrance
     * @param entrance
     */
    public void setEntrance(ArrayList<Student> entrance){
        this.entrance = (ArrayList<Student>) entrance.clone();
    }

    /**
     *
     * @param t indicates the type of students you want to check
     * @return the number of students of the specified type sitting in a table
     */
    public int getStudentsOfType(Type_Student t) {
        switch (t) {
            case DRAGON:
                return dragons.size();
            case GNOME:
                return gnomes.size();
            case FAIRIE:
                return fairies.size();
            case UNICORN:
                return unicorns.size();
            default:
                return frogs.size();
        }
    }

    /**
     * @deprecated
     * @return a copy of the list of the towers on the board
     */
    public ArrayList<Tower> getTowers() {
        return (ArrayList<Tower>) towers.clone();
    }

    /**
     *
     * @return the number of towers on the board
     */
    public int getTowersNum() {
        return towers.size();
    }

    /**
     *
     * @return a copy of the list of the students waiting in the entrance
     */
    public ArrayList<Student> getEntrance() {
        return (ArrayList<Student>) entrance.clone();
    }

    /**
     * Checks if the specified student is waiting in the entrance and transfers it to the correct table
     * @param student
     * @throws Exception if the specified student is not present in the entrance
     */
    public void placeStudent(Student student) throws Exception{
        if (getStudentsOfType(student.getType()) == 10){
            throw new Exception("This table is already full. Please place that student on a cloud.");
        }

        switch (student.getType()){
            case DRAGON:
                dragons.add(student);
                break;
            case GNOME:
                gnomes.add(student);
                break;
            case FAIRIE:
                fairies.add(student);
                break;
            case UNICORN:
                unicorns.add(student);
                break;
            case FROG:
                frogs.add(student);
                break;
        }
            entrance.remove(student);
    }

    /**
     * Transfers the specified list of students to the entrance
     * @param entrance
     */
    public void importStudents(ArrayList<Student> entrance) {
        this.entrance.addAll(entrance);
    }

    /**
     * Removes the specified student from the entrance
     * @param s
     * @return the student removed
     */
    public Student removeStudent(Student s){
        return entrance.remove(entrance.indexOf(s));
    }

    /**
     * Removes a tower
     * @return the removed tower
     * @throws Exception if there are no more towers
     */
    public Tower removeTower() throws Exception{
        if (towers.isEmpty()){
            throw new Exception("You have no towers in your board");
        }
        return towers.remove(0);
    }

    /**
     * Inserts the specified tower in the board
     * @param tower
     */
    public void returnTower(Tower tower){
        towers.add(tower);
    }

    /**
     *
     * @return true if there are no more towers in the board
     */
    public boolean hasNoTowersLeft(){
        return towers.isEmpty();
    }

}
