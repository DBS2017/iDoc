package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import modelo.Dashboard;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.hbar.HorizontalBarChartDataSet;
import org.primefaces.model.charts.hbar.HorizontalBarChartModel;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.pie.PieChartDataSet;

/**
 *
 * @version 0.1
 * @author Martín Samán
 */
public class TransferenciaDashboardImpl extends Conexion implements IDashboard<Dashboard> {

    @Override
    public BarChartModel generarBar(Dashboard modelo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BarChartModel generarBar(List<Dashboard> modelo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Dashboard> listarCantidades() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Dashboard> listarCantidades(Dashboard modelo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Derivaciones por meses y años
     *
     * @return
     * @throws Exception
     */
    @Override
    public LineChartModel generarLinear() throws Exception {
        LineChartModel linear = new LineChartModel();
        try {
            String sql = "SELECT transcurso, tipo, cantidad FROM (\n"
                    + "    SELECT TO_CHAR(t.FECINGTRAN, 'Month/yyyy') as transcurso, exp.TIPEXP as tipo, count(TO_CHAR(t.FECINGTRAN, 'Month/yyyy')) as cantidad\n"
                    + "    FROM TRANSFERENCIA t\n"
                    + "    INNER JOIN EXPEDIENTE exp\n"
                    + "    ON t.IDEXP = exp.IDEXP\n"
                    + "    group by TO_CHAR(t.FECINGTRAN, 'Month/yyyy'), exp.TIPEXP\n"
                    + "    )\n"
                    + "ORDER BY TO_DATE(transcurso, 'Month/yyyy')";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            List<Number> cantidadesExterno = new ArrayList<>();
            List<Number> cantidadesInterno = new ArrayList<>();
            List<String> labels = new ArrayList<>();

            LineChartDataSet datasetExterno = new LineChartDataSet();
            datasetExterno.setLabel("Externo");
            datasetExterno.setBorderColor("rgb(75, 150, 192)");

            LineChartDataSet datasetInterno = new LineChartDataSet();
            datasetInterno.setLabel("Interno");
            datasetInterno.setBorderColor("rgb(50, 190, 152)");

            while (rs.next()) {
                String label = rs.getString(1);
                String tipo = rs.getString(2);
                int cantidad = rs.getInt(3);

                if ("E".equals(tipo)) {
                    cantidadesExterno.add(cantidad);
                    if (!labels.contains(label)) {
                        cantidadesInterno.add(0);
                    }
                } else {
                    cantidadesInterno.add(cantidad);
                    if (!labels.contains(label)) {
                        cantidadesExterno.add(0);
                    }
                }

                if (!labels.contains(label)) {
                    labels.add(label);
                }

            }

            rs.close();
            ps.close();
            datasetInterno.setData(cantidadesInterno);
            datasetExterno.setData(cantidadesExterno);

            ChartData data = new ChartData();
            data.addChartDataSet(datasetExterno);
            data.addChartDataSet(datasetInterno);
            data.setLabels(labels);
            linear.setData(data);

            //Options
            LineChartOptions options = new LineChartOptions();

            Title title = new Title();
            title.setDisplay(true);
            title.setText("Derivaciones");
            options.setTitle(title);

            linear.setOptions(options);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.desconectar();
        }
        return linear;
    }

    @Override
    public HorizontalBarChartModel generarBarHorizontal() throws Exception {

        HorizontalBarChartModel bar = new HorizontalBarChartModel();

        try {

            ChartData data = new ChartData();
            HorizontalBarChartDataSet dataset = new HorizontalBarChartDataSet();
            List<Number> valores = new ArrayList<>();
            List<String> etiquetas = new ArrayList<>();
            List<String> bgColors = new ArrayList<>();

            String sql = "SELECT area, cantidad FROM ("
                    + "SELECT area.NOMARE as area, count(area.NOMARE) as cantidad FROM TRANSFERENCIA t\n"
                    + "INNER JOIN AREA area\n"
                    + "ON t.IDARE_EMI = area.IDARE\n"
                    + "GROUP BY area.NOMARE"
                    + ") ORDER BY 2 DESC";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                etiquetas.add(rs.getString(1));
                valores.add(rs.getInt(2));
                Random rand = new Random();
                int r = rand.nextInt(255);
                int g = rand.nextInt(255);
                int b = rand.nextInt(255);
                bgColors.add("rgb(" + r + ", " + g + ", " + b + ")");
            }

            rs.close();
            ps.close();

            dataset.setData(valores);
            dataset.setBackgroundColor(bgColors);
            data.addChartDataSet(dataset);
            data.setLabels(etiquetas);
            bar.setData(data);

            BarChartOptions options = new BarChartOptions();
            Title title = new Title();
            title.setDisplay(true);
            title.setText("Derivaciones por Área");
            options.setTitle(title);

            bar.setOptions(options);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.desconectar();
        }

        return bar;
    }

}
