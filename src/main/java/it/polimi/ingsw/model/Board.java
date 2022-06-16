package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The class representing the board each player controls in an ordinary match.
 * It contains all the towers, has an entrance to put students from a cloud and a table for each type of student.
 */
public class Board implements Serializable {
    private final ArrayList<Student> dragons;
    private final ArrayList<Student> unicorns;
    private final ArrayList<Student> fairies;
    private final ArrayList<Student> gnomes;
    private final ArrayList<Student> frogs;
    private ArrayList<Tower> towers;
    private ArrayList<Student> entrance;

    /**
     *
     * @param towersNum 8: 2 players;
     *                  6: 3 players;
     *                  0 or 8: 4 players
     * @param color the color of the towers
     */
    public Board(int towersNum, Colors color){
        dragons = new ArrayList<>();
        unicorns = new ArrayList<>();
        fairies = new ArrayList<>();
        gnomes = new ArrayList<>();
        frogs = new ArrayList<>();
        entrance = null;
        initializeTowers(towersNum, color);
    }

    /**
     * Creates the correct number of towers with the specified color
     * @param towersNum 8: 2 players;
     *                  6: 3 players;
     *                  0 or 8: 4 players
     * @param color the color of the towers
     */
    private void initializeTowers(int towersNum, Colors color){
        Tower temp;
        towers=new ArrayList<>();

        for (int i=0; i< towersNum; i++){
            temp = new Tower(color, this);
            towers.add(temp);
        }
    }

    /**
     * To use before the start of the match to initialize the entrance
     * @param entrance
     */
    public void setEntrance(ArrayList<Student> entrance)throws Exception{
        if (this.entrance != null){
            throw new Exception("Entrance already set.");
        }
        this.entrance = (ArrayList<Student>) entrance.clone();
    }

    /**
     *
     * @param t indicates the type of students you want to check
     * @return the number of students of the specified type sitting in a table
     */
    public int getStudentsOfType(Type_Student t) {
        return switch (t) {
            case DRAGON -> dragons.size();
            case GNOME -> gnomes.size();
            case FAIRY -> fairies.size();
            case UNICORN -> unicorns.size();
            default -> frogs.size();
        };
    }

    /**
     *
     * @return a copy of the list of the towers on the board
     */
    public ArrayList<Tower> getTowers() {
        return (ArrayList<Tower>) towers.clone();
    }

    /**
     *
     * @return the number of towers on the board
     */
    public int getTowersNum() {
        return towers.size();
    }

    /**
     *
     * @return a copy of the list of the students waiting in the entrance
     */
    public ArrayList<Student> getEntrance() {
        return (ArrayList<Student>) entrance.clone();
    }

    /**
     * Checks if the specified student is waiting in the entrance and transfers it to the correct table
     * @param student
     * @throws Exception if the table you want to put the student in is already full
     */
    public void placeStudent(Student student) throws Exception{
        if (getStudentsOfType(student.type()) == 10){
            throw new Exception("This table is already full. Please place that student on a cloud.");
        }

        switch (student.type()) {
            case DRAGON -> dragons.add(student);
            case GNOME -> gnomes.add(student);
            case FAIRY -> fairies.add(student);
            case UNICORN -> unicorns.add(student);
            default -> frogs.add(student);
        }
        removeStudent(student);
    }

    /**
     * Transfers the specified list of students to the entrance
     * @param entrance
     */
    public void importStudents(ArrayList<Student> entrance) {
        this.entrance.addAll(entrance);
    }

    /**
     * Removes the specified student from the entrance
     * @param student
     * @return the student removed
     */
    public Student removeStudent(Student student){
        int found = 9;

        for (int i=0; i< entrance.size(); i++) {
            if (entrance.get(i).type().equals(student.type())) {
                found = i;
                break;
            }
        }
        if (found!=9)
            return entrance.remove(found);
        else return null;
    }

    /**
     * Removes a tower
     * @return the removed tower
     * @throws Exception if there are no more towers
     */
    public Tower removeTower() throws Exception{
        if (towers.isEmpty()){
            throw new Exception("You have no towers in your board");
        }
        return towers.remove(0);
    }

    /**
     * Inserts the specified tower in the board
     * @param tower
     */
    public void returnTower(Tower tower){
        towers.add(tower);
    }

    /**
     *
     * @return true if there are no more towers in the board
     */
    public boolean hasNoTowersLeft(){
        return towers.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder board = new StringBuilder().append(
                """
                        ╔═════════════╦══════════════════════════════════════════════════════════╦═════════════════╗
                        ║             ║                                                          ║                 ║
                        """);
        board.append("║   ");
        addStudentToString(board, 7);
        board.append(" ");
        addStudentToString(board, 0);
        board.append("   ║   ").append(Type_Student.DRAGON).append("Draghi   \u001B[0m:  ");
        addTableToString(board, dragons);
        board.append("   ║                 ║\n");
        board.append("║             ║                                                          ║ ");
        addTowerToString(board, 0);
        board.append(" ");
        addTowerToString(board, 4);
        board.append(" ");
        addTowerToString(board, 8);
        board.append(" ");
        addTowerToString(board, 12);
        board.append(" \u001B[0m║\n");
        board.append("║   ");
        addStudentToString(board, 1);
        board.append(" ");
        addStudentToString(board, 2);
        board.append("   ║   ").append(Type_Student.GNOME).append("Gnomi   \u001B[0m :  ");
        addTableToString(board, gnomes);
        board.append("   ║ ");
        addTowerToString(board, 1);
        board.append(" ");
        addTowerToString(board, 5);
        board.append(" ");
        addTowerToString(board, 9);
        board.append(" ");
        addTowerToString(board, 13);
        board.append(" \u001B[0m║\n");
        board.append("║             ║                                                          ║                 ║\n");
        board.append("║   ");
        addStudentToString(board, 3);
        board.append("    ");
        board.append("   ║   ").append(Type_Student.FAIRY).append("Fate    \u001B[0m :  ");
        addTableToString(board, fairies);
        board.append("   ║                 ║\n");
        board.append("║             ║                                                          ║                 ║\n");
        board.append("║   ");
        addStudentToString(board, 4);
        board.append(" ");
        addStudentToString(board, 5);
        board.append("   ║   ").append(Type_Student.UNICORN).append("Unicorni\u001B[0m :  ");
        addTableToString(board, unicorns);
        board.append("   ║ ");
        addTowerToString(board, 2);
        board.append(" ");
        addTowerToString(board, 6);
        board.append(" ");
        addTowerToString(board, 10);
        board.append(" ");
        addTowerToString(board, 14);
        board.append(" \u001B[0m║\n");
        board.append("║             ║                                                          ║ ");
        addTowerToString(board, 3);
        board.append(" ");
        addTowerToString(board, 7);
        board.append(" ");
        addTowerToString(board, 11);
        board.append(" ");
        addTowerToString(board, 15);
        board.append(" \u001B[0m║\n");
        board.append("║   ");
        addStudentToString(board, 8);
        board.append(" ");
        addStudentToString(board, 6);
        board.append("   ║   ").append(Type_Student.FROG).append("Rane     \u001B[0m:  ");
        addTableToString(board, frogs);
        board.append("""
                   ║                 ║
                ║             ║                                                          ║                 ║
                ╚═════════════╩══════════════════════════════════════════════════════════╩═════════════════╝""");
        return board.toString();
    }

    private void addStudentToString (StringBuilder board, int position){
        if (entrance == null)
            board.append("( )");
        else if (position > entrance.size()-1)
            board.append("( )");
        else
            board.append(entrance.get(position).toString());
    }

    private void addTableToString (StringBuilder board, ArrayList<Student> table) {
        for (int i = 0; i<10; i++) {
            if (i > table.size()-1)
                board.append(" ( )");
            else
                board.append(" ").append(table.get(i).toString());
        }
    }

    private void addTowerToString (StringBuilder board, int position) {
        if (position/2 > towers.size()-1)
            if (position%2 ==1)
                board.append("( )");
            else
                board.append("   ");
        else if (position%2 == 0)
            board.append(towers.get(0).getColor().toString()).append("|_|");
        else
            board.append(towers.get(0).getColor().toString()).append("/_\\");
    }

    /**
     *
     * @param towers to insert in the board
     */
    public void setTowers(ArrayList<Tower> towers) {
        this.towers = towers;
    }

    public void ch_11_effect(Student s){
        switch (s.type()) {
            case DRAGON -> dragons.add(s);
            case GNOME -> gnomes.add(s);
            case FAIRY -> fairies.add(s);
            case UNICORN -> unicorns.add(s);
            case FROG -> frogs.add(s);
        }
    }

    public List<Student> ch_12_effect(Type_Student type){
        List<Student> stu=new ArrayList<>();
        switch (type){
            case DRAGON:
                for (int i = 0; i < 3; i++)
                    if(!dragons.isEmpty())
                        stu.add(dragons.remove(0));
                break;
            case GNOME:
                for (int i = 0; i < 3; i++)
                    if(!gnomes.isEmpty())
                        stu.add(gnomes.remove(0));
                break;
            case FAIRY:
                for (int i = 0; i < 3; i++)
                    if(!fairies.isEmpty())
                        stu.add(fairies.remove(0));
                break;
            case UNICORN:
                for (int i = 0; i < 3; i++)
                    if(!unicorns.isEmpty())
                        stu.add(unicorns.remove(0));
                break;
            case FROG:
                for (int i = 0; i < 3; i++)
                    if(!frogs.isEmpty())
                        stu.add(frogs.remove(0));
                break;
        }
        return stu;
    }

    public void ch_10_effect(Student st,Type_Student type){
        try {
            this.placeStudent(st);
        } catch (Exception e) {
            e.printStackTrace();
        }
        entrance.remove(st);
        switch (type) {
            case DRAGON -> entrance.add(dragons.remove(0));
            case GNOME -> entrance.add(gnomes.remove(0));
            case FAIRY -> entrance.add(fairies.remove(0));
            case UNICORN -> entrance.add(unicorns.remove(0));
            case FROG -> entrance.add(frogs.remove(0));
        }
    }

    public void ch_7_effect(Student st1, Student st2, Student st3, List<Student> stu){
        entrance.remove(st1);
        entrance.remove(st2);
        entrance.remove(st3);
        entrance.addAll(stu);
    }
}
