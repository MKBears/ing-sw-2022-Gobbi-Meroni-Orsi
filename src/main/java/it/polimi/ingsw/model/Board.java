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
