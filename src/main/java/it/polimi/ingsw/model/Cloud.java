package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Cloud {

    private final Student[] students;
    private final Bag bag;
    private boolean hasBeenChosen;
    private int nop=0;

    public Cloud (Bag bag, int number_of_players) throws Exception{
        if(number_of_players==2 || number_of_players==4){
            students = new Student[3];
            nop=3;
        }
        else if(number_of_players==3){
            students = new Student[4];
            nop=4;
        }
        else
            throw new Exception("Wrong number of players");
        for(int i=0; i<nop; i++){
            students[i]=null;
        }
        this.bag = bag;
        hasBeenChosen=false;
    }

    public void importStudents(){
        for (int i = 0; i < nop; i++) {
            try {
                students[i] = bag.getRandomStudent();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Student> getStudents() {
        ArrayList<Student> s = new ArrayList<>();
        for (int i=0; i<nop; i++){
            s.add(students[i]);
            students[i] = null;
        }
        return s;
    }

    public void choose(){
        hasBeenChosen = true;
    }

    public boolean hasBeenChosen(){
        return hasBeenChosen;
    }

    public void reset(){
        hasBeenChosen = false;
    }
}
