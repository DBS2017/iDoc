package modelo;

import java.util.Date;

public class Expediente {

    private int IDEXP;
    private int NUMEXP;
    private Login login = new Login();
    private Date FECEXP_EN;
    private Date FECEXP_SAL;
    private String ASUEXP;
    private String KEYEXP;
    private String ESTEXP;
    private String TIPEXP = "E"; // I (interno) E (externo)

    public int getIDEXP() {
        return IDEXP;
    }

    public void setIDEXP(int IDEXP) {
        this.IDEXP = IDEXP;
    }

    public int getNUMEXP() {
        return NUMEXP;
    }

    public void setNUMEXP(int NUMEXP) {
        this.NUMEXP = NUMEXP;
    }

    public Date getFECEXP_EN() {
        return FECEXP_EN;
    }

    public void setFECEXP_EN(Date FECEXP_EN) {
        this.FECEXP_EN = FECEXP_EN;
    }

    public Date getFECEXP_SAL() {
        return FECEXP_SAL;
    }

    public void setFECEXP_SAL(Date FECEXP_SAL) {
        this.FECEXP_SAL = FECEXP_SAL;
    }

    public String getASUEXP() {
        return ASUEXP;
    }

    public void setASUEXP(String ASUEXP) {
        this.ASUEXP = ASUEXP;
    }

    public String getKEYEXP() {
        return KEYEXP;
    }

    public void setKEYEXP(String KEYEXP) {
        this.KEYEXP = KEYEXP;
    }

    public String getESTEXP() {
        return ESTEXP;
    }

    public void setESTEXP(String ESTEXP) {
        this.ESTEXP = ESTEXP;
    }

    public String getTIPEXP() {
        return TIPEXP;
    }

    public void setTIPEXP(String TIPEXP) {
        this.TIPEXP = TIPEXP;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

}
