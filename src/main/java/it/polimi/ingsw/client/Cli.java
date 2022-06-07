package it.polimi.ingsw.client;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characterCards.*;

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
    private CharacterCard[] characters;
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

    /**
     * request for the username
     * @return username of the player
     */
    public String getUsername(){
        String user;
        System.out.println("inserire username:");
        user=input.nextLine();
        return user;
    }

    /**
     * request of the wizard
     * @param wizards wizard that can be chosen
     * @return the wizard chosen
     */
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

    /**
     * request to choose the cloud
     * @param clouds that can be chosen
     * @return the cloud chosen
     */
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

    /**
     * request to choose the assistant card
     * @param cards that can be chosen
     * @return the card chosen
     */
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

    /**
     * request for the number of steps
     * @param pl the player who want to move mother nature
     * @return the number of step
     */
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

    /**
     * comunicate the player who win the match
     * @param pl player who win
     */
    public void getWinner(Player pl){
        System.out.println("il vincitore della partita è: "+pl.getUserName());
    }

    /**
     * choose of a student in the entrance to be moved
     * @param pl player who have to move the student
     * @return the student chosen
     */
    public Student getStudent(Player pl){
        int i=1,choose;
        System.out.println("scegli uno studente tra: \n");
        for (Student e:pl.getBoard().getEntrance()) {
            System.out.println(e.toString()+'\n');
            i++;
        }
        System.out.println("scegli un numero tra 1 e "+(i-1)+" :");
        choose=input.nextInt();
        while (choose<1 || choose>i){
            System.out.println("scegli un numero tra 1 e "+(i-1)+" :");
            choose=input.nextInt();
        }
        return pl.getBoard().getEntrance().get(choose-1);
    }

    /**
     * request of where move the student
     * @param match match of the player
     * @return int that express where move the student (12 for the board if less than 12 is the id of the land)
     */
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
            }while ((choose<0 || choose>=match.getLands().size()) && choose!=12);
            if(choose!=12)
                return match.getLands().get(i).getID();
            else
                return 12;
        } catch (Exception e) {
            return 12;
        }
    }

    /**
     * show the match
     * @param match match of the player
     */
    public void printMatch(Match match){
        System.out.println("\\033[H\\033[2J");
        getTitolo();
        System.out.println(match.toString()+'\n');
    }

    /**
     * show the turn
     * @param pl player of the turn
     * @param phase phase of the match
     */
    public void printTurn(Player pl,String phase){
        System.out.println("tocca a: "+pl.getUserName()+"in fase di"+phase+"\n");
    }

    /**
     * show that it is the last round
     */
    public void lastRound(){
        System.out.println("sono finiti gli studenti nel sacchetto questo sarà l'ultimo round\n");
    }

    /**
     * request of the number of the players of the match
     * @return the number of the players
     */
    public int getNumPlayer(){
        System.out.println("inserire il numero di giocatori: ");
        int num=input.nextInt();
        while(num<=1 || num>=5){
            System.out.println("inserire il numero di giocatori: ");
            num=input.nextInt();
        }
        return num;
    }

    /**
     * show the title
     */
    public void getTitolo(){
        System.out.println("""
                \u001B[34m\t\t  ___________ ________  ___ __________ ______    _________________    ___ ___________
                \t\t /__________//_______/\\/__//_________//_____/\\  /________________/|  /__//__________/|
                \t\t |__________||   __  \\/|__||   ___   ||     \\ \\ |   ___    ___   ||__|  ||   _______/
                \t\t\t/_______/|  |/_|  |/__/|  |/__|  ||  |\\  \\ \\|  ||  |  ||  |  |/__|  ||  |/___/|
                \t\t\t|   ____||   _   /\\|  ||   ___   ||  ||\\  \\ |  ||  |  ||  |______   ||____   ||
                \t\t\t|  |/____|  ||\\  \\_|  ||  ||  |  ||  || \\  \\|  ||  |  ||  /______|  |/____|  ||
                \t\t\t|___________/  \\__________/   |______/   \\_____/   |__/   |__________________/
                \u001B[0m""");
    }

    /**
     * thread that allows the player to choose his plays
     */
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
                                     input.nextLine();
                                     action.moveFromIngressToBoard(me, st);
                                 } catch (Exception e) {
                                 }
                             } else {
                                 action.moveFromIngressToLand(me, st, match.getLands().get(move));
                             }
                             server.sendMovedStudent(st, move);
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
                     case("Ch"):
                         CharacterCard character=chooseChCard(characters);
                         Board_Experts me_ex=(Board_Experts) me.getBoard();
                         if(character==null){
                             //no carta personaggio
                         }else{
                             if(character.getPrice()>me_ex.getCoinsNumber()){
                                 System.out.println("non hai abbastanza monete");
                                 //no carta personaggio
                             }else{
                                 if(character instanceof Ch_1){
                                     Student student=chooseStudent(((Ch_1) character).getStudents());
                                     Land land= chooseLand(match.getLands());
                                     //messaggio
                                 }else if(character instanceof Ch_2){
                                     //messaggio
                                 }else if(character instanceof Ch_4){
                                     //messaggio
                                 }else if(character instanceof Ch_5){
                                     Land land=chooseLand(match.getLands());
                                     //messaggio
                                 }else if(character instanceof Ch_7){
                                     Student st1=chooseStudent(me.getBoard().getEntrance());
                                     Student st2=chooseStudent(me.getBoard().getEntrance());
                                     Student st3=chooseStudent(me.getBoard().getEntrance());
                                     //messaggio
                                 }else if(character instanceof Ch_10){
                                     for (int i = 0; i < 2; i++) {
                                         Student entrance_student=chooseStudent(me.getBoard().getEntrance());
                                         Type_Student room_student=chooseColorStudent();
                                     }
                                     //messaggio
                                 }else if(character instanceof Ch_11){
                                     Student student=chooseStudent(((Ch_11) character).getStudents());
                                     //messaggio
                                 }else if(character instanceof Ch_12){
                                     Type_Student type=chooseColorStudent();
                                     //messaggio
                                 }
                                 // invio carta personaggio con quello che serve
                             }
                         }
                         synchronized (this) {
                             nack=false;
                             this.wait();
                         }
                         break;
                 }
             }
         }catch (InterruptedException e){
             e.printStackTrace();
         }
    }

    /**
     * wake up the thread from a wait to do a new request
     * @param state request that the thread has to do to the player
     */
    public synchronized void wakeUp(String state){
        this.state=state;
        this.nack=false;
        this.notifyAll();
    }

    /**
     * set nack tru to resend the parameters to the sever
     */
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
        return student.remove(i-1);
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

    public CharacterCard chooseChCard(CharacterCard[] cards){
        for (int i = 0; i < 2; i++) {
            System.out.println(i+')'+characters[i].toString()+'\n');
        }
        System.out.println("vuoi giocare una carta personaggio?");
        String choose=input.next();
        int chosen;
        if(choose.equals("si")){
            do {
                System.out.println("quale delle tre?");
                chosen = input.nextInt();
            }while(chosen<0 || chosen>2);
            return cards[chosen];
        }else{
            return null;
        }
    }

    public void setCharacters(CharacterCard[] characters) {
        this.characters = characters;
    }
}
