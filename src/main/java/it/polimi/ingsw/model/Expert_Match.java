package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.Arrays;

public class Expert_Match extends Match {
    CharacterCard card[];

    /**
     * create an instance of an expert match
     * @param pl1 first player of the match
     * @param pl2 second player of the match
     * @param card the character cards of the match
     */
    Expert_Match(Player pl1,Player pl2,CharacterCard[] card){
       super(pl1,pl2);
       this.card=new CharacterCard[3];
       this.card=card;
    }

    /**
     * create an instance of an expert match
     * @param pl1 first player of the match
     * @param pl2 second player of the match
     * @param pl3 third player of the match
     * @param card the character cards of the match
     */
    Expert_Match(Player pl1,Player pl2,Player pl3,CharacterCard[] card){
        super(pl1,pl2,pl3);
        this.card=new CharacterCard[3];
        this.card=card;
    }

    /**
     *
     * @return the character card of the match
     */
    public CharacterCard[] getCard() {
        return card;
    }

    @Override
    public String toString() {
        return super.toString() +
                "carte personaggio= " + Arrays.toString(card);
    }
}
