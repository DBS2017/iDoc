package modelo;

import java.util.Date;


public class Reclamacion {

    Persona persona = new Persona();
    int IDREC;

    String ASUREC, ESTREC, IDPER, IDARE;
    
    Date FECREC;

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public int getIDREC() {
        return IDREC;
    }

    public void setIDREC(int IDREC) {
        this.IDREC = IDREC;
    }

    public String getIDPER() {
        return IDPER;
    }

    public void setIDPER(String IDPER) {
        this.IDPER = IDPER;
    }

    public String getIDARE() {
        return IDARE;
    }

    public void setIDARE(String IDARE) {
        this.IDARE = IDARE;
    }

    public String getASUREC() {
        return ASUREC;
    }

    public void setASUREC(String ASUREC) {
        this.ASUREC = ASUREC;
    }

    public String getESTREC() {
        return ESTREC;
    }

    public void setESTREC(String ESTREC) {
        this.ESTREC = ESTREC;
    }

    public Date getFECREC() {
        return FECREC;
    }

    public void setFECREC(Date FECREC) {
        this.FECREC = FECREC;
    }

    
}
