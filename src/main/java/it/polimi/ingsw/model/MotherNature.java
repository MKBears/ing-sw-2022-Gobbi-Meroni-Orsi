package it.polimi.ingsw.model;

public class MotherNature {
    private Land position;

    public MotherNature (Land position){
        this. position = position;
    }

    public Land getPosition() {
        return position;
    }

    /*
       In questo modo facciamo calcolare la nuova posizione di MN alla classe Match, che invia qui direttamente
        la nuova isola, ma si potrebbe anche far calcolare direttamente qui la nuova posizione inviando solo
        un intero/short col numero di passi che MN deve fare (pero' serve un riferimento alle isole anche in
        questa classe)
     */
    public void setPosition(Land position) {
        this.position = position;
    }
}
