package it.polimi.ingsw.model;

import java.util.ArrayList;

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
     * @param towersNum 8 if there are 2 or 4 players, 6 if the number of players is 3
     * @param color the color of the towers
     */
    public Board(int towersNum, Colors color){
        dragons=new ArrayList<>();
        unicorns= new ArrayList<>();
        fairies= new ArrayList<>();
        gnomes= new ArrayList<>();
        frogs=new ArrayList<>();
        initializeTowers(towersNum, color);
        entrance=new ArrayList<>(4);
    }

    /**
     * Creates the correct number of towers with the specified color
     * @param towersNum 8 if there are 2 or 4 players, 6 if the number of players is 3
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
     *
     * @return the list of the towers on the board
     */
    public ArrayList<Tower> getTowers() {
        return (ArrayList<Tower>) towers.clone();
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
        if (!entrance.contains(student)){
            throw new Exception("This student is not placed in the entrance");
        }

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
    public void setEntrance(ArrayList<Student> entrance) {
        this.entrance = (ArrayList<Student>) entrance.clone();
    }

    /**
     * Removes the specified student from the entrance
     * @param s
     */
    public void removeStudent(Student s){
        entrance.remove(s);
    }

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
