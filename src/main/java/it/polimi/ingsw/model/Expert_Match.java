package it.polimi.ingsw.model;

import it.polimi.ingsw.characterCards.*;
import it.polimi.ingsw.characterCards.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Expert_Match extends Match{
    CharacterCard[] card;

    /**
     * set the character cards on the match
     * @throws Exception the bag is empty
     */
    public void setCard() throws Exception{
        card=new CharacterCard[3];
        List<CharacterCard> c=new ArrayList<>();
        c.add(new Ch_1(this));
        c.add(new Ch_2(this));
        c.add(new Ch_4());
        c.add(new Ch_5(this));
        c.add(new Ch_8());
        c.add(new Ch_10(this));
        c.add(new Ch_11(this));
        c.add(new Ch_12(this));
        for(int i=0; i<3; i++) {
            Random a = new Random();
            int x = a.nextInt(1000);
            x = x % c.size();
            card[i]=c.remove(x);
        }
    }
    /**
     * create an instance of an expert match
     * @param pl1 first player of the match
     * @param pl2 second player of the match
     */
    public Expert_Match(Player pl1,Player pl2){
       super(pl1,pl2);
    }

    /**
     * create an instance of an expert match
     * @param pl1 first player of the match
     * @param pl2 second player of the match
     * @param pl3 third player of the match
     */
    public Expert_Match(Player pl1,Player pl2,Player pl3){
        super(pl1,pl2,pl3);
    }

    /**
     *
     * @return the character card of the match
     */
    public CharacterCard[] getCard() {
        return card;
    }
}
