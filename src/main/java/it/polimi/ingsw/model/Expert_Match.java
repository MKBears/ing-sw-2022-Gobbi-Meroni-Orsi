package it.polimi.ingsw.model;

public class Expert_Match extends Match{
    CharacterCard[] card;

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
