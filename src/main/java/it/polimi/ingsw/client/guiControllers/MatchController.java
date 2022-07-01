package it.polimi.ingsw.client.guiControllers;

import it.polimi.ingsw.client.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characterCards.*;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MatchController extends Thread {
    private static Gui gui;
    private static ClientGui cg;
    private static Match match;
    private static Action action;
    private static Player me;
    private static Message4Server server;
    private ObjectInputStream in;
    private boolean selectedmn;
    private boolean selectedstudent;
    private Student assistantchoosen;
    private InputStream red_student;
    private InputStream yellow_student;
    private InputStream green_student;
    private InputStream blue_student;
    private InputStream pink_student;
    private String state;
    private ArrayList<Land> stepsmn;
    private CharacterCard[] ch;
    private InputStream noentry;
    private Student stuch1;
    private  boolean ich1;
    private  boolean ich5;
    private boolean ich10;
    private boolean ich11;
    private boolean ich12;
    private ArrayList<Student> stuch10;
    private ArrayList<Type_Student> tych10;
    private int selection;
    @FXML
    Pane land_view;
    @FXML
    Pane board_view;
    @FXML
    ImageView cloud0;
    @FXML
    ImageView cloud1;
    @FXML
    ImageView cloud2;
    @FXML
    ImageView assistant0;
    @FXML
    ImageView assistant1;
    @FXML
    ImageView assistant2;
    @FXML
    ImageView assistant3;
    @FXML
    ImageView assistant4;
    @FXML
    ImageView assistant5;
    @FXML
    ImageView assistant6;
    @FXML
    ImageView assistant7;
    @FXML
    ImageView assistant8;
    @FXML
    ImageView assistant9;
    @FXML
    ImageView island0;
    @FXML
    ImageView island1;
    @FXML
    ImageView island2;
    @FXML
    ImageView island3;
    @FXML
    ImageView island4;
    @FXML
    ImageView island5;
    @FXML
    ImageView island6;
    @FXML
    ImageView island7;
    @FXML
    ImageView island8;
    @FXML
    ImageView island9;
    @FXML
    ImageView island10;
    @FXML
    ImageView island11;
    @FXML
    ImageView entry00;
    @FXML
    ImageView entry01;
    @FXML
    ImageView entry02;
    @FXML
    ImageView entry03;
    @FXML
    ImageView entry04;
    @FXML
    ImageView entry05;
    @FXML
    ImageView entry06;
    @FXML
    ImageView entry07;
    @FXML
    ImageView entry08;
    @FXML
    ImageView entry10;
    @FXML
    ImageView entry11;
    @FXML
    ImageView entry12;
    @FXML
    ImageView entry13;
    @FXML
    ImageView entry14;
    @FXML
    ImageView entry15;
    @FXML
    ImageView entry16;
    @FXML
    ImageView entry17;
    @FXML
    ImageView entry18;
    @FXML
    ImageView entry20;
    @FXML
    ImageView entry21;
    @FXML
    ImageView entry22;
    @FXML
    ImageView entry23;
    @FXML
    ImageView entry24;
    @FXML
    ImageView entry25;
    @FXML
    ImageView entry26;
    @FXML
    ImageView entry27;
    @FXML
    ImageView entry28;
    @FXML
    ImageView board0;
    @FXML
    ImageView board1;
    @FXML
    ImageView board2;
    @FXML
    HBox green0;
    @FXML
    HBox green1;
    @FXML
    HBox green2;
    @FXML
    HBox red0;
    @FXML
    HBox red1;
    @FXML
    HBox red2;
    @FXML
    HBox yellow0;
    @FXML
    HBox yellow1;
    @FXML
    HBox yellow2;
    @FXML
    HBox pink0;
    @FXML
    HBox pink1;
    @FXML
    HBox pink2;
    @FXML
    HBox blue0;
    @FXML
    HBox blue1;
    @FXML
    HBox blue2;
    @FXML
    Label name0;
    @FXML
    Label name1;
    @FXML
    Label name2;
    @FXML
    Label state_label;
    @FXML
    ImageView profred0;
    @FXML
    ImageView profred1;
    @FXML
    ImageView profred2;
    @FXML
    ImageView profgreen0;
    @FXML
    ImageView profgreen1;
    @FXML
    ImageView profgreen2;
    @FXML
    ImageView profyellow0;
    @FXML
    ImageView profyellow1;
    @FXML
    ImageView profyellow2;
    @FXML
    ImageView profblue0;
    @FXML
    ImageView profblue1;
    @FXML
    ImageView profblue2;
    @FXML
    ImageView profpink0;
    @FXML
    ImageView profpink1;
    @FXML
    ImageView profpink2;
    @FXML
    ImageView tower00;
    @FXML
    ImageView tower01;
    @FXML
    ImageView tower02;
    @FXML
    ImageView tower03;
    @FXML
    ImageView tower04;
    @FXML
    ImageView tower05;
    @FXML
    ImageView tower06;
    @FXML
    ImageView tower07;
    @FXML
    ImageView tower10;
    @FXML
    ImageView tower11;
    @FXML
    ImageView tower12;
    @FXML
    ImageView tower13;
    @FXML
    ImageView tower14;
    @FXML
    ImageView tower15;
    @FXML
    ImageView tower16;
    @FXML
    ImageView tower17;
    @FXML
    ImageView tower20;
    @FXML
    ImageView tower21;
    @FXML
    ImageView tower22;
    @FXML
    ImageView tower23;
    @FXML
    ImageView tower24;
    @FXML
    ImageView tower25;
    @FXML
    ImageView tower26;
    @FXML
    ImageView tower27;
    @FXML
    ImageView white0;
    @FXML
    ImageView white1;
    @FXML
    ImageView white2;
    @FXML
    ImageView white3;
    @FXML
    ImageView white4;
    @FXML
    ImageView white5;
    @FXML
    ImageView white6;
    @FXML
    ImageView white7;
    @FXML
    ImageView white8;
    @FXML
    ImageView white9;
    @FXML
    ImageView white10;
    @FXML
    ImageView white11;
    @FXML
    ImageView black0;
    @FXML
    ImageView black1;
    @FXML
    ImageView black2;
    @FXML
    ImageView black3;
    @FXML
    ImageView black4;
    @FXML
    ImageView black5;
    @FXML
    ImageView black6;
    @FXML
    ImageView black7;
    @FXML
    ImageView black8;
    @FXML
    ImageView black9;
    @FXML
    ImageView black10;
    @FXML
    ImageView black11;
    @FXML
    ImageView grey0;
    @FXML
    ImageView grey1;
    @FXML
    ImageView grey2;
    @FXML
    ImageView grey3;
    @FXML
    ImageView grey4;
    @FXML
    ImageView grey5;
    @FXML
    ImageView grey6;
    @FXML
    ImageView grey7;
    @FXML
    ImageView grey8;
    @FXML
    ImageView grey9;
    @FXML
    ImageView grey10;
    @FXML
    ImageView grey11;
    @FXML
    ImageView mn0;
    @FXML
    ImageView mn1;
    @FXML
    ImageView mn2;
    @FXML
    ImageView mn3;
    @FXML
    ImageView mn4;
    @FXML
    ImageView mn5;
    @FXML
    ImageView mn6;
    @FXML
    ImageView mn7;
    @FXML
    ImageView mn8;
    @FXML
    ImageView mn9;
    @FXML
    ImageView mn10;
    @FXML
    ImageView mn11;
    @FXML
    Label red_0;
    @FXML
    Label red_1;
    @FXML
    Label red_2;
    @FXML
    Label red_3;
    @FXML
    Label red_4;
    @FXML
    Label red_5;
    @FXML
    Label red_6;
    @FXML
    Label red_7;
    @FXML
    Label red_8;
    @FXML
    Label red_9;
    @FXML
    Label red_10;
    @FXML
    Label red_11;
    @FXML
    Label blue_0;
    @FXML
    Label blue_1;
    @FXML
    Label blue_2;
    @FXML
    Label blue_3;
    @FXML
    Label blue_4;
    @FXML
    Label blue_5;
    @FXML
    Label blue_6;
    @FXML
    Label blue_7;
    @FXML
    Label blue_8;
    @FXML
    Label blue_9;
    @FXML
    Label blue_10;
    @FXML
    Label blue_11;
    @FXML
    Label green_0;
    @FXML
    Label green_1;
    @FXML
    Label green_2;
    @FXML
    Label green_3;
    @FXML
    Label green_4;
    @FXML
    Label green_5;
    @FXML
    Label green_6;
    @FXML
    Label green_7;
    @FXML
    Label green_8;
    @FXML
    Label green_9;
    @FXML
    Label green_10;
    @FXML
    Label green_11;
    @FXML
    Label pink_0;
    @FXML
    Label pink_1;
    @FXML
    Label pink_2;
    @FXML
    Label pink_3;
    @FXML
    Label pink_4;
    @FXML
    Label pink_5;
    @FXML
    Label pink_6;
    @FXML
    Label pink_7;
    @FXML
    Label pink_8;
    @FXML
    Label pink_9;
    @FXML
    Label pink_10;
    @FXML
    Label pink_11;
    @FXML
    Label yellow_0;
    @FXML
    Label yellow_1;
    @FXML
    Label yellow_2;
    @FXML
    Label yellow_3;
    @FXML
    Label yellow_4;
    @FXML
    Label yellow_5;
    @FXML
    Label yellow_6;
    @FXML
    Label yellow_7;
    @FXML
    Label yellow_8;
    @FXML
    Label yellow_9;
    @FXML
    Label yellow_10;
    @FXML
    Label yellow_11;
    @FXML
    AnchorPane land0;
    @FXML
    AnchorPane land1;
    @FXML
    AnchorPane land2;
    @FXML
    AnchorPane land3;
    @FXML
    AnchorPane land4;
    @FXML
    AnchorPane land5;
    @FXML
    AnchorPane land6;
    @FXML
    AnchorPane land7;
    @FXML
    AnchorPane land8;
    @FXML
    AnchorPane land9;
    @FXML
    AnchorPane land10;
    @FXML
    AnchorPane land11;
    @FXML
    Label size0;
    @FXML
    Label size1;
    @FXML
    Label size2;
    @FXML
    Label size3;
    @FXML
    Label size4;
    @FXML
    Label size5;
    @FXML
    Label size6;
    @FXML
    Label size7;
    @FXML
    Label size8;
    @FXML
    Label size9;
    @FXML
    Label size10;
    @FXML
    Label size11;
    @FXML
    AnchorPane cloud_for_three_player;
    @FXML
    ImageView cloudstudent00;
    @FXML
    ImageView cloudstudent01;
    @FXML
    ImageView cloudstudent02;
    @FXML
    ImageView cloudstudent03;
    @FXML
    ImageView cloudstudent10;
    @FXML
    ImageView cloudstudent11;
    @FXML
    ImageView cloudstudent12;
    @FXML
    ImageView cloudstudent13;
    @FXML
    ImageView cloudstudent20;
    @FXML
    ImageView cloudstudent21;
    @FXML
    ImageView cloudstudent22;
    @FXML
    ImageView cloudstudent23;
    @FXML
    ImageView wizard0;
    @FXML
    ImageView wizard1;
    @FXML
    ImageView wizard2;
    @FXML
    Label ncoin0;
    @FXML
    Label ncoin1;
    @FXML
    Label ncoin2;
    @FXML
    ImageView coin1;
    @FXML
    ImageView coin2;
    @FXML
    ImageView coin0;
    @FXML
    ImageView ch0;
    @FXML
    ImageView ch1;
    @FXML
    ImageView ch2;
    @FXML
    Pane characters;
    @FXML
    Button character_button;
    @FXML
    AnchorPane two;
    @FXML
    Button tomyboard0;
    @FXML
    Button tomyboard1;
    @FXML
    Button tomyboard2;
    @FXML
    ImageView ch_00;
    @FXML
    ImageView ch_01;
    @FXML
    ImageView ch_02;
    @FXML
    ImageView ch_03;
    @FXML
    ImageView ch_10;
    @FXML
    ImageView ch_11;
    @FXML
    ImageView ch_12;
    @FXML
    ImageView ch_13;
    @FXML
    ImageView ch_20;
    @FXML
    ImageView ch_21;
    @FXML
    ImageView ch_22;
    @FXML
    ImageView ch_23;
    @FXML
    Text power0;
    @FXML
    Text power1;
    @FXML
    Text power2;
    @FXML
    Pane youwantusechcards;
    @FXML
    Button yesiwant;
    @FXML
    Button noidont;
    @FXML
    Button ok0;
    @FXML
    Button ok1;
    @FXML
    Button ok2;
    @FXML
    ImageView noentry0;
    @FXML
    ImageView noentry1;
    @FXML
    ImageView noentry2;
    @FXML
    ImageView noentry3;
    @FXML
    ImageView noentry4;
    @FXML
    ImageView noentry5;
    @FXML
    ImageView noentry6;
    @FXML
    ImageView noentry7;
    @FXML
    ImageView noentry8;
    @FXML
    ImageView noentry9;
    @FXML
    ImageView noentry10;
    @FXML
    ImageView noentry11;
    @FXML
    ImageView asschosen;
    @FXML
    Button greenbutton;
    @FXML
    Button redbutton;
    @FXML
    Button yellowbutton;
    @FXML
    Button bluebutton;
    @FXML
    Button pinkbutton;
    @FXML
    Pane color;
    @FXML
    ImageView activatedcoin0;
    @FXML
    ImageView activatedcoin1;
    @FXML
    ImageView activatedcoin2;
    @FXML
    ImageView asschosen0;
    @FXML
    ImageView asschosen1;
    @FXML
    ImageView asschosen2;

    /**
     * Sets the Message4Client, server
     * @param server
     */
    public static void setServer(Message4Server server) {
        MatchController.server = server;
    }

    /**
     * Sets the Action, action
     * @param action
     */
    public static void setAction(Action action) {
        MatchController.action = action;
    }

    /**
     * Sets the Player me
     * @param me
     */
    public static void setMe(Player me) {
        MatchController.me = me;
    }

    /**
     * Sets the Gui gui
     * @param gui
     */
    public static void setGui(Gui gui) {
        MatchController.gui = gui;
    }

    /**
     * set the scene before the game starts
     */
    public void initialize() {
        tomyboard1.setVisible(false);
        tomyboard2.setVisible(false);
        tomyboard0.setVisible(false);
        white0.setVisible(false);
        white1.setVisible(false);
        white2.setVisible(false);
        white3.setVisible(false);
        white4.setVisible(false);
        white5.setVisible(false);
        white6.setVisible(false);
        white7.setVisible(false);
        white8.setVisible(false);
        white9.setVisible(false);
        white10.setVisible(false);
        white11.setVisible(false);
        black0.setVisible(false);
        black1.setVisible(false);
        black2.setVisible(false);
        black3.setVisible(false);
        black4.setVisible(false);
        black5.setVisible(false);
        black6.setVisible(false);
        black7.setVisible(false);
        black8.setVisible(false);
        black9.setVisible(false);
        black10.setVisible(false);
        black11.setVisible(false);
        grey0.setVisible(false);
        grey1.setVisible(false);
        grey2.setVisible(false);
        grey3.setVisible(false);
        grey4.setVisible(false);
        grey5.setVisible(false);
        grey6.setVisible(false);
        grey7.setVisible(false);
        grey8.setVisible(false);
        grey9.setVisible(false);
        grey10.setVisible(false);
        grey11.setVisible(false);
        mn1.setVisible(false);
        mn2.setVisible(false);
        mn3.setVisible(false);
        mn4.setVisible(false);
        mn5.setVisible(false);
        mn6.setVisible(false);
        mn7.setVisible(false);
        mn8.setVisible(false);
        mn9.setVisible(false);
        mn10.setVisible(false);
        mn11.setVisible(false);
        state_label.setText("Benvenuto!");
        state_label.setVisible(true);
        ok0.setVisible(false);
        ok1.setVisible(false);
        ok2.setVisible(false);
        ch_00.setVisible(true);
        ch_01.setVisible(true);
        ch_02.setVisible(true);
        ch_03.setVisible(true);
        ch_10.setVisible(true);
        ch_11.setVisible(true);
        ch_12.setVisible(true);
        ch_13.setVisible(true);
        ch_20.setVisible(true);
        ch_21.setVisible(true);
        ch_22.setVisible(true);
        ch_23.setVisible(true);
        ch_00.setDisable(true);
        ch_01.setDisable(true);
        ch_02.setDisable(true);
        ch_03.setDisable(true);
        ch_10.setDisable(true);
        ch_11.setDisable(true);
        ch_12.setDisable(true);
        ch_13.setDisable(true);
        ch_20.setDisable(true);
        ch_21.setDisable(true);
        ch_22.setDisable(true);
        ch_23.setDisable(true);
        asschosen.setVisible(false);
        for (int i = 0; i < 12; i++) {
            show_islands(i);
        }

        red_student= getClass().getClassLoader().getResourceAsStream("student_red.png");
        yellow_student = getClass().getClassLoader().getResourceAsStream("student_yellow.png");
        blue_student = getClass().getClassLoader().getResourceAsStream("student_blue.png");
        pink_student = getClass().getClassLoader().getResourceAsStream("student_pink.png");
        green_student = getClass().getClassLoader().getResourceAsStream("student_green.png");

        if (match.getPlayer().length == 2) {
            cloud_for_three_player.setVisible(false);
        }
        show_cloud();
        for (int i = 0; i < match.getPlayer().length; i++) {
            switch (i) {
                case 0 -> show_wizard(wizard0, match.getPlayer()[0].getWizard());
                case 1 -> show_wizard(wizard1, match.getPlayer()[1].getWizard());
                case 2 -> show_wizard(wizard2, match.getPlayer()[2].getWizard());
            }
        }
        if (!(match instanceof Expert_Match)) {
            coin0.setVisible(false);
            coin1.setVisible(false);
            coin2.setVisible(false);
            ncoin0.setVisible(false);
            ncoin1.setVisible(false);
            ncoin2.setVisible(false);
            character_button.setVisible(false);
        } else {
            System.out.println("Confermo che la partita è modalità esperto");
            stuch10= new ArrayList<>();
            setCharacters(((Expert_Match)match).getCard());
            character(ch0, ((Expert_Match)match).getCard()[0]);
            character(ch1, ((Expert_Match)match).getCard()[1]);
            character(ch2, ((Expert_Match)match).getCard()[2]);
            coin0.setVisible(true);
            coin1.setVisible(true);
            coin2.setVisible(true);
            ncoin0.setVisible(true);
            ncoin1.setVisible(true);
            ncoin0.setText(String.valueOf(((Board_Experts)(((Expert_Match)match).getPlayer()[0].getBoard())).getCoinsNumber()));
            ncoin1.setText(String.valueOf(((Board_Experts)(((Expert_Match)match).getPlayer()[1].getBoard())).getCoinsNumber()));
            if(match.getPlayersNum()==3) {
                ncoin2.setText(String.valueOf(((Board_Experts) (((Expert_Match) match).getPlayer()[2].getBoard())).getCoinsNumber()));
                ncoin2.setVisible(true);
            }
            character_button.setVisible(true);
            power0.setText(((Expert_Match)match).getCard()[0].getPowerUp());
            power1.setText(((Expert_Match)match).getCard()[1].getPowerUp());
            power2.setText(((Expert_Match)match).getCard()[2].getPowerUp());
            setDisableChCards(true);
        }
        if(match.getPlayersNum()==2){
            two.setVisible(false);
            entry07.setVisible(false);
            entry08.setVisible(false);
            entry17.setVisible(false);
            entry18.setVisible(false);
        }
        ich1=false;
        ich5=false;
        ich10=false;
        ich11=false;
        ich12=false;
        stuch10=new ArrayList<>();
        tych10= new ArrayList<>();
        selection=0;
        state = "Start";
    }

    /**
     * Sets the state for the switch in the run() method
     * @param s
     */
    public void setStateLabel(String s) {
        state_label.setText(s);
    }

    /**
     * Sets near the other character's board the image of the AssistantCard chosen by him
     * @param i the number of the player
     * @param c the AssistantCard chosen by the other player
     */
    public void setCardChosenbytheother(int i, AssistantCard c){
        if(i==0){
            asschosen0.setVisible(true);
            switch (c.getValue()){
                case 1:
                    asschosen0.setImage(assistant0.getImage());
                    break;
                case 2:
                    asschosen0.setImage(assistant1.getImage());
                    break;
                case 3:
                    asschosen0.setImage(assistant2.getImage());
                    break;
                case 4:
                    asschosen0.setImage(assistant3.getImage());
                    break;
                case 5:
                    asschosen0.setImage(assistant4.getImage());
                    break;
                case 6:
                    asschosen0.setImage(assistant5.getImage());
                    break;
                case 7:
                    asschosen0.setImage(assistant6.getImage());
                    break;
                case 8:
                    asschosen0.setImage(assistant7.getImage());
                    break;
                case 9:
                    asschosen0.setImage(assistant8.getImage());
                    break;
                case 10:
                    asschosen0.setImage(assistant9.getImage());
                    break;

            }
        } else if (i==1) {
            asschosen1.setVisible(true);
            switch (c.getValue()){
                case 1:
                    asschosen1.setImage(assistant0.getImage());
                    break;
                case 2:
                    asschosen1.setImage(assistant1.getImage());
                    break;
                case 3:
                    asschosen1.setImage(assistant2.getImage());
                    break;
                case 4:
                    asschosen1.setImage(assistant3.getImage());
                    break;
                case 5:
                    asschosen1.setImage(assistant4.getImage());
                    break;
                case 6:
                    asschosen1.setImage(assistant5.getImage());
                    break;
                case 7:
                    asschosen1.setImage(assistant6.getImage());
                    break;
                case 8:
                    asschosen1.setImage(assistant7.getImage());
                    break;
                case 9:
                    asschosen1.setImage(assistant8.getImage());
                    break;
                case 10:
                    asschosen1.setImage(assistant9.getImage());
                    break;

            }
        } else if (i==2) {
            asschosen2.setVisible(true);
            switch (c.getValue()){
                case 1:
                    asschosen2.setImage(assistant0.getImage());
                    break;
                case 2:
                    asschosen2.setImage(assistant1.getImage());
                    break;
                case 3:
                    asschosen2.setImage(assistant2.getImage());
                    break;
                case 4:
                    asschosen2.setImage(assistant3.getImage());
                    break;
                case 5:
                    asschosen2.setImage(assistant4.getImage());
                    break;
                case 6:
                    asschosen2.setImage(assistant5.getImage());
                    break;
                case 7:
                    asschosen2.setImage(assistant6.getImage());
                    break;
                case 8:
                    asschosen2.setImage(assistant7.getImage());
                    break;
                case 9:
                    asschosen2.setImage(assistant8.getImage());
                    break;
                case 10:
                    asschosen2.setImage(assistant9.getImage());
                    break;

            }
        }
    }

    /**
     * set the character card on the view
     * @param ch characters of the match
     */
    public void setCharacters(CharacterCard[] ch){
        this.ch=ch;
        character(ch0, ch[0]);
        character(ch1, ch[1]);
        character(ch2, ch[2]);
        power0.setText(ch[0].getPowerUp());
        power1.setText(ch[1].getPowerUp());
        power2.setText(ch[2].getPowerUp());
        if(ch[0].hasBeenActivated()){
            activatedcoin0.setVisible(true);
        }
        if(ch[1].hasBeenActivated()){
            activatedcoin1.setVisible(true);
        }
        if(ch[2].hasBeenActivated()){
            activatedcoin2.setVisible(true);
        }
        int i=0;
        for(CharacterCard c: ((Expert_Match)match).getCard()){
            if(i==0) {
                if (c instanceof Ch_1) {
                    show_student(ch_00, ((Ch_1) c).getStudents().get(0));
                    show_student(ch_01, ((Ch_1) c).getStudents().get(1));
                    show_student(ch_02, ((Ch_1) c).getStudents().get(2));
                    show_student(ch_03, ((Ch_1) c).getStudents().get(3));
                    ch_00.setVisible(true);
                    ch_01.setVisible(true);
                    ch_02.setVisible(true);
                    ch_03.setVisible(true);
                }else if(c instanceof Ch_5){
                    show_noentry(ch_00);
                    show_noentry(ch_01);
                    show_noentry(ch_02);
                    show_noentry(ch_03);
                    ch_00.setVisible(true);
                    ch_01.setVisible(true);
                    ch_02.setVisible(true);
                    ch_03.setVisible(true);
                } else if (c instanceof  Ch_11){
                    show_student(ch_00, ((Ch_11) c).getStudents().get(0));
                    show_student(ch_01, ((Ch_11) c).getStudents().get(1));
                    show_student(ch_02, ((Ch_11) c).getStudents().get(2));
                    show_student(ch_03, ((Ch_11) c).getStudents().get(3));
                    ch_00.setVisible(true);
                    ch_01.setVisible(true);
                    ch_02.setVisible(true);
                    ch_03.setVisible(true);
                } else {
                    ch_00.setVisible(false);
                    ch_01.setVisible(false);
                    ch_02.setVisible(false);
                    ch_03.setVisible(false);
                }
            }else if(i==1) {
                if (c instanceof Ch_1) {
                    show_student(ch_10, ((Ch_1) c).getStudents().get(0));
                    show_student(ch_11, ((Ch_1) c).getStudents().get(1));
                    show_student(ch_12, ((Ch_1) c).getStudents().get(2));
                    show_student(ch_13, ((Ch_1) c).getStudents().get(3));
                    ch_10.setVisible(true);
                    ch_11.setVisible(true);
                    ch_12.setVisible(true);
                    ch_13.setVisible(true);
                } else if (c instanceof Ch_5) {
                    show_noentry(ch_10);
                    show_noentry(ch_11);
                    show_noentry(ch_12);
                    show_noentry(ch_13);
                    ch_10.setVisible(true);
                    ch_11.setVisible(true);
                    ch_12.setVisible(true);
                    ch_13.setVisible(true);
                } else if (c instanceof  Ch_11){
                    show_student(ch_10, ((Ch_11) c).getStudents().get(0));
                    show_student(ch_11, ((Ch_11) c).getStudents().get(1));
                    show_student(ch_12, ((Ch_11) c).getStudents().get(2));
                    show_student(ch_13, ((Ch_11) c).getStudents().get(3));
                    ch_10.setVisible(true);
                    ch_11.setVisible(true);
                    ch_12.setVisible(true);
                    ch_13.setVisible(true);
                } else {
                    ch_10.setVisible(false);
                    ch_11.setVisible(false);
                    ch_12.setVisible(false);
                    ch_13.setVisible(false);
                }
            }
            else if(i==2){
                if (c instanceof Ch_1) {
                    show_student(ch_20, ((Ch_1) c).getStudents().get(0));
                    show_student(ch_21, ((Ch_1) c).getStudents().get(1));
                    show_student(ch_22, ((Ch_1) c).getStudents().get(2));
                    show_student(ch_23, ((Ch_1) c).getStudents().get(3));
                    ch_20.setVisible(true);
                    ch_21.setVisible(true);
                    ch_22.setVisible(true);
                    ch_23.setVisible(true);
                }else if(c instanceof Ch_5){
                    show_noentry(ch_20);
                    show_noentry(ch_21);
                    show_noentry(ch_22);
                    show_noentry(ch_23);
                    ch_20.setVisible(true);
                    ch_21.setVisible(true);
                    ch_22.setVisible(true);
                    ch_23.setVisible(true);
                } else if (c instanceof  Ch_11){
                    show_student(ch_20, ((Ch_11) c).getStudents().get(0));
                    show_student(ch_21, ((Ch_11) c).getStudents().get(1));
                    show_student(ch_22, ((Ch_11) c).getStudents().get(2));
                    show_student(ch_23, ((Ch_11) c).getStudents().get(3));
                    ch_20.setVisible(true);
                    ch_21.setVisible(true);
                    ch_22.setVisible(true);
                    ch_23.setVisible(true);
                } else {
                    ch_20.setVisible(false);
                    ch_21.setVisible(false);
                    ch_22.setVisible(false);
                    ch_23.setVisible(false);
                }
            }
            i++;
        }
    }

    /**
     * Sets the image for the Imageview noentry
     * @param imageView the Imageview to set
     */
    public void show_noentry(ImageView imageView){
        noentry=getClass().getClassLoader().getResourceAsStream("deny_island_icon.png");
        imageView.setImage(new Image(noentry));
    }

    @FXML
    /**
     * On the click of the mouse on the island if selected Mother Nature, moves it, if selected a Student and the click
     * is on the island or on one board it moves the student on the thing that was cliked and in any case sends to the
     * server the message
     * @param mouseEvent the click of the mouse on an island or on a board
     */
    public void moveto(MouseEvent mouseEvent) {
        int idland = -1;
        switch (((ImageView) mouseEvent.getSource()).getId()) {
            case "island0":
                idland = 0;
                break;
            case "island1":
                idland = 1;
                break;
            case "island2":
                idland = 2;
                break;
            case "island3":
                idland = 3;
                break;
            case "island4":
                idland = 4;
                break;
            case "island5":
                idland = 5;
                break;
            case "island6":
                idland = 6;
                break;
            case "island7":
                idland = 7;
                break;
            case "island8":
                idland = 8;
                break;
            case "island9":
                idland = 9;
                break;
            case "island10":
                idland = 10;
                break;
            case "island11":
                idland = 11;
                break;
        }
        if (idland >= 0) {
            if (selectedmn) {
                int indexmn = match.getLands().indexOf(match.getMotherNature().getPosition());
                int indexlandselected = -1;
                for (Land l : match.getLands()) {
                    if (l.getID() == idland)
                        indexlandselected = match.getLands().indexOf(l);
                }
                int step = 0;
                if (indexlandselected >= 0) {
                    if (indexmn <= indexlandselected) {
                        step = indexlandselected - indexmn;
                    } else if (indexmn > indexlandselected) {
                        step = match.getLands().size() - 1 - indexmn;
                        step++;
                        step = step + indexlandselected;
                    }
                    if (step >= 0 && step <= me.getPlayedCard().getMNSteps()) {
                        server.sendStepsMN(step);
                    }
                }
            } else if (selectedstudent) {
                server.sendMovedStudent(assistantchoosen, idland);
                for (Land l : match.getLands()) {
                    if (l.getID() == idland)
                        action.moveFromIngressToLand(me, assistantchoosen, l);
                }
                action.checkAllProfessors();
            }
            if(ich1){
                server.sendChooseCh1(stuch1,match.findIsland(idland));
                ch_00.setEffect(new DropShadow());
                ch_01.setEffect(new DropShadow());
                ch_02.setEffect(new DropShadow());
                ch_03.setEffect(new DropShadow());
                ch_10.setEffect(new DropShadow());
                ch_11.setEffect(new DropShadow());
                ch_12.setEffect(new DropShadow());
                ch_13.setEffect(new DropShadow());
                ch_20.setEffect(new DropShadow());
                ch_21.setEffect(new DropShadow());
                ch_22.setEffect(new DropShadow());
                ch_23.setEffect(new DropShadow());
                ich1=false;
            }
            if(ich5){
                server.sendChooseCh5(match.findIsland(idland));
                switch(idland){
                    case 0:
                        noentry0.setVisible(true);
                        break;
                    case 1:
                        noentry1.setVisible(true);
                        break;
                    case 2:
                        noentry2.setVisible(true);
                        break;
                    case 3:
                        noentry3.setVisible(true);
                        break;
                    case 4:
                        noentry4.setVisible(true);
                        break;
                    case 5:
                        noentry5.setVisible(true);
                        break;
                    case 6:
                        noentry6.setVisible(true);
                        break;
                    case 7:
                        noentry7.setVisible(true);
                        break;
                    case 8:
                        noentry8.setVisible(true);
                        break;
                    case 9:
                        noentry9.setVisible(true);
                        break;
                    case 10:
                        noentry10.setVisible(true);
                        break;
                    case 11:
                        noentry11.setVisible(true);
                        break;
                }
                ich5=false;
            }
        }
        refreshEntry();
        selectedstudent = false;
        selectedmn = false;
        setDisableMN(true);
        setDisableClouds(true);
        setDisableAssistants(true);
        setDisableLands(true);
        setDisableColumns(true);
        setDisableEntrance(true);
        setDisableBoards(true);
        tomyboard2.setVisible(false);
        tomyboard1.setVisible(false);
        tomyboard0.setVisible(false);
        synchronized (this) {
            notifyAll();
        }
    }

    /**
     * Update the student on the entrance of the boards
     */
    public void refreshEntry() {
        for (Player p : match.getPlayer()) {
            switch (p.getColor()) {
                case WHITE:
                    switch (p.getBoard().getEntrance().size()) {
                        case 1:
                            entry01.setVisible(false);
                            entry02.setVisible(false);
                            entry03.setVisible(false);
                            entry04.setVisible(false);
                            entry05.setVisible(false);
                            entry06.setVisible(false);
                            entry07.setVisible(false);
                            entry08.setVisible(false);
                            entry00.setEffect(new DropShadow());
                            break;
                        case 2:
                            entry02.setVisible(false);
                            entry03.setVisible(false);
                            entry04.setVisible(false);
                            entry05.setVisible(false);
                            entry06.setVisible(false);
                            entry07.setVisible(false);
                            entry08.setVisible(false);
                            entry00.setEffect(new DropShadow());
                            entry01.setEffect(new DropShadow());
                            break;
                        case 3:
                            entry03.setVisible(false);
                            entry04.setVisible(false);
                            entry05.setVisible(false);
                            entry06.setVisible(false);
                            entry07.setVisible(false);
                            entry08.setVisible(false);
                            entry00.setEffect(new DropShadow());
                            entry01.setEffect(new DropShadow());
                            entry02.setEffect(new DropShadow());
                            break;
                        case 4:
                            entry04.setVisible(false);
                            entry05.setVisible(false);
                            entry06.setVisible(false);
                            entry07.setVisible(false);
                            entry08.setVisible(false);
                            entry00.setEffect(new DropShadow());
                            entry01.setEffect(new DropShadow());
                            entry02.setEffect(new DropShadow());
                            entry03.setEffect(new DropShadow());
                            break;
                        case 5:
                            entry05.setVisible(false);
                            entry06.setVisible(false);
                            entry07.setVisible(false);
                            entry08.setVisible(false);
                            entry00.setEffect(new DropShadow());
                            entry01.setEffect(new DropShadow());
                            entry02.setEffect(new DropShadow());
                            entry03.setEffect(new DropShadow());
                            entry04.setEffect(new DropShadow());
                            break;
                        case 6:
                            entry06.setVisible(false);
                            entry07.setVisible(false);
                            entry08.setVisible(false);
                            entry00.setEffect(new DropShadow());
                            entry01.setEffect(new DropShadow());
                            entry02.setEffect(new DropShadow());
                            entry03.setEffect(new DropShadow());
                            entry04.setEffect(new DropShadow());
                            entry05.setEffect(new DropShadow());
                            break;
                        case 7:
                            entry07.setVisible(false);
                            entry08.setVisible(false);
                            entry00.setEffect(new DropShadow());
                            entry01.setEffect(new DropShadow());
                            entry02.setEffect(new DropShadow());
                            entry03.setEffect(new DropShadow());
                            entry04.setEffect(new DropShadow());
                            entry05.setEffect(new DropShadow());
                            entry06.setEffect(new DropShadow());
                            break;
                        case 8:
                            entry08.setVisible(false);
                            entry00.setEffect(new DropShadow());
                            entry01.setEffect(new DropShadow());
                            entry02.setEffect(new DropShadow());
                            entry03.setEffect(new DropShadow());
                            entry04.setEffect(new DropShadow());
                            entry05.setEffect(new DropShadow());
                            entry06.setEffect(new DropShadow());
                            entry07.setEffect(new DropShadow());
                            break;
                        default:
                            entry00.setEffect(new DropShadow());
                            entry01.setEffect(new DropShadow());
                            entry02.setEffect(new DropShadow());
                            entry03.setEffect(new DropShadow());
                            entry04.setEffect(new DropShadow());
                            entry05.setEffect(new DropShadow());
                            entry06.setEffect(new DropShadow());
                            entry07.setEffect(new DropShadow());
                            entry08.setEffect(new DropShadow());
                            break;
                    }
                    break;
                case BLACK:
                    switch (p.getBoard().getEntrance().size()) {
                        case 1:
                            entry11.setVisible(false);
                            entry12.setVisible(false);
                            entry13.setVisible(false);
                            entry14.setVisible(false);
                            entry15.setVisible(false);
                            entry16.setVisible(false);
                            entry17.setVisible(false);
                            entry18.setVisible(false);
                            entry10.setEffect(new DropShadow());
                            break;
                        case 2:
                            entry12.setVisible(false);
                            entry13.setVisible(false);
                            entry14.setVisible(false);
                            entry15.setVisible(false);
                            entry16.setVisible(false);
                            entry17.setVisible(false);
                            entry18.setVisible(false);
                            entry10.setEffect(new DropShadow());
                            entry11.setEffect(new DropShadow());
                            break;
                        case 3:
                            entry13.setVisible(false);
                            entry14.setVisible(false);
                            entry15.setVisible(false);
                            entry16.setVisible(false);
                            entry17.setVisible(false);
                            entry18.setVisible(false);
                            entry10.setEffect(new DropShadow());
                            entry11.setEffect(new DropShadow());
                            entry12.setEffect(new DropShadow());
                            break;
                        case 4:
                            entry14.setVisible(false);
                            entry15.setVisible(false);
                            entry16.setVisible(false);
                            entry17.setVisible(false);
                            entry18.setVisible(false);
                            entry10.setEffect(new DropShadow());
                            entry11.setEffect(new DropShadow());
                            entry12.setEffect(new DropShadow());
                            entry13.setEffect(new DropShadow());
                            break;
                        case 5:
                            entry15.setVisible(false);
                            entry16.setVisible(false);
                            entry17.setVisible(false);
                            entry18.setVisible(false);
                            entry10.setEffect(new DropShadow());
                            entry11.setEffect(new DropShadow());
                            entry12.setEffect(new DropShadow());
                            entry13.setEffect(new DropShadow());
                            entry14.setEffect(new DropShadow());
                            break;
                        case 6:
                            entry16.setVisible(false);
                            entry17.setVisible(false);
                            entry18.setVisible(false);
                            entry10.setEffect(new DropShadow());
                            entry11.setEffect(new DropShadow());
                            entry12.setEffect(new DropShadow());
                            entry13.setEffect(new DropShadow());
                            entry14.setEffect(new DropShadow());
                            entry15.setEffect(new DropShadow());
                            break;
                        case 7:
                            entry17.setVisible(false);
                            entry18.setVisible(false);
                            entry10.setEffect(new DropShadow());
                            entry11.setEffect(new DropShadow());
                            entry12.setEffect(new DropShadow());
                            entry13.setEffect(new DropShadow());
                            entry14.setEffect(new DropShadow());
                            entry15.setEffect(new DropShadow());
                            entry16.setEffect(new DropShadow());
                            break;
                        case 8:
                            entry18.setVisible(false);
                            entry10.setEffect(new DropShadow());
                            entry11.setEffect(new DropShadow());
                            entry12.setEffect(new DropShadow());
                            entry13.setEffect(new DropShadow());
                            entry14.setEffect(new DropShadow());
                            entry15.setEffect(new DropShadow());
                            entry16.setEffect(new DropShadow());
                            entry17.setEffect(new DropShadow());
                            break;
                        default:
                            entry10.setEffect(new DropShadow());
                            entry11.setEffect(new DropShadow());
                            entry12.setEffect(new DropShadow());
                            entry13.setEffect(new DropShadow());
                            entry14.setEffect(new DropShadow());
                            entry15.setEffect(new DropShadow());
                            entry16.setEffect(new DropShadow());
                            entry17.setEffect(new DropShadow());
                            entry18.setEffect(new DropShadow());
                            break;
                    }
                    break;
                case GREY:
                    switch (p.getBoard().getEntrance().size()) {
                        case 1:
                            entry21.setVisible(false);
                            entry22.setVisible(false);
                            entry23.setVisible(false);
                            entry24.setVisible(false);
                            entry25.setVisible(false);
                            entry26.setVisible(false);
                            entry27.setVisible(false);
                            entry28.setVisible(false);
                            entry20.setEffect(new DropShadow());
                            break;
                        case 2:
                            entry22.setVisible(false);
                            entry23.setVisible(false);
                            entry24.setVisible(false);
                            entry25.setVisible(false);
                            entry26.setVisible(false);
                            entry27.setVisible(false);
                            entry28.setVisible(false);
                            entry20.setEffect(new DropShadow());
                            entry21.setEffect(new DropShadow());
                            break;
                        case 3:
                            entry23.setVisible(false);
                            entry24.setVisible(false);
                            entry25.setVisible(false);
                            entry26.setVisible(false);
                            entry27.setVisible(false);
                            entry28.setVisible(false);
                            entry20.setEffect(new DropShadow());
                            entry21.setEffect(new DropShadow());
                            entry22.setEffect(new DropShadow());
                            break;
                        case 4:
                            entry24.setVisible(false);
                            entry25.setVisible(false);
                            entry26.setVisible(false);
                            entry27.setVisible(false);
                            entry28.setVisible(false);
                            entry20.setEffect(new DropShadow());
                            entry21.setEffect(new DropShadow());
                            entry22.setEffect(new DropShadow());
                            entry23.setEffect(new DropShadow());
                            break;
                        case 5:
                            entry25.setVisible(false);
                            entry26.setVisible(false);
                            entry27.setVisible(false);
                            entry28.setVisible(false);
                            entry20.setEffect(new DropShadow());
                            entry21.setEffect(new DropShadow());
                            entry22.setEffect(new DropShadow());
                            entry23.setEffect(new DropShadow());
                            entry24.setEffect(new DropShadow());
                            break;
                        case 6:
                            entry26.setVisible(false);
                            entry27.setVisible(false);
                            entry28.setVisible(false);
                            entry20.setEffect(new DropShadow());
                            entry21.setEffect(new DropShadow());
                            entry22.setEffect(new DropShadow());
                            entry23.setEffect(new DropShadow());
                            entry24.setEffect(new DropShadow());
                            entry25.setEffect(new DropShadow());
                            break;
                        case 7:
                            entry27.setVisible(false);
                            entry28.setVisible(false);
                            entry20.setEffect(new DropShadow());
                            entry21.setEffect(new DropShadow());
                            entry22.setEffect(new DropShadow());
                            entry23.setEffect(new DropShadow());
                            entry24.setEffect(new DropShadow());
                            entry25.setEffect(new DropShadow());
                            entry26.setEffect(new DropShadow());
                            break;
                        case 8:
                            entry28.setVisible(false);
                            entry20.setEffect(new DropShadow());
                            entry21.setEffect(new DropShadow());
                            entry22.setEffect(new DropShadow());
                            entry23.setEffect(new DropShadow());
                            entry24.setEffect(new DropShadow());
                            entry25.setEffect(new DropShadow());
                            entry26.setEffect(new DropShadow());
                            entry27.setEffect(new DropShadow());
                            break;

                    }
            }
        }
    }

    /**
     * Sets the ClientGui, cg
     * @param cg the ClientGui to set
     */
    public static void setClientGui(ClientGui cg){
        MatchController.cg=cg;
    }

    @FXML
    /**
     * When the cloud is chosen by the click it sends the Students to the player's board and sends the message to
     * the server
     * @param mouseEvent
     */
    public void choose_cloud(MouseEvent mouseEvent) {
        switch (((ImageView) mouseEvent.getSource()).getId()) {
            case "cloud0" -> {
                if(!match.getCloud()[0].getStudents().isEmpty()) {
                    action.chooseCloud(me, match.getCloud()[0]);
                    server.sendChoiceCloud(match.getCloud()[0].clone());
                    match.getCloud()[0].clearStudents();
                }
            }
            case "cloud1" -> {
                if(!match.getCloud()[1].getStudents().isEmpty()) {
                    action.chooseCloud(me, match.getCloud()[1]);
                    server.sendChoiceCloud(match.getCloud()[1].clone());
                    match.getCloud()[1].clearStudents();
                }
            }
            case "cloud2" -> {
                if(!match.getCloud()[2].getStudents().isEmpty()) {
                    action.chooseCloud(me, match.getCloud()[2]);
                    server.sendChoiceCloud(match.getCloud()[2].clone());
                    match.getCloud()[2].clearStudents();
                }
            }
        }
        entry00.setVisible(true);
        entry01.setVisible(true);
        entry02.setVisible(true);
        entry03.setVisible(true);
        entry04.setVisible(true);
        entry05.setVisible(true);
        entry06.setVisible(true);
        entry10.setVisible(true);
        entry11.setVisible(true);
        entry12.setVisible(true);
        entry13.setVisible(true);
        entry14.setVisible(true);
        entry15.setVisible(true);
        entry16.setVisible(true);
        if(match.getPlayersNum()==3){
            entry07.setVisible(true);
            entry08.setVisible(true);
            entry17.setVisible(true);
            entry18.setVisible(true);
            entry20.setVisible(true);
            entry21.setVisible(true);
            entry22.setVisible(true);
            entry23.setVisible(true);
            entry24.setVisible(true);
            entry25.setVisible(true);
            entry26.setVisible(true);
            entry27.setVisible(true);
            entry28.setVisible(true);
        }
        synchronized (this) {
            notifyAll();
        }
    }

    /**
     * This method sets the towers on the board
     *
     * @param i is the player
     */
    private void show_towers(int i) {
        Player pl = match.getPlayer()[i];
        Image colored_tower = null;
        InputStream f=null;
        switch (pl.getColor()) {
            case GREY -> f = getClass().getClassLoader().getResourceAsStream("grey_tower.png");
            case BLACK -> f = getClass().getClassLoader().getResourceAsStream("black_tower.png");
            case WHITE -> f = getClass().getClassLoader().getResourceAsStream("white_tower.png");
        }

        colored_tower = new Image(f);
        switch (i) {
            case 0 -> {
                tower00.setImage(colored_tower);
                tower00.setVisible(pl.getBoard().getTowersNum() >= 1);
                tower01.setImage(colored_tower);
                tower01.setVisible(pl.getBoard().getTowersNum() >= 2);
                tower02.setImage(colored_tower);
                tower02.setVisible(pl.getBoard().getTowersNum() >= 3);
                tower03.setImage(colored_tower);
                tower03.setVisible(pl.getBoard().getTowersNum() >= 4);
                tower04.setImage(colored_tower);
                tower04.setVisible(pl.getBoard().getTowersNum() >= 5);
                tower05.setImage(colored_tower);
                tower05.setVisible(pl.getBoard().getTowersNum() >= 6);
                tower06.setImage(colored_tower);
                tower06.setVisible(pl.getBoard().getTowersNum() >= 7);
                tower07.setImage(colored_tower);
                tower07.setVisible(pl.getBoard().getTowersNum() >= 8);
            }
            case 1 -> {
                tower10.setImage(colored_tower);
                tower10.setVisible(pl.getBoard().getTowersNum() >= 1);
                tower11.setImage(colored_tower);
                tower11.setVisible(pl.getBoard().getTowersNum() >= 2);
                tower12.setImage(colored_tower);
                tower12.setVisible(pl.getBoard().getTowersNum() >= 3);
                tower13.setImage(colored_tower);
                tower13.setVisible(pl.getBoard().getTowersNum() >= 4);
                tower14.setImage(colored_tower);
                tower14.setVisible(pl.getBoard().getTowersNum() >= 5);
                tower15.setImage(colored_tower);
                tower15.setVisible(pl.getBoard().getTowersNum() >= 6);
                tower16.setImage(colored_tower);
                tower16.setVisible(pl.getBoard().getTowersNum() >= 7);
                tower17.setImage(colored_tower);
                tower17.setVisible(pl.getBoard().getTowersNum() >= 8);
            }
            case 2 -> {
                tower20.setImage(colored_tower);
                tower20.setVisible(pl.getBoard().getTowersNum() >= 1);
                tower21.setImage(colored_tower);
                tower21.setVisible(pl.getBoard().getTowersNum() >= 2);
                tower22.setImage(colored_tower);
                tower22.setVisible(pl.getBoard().getTowersNum() >= 3);
                tower23.setImage(colored_tower);
                tower23.setVisible(pl.getBoard().getTowersNum() >= 4);
                tower24.setImage(colored_tower);
                tower24.setVisible(pl.getBoard().getTowersNum() >= 5);
                tower25.setImage(colored_tower);
                tower25.setVisible(pl.getBoard().getTowersNum() >= 6);
                tower26.setImage(colored_tower);
                tower26.setVisible(pl.getBoard().getTowersNum() >= 7);
                tower27.setImage(colored_tower);
                tower27.setVisible(pl.getBoard().getTowersNum() >= 8);
            }
        }
    }


    /**
     * This method sets the student on the Dining Room of the player's board
     *
     * @param i is the player
     */
    private void show(int i) {
        Player pl = match.getPlayer()[i];
        Map<Type_Student, Player> professors = match.getProfessors();
        int num_red = pl.getBoard().getStudentsOfType(Type_Student.DRAGON);
        int num_blue = pl.getBoard().getStudentsOfType(Type_Student.UNICORN);
        int num_yellow = pl.getBoard().getStudentsOfType(Type_Student.GNOME);
        int num_green = pl.getBoard().getStudentsOfType(Type_Student.FROG);
        int num_pink = pl.getBoard().getStudentsOfType(Type_Student.FAIRY);
        switch (i) {
            case 0 -> {
                name0.setText(match.getPlayer()[0].getUserName());
                for (Node e : red0.getChildren()) {
                    if (num_red > 0) {
                        e.setVisible(true);
                        num_red--;
                    } else {
                        e.setVisible(false);
                    }
                }
                for (Node e : green0.getChildren()) {
                    if (num_green > 0) {
                        e.setVisible(true);
                        num_green--;
                    } else {
                        e.setVisible(false);
                    }
                }
                for (Node e : yellow0.getChildren()) {
                    if (num_yellow > 0) {
                        e.setVisible(true);
                        num_yellow--;
                    } else {
                        e.setVisible(false);
                    }
                }
                for (Node e : blue0.getChildren()) {
                    if (num_blue > 0) {
                        e.setVisible(true);
                        num_blue--;
                    } else {
                        e.setVisible(false);
                    }
                }
                for (Node e : pink0.getChildren()) {
                    if (num_pink > 0) {
                        e.setVisible(true);
                        num_pink--;
                    } else {
                        e.setVisible(false);
                    }
                }
                profred0.setVisible(professors.containsKey(Type_Student.DRAGON) && professors.get(Type_Student.DRAGON) == pl);
                profblue0.setVisible(professors.containsKey(Type_Student.UNICORN) && professors.get(Type_Student.UNICORN) == pl);
                profgreen0.setVisible(professors.containsKey(Type_Student.FROG) && professors.get(Type_Student.FROG) == pl);
                profyellow0.setVisible(professors.containsKey(Type_Student.GNOME) && professors.get(Type_Student.GNOME) == pl);
                profpink0.setVisible(professors.containsKey(Type_Student.FAIRY) && professors.get(Type_Student.FAIRY) == pl);
            }
            case 1 -> {
                name1.setText(match.getPlayer()[1].getUserName());
                for (Node e : red1.getChildren()) {
                    if (num_red > 0) {
                        e.setVisible(true);
                        num_red--;
                    } else {
                        e.setVisible(false);
                    }
                }
                for (Node e : green1.getChildren()) {
                    if (num_green > 0) {
                        e.setVisible(true);
                        num_green--;
                    } else {
                        e.setVisible(false);
                    }
                }
                for (Node e : yellow1.getChildren()) {
                    if (num_yellow > 0) {
                        e.setVisible(true);
                        num_yellow--;
                    } else {
                        e.setVisible(false);
                    }
                }
                for (Node e : blue1.getChildren()) {
                    if (num_blue > 0) {
                        e.setVisible(true);
                        num_blue--;
                    } else {
                        e.setVisible(false);
                    }
                }
                for (Node e : pink1.getChildren()) {
                    if (num_pink > 0) {
                        e.setVisible(true);
                        num_pink--;
                    } else {
                        e.setVisible(false);
                    }
                }
                profred1.setVisible(professors.containsKey(Type_Student.DRAGON) && professors.get(Type_Student.DRAGON) == pl);
                profblue1.setVisible(professors.containsKey(Type_Student.UNICORN) && professors.get(Type_Student.UNICORN) == pl);
                profgreen1.setVisible(professors.containsKey(Type_Student.FROG) && professors.get(Type_Student.FROG) == pl);
                profyellow1.setVisible(professors.containsKey(Type_Student.GNOME) && professors.get(Type_Student.GNOME) == pl);
                profpink1.setVisible(professors.containsKey(Type_Student.FAIRY) && professors.get(Type_Student.FAIRY) == pl);
            }
            case 2 -> {
                name2.setText(match.getPlayer()[2].getUserName());
                for (Node e : red2.getChildren()) {
                    if (num_red > 0) {
                        e.setVisible(true);
                        num_red--;
                    } else {
                        e.setVisible(false);
                    }
                }
                for (Node e : green2.getChildren()) {
                    if (num_green > 0) {
                        e.setVisible(true);
                        num_green--;
                    } else {
                        e.setVisible(false);
                    }
                }
                for (Node e : yellow2.getChildren()) {
                    if (num_yellow > 0) {
                        e.setVisible(true);
                        num_yellow--;
                    } else {
                        e.setVisible(false);
                    }
                }
                for (Node e : blue2.getChildren()) {
                    if (num_blue > 0) {
                        e.setVisible(true);
                        num_blue--;
                    } else {
                        e.setVisible(false);
                    }
                }
                for (Node e : pink2.getChildren()) {
                    if (num_pink > 0) {
                        e.setVisible(true);
                        num_pink--;
                    } else {
                        e.setVisible(false);
                    }
                }
                profred2.setVisible(professors.containsKey(Type_Student.DRAGON) && professors.get(Type_Student.DRAGON) == pl);
                profblue2.setVisible(professors.containsKey(Type_Student.UNICORN) && professors.get(Type_Student.UNICORN) == pl);
                profgreen2.setVisible(professors.containsKey(Type_Student.FROG) && professors.get(Type_Student.FROG) == pl);
                profyellow2.setVisible(professors.containsKey(Type_Student.GNOME) && professors.get(Type_Student.GNOME) == pl);
                profpink2.setVisible(professors.containsKey(Type_Student.FAIRY) && professors.get(Type_Student.FAIRY) == pl);
            }
        }
    }

    @FXML
    /**
     * Sets the graphic interface of the board view (board_view)
     * @param actionEvent the click of the specified botton
     */
    public void show_boards(ActionEvent actionEvent) {
        land_view.setVisible(false);
        board_view.setVisible(true);
        characters.setVisible(false);
        for (int i = 0; i < match.getPlayer().length; i++) {
            show(i);
            show_towers(i);
        }
        show_entry();
        if(state!="ChooseAssistant") {
            for (AssistantCard a : me.getDeck()) {
                if (a.getValue() == 1) {
                    assistant0.setVisible(true);
                    break;
                }
            }
            for (AssistantCard a : me.getDeck()) {
                if (a.getValue() == 2) {
                    assistant1.setVisible(true);
                    break;
                }
            }
            for (AssistantCard a : me.getDeck()) {
                if (a.getValue() == 3) {
                    assistant2.setVisible(true);
                    break;
                }
            }
            for (AssistantCard a : me.getDeck()) {
                if (a.getValue() == 4) {
                    assistant3.setVisible(true);
                    break;
                }
            }
            for (AssistantCard a : me.getDeck()) {
                if (a.getValue() == 5) {
                    assistant4.setVisible(true);
                    break;
                }
            }
            for (AssistantCard a : me.getDeck()) {
                if (a.getValue() == 6) {
                    assistant5.setVisible(true);
                    break;
                }
            }
            for (AssistantCard a : me.getDeck()) {
                if (a.getValue() == 7) {
                    assistant6.setVisible(true);
                    break;
                }
            }
            for (AssistantCard a : me.getDeck()) {
                if (a.getValue() == 8) {
                    assistant7.setVisible(true);
                    break;
                }
            }
            for (AssistantCard a : me.getDeck()) {
                if (a.getValue() == 9) {
                    assistant8.setVisible(true);
                    break;
                }
            }
            for (AssistantCard a : me.getDeck()) {
                if (a.getValue() == 10) {
                    assistant9.setVisible(true);
                    break;
                }
            }
        }
    }

    @FXML
    /**
     * Selects the assistant card, clears it from the graphic interface, calls the draw() method and sends the choice to the server
     * @param mouseEvent the click of the mouse on the selected card
     */
    public void use_assistant(MouseEvent mouseEvent) {
        switch (((ImageView) mouseEvent.getSource()).getId()) {
            case "assistant0":
                for (AssistantCard a : me.getDeck()) {
                    if (a.getValue() == 1) {
                        server.sendChosenCard(a);
                        me.draw(a);
                        assistant0.setVisible(false);
                        gui.setAssistant(a);
                        asschosen.setImage(assistant0.getImage());
                        asschosen.setVisible(true);
                    }
                }
                break;
            case "assistant1":
                for (AssistantCard a : me.getDeck()) {
                    if (a.getValue() == 2) {
                        server.sendChosenCard(a);
                        me.draw(a);
                        assistant1.setVisible(false);
                        gui.setAssistant(a);
                        asschosen.setImage(assistant1.getImage());
                        asschosen.setVisible(true);
                    }
                }
                break;
            case "assistant2":
                for (AssistantCard a : me.getDeck()) {
                    if (a.getValue() == 3) {
                        server.sendChosenCard(a);
                        me.draw(a);
                        assistant2.setVisible(false);
                        gui.setAssistant(a);
                        asschosen.setImage(assistant2.getImage());
                        asschosen.setVisible(true);
                    }
                }
                break;
            case "assistant3":
                for (AssistantCard a : me.getDeck()) {
                    if (a.getValue() == 4) {
                        server.sendChosenCard(a);
                        me.draw(a);
                        assistant3.setVisible(false);
                        gui.setAssistant(a);
                        asschosen.setImage(assistant3.getImage());
                        asschosen.setVisible(true);
                    }
                }
                break;
            case "assistant4":
                for (AssistantCard a : me.getDeck()) {
                    if (a.getValue() == 5) {
                        server.sendChosenCard(a);
                        me.draw(a);
                        assistant4.setVisible(false);
                        gui.setAssistant(a);
                        asschosen.setImage(assistant4.getImage());
                        asschosen.setVisible(true);
                    }
                }
                break;
            case "assistant5":
                for (AssistantCard a : me.getDeck()) {
                    if (a.getValue() == 6) {
                        server.sendChosenCard(a);
                        me.draw(a);
                        assistant5.setVisible(false);
                        gui.setAssistant(a);
                        asschosen.setImage(assistant5.getImage());
                        asschosen.setVisible(true);
                    }
                }
                break;
            case "assistant6":
                for (AssistantCard a : me.getDeck()) {
                    if (a.getValue() == 7) {
                        server.sendChosenCard(a);
                        me.draw(a);
                        assistant6.setVisible(false);
                        gui.setAssistant(a);
                        asschosen.setImage(assistant6.getImage());
                        asschosen.setVisible(true);
                    }
                }
                break;
            case "assistant7":
                for (AssistantCard a : me.getDeck()) {
                    if (a.getValue() == 8) {
                        server.sendChosenCard(a);
                        me.draw(a);
                        assistant7.setVisible(false);
                        gui.setAssistant(a);
                        asschosen.setImage(assistant7.getImage());
                        asschosen.setVisible(true);
                    }
                }
                break;
            case "assistant8":
                for (AssistantCard a : me.getDeck()) {
                    if (a.getValue() == 9) {
                        server.sendChosenCard(a);
                        me.draw(a);
                        assistant8.setVisible(false);
                        gui.setAssistant(a);
                        asschosen.setImage(assistant8.getImage());
                        asschosen.setVisible(true);
                    }
                }
                break;
            case "assistant9":
                for (AssistantCard a : me.getDeck()) {
                    if (a.getValue() == 10) {
                        server.sendChosenCard(a);
                        me.draw(a);
                        assistant9.setVisible(false);
                        gui.setAssistant(a);
                        asschosen.setImage(assistant9.getImage());
                        asschosen.setVisible(true);
                    }
                }
                break;
        }
        synchronized (cg){
            cg.notifyAll();
            cg.notifyAll();
        }

    }

    /**
     * Sets the graphic interface of the Island or Archipelago
     *
     * @param i is the id of the land
     * @param l is the land
     */
    private void print_island(int i, Land l) {
        ArrayList<Type_Student> dragon = new ArrayList<>();
        dragon.add(Type_Student.DRAGON);
        ArrayList<Type_Student> gnome = new ArrayList<>();
        gnome.add(Type_Student.GNOME);
        ArrayList<Type_Student> fairie = new ArrayList<>();
        fairie.add(Type_Student.FAIRY);
        ArrayList<Type_Student> frog = new ArrayList<>();
        frog.add(Type_Student.FROG);
        ArrayList<Type_Student> unicorn = new ArrayList<>();
        unicorn.add(Type_Student.UNICORN);
        boolean exp=false;
        if(match instanceof Expert_Match) exp=true;
        switch (i) {
            case 0 -> {
                red_0.setText(":" + l.getInfluence(dragon));
                blue_0.setText(":" + l.getInfluence(unicorn));
                yellow_0.setText(":" + l.getInfluence(gnome));
                pink_0.setText(":" + l.getInfluence(fairie));
                green_0.setText(":" + l.getInfluence(frog));
                mn0.setVisible(match.getMotherNature().getPosition() == l);
                Integer temp = l.size();
                size0.setText(temp.toString());
                try {
                    switch (l.getTowerColor()) {
                        case WHITE -> {
                            white0.setVisible(true);
                            black0.setVisible(false);
                            grey0.setVisible(false);
                        }
                        case BLACK -> {
                            white0.setVisible(false);
                            black0.setVisible(true);
                            grey0.setVisible(false);
                        }
                        case GREY -> {
                            white0.setVisible(false);
                            black0.setVisible(false);
                            grey0.setVisible(true);
                        }
                    }
                } catch (Exception e) {
                    white0.setVisible(false);
                    black0.setVisible(false);
                    grey0.setVisible(false);
                }
                if(exp){
                    if(l.isThereNoEntry()) noentry0.setVisible(true);
                    else noentry0.setVisible(false);
                }
            }
            case 1 -> {
                red_1.setText(":" + l.getInfluence(dragon));
                blue_1.setText(":" + l.getInfluence(unicorn));
                yellow_1.setText(":" + l.getInfluence(gnome));
                pink_1.setText(":" + l.getInfluence(fairie));
                green_1.setText(":" + l.getInfluence(frog));
                mn1.setVisible(match.getMotherNature().getPosition() == l);
                Integer temp = l.size();
                size1.setText(temp.toString());
                try {
                    switch (l.getTowerColor()) {
                        case WHITE -> {
                            white1.setVisible(true);
                            black1.setVisible(false);
                            grey1.setVisible(false);
                        }
                        case BLACK -> {
                            white1.setVisible(false);
                            black1.setVisible(true);
                            grey1.setVisible(false);
                        }
                        case GREY -> {
                            white1.setVisible(false);
                            black1.setVisible(false);
                            grey1.setVisible(true);
                        }
                    }
                } catch (Exception e) {
                    white1.setVisible(false);
                    black1.setVisible(false);
                    grey1.setVisible(false);
                }
                if(exp){
                    if(l.isThereNoEntry()) noentry1.setVisible(true);
                    else noentry1.setVisible(false);
                }
            }
            case 2 -> {
                red_2.setText(":" + l.getInfluence(dragon));
                blue_2.setText(":" + l.getInfluence(unicorn));
                yellow_2.setText(":" + l.getInfluence(gnome));
                pink_2.setText(":" + l.getInfluence(fairie));
                green_2.setText(":" + l.getInfluence(frog));
                mn2.setVisible(match.getMotherNature().getPosition() == l);
                Integer temp = l.size();
                size2.setText(temp.toString());
                try {
                    switch (l.getTowerColor()) {
                        case WHITE -> {
                            white2.setVisible(true);
                            black2.setVisible(false);
                            grey2.setVisible(false);
                        }
                        case BLACK -> {
                            white2.setVisible(false);
                            black2.setVisible(true);
                            grey2.setVisible(false);
                        }
                        case GREY -> {
                            white2.setVisible(false);
                            black2.setVisible(false);
                            grey2.setVisible(true);
                        }
                    }
                } catch (Exception e) {
                    white2.setVisible(false);
                    black2.setVisible(false);
                    grey2.setVisible(false);
                }
                if(exp){
                    if(l.isThereNoEntry()) noentry2.setVisible(true);
                    else noentry2.setVisible(false);
                }
            }
            case 3 -> {
                red_3.setText(":" + l.getInfluence(dragon));
                blue_3.setText(":" + l.getInfluence(unicorn));
                yellow_3.setText(":" + l.getInfluence(gnome));
                pink_3.setText(":" + l.getInfluence(fairie));
                green_3.setText(":" + l.getInfluence(frog));
                mn3.setVisible(match.getMotherNature().getPosition() == l);
                Integer temp = l.size();
                size3.setText(temp.toString());
                try {
                    switch (l.getTowerColor()) {
                        case WHITE -> {
                            white3.setVisible(true);
                            black3.setVisible(false);
                            grey3.setVisible(false);
                        }
                        case BLACK -> {
                            white3.setVisible(false);
                            black3.setVisible(true);
                            grey3.setVisible(false);
                        }
                        case GREY -> {
                            white3.setVisible(false);
                            black3.setVisible(false);
                            grey3.setVisible(true);
                        }
                    }
                } catch (Exception e) {
                    white3.setVisible(false);
                    black3.setVisible(false);
                    grey3.setVisible(false);
                }
                if(exp){
                    if(l.isThereNoEntry()) noentry3.setVisible(true);
                    else noentry3.setVisible(false);
                }
            }
            case 4 -> {
                red_4.setText(":" + l.getInfluence(dragon));
                blue_4.setText(":" + l.getInfluence(unicorn));
                yellow_4.setText(":" + l.getInfluence(gnome));
                pink_4.setText(":" + l.getInfluence(fairie));
                green_4.setText(":" + l.getInfluence(frog));
                mn4.setVisible(match.getMotherNature().getPosition() == l);
                Integer temp = l.size();
                size4.setText(temp.toString());
                try {
                    switch (l.getTowerColor()) {
                        case WHITE -> {
                            white4.setVisible(true);
                            black4.setVisible(false);
                            grey4.setVisible(false);
                        }
                        case BLACK -> {
                            white4.setVisible(false);
                            black4.setVisible(true);
                            grey4.setVisible(false);
                        }
                        case GREY -> {
                            white4.setVisible(false);
                            black4.setVisible(false);
                            grey4.setVisible(true);
                        }
                    }
                } catch (Exception e) {
                    white4.setVisible(false);
                    black4.setVisible(false);
                    grey4.setVisible(false);
                }
                if(exp){
                    if(l.isThereNoEntry()) noentry4.setVisible(true);
                    else noentry4.setVisible(false);
                }
            }
            case 5 -> {
                red_5.setText(":" + l.getInfluence(dragon));
                blue_5.setText(":" + l.getInfluence(unicorn));
                yellow_5.setText(":" + l.getInfluence(gnome));
                pink_5.setText(":" + l.getInfluence(fairie));
                green_5.setText(":" + l.getInfluence(frog));
                mn5.setVisible(match.getMotherNature().getPosition() == l);
                Integer temp = l.size();
                size5.setText(temp.toString());
                try {
                    switch (l.getTowerColor()) {
                        case WHITE -> {
                            white5.setVisible(true);
                            black5.setVisible(false);
                            grey5.setVisible(false);
                        }
                        case BLACK -> {
                            white5.setVisible(false);
                            black5.setVisible(true);
                            grey5.setVisible(false);
                        }
                        case GREY -> {
                            white5.setVisible(false);
                            black5.setVisible(false);
                            grey5.setVisible(true);
                        }
                    }
                } catch (Exception e) {
                    white5.setVisible(false);
                    black5.setVisible(false);
                    grey5.setVisible(false);
                }
                if(exp){
                    if(l.isThereNoEntry()) noentry5.setVisible(true);
                    else noentry5.setVisible(false);
                }
            }
            case 6 -> {
                red_6.setText(":" + l.getInfluence(dragon));
                blue_6.setText(":" + l.getInfluence(unicorn));
                yellow_6.setText(":" + l.getInfluence(gnome));
                pink_6.setText(":" + l.getInfluence(fairie));
                green_6.setText(":" + l.getInfluence(frog));
                mn6.setVisible(match.getMotherNature().getPosition() == l);
                Integer temp = l.size();
                size6.setText(temp.toString());
                try {
                    switch (l.getTowerColor()) {
                        case WHITE -> {
                            white6.setVisible(true);
                            black6.setVisible(false);
                            grey6.setVisible(false);
                        }
                        case BLACK -> {
                            white6.setVisible(false);
                            black6.setVisible(true);
                            grey6.setVisible(false);
                        }
                        case GREY -> {
                            white6.setVisible(false);
                            black6.setVisible(false);
                            grey6.setVisible(true);
                        }
                    }
                } catch (Exception e) {
                    white6.setVisible(false);
                    black6.setVisible(false);
                    grey6.setVisible(false);
                }
                if(exp){
                    if(l.isThereNoEntry()) noentry6.setVisible(true);
                    else noentry6.setVisible(false);
                }
            }
            case 7 -> {
                red_7.setText(":" + l.getInfluence(dragon));
                blue_7.setText(":" + l.getInfluence(unicorn));
                yellow_7.setText(":" + l.getInfluence(gnome));
                pink_7.setText(":" + l.getInfluence(fairie));
                green_7.setText(":" + l.getInfluence(frog));
                mn7.setVisible(match.getMotherNature().getPosition() == l);
                Integer temp = l.size();
                size7.setText(temp.toString());
                try {
                    switch (l.getTowerColor()) {
                        case WHITE -> {
                            white7.setVisible(true);
                            black7.setVisible(false);
                            grey7.setVisible(false);
                        }
                        case BLACK -> {
                            white7.setVisible(false);
                            black7.setVisible(true);
                            grey7.setVisible(false);
                        }
                        case GREY -> {
                            white7.setVisible(false);
                            black7.setVisible(false);
                            grey7.setVisible(true);
                        }
                    }
                } catch (Exception e) {
                    white7.setVisible(false);
                    black7.setVisible(false);
                    grey7.setVisible(false);
                }
                if(exp){
                    if(l.isThereNoEntry()) noentry7.setVisible(true);
                    else noentry7.setVisible(false);
                }
            }
            case 8 -> {
                red_8.setText(":" + l.getInfluence(dragon));
                blue_8.setText(":" + l.getInfluence(unicorn));
                yellow_8.setText(":" + l.getInfluence(gnome));
                pink_8.setText(":" + l.getInfluence(fairie));
                green_8.setText(":" + l.getInfluence(frog));
                mn8.setVisible(match.getMotherNature().getPosition() == l);
                Integer temp = l.size();
                size8.setText(temp.toString());
                try {
                    switch (l.getTowerColor()) {
                        case WHITE -> {
                            white8.setVisible(true);
                            black8.setVisible(false);
                            grey8.setVisible(false);
                        }
                        case BLACK -> {
                            white8.setVisible(false);
                            black8.setVisible(true);
                            grey8.setVisible(false);
                        }
                        case GREY -> {
                            white8.setVisible(false);
                            black8.setVisible(false);
                            grey8.setVisible(true);
                        }
                    }
                } catch (Exception e) {
                    white8.setVisible(false);
                    black8.setVisible(false);
                    grey8.setVisible(false);
                }
                if(exp){
                    if(l.isThereNoEntry()) noentry8.setVisible(true);
                    else noentry8.setVisible(false);
                }
            }
            case 9 -> {
                red_9.setText(":" + l.getInfluence(dragon));
                blue_9.setText(":" + l.getInfluence(unicorn));
                yellow_9.setText(":" + l.getInfluence(gnome));
                pink_9.setText(":" + l.getInfluence(fairie));
                green_9.setText(":" + l.getInfluence(frog));
                mn9.setVisible(match.getMotherNature().getPosition() == l);
                Integer temp = l.size();
                size9.setText(temp.toString());
                try {
                    switch (l.getTowerColor()) {
                        case WHITE -> {
                            white9.setVisible(true);
                            black9.setVisible(false);
                            grey9.setVisible(false);
                        }
                        case BLACK -> {
                            white9.setVisible(false);
                            black9.setVisible(true);
                            grey9.setVisible(false);
                        }
                        case GREY -> {
                            white9.setVisible(false);
                            black9.setVisible(false);
                            grey9.setVisible(true);
                        }
                    }
                } catch (Exception e) {
                    white9.setVisible(false);
                    black9.setVisible(false);
                    grey9.setVisible(false);
                }
                if(exp){
                    if(l.isThereNoEntry()) noentry9.setVisible(true);
                    else noentry9.setVisible(false);
                }
            }
            case 10 -> {
                red_10.setText(":" + l.getInfluence(dragon));
                blue_10.setText(":" + l.getInfluence(unicorn));
                yellow_10.setText(":" + l.getInfluence(gnome));
                pink_10.setText(":" + l.getInfluence(fairie));
                green_10.setText(":" + l.getInfluence(frog));
                mn10.setVisible(match.getMotherNature().getPosition() == l);
                Integer temp = l.size();
                size10.setText(temp.toString());
                try {
                    switch (l.getTowerColor()) {
                        case WHITE -> {
                            white10.setVisible(true);
                            black10.setVisible(false);
                            grey10.setVisible(false);
                        }
                        case BLACK -> {
                            white10.setVisible(false);
                            black10.setVisible(true);
                            grey10.setVisible(false);
                        }
                        case GREY -> {
                            white10.setVisible(false);
                            black10.setVisible(false);
                            grey10.setVisible(true);
                        }
                    }
                } catch (Exception e) {
                    white10.setVisible(false);
                    black10.setVisible(false);
                    grey10.setVisible(false);
                }
                if(exp){
                    if(l.isThereNoEntry()) noentry10.setVisible(true);
                    else noentry10.setVisible(false);
                }
            }
            case 11 -> {
                red_11.setText(":" + l.getInfluence(dragon));
                blue_11.setText(":" + l.getInfluence(unicorn));
                yellow_11.setText(":" + l.getInfluence(gnome));
                pink_11.setText(":" + l.getInfluence(fairie));
                green_11.setText(":" + l.getInfluence(frog));
                mn11.setVisible(match.getMotherNature().getPosition() == l);
                Integer temp = l.size();
                size11.setText(temp.toString());
                try {
                    switch (l.getTowerColor()) {
                        case WHITE -> {
                            white11.setVisible(true);
                            black11.setVisible(false);
                            grey11.setVisible(false);
                        }
                        case BLACK -> {
                            white11.setVisible(false);
                            black11.setVisible(true);
                            grey11.setVisible(false);
                        }
                        case GREY -> {
                            white11.setVisible(false);
                            black11.setVisible(false);
                            grey11.setVisible(true);
                        }
                    }
                } catch (Exception e) {
                    white11.setVisible(false);
                    black11.setVisible(false);
                    grey11.setVisible(false);
                }
                if(exp){
                    if(l.isThereNoEntry()) noentry11.setVisible(true);
                    else noentry11.setVisible(false);
                }
            }
        }
    }

    @FXML
    /**
     * Sets invisible the island or land that has the id in input
     * @param i the island to set invisible
     */
    private void show_islands(int i) {

        boolean found = false;
        for (Land l : match.getLands()) {
            if (l.getID() == i) {
                found = true;
                print_island(i, l);
            }
        }
        if (!found) {
            switch (i) {
                case 0 -> land0.setVisible(false);
                case 1 -> land1.setVisible(false);
                case 2 -> land2.setVisible(false);
                case 3 -> land3.setVisible(false);
                case 4 -> land4.setVisible(false);
                case 5 -> land5.setVisible(false);
                case 6 -> land6.setVisible(false);
                case 7 -> land7.setVisible(false);
                case 8 -> land8.setVisible(false);
                case 9 -> land9.setVisible(false);
                case 10 -> land10.setVisible(false);
                case 11 -> land11.setVisible(false);
            }
        }
    }

    @FXML
    /**
     * Shows the islands and clous situation and hides the boards
     * @param actionEvent the click of the muse on the specified botton
     */
    public void go_to_island(ActionEvent actionEvent) {
        board_view.setVisible(false);
        land_view.setVisible(true);
        characters.setVisible(false);
        for (int i = 0; i < 12; i++) {
            show_islands(i);
        }
        show_cloud();
    }

    @FXML
    /**
     * Sets selectedmn on true and selectedstudent on false
     * @param mouseEvent the click on Mother Nature
     */
    public void selectmn(MouseEvent mouseEvent) {
        selectedmn = true;
        selectedstudent = false;
    }

    /**
     * Sets selectedstudent on true and selectedmn on false and sets assistantchoonsen
     *
     * @param pla
     * @param stu
     */
    private void selected(int pla, int stu) {
        if (me == match.getPlayer()[pla]) {
            selectedstudent = true;
            selectedmn = false;
            assistantchoosen = me.getBoard().getEntrance().get(stu);
        }
        if(pla==0){
            tomyboard0.setVisible(true);
        }else if(pla==1){
            tomyboard1.setVisible(true);
        }else if(pla==2){
            tomyboard2.setVisible(true);
        }
    }

    /**
     * Sets the entry of the boards
     */
    private void show_entry() {
            for (int i = 0; i < match.getPlayer().length; i++) {
                ArrayList<Student> entrance = match.getPlayer()[i].getBoard().getEntrance();
                switch (i) {
                    case 0: {
                        if (!entrance.isEmpty()) {
                            if (entry00.isVisible()) {
                                show_student(entry00, entrance.get(0));
                            }
                            if (entry01.isVisible()) {
                                show_student(entry01, entrance.get(1));
                            }
                            if (entry02.isVisible()) {
                                show_student(entry02, entrance.get(2));
                            }
                            if (entry03.isVisible()) {
                                show_student(entry03, entrance.get(3));
                            }
                            if (entrance.size() >= 5) {
                                show_student(entry04, entrance.get(4));
                            }
                            if (entrance.size() >= 6) {
                                show_student(entry05, entrance.get(5));
                            }
                            if (entrance.size() >= 7) {
                                show_student(entry06, entrance.get(6));
                            }
                            if (entrance.size() >= 8) {
                                show_student(entry07, entrance.get(7));
                            }
                            if (entrance.size() >= 9) {
                                show_student(entry08, entrance.get(8));
                            }
                        } else {
                            entry00.setVisible(false);
                            entry01.setVisible(false);
                            entry02.setVisible(false);
                            entry03.setVisible(false);
                            entry04.setVisible(false);
                            entry05.setVisible(false);
                            entry06.setVisible(false);
                            entry07.setVisible(false);
                            entry08.setVisible(false);
                        }
                    }
                    case 1: {
                        if (!entrance.isEmpty()) {
                            if (entry10.isVisible()) {
                                show_student(entry10, entrance.get(0));
                            }
                            if (entry11.isVisible()) {
                                show_student(entry11, entrance.get(1));
                            }
                            if (entry12.isVisible()) {
                                show_student(entry12, entrance.get(2));
                            }
                            if (entry13.isVisible()) {
                                show_student(entry13, entrance.get(3));
                            }
                            if (entrance.size() >= 5) {
                                show_student(entry14, entrance.get(4));
                            }
                            if (entrance.size() >= 6) {
                                show_student(entry15, entrance.get(5));
                            }
                            if (entrance.size() >= 7) {
                                show_student(entry16, entrance.get(6));
                            }
                            if (entrance.size() >= 8) {
                                show_student(entry17, entrance.get(7));
                            }
                            if (entrance.size() >= 9) {
                                show_student(entry18, entrance.get(8));
                            }
                        } else {
                            entry10.setVisible(false);
                            entry11.setVisible(false);
                            entry12.setVisible(false);
                            entry13.setVisible(false);
                            entry14.setVisible(false);
                            entry15.setVisible(false);
                            entry16.setVisible(false);
                            entry17.setVisible(false);
                            entry18.setVisible(false);
                        }
                    }
                    case 2: {
                        if (!entrance.isEmpty()) {
                            if (entry20.isVisible()) {
                                show_student(entry20, entrance.get(0));
                            }
                            if (entry21.isVisible()) {
                                show_student(entry21, entrance.get(1));
                            }
                            if (entry22.isVisible()) {
                                show_student(entry22, entrance.get(2));
                            }
                            if (entry23.isVisible()) {
                                show_student(entry23, entrance.get(3));
                            }
                            if (entrance.size() >= 5) {
                                show_student(entry24, entrance.get(4));
                            }
                            if (entrance.size() >= 6) {
                                show_student(entry25, entrance.get(5));
                            }
                            if (entrance.size() >= 7) {
                                show_student(entry26, entrance.get(6));
                            }
                            if (entrance.size() >= 8) {
                                show_student(entry27, entrance.get(7));
                            }
                            if (entrance.size() >= 9) {
                                show_student(entry28, entrance.get(8));
                            }
                        } else {
                            entry20.setVisible(false);
                            entry21.setVisible(false);
                            entry22.setVisible(false);
                            entry23.setVisible(false);
                            entry24.setVisible(false);
                            entry25.setVisible(false);
                            entry26.setVisible(false);
                            entry27.setVisible(false);
                            entry28.setVisible(false);
                        }
                    }
                }
            }
        if(state.equals("MoveStudent") || state.equals("ChooseMN") || state.equals("ChooseCloud")) {
            refreshEntry();
        }
    }

    @FXML
    /**
     * Calls the selected() method for every case of student's selection
     * @param mouseEvent
     */
    public void selectfromentry(MouseEvent mouseEvent) {
        ((ImageView) mouseEvent.getSource()).setEffect(new Bloom());
        if(ich10){
            selection++;
            if(selection==1) {
                switch (me.getColor()){
                    case WHITE -> ok0.setVisible(true);
                    case BLACK -> ok1.setVisible(true);
                    case GREY -> ok2.setVisible(true);
                }
                gui.popUp("Ok", "Ora scegli un altro studente oppure premi sul tasto 'OK' per scegliere quello della plancia con cui scambiarlo");
                switch (((ImageView) mouseEvent.getSource()).getId()) {
                    case "entry00" -> stuch10.add(match.getPlayer()[0].getBoard().getEntrance().get(0));
                    case "entry01" -> stuch10.add(match.getPlayer()[0].getBoard().getEntrance().get(1));
                    case "entry02" -> stuch10.add(match.getPlayer()[0].getBoard().getEntrance().get(2));
                    case "entry03" -> stuch10.add(match.getPlayer()[0].getBoard().getEntrance().get(3));
                    case "entry04" -> stuch10.add(match.getPlayer()[0].getBoard().getEntrance().get(4));
                    case "entry05" -> stuch10.add(match.getPlayer()[0].getBoard().getEntrance().get(5));
                    case "entry06" -> stuch10.add(match.getPlayer()[0].getBoard().getEntrance().get(6));
                    case "entry07" -> stuch10.add(match.getPlayer()[0].getBoard().getEntrance().get(7));
                    case "entry08" -> stuch10.add(match.getPlayer()[0].getBoard().getEntrance().get(8));
                    case "entry10" -> stuch10.add(match.getPlayer()[1].getBoard().getEntrance().get(0));
                    case "entry11" -> stuch10.add(match.getPlayer()[1].getBoard().getEntrance().get(1));
                    case "entry12" -> stuch10.add(match.getPlayer()[1].getBoard().getEntrance().get(2));
                    case "entry13" -> stuch10.add(match.getPlayer()[1].getBoard().getEntrance().get(3));
                    case "entry14" -> stuch10.add(match.getPlayer()[1].getBoard().getEntrance().get(4));
                    case "entry15" -> stuch10.add(match.getPlayer()[1].getBoard().getEntrance().get(5));
                    case "entry16" -> stuch10.add(match.getPlayer()[1].getBoard().getEntrance().get(6));
                    case "entry17" -> stuch10.add(match.getPlayer()[1].getBoard().getEntrance().get(7));
                    case "entry18" -> stuch10.add(match.getPlayer()[1].getBoard().getEntrance().get(8));
                    case "entry20" -> stuch10.add(match.getPlayer()[2].getBoard().getEntrance().get(0));
                    case "entry21" -> stuch10.add(match.getPlayer()[2].getBoard().getEntrance().get(1));
                    case "entry22" -> stuch10.add(match.getPlayer()[2].getBoard().getEntrance().get(2));
                    case "entry23" -> stuch10.add(match.getPlayer()[2].getBoard().getEntrance().get(3));
                    case "entry24" -> stuch10.add(match.getPlayer()[2].getBoard().getEntrance().get(4));
                    case "entry25" -> stuch10.add(match.getPlayer()[2].getBoard().getEntrance().get(5));
                    case "entry26" -> stuch10.add(match.getPlayer()[2].getBoard().getEntrance().get(6));
                    case "entry27" -> stuch10.add(match.getPlayer()[2].getBoard().getEntrance().get(7));
                    case "entry28" -> stuch10.add(match.getPlayer()[2].getBoard().getEntrance().get(8));
                }
                //gui.popUp("Ok", "Ora scegli una o due colonne della tua board");
            } else if (selection ==2){
                switch (((ImageView) mouseEvent.getSource()).getId()) {
                    case "entry00" -> stuch10.add(match.getPlayer()[0].getBoard().getEntrance().get(0));
                    case "entry01" -> stuch10.add(match.getPlayer()[0].getBoard().getEntrance().get(1));
                    case "entry02" -> stuch10.add(match.getPlayer()[0].getBoard().getEntrance().get(2));
                    case "entry03" -> stuch10.add(match.getPlayer()[0].getBoard().getEntrance().get(3));
                    case "entry04" -> stuch10.add(match.getPlayer()[0].getBoard().getEntrance().get(4));
                    case "entry05" -> stuch10.add(match.getPlayer()[0].getBoard().getEntrance().get(5));
                    case "entry06" -> stuch10.add(match.getPlayer()[0].getBoard().getEntrance().get(6));
                    case "entry07" -> stuch10.add(match.getPlayer()[0].getBoard().getEntrance().get(7));
                    case "entry08" -> stuch10.add(match.getPlayer()[0].getBoard().getEntrance().get(8));
                    case "entry10" -> stuch10.add(match.getPlayer()[1].getBoard().getEntrance().get(0));
                    case "entry11" -> stuch10.add(match.getPlayer()[1].getBoard().getEntrance().get(1));
                    case "entry12" -> stuch10.add(match.getPlayer()[1].getBoard().getEntrance().get(2));
                    case "entry13" -> stuch10.add(match.getPlayer()[1].getBoard().getEntrance().get(3));
                    case "entry14" -> stuch10.add(match.getPlayer()[1].getBoard().getEntrance().get(4));
                    case "entry15" -> stuch10.add(match.getPlayer()[1].getBoard().getEntrance().get(5));
                    case "entry16" -> stuch10.add(match.getPlayer()[1].getBoard().getEntrance().get(6));
                    case "entry17" -> stuch10.add(match.getPlayer()[1].getBoard().getEntrance().get(7));
                    case "entry18" -> stuch10.add(match.getPlayer()[1].getBoard().getEntrance().get(8));
                    case "entry20" -> stuch10.add(match.getPlayer()[2].getBoard().getEntrance().get(0));
                    case "entry21" -> stuch10.add(match.getPlayer()[2].getBoard().getEntrance().get(1));
                    case "entry22" -> stuch10.add(match.getPlayer()[2].getBoard().getEntrance().get(2));
                    case "entry23" -> stuch10.add(match.getPlayer()[2].getBoard().getEntrance().get(3));
                    case "entry24" -> stuch10.add(match.getPlayer()[2].getBoard().getEntrance().get(4));
                    case "entry25" -> stuch10.add(match.getPlayer()[2].getBoard().getEntrance().get(5));
                    case "entry26" -> stuch10.add(match.getPlayer()[2].getBoard().getEntrance().get(6));
                    case "entry27" -> stuch10.add(match.getPlayer()[2].getBoard().getEntrance().get(7));
                    case "entry28" -> stuch10.add(match.getPlayer()[2].getBoard().getEntrance().get(8));
                }
                setDisableEntrance(true);
                gui.popUp("OK", "Ora premi su OK per scegliere gli studenti con cui scambiarli nella plancia");
                switch (me.getColor()){
                    case WHITE -> ok0.setVisible(true);
                    case BLACK -> ok1.setVisible(true);
                    case GREY -> ok2.setVisible(true);
                }
            }
        } else {
            switch (((ImageView) mouseEvent.getSource()).getId()) {
                case "entry00" -> selected(0, 0);
                case "entry01" -> selected(0, 1);
                case "entry02" -> selected(0, 2);
                case "entry03" -> selected(0, 3);
                case "entry04" -> selected(0, 4);
                case "entry05" -> selected(0, 5);
                case "entry06" -> selected(0, 6);
                case "entry07" -> selected(0, 7);
                case "entry08" -> selected(0, 8);
                case "entry10" -> selected(1, 0);
                case "entry11" -> selected(1, 1);
                case "entry12" -> selected(1, 2);
                case "entry13" -> selected(1, 3);
                case "entry14" -> selected(1, 4);
                case "entry15" -> selected(1, 5);
                case "entry16" -> selected(1, 6);
                case "entry17" -> selected(1, 7);
                case "entry18" -> selected(1, 8);
                case "entry20" -> selected(2, 0);
                case "entry21" -> selected(2, 1);
                case "entry22" -> selected(2, 2);
                case "entry23" -> selected(2, 3);
                case "entry24" -> selected(2, 4);
                case "entry25" -> selected(2, 5);
                case "entry26" -> selected(2, 6);
                case "entry27" -> selected(2, 7);
                case "entry28" -> selected(2, 8);
            }
        }
        setStateLabel("Ora scegli se mettere lo studente nella plancia o in un isola");
        setDisableEntrance(true);
        setDisableBoards(true);
        setDisableLands(false);
        setDisableAssistants(true);
        setDisableClouds(true);
        setDisableMN(true);
        setDisableChCards(true);
        setDisableColumns(true);
        ((ImageView) mouseEvent.getSource()).setEffect(null);
    }

    /**
     * action of the column
     * @param mouseEvent click
     */
    public void columnselected(MouseEvent mouseEvent) {
        if(ich10) {
            selection--;
            switch(((Button)mouseEvent.getSource()).getId()){
                case "yellowbutton":
                    tych10.add(Type_Student.GNOME);
                    break;
                case "greenbutton":
                    tych10.add(Type_Student.FROG);
                    break;
                case "redbutton":
                    tych10.add(Type_Student.DRAGON);
                    break;
                case "pinkbutton":
                    tych10.add(Type_Student.FAIRY);
                    break;
                case "bluebutton":
                    tych10.add(Type_Student.UNICORN);
                    break;
            }
            if (selection == 0) {
                setDisableEntrance(true);
                setDisableColumns(true);
                color.setVisible(false);
                if (stuch10.size() == tych10.size()) {
                    server.sendChooseCh10(stuch10, tych10);
                }
            }
            stuch10 = null;
            tych10 = null;
            ich10 = false;
            selection = 0;
        } else if (ich12){
            color.setVisible(false);
            switch(((Button)mouseEvent.getSource()).getId()){
                case "yellowbutton":
                    server.sendChooseCh12(Type_Student.GNOME);
                    break;
                case "greenbutton":
                    server.sendChooseCh12(Type_Student.FROG);
                    break;
                case "redbutton":
                    server.sendChooseCh12(Type_Student.DRAGON);
                    break;
                case "pinkbutton":
                    server.sendChooseCh12(Type_Student.FAIRY);
                    break;
                case "bluebutton":
                    server.sendChooseCh12(Type_Student.UNICORN);
                    break;
            }
            setDisableColumns(true);
            ich12=false;
        }
    }

    /**
     * set no action to the column in the dinning room
     * @param v
     */
    public void setDisableColumns(boolean v){
        green0.setDisable(v);
        red0.setDisable(v);
        yellow0.setDisable(v);
        pink0.setDisable(v);
        blue0.setDisable(v);
        green1.setDisable(v);
        red1.setDisable(v);
        yellow1.setDisable(v);
        pink1.setDisable(v);
        blue1.setDisable(v);
        green2.setDisable(v);
        red2.setDisable(v);
        yellow2.setDisable(v);
        pink2.setDisable(v);
        blue2.setDisable(v);
    }

    /**
     * set effect drop shadow to the student in the dinning room
     */
    public void setNoEffectColumns(){
        green0.setEffect(new DropShadow());
        red0.setEffect(new DropShadow());
        yellow0.setEffect(new DropShadow());
        pink0.setEffect(new DropShadow());
        blue0.setEffect(new DropShadow());
        green1.setEffect(new DropShadow());
        red1.setEffect(new DropShadow());
        yellow1.setEffect(new DropShadow());
        pink1.setEffect(new DropShadow());
        blue1.setEffect(new DropShadow());
        green2.setEffect(new DropShadow());
        red2.setEffect(new DropShadow());
        yellow2.setEffect(new DropShadow());
        pink2.setEffect(new DropShadow());
        blue2.setEffect(new DropShadow());
    }

    /**
     * action after chose the students of the entrance for the character card 10
     * @param actionEvent the click of the mouse
     */
    public void Ok(ActionEvent actionEvent) {
        ((Button)actionEvent.getSource()).setVisible(false);
        int noprof=0;
        if(me.getBoard().getStudentsOfType(Type_Student.DRAGON)==0){
            redbutton.setVisible(false);
            noprof++;
        }
        if(me.getBoard().getStudentsOfType(Type_Student.GNOME)==0){
            yellowbutton.setVisible(false);
            noprof++;
        }
        if(me.getBoard().getStudentsOfType(Type_Student.FAIRY)==0){
            pinkbutton.setVisible(false);
            noprof++;
        }
        if(me.getBoard().getStudentsOfType(Type_Student.UNICORN)==0){
            bluebutton.setVisible(false);
            noprof++;
        }
        if(me.getBoard().getStudentsOfType(Type_Student.FROG)==0){
            greenbutton.setVisible(false);
            noprof++;
        }
        if(noprof==5){
            server.sendNoCh();
            gui.popUp("Oh no!!", "Non hai professori sulla tua plancia!!");
        }
        color.setVisible(true);
        setDropShadow();
    }

    /**
     * Sets the Match match
     * @param m the match we are playing
     */
    public static void setmatch(Match m) {
        MatchController.match = m;
    }

    /**
     * Sets the ObjectInputStream in
     * @param i the ObjectInputStream to set
     */
    public void setIn(ObjectInputStream i) {
        in = i;
    }

    /**
     * set no action on the clouds
     * @param v
     */
    private void setDisableClouds(boolean v){
        cloud0.setDisable(v);
        cloud1.setDisable(v);
        cloud2.setDisable(v);
    }

    /**
     * set no action on the image of the land
     * @param v
     */
    private void setDisableLands(boolean v) {
        island0.setDisable(v);
        island1.setDisable(v);
        island2.setDisable(v);
        island3.setDisable(v);
        island4.setDisable(v);
        island5.setDisable(v);
        island6.setDisable(v);
        island7.setDisable(v);
        island8.setDisable(v);
        island9.setDisable(v);
        island10.setDisable(v);
        island11.setDisable(v);
    }

    /**
     * set no action on the mn
     * @param v
     */
    private void setDisableMN(boolean v){
        mn0.setDisable(v);
        mn1.setDisable(v);
        mn2.setDisable(v);
        mn3.setDisable(v);
        mn4.setDisable(v);
        mn5.setDisable(v);
        mn6.setDisable(v);
        mn7.setDisable(v);
        mn8.setDisable(v);
        mn9.setDisable(v);
        mn10.setDisable(v);
        mn11.setDisable(v);
    }

    /**
     * set no action on the student of the entrance
     * @param v
     */
    private void setDisableEntrance(boolean v) {
        entry00.setDisable(v);
        entry01.setDisable(v);
        entry02.setDisable(v);
        entry03.setDisable(v);
        entry04.setDisable(v);
        entry05.setDisable(v);
        entry06.setDisable(v);
        entry07.setDisable(v);
        entry08.setDisable(v);
        entry10.setDisable(v);
        entry11.setDisable(v);
        entry12.setDisable(v);
        entry13.setDisable(v);
        entry14.setDisable(v);
        entry15.setDisable(v);
        entry16.setDisable(v);
        entry17.setDisable(v);
        entry18.setDisable(v);
        entry20.setDisable(v);
        entry21.setDisable(v);
        entry22.setDisable(v);
        entry23.setDisable(v);
        entry24.setDisable(v);
        entry25.setDisable(v);
        entry26.setDisable(v);
        entry27.setDisable(v);
        entry28.setDisable(v);
    }

    /**
     * set no action on the assistant
     * @param v
     */
    private void setDisableAssistants(boolean v){
        assistant0.setDisable(v);
        assistant1.setDisable(v);
        assistant2.setDisable(v);
        assistant3.setDisable(v);
        assistant4.setDisable(v);
        assistant5.setDisable(v);
        assistant6.setDisable(v);
        assistant7.setDisable(v);
        assistant8.setDisable(v);
        assistant9.setDisable(v);
    }

    /**
     * set the no action of the board
     * @param v true if you want to set no action
     */
    private void setDisableBoards(boolean v) {
        board0.setDisable(v);
        board1.setDisable(v);
        board2.setDisable(v);
    }

    /**
     * set visible the assistant card that you have in the deck, or you can choose
     * @param ass the card to be visible
     */
    public void setVisibleAssCards(List<AssistantCard> ass){
        System.out.println("Sono all'inizio del settaggio delle carte (MatchController)");
        assistant0.setVisible(false);
        assistant1.setVisible(false);
        assistant2.setVisible(false);
        assistant3.setVisible(false);
        assistant4.setVisible(false);
        assistant5.setVisible(false);
        assistant6.setVisible(false);
        assistant7.setVisible(false);
        assistant8.setVisible(false);
        assistant9.setVisible(false);
        for(AssistantCard a: ass){
            switch(a.getValue()){
                case 1:
                    assistant0.setVisible(true);
                    break;
                case 2:
                    assistant1.setVisible(true);
                    break;
                case 3:
                    assistant2.setVisible(true);
                    break;
                case 4:
                    assistant3.setVisible(true);
                    break;
                case 5:
                    assistant4.setVisible(true);
                    break;
                case 6:
                    assistant5.setVisible(true);
                    break;
                case 7:
                    assistant6.setVisible(true);
                    break;
                case 8:
                    assistant7.setVisible(true);
                    break;
                case 9:
                    assistant8.setVisible(true);
                    break;
                case 10:
                    assistant9.setVisible(true);
                    break;
            }
        }
        System.out.println("Ho finito di settare le cards (MatchControler)");
    }


    /**
     * represent the match and set the right action that you can do in this phase
     */
    @Override
    public void run() {

        System.out.println("Sono qui");
        Runnable updater = new Runnable() {
            @Override
            public void run() {
                System.out.println(state);
                switch (state) {
                    case "Start":
                    case "EndGame":
                    case "Next Turn":
                        setDisableAssistants(true);
                        setDisableBoards(true);
                        setDisableClouds(true);
                        setDisableEntrance(true);
                        setDisableLands(true);
                        setDisableColumns(true);
                        setDisableMN(true);
                        setDisableChCards(true);
                        break;
                    case "ChooseAssistant":
                        //System.out.println("Sono in MatchController ChooseAssistant");
                        setDisableAssistants(false);
                        setDisableBoards(true);
                        setDisableClouds(true);
                        setDisableColumns(true);
                        setDisableEntrance(true);
                        setDisableLands(true);
                        setDisableMN(true);
                        setDisableChCards(true);
                        gui.popUp("Scegliere carta assistente", "Tocca a te! Scegli una carta assistente");
                        break;
                    case "MoveStudent":

                        setDisableMN(true);
                        setDisableLands(true);
                        setDisableColumns(true);
                        setDisableEntrance(true);
                        switch(me.getColor()){
                            case WHITE:
                                entry00.setDisable(false);
                                entry01.setDisable(false);
                                entry02.setDisable(false);
                                entry03.setDisable(false);
                                entry04.setDisable(false);
                                entry05.setDisable(false);
                                entry06.setDisable(false);
                                entry07.setDisable(false);
                                entry08.setDisable(false);
                                break;
                            case BLACK:
                                entry10.setDisable(false);
                                entry11.setDisable(false);
                                entry12.setDisable(false);
                                entry13.setDisable(false);
                                entry14.setDisable(false);
                                entry15.setDisable(false);
                                entry16.setDisable(false);
                                entry17.setDisable(false);
                                entry18.setDisable(false);
                                break;
                            case GREY:
                                entry20.setDisable(false);
                                entry21.setDisable(false);
                                entry22.setDisable(false);
                                entry23.setDisable(false);
                                entry24.setDisable(false);
                                entry25.setDisable(false);
                                entry26.setDisable(false);
                                entry27.setDisable(false);
                                entry28.setDisable(false);
                                break;
                        }
                        setDisableClouds(true);
                        setDisableBoards(true);
                        setDisableAssistants(true);
                        setDisableChCards(true);
                        gui.popUp("Spostare lo studente", "Tocca a te! Scegli lo studente dall'ingresso e poi scegli la isola o la tua plancia in cui piazzarlo");
                        break;
                    case "ChooseMN":
                        setDisableMN(false);
                        setDisableLands(false);
                        setDisableColumns(true);
                        setDisableAssistants(true);
                        setDisableBoards(true);
                        setDisableClouds(true);
                        setDisableEntrance(true);
                        setDisableChCards(true);
                        gui.popUp("Spostare Madre Natura", "Ora puoi spostare madre natura: selezionala e poi scegli l'isola");
                        setStateLabel("E' il momento di spostare Madre Natura");
                        break;
                    case "ChooseCloud":
                        asschosen.setVisible(false);
                        setDisableEntrance(true);
                        setDisableClouds(false);
                        setDisableBoards(true);
                        setDisableAssistants(true);
                        setDisableColumns(true);
                        setDisableLands(true);
                        setDisableMN(true);
                        setDisableChCards(true);
                        gui.popUp("Scegliere una nuvola", "Ora puoi scegliere una nuvola");
                        setStateLabel("Ora scegli una nuvola");
                        break;
                    case "Ch":
                        setDisableMN(true);
                        setDisableColumns(true);
                        setDisableLands(true);
                        setDisableAssistants(true);
                        setDisableBoards(true);
                        setDisableClouds(true);
                        setDisableClouds(true);
                        setDisableEntrance(true);
                        setDisableChCards(true);
                        if(cg.getNoCh()) {
                            ch_question();
                        }
                        break;
                }
                theIfMethd();
            }
        };

        while (true) {
            try {
                synchronized (this) {
                    System.out.println("Sono in wait in Matchcontroller");
                    this.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(updater);
        }
    }

    /**
     * Sets the CharacterCards in order to not use them when is not the moment
     * @param v
     */
    public void setDisableChCards(boolean v){
        ch0.setDisable(v);
        ch1.setDisable(v);
        ch2.setDisable(v);
    }

    /**
     * Makes visible the Pane for the choice if you want to use a CharacterCard
     */
    public void ch_question(){
        youwantusechcards.setVisible(true);
    }

    /**
     * show the state of the match
     */
    public void theIfMethd(){
        if(match instanceof Expert_Match){
            setCharacters(gui.getCh());
        }
        if (land_view.isVisible()) {
            for (int i = 0; i < 12; i++) {
                show_islands(i);
                show_cloud();
            }
        } else {
            for (int i = 0; i < match.getPlayer().length; i++) {
                show(i);
                show_towers(i);
            }
            show_entry();
            setDropShadow();
            if (!state.equals("ChooseAssistant")) {
                assistant0.setVisible(false);
                assistant1.setVisible(false);
                assistant2.setVisible(false);
                assistant3.setVisible(false);
                assistant4.setVisible(false);
                assistant5.setVisible(false);
                assistant6.setVisible(false);
                assistant7.setVisible(false);
                assistant8.setVisible(false);
                assistant9.setVisible(false);
                for (AssistantCard a : me.getDeck()) {
                    if (a.getValue() == 1) {
                        assistant0.setVisible(true);
                        break;
                    }
                }
                for (AssistantCard a : me.getDeck()) {
                    if (a.getValue() == 2) {
                        assistant1.setVisible(true);
                        break;
                    }
                }
                for (AssistantCard a : me.getDeck()) {
                    if (a.getValue() == 3) {
                        assistant2.setVisible(true);
                        break;
                    }
                }
                for (AssistantCard a : me.getDeck()) {
                    if (a.getValue() == 4) {
                        assistant3.setVisible(true);
                        break;
                    }
                }
                for (AssistantCard a : me.getDeck()) {
                    if (a.getValue() == 5) {
                        assistant4.setVisible(true);
                        break;
                    }
                }
                for (AssistantCard a : me.getDeck()) {
                    if (a.getValue() == 6) {
                        assistant5.setVisible(true);
                        break;
                    }
                }
                for (AssistantCard a : me.getDeck()) {
                    if (a.getValue() == 7) {
                        assistant6.setVisible(true);
                        break;
                    }
                }
                for (AssistantCard a : me.getDeck()) {
                    if (a.getValue() == 8) {
                        assistant7.setVisible(true);
                        break;
                    }
                }
                for (AssistantCard a : me.getDeck()) {
                    if (a.getValue() == 9) {
                        assistant8.setVisible(true);
                        break;
                    }
                }
                for (AssistantCard a : me.getDeck()) {
                    if (a.getValue() == 10) {
                        assistant9.setVisible(true);
                        break;
                    }
                }
            }
        }
        if(match instanceof Expert_Match) {
            ncoin0.setText(String.valueOf(((Board_Experts) (((Expert_Match) match).getPlayer()[0].getBoard())).getCoinsNumber()));
            ncoin1.setText(String.valueOf(((Board_Experts) (((Expert_Match) match).getPlayer()[1].getBoard())).getCoinsNumber()));
            if (match.getPlayersNum() == 3) {
                ncoin2.setText(String.valueOf(((Board_Experts) (((Expert_Match) match).getPlayer()[2].getBoard())).getCoinsNumber()));
            }
        }
    }

    /**
     * set effect on the student of the entrance
     */
    public void setDropShadow(){
        entry00.setEffect(new DropShadow());
        entry01.setEffect(new DropShadow());
        entry02.setEffect(new DropShadow());
        entry03.setEffect(new DropShadow());
        entry04.setEffect(new DropShadow());
        entry15.setEffect(new DropShadow());
        entry06.setEffect(new DropShadow());
        entry07.setEffect(new DropShadow());
        entry08.setEffect(new DropShadow());
        entry10.setEffect(new DropShadow());
        entry11.setEffect(new DropShadow());
        entry12.setEffect(new DropShadow());
        entry13.setEffect(new DropShadow());
        entry14.setEffect(new DropShadow());
        entry15.setEffect(new DropShadow());
        entry16.setEffect(new DropShadow());
        entry17.setEffect(new DropShadow());
        entry18.setEffect(new DropShadow());
        entry20.setEffect(new DropShadow());
        entry21.setEffect(new DropShadow());
        entry22.setEffect(new DropShadow());
        entry23.setEffect(new DropShadow());
        entry24.setEffect(new DropShadow());
        entry25.setEffect(new DropShadow());
        entry26.setEffect(new DropShadow());
        entry27.setEffect(new DropShadow());
        entry28.setEffect(new DropShadow());
    }


    /**
     * wake up the thread to see the right state of the match
     * @param state phase of the match
     */
    public synchronized void wakeUp(String state) {
        System.out.println("Wake up del MatchController");
        this.state=state;
        this.notifyAll();
    }

    /**
     *
     * @return the match
     */
    public Match getMatch() {
        return match;
    }

    /**
     * Stes visible the specified type of student
     * @param imageView position of the student
     * @param student student that will be on it
     */
    private void show_student(ImageView imageView,Student student){
        try {
            //InputStream inputStream=getClass().getClassLoader().getResourceAsStream("CarteTOT_back_1@3x.png");
            //imageView.setImage(new Image(inputStream));
            //case WIZARD1 -> {
            //                    InputStream inputStream=getClass().getClassLoader().getResourceAsStream("CarteTOT_back_1@3x.png");
            //                    imageView.setImage(new Image(inputStream));
            //                }
            //InputStream f = null;

            switch (student.type()){
                case GNOME -> {
                    yellow_student=getClass().getClassLoader().getResourceAsStream("student_yellow.png");
                    imageView.setImage(new Image(yellow_student));
                }
                case FAIRY -> {
                    pink_student=getClass().getClassLoader().getResourceAsStream("student_pink.png");
                    imageView.setImage(new Image(pink_student));
                }
                case FROG -> {
                    green_student=getClass().getClassLoader().getResourceAsStream("student_green.png");
                    imageView.setImage(new Image(green_student));
                }
                case UNICORN -> {
                    blue_student=getClass().getClassLoader().getResourceAsStream("student_blue.png");
                    imageView.setImage(new Image(blue_student));
                }
                case DRAGON -> {
                    red_student=getClass().getClassLoader().getResourceAsStream("student_red.png");
                    imageView.setImage(new Image(red_student));
                }
            }
            imageView.setVisible(true);
        }catch (Exception e) {
            imageView.setVisible(false);
        }
    }

    /**
     * Sets the clouds with the right color of the students
     */
    @FXML
    private void show_cloud(){
        for (int i = 0; i < match.getPlayer().length; i++) {
            switch (i) {
                case 0 -> {
                    if(!match.getCloud()[0].getStudents().isEmpty()) {
                        show_student(cloudstudent00, match.getCloud()[0].getStudents().get(0));
                        show_student(cloudstudent01, match.getCloud()[0].getStudents().get(1));
                        show_student(cloudstudent02, match.getCloud()[0].getStudents().get(2));
                        if (match.getPlayer().length == 3)
                            show_student(cloudstudent03, match.getCloud()[0].getStudents().get(3));
                        else
                            cloudstudent03.setVisible(false);
                    }else{
                        cloudstudent00.setVisible(false);
                        cloudstudent01.setVisible(false);
                        cloudstudent02.setVisible(false);
                        cloudstudent03.setVisible(false);
                    }
                }
                case 1 ->{
                    if(!match.getCloud()[1].getStudents().isEmpty()) {
                        show_student(cloudstudent10, match.getCloud()[1].getStudents().get(0));
                        show_student(cloudstudent11, match.getCloud()[1].getStudents().get(1));
                        show_student(cloudstudent12, match.getCloud()[1].getStudents().get(2));
                        if (match.getPlayer().length == 3)
                            show_student(cloudstudent13, match.getCloud()[1].getStudents().get(3));
                        else
                            cloudstudent13.setVisible(false);
                    }else{
                        cloudstudent10.setVisible(false);
                        cloudstudent11.setVisible(false);
                        cloudstudent12.setVisible(false);
                        cloudstudent13.setVisible(false);
                    }
                }
                case 2 ->{
                    if(!match.getCloud()[2].getStudents().isEmpty()) {
                        show_student(cloudstudent20, match.getCloud()[2].getStudents().get(0));
                        show_student(cloudstudent21, match.getCloud()[2].getStudents().get(1));
                        show_student(cloudstudent22, match.getCloud()[2].getStudents().get(2));
                        show_student(cloudstudent23, match.getCloud()[2].getStudents().get(3));
                    }else{
                        cloudstudent20.setVisible(false);
                        cloudstudent21.setVisible(false);
                        cloudstudent22.setVisible(false);
                        cloudstudent23.setVisible(false);
                    }
                }
            }
        }
    }

    /**
     * Sets the wizards on the board_view
     * @param imageView position of the wizard chosed at the begginning of the match
     * @param wizards type of wizard chosen
     */
    @FXML
    private void show_wizard(ImageView imageView,Wizards wizards){
        try {
            switch (wizards) {
                case WIZARD1 -> {
                    InputStream inputStream=getClass().getClassLoader().getResourceAsStream("CarteTOT_back_1@3x.png");
                    imageView.setImage(new Image(inputStream));
                }
                case WIZARD2 -> {
                    InputStream inputStream=getClass().getClassLoader().getResourceAsStream("CarteTOT_back_11@3x.png");
                    imageView.setImage(new Image(inputStream));
                }
                case WIZARD3 -> {
                    InputStream inputStream=getClass().getClassLoader().getResourceAsStream("CarteTOT_back_21@3x.png");
                    imageView.setImage(new Image(inputStream));
                }
                case WIZARD4 -> {
                    InputStream inputStream=getClass().getClassLoader().getResourceAsStream("CarteTOT_back_31@3x.png");
                    imageView.setImage(new Image(inputStream));
                }
            }
            imageView.setRotate(90);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Sets the character card image
     * @param imageView the position on the view
     * @param character that will be on the view
     */
    @FXML
    private void character(ImageView imageView,CharacterCard character){
        URL url;
        File f;
        FileInputStream fil;
        try {
            if (character instanceof Ch_1) {
                InputStream inputStream=getClass().getClassLoader().getResourceAsStream("CarteTOT_front.jpg");
                imageView.setImage(new Image(inputStream));
            }else if(character instanceof Ch_2){
                InputStream inputStream=getClass().getClassLoader().getResourceAsStream("CarteTOT_front12.jpg");
                imageView.setImage(new Image(inputStream));
            }else if(character instanceof Ch_4){
                InputStream inputStream=getClass().getClassLoader().getResourceAsStream("CarteTOT_front3.jpg");
                imageView.setImage(new Image(inputStream));
            }else if(character instanceof Ch_5){
                InputStream inputStream=getClass().getClassLoader().getResourceAsStream("CarteTOT_front4.jpg");
                imageView.setImage(new Image(inputStream));
            } if(character instanceof Ch_8){
                InputStream inputStream=getClass().getClassLoader().getResourceAsStream("CarteTOT_front7.jpg");
                imageView.setImage(new Image(inputStream));
            }else if(character instanceof Ch_10){
                InputStream inputStream=getClass().getClassLoader().getResourceAsStream("CarteTOT_front9.jpg");
                imageView.setImage(new Image(inputStream));
            }else if(character instanceof Ch_11){
                InputStream inputStream=getClass().getClassLoader().getResourceAsStream("CarteTOT_front10.jpg");
                imageView.setImage(new Image(inputStream));
            }else if(character instanceof Ch_12){
                InputStream inputStream=getClass().getClassLoader().getResourceAsStream("CarteTOT_front11.jpg");
                imageView.setImage(new Image(inputStream));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    /**
     * action for see the character cards
     */
    public void show_characters(ActionEvent actionEvent) {
        land_view.setVisible(false);
        board_view.setVisible(false);
        characters.setVisible(true);
    }

     @FXML
     /**
      * action to move an assistant from the entrance to board
      */
    public void movetoboard(ActionEvent actionEvent) {

        switch (((Button) actionEvent.getSource()).getId()) {
            case "tomyboard0":
                tomyboard0.setVisible(false);
                break;
            case "tomyboard1":
                tomyboard1.setVisible(false);
                break;
            case "tomyboard2":
                tomyboard2.setVisible(false);
                break;

        }
        if (selectedstudent) {
            server.sendMovedStudent(assistantchoosen, 12);
            try {
                action.moveFromIngressToBoard(me, assistantchoosen);
            } catch (Exception e) {
                e.printStackTrace();
            }
            action.checkAllProfessors();
        }
        refreshEntry();
        selectedstudent = false;
        selectedmn = false;
        setDisableMN(true);
        setDisableClouds(true);
        setDisableAssistants(true);
        setDisableLands(true);
        setDisableColumns(true);
        setDisableEntrance(true);
        setDisableBoards(true);
        tomyboard2.setVisible(false);
        tomyboard1.setVisible(false);
        tomyboard0.setVisible(false);
        if(match instanceof Expert_Match) {
            ncoin0.setText(String.valueOf(((Board_Experts) (((Expert_Match) match).getPlayer()[0].getBoard())).getCoinsNumber()));
            ncoin1.setText(String.valueOf(((Board_Experts) (((Expert_Match) match).getPlayer()[1].getBoard())).getCoinsNumber()));
            if (match.getPlayersNum() == 3) {
                ncoin2.setText(String.valueOf(((Board_Experts) (((Expert_Match) match).getPlayer()[2].getBoard())).getCoinsNumber()));
            }
        }
        synchronized (this) {
            notifyAll();
        }
    }

    @FXML
    /**
     * set you can use the character card
     */
    public void usechyes(ActionEvent actionEvent) {
        setStateLabel("Momento carte personaggio!");
        youwantusechcards.setVisible(false);
        System.out.println("Hai scelto sii!!!");
        //setDisableChCards(false);
        if(gui.getUsability(1)){
            System.out.println("Rendo usabile la 1 perchè costa "+gui.getCh()[0].getPrice());
            ch0.setDisable(false);
        } else {
            ch0.setEffect(new GaussianBlur());
        }
        if(gui.getUsability(2)){
            ch1.setDisable(false);
            System.out.println("Rendo usabile la 2 perchè costa "+gui.getCh()[1].getPrice());
        } else {
            ch1.setEffect(new GaussianBlur());
        }
        if(gui.getUsability(3)){
            ch2.setDisable(false);
            System.out.println("Rendo usabile la 3 perchè costa "+gui.getCh()[2].getPrice());
        } else {
            ch2.setEffect(new GaussianBlur());
        }
        int i=0;
        for(CharacterCard c: ch){
            if(i==0) {
                if (c instanceof Ch_1) {
                    ch_00.setDisable(false);
                    ch_01.setDisable(false);
                    ch_02.setDisable(false);
                    ch_03.setDisable(false);
                }else if(c instanceof Ch_5){
                    ch_00.setDisable(false);
                    ch_01.setDisable(false);
                    ch_02.setDisable(false);
                    ch_03.setDisable(false);
                }
            }else if(i==1) {
                if (c instanceof Ch_1) {
                    ch_10.setDisable(false);
                    ch_11.setDisable(false);
                    ch_12.setDisable(false);
                    ch_13.setDisable(false);
                } else if (c instanceof Ch_5) {
                    ch_10.setDisable(false);
                    ch_11.setDisable(false);
                    ch_12.setDisable(false);
                    ch_13.setDisable(false);
                }
            }
            else if(i==2){
                if (c instanceof Ch_1) {
                    ch_20.setDisable(false);
                    ch_21.setDisable(false);
                    ch_22.setDisable(false);
                    ch_23.setDisable(false);
                }else if(c instanceof Ch_5){
                    ch_20.setDisable(false);
                    ch_21.setDisable(false);
                    ch_22.setDisable(false);
                    ch_23.setDisable(false);
                }
            }
            i++;
        }
    }

    @FXML
    /**
     * action whe you don't use a charcter card
     */
    public void usechno(ActionEvent actionEvent) {
        System.out.println("Mando NO al server");
        server.sendNoCh();
        youwantusechcards.setVisible(false);
    }

    /**
     * effect of card 1
     * @param h position of the card
     */
    public void useCh1(int h){
        setDisableChCards(true);
        setDisableAssistants(true);
        setDisableBoards(true);
        setDisableClouds(true);
        setDisableEntrance(true);
        setDisableMN(true);
        setDisableColumns(true);
        setDisableLands(true);
        gui.popUp("Scelta studente", "Ora scegli lo studente da spostare");
        ich1 = true;
        if(h==0){
            ch_00.setDisable(false);
            ch_01.setDisable(false);
            ch_02.setDisable(false);
            ch_03.setDisable(false);
        }else if(h==1){
            ch_10.setDisable(false);
            ch_11.setDisable(false);
            ch_12.setDisable(false);
            ch_13.setDisable(false);
        }else if (h==2){
            ch_20.setDisable(false);
            ch_21.setDisable(false);
            ch_22.setDisable(false);
            ch_23.setDisable(false);
        }
    }

    @FXML
    /**
     * action of the character card after you click it
     */
    public void useit(MouseEvent mouseEvent) {
        switch (((ImageView) mouseEvent.getSource()).getId()){
            case "ch_00":
                if(ich1) stuch1=(((Ch_1)gui.getCh()[0]).getStudents().get(0));
                if (ich11) server.sendChooseCh11(((Ch_11)gui.getCh()[0]).getStudents().get(0));
                ch_00.setDisable(true);
                ch_01.setDisable(true);
                ch_02.setDisable(true);
                ch_03.setDisable(true);
                ch_00.setEffect(new Bloom());
                break;
            case "ch_01":
                if(ich1) stuch1=(((Ch_1)gui.getCh()[0]).getStudents().get(1));
                if (ich11) server.sendChooseCh11(((Ch_11)gui.getCh()[0]).getStudents().get(1));
                ch_00.setDisable(true);
                ch_01.setDisable(true);
                ch_02.setDisable(true);
                ch_03.setDisable(true);
                ch_01.setEffect(new Bloom());
                break;
            case "ch_02":
                if(ich1) stuch1=(((Ch_1)gui.getCh()[0]).getStudents().get(2));
                if (ich11) server.sendChooseCh11(((Ch_11)gui.getCh()[0]).getStudents().get(2));
                ch_00.setDisable(true);
                ch_01.setDisable(true);
                ch_02.setDisable(true);
                ch_03.setDisable(true);
                ch_02.setEffect(new Bloom());
                break;
            case "ch_03":
                if(ich1) stuch1=(((Ch_1)gui.getCh()[0]).getStudents().get(3));
                if (ich11) server.sendChooseCh11(((Ch_11)gui.getCh()[0]).getStudents().get(3));
                ch_00.setDisable(true);
                ch_01.setDisable(true);
                ch_02.setDisable(true);
                ch_03.setDisable(true);
                ch_03.setEffect(new Bloom());
                break;
            case "ch_10":
                if(ich1) stuch1=(((Ch_1)gui.getCh()[1]).getStudents().get(0));
                if (ich11) server.sendChooseCh11(((Ch_11)gui.getCh()[1]).getStudents().get(0));
                ch_10.setDisable(true);
                ch_11.setDisable(true);
                ch_12.setDisable(true);
                ch_13.setDisable(true);
                ch_10.setEffect(new Bloom());
                break;
            case "ch_11":
                if(ich1)stuch1=(((Ch_1)gui.getCh()[1]).getStudents().get(1));
                if (ich11) server.sendChooseCh11(((Ch_11)gui.getCh()[1]).getStudents().get(1));
                ch_10.setDisable(true);
                ch_11.setDisable(true);
                ch_12.setDisable(true);
                ch_13.setDisable(true);
                ch_11.setEffect(new Bloom());
                break;
            case "ch_12":
                if(ich1)stuch1=(((Ch_1)gui.getCh()[1]).getStudents().get(2));
                if (ich11) server.sendChooseCh11(((Ch_11)gui.getCh()[1]).getStudents().get(2));
                ch_10.setDisable(true);
                ch_11.setDisable(true);
                ch_12.setDisable(true);
                ch_13.setDisable(true);
                ch_12.setEffect(new Bloom());
                break;
            case "ch_13":
                if(ich1)stuch1=(((Ch_1)gui.getCh()[1]).getStudents().get(3));
                if (ich11) server.sendChooseCh11(((Ch_11)gui.getCh()[1]).getStudents().get(3));
                ch_10.setDisable(true);
                ch_11.setDisable(true);
                ch_12.setDisable(true);
                ch_13.setDisable(true);
                ch_13.setEffect(new Bloom());
                break;
            case "ch_20":
                if(ich1)stuch1=(((Ch_1)gui.getCh()[2]).getStudents().get(0));
                if (ich11) server.sendChooseCh11(((Ch_11)gui.getCh()[2]).getStudents().get(0));
                ch_20.setDisable(true);
                ch_21.setDisable(true);
                ch_22.setDisable(true);
                ch_23.setDisable(true);
                ch_20.setEffect(new Bloom());
                break;
            case "ch_21":
                if(ich1)stuch1=(((Ch_1)gui.getCh()[2]).getStudents().get(1));
                if (ich11) server.sendChooseCh11(((Ch_11)gui.getCh()[2]).getStudents().get(1));
                ch_20.setDisable(true);
                ch_21.setDisable(true);
                ch_22.setDisable(true);
                ch_23.setDisable(true);
                ch_21.setEffect(new Bloom());
                break;
            case "ch_22":
                if(ich1)stuch1=(((Ch_1)gui.getCh()[2]).getStudents().get(2));
                if (ich11) server.sendChooseCh11(((Ch_11)gui.getCh()[2]).getStudents().get(2));
                ch_20.setDisable(true);
                ch_21.setDisable(true);
                ch_22.setDisable(true);
                ch_23.setDisable(true);
                ch_22.setEffect(new Bloom());
                break;
            case "ch_23":
                if(ich1)stuch1=(((Ch_1)gui.getCh()[2]).getStudents().get(3));
                if (ich11) server.sendChooseCh11(((Ch_11)gui.getCh()[2]).getStudents().get(3));
                ch_20.setDisable(true);
                ch_21.setDisable(true);
                ch_22.setDisable(true);
                ch_23.setDisable(true);
                ch_23.setEffect(new Bloom());
                break;
        }
        if(ich1) {
            gui.popUp("Scelta isola", "Ora scegli l'isola in cui piazzarlo");
            setDisableLands(false);
        } else if (ich11){
            ich11=false;
        }
    }

    /**
     * effect of ch 5
     */
    public void useCh5(){
        gui.popUp("Scelta isola", "Scegliere l'isola su cui piazzare la tessera divieto");
        setDisableLands(false);
        setDisableChCards(true);
        ich5=true;
    }

    /**
     * effect of ch 11
     */
    public void useCh11(){
        gui.popUp("Scelta studente", "Scegliere lo studente da mettere nella tua sala");
        int j=0;
        for (j=0; j<3; j++){
            if (gui.getCh()[j] instanceof Ch_11){
                break;
            }
        }
        if(j==0){
            ch_00.setDisable(false);
            ch_01.setDisable(false);
            ch_02.setDisable(false);
            ch_03.setDisable(false);
        }else if(j==1){
            ch_10.setDisable(false);
            ch_11.setDisable(false);
            ch_12.setDisable(false);
            ch_13.setDisable(false);
        }else if (j==2){
            ch_20.setDisable(false);
            ch_21.setDisable(false);
            ch_22.setDisable(false);
            ch_23.setDisable(false);
        }
        ich11=true;
    }

    /**
     * effect of ch 12
     */
    public void useCh12(){
        ich12=true;
        setDisableBoards(true);
        gui.popUp("Ok", "Scegli il colore che desideri per attivare la carta");
        color.setVisible(true);
    }

    /**
     * effect of ch 10
     */
    public void useCh10(){  //NON VA
        gui.popUp("Ok", "Scegli uno o due studenti dall'ingresso della tua plancia da scambiare con quelli nella sala");
        switch (me.getColor()) {
            case WHITE:
                board0.setDisable(false);
                switch (me.getBoard().getEntrance().size()) {
                    case 8:
                        entry01.setDisable(false);
                        entry02.setDisable(false);
                        entry03.setDisable(false);
                        entry04.setDisable(false);
                        entry05.setDisable(false);
                        entry06.setDisable(false);
                        entry07.setDisable(false);
                        entry08.setDisable(false);
                        break;
                    case 7:
                        entry01.setDisable(false);
                        entry02.setDisable(false);
                        entry03.setDisable(false);
                        entry04.setDisable(false);
                        entry05.setDisable(false);
                        entry06.setDisable(false);
                        entry07.setDisable(false);
                        break;
                    case 6:
                        entry01.setDisable(false);
                        entry02.setDisable(false);
                        entry03.setDisable(false);
                        entry04.setDisable(false);
                        entry05.setDisable(false);
                        entry06.setDisable(false);
                        break;
                    case 5:
                        entry01.setDisable(false);
                        entry02.setDisable(false);
                        entry03.setDisable(false);
                        entry04.setDisable(false);
                        entry05.setDisable(false);
                        break;
                    case 4:
                        entry01.setDisable(false);
                        entry02.setDisable(false);
                        entry03.setDisable(false);
                        entry04.setDisable(false);
                        break;
                    case 3:
                        entry01.setDisable(false);
                        entry02.setDisable(false);
                        entry03.setDisable(false);
                        break;
                    case 2:
                        entry01.setDisable(false);
                        entry02.setDisable(false);
                        break;
                    case 1:
                        entry01.setDisable(false);
                        break;
                }
                break;
            case BLACK:
                board1.setDisable(false);
                switch (me.getBoard().getEntrance().size()) {
                    case 8:
                        entry11.setDisable(false);
                        entry12.setDisable(false);
                        entry13.setDisable(false);
                        entry14.setDisable(false);
                        entry15.setDisable(false);
                        entry16.setDisable(false);
                        entry17.setDisable(false);
                        entry18.setDisable(false);
                        break;
                    case 7:
                        entry11.setDisable(false);
                        entry12.setDisable(false);
                        entry13.setDisable(false);
                        entry14.setDisable(false);
                        entry15.setDisable(false);
                        entry16.setDisable(false);
                        entry17.setDisable(false);
                        break;
                    case 6:
                        entry11.setDisable(false);
                        entry12.setDisable(false);
                        entry13.setDisable(false);
                        entry14.setDisable(false);
                        entry15.setDisable(false);
                        entry16.setDisable(false);
                        break;
                    case 5:
                        entry11.setDisable(false);
                        entry12.setDisable(false);
                        entry13.setDisable(false);
                        entry14.setDisable(false);
                        entry15.setDisable(false);
                        break;
                    case 4:
                        entry11.setDisable(false);
                        entry12.setDisable(false);
                        entry13.setDisable(false);
                        entry14.setDisable(false);
                        break;
                    case 3:
                        entry11.setDisable(false);
                        entry12.setDisable(false);
                        entry13.setDisable(false);
                        break;
                    case 2:
                        entry11.setDisable(false);
                        entry12.setDisable(false);
                        break;
                    case 1:
                        entry11.setDisable(false);
                        break;
                }
                break;
            case GREY:
                board2.setDisable(false);
                switch (me.getBoard().getEntrance().size()) {
                    case 8:
                        entry21.setDisable(false);
                        entry22.setDisable(false);
                        entry23.setDisable(false);
                        entry24.setDisable(false);
                        entry25.setDisable(false);
                        entry26.setDisable(false);
                        entry27.setDisable(false);
                        entry28.setDisable(false);
                        break;
                    case 7:
                        entry21.setDisable(false);
                        entry22.setDisable(false);
                        entry23.setDisable(false);
                        entry24.setDisable(false);
                        entry25.setDisable(false);
                        entry26.setDisable(false);
                        entry27.setDisable(false);
                        break;
                    case 6:
                        entry21.setDisable(false);
                        entry22.setDisable(false);
                        entry23.setDisable(false);
                        entry24.setDisable(false);
                        entry25.setDisable(false);
                        entry26.setDisable(false);
                        break;
                    case 5:
                        entry21.setDisable(false);
                        entry22.setDisable(false);
                        entry23.setDisable(false);
                        entry24.setDisable(false);
                        entry25.setDisable(false);
                        break;
                    case 4:
                        entry21.setDisable(false);
                        entry22.setDisable(false);
                        entry23.setDisable(false);
                        entry24.setDisable(false);
                        break;
                    case 3:
                        entry21.setDisable(false);
                        entry22.setDisable(false);
                        entry23.setDisable(false);
                        break;
                    case 2:
                        entry21.setDisable(false);
                        entry22.setDisable(false);
                        break;
                    case 1:
                        entry21.setDisable(false);
                        break;
                }
                break;
        }
        ich10=true;
    }
    @FXML
    /**
     * action of the character cards
     */
    public void use_ch(MouseEvent mouseEvent){
        switch (((ImageView) mouseEvent.getSource()).getId()){
            case "ch0":
                if (gui.getCh()[0] instanceof Ch_1) {
                    useCh1(0);
                } else if (gui.getCh()[0] instanceof Ch_2){
                    gui.popUp("Ok", "Hai scelto la carta assistente per avere il controllo dei professori");
                    server.sendChooseCh2();
                }else if(gui.getCh()[0] instanceof Ch_4){
                    gui.popUp("Ok", "Hai scelto la carta assistente per aumentare il numero di isole per lo spostamento di Madre Natura");
                    me.getPlayedCard().ch_4_effect();
                    server.sendChooseCh4();
                }else if(gui.getCh()[0] instanceof Ch_5){
                    useCh5();
                }else if (gui.getCh()[0] instanceof Ch_8){
                    gui.popUp("Ok", "Hai scelto la carta assistente per avere più influenza durante il conteggio");
                    server.sendChooseCh8();
                } else if (gui.getCh()[0] instanceof Ch_10){
                    useCh10();
                }else if (gui.getCh()[0] instanceof Ch_11){
                    useCh11();
                } else if (gui.getCh()[0] instanceof Ch_12){
                    useCh12();
                }
                break;
            case "ch1":
                if (gui.getCh()[1] instanceof Ch_1) {
                    useCh1(1);
                } else if (gui.getCh()[1] instanceof Ch_2){
                    gui.popUp("Ok", "Hai scelto la carta assistente per avere il controllo dei professori");
                    server.sendChooseCh2();
                }else if(gui.getCh()[1] instanceof Ch_4){
                    gui.popUp("Ok", "Hai scelto la carta assistente per aumentare il numero di isole per lo spostamento di Madre Natura");
                    me.getPlayedCard().ch_4_effect();
                    server.sendChooseCh4();
                }else if(gui.getCh()[1] instanceof Ch_5){
                    useCh5();
                }else if (gui.getCh()[1] instanceof Ch_8){
                    gui.popUp("Ok", "Hai scelto la carta assistente per avere più influenza durante il conteggio");
                    server.sendChooseCh8();
                } else if (gui.getCh()[1] instanceof Ch_10){
                    useCh10();
                }else if (gui.getCh()[1] instanceof Ch_11){
                    useCh11();
                } else if (gui.getCh()[1] instanceof Ch_12){
                    useCh12();
                }
                break;
            case "ch2":
                if (gui.getCh()[2] instanceof Ch_1) {
                    useCh1(2);
                } else if (gui.getCh()[2] instanceof Ch_2){
                    gui.popUp("Ok", "Hai scelto la carta assistente per avere il controllo dei professori");
                    server.sendChooseCh2();
                }else if(gui.getCh()[2] instanceof Ch_4){
                    me.getPlayedCard().ch_4_effect();
                    server.sendChooseCh4();
                }else if(gui.getCh()[2] instanceof Ch_5){
                    useCh5();
                }else if (gui.getCh()[2] instanceof Ch_8){
                    gui.popUp("Ok", "Hai scelto la carta assistente per avere più influenza durante il conteggio");
                    server.sendChooseCh8();
                } else if (gui.getCh()[2] instanceof Ch_10){
                    useCh10();
                }else if (gui.getCh()[2] instanceof Ch_11){
                    useCh11();
                } else if (gui.getCh()[2] instanceof Ch_12){
                    useCh12();
                }
                break;
        }
        cg.setNoCh(false);
        refreshEntry();
        ch0.setEffect(null);
        ch1.setEffect(null);
        ch2.setEffect(null);
    }
}
