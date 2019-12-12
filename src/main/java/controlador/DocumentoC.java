package controlador;

import dao.DocumentoImpl;
import dao.ExpedienteImpl;
import dao.TransferenciaImpl;
import dao.Utils;
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
import modelo.Area;
import modelo.Documento;
import modelo.Transferencia;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

@Named(value = "documentoC")
@SessionScoped
public class DocumentoC implements Serializable {

    Transferencia transferencia;
    Documento selectedDocumento, documento, seleccionado;
    List<Documento> lista, listaFiltrado;
    List<Area> listaCircular, listaCircularFiltrado;
    DocumentoImpl daoDocumento;
    ExpedienteImpl daoExpediente;
    TransferenciaImpl daoTransferencia;
    Utils util;
    boolean circular;
    ScheduleModel calendario;
    ScheduleEvent evento;

    public DocumentoC() {
        transferencia = new Transferencia();
        selectedDocumento = new Documento();
        daoDocumento = new DocumentoImpl();
        daoExpediente = new ExpedienteImpl();
        daoTransferencia = new TransferenciaImpl();
        listaCircular = new ArrayList<>();
        documento = new Documento();
        util = new Utils();
        lista = new ArrayList<>();
        calendario = new DefaultScheduleModel();
        evento = new DefaultScheduleEvent();
        seleccionado = new Documento();
    }

    @PostConstruct
    public void iniciar() {
        try {
            generarCalendario();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void registrarCircular() throws Exception {
        try {

            if (listaCircular.size() > 1) {

                daoExpediente.registrar(transferencia.getExpediente());
                transferencia.setOBSTRAN(documento.getOBSDOC());
                transferencia.getExpediente().setIDEXP(util.currentExpediente());
                transferencia.getAreaEmisora().setIDARE(servicios.SesionS.getSesion().getTrabajador().getArea().getIDARE());

                for (Area a : listaCircular) {
                    transferencia.setAreaReceptora(a);
                    daoTransferencia.registrar(transferencia);
                }

                documento.setExpediente(transferencia.getExpediente());
                documento.setCADDOC(util.obtenerCadenaDocumento(servicios.SesionS.getSesion().getTrabajador().getArea().getIDARE(), transferencia.getAreaReceptora().getIDARE(), documento.getINIJEF(), documento.getNUMDOC()));
                daoDocumento.registrar(documento);
                transferencia = new Transferencia();
                documento = new Documento();
                listaCircular = new ArrayList<>();
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Agregado Correctamente", null));
            }else{
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Seleccione almenos 2 áreas", null));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registrar() throws Exception {
        try {
            daoExpediente.registrar(transferencia.getExpediente());
            transferencia.setOBSTRAN(documento.getOBSDOC());
            transferencia.getExpediente().setIDEXP(util.currentExpediente());
            transferencia.getAreaEmisora().setIDARE(servicios.SesionS.getSesion().getTrabajador().getArea().getIDARE());
            daoTransferencia.registrar(transferencia);
            documento.setExpediente(transferencia.getExpediente());
            documento.setCADDOC(util.obtenerCadenaDocumento(servicios.SesionS.getSesion().getTrabajador().getArea().getIDARE(), transferencia.getAreaReceptora().getIDARE(), documento.getINIJEF(), documento.getNUMDOC()));
            daoDocumento.registrar(documento);
            transferencia = new Transferencia();
            documento = new Documento();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Agregado Correctamente", null));
//            listarDocumento();
            PrimeFaces.current().executeScript("enviar('" + "Documento" + "');");
            PrimeFaces.current().executeScript("enviar('" + "Transferencia" + "');");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
            e.printStackTrace();
        }
    }

    public void modificarDocumento() throws Exception {
        try {
            selectedDocumento.setLogin(servicios.SesionS.getSesion());
            daoDocumento.editar(selectedDocumento);
//            listarDocumento();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Modificado Correctamente", null));
            PrimeFaces.current().executeScript("enviar('" + "Documento" + "');");
            PrimeFaces.current().executeScript("enviar('" + "Transferencia" + "');");
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al Modificar" + e, null));
        }
    }

    public void eliminarDocumento() throws Exception {
        try {
            selectedDocumento.setLogin(servicios.SesionS.getSesion());
            daoDocumento.eliminar(selectedDocumento);
//            listarDocumento();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminado Correctamente", null));
            PrimeFaces.current().executeScript("enviar('" + "Documento" + "');");
            PrimeFaces.current().executeScript("enviar('" + "Transferencia" + "');");
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al Eliminar", null));
        }
    }
    
    public void duplicarInput(String v) throws Exception{
        documento.setASUDOC(v);
    }

    public void listarDocumento() {
        try {
            if (servicios.SesionS.getSesion().getTIPLOG().charAt(1) == 'T') {
//                lista = daoDocumento.listar();
            } else {
//                lista = daoDocumento.listar(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //DESCARGAR REPORTE DE ALUMNOS
    public void reporte(int IDDOC) throws Exception {
        DocumentoImpl report = new DocumentoImpl();
        try {
            Map<String, Object> parameters = new HashMap(); // Libro de parametros
            parameters.put("IDDOC", IDDOC); //Insertamos un parametro
            report.generarReporteIndividual(parameters); //Pido exportar Reporte con los parametros
//            report.exportarPDF2(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void generarCalendario() throws Exception{
        try {
            calendario = daoDocumento.obtenerCalendario();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void onEventSelect(SelectEvent selectEvent){
        evento = (ScheduleEvent) selectEvent.getObject();
        seleccionado = (Documento) evento.getData();
    }

    public Documento getSelectedDocumento() {
        return selectedDocumento;
    }

    public void setSelectedDocumento(Documento selectedDocumento) {
        this.selectedDocumento = selectedDocumento;
    }

    public List<Documento> getLista() {
        return lista;
    }

    public void setLista(List<Documento> lista) {
        this.lista = lista;
    }

    public List<Documento> getListaFiltrado() {
        return listaFiltrado;
    }

    public void setListaFiltrado(List<Documento> listaFiltrado) {
        this.listaFiltrado = listaFiltrado;
    }

    public Transferencia getTransferencia() {
        return transferencia;
    }

    public void setTransferencia(Transferencia transferencia) {
        this.transferencia = transferencia;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public boolean isCircular() {
        return circular;
    }

    public void setCircular(boolean circular) {
        this.circular = circular;
    }

    public List<Area> getListaCircular() {
        return listaCircular;
    }

    public void setListaCircular(List<Area> listaCircular) {
        this.listaCircular = listaCircular;
    }

    public List<Area> getListaCircularFiltrado() {
        return listaCircularFiltrado;
    }

    public void setListaCircularFiltrado(List<Area> listaCircularFiltrado) {
        this.listaCircularFiltrado = listaCircularFiltrado;
    }

    public ScheduleModel getCalendario() {
        return calendario;
    }

    public void setCalendario(ScheduleModel calendario) {
        this.calendario = calendario;
    }

    public ScheduleEvent getEvento() {
        return evento;
    }

    public void setEvento(ScheduleEvent evento) {
        this.evento = evento;
    }

    public Documento getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(Documento seleccionado) {
        this.seleccionado = seleccionado;
    }

}
