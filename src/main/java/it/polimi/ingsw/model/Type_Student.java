package it.polimi.ingsw.model;

public enum Type_Student {
    DRAGON("dragon"),GNOME("gnome"),FAIRIE("fairie"),UNICORN("unicorn"),FROG("frog");
     private String nome;

    Type_Student(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
