package modelo;

import java.util.Date;

public class Documento {

    private int IDDOC;
    private int NUMFOLDOC;
    private Date FECDOC = new Date();
    private Date entrega = new Date();
    private String ASUDOC;
    private String OBSDOC;
    private String ESTDOC;
    private Expediente expediente = new Expediente();
    private Persona persona = new Persona();
    private Login login = new Login();
    private Empresa empresa = new Empresa();
    private Tupa tupa = new Tupa();
    private DocumentoTipo documentotipo = new DocumentoTipo();
    private String CADDOC;

    private int TRANSDOC;
    private String OUTPLAZ;

    private int NUMDOC;
    private String INIJEF;

    int NUMEXP;

    public void clear() {

    }

    public int getIDDOC() {
        return IDDOC;
    }

    public void setIDDOC(int IDDOC) {
        this.IDDOC = IDDOC;
    }

    public int getNUMFOLDOC() {
        return NUMFOLDOC;
    }

    public void setNUMFOLDOC(int NUMFOLDOC) {
        this.NUMFOLDOC = NUMFOLDOC;
    }

    public DocumentoTipo getDocumentotipo() {
        return documentotipo;
    }

    public void setDocumentotipo(DocumentoTipo documentotipo) {
        this.documentotipo = documentotipo;
    }

    public Date getFECDOC() {
        return FECDOC;
    }

    public void setFECDOC(Date FECDOC) {
        this.FECDOC = FECDOC;
    }

    public String getASUDOC() {
        return ASUDOC;
    }

    public void setASUDOC(String ASUDOC) {
        this.ASUDOC = ASUDOC;
    }

    public String getOBSDOC() {
        return OBSDOC;
    }

    public void setOBSDOC(String OBSDOC) {
        this.OBSDOC = OBSDOC;
    }

    public String getESTDOC() {
        return ESTDOC;
    }

    public void setESTDOC(String ESTDOC) {
        this.ESTDOC = ESTDOC;
    }

    public Tupa getTupa() {
        return tupa;
    }

    public void setTupa(Tupa tupa) {
        this.tupa = tupa;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Expediente getExpediente() {
        return expediente;
    }

    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    public String getCADDOC() {
        return CADDOC;
    }

    public void setCADDOC(String CADDOC) {
        this.CADDOC = CADDOC;
    }

    public int getNUMDOC() {
        return NUMDOC;
    }

    public void setNUMDOC(int NUMDOC) {
        this.NUMDOC = NUMDOC;
    }

    public String getINIJEF() {
        return INIJEF;
    }

    public void setINIJEF(String INIJEF) {
        this.INIJEF = INIJEF;
    }

    public int getNUMEXP() {
        return NUMEXP;
    }

    public void setNUMEXP(int NUMEXP) {
        this.NUMEXP = NUMEXP;
    }

    public int getTRANSDOC() {
        return TRANSDOC;
    }

    public void setTRANSDOC(int TRANSDOC) {
        this.TRANSDOC = TRANSDOC;
    }

    public String getOUTPLAZ() {
        return OUTPLAZ;
    }

    public void setOUTPLAZ(String OUTPLAZ) {
        this.OUTPLAZ = OUTPLAZ;
    }

    public Date getEntrega() {
        return entrega;
    }

    public void setEntrega(Date entrega) {
        this.entrega = entrega;
    }

}
