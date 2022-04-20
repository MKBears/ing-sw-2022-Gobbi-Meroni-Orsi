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
        try {
        cloud[0]=new Cloud(bag,2);
        cloud[1]=new Cloud(bag,2);
        }catch(Exception e){
            e.printStackTrace();
        }
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
        try {
            cloud[0] = new Cloud(bag,3);
            cloud[1] = new Cloud(bag,3);
            cloud[2] = new Cloud(bag,3);
        }catch (Exception e){
            e.printStackTrace();
        }
        professors=new HashMap<Type_Student,Player>();
        lands=new ArrayList<Land>();
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

    public MotherNature getMotherNature() {
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

    public void uniteLandAfter(int i) throws Exception,IllegalArgumentException
    {
        if(i<0 || i>=lands.size()) throw new IllegalArgumentException();
        if (i>=0 && i<lands.size()-1){
            if(!(lands.get(i).getTower().getColor()==lands.get(i+1).getTower().getColor()))throw new IllegalArgumentException();
            Land a;
            Land unito;
            a=lands.remove(i+1);
            unito=lands.remove(i);
            lands.add(i,unito.uniteIslands(a));
        }else{
            if(!(lands.get(0).getTower().getColor()==lands.get(i).getTower().getColor()))throw new IllegalArgumentException();
            Land a;
            Land unito;
            a=lands.remove(i);
            unito=lands.remove(0);
            lands.add(0,unito.uniteIslands(a));
        }
    }

    public void uniteLandBefore(int i) throws Exception,IllegalArgumentException
    {
        if(i<0 || i>lands.size()-1) throw new IllegalArgumentException();
        Land a;
        if(i>=1 && i<lands.size()) {
            if(!(lands.get(i).getTower().getColor()==lands.get(i-1).getTower().getColor()))throw new IllegalArgumentException();
            a=lands.remove(i);
            lands.add(i,lands.get(i-1).uniteIslands(a));
            lands.remove(i-1);
        }else{
            if(!(lands.get(0).getTower().getColor()==lands.get(lands.size()-1).getTower().getColor()))throw new IllegalArgumentException();
            a=lands.remove(lands.size()-1);
            lands.add(1,lands.get(0).uniteIslands(a));
            lands.remove(0);
        }
    }

    public void uniteLandBeforeAndAfter(int i) throws Exception,IllegalArgumentException
    {
        if(i<1 || i>=lands.size()-1) throw new IllegalArgumentException();
        if(!(lands.get(i).getTower().getColor()==lands.get(i-1).getTower().getColor() &&
                lands.get(i).getTower().getColor()==lands.get(i+1).getTower().getColor()))throw new IllegalArgumentException();
        uniteLandAfter(i);
        uniteLandBefore(i);
    }

    public Player checkProfessor(Type_Student e) {
        int a=0;
        int i;
        for (i = 0; i < player.length; i++)
            if(player[i].getBoard().getStudentsOfType(e)>player[a].getBoard().getStudentsOfType(e))
                a=i;
        if(professors.containsKey(e))
            professors.replace(e,player[a]);
        else
            professors.put(e,player[a]);
        return player[a];
    }
}
