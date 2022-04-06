package it.polimi.ingsw.model;

import java.util.ArrayList;

public class GroupOfIslands {
    private final ArrayList<Island> group;
    private short size;
    private final Island head;
    private Colors color;

    public GroupOfIslands(ArrayList<Island> group){
        this.group = new ArrayList<>(group);
        size = (short) this.group.size();
        head = this.group.get(0);

        for (short i=1; i<size; i++){
            head.setStudents(group.get(i).transferStudents());
        }
        /*
        il parametro in ingresso al costruttore arriva da una subList fatta nel Match prima di creare
        il gruppo di isole.
        */
        try {
            color = head.getControllingColor();
        }catch (NoControlException e){
            //se abbiamo fatto bene le cose non dovrebbe mai arrivare qui
        }
    }

    public short size(){
        return (short) group.size();
    }

    public ArrayList<Island> getGroup() {
        return group;
    }

    public Island getHead() {
        return head;
    }

    public void addIsland(Island i){
        group.add(i);
        head.setStudents(i.transferStudents());
        size++;
    }

    public void mergeGroups(ArrayList<Island> group){
        short size = (short) group.size();
        for (Island i : group){
            this.group.add(i);
            group.remove(i);
            this.size++;
        }
    }

    public Colors getTowerColor(){
        return color;
    }

    public void changeControllingColor(ArrayList<Tower> towers){
        if (towers.size() < size){
            /*
            Questo player ha vinto, ma direi che si puo' fare un controllo in Match prima di chiamare
            questo metodo.
            Se abbiamo fato le cose bene in Match, towers.size() non dovrebbe poter essere > size
            */
        }

        for (short i=0; i<size; i++){
            group.get(i).setTower(towers.get(i));
        }
    }

    public Type_Student getControllingColor(){
        return head.getControllingType();
    }

    public void setStudents(ArrayList<Student> students){
        head.setStudents(students);
        //Non so se sia necessario questo metodo
    }

}
