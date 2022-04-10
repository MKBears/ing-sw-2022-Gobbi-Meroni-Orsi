package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Board {
    private final ArrayList<Student> dragons;
    private final ArrayList<Student> unicorns;
    private final ArrayList<Student> fairies;
    private final ArrayList<Student> gnomes;
    private final ArrayList<Student> frogs;
    private final ArrayList<Tower> towers;
    private ArrayList<Student> entrance;

    public Board(int towersNum, Colors color){
        dragons=new ArrayList<>();
        unicorns= new ArrayList<>();
        fairies= new ArrayList<>();
        gnomes= new ArrayList<>();
        frogs=new ArrayList<>();
        towers=new ArrayList<>(towersNum);
        initializeTowers(towersNum, color);
        entrance=new ArrayList<>();
    }

    private void initializeTowers(int towersNum, Colors color){
        Tower temp;

        for (int i=0; i< towersNum; i++){
            temp = new Tower(color, this);
            towers.add(temp);
        }
    }

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

    public ArrayList<Tower> getTowers() {
        return (ArrayList<Tower>) towers.clone();
    }

    public ArrayList<Student> getEntrance() {
        return (ArrayList<Student>) entrance.clone();
    }

    public void placeStudent(Student student) throws Exception{
        if (getStudentsOfType(student.getType()) == 10){
            throw new Exception("This table is already full. Please place that student on a cloud.");
        }

        if (!entrance.contains(student)){
            throw new Exception("This student is not placed in the entrance");
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

    public void setEntrance(ArrayList<Student> entrance) {
        this.entrance = (ArrayList<Student>) entrance.clone();
    }

    public void removeStudent(Student s){
        entrance.remove(s);
    }

    public Tower removeTower() throws Exception{
        if (towers.isEmpty()){
            throw new Exception("You have no towers in your board");
        }
        return towers.remove(0);
    }

    public void returnTower(Tower tower){
        towers.add(tower);
    }

    public boolean hasNoTowersLeft(){
        return towers.isEmpty();
    }

}
