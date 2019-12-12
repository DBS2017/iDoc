package modelo;

public class Requerimiento {

    Tupa tupa = new Tupa();

    int IDREQ;
    String NOMREQ, ESTREQ;

    public int getIDREQ() {
        return IDREQ;
    }

    public void setIDREQ(int IDREQ) {
        this.IDREQ = IDREQ;
    }

    public String getNOMREQ() {
        return NOMREQ;
    }

    public void setNOMREQ(String NOMREQ) {
        this.NOMREQ = NOMREQ;
    }

    public String getESTREQ() {
        return ESTREQ;
    }

    public void setESTREQ(String ESTREQ) {
        this.ESTREQ = ESTREQ;
    }

    public Tupa getTupa() {
        return tupa;
    }

    public void setTupa(Tupa tupa) {
        this.tupa = tupa;
    }

}
