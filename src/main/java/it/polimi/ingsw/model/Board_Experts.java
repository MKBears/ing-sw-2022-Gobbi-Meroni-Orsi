package it.polimi.ingsw.model;

public class Board_Experts extends Board{

    private int coins;

    public Board_Experts(){
        coins=1;
    }

    public int getCoinsNumber() {
        return coins;
    }

    public void addCoin(){
        coins++;
    }

    public void playCharacter(int cost) throws Exception{
        if(coins<cost){
            throw new Exception("You don't have enough money");
        }
        else
        {
            coins=coins-cost;
        }

    }

    public void addDragon(Student dragon){
        dragons.add(dragon);
        if(getdragons()==2 || getdragons()==5 || getdragons()==8){
            addCoin();
        }
    }
    public void addGnome(Student gnome){
        gnomes.add(gnome);
        if(getgnomes()==2 || getgnomes()==5 || getgnomes()==8){
            addCoin();
        }
    }
    public void addFairie(Student fairie){
        fairies.add(fairie);
        if(getfairies()==2 || getfairies()==5 || getfairies()==8){
            addCoin();
        }
    }
    public void addFrog(Student frog){
        frogs.add(frog);
        if(getfrogs()==2 || getfrogs()==5 || getfrogs()==8){
            addCoin();
        }
    }
    public void addUnicorn(Student unicorn){
        unicorns.add(unicorn);
        if(getunicorns()==2 || getunicorns()==5 || getunicorns()==8){
            addCoin();
        }
    }
}
