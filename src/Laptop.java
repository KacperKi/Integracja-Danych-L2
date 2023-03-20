import java.util.ArrayList;

public class Laptop {
    String producentLaptopa;
    String przekatnaEkranu;
    String rozdzielczoscEkranu;
    String rodzajPowierzchni;
    String czyDotykowyEkran;
    String nazwaProcesora;
    String liczbaRdzeni;
    String predkoscProcesora;
    String pamiecRam;
    String pojemnoscDysku;
    String rodzajDysku;
    String nazwaGrafiki;
    String pamiecGrafiki;
    String nazwaOs;
    String napedFizyczny;

    public Laptop(String producentLaptopa, String przekatnaEkranu, String rozdzielczoscEkranu, String rodzajPowierzchni, String czyDotykowyEkran, String nazwaProcesora, String liczbaRdzeni, String predkoscProcesora, String pamiecRam, String pojemnoscDysku, String rodzajDysku, String nazwaGrafiki, String pamiecGrafiki, String nazwaOs, String napedFizyczny) {
        this.producentLaptopa = producentLaptopa;
        this.przekatnaEkranu = przekatnaEkranu;
        this.rozdzielczoscEkranu = rozdzielczoscEkranu;
        this.rodzajPowierzchni = rodzajPowierzchni;
        this.czyDotykowyEkran = czyDotykowyEkran;
        this.nazwaProcesora = nazwaProcesora;
        this.liczbaRdzeni = liczbaRdzeni;
        this.predkoscProcesora = predkoscProcesora;
        this.pamiecRam = pamiecRam;
        this.pojemnoscDysku = pojemnoscDysku;
        this.rodzajDysku = rodzajDysku;
        this.nazwaGrafiki = nazwaGrafiki;
        this.pamiecGrafiki = pamiecGrafiki;
        this.nazwaOs = nazwaOs;
        this.napedFizyczny = napedFizyczny;
    }

    public Laptop() {
    }

    public String getProducentLaptopa() {
        return producentLaptopa;
    }

    public void setProducentLaptopa(String producentLaptopa) {
        this.producentLaptopa = producentLaptopa;
    }

    public String getPrzekatnaEkranu() {
        return przekatnaEkranu;
    }

    public void setPrzekatnaEkranu(String przekatnaEkranu) {
        this.przekatnaEkranu = przekatnaEkranu;
    }

    public String getRozdzielczoscEkranu() {
        return rozdzielczoscEkranu;
    }

    public void setRozdzielczoscEkranu(String rozdzielczoscEkranu) {
        this.rozdzielczoscEkranu = rozdzielczoscEkranu;
    }

    public String getRodzajPowierzchni() {
        return rodzajPowierzchni;
    }

    public void setRodzajPowierzchni(String rodzajPowierzchni) {
        this.rodzajPowierzchni = rodzajPowierzchni;
    }

    public String getCzyDotykowyEkran() {
        return czyDotykowyEkran;
    }

    public void setCzyDotykowyEkran(String czyDotykowyEkran) {
        this.czyDotykowyEkran = czyDotykowyEkran;
    }

    public String getNazwaProcesora() {
        return nazwaProcesora;
    }

    public void setNazwaProcesora(String nazwaProcesora) {
        this.nazwaProcesora = nazwaProcesora;
    }

    public String getLiczbaRdzeni() {
        return liczbaRdzeni;
    }

    public void setLiczbaRdzeni(String liczbaRdzeni) {
        this.liczbaRdzeni = liczbaRdzeni;
    }

    public String getPredkoscProcesora() {
        return predkoscProcesora;
    }

    public void setPredkoscProcesora(String predkoscProcesora) {
        this.predkoscProcesora = predkoscProcesora;
    }

    public String getPamiecRam() {
        return pamiecRam;
    }

    public void setPamiecRam(String pamiecRam) {
        this.pamiecRam = pamiecRam;
    }

    public String getPojemnoscDysku() {
        return pojemnoscDysku;
    }

    public void setPojemnoscDysku(String pojemnoscDysku) {
        this.pojemnoscDysku = pojemnoscDysku;
    }

    public String getRodzajDysku() {
        return rodzajDysku;
    }

    public void setRodzajDysku(String rodzajDysku) {
        this.rodzajDysku = rodzajDysku;
    }

    public String getNazwaGrafiki() {
        return nazwaGrafiki;
    }

    public void setNazwaGrafiki(String nazwaGrafiki) {
        this.nazwaGrafiki = nazwaGrafiki;
    }

    public String getPamiecGrafiki() {
        return pamiecGrafiki;
    }

    public void setPamiecGrafiki(String pamiecGrafiki) {
        this.pamiecGrafiki = pamiecGrafiki;
    }

    public String getNazwaOs() {
        return nazwaOs;
    }

    public void setNazwaOs(String nazwaOs) {
        this.nazwaOs = nazwaOs;
    }

    public String getNapedFizyczny() {
        return napedFizyczny;
    }

    public void setNapedFizyczny(String napedFizyczny) {
        this.napedFizyczny = napedFizyczny;
    }


    public ArrayList<String> convertClassToArrayList(){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(this.producentLaptopa);
        arrayList.add(this.przekatnaEkranu);
        arrayList.add(this.rozdzielczoscEkranu);
        arrayList.add(this.rodzajPowierzchni);
        arrayList.add(this.czyDotykowyEkran);
        arrayList.add(this.nazwaProcesora);
        arrayList.add(this.liczbaRdzeni);
        arrayList.add(this.predkoscProcesora);
        arrayList.add(this.pamiecRam);
        arrayList.add(this.pojemnoscDysku);
        arrayList.add(this.rodzajDysku);
        arrayList.add(this.nazwaGrafiki);
        arrayList.add(this.pamiecGrafiki);
        arrayList.add(this.nazwaOs);
        arrayList.add(this.napedFizyczny);
        return arrayList;
    }





}
