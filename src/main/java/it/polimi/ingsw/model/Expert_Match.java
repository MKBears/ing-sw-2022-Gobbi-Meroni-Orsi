package it.polimi.ingsw.model;

import it.polimi.ingsw.model.characterCards.Ch_1;

import java.util.Random;

public class Expert_Match extends Match{

    CharacterCard[] card;

    /*private void setCard() {
        for(int i=0; i<3; i++) {
            Random a = new Random();
            int x = a.nextInt(2000);
            x = x % 8;
            switch (x){
                case 1:
                    Ch_1 ch_1;
                    this.card[i]=ch_1;
            }

        }
    }*/
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
