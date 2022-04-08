package it.polimi.ingsw.model;

public class Board_Experts extends Board{
    private final int c1 = 2, c2 = 5, c3 = 8;
    private int coins;

    public Board_Experts(int towersNum, Colors color){
        super(towersNum, color);
        coins=1;
    }

    public int getCoinsNumber() {
        return coins;
    }

    public void playCharacter(int cost) throws Exception{
        if(coins<cost){
            throw new Exception("You don't have enough money");
        }
        else
        {
            coins=coins-cost;
        }

    }

    @Override
    public void addStudent(Student student){
        int temp;
        
        switch (student.getType()){
            case DRAGON:
                dragons.add(student);
                temp = dragons.size();
                if(temp==c1 || temp==c2 || temp==c3) {
                    coins++;
                }
                break;
            case GNOME:
                gnomes.add(student);
                temp = gnomes.size();
                if(temp==c1 || temp==c2 || temp==c3){
                    coins++;
                }
                break;
            case FAIRIE:
                fairies.add(student);
                temp = fairies.size();
                if(temp==c1 || temp==c2 || temp==c3){
                    coins++;
                }
                break;
            case UNICORN:
                unicorns.add(student);
                temp = unicorns.size();
                if(temp==c1 || temp==c2 || temp==c3){
                    coins++;
                }
                break;
            case FROG:
                frogs.add(student);
                temp = frogs.size();
                if(temp==c1 || temp==c2 || temp==c3){
                    coins++;
                }
                break;
        }
    }
    
}
