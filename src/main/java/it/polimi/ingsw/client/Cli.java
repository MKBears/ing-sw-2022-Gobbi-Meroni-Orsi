package it.polimi.ingsw.client;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characterCards.*;

import java.io.IOException;
import java.util.*;
import java.util.List;

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
    private String card;

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
        while (true) {
            System.out.println("Inserire username:");
            user = input.nextLine();
            if (!user.equals(""))
                return user;
        }
    }

    /**
     * request of the wizard
     * @param wizards wizard that can be chosen
     * @return the wizard chosen
     */
    public Wizards getWizard(List<Wizards> wizards){
        int choose;

        System.out.println("Scegli il mago tra:");
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
            }
        }
    }

    private void printWizards() {
        StringBuilder wizards = new StringBuilder();
        for (int i=0; i<5; i++) {
            for (Wizards w : willy) {
                switch (i) {
                    case 0 -> wizards.append(" _____   ");
                    case 1 -> {
                        switch (w) {
                            case WIZARD1 -> wizards.append("|  1  |  ");
                            case WIZARD2 -> wizards.append("|  2  |  ");
                            case WIZARD3 -> wizards.append("|  3  |  ");
                            default -> wizards.append("|  4  |");
                        }
                    }
                    case 2 -> {
                        switch (w) {
                            case WIZARD1 -> wizards.append("| $ $ |  ");
                            case WIZARD2 -> wizards.append("| °|° |  ");
                            case WIZARD3 -> wizards.append("| *_* |  ");
                            default -> wizards.append("| ^ ^ |");
                        }
                    }
                    case 3 -> {
                        switch (w) {
                            case WIZARD1 -> wizards.append("|  () |  ");
                            case WIZARD2 -> wizards.append("|  ~  |  ");
                            case WIZARD3 -> wizards.append("|     |  ");
                            default -> wizards.append("|  o  |");
                        }
                    }
                    default -> wizards.append("|_____|  ");
                }
            }
            wizards.append('\n');
        }

        System.out.println(wizards.toString().indent(12));
    }

    /**
     * request to choose the cloud
     * @param clouds that can be chosen
     * @return the cloud chosen
     */
    public Cloud getCloud(List<Cloud> clouds){
        while (true) {
            Cloud[] c = match.getCloud();
            System.out.println("Scegli la nuvola tra: \n");
            try {
                int choose = input.nextInt();

                while (choose < 1 || choose > c.length) {
                    System.out.println("Scegli un numero tra 1 e " + c.length + ":");
                    choose = input.nextInt();
                }
                if (clouds.contains(c[choose - 1]))
                    return c[choose - 1];
                else
                    System.out.println("Questa nuvola é già stata scelta da un altro giocatore in questo  round");
            }
            catch (Exception e) {
                System.out.println("Inserire un valore numerico");
            }
        }
    }

    /**
     * request to choose the assistant card
     * @param cards that can be chosen
     * @return the card chosen
     */
    public AssistantCard getAssistantCard(List<AssistantCard> cards){
        int choose = 1;
        ArrayList<AssistantCard> deck = me.getDeck();
        AssistantCard card;
        boolean correctChoice;

        do {
            System.out.println("Scegli la carta assistente \n");
            card = null;

            try {
                choose = input.nextInt();

                for (AssistantCard c : deck) {
                    if (c.getValue() == choose)
                        card = c;
                }
                if (card != null) {
                    if (cards.contains(deck.get(choose - 1)))
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

        return deck.get(choose-1);
    }

    /**
     * request for the number of steps
     * @param pl the player who want to move mother nature
     * @return the number of step
     */
    public int getNumStep(Player pl){
        System.out.println("Scegli di spostare Madre Natura di? (deve " +
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
            }
        }
    }

    /**
     * comunicate the player who win the match
     * @param pl player who win
     */
    public void getWinner(Player pl){
        System.out.println("Il vincitore della partita è: "+pl.getUserName());
    }

    /**
     * choose of a student in the entrance to be moved
     * @param pl player who have to move the student
     * @return the student chosen
     */
    public Student getStudent(Player pl){
        int size = me.getBoard().getEntrance().size();
        int choose;

        while (true) {
            System.out.println("Scegli uno studente nell'ingresso\n");

            try {
                choose = input.nextInt();
                while (choose < 1 || choose >= size) {
                    System.out.println("Scegli un numero tra 1 e " + size + " :");
                    choose = input.nextInt();
                }
                return pl.getBoard().getEntrance().get(choose - 1);
            } catch (Exception e) {
                System.out.println("Inserire un valore numerico");
            }
        }
    }

    /**
     * request of where move the student
     * @param match match of the player
     * @return int that express where move the student (12 for the board if less than 12 is the id of the land)
     */
    public int getDestination(Match match) {
        String value;
        int choose;
        System.out.println("Dove vuoi che vada lo studente?\n");
        System.out.println("Se si vuole aggiungere alla sala scrivi sala oppure scegli tra le isole\n");
        do {
            try {
                do {
                    System.out.println("Inserire scelta: ");
                    value = input.next().toLowerCase();

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

    /**
     * show the match
     * @param match match of the player
     */
    public void printMatch(Match match){
        clearConsole();
        getTitolo();
        System.out.println(match.toString()+printAssistants());
    }

    /**
     * show the turn
     * @param pl player of the turn
     * @param phase phase of the match
     */
    public void printTurn(Player pl,String phase){
        System.out.println("Tocca a: "+pl.getUserName()+" in fase di "+phase+"\n");
    }

    /**
     * show that it is the last round
     */
    public void lastRound(){
        System.out.println("Sono finiti gli studenti nel sacchetto! Questo sarà l'ultimo round\n");
    }

    /**
     * request of the number of the players of the match
     * @return the number of the players
     */
    public int getNumPlayer(){
        System.out.println("Inserire il numero di giocatori: ");
        while (true) {
            try {
                int num = input.nextInt();
                while (num <= 1 || num >= 4) {
                    System.out.println("Inserire il numero di giocatori: ");
                    num = input.nextInt();
                }
                return num;
            } catch (Exception e) {
                System.out.println("Inserire un valore numerico");
            }
        }
    }

    /**
     * show the title
     */
    public void getTitolo(){
        System.out.println("""
                \u001B[36m         ___________ ________  ___ __________ ______    _________________    ___ ___________
                        /__________//_______/\\/__//_________//_____/\\  /________________/|  /__//__________/|
                        |__________||   __  \\/|__||   ___   ||     \\ \\ |   ___    ___   ||__|  ||   _______/
                           /_______/|  |/_|  |/__/|  |/__|  ||  |\\  \\ \\|  ||  |  ||  |  |/__|  ||  |/___/|
                           |   ____||   _   /\\|  ||   ___   ||  ||\\  \\ |  ||  |  ||  |______   ||____   ||
                           |  |/____|  ||\\  \\_|  ||  ||  |  ||  || \\  \\|  ||  |  ||  /______|  |/____|  ||
                           |___________/  \\__________/   |______/   \\_____/   |__/   |__________________/
                \u001B[0m""".indent(10));
    }

    private String printAssistants() {
        StringBuilder c = new StringBuilder();
        ArrayList<AssistantCard> deck = me. getDeck();

        for (int i=0; i<6; i++) {
            for (AssistantCard card : deck) {
                if (cards != null) {
                    if (!cards.contains(card))
                        c.append("\u001b[30;1m");
                }

                switch (i) {
                    case 0 -> c.append("  _______  ");
                    case 1 -> {
                        c.append(" | ").append(card.getValue());

                        if (card.getValue() < 10)
                            c.append(" ");
                        c.append(card.getMNSteps()).append(" | ");
                    }
                    case 2 -> c.append(" |  /\\\\  | ");
                    case 3 -> c.append(" | //_\\\\ | ");
                    case 4 -> c.append(" |//   \\\\| ");
                    default -> c.append(" |_______| ");
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
                                 this.wait();
                             }
                         } while (nack);
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
                         } while (nack);
                         break;
                     case ("MoveMN"):
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
                         System.out.println("Nuvola scelta:\n"+ clo.toString());
                         action.chooseCloud(me, clo);
                         do {
                             server.sendChoiceCloud(clo.clone());
                             for (Cloud e:match.getCloud()) {
                                 if(e.equals(clo)){
                                     e.clearStudents();
                                 }
                             }
                             printMatch(match);
                             synchronized (this) {
                                 nack=false;
                                 this.wait();
                             }
                         } while (nack);
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
                                 } catch (Exception e) {}
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
                         break;
                     case("Ch"):
                         CharacterCard character=chooseChCard(characters);
                         Board_Experts me_ex=(Board_Experts) me.getBoard();
                         if(character==null){
                             server.sendNoCh();
                         }else{
                             if(character.getPrice()>me_ex.getCoinsNumber()){
                                 System.out.println("non hai abbastanza monete");
                                 server.sendNoCh();
                             }else{
                                 if(character instanceof Ch_1){
                                     System.out.println("scegli uno studente da mettere in un'isola\n");
                                     Student student=chooseStudent(((Ch_1) character).getStudents());
                                     Land land= chooseLand(match.getLands());
                                     server.sendChooseCh1(student,land);
                                 }else if(character instanceof Ch_2){
                                     server.sendChooseCh2();
                                 }else if(character instanceof Ch_4){
                                     server.sendChooseCh4();
                                     character.setPlayer(me);
                                 }else if(character instanceof Ch_5){
                                     System.out.println("scegli l'isola su cui mettere il divieto\n");
                                     Land land=chooseLand(match.getLands());
                                     server.sendChooseCh5(land);
                                 }else if(character instanceof Ch_10){
                                     ArrayList<Student> students=new ArrayList<>();
                                     ArrayList<Type_Student> type_students=new ArrayList<>();
                                     for (int i = 0; i < 2; i++) {
                                         System.out.println("scegli uno studente da sostituire con uno della tua sala da pranzo\n");
                                         students.add(chooseStudent(me.getBoard().getEntrance()));
                                         card="Ch_10";
                                         type_students.add(chooseColorStudent());
                                     }
                                     server.sendChooseCh10(students,type_students);
                                 }else if(character instanceof Ch_11){
                                     System.out.println("scegli uno studente dalla carta da piazzare nella tua sala da pranzo\n");
                                     Student student=chooseStudent(((Ch_11) character).getStudents());
                                     server.sendChooseCh11(student);
                                 }else if(character instanceof Ch_12){
                                     card="Ch_12";
                                     Type_Student type=chooseColorStudent();
                                     server.sendChooseCh12(type);
                                 }else if(character instanceof Ch_8){
                                     server.sendChooseCh8();
                                 }
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
        } else {
            System.out.println("Puoi unirti alle seguenti partite: ");
            for (String e : join) {
                System.out.println(e);
            }
        }

        if (resume.isEmpty()) {
            System.out.println("Non ci sono partite da riprendere");
        } else {
            System.out.println("Puoi riunirti alle seguenti partite:");
            for (String e : resume) {
                System.out.println(e);
            }
        }

        do {
            System.out.println("Scegli la partita (oppure per creare una nuova partita scrivi NewGame):");
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

    public void createMatch () {
        int playersNum = 0;
        String expert;
        while (playersNum == 0) {
            try {
                do {
                    System.out.println("Da quanti giocatori sara' formata la partita? [2/3]");
                    playersNum = input.nextInt();
                } while (playersNum < 2 || playersNum > 3);
            } catch (Exception e) {
                System.out.println("Inserire un valore numerico");
            }
        }

        do {
            System.out.println("Creare una partita per esperti? [si/no]");
            expert = input.next();
        } while (!expert.equalsIgnoreCase("si") && !expert.equalsIgnoreCase("no"));
        server.sendNumPlayers(playersNum);
        server.sendExpertMatch(expert.equalsIgnoreCase("si"));
    }

    public  String chooseLogin(){
        String choose;
        do {
            System.out.println("Vuoi registrarti?");
            choose = input.nextLine();
        } while (!choose.equals("si") && !choose.equals("no"));
        return choose.toLowerCase();
    }

    public Student chooseStudent(List<Student> student) {
        int i;

        while (true){
            try {
                i = input.nextInt();
                System.out.println("Scegli uno studente dall'ingresso");
                return student.remove(i - 1);
            } catch (Exception e) {
                System.out.println("Inserire un valore numerico");
            }
        }
    }

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
            }
        }
    }

    public Type_Student chooseColorStudent(){
        if(card.equals("Ch_10")) {
            System.out.println("Scegli un colore da sostituire con lo studente dell'entrata\n");
            while (true) {
                String choose = input.nextLine();
                switch (choose.toLowerCase()) {
                    case ("rosso"):
                        if (me.getBoard().getStudentsOfType(Type_Student.DRAGON) > 0)
                            return Type_Student.DRAGON;
                        break;
                    case ("verde"):
                        if (me.getBoard().getStudentsOfType(Type_Student.FROG) > 0)
                            return Type_Student.FROG;
                        break;
                    case ("blu"):
                        if (me.getBoard().getStudentsOfType(Type_Student.UNICORN) > 0)
                            return Type_Student.UNICORN;
                        break;
                    case ("giallo"):
                        if(me.getBoard().getStudentsOfType(Type_Student.GNOME) > 0)
                            return Type_Student.GNOME;
                        break;
                    case ("rosa"):
                        if(me.getBoard().getStudentsOfType(Type_Student.FAIRY) > 0)
                            return Type_Student.FAIRY;
                        break;
                }
            }
        }else{
            System.out.println("Scegli un colore a cui togliere a tutti tre studenti\n");
            while (true) {
                String choose = input.nextLine();
                switch (choose.toLowerCase()) {
                    case ("rosso"):
                        return Type_Student.DRAGON;
                    case ("verde"):
                        return Type_Student.FROG;
                    case ("blu"):
                        return Type_Student.UNICORN;
                    case ("giallo"):
                        return Type_Student.GNOME;
                    case ("rosa"):
                        return Type_Student.FAIRY;
                }
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
        System.out.println(p+" ha finito le carte assistente: ultimo turno!");
    }

    public CharacterCard chooseChCard(CharacterCard[] cards){
        for (int i = 0; i < 2; i++) {
            System.out.println(i+")  "+characters[i].toString()+'\n');
        }
        System.out.println("Vuoi giocare una carta personaggio? [si/no] il tio numero di monete è "+((Board_Experts)me.getBoard()).getCoinsNumber());
        String choose=input.next();
        int chosen;
        if(choose.equals("si")){
            do {
                System.out.println("Quale delle tre?");
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

    public synchronized String state() {
        return state;
    }

    public void printNotifications(String s){
        System.out.println(s+"\n");
    }
}
