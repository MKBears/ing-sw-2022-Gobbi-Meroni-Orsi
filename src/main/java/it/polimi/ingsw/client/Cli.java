package it.polimi.ingsw.client;

import it.polimi.ingsw.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Cli {
    Scanner in;
    public void Cli(){
       in=new Scanner(System.in);
    }


    public String getUsername(){
        String user;
        System.out.println("inserire username:");
        user=in.nextLine();
        return user;
    }

    public Wizards getWizard(){
        Wizards wizard=null;
        System.out.println("scegli il mago:");
        int choose=in.nextInt();
        while(choose<1 || choose>4){
            System.out.println("scegli il mago:");
            in.nextInt();
        }
        switch (choose) {
            case (1):
                return Wizards.WIZARD1;
            case (2):
                return Wizards.WIZARD2;
            case (3):
                return Wizards.WIZARD3;
            case (4):
                return Wizards.WIZARD4;
        }
        return wizard;
    }

    public Cloud getCloud(Match match){
        int i =1;
        Map<Integer,Cloud> map=new HashMap<>();
        System.out.println("Scegli la nuvola tra: \n");
        for (Cloud e: match.getCloud()) {
            if(!e.hasBeenChosen()){
                System.out.println("nuvola "+i+" "+e);
                map.put(i,e);
                i++;
            }
        }
        int choose= in.nextInt();
        while (choose<1 || choose>=i){
            System.out.println("scegli un numero tra 1 e "+(i-1)+":");
            in.nextInt();
        }
        return map.get(choose);
    }

    public AssistantCard getAssistantCard(Player pl){
        System.out.println("scegli la carta assistente tra: \n");
        for(int i=0;i<pl.getDeck().size();i++){
            System.out.println(i+" "+pl.getDeck().get(i)+"\n");
        }
        int choose=in.nextInt();
        while (choose<1 ||choose>pl.getDeck().size()){
            System.out.println("scegli la carta assistente tra: \n");
            choose=in.nextInt();
        }
        return pl.getDeck().get(choose-1);
    }


    public int getNumStep(Player pl){
        System.out.println("scegli di spostare Madre Natura di? (deve " +
                "essere un numero compreso tra 0 e "+pl.getPlayedCard().getMNSteps());
        int step= in.nextInt();
        while (step<0 || step>pl.getPlayedCard().getMNSteps()){
            System.out.println("scegli di spostare Madre Natura di? (deve " +
                    "essere un numero compreso tra 0 e "+pl.getPlayedCard().getMNSteps());
            step=in.nextInt();
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
        choose=in.nextInt();
        while (choose<1 || choose>i){
            System.out.println("scegli un numero tra 1 e "+i+" :");
            choose=in.nextInt();
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
        choose=in.nextLine();
        int chooseInt=parseInt(choose);
        while (choose.toLowerCase()!="sala" && (chooseInt<1 || chooseInt>i)){
            System.out.println("inserire scelta: ");
            choose=in.nextLine();
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
        System.out.println("tocca a: "+pl.getUserName()+"in fase di"+phase);
    }
}
