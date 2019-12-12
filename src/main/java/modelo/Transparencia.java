package modelo;

import java.util.Date;

public class Transparencia {
    int IDTRAN;
    Date FECRECTRAN = new Date();
    Date FECINGTRAN = new Date();
    String OBSTRAN;
    String OBSDER;
    Area emisor = new Area();
    Area receptor = new Area();
    String ESTTRA;
    Expediente expediente = new Expediente();

    String KEYEXP, dni;
    int NUMEXP;

    public int getIDTRAN() {
        return IDTRAN;
    }

    public void setIDTRAN(int IDTRAN) {
        this.IDTRAN = IDTRAN;
    }

    public Date getFECRECTRAN() {
        return FECRECTRAN;
    }

    public void setFECRECTRAN(Date FECRECTRAN) {
        this.FECRECTRAN = FECRECTRAN;
    }

    public Date getFECINGTRAN() {
        return FECINGTRAN;
    }

    public void setFECINGTRAN(Date FECINGTRAN) {
        this.FECINGTRAN = FECINGTRAN;
    }

    public String getOBSTRAN() {
        return OBSTRAN;
    }

    public void setOBSTRAN(String OBSTRAN) {
        this.OBSTRAN = OBSTRAN;
    }

    public String getOBSDER() {
        return OBSDER;
    }

    public void setOBSDER(String OBSDER) {
        this.OBSDER = OBSDER;
    }

    public Area getEmisor() {
        return emisor;
    }

    public void setEmisor(Area emisor) {
        this.emisor = emisor;
    }

    public Area getReceptor() {
        return receptor;
    }

    public void setReceptor(Area receptor) {
        this.receptor = receptor;
    }

    public String getESTTRA() {
        return ESTTRA;
    }

    public void setESTTRA(String ESTTRA) {
        this.ESTTRA = ESTTRA;
    }

    public Expediente getExpediente() {
        return expediente;
    }

    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    public String getKEYEXP() {
        return KEYEXP;
    }

    public void setKEYEXP(String KEYEXP) {
        this.KEYEXP = KEYEXP;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public int getNUMEXP() {
        return NUMEXP;
    }

    public void setNUMEXP(int NUMEXP) {
        this.NUMEXP = NUMEXP;
    }




}
