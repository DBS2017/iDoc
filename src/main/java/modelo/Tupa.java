package modelo;

public class Tupa {

    private int IDTUP = 20;
    private String NUMTUP;
    private String NOMTUP;
    private double PRETUP;
    private int PLATUP;
    private Area area = new Area();
    private String ESTTUP;


    public int getIDTUP() {
        return IDTUP;
    }

    public void setIDTUP(int IDTUP) {
        this.IDTUP = IDTUP;
    }

    public String getNUMTUP() {
        return NUMTUP;
    }

    public void setNUMTUP(String NUMTUP) {
        this.NUMTUP = NUMTUP;
    }

    public String getNOMTUP() {
        return NOMTUP;
    }

    public void setNOMTUP(String NOMTUP) {
        this.NOMTUP = NOMTUP;
    }

    public double getPRETUP() {
        return PRETUP;
    }

    public void setPRETUP(double PRETUP) {
        this.PRETUP = PRETUP;
    }

    public int getPLATUP() {
        return PLATUP;
    }

    public void setPLATUP(int PLATUP) {
        this.PLATUP = PLATUP;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public String getESTTUP() {
        return ESTTUP;
    }

    public void setESTTUP(String ESTTUP) {
        this.ESTTUP = ESTTUP;
    }

}
