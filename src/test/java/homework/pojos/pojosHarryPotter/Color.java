package homework.pojos.pojosHarryPotter;

public class Color {
    private String scarlet ;
    private String gold ;

    public String getScarlet() {
        return scarlet;
    }

    public void setScarlet(String scarlet) {
        this.scarlet = scarlet;
    }

    public String getGold() {
        return gold;
    }

    public void setGold(String gold) {
        this.gold = gold;
    }

    @Override
    public String toString() {
        return "Color{" +
                "scarlet='" + scarlet + '\'' +
                ", gold='" + gold + '\'' +
                '}';
    }
}
