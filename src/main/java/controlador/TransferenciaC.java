package controlador;

import dao.DocumentoImpl;
import dao.TransferenciaImpl;
import dao.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import modelo.Area;
import modelo.Documento;
import modelo.Transferencia;
import org.apache.commons.lang.time.DateUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.model.StreamedContent;

@Named(value = "transferenciaC")
@SessionScoped
public class TransferenciaC implements Serializable {

    Transferencia transferencia;
    Transferencia selectedTransferencia;
    List<Transferencia> lstTransferencia, lstTransferenciaFiltrado;
    List<Transferencia> listaBandejaEntrada, listaBandejaSalida, listaBandejaEntradaFiltrada, listaBandejaSalidaFiltrada, listaBandejaSeleccionado;
    TransferenciaImpl dao;
    boolean tipoBandeja;
    List<Documento> listaDocumentos, listaDocumentosFiltrado;
    Documento documento;
    int currentExp;

    int receptor;

    StreamedContent reporte;

    public TransferenciaC() {
        transferencia = new Transferencia();
        lstTransferencia = new ArrayList<>();
        listaBandejaEntrada = new ArrayList<>();
        listaBandejaSalida = new ArrayList<>();
//        listaBandejaEntradaFiltrada = new ArrayList<>();
//        listaBandejaSalidaFiltrada = new ArrayList<>();
        dao = new TransferenciaImpl();
        selectedTransferencia = new Transferencia();
        listaBandejaSeleccionado = new ArrayList<>();
        listaDocumentos = new ArrayList<>();
        documento = new Documento();
    }

    @PostConstruct
    public void iniciar() {
        try {
            listarTransferencia();
            listarBandeja();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void limpiarTransferencia() throws Exception {
        transferencia = new Transferencia();
    }

    public void registrarTransferencia() throws Exception {
        try {
            dao.registrar(transferencia);
            limpiarTransferencia();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registrado Correctamente", null));
            PrimeFaces.current().executeScript("enviar('" + "Transferencia" + "');");
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al Registrar" + e, null));
        }
    }

    public void editarTransferencia() throws Exception {
        try {
            dao.editar(selectedTransferencia);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Modificado Correctamente", null));
            PrimeFaces.current().executeScript("enviar('" + "Transferencia" + "');");
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al Modificar" + e, null));
        }
    }

    public void marcarSalida() throws Exception {
        try {
            if (listaBandejaSeleccionado.size() > 0) {
                dao.marcarSalida(listaBandejaSeleccionado);
                listaBandejaSeleccionado = new ArrayList<>();
                listaBandejaSalida = dao.listarBandejaSalida();
                PrimeFaces.current().executeScript("enviar('Bandeja');");
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Marcado de salida satisfactorio", null));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Seleccione almenos una derivación", null));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void agregarDocumento() throws Exception {
        try {
            documento.setExpediente(selectedTransferencia.getExpediente());
            actualizarCadena();
            DocumentoImpl daoDocumento = new DocumentoImpl();
            daoDocumento.registrar(documento);
            listarDocumentos();
            documento = new Documento();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Agregado Correctamente", null));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
            e.printStackTrace();
        }
    }

    public void actualizarCadena() throws Exception {
        Utils u = new Utils();
        documento.setCADDOC(u.obtenerCadenaDocumento(servicios.SesionS.getSesion().getTrabajador().getArea().getIDARE(), receptor, documento.getINIJEF(), documento.getNUMDOC()));
    }

    public void currentExpediente() throws Exception {
        Utils u = new Utils();
        currentExp = u.obtenerNumeroExpediente();
    }

    public void listarDocumentos() throws Exception {
        try {
            listaDocumentos = dao.listarDocumentoExpediente(selectedTransferencia);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarDocumentos(Transferencia s) throws Exception {
        try {
            listaDocumentos = dao.listarDocumentoExpediente(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarTransferencia() throws Exception {
        try {
            dao.eliminar(selectedTransferencia);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminado Correctamente", null));
            PrimeFaces.current().executeScript("enviar('Bandeja');");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al Eliminar" + e, null));

        }
    }

    public void listarBandeja() throws Exception {
        try {
            listaBandejaEntrada = dao.listarBandejaEntrada();
            listaBandejaEntradaFiltrada = listaBandejaEntrada;
            listaBandejaSalida = dao.listarBandejaSalida();
            listaBandejaSalidaFiltrada = listaBandejaSalida;
            currentExpediente();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void recepcionarOrRechazar(Transferencia tran) throws Exception {
        try {
            selectedTransferencia = tran;
            switch (tran.getEstadoTransferencia()) {
                case "1":
                    recepcionarTransferencia();
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Recepcionado", null));
                    break;
                case "2":
                    rechazarTransferencia();
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Rechazado", null));
                    break;
            }
            PrimeFaces.current().executeScript("enviar('Bandeja');");
//            listarBandeja();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void recepcionarTransferencia(Transferencia tran) throws Exception {
        try {
            selectedTransferencia = tran;
            recepcionarTransferencia();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void recepcionarTransferencia() throws Exception {
        try {
            selectedTransferencia.setESTTRA("1");
            dao.recibir(selectedTransferencia);
            PrimeFaces.current().executeScript("enviar('" + "Bandeja" + "');");
            PrimeFaces.current().executeScript("enviar('" + "Transferencia" + "');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rechazarTransferencia() throws Exception {
        try {
            selectedTransferencia.setESTTRA("2");
            dao.recibir(selectedTransferencia);
            selectedTransferencia.setAreaReceptora(selectedTransferencia.getAreaEmisora());
            selectedTransferencia.setAreaEmisora(servicios.SesionS.getSesion().getTrabajador().getArea());
            selectedTransferencia.setOBSTRAN(selectedTransferencia.getOBSDER());
            derivar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void derivar(Transferencia tran) throws Exception {
        try {
            selectedTransferencia = tran;
            selectedTransferencia.setAreaEmisora(servicios.SesionS.getSesion().getTrabajador().getArea());

            Area t = new Area();
            t.setIDARE(selectedTransferencia.getAreaDerivada().getIDARE());
            selectedTransferencia.setAreaReceptora(t);
            derivar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void derivar() throws Exception {
        try {
            selectedTransferencia.setAreaEmisora(servicios.SesionS.getSesion().getTrabajador().getArea());
            dao.actualizarDerivacion(selectedTransferencia);
            dao.registrar(selectedTransferencia);
//            listarBandeja();
            PrimeFaces.current().executeScript("enviar('Bandeja');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarTransferencia() throws Exception {
        try {
            //lstTransferencia = dao.listar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void seleccionaTodo(ToggleSelectEvent e) throws Exception {
        if (e.isSelected()) {
            if (listaBandejaSeleccionado.size() > 5) {
                listaBandejaSeleccionado = listaBandejaSeleccionado.subList(0, 5);
            }
        }

    }

//    public void generarReporte() throws Exception {
//        try {
//            reporte = dao.generarReporteIndividualPrev(transferencia);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public void generarReporte2(int IDARE) throws Exception {
        TransferenciaImpl report = new TransferenciaImpl();
        try {
            Map<String, Object> parameters = new HashMap(); // Libro de parametros
            parameters.put("IDARE", IDARE); //Insertamos un parametro
            report.generarReporteIndividual2(parameters); //Pido exportar Reporte con los parametros
//            report.exportarPDF2(parameters);

        } catch (Exception e) {
            throw e;
        }
    }

    public void REPORTE(int IDDOC) throws Exception {
        TransferenciaImpl report = new TransferenciaImpl();
        try {
            Map<String, Object> parameters = new HashMap(); // Libro de parametros
            parameters.put(null, IDDOC); //Insertamos un parametro
            report.generarReporteIndividual(parameters); //Pido exportar Reporte con los parametros
//            report.exportarPDF2(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean filterByDate(Object value, Object filter, Locale locale) {

        if (filter == null) {
            return true;
        }

        if (value == null) {
            return false;
        }

        return DateUtils.truncatedEquals((Date) filter, (Date) value, Calendar.DATE);
    }

    public Transferencia getTransferencia() {
        return transferencia;
    }

    public void setTransferencia(Transferencia transferencia) {
        this.transferencia = transferencia;
    }

    public Transferencia getSelectedTransferencia() {
        return selectedTransferencia;
    }

    public void setSelectedTransferencia(Transferencia selectedTransferencia) {
        this.selectedTransferencia = selectedTransferencia;
    }

    public List<Transferencia> getLstTransferencia() {
        return lstTransferencia;
    }

    public void setLstTransferencia(List<Transferencia> lstTransferencia) {
        this.lstTransferencia = lstTransferencia;
    }

    public List<Transferencia> getLstTransferenciaFiltrado() {
        return lstTransferenciaFiltrado;
    }

    public void setLstTransferenciaFiltrado(List<Transferencia> lstTransferenciaFiltrado) {
        this.lstTransferenciaFiltrado = lstTransferenciaFiltrado;
    }

    public List<Transferencia> getListaBandejaEntradaFiltrada() {
        return listaBandejaEntradaFiltrada;
    }

    public void setListaBandejaEntradaFiltrada(List<Transferencia> listaBandejaEntradaFiltrada) {
        this.listaBandejaEntradaFiltrada = listaBandejaEntradaFiltrada;
    }

    public StreamedContent getReporte() {
        return reporte;
    }

    public void setReporte(StreamedContent reporte) {
        this.reporte = reporte;
    }

    public List<Transferencia> getListaBandejaEntrada() {
        return listaBandejaEntrada;
    }

    public void setListaBandejaEntrada(List<Transferencia> listaBandejaEntrada) {
        this.listaBandejaEntrada = listaBandejaEntrada;
    }

    public List<Transferencia> getListaBandejaSalida() {
        return listaBandejaSalida;
    }

    public void setListaBandejaSalida(List<Transferencia> listaBandejaSalida) {
        this.listaBandejaSalida = listaBandejaSalida;
    }

    public boolean isTipoBandeja() {
        return tipoBandeja;
    }

    public void setTipoBandeja(boolean tipoBandeja) {
        this.tipoBandeja = tipoBandeja;
    }

    public List<Transferencia> getListaBandejaSeleccionado() {
        return listaBandejaSeleccionado;
    }

    public void setListaBandejaSeleccionado(List<Transferencia> listaBandejaSeleccionado) {
        this.listaBandejaSeleccionado = listaBandejaSeleccionado;
    }

    public List<Transferencia> getListaBandejaSalidaFiltrada() {
        return listaBandejaSalidaFiltrada;
    }

    public void setListaBandejaSalidaFiltrada(List<Transferencia> listaBandejaSalidaFiltrada) {
        this.listaBandejaSalidaFiltrada = listaBandejaSalidaFiltrada;
    }

    public List<Documento> getListaDocumentos() {
        return listaDocumentos;
    }

    public void setListaDocumentos(List<Documento> listaDocumentos) {
        this.listaDocumentos = listaDocumentos;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public int getReceptor() {
        return receptor;
    }

    public void setReceptor(int receptor) {
        this.receptor = receptor;
    }

    public List<Documento> getListaDocumentosFiltrado() {
        return listaDocumentosFiltrado;
    }

    public void setListaDocumentosFiltrado(List<Documento> listaDocumentosFiltrado) {
        this.listaDocumentosFiltrado = listaDocumentosFiltrado;
    }

    public int getCurrentExp() {
        return currentExp;
    }

    public void setCurrentExp(int currentExp) {
        this.currentExp = currentExp;
    }

}
