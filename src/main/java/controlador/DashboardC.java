package controlador;

import dao.ActaDashboardImpl;
import dao.BandejaDashboardImpl;
import dao.DocumentosDashboardImpl;
import dao.ReclamacionDashboardImpl;
import dao.TransferenciaDashboardImpl;
import dao.TupaDashboardImpl;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import modelo.Dashboard;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.hbar.HorizontalBarChartModel;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;

@Named(value = "dashboardC")
@SessionScoped
public class DashboardC implements Serializable {

    Dashboard dashboard;
//    ===================
    List<Dashboard> listaCantidadesActa;
    ActaDashboardImpl daoDashboard;
// =====DOC=====
    List<Dashboard> listaCantidadesDocumento;
    DocumentosDashboardImpl daoDashboardD;
//=====TUPA=======
    List<Dashboard> listaCantidadesTupa;
    TupaDashboardImpl daoDashboardTU;
//=====bandeja
    List<Dashboard> listaCantidadesBandeja;
    BandejaDashboardImpl daoDashboardBAN;

    TransferenciaDashboardImpl daoTransferencia;
    ReclamacionDashboardImpl daoReclamacion;
    LineChartModel derivaciones, reclamaciones;

    PieChartModel pieModel;
    HorizontalBarChartModel derivacionesArea;

    public DashboardC() {
        dashboard = new Dashboard();
        daoDashboard = new ActaDashboardImpl();
        listaCantidadesActa = new ArrayList<>();

        daoDashboardD = new DocumentosDashboardImpl();
        listaCantidadesDocumento = new ArrayList<>();

        daoDashboardTU = new TupaDashboardImpl();
        listaCantidadesTupa = new ArrayList<>();

        daoDashboardBAN = new BandejaDashboardImpl();
        listaCantidadesBandeja = new ArrayList<>();

        daoTransferencia = new TransferenciaDashboardImpl();
        daoReclamacion = new ReclamacionDashboardImpl();

        derivaciones = new LineChartModel();
        reclamaciones = new LineChartModel();
        derivacionesArea = new HorizontalBarChartModel();
    }

    @PostConstruct
    public void onInit() {
        try {
            if (servicios.SesionS.getSesion().getTIPLOG().charAt(1) == 'T') {
                listarDerivacionesArea();
                listarDerivaciones();
                listarCantidadesTupa();
                listarReclamaciones();
            } else {
                actas();
                listarCantidadesActa();

            }
            listarCantidadesBandeja();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actas() {
        pieModel = new PieChartModel();
        ChartData data = new ChartData();

        PieChartDataSet dataSet = new PieChartDataSet();
        List<Number> values = new ArrayList<>();

        for (Dashboard value : listaCantidadesActa) {
            values.add(value.getCantidad());
        }
        dataSet.setData(values);

        List<String> bgColors = new ArrayList<>();
        bgColors.add("rgb(255, 99, 132)");
        bgColors.add("rgb(54, 162, 235)");
        bgColors.add("rgb(255, 205, 86)");
        dataSet.setBackgroundColor(bgColors);

        data.addChartDataSet(dataSet);
        List<String> labels = new ArrayList<>();
        labels.add("Nacimiento");
        labels.add("Defunción");
        labels.add("Matrimonio");
        data.setLabels(labels);

        pieModel.setData(data);
    }

    public void listarDerivacionesArea() throws Exception {
        try {
            derivacionesArea = daoTransferencia.generarBarHorizontal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void documentos() {
//        pieModel = new PieChartModel();
//        ChartData data = new ChartData();
//
//        PieChartDataSet dataSet = new PieChartDataSet();
//        List<Number> values = new ArrayList<>();
//        values.add(15);
//        values.add(5);
//        values.add(14);
//        dataSet.setData(values);
//
//        List<String> bgColors = new ArrayList<>();
//        bgColors.add("rgb(255, 99, 132)");
//        bgColors.add("rgb(54, 162, 235)");
//        bgColors.add("rgb(255, 205, 86)");
//        dataSet.setBackgroundColor(bgColors);
//
//        data.addChartDataSet(dataSet);
//        List<String> labels = new ArrayList<>();
//        labels.add("Aceptado");
//        labels.add("Rechazado");
//        labels.add("Derivado");
//        data.setLabels(labels);
//
//        pieModel.setData(data);
    }

    public void listarDerivaciones() throws Exception {
        try {
            derivaciones = daoTransferencia.generarLinear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarReclamaciones() throws Exception {
        try {
            reclamaciones = daoReclamacion.generarLinear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarCantidadesBandeja() throws Exception {
        try {
            listaCantidadesBandeja = daoDashboardBAN.listarCantidades();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarCantidadesTupa() throws Exception {
        try {
            listaCantidadesTupa = daoDashboardTU.listarCantidades();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarCantidadesActa() throws Exception {
        try {
            listaCantidadesActa = daoDashboard.listarCantidades();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarCantidadesDocumento() throws Exception {
        try {
            listaCantidadesDocumento = daoDashboardD.listarCantidades();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Dashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

    public List<Dashboard> getListaCantidadesActa() {
        return listaCantidadesActa;
    }

    public void setListaCantidadesActa(List<Dashboard> listaCantidadesActa) {
        this.listaCantidadesActa = listaCantidadesActa;
    }

    public List<Dashboard> getListaCantidadesDocumento() {
        return listaCantidadesDocumento;
    }

    public void setListaCantidadesDocumento(List<Dashboard> listaCantidadesDocumento) {
        this.listaCantidadesDocumento = listaCantidadesDocumento;
    }

    public List<Dashboard> getListaCantidadesTupa() {
        return listaCantidadesTupa;
    }

    public void setListaCantidadesTupa(List<Dashboard> listaCantidadesTupa) {
        this.listaCantidadesTupa = listaCantidadesTupa;
    }

    public List<Dashboard> getListaCantidadesBandeja() {
        return listaCantidadesBandeja;
    }

    public void setListaCantidadesBandeja(List<Dashboard> listaCantidadesBandeja) {
        this.listaCantidadesBandeja = listaCantidadesBandeja;
    }

    public LineChartModel getDerivaciones() {
        return derivaciones;
    }

    public void setDerivaciones(LineChartModel derivaciones) {
        this.derivaciones = derivaciones;
    }

    public PieChartModel getPieModel() {
        return pieModel;
    }

    public void setPieModel(PieChartModel pieModel) {
        this.pieModel = pieModel;
    }

    public LineChartModel getReclamaciones() {
        return reclamaciones;
    }

    public void setReclamaciones(LineChartModel reclamaciones) {
        this.reclamaciones = reclamaciones;
    }

    public HorizontalBarChartModel getDerivacionesArea() {
        return derivacionesArea;
    }

    public void setDerivacionesArea(HorizontalBarChartModel derivacionesArea) {
        this.derivacionesArea = derivacionesArea;
    }

}
