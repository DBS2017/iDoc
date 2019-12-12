package modelo;

import java.util.Date;

public class Transferencia {

    private int IDTRAN;
    private Date FECRECTRAN = new Date();
    private Date FECSALTRAN = new Date();
    private Date FECINGTRAN = new Date();
    private String OBSTRAN, OBSDER;
    private String ESTTRA;
    private Expediente expediente = new Expediente();
    private Area areaEmisora = new Area();
    private Area areaReceptora = new Area();
    private Area areaDerivada = new Area();
    private int diasHabiles;
    private boolean fueraDePlazo;
//    private String CADTRAN;
    private String inicialesJefe;
    private String estadoTransferencia; //triStateCheckbox (0)=> no recepcionado (1)=>recepcionado (2)=>rechazado

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

    public Date getFECSALTRAN() {
        return FECSALTRAN;
    }

    public void setFECSALTRAN(Date FECSALTRAN) {
        this.FECSALTRAN = FECSALTRAN;
    }

    public String getOBSTRAN() {
        return OBSTRAN;
    }

    public void setOBSTRAN(String OBSTRAN) {
        this.OBSTRAN = OBSTRAN;
    }

    public String getESTTRA() {
        return ESTTRA;
    }

    public void setESTTRA(String ESTTRA) {
        this.ESTTRA = ESTTRA;
    }

    public Area getAreaEmisora() {
        return areaEmisora;
    }

    public void setAreaEmisora(Area areaEmisora) {
        this.areaEmisora = areaEmisora;
    }

    public Area getAreaReceptora() {
        return areaReceptora;
    }

    public void setAreaReceptora(Area areaReceptora) {
        this.areaReceptora = areaReceptora;
    }

    public String getOBSDER() {
        return OBSDER;
    }

    public void setOBSDER(String OBSDER) {
        this.OBSDER = OBSDER;
    }

    public int getDiasHabiles() {
        return diasHabiles;
    }

    public void setDiasHabiles(int diasHabiles) {
        this.diasHabiles = diasHabiles;
    }

    public boolean isFueraDePlazo() {
        return fueraDePlazo;
    }

    public void setFueraDePlazo(boolean fueraDePlazo) {
        this.fueraDePlazo = fueraDePlazo;
    }

    public Area getAreaDerivada() {
        return areaDerivada;
    }

    public void setAreaDerivada(Area areaDerivada) {
        this.areaDerivada = areaDerivada;
    }

    public String getInicialesJefe() {
        return inicialesJefe;
    }

    public void setInicialesJefe(String inicialesJefe) {
        this.inicialesJefe = inicialesJefe;
    }

    public Date getFECINGTRAN() {
        return FECINGTRAN;
    }

    public void setFECINGTRAN(Date FECINGTRAN) {
        this.FECINGTRAN = FECINGTRAN;
    }

    public String getEstadoTransferencia() {
        return estadoTransferencia;
    }

    public void setEstadoTransferencia(String estadoTransferencia) {
        this.estadoTransferencia = estadoTransferencia;
    }

    public Expediente getExpediente() {
        return expediente;
    }

    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

}
