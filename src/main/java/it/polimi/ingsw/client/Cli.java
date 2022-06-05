package it.polimi.ingsw.client;

import it.polimi.ingsw.model.*;

import java.util.*;
import java.util.List;


import static java.lang.Integer.parseInt;

public class Cli extends Thread implements View{
    private final Scanner input;
    private String state;
    private Boolean end;
    private Message4Server server;
    private Player me;
    private Match match;
    private Action action;
    private List<Wizards> willy;
    private List<Cloud> clouds;
    private List<AssistantCard> cards;
    Boolean nack;

    public Cli(){
       input=new Scanner(System.in);
       end=false;
       nack=false;
       state="Start";
    }

    public static void main(String[] args) {
        Cli view = new Cli();
        Client client = new Client(view);
        client.start();
        view.start();
    }

    public void setServer(Message4Server server) {
        this.server = server;
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
            choose = input.nextInt();
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
             choose = input.nextInt();
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

    public int getDestination(Match match) {
        int i = 0;
        int choose;
        System.out.println("dove vuoi che vada lo studente?\n");
        System.out.println("se si vuole aggiungere alla sala scrivi sala oppure scegli tra le seguenti isole\n");
        for (Land e : match.getLands()) {
            System.out.println("scrivi " + i + " per scegliere" + e + "\n");
            i++;
        }
        try {
            do {
                System.out.println("inserire scelta: ");
                choose = input.nextInt();
            }while (choose<0 ||choose>12);
            return choose;
        } catch (Exception e) {
            return 12;
        }
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
        System.out.println( "\u001B[34m"+"\t\t  ___________ ________  ___ __________ ______    _________________    ___ ___________ \n"+
                            "\t\t /__________//_______/\\/__//_________//_____/\\  /________________/|  /__//__________/|\n"+
                            "\t\t |__________||   __  \\/|__||   ___   ||     \\ \\ |   ___    ___   ||__|  ||   _______/ \n"+
                            "\t\t\t/_______/|  |/_|  |/__/|  |/__|  ||  |\\  \\ \\|  ||  |  ||  |  |/__|  ||  |/___/|\n"+
                            "\t\t\t|   ____||   _   /\\|  ||   ___   ||  ||\\  \\ |  ||  |  ||  |______   ||____   ||\n"+
                            "\t\t\t|  |/____|  ||\\  \\_|  ||  ||  |  ||  || \\  \\|  ||  |  ||  /______|  |/____|  ||\n"+
                            "\t\t\t|___________/  \\__________/   |______/   \\_____/   |__/   |__________________/ \n"+"\u001B[0m");
    }

    @Override
    public void run() {
         try {
             while (end == false) {
                 switch (state) {
                     case ("Start"):
                         synchronized (this) {
                             nack=false;
                             this.wait();
                         }
                     case ("Wizard"):
                         Wizards choose = this.getWizard(willy);
                         do {
                             server.sendChoice(choose);
                             synchronized (this) {
                                 nack=false;
                                 this.wait();
                             }
                         } while (nack == true);
                         break;
                     case ("ChooseCard"):
                         AssistantCard a;
                         a = this.getAssistantCard(cards);
                         me.draw(a);
                         do {
                             server.sendChosenCard(a);
                             synchronized (this) {
                                 nack=false;
                                 this.wait();
                             }
                         } while (nack == true);
                         break;
                     case ("MoveMN"):
                         int step = this.getNumStep(me);
                         do {
                             server.sendStepsMN(step);

                             synchronized (this) {
                                 nack=false;
                                 this.wait();
                             }
                         } while (nack == true);
                         break;
                     case ("ChooseCloud"):
                         Cloud clo = this.getCloud(clouds);
                         action.chooseCloud(me, clo);
                         do {
                             server.sendChoiceCloud(clo);
                             synchronized (this) {
                                 nack=false;
                                 this.wait();
                             }
                         } while (nack == true);
                     case ("MoveStudents"):
                         Student st;
                         int move;
                         for (int i = 0; i < match.getPlayer().length + 1; i++) {
                             st = this.getStudent(me);
                             move = this.getDestination(match);
                             if (move==12) {
                                 try {
                                     action.moveFromIngressToBoard(me, st);
                                 } catch (Exception e) {
                                 }
                             } else {
                                 action.moveFromIngressToLand(me, st, match.getLands().get(move));
                             }
                             do {
                                 server.sendMovedStudent(st, move);
                             } while (nack == true);
                             printMatch(match);
                         }
                         synchronized (this) {
                             nack=false;
                             this.wait();
                         }
                         break;
                     case ("EndGame"):
                         end = true;
                         break;
                 }
             }
         }catch (InterruptedException e){
             e.printStackTrace();
         }
    }

    public synchronized void wakeUp(String state){
        this.state=state;
        this.nack=false;
        this.notifyAll();
    }

    public synchronized void setNack(){
        nack=true;
        this.notifyAll();
    }

    public void setWilly(List<Wizards> willy) {
        this.willy = willy;
    }

    public void setCards(List<AssistantCard> cards) {
        this.cards = cards;
    }

    public void setClouds(List<Cloud> clouds) {
        this.clouds = clouds;
    }

    @Override
    public void chooseMatch(List<String> join, List<String> resume) {
        String choose;
        if (join.isEmpty()) {
            System.out.println("Non ci sono partite a cui unirsi");
        }
        else {
            System.out.println("puoi unirti alle partite:");
            for (String e : join) {
                System.out.println(e);
            }
        }

        if (resume.isEmpty()) {
            System.out.println("Non ci sono partite da riprendere");
        }
        else {
            System.out.println("puoi riunirti alle partite:");
            for (String e : resume) {
                System.out.println(e);
            }
        }
        System.out.println("scegli la partita(per creare una nuova partita scrivi NewGame):");
        choose=input.nextLine();

        if(choose.toLowerCase().equals("newgame")){
            server.sendChoosingGame("NewGame");
            createMatch();
        }
        else {
            server.sendChoosingGame(choose);
        }
    }

    public void createMatch () {
        int playersNum;
        boolean expert;

        do {
            System.out.println("Da quanti giocatori sara' formata la partita? [2/3]");
            playersNum = input.nextInt();
        } while (playersNum<2 || playersNum>3);
        System.out.println("Creare una partita per esperti? [true/false]");
        expert = input.nextBoolean();
        server.sendNumPlayers(playersNum);
        server.sendExpertMatch(expert);
    }

    public  String chooseLogin(){
        System.out.println("vuoi registrarti?");
        String choose=input.nextLine();
        return choose.toLowerCase();
    }

    public Student chooseStudent(List<Student> student){
        int i=1;
        for (Student e:student) {
            System.out.println("scegli "+i+" per lo studente "+e+"\n");
        }
        input.nextInt();
        return student.get(i-1);
    }

    public Land chooseLand(List<Land> lands){
        int i=1;
        for (Land e:lands) {
            System.out.println("scegli "+i+" per l'isola "+e+"\n");
        }
        input.nextInt();
        return lands.get(i-1);
    }

    public Type_Student chooseColorStudent(){
        System.out.println("scegli un colore di cui non verrà calcolata l'influenza");
        while (true) {
            String choose=input.nextLine();
            switch (choose.toLowerCase()) {
                case ("rosso"):
                    return Type_Student.DRAGON;
                case ("verde"):
                    return Type_Student.FROG;
                case ("blu"):
                    return Type_Student.UNICORN;
                case ("giallo"):
                    return Type_Student.FAIRY;
                case ("rosa"):
                    return Type_Student.GNOME;
            }
        }
    }
    public void playerConnected(String username){
        System.out.println("Si e' connesso "+ username);
    }
    public void playerDisconnected(String username){
        System.out.println("Si e' disconnesso "+ username);
    }

    public void playerDisconnectedAll(){
        System.out.println("Tutti gli altri giocatori si sono disconnessi");
    }

    public void finishedAC(Player p){
        System.out.println(p+" ha finito le carte assistente: ultimo turno");
    }

    public void chooseChCard(CharacterCard[] cards){

    }
}
