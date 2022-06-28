package it.polimi.ingsw.serverController;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.model.*;

/**
 * This class contains all the possible message to send to the client
 */
public class Message4Client extends Thread {  //METTI DENTRO RUN DEL PING

    private final ObjectOutputStream out;
    private String name;
    private boolean condition;

    public Message4Client(Socket socket) throws IOException {
        out = new ObjectOutputStream(socket.getOutputStream());
    }

    /**
     * Base positive response for a client request (the message received was correct)
     */
    public void sendACK() {
        synchronized (this) {
            name = "ACK";
            try {
                out.writeObject(name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Base negative response for a client request (the message received was not correct)
     */
    public void sendNACK() {
        synchronized (this) {
            name = "NACK";
            try {
                out.writeObject(name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * When the username is correct
     */
    public void sendLoginSucceeded() {
        synchronized (this) {
            name = "LoginSucceeded";
            try {
                out.writeObject(name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * When the username is incorrect
     */
    public void sendLoginFailed() {
        synchronized (this) {
            name = "LoginFailed";
            try {
                out.writeObject(name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * When the login is successful
     *
     * @param joinGames   a list of games that is possible to join
     * @param resumeGames a list of games that is possible to resume
     */
    public void sendListOfGames(ArrayList<String> joinGames, ArrayList<String> resumeGames) {
        synchronized (this) {
            name = "ListOfGames";
            try {
                out.writeObject(name);
                out.writeObject(joinGames);
                out.writeObject(resumeGames);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * It sends the client all the actual match (initialized)
     *
     * @param match the actual match
     */
    public void sendCreation(Match match) {
        synchronized (this) {
            name = "Creation";
            try {
                out.writeObject(name);
                out.writeObject(match);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Asks the client which wizard wants to choose
     *
     * @param wizards a list of the possible wizard to choose
     */
    public void sendWizard(ArrayList<Wizards> wizards) { //ritorna il wizard scelto
        synchronized (this) {
            name = "Wizard";
            try {
                out.writeObject(name);
                out.writeObject(wizards);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server sends the students to refill the clouds
     *
     * @param newClouds list of the new clouds full of new students
     */
    public void sendRefillClouds(Cloud[] newClouds) {
        synchronized (this) {
            name = "RefillClouds";

            try {
                out.writeObject(name);
                for (Cloud c : newClouds)
                    out.writeObject(c.getStudents());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server asks the client to choose the assistant card
     *
     * @param cards list of the possible card to choose
     */
    public void sendChooseCard(ArrayList<AssistantCard> cards) {
        synchronized (this) {
            name = "ChooseCard";
            try {
                out.writeObject(name);
                out.writeObject(cards);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server asks the client to tell him where the player decides to move the students
     */
    public void sendMoveStudents() {
        synchronized (this) {
            name = "MoveStudents";
            try {
                out.writeObject(name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server asks the client the cloud that chooses the player
     *
     * @param clouds the possible clouds to chose
     */
    public void sendChooseCloud(ArrayList<Cloud> clouds) {
        synchronized (this) {
            name = "ChooseCloud";
            try {
                out.writeObject(name);
                out.writeObject(clouds);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server notifies to all the clients the chosen card
     *
     * @param card   the chosen card
     * @param player the player that chose the card
     */
    public void sendNotifyChosenCard(AssistantCard card, Player player) {
        synchronized (this) {
            name = "NotifyChosenCard";
            try {
                out.writeObject(name);
                out.writeObject(card);
                out.writeObject(player);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server asks the client the steps the player decides for Mother Nature
     */
    public void sendMoveMN() {
        synchronized (this) {
            name = "MoveMN";
            try {
                out.writeObject(name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server notifies to all the clients the movement of someone's students, we have to call it for every student moved
     *
     * @param student  the signle student
     * @param username the username of the player that moves the students
     * @param id       the id of the island or archipelago
     */
    public void sendNotifyMoveStudent(Student student, int id, String username) {
        synchronized (this) {
            name = "NotifyMoveStudents (id)";
            try {
                out.writeObject(name);
                out.writeObject(student);
                out.writeObject(id);
                out.writeObject(username);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server notifies to all the clients the movement of someone's students, we have to call it for every student moved
     *
     * @param student  the single student
     * @param username the username of the player that moves the students
     */
    public void sendNotifyMoveStudent(Student student, String username) {
        synchronized (this) {
            name = "NotifyMoveStudents (board)";
            try {
                out.writeObject(name);
                out.writeObject(student);
                out.writeObject(username);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server notifies to all the clients the new position of Mother Nature
     *
     * @param steps the number of steps of Mother Nature
     * @param lands an update of the situation of the lands
     */
    public void sendNotifyMovementMN(int steps, ArrayList<Land> lands) {
        synchronized (this) {
            name = "NotifyMovementMN";
            try {
                out.writeObject(name);
                out.writeObject(steps);
                out.writeObject(lands);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server notifies to all the clients the changes of the professors situation
     *
     * @param professors the professors of the board
     */
    public void sendNotifyProfessors(Map<Type_Student, Player> professors) {
        synchronized (this) {
            name = "NotifyProfessors";
            try {
                out.writeObject(name);
                out.writeObject(professors);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server notifies to all the clients the cloud chosen by a player
     *
     * @param player the player that chose
     * @param cloud  the chosen cloud
     */
    public void sendNotifyChosenCloud(Player player, Cloud cloud) {
        synchronized (this) {
            name = "NotifyChosenCloud";
            try {
                out.writeObject(name);
                out.writeObject(player);
                out.writeObject(cloud);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server notifies the clients the situation of the towers in the game
     *
     * @param towers
     * @param land   the land involved
     */
    public void sendNotifyTowers(ArrayList<Tower> towers, Land land, String username) {
        synchronized (this) {
            name = "NotifyTowers (land)";
            System.out.println("mando NOTIFYTOWERS");
            try {
                out.writeObject(name);
                out.writeObject(towers);
                out.writeObject(land);
                out.writeObject(username);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server notifies the clients the situation of the towers in the game
     */
    public void sendNotifyTowers(String username) {
        synchronized (this) {
            name = "NotifyTowers (board)";
            try {
                out.writeObject(name);
                out.writeObject(username);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server notifies to all the clients the end of the game
     *
     * @param winner      the winner player
     * @param explanation the string that contain the explaination of the winning
     * @param gameRecap   the recap of players, colors, wizards, built towers and controlled professors
     */
    public void sendEndGame(Player winner, String
            explanation, GameRecap gameRecap) {
        synchronized (this) {
            name = "EndGame";
            try {
                out.writeObject(name);
                out.writeObject(winner);
                out.writeObject(explanation);
                out.writeObject(gameRecap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server notifies to the players that someone has on his board the last tower
     *
     * @param player the involved player
     */
    public void sendLastTower(Player player) {
        synchronized (this) {
            name = "LastTower";
            try {
                out.writeObject(name);
                out.writeObject(player);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server notifies the players that there are no more students
     */
    public void sendNoMoreStudents() {
        synchronized (this) {
            name = "NoMoreStudents";
            try {
                out.writeObject(name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server notifies the players the one ho has to begin his turn
     *
     * @param player the palyer that begins the turn
     * @param turn
     */
    public void sendNextTurn(Player player, String turn) {
        synchronized (this) {
            name = "NextTurn";
            try {
                out.writeObject(name);
                out.writeObject(player);
                out.writeObject(turn);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void run() {
        condition = true;

        while (condition) {
            try {
                sleep(4000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                synchronized (this) {
                    out.writeObject("Ping");
                }
            } catch (IOException e) {
                    sendGenericError("Internal server error");
            }
        }
        try {
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * The server notifies all the clients if another player is connected or is disconnected
     *
     * @param username  the username of the player
     * @param connected true if connected, false if disconnected
     */
    public void sendNotifyPlayerConnected(String username, boolean connected) {
        synchronized (this) {
            name = "NotifyPlayerConnected";
            try {
                out.writeObject(name);
                out.writeObject(username);
                out.writeObject(connected);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server notifies to the client that all the other players are disconnected
     */
    public void sendNotifyAllPlayersDisconnected() {
        synchronized (this) {
            name = "NotifyAllPlayersDisconnected";
            try {
                out.writeObject(name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server notifies to all the client that a player finished his assistant card
     *
     * @param player the player that finished the assistant cards
     */
    public void sendFinishedAssistants(Player player) {
        synchronized (this) {
            name = "FinishedAssistants";
            try {
                out.writeObject(name);
                out.writeObject(player);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * It sends an error message to the client
     * @param error specifies the type of error
     */
    public void sendGenericError(String error) {
        try {
            out.writeObject("GenericError");
            out.writeObject(error);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends the CharacterCards to the client in order to make him choose which one he wants to use
     * @param ch the cards
     */
    public void sendCh(CharacterCard[] ch){
        try {
            out.writeObject("Ch");
            out.writeObject(ch.clone());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Closes the connection with the client
     */
    public void halt() {
        condition = false;
    }

    /**
     * notification of played character card 1
     * @param land land to move the student
     * @param students new students of the card
     * @param student student moved
     * @param username who moved the student
     */
    public void sendNotifyCh_1(Land land,List<Student> students, Student student,String username) {
        synchronized (this) {
            name = "NotifyCh_1";
            try {
                out.writeObject(name);
                out.writeObject(land);
                out.writeObject(students);
                out.writeObject(student);
                out.writeObject(username);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * notify changes of the card 2
     * @param professors change on the professors
     * @param username who played the card
     */
    public void sendNotifyCh_2(Map<Type_Student,Player> professors,String username) {
        synchronized (this) {
            name = "NotifyCh_2";
            try {
                out.writeObject(name);
                out.writeObject(professors);
                out.writeObject(username);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * notify played the card 4
     * @param username who played the card
     */
    public void sendNotifyCh_4(String username) {
        synchronized (this) {
            name = "NotifyCh_4";
            try {
                out.writeObject(name);
                out.writeObject(username);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * notify played the card number 5
     * @param land land to put the no_entry
     * @param username who played the card
     */
    public void sendNotifyCh_5(Land land,String username) {
        synchronized (this) {
            name = "NotifyCh_5";
            try {
                out.writeObject(name);
                out.writeObject(land);
                out.writeObject(username);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * notify played the card number 10
     * @param username who played the card
     * @param students student moved from the entry to the dining room
     * @param type_students type of student moved from dining room to entrance
     */
    public void sendNotifyCh_10(String username,ArrayList<Student> students,ArrayList<Type_Student> type_students) {
        synchronized (this) {
            name = "NotifyCh_10";
            try {
                out.writeObject(name);
                out.writeObject(username);
                out.writeObject(students);
                out.writeObject(type_students);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * notify played card 11
     * @param card new students on the card
     * @param student chosen to be placed in the dining room
     * @param username who played the card
     */
    public void sendNotifyCh_11(ArrayList<Student> card,Student student,String username) {
        synchronized (this) {
            name = "NotifyCh_11";
            try {
                out.writeObject(name);
                out.writeObject(card);
                out.writeObject(username);
                out.writeObject(student);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * notify used card number 12
     * @param type_student type of the student to be sub in the dinning room
     * @param username who played the card
     */
    public void sendNotifyCh_12(Type_Student type_student,String username) {
        synchronized (this) {
            name = "NotifyCh_12";
            try {
                out.writeObject(name);
                out.writeObject(type_student);
                out.writeObject(username);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * notify used card 8
     * @param username  who played the card
     */
    public void sendNotifyCh_8(String username){
        synchronized (this) {
            name = "NotifyCh_8";
            try {
                out.writeObject(name);
                out.writeObject(username);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
