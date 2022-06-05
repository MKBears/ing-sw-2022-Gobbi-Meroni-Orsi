package it.polimi.ingsw.client;

import it.polimi.ingsw.model.*;

import java.util.HashMap;
import java.util.Map;


public class Action {
    Match match;

    public Action(Match match) {
        this.match = match;
    }

    /**
     * move mother nature for a number of steps
     * @param assistant card of a player to check the number of step
     * @param step number of steps
     * @throws IllegalArgumentException in case steps<0 or steps>of the value of the step of the card
     */
    public void cardAndMoveMN(AssistantCard assistant,int step) throws IllegalArgumentException{
        if (assistant.getMNSteps()<step || step<0) throw new IllegalArgumentException();
        match.moveMotherNature(step);
    }

    /**
     * check the professors of every type
     */
    public void checkAllProfessors(){
        for (Type_Student e: Type_Student.values()) {
            match.checkProfessor(e);
        }
    }

    /**
     * unite lands in case of same color of tower
     */
    public void uniteLands()  {
        try{
            if(match.getLands().indexOf(match.getMotherNature().getPosition())!=match.getLands().size()-1) {
                if (match.getMotherNature().getPosition().getTowerColor() ==
                        match.getLands().get((match.getLands().indexOf(match.getMotherNature().getPosition()) + 1) % match.getLands().size()).getTowerColor()) {
                    match.uniteLandAfter(match.getLands().indexOf(match.getMotherNature().getPosition()));
                }
            }else if (match.getLands().get(0).getTowerColor()==match.getLands().get(match.getLands().size()-1).getTowerColor()){
                match.uniteLandAfter(match.getLands().indexOf(match.getMotherNature().getPosition()));
            }
        }catch (Exception e){
            System.out.println("isola dopo senza torre");
        }
        try{
            if(match.getLands().indexOf(match.getMotherNature().getPosition())!=0){
                if(match.getMotherNature().getPosition().getTowerColor()==
                        match.getLands().get(match.getLands().indexOf(match.getMotherNature().getPosition())-1).getTowerColor()){
                    match.uniteLandBefore(match.getLands().indexOf(match.getMotherNature().getPosition()));
                }
            }else if(match.getLands().get(0).getTowerColor()==match.getLands().get(match.getLands().size()-1).getTowerColor()){
                match.uniteLandBefore(match.getLands().indexOf(match.getMotherNature().getPosition()));
            }
        }catch(Exception e){
            System.out.println("isola prima senza torre");
        }
    }

    /**
     * import in the entry the students of the cloud
     * @param player player who choose the cloud
     * @param cloud cloud choosen
     */
    public void chooseCloud(Player player, Cloud cloud){
        player.getBoard().importStudents(cloud.getStudents());
        cloud.choose();
    }

    /**
     * move a student from the entry of the board to a land
     * @param player player who move the student
     * @param student the student to move to the land
     * @param land the land where add the student
     */
    public void moveFromIngressToLand(Player player,Student student,Land land){
        land.addStudent(player.getBoard().removeStudent(student));
    }

    /**
     * move a student from the entrance to the dinning room of a board
     * @param player player who move the student
     * @param student student to be moved
     * @throws Exception if the student isn't in the entry of the board
     */
    public void moveFromIngressToBoard(Player player,Student student) throws Exception{
            player.getBoard().placeStudent(student);
    }
}
