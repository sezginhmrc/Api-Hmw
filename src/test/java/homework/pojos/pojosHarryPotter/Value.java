package homework.pojos.pojosHarryPotter;
/*
* "values": [
        "courage",
        "bravery",
        "nerve",
        "chivalry"
    ],*/
public class Value {
 private String courage ;
 private String bravery ;
 private String nerve;
 private String chivalry ;

 public String getCourage() {
  return courage;
 }

 public void setCourage(String courage) {
  this.courage = courage;
 }

 public String getBravery() {
  return bravery;
 }

 public void setBravery(String bravery) {
  this.bravery = bravery;
 }

 public String getNerve() {
  return nerve;
 }

 public void setNerve(String nerve) {
  this.nerve = nerve;
 }

 public String getChivalry() {
  return chivalry;
 }

 public void setChivalry(String chivalry) {
  this.chivalry = chivalry;
 }

 @Override
 public String toString() {
  return "Value{" +
          "courage='" + courage + '\'' +
          ", bravery='" + bravery + '\'' +
          ", nerve='" + nerve + '\'' +
          ", chivalry='" + chivalry + '\'' +
          '}';
 }
}
