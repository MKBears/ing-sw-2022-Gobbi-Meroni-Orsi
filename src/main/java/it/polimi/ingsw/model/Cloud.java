package it.polimi.ingsw.model;

import java.util.ArrayList;

/**
 * it contains 3 or 4 students (it depends by the number of players)
 */
public class Cloud {

    private final Student[] students;
    private final Bag bag;
    private boolean hasBeenChosen;
    private int nop=0;

    /**
     *
     * @param bag link of the bag of the game
     * @param number_of_players necessary in order to know the number of Students to get
     * @throws Exception
     */
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

    /**
     * refills the cloud automatically
     */
    public void importStudents(){
        for (int i = 0; i < nop; i++) {
            try {
                students[i] = bag.getRandomStudent();
            } catch (Exception e) { //
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @return ArrayList that contains the students of the cloud
     */
    public ArrayList<Student> getStudents() {
        ArrayList<Student> s = new ArrayList<>();
        for (int i=0; i<nop; i++){
            s.add(students[i]);
            students[i] = null;
        }
        return s;
    }

    /**
     * changes to true the state "hasBeenChosen"
     */
    public void choose(){
        hasBeenChosen = true;
    }

    /**
     *
     * @return boolean valor: true if it has been chosen
     */
    public boolean hasBeenChosen(){
        return hasBeenChosen;
    }

    /**
     * turns the state "hasBeenChosen" to false
     */
    public void reset(){
        hasBeenChosen = false;
    }
}
