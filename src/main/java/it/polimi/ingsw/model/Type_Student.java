package it.polimi.ingsw.model;

public enum Type_Student {
    DRAGON("dragon"),GNOME("gnome"),FAIRIE("fairie"),UNICORN("unicorn"),FROG("frog");
     private final String name;

    Type_Student(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
