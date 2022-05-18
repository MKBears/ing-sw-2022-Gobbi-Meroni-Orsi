package it.polimi.ingsw.client;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.client.Message4Server;
import it.polimi.ingsw.serverController.Action;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.List;


import static java.lang.Integer.parseInt;

public class Cli implements View,Runnable{
    Scanner input;
    String state;
    Boolean end;
    Message4Server server;
    ObjectInputStream in;
    ObjectOutputStream out;
    Player me;
    Match match;
    Action action;
    public Cli(Message4Server server, ObjectInputStream in, ObjectOutputStream out){
       input=new Scanner(System.in);
       end=false;
       this.server=server;
       this.in=in;
       this.out=out;
    }

    public void setMe(Player me) {
        this.me = me;
    }

    public void setMatch(Match match) {
        this.match = match;
        action=new Action(match);
    }

    public String getUsername(){
        String user;
        System.out.println("inserire username:");
        user=input.nextLine();
        return user;
    }

    public Wizards getWizard(List<Wizards> wizards){
        System.out.println("scegli il mago:");
        for (Wizards e:wizards) {
            System.out.println((wizards.indexOf(e)+1)+" per "+e.toString());
        }
        int choose=input.nextInt();
        while(choose<1 || choose>4){
            System.out.println("scegli il mago:");
            input.nextInt();
        }
        return wizards.get(choose-1);
    }

    public Cloud getCloud(List<Cloud> clouds){
        int i =1;
        System.out.println("Scegli la nuvola tra: \n");
        for (Cloud e: clouds) {
            if(!e.hasBeenChosen()){
                System.out.println("nuvola "+i+" "+e);
                i++;
            }
        }
        int choose= input.nextInt();
        while (choose<1 || choose>=i){
            System.out.println("scegli un numero tra 1 e "+(i-1)+":");
            input.nextInt();
        }
        return clouds.get(choose-1);
    }

    public AssistantCard getAssistantCard(List<AssistantCard> cards){
        System.out.println("scegli la carta assistente tra: \n");
        for(int i=0;i<cards.size();i++){
            System.out.println(i+" "+cards.get(i)+"\n");
        }
        int choose=input.nextInt();
        while (choose<1 ||choose>cards.size()){
            System.out.println("scegli la carta assistente tra: \n");
            choose=input.nextInt();
        }
        return cards.get(choose-1);
    }


    public int getNumStep(Player pl){
        System.out.println("scegli di spostare Madre Natura di? (deve " +
                "essere un numero compreso tra 0 e "+pl.getPlayedCard().getMNSteps());
        int step= input.nextInt();
        while (step<0 || step>pl.getPlayedCard().getMNSteps()){
            System.out.println("scegli di spostare Madre Natura di? (deve " +
                    "essere un numero compreso tra 0 e "+pl.getPlayedCard().getMNSteps());
            step=input.nextInt();
        }
        return step;
    }

    public void getWinner(Player pl){
        System.out.println("il vincitore della partita è: "+pl.getUserName());
    }

    public Student getStudent(Player pl){
        int i=0,choose;
        System.out.println("scegli uno studente tra: \n");
        for (Student e:pl.getBoard().getEntrance()) {
            System.out.println(e.toString()+'\n');
            i++;
        }
        System.out.println("scegli un numero tra 1 e "+i+" :");
        choose=input.nextInt();
        while (choose<1 || choose>i){
            System.out.println("scegli un numero tra 1 e "+i+" :");
            choose=input.nextInt();
        }
        return pl.getBoard().removeStudent(pl.getBoard().getEntrance().get(choose-1));
    }

    public String getDestination(Match match){
        int i=1;
        String choose;
        System.out.println("dove vuoi che vada lo studente?\n");
        System.out.println("se si vuole aggiungere alla sala scrivi sala oppure scegli tra le seguenti isole\n");
        for (Land e:match.getLands()) {
            System.out.println("scrivi "+i+" per scegliere"+e+"\n");
            i++;
        }
        System.out.println("inserire scelta: ");
        choose=input.nextLine();
        int chooseInt=parseInt(choose);
        while (choose.toLowerCase()!="sala" && (chooseInt<1 || chooseInt>i)){
            System.out.println("inserire scelta: ");
            choose=input.nextLine();
            chooseInt=parseInt(choose);
        }
        if(choose.toLowerCase()=="sala"){
            choose="board";
        }else{
            Integer temp=match.getLands().get(chooseInt-1).getID();
            choose=temp.toString();
        }
        return choose;
    }

    public void printMatch(Match match){
        System.out.println(match.toString()+'\n');
    }

    public void printTurn(Player pl,String phase){
        System.out.println("tocca a: "+pl.getUserName()+"in fase di"+phase+"\n");
    }

    public void lastRound(){
        System.out.println("sono finiti gli studenti nel sacchetto questo sarà l'ultimo round\n");
    }

    public int getNumPlayer(){
        System.out.println("inserire il numero di giocatori: ");
        int num=input.nextInt();
        while(num<=1 || num>=5){
            System.out.println("inserire il numero di giocatori: ");
            num=input.nextInt();
        }
        return num;
    }

    public void getTitolo(){
        System.out.println( "\t\t\t████ ███  █    █     █    █ ███████ █     █  ███  \n" +
                            "\t\t\t█    █  █ █   █ █    ██   █    █     █   █  █     \n" +
                            "\t\t\t███  ███  █  █   █   █ █  █    █      █ █    ███  \n" +
                            "\t\t\t█    █ █  █ ███████  █  █ █    █       █        █ \n" +
                            "\t\t\t████ █  █ █ █     █  █   ██    █       █     ███  \n");
    }

    @Override
    public void run() {
        while (end==false){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            switch (state){
                case("Wizard"):
                    List<Wizards> willy= null;
                    try {
                        willy = (ArrayList<Wizards>)in.readObject();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    server.sendChoice(this.getWizard(willy));
                    break;
                case("ChooseCard"):
                    List<AssistantCard> cards= null;
                    try {
                        cards = (ArrayList<AssistantCard>) in.readObject();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    AssistantCard a;
                    a=this.getAssistantCard(cards);
                    me.draw(a.getValue());
                    server.sendChosenCard(a);
                    break;
                case("MoveMN"):
                    int step=this.getNumStep(me);
                    action.cardAndMoveMN(me.getPlayedCard(),step);
                    server.sendStepsMN(step);
                    try {
                        action.controlLand(me);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    this.printMatch(match);
                    break;
                case("ChooseCloud"):
                    List<Cloud> clouds= null;
                    try {
                        clouds = (ArrayList<Cloud>) in.readObject();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    Cloud clo=this.getCloud(clouds);
                    action.chooseCloud(me,clo);
                    server.sendChoiceCloud(clo);
                    this.printMatch(match);
                case("MoveStudents"):
                    Student st;
                    String move;
                    for (int i=0;i<match.getPlayer().length+1;i++) {
                        st = this.getStudent(me);
                        move = this.getDestination(match);
                        server.sendMovedStudent("Student"+(i+1), st, move);
                        if (move.equals("board")) {
                            try {
                                action.moveFromIngressToBoard(me, st);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Integer temp = parseInt(move);
                            action.moveFromIngressToLand(me, st, match.getLands().get(temp.intValue()));
                        }
                    }
                    break;
                case("EndGame"):
                    end=true;
            }
        }
    }

    public void wakeUp(String state){
        this.state=state;
        this.notifyAll();
    }
}
