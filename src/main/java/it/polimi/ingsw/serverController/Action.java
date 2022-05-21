package it.polimi.ingsw.serverController;

import it.polimi.ingsw.model.*;

import java.util.HashMap;
import java.util.Map;


public class Action {
    Match match;

    public Action(Match match) {
        this.match = match;
    }

    public void cardAndMoveMN(AssistantCard assistant,int step) throws IllegalArgumentException{
        if (assistant.getMNSteps()<step || step<0) throw new IllegalArgumentException();
        match.moveMotherNature(step);
    }

    public void checkAllProfessors(){
        for (Type_Student e: Type_Student.values()) {
            match.checkProfessor(e);
        }
    }

    public void controlLand(Player player) throws Exception {
        Type_Student a=null;
        int max=0;
        Player owner=null;
        Player nuovo=null;
        Map<Player, Integer> influenze=new HashMap<>();
        for(int i=0;i< match.getPlayer().length;i++){
            influenze.put(match.getPlayer()[i],0);
            try {
                if (match.getPlayer()[i].getColor() == match.getMotherNature().getPosition().getTowerColor()) {
                    influenze.put(match.getPlayer()[i], 1);
                    owner = match.getPlayer()[i];
                }
            }catch (Exception e){}
        }
        for (Type_Student e:Type_Student.values()) {
            if (match.checkProfessor(e)!=null)
                influenze.replace(match.checkProfessor(e),influenze.get(match.checkProfessor(e))+match.getMotherNature().getPosition().getInfluence(e));
            }
        for(int j=0;j<match.getPlayer().length;j++){
            if(influenze.get(match.getPlayer()[j])>max) {
                max = influenze.get(match.getPlayer()[j]);
                nuovo=match.getPlayer()[j];
            }
        }
        if(owner==null){
            match.getMotherNature().getPosition().changeTower(nuovo.getBoard().removeTower());
        }else if(influenze.get(owner)!=influenze.get(nuovo)){
            match.getMotherNature().getPosition().changeTower(nuovo.getBoard().removeTower());
        }

    }

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

    public void chooseCloud(Player player, Cloud cloud){
        player.getBoard().importStudents(cloud.getStudents());
        cloud.choose();
    }


    public void moveFromEntranceToLand(Player player,Student student,Land land){
        land.addStudent(player.getBoard().removeStudent(student));
    }

    public void moveFromEntranceToBoard(Player player,Student student) throws Exception{
            player.getBoard().placeStudent(player.getBoard().removeStudent(student));
    }
}
