package homework.pojos;

/*{
        "name": "Betül",
        "surname": "Bahçel",
        "gender": "female",
        "region": "Turkey"
    },*/
public class UIPojo {

    private String name ;
    private String surname ;
    private String gender ;
    private String region ;

    public String getName() {
        return name;
    }


    public String getSurname() {
        return surname;
    }

    public String getGender() {
        return gender;
    }


    public String getRegion() {
        return region;
    }



    @Override
    public String toString() {
        return "UIPojo{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", gender='" + gender + '\'' +
                ", region='" + region + '\'' +
                '}';
    }
}
