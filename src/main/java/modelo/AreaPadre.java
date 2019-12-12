package modelo;

import java.util.Objects;

public class AreaPadre {

    private int IDARE;
    private String NOMARE, ESTARE, INARE, CADARE;
    private Municipalidad municipalidad = new Municipalidad();

    public void clear() {
        this.IDARE = 0;
        this.NOMARE = null;
        this.ESTARE = null;
        this.INARE = null;
        this.CADARE = null;
        this.municipalidad.clear();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.NOMARE);
        hash = 79 * hash + Objects.hashCode(this.municipalidad);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AreaPadre other = (AreaPadre) obj;
        if (!Objects.equals(this.NOMARE, other.NOMARE)) {
            return false;
        }
        return Objects.equals(this.municipalidad, other.municipalidad);
    }

    public int getIDARE() {
        return IDARE;
    }

    public void setIDARE(int IDARE) {
        this.IDARE = IDARE;
    }

    public String getNOMARE() {
        return NOMARE;
    }

    public void setNOMARE(String NOMARE) {
        this.NOMARE = NOMARE;
    }

    public String getESTARE() {
        return ESTARE;
    }

    public void setESTARE(String ESTARE) {
        this.ESTARE = ESTARE;
    }

    public Municipalidad getMunicipalidad() {
        return municipalidad;
    }

    public void setMunicipalidad(Municipalidad municipalidad) {
        this.municipalidad = municipalidad;
    }

    public String getINARE() {
        return INARE;
    }

    public void setINARE(String INARE) {
        this.INARE = INARE;
    }

    public String getCADARE() {
        return CADARE;
    }

    public void setCADARE(String CADARE) {
        this.CADARE = CADARE;
    }

}
