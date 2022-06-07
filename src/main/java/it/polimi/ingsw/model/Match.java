package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.*;

public class Match implements Serializable {
    Player[] player;
    Cloud[] cloud;
    Bag bag;
    MotherNature motherNature;
    Map<Type_Student,Player> professors;
    ArrayList<Land> lands;

    /**
     * create an instance of the match with two players
     * @param player first player who play
     * @param player1 second player who play
     */
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
        professors=new HashMap<>();
        lands=new ArrayList<>();
        for(short i=0;i<12;i++)
            lands.add(new Island(i));
        List<Student> a=new ArrayList<>();
        for (Type_Student e:Type_Student.values()) {
            a.add(new Student(e));
            a.add(new Student(e));
        }
        Random x=new Random();
        int r;
        for(int j=0;j<12;j++){
            if(j!=0 && j!=6){
                r= x.nextInt(2000);
                lands.get(j).addStudent(a.remove(r%a.size()));
            }
        }
        motherNature =new MotherNature(lands.get(0));
    }

    /**
     * create an instance of match of three players
     * @param player first player of the match
     * @param player1 second player of the match
     * @param player2 third player of the match
     */
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
        professors=new HashMap<>();
        lands=new ArrayList<>();
        for(short i=0;i<12;i++)
            lands.add(new Island(i));
        motherNature =new MotherNature(lands.get(0));
    }

    public int getPlayersNum() {
        return player.length;
    }

    /**
     *
     * @return the cloud of the match
     */
    public Cloud[] getCloud() {
        return cloud.clone();
    }

    /**
     * set the cloud on the match
     * @param cloud cloud to set in the match
     */
    public void setCloud(Cloud[] cloud) {
        this.cloud = cloud;
    }

    /**
     *
     * @return the map with the professors and the player who has the professor
     */
    public Map<Type_Student, Player> getProfessors() {
        return professors;
    }

    /**
     *
     * @return the players of the match
     */
    public Player[] getPlayer() {
        return player;
    }

    /**
     *
     * @return the mother nature of the match
     */
    public MotherNature getMotherNature() {
        return motherNature;
    }

    /**
     *
     * @return the bag of the match
     */
    public Bag getBag() {
        return bag;
    }

    /**
     *
     * @return the lands of the match
     */
    public ArrayList<Land> getLands() {
        return lands;
    }

    /**
     * move mother nature to the land after the number of step
     * @param step number of step that mother nature has to do
     */
    public void moveMotherNature(int step){
        int pos=lands.indexOf(motherNature.getPosition());
        pos=(step+pos)%lands.size();
        motherNature.setPosition(lands.get(pos));
    }

    /**
     * unite the land with the land after
     * @param i index of the first to unite with after
     * @throws IllegalArgumentException  if i is i<0 or i>lands.size() or the colors of the towers are different
     */
    public void uniteLandAfter(int i) throws Exception
    {
        if(i<0 || i>=lands.size()) throw new IllegalArgumentException();
        if (i<lands.size()-1){
            if(!(lands.get(i).getTower().getColor()==lands.get(i+1).getTower().getColor()))throw new IllegalArgumentException();
            Land a;
            Land unito;
            a=lands.remove(i+1);
            unito=lands.remove(i);
            lands.add(i,unito.uniteIslands(a));
            motherNature.setPosition(lands.get(i));
        }else{
            if(!(lands.get(0).getTower().getColor()==lands.get(i).getTower().getColor()))throw new IllegalArgumentException();
            Land a;
            Land unito;
            a=lands.remove(i);
            unito=lands.remove(0);
            lands.add(0,a.uniteIslands(unito));
            motherNature.setPosition(lands.get(0));
        }
    }

    /**
     * unite the land with the one before
     * @param i is the index of the land to unite to the one before
     * @throws IllegalArgumentException if the islands have different colors of tower or i<0 or i>lands.size()-1
     */
    public void uniteLandBefore(int i) throws Exception
    {
        if(i<0 || i>lands.size()-1) throw new IllegalArgumentException();
        Land a;
        if(i>=1) {
            if(!(lands.get(i).getTower().getColor()==lands.get(i-1).getTower().getColor()))throw new IllegalArgumentException();
            a=lands.remove(i);
            lands.add(i,a.uniteIslands(lands.get(i-1)));
            lands.remove(i-1);
            motherNature.setPosition(lands.get(i-1));
        }else{
            if(!(lands.get(0).getTower().getColor()==lands.get(lands.size()-1).getTower().getColor()))throw new IllegalArgumentException();
            a=lands.remove(lands.size()-1);
            lands.add(1,lands.get(0).uniteIslands(a));
            lands.remove(0);
            motherNature.setPosition(lands.get(0));
        }
    }

    /**
     * unite the island in the center with the land before and after
     * @param i is the position of the island that is in the center
     * @throws Exception if the position of the land is i<1 or i>lands.size()-1 or if the towers of the island have different colors
     */
    public void uniteLandBeforeAndAfter(int i) throws Exception
    {
        if(i<1 || i>=lands.size()-1) throw new IllegalArgumentException();
        if(!(lands.get(i).getTower().getColor()==lands.get(i-1).getTower().getColor() &&
                lands.get(i).getTower().getColor()==lands.get(i+1).getTower().getColor()))throw new IllegalArgumentException();
        uniteLandAfter(i);
        uniteLandBefore(i);
    }

    /**
     * change the professor of type e
     * @param e type of the student
     * @return the player who have the professor of type e
     */
    public Player checkProfessor(Type_Student e) {
        int a=0;
        int i;
        for (i = 0; i < player.length; i++)
            if(player[i].getBoard().getStudentsOfType(e)>player[a].getBoard().getStudentsOfType(e))
                a=i;
        if(professors.containsKey(e) && player[a].getBoard().getStudentsOfType(e)>professors.get(e).getBoard().getStudentsOfType(e)) {
            professors.replace(e, player[a]);
            return player[a];
        } else if(!professors.containsKey(e)) {
            professors.put(e, player[a]);
            return player[a];
        }else return professors.get(e);
    }

    public Player getWinner() {
        int minIndex, min, temp, countProf1, countProf2;
        minIndex = 0;
        min = player[0].getBoard().getTowersNum();
        countProf1 = 0;

        for (int i=1; i<player.length; i++){
            temp = player[i].getBoard().getTowersNum();

            if (temp < min) {
                minIndex = i;
                min = temp;
                countProf1 = 0;
            } else if (temp == min) {
                countProf2 = 0;
                for (Type_Student professor : Type_Student.values()) {
                    if (professors.get(professor).equals(player[min]) && countProf1==0) {
                        countProf1++;
                    } else if (professors.get(professor).equals(player[i])) {
                        countProf2++;
                    }
                }
                if (countProf2 > countProf1) {
                    minIndex = i;
                    countProf1 = countProf2;
                }
            }
        }

        return player[minIndex];
    }


    public void setProfessors(Map<Type_Student, Player> professors) {
        this.professors = professors;
    }

    public void setLands(ArrayList<Land> lands) {
        this.lands = lands;
    }

    @Override
    public String toString() {
        String a="player= " + Arrays.toString(player) +'\n'+
                "nuvole= " + Arrays.toString(cloud) +"\n"+
                "professori: \n";
        for (Type_Student e:professors.keySet()) {
            a=a+professors.get(e).getUserName()+" ha professore di tipo "+e+'\n';
        }
        a=a+"\n"+"isole:";
        for (Land e:lands) {
            if(motherNature.getPosition()==e){
                a=a+" madre natura è su quest'";
            }
            a=a+e.toString()+"\n";
        }
        return a;
    }
}
