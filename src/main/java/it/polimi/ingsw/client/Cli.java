package it.polimi.ingsw.client;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characterCards.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Representation of the game via command line
 */
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
    ProcessBuilder svnProcessBuilder;

    /**
     * Constructor of class Cli: initializes the input Scanner and sets the state to "Start"
     */
    public Cli(){
       input=new Scanner(System.in);
       end=false;
       nack=false;
       state="Start";
       svnProcessBuilder = new ProcessBuilder("PowerShell", "/c", "clear");
    }

    public static void main(String[] args) {
        Cli view = new Cli();
        Client client = new Client(view);
        client.start();
        view.start();
    }

    @Override
    public void setServer(Message4Server server) {
        this.server = server;
    }

    @Override
    public void setMe(Player me) {
        this.me = me;
    }

    @Override
    public void setMatch(Match match) {
        this.match = match;
        action=new Action(match);
    }

    @Override
    public String getUsername(){
        String user;
        while (true) {
            System.out.println("\nInserire username: ");
            user = input.nextLine();
            if (!user.equals(""))
                return user;
        }
    }

    @Override
    public Wizards getWizard(List<Wizards> wizards){
        int choose;

        System.out.println("\nScegli il mago tra:");
        printWizards();
        while (true) {
            try {
                choose = input.nextInt();

                for (Wizards w : willy) {
                    if (choose == w.getOrder())
                        return w;
                }
                System.out.println("Scegli il mago tra quelli che vedi");
            } catch (Exception e) {
                System.out.println("Inserire un valore numerico");
                input.nextLine();
            }
        }
    }

    /**
     * Auxiliary method used in order to print all the available wizards
     * when asking the player to choose a wizard
     */
    private void printWizards() {
        StringBuilder wizards = new StringBuilder();
        for (int i=0; i<5; i++) {
            for (Wizards w : willy) {
                switch (i) {
                    case 0 -> wizards.append(" _____       ");
                    case 1 -> {
                        switch (w) {
                            case WIZARD1 -> wizards.append("|  1  |      ");
                            case WIZARD2 -> wizards.append("|  2  |      ");
                            case WIZARD3 -> wizards.append("|  3  |      ");
                            default -> wizards.append("|  4  |");
                        }
                    }
                    case 2 -> {
                        switch (w) {
                            case WIZARD1 -> wizards.append("| $ $ |      ");
                            case WIZARD2 -> wizards.append("| °|° |      ");
                            case WIZARD3 -> wizards.append("| *_* |      ");
                            default -> wizards.append("| ^ ^ |");
                        }
                    }
                    case 3 -> {
                        switch (w) {
                            case WIZARD1 -> wizards.append("|  () |      ");
                            case WIZARD2 -> wizards.append("|  ~  |      ");
                            case WIZARD3 -> wizards.append("|     |      ");
                            default -> wizards.append("|  o  |");
                        }
                    }
                    default -> wizards.append("|_____|      ");
                }
            }
            wizards.append('\n');
        }

        System.out.println(wizards.toString().indent(12));
    }

    @Override
    public Cloud getCloud(List<Cloud> clouds){
        while (true) {
            Cloud[] c = match.getCloud();
            System.out.println("\nScegli una nuvola");
            try {
                int choose = input.nextInt();

                while (choose < 1 || choose > c.length) {
                    System.out.println("Scegli un numero tra 1 e " + c.length + ":");
                    choose = input.nextInt();
                }
                if (!c[choose-1].hasBeenChosen())
                    return c[choose - 1];
                else
                    System.out.println("Questa nuvola é già stata scelta da un altro giocatore in questo  round");
            }
            catch (Exception e) {
                System.out.println("Inserire un valore numerico");
                input.nextLine();
            }
        }
    }

    @Override
    public AssistantCard getAssistantCard(List<AssistantCard> cards){
        int choose;
        ArrayList<AssistantCard> deck = me.getDeck();
        AssistantCard card;
        boolean correctChoice;

        do {
            System.out.println("\nGioca una carta assistente ");
            card = null;

            try {
                choose = input.nextInt();

                for (AssistantCard c : deck) {
                    if (c.getValue() == choose)
                        card = c;
                }
                if (card != null) {
                    if (cards.contains(card) || cards.isEmpty())
                        correctChoice = true;
                    else {
                        System.out.println("Questa carta é già stata scelta da un altro giocatore in questo round");
                        correctChoice = false;
                    }
                }
                else {
                    System.out.println("Non hai in mano questa carta");
                    correctChoice = false;
                }
            } catch (Exception e) {
                System.out.println("Inserire un valore numerico\n");
                correctChoice = false;
            }
        } while (!correctChoice);
        cards.clear();

        return card;
    }

    @Override
    public int getNumStep(Player pl){
        System.out.println("\nScegli di spostare Madre Natura di? (deve " +
                "essere un numero compreso tra 0 e "+pl.getPlayedCard().getMNSteps());

        while (true) {
            try {
                int step = input.nextInt();
                while (step < 0 || step > pl.getPlayedCard().getMNSteps()) {
                    System.out.println("Di quanti passi scegli di spostare Madre Natura? (deve " +
                            "essere un numero compreso tra 0 e " + pl.getPlayedCard().getMNSteps());
                    step = input.nextInt();
                }
                return step;
            } catch (Exception e) {
                System.out.println("Inserire un valore numerico");
                input.nextLine();
            }
        }
    }

    @Override
    public void getWinner(Player pl){
        System.out.println("Il vincitore della partita è: "+pl.getUserName());
    }

    @Override
    public Student getStudent(Player pl){
        int size = me.getBoard().getEntrance().size();
        int choose;

        while (true) {
            System.out.println("\nScegli uno studente dell'ingresso");

            try {
                choose = input.nextInt();
                while (choose < 1 || choose > size) {
                    System.out.println("Scegli un numero tra 1 e " + size + " :");
                    choose = input.nextInt();
                }
                return pl.getBoard().getEntrance().get(choose - 1);
            } catch (Exception e) {
                System.out.println("Inserire un valore numerico");
                input.nextLine();
            }
        }
    }

    @Override
    public int getDestination(Match match) {
        String value;
        int choose;
        do {
            try {
                do {
                    System.out.println("\nDove vuoi che vada lo studente?");
                    System.out.println("Se vuoi aggiungere lo studente alla sala scrivi 'sala' oppure scegli tra le isole");
                    value = input.nextLine().toLowerCase();

                    if (value.equals("") || value.equals("\n"))
                        value = input.nextLine().toLowerCase();

                    if (value.equals("sala"))
                        return 12;
                    choose = Integer.parseInt(value);
                } while (choose < 0 || choose > 12);
                if (choose >= match.getLands().size())
                    return 12;
                else
                    return choose;
            } catch (Exception e) {
                System.out.println("Inserire un valore numerico o 'sala'");
            }
        } while (true);
    }

    @Override
    public void printMatch(Match match){
        //clearConsole();
        getTitolo();
        System.out.println(match.toString()+printAssistants());
    }

    @Override
    public void printTurn(Player pl,String phase){
        System.out.println("Tocca a "+pl.getColor().toString()+pl.getUserName()+"\u001b[0m in fase di "+phase);
    }

    @Override
    public void lastRound(){
        System.out.println("Sono finiti gli studenti nel sacchetto! Questo sarà l'ultimo round\n");
    }

    @Override
    public void getTitolo(){
        System.out.println("""
                
                \u001B[36m         ___________ ________  ___ __________ ______    _________________    ___ ___________
                        /__________//_______/\\/__//_________//_____/\\  /________________/|  /__//__________/|
                        |__________||   __  \\/|__||   ___   ||     \\ \\ |   ___    ___   ||__|  ||   _______/
                           /_______/|  |/_|  |/__/|  |/__|  ||  |\\  \\ \\|  ||  |  ||  |  |/__|  ||  |/___/|
                           |   ____||   _   /\\|  ||   ___   ||  ||\\  \\ |  ||  |  ||  |______   ||____   ||
                           |  |/____|  ||\\  \\_|  ||  ||  |  ||  || \\  \\|  ||  |  ||  /______|  |/____|  ||
                           |___________/  \\__________/   |______/   \\_____/   |__/   |__________________/
                \u001B[0m""".indent(18));
    }

    /**
     * Auxiliary method used in order to get the String for of the available cards too play
     * @return all the available assistant cards in a human-readable form
     */
    private @NotNull String printAssistants() {
        StringBuilder c = new StringBuilder();
        ArrayList<AssistantCard> deck = me. getDeck();
        Wizards gandalf = me.getWizard();

        for (int i=0; i<6; i++) {
            switch (i) {
                case 0 -> c.append("    _______ ");
                case 1, 4 -> c.append("   |   ").append(gandalf.getOrder()).append("   |");
                case 2 -> {
                    switch (gandalf) {
                        case WIZARD1 -> c.append("   |  $ $  |");
                        case WIZARD2 -> c.append("   |  °|°  |");
                        case WIZARD3 -> c.append("   |  *_*  |");
                        case WIZARD4 -> c.append("   |  ^ ^  |");
                    }
                }
                case 3 -> {
                    switch (gandalf) {
                        case WIZARD1 -> c.append("   |   ()  |");
                        case WIZARD2 -> c.append("   |   ~   |");
                        case WIZARD3 -> c.append("   |       |");
                        case WIZARD4 -> c.append("   |   o   |");
                    }
                }
                default -> c.append("   |_______|");
            }
            c.append("  | ");
            for (AssistantCard card : deck) {
                if (cards != null) {
                    if (!cards.isEmpty())
                        if (!cards.contains(card))
                            c.append("\u001b[30;1m");
                }

                switch (i) {
                    case 0 -> c.append("  _______  ");
                    case 1 -> {
                        c.append(" | ").append(card.getValue());

                        if (card.getValue() < 10)
                            c.append(" ");
                        c.append("  ").append(card.getMNSteps()).append(" | ");
                    }
                    case 2 -> c.append(" |  /\\\\  | ");
                    case 3 -> c.append(" | //_\\\\ | ");
                    case 4 -> c.append(" |//   \\\\| ");
                    default -> c.append(" |_______| ");
                }
                c.append("\u001b[0m");
            }
            c.append(" | ");

            if (me.getPlayedCard() != null) {
                c.append("\u001b[36m");

                switch (i) {
                    case 0 -> c.append("  _______  ");
                    case 1 -> {
                        c.append(" | ").append(me.getPlayedCard().getValue());

                        if (me.getPlayedCard().getValue() < 10)
                            c.append(" ");
                        c.append("  ").append(me.getPlayedCard().getMNSteps()).append(" | ");
                    }
                    case 2 -> c.append(" |  /\\\\  |");
                    case 3 -> c.append(" | //_\\\\ |");
                    case 4 -> c.append(" |//   \\\\|");
                    default -> c.append(" |_______|");
                }
                c.append("\u001b[0m");
            }
            c.append("\n");
        }

        return c.toString();
    }

    /**
     * thread that allows the player to choose his plays
     */
    @Override
    public void run() {
         try {
             while (!end) {
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
                                 System.out.println("Aspettiamo che si connettano gli altri giocatori...");
                                 this.wait();
                             }
                         } while (nack);
                         break;
                     case ("ChooseCard"):
                         AssistantCard a;
                         printMatch(match);
                         a = this.getAssistantCard(cards);
                         me.draw(a);
                         System.out.println("Carta scelta:"+a.toString());
                         do {
                             server.sendChosenCard(a);
                             synchronized (this) {
                                 nack=false;
                                 this.wait();
                             }
                         } while (nack);
                         break;
                     case ("MoveMN"):
                         printMatch(match);
                         int step = this.getNumStep(me);
                         do {
                             server.sendStepsMN(step);

                             synchronized (this) {
                                 nack=false;
                                 this.wait();
                             }
                         } while (nack);
                         break;
                     case ("ChooseCloud"):
                         Cloud clo = this.getCloud(clouds);
                         printMatch(match);
                         System.out.println("Nuvola scelta:\n"+ clo.toString());
                         action.chooseCloud(me, clo);
                         do {
                             server.sendChoiceCloud(clo.clone());
                             for (Cloud e:match.getCloud()) {
                                 if(e.equals(clo)){
                                     e.clearStudents();
                                 }
                             }
                             synchronized (this) {
                                 nack=false;
                                 this.wait();
                             }
                         } while (nack);
                         printMatch(match);
                         break;
                     case ("MoveStudents"):
                         Student st;
                         int move;
                         for (int i = 0; i < match.getPlayer().length + 1; i++) {
                             st = this.getStudent(me);
                             move = this.getDestination(match);
                             if (move==12) {
                                 try {
                                     action.moveFromIngressToBoard(me, st);
                                     server.sendMovedStudent(st, 12);
                                 } catch (Exception e) {
                                     throw new RuntimeException();
                                 }
                             } else {
                                 int pos = 0;
                                 for (int j=0; j<match.getLands().size(); j++) {
                                     pos += match.getLands().get(j).size();
                                     if (pos >= move) {
                                         move = j;
                                         break;
                                     }
                                 }
                                 action.moveFromIngressToLand(me, st, match.getLands().get(move));
                                 server.sendMovedStudent(st, match.getLands().get(move).getID());
                             }
                             action.checkAllProfessors();
                             printMatch(match);
                         }
                         synchronized (this) {
                             nack=false;
                             this.wait();
                         }
                         break;
                     case ("EndGame"):
                         end = true;
                         System.out.println("Grazie di aver giocato a Eriantys. Premere qualsiasi tasto per terminare");
                         state = input.next();
                         svnProcessBuilder = new ProcessBuilder("PowerShell", "/c", "exit");
                         svnProcessBuilder.inheritIO().start().waitFor();
                         break;
                     case("Ch"):
                         CharacterCard character=chooseChCard(characters);
                         Board_Experts me_ex=(Board_Experts) me.getBoard();
                         if(character != null ) {
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
         }catch (InterruptedException e) {
             e.printStackTrace();
         } catch (IOException e) {
             throw new RuntimeException(e);
         }
    }

    @Override
    public synchronized void wakeUp(String state){
        this.state=state;
        this.nack=false;
        this.notifyAll();
    }

    @Override
    public synchronized void setNack(){
        nack=true;
        this.notifyAll();
    }

    /**
     * Sets the list of available wizards
     * @param willy
     */
    @Override
    public void setWilly(List<Wizards> willy) {
        this.willy = willy;
    }

    /**
     * Sets the lists of available assistants cards to play
     * @param cards
     */
    @Override
    public void setCards(List<AssistantCard> cards) {
        this.cards = cards;
    }

    /**
     * sets the list of available clouds to choose between
     * @param clouds
     */
    @Override
    public void setClouds(List<Cloud> clouds) {
        this.clouds = clouds;
    }

    /**
     * Asks the user to choose between joining, resuming o creating a new match
     * @param join the list of matches the player can join
     * @param resume the list of matches the player can resume
     */
    @Override
    public void chooseMatch(List<String> join, List<String> resume) {
        String choose;

        if (join.isEmpty()) {
            System.out.println("\n    Non ci sono partite a cui unirsi");
        } else {
            System.out.println("\nPuoi unirti alle partite create da: ");
            for (String e : join) {
                System.out.println(">    "+e);
            }
        }

        if (resume.isEmpty()) {
            System.out.println("\n    Non ci sono partite da riprendere");
        } else {
            System.out.println("\nPuoi riunirti alle partite create da:");
            for (String e : resume) {
                System.out.println(">    "+e);
            }
        }

        do {
            System.out.println("\nScegli la partita (oppure per creare una nuova partita scrivi NewGame):");
            choose = input.nextLine();

            if (choose.equalsIgnoreCase("newgame")) {
                server.sendChoosingGame("NewGame");
                createMatch();
            } else {
                if (join.contains(choose) || resume.contains(choose))
                    server.sendChoosingGame(choose);
                else
                    System.out.println("Non ci sono partite create da "+choose);
            }
        } while (!join.contains(choose) && !resume.contains(choose) && !choose.equalsIgnoreCase("newgame"));
    }

    /**
     * If the player chooses to create a match this method asks them how many players there will be
     * and if they want to create an expert match
     */
    public void createMatch () {
        int playersNum = 0;
        String expert;
        while (playersNum == 0) {
            try {
                do {
                    System.out.println("\nDa quanti giocatori sara' formata la partita? [2/3]");
                    playersNum = input.nextInt();
                } while (playersNum < 2 || playersNum > 3);
            } catch (Exception e) {
                System.out.println("Inserire un valore numerico");
                input.nextLine();
            }
        }

        do {
            System.out.println("\nCreare una partita per esperti? [si/no]");
            expert = input.next();
        } while (!expert.equalsIgnoreCase("si") && !expert.equalsIgnoreCase("no"));
        server.sendNumPlayers(playersNum);
        server.sendExpertMatch(expert.equalsIgnoreCase("si"));
    }

    /**
     * Asks the player if they want to register and then the username
     * @return the username
     */
    @Override
    public  String chooseLogin(){
        String choose;
        do {
            System.out.println("Vuoi registrarti?");
            choose = input.nextLine();
        } while (!choose.equals("si") && !choose.equals("no"));
        return choose.toLowerCase();
    }

    /**
     * Asks the player to choose a student to move from the entrance (used when playing a character card)
     * @param student the entrance
     * @return the chosen student
     */
    @Override
    public Student chooseStudent(List<Student> student) {
        int i;

        while (true){
            try {
                i = input.nextInt();
                System.out.println("Scegli uno studente dall'ingresso");
                return student.remove(i - 1);
            } catch (Exception e) {
                System.out.println("Inserire un valore numerico");
                input.nextLine();
            }
        }
    }

    /**
     * Asks the player to choose a land to put the student on
     * @param lands all the lands in the sky
     * @return the chosen land
     */
    @Override
    public Land chooseLand(List<Land> lands){
        int i, c;
        System.out.println("Scegli un'isola");
        while (true) {
            try {
                i = input.nextInt();
                if (i<=0 || i>11)
                    System.out.println("Inserire un numero tra 1 e 12");
                else {
                    c = 0;
                    for (Land l : lands) {
                        c += l.size();

                        if (c >= i)
                            return l.getIslands().get(c-i);
                    }
                }
            } catch (Exception e) {
                System.out.println("Inserire un valore numerico");
                input.nextLine();
            }
        }
    }

    /**
     * Asks the player to choose a color of which not to compute influence on a land
     * @return
     */
    @Override
    public Type_Student chooseColorStudent(){
        System.out.println("Scegli un colore di cui non verrà calcolata l'influenza");
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

    /**
     * Notifies the player if another one connects to the match
     * @param username the username of the connected player
     */
    @Override
    public void playerConnected(String username){
        System.out.println("Si e' connesso "+ username);
    }

    /**
     * Notifies the player if another one disconnects from the match
     * @param username the username of the disconnected player
     */
    @Override
    public void playerDisconnected(String username){
        System.out.println("Si e' disconnesso "+ username);
    }

    /**
     * Notifies the player if all the other players are disconnected from the match
     */
    @Override
    public void playerDisconnectedAll(){
        System.out.println("Tutti gli altri giocatori si sono disconnessi");
    }

    /**
     * Notifies the player if another player finished their assistant cards
     * @param p the player who finished their assistants
     */
    @Override
    public void finishedAC(Player p){
        System.out.println(p+" ha finito le carte assistente: ultimo turno!");
    }

    /**
     * Asks the user if they want to play a character card
     * @param cards the available character cards
     * @return the chosen card
     */
    @Override
    public CharacterCard chooseChCard(CharacterCard[] cards){
        StringBuilder chCards = new StringBuilder();

        for (int j=0; j<6; j++) {
            for (int i = 0; i < 2; i++) {
                switch (j) {
                    case 0 -> chCards.append("       _______ ");
                    case 1 -> chCards.append("      | |‾‾‾| |");
                    case 2 -> chCards.append("      | |___| |");
                    case 3 -> chCards.append("      | |     |");
                    case 4 -> {
                        chCards.append("      | |  ").append(cards[i].getPrice());

                        if (cards[i].getPrice() < 10)
                            chCards.append(" ");
                        chCards.append(" |");
                    }
                    default -> chCards.append("      |_______|");
                }
                chCards.append('\n');
            }
        }
        System.out.println("\n" + chCards.toString().indent(20));
        System.out.println("\nVuoi giocare una carta personaggio? [si/no]");
        System.out.println("Per visualizzare la descrizione dell'effetto della carta scrivi 'info'");
        String choose=input.next();
        int chosen;
        while (true) {
            switch (choose.toLowerCase()) {
                case "si" -> {
                    do {
                        System.out.println("\nQuale delle tre?");
                        chosen = input.nextInt();
                    } while (chosen < 0 || chosen > 2);
                    return cards[chosen];
                }
                case "no" -> {
                    return null;
                }
                case "info" -> {
                    for (CharacterCard card : cards) {
                        System.out.println("\nPersonaggio 1 :\n\t" + card.getPowerUp());
                    }
                }
                default -> System.out.println("Inserisci si/no oppure info");
            }
        }
    }

    /**
     * Sets the available character cards
     * @param characters
     */
     @Override
    public void setCharacters(CharacterCard[] characters) {
        this.characters = characters;
    }

    /**
     * Clears the console screen
     */
    private void clearConsole(){
        if (System.getProperty("os.name").contains("Windows")) {
            try {
                svnProcessBuilder.inheritIO().start().waitFor();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        else {
            System.out.println("\033[H\033[2J");
            System.out.flush();
        }
    }

    @Override
    public void printNotification(String message) {
        System.out.println('\n' + message);
    }

}