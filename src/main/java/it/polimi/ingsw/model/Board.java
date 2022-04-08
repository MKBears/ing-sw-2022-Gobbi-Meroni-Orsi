package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    protected final List<Student> dragons;
    protected final List<Student> unicorns;
    protected final List<Student> fairies;
    protected final List<Student> gnomes;
    protected final List<Student> frogs;
    protected final List<Tower> towers;
    protected List<Student> entrance;

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

    public int getDragons(){
        return dragons.size();
    }

    public int getUnicorns() {
        return unicorns.size();
    }

    public int getFairies() {
        return fairies.size();
    }

    public int getGnomes(){
        return gnomes.size();
    }
    public int getFrogs(){
        return frogs.size();
    }

    public int getTowers(){
       return towers.size();
    }

    public List<Student> getEntrance() {
        return entrance;
    }
    public void addTower(Tower tower){
        towers.add(tower);
    }

    public void addStudent(Student student){
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
    }

    public void setEntrance(List<Student> entrance) {
        this.entrance = entrance;
    }

    public Student removeEntrance(int e){
        return entrance.remove(e);
    }

    public Tower removeTower(){
        return towers.remove(0);
    }

    public void returnTower(Tower tower){
        towers.add(tower);
    }

    public boolean hasNoTowersLeft(){
        return towers.isEmpty();
    }

}
