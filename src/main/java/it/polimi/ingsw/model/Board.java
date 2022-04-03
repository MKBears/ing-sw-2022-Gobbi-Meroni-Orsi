package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private List<Student> dragons;
    private List<Student> unicorns;
    private List<Student> fairies;
    private List<Student> gnomes;
    private List<Student> frogs;
    private List<Tower> towers;
    private List<Student> entrance;

    public Board(){
        dragons=new ArrayList<Student>();
        unicorns= new ArrayList<Student>();
        fairies= new ArrayList<Student>();
        gnomes= new ArrayList<Student>();
        frogs=new ArrayList<Student>();
        towers=new ArrayList<Tower>();
        entrance=new ArrayList<Student>();
    }

    public int getdragons(){
        return dragons.size();
    }

    public int getunicorns() {
        return unicorns.size();
    }

    public int getfairies() {
        return fairies.size();
    }

    public int getgnomes(){
        return gnomes.size();
    }
    public int getfrogs(){
        return frogs.size();
    }

    public int gettowers(){
       return towers.size();
    }

    public List<Student> getEntrance() {
        return entrance;
    }
    public void addTower(Tower tower){
        towers.add(tower);
    }
    public void addDragon(Student dragon){
        dragons.add(dragon);
    }
    public void addGnome(Student gnome){
        gnomes.add(gnome);
    }
    public void addFairie(Student fairie){
        fairies.add(fairie);
    }
    public void addFrog(Student frog){
        frogs.add(frog);
    }
    public void addUnicorn(Student unicorn){
        unicorns.add(unicorn);
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

}
