package it.polimi.ingsw.model;

import java.io.Serializable;

public enum Type_Student implements Serializable {
    DRAGON("dragon"), GNOME("gnome"), FAIRIE("fairie"), UNICORN("unicorn"), FROG("frog");
    private final String name;

    Type_Student(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        switch (name) {
            case ("dragon"):
                return "Rosso";
            case ("frog"):
                return "Verde";
            case ("unicorn"):
                return "Blu";
            case ("fairie"):
                return "Giallo";
            case ("gnome"):
                return "Rosa";
        }
        return null;
    }
}
