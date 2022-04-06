package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Match {
Player[] player;
Cloud cloud[];
Bag bag;
MotherNature mothernature;
Map<Type_Student,Player> professors;
List<Land> lands;

    public Match(Player player,Player player1) {
        this.player=new Player[2];
        this.player[0] = player;
        this.player[1] = player1;
        bag=new Bag();
        cloud=new Cloud[2];
        cloud[0]=new Cloud(bag);
        cloud[1]=new Cloud(bag);
        professors=new HashMap<Type_Student,Player>();
        lands=new ArrayList<Land>();
        for(short i=0;i<12;i++)
            lands.add(new Island(i));
        mothernature=new MotherNature(lands.get(0));
    }
    public Match(Player player,Player player1,Player player2) {
        this.player=new Player[3];
        this.player[0] = player;
        this.player[1] = player1;
        this.player[2] = player2;
        cloud=new Cloud[3];
        bag=new Bag();
        cloud[0]=new Cloud(bag);
        cloud[1]=new Cloud(bag);
        cloud[2]=new Cloud(bag);
        professors=new HashMap<Type_Student,Player>();
        for(short i=0;i<12;i++)
            lands.add(new Island(i));
        mothernature=new MotherNature(lands.get(0));
    }

    public Cloud[] getCloud() {
        return cloud;
    }

    public void setCloud(Cloud[] cloud) {
        this.cloud = cloud;
    }

    public Map<Type_Student, Player> getProfessors() {
        return professors;
    }

    public Player[] getPlayer() {
        return player;
    }

    public MotherNature getMothernature() {
        return mothernature;
    }

    public Bag getBag() {
        return bag;
    }

    public List<Land> getLands() {
        return lands;
    }

    public void moveMotherNature(int step){
        int pos=lands.indexOf(mothernature.getPosition());
        pos=(step+pos)%lands.size();
        mothernature.setPosition(lands.get(pos));
    }
}
