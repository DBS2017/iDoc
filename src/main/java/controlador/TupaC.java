package controlador;

import dao.RequerimientoImpl;
import dao.TupaImpl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import modelo.Requerimiento;
import modelo.Tupa;
import org.primefaces.PrimeFaces;

@Named(value = "tupaC")
@SessionScoped
public class TupaC implements Serializable {

    Tupa tupa;
    Tupa selectedTupa;
    List<Tupa> lstTupa, filtrado;
    TupaImpl dao;

    Requerimiento requerimiento, requerimientoSeleccionado, requerimientoReSeleccionado;
    RequerimientoImpl daoRequerimiento;
    List<Requerimiento> listaRequerimiento, listaRequerimientoFiltrado;

    public TupaC() {
        tupa = new Tupa();
        lstTupa = new ArrayList<>();
        filtrado = new ArrayList<>();
        selectedTupa = new Tupa();
        dao = new TupaImpl();

        requerimiento = new Requerimiento();
        requerimientoSeleccionado = new Requerimiento();
        requerimientoReSeleccionado = new Requerimiento();

        daoRequerimiento = new RequerimientoImpl();
        listaRequerimiento = new ArrayList<>();
        listaRequerimientoFiltrado = new ArrayList<>();
    }

    @PostConstruct
    public void iniciar() {
        try {
            listarTupa();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void limpiarTupa() throws Exception {
        tupa = new Tupa();
    }

    public void registrarTupa() throws Exception {
        try {
            dao.registrar(tupa);
//            listarTupa();
            limpiarTupa();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registrado Correctamente", null));
            PrimeFaces.current().executeScript("enviar('Tupa')");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al Registrar" + e, null));
        }
    }

    public void editarTupa() throws Exception {
        try {
            dao.editar(selectedTupa);
//            listarTupa();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Modificado Correctamente", null));
            PrimeFaces.current().executeScript("enviar('Tupa')");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al Modificar" + e, null));
        }
    }

    public void eliminarTupa() throws Exception {
        try {
            dao.eliminar(selectedTupa);
//            listarTupa();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminado Correctamente", null));
            PrimeFaces.current().executeScript("enviar('Tupa')");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al Eliminar" + e, null));
        }
    }

    public void listarTupa() throws Exception {
        try {
            lstTupa = dao.listar();
        } catch (Exception e) {
            throw e;

        }
    }

    public void registrarRequerimiento() {
        try {
            daoRequerimiento.registrar(requerimiento);
            listarRequerimiento();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Agregado Correctamente", null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editarRequerimiento() {
        try {
            daoRequerimiento.editar(requerimientoReSeleccionado);
            listarRequerimiento();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Editado Correctamente", null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarRequerimiento() {
        try {
            daoRequerimiento.eliminar(requerimientoReSeleccionado);
            listarRequerimiento();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminado Correctamente", null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarRequerimiento() {
        try {
            requerimiento.setTupa(selectedTupa);
            requerimientoSeleccionado.setTupa(selectedTupa);
            listaRequerimiento = daoRequerimiento.listar(requerimientoSeleccionado);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
        public void generarReporte(int IDTUP) throws Exception {
        TupaImpl report = new TupaImpl();
        try {
            Map<String, Object> parameters = new HashMap(); // Libro de parametros
            parameters.put("IDTUP", IDTUP); //Insertamos un parametro
            report.generarReporteIndividual(parameters); //Pido exportar Reporte con los parametros
//            report.exportarPDF2(parameters);
        } catch (Exception e) {
            throw e;
        }
    }

    public Requerimiento getRequerimiento() {
        return requerimiento;
    }

    public void setRequerimiento(Requerimiento requerimiento) {
        this.requerimiento = requerimiento;
    }

    public List<Requerimiento> getListaRequerimiento() {
        return listaRequerimiento;
    }

    public void setListaRequerimiento(List<Requerimiento> listaRequerimiento) {
        this.listaRequerimiento = listaRequerimiento;
    }

    public List<Requerimiento> getListaRequerimientoFiltrado() {
        return listaRequerimientoFiltrado;
    }

    public void setListaRequerimientoFiltrado(List<Requerimiento> listaRequerimientoFiltrado) {
        this.listaRequerimientoFiltrado = listaRequerimientoFiltrado;
    }

    public Requerimiento getRequerimientoReSeleccionado() {
        return requerimientoReSeleccionado;
    }

    public void setRequerimientoReSeleccionado(Requerimiento requerimientoReSeleccionado) {
        this.requerimientoReSeleccionado = requerimientoReSeleccionado;
    }

    public Tupa getTupa() {
        return tupa;
    }

    public void setTupa(Tupa tupa) {
        this.tupa = tupa;
    }

    public Tupa getSelectedTupa() {
        return selectedTupa;
    }

    public void setSelectedTupa(Tupa selectedTupa) {
        this.selectedTupa = selectedTupa;
    }

    public List<Tupa> getLstTupa() {
        return lstTupa;
    }

    public void setLstTupa(List<Tupa> lstTupa) {
        this.lstTupa = lstTupa;
    }

    public List<Tupa> getFiltrado() {
        return filtrado;
    }

    public void setFiltrado(List<Tupa> filtrado) {
        this.filtrado = filtrado;
    }

    public Requerimiento getRequerimientoSeleccionado() {
        return requerimientoSeleccionado;
    }

    public void setRequerimientoSeleccionado(Requerimiento requerimientoSeleccionado) {
        this.requerimientoSeleccionado = requerimientoSeleccionado;
    }

}
