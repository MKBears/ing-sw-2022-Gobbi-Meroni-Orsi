package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final List<Student> dragons;
    private final List<Student> unicorns;
    private final List<Student> fairies;
    private final List<Student> gnomes;
    private final List<Student> frogs;
    private final List<Tower> towers;
    private List<Student> entrance;

    public Board(){
        dragons=new ArrayList<>();
        unicorns= new ArrayList<>();
        fairies= new ArrayList<>();
        gnomes= new ArrayList<>();
        frogs=new ArrayList<>();
        towers=new ArrayList<>();
        entrance=new ArrayList<>();
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
