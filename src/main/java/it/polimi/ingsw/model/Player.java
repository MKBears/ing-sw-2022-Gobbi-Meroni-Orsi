package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Random;

public class Player {
    private final int playerID;
    private final Colors color;
    private final Board board;
    private ArrayList<Tower> towers;
    private final Wizard wizard;
    private ArrayList<AssistantCard> deck;
    private final MotherNature motherNature;

    public Player(int ID, Colors color, short towersNum, Wizard wizard, MotherNature mn){
        playerID = ID;
        this.color = color;
        this.wizard = wizard;
        board= new Board();
        motherNature = mn;
        initializeDeck();
        initializeTowers(towersNum);
    }

    private void initializeDeck(){
        AssistantCard temp;
        short steps;

        deck = new ArrayList<>();
        for (short i=0; i<10; i++){
            steps = (short) (i%3);
            if (steps == 0){
                temp = new AssistantCard(i, (short) 3);
            }
            else {
                temp = new AssistantCard(i, steps);
            }
            deck.add(temp);
        }
    }

    private void initializeTowers(short towersNum){
        Tower temp;

        towers = new ArrayList<>();

        for (int i=0; i< towersNum; i++){
            temp = new Tower(color);
            towers.add(temp);
        }
    }

    public int getPlayerID() {
        return playerID;
    }

    public Colors getColor() {
        return color;
    }

    public Tower getTower() {
        return towers.remove(0);
    }

    public void returnTower(Tower tower){
        towers.add(tower);
    }

    public AssistantCard draw (){
        Random card = new Random();
        return deck.remove(card.nextInt(10));
    }

    public void importStudents(Cloud c){
        board.setEntrance(c.getStudents());
    }

}
