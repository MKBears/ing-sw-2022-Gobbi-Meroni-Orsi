package it.polimi.ingsw.model;

public class Expert_Match extends Match{
    CharacterCard card[];
    Expert_Match(Player pl1,Player pl2,CharacterCard[] card){
       super(pl1,pl2);
       this.card=new CharacterCard[3];
       this.card=card;
    }
    Expert_Match(Player pl1,Player pl2,Player pl3,CharacterCard[] card){
        super(pl1,pl2,pl3);
        this.card=new CharacterCard[3];
        this.card=card;
    }

    public CharacterCard[] getCard() {
        return card;
    }
}
