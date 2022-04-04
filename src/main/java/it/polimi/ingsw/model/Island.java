package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Island {
    private ArrayList<Student> students;
    private final short islandID;
    private Tower tower;
    private boolean noEntry;

    public Island (short id){
        islandID = id;
        tower = null;
        noEntry = false;
    }

    public short getIslandID() {
        return islandID;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public ArrayList<Student> getStudents(){
        return students;
    }

    public Colors getControllingColor() throws NoControlException{
        if (tower == null){
            throw new NoControlException("This island isn't controlled by any player");
        }
        return tower.getColor();
    }

    public Type_Student getControllingType() {
        short[] counter = new short[5];
        short max = 0;

        if (students.isEmpty()) {
            return null;
        } else {
            for (short i = 0; i < 5; i++) {
                counter[i] = 0;
            }

            for (Student s : students) {
                switch (s.getType().getName()) {
                    case "dragon":
                        counter[0]++;
                        break;
                    case "gnome":
                        counter[1]++;
                        break;
                    case "fairie":
                        counter[2]++;
                        break;
                    case "unicorn":
                        counter[3]++;
                        break;
                    default:
                        counter[4]++;
                        break;
                }
            }

            for (short i = 1; i < 5; i++) {
                if (counter[i] > counter[max]) {
                    max = i;
                }
            }

            switch (max) {
                case 0:
                    return Type_Student.DRAGON;
                case 1:
                    return Type_Student.GNOME;
                case 2:
                    return Type_Student.FAIRIE;
                case 3:
                    return Type_Student.UNICORN;
                default:
                    return Type_Student.FROG;
            }
        }
    }

    public boolean isThereNoEntry(){
        return noEntry;
    }

    public void setNoEntry(boolean noEntry) throws DuplicateValueException{
        if (noEntry == this.noEntry){
            throw new DuplicateValueException("A No Entry tile has already been set on this island");
        }
        this.noEntry = noEntry;
    }

    public void setTower(Tower tower) {
        this.tower = tower;
    }

    public ArrayList<Student> transferStudents(){
        ArrayList<Student> cpy = students;
        students = null;
        return cpy;
    }

}
