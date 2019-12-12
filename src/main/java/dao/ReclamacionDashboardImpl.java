package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import modelo.Dashboard;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.hbar.HorizontalBarChartModel;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;

/**
 *
 * @version 0.1
 * @author Martín Samán
 */
public class ReclamacionDashboardImpl extends Conexion implements IDashboard<Dashboard> {

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

    @Override
    public LineChartModel generarLinear() throws Exception {
        LineChartModel linear = new LineChartModel();
        try {
            String sql = "SELECT transcurso, cantidad FROM\n"
                    + "(\n"
                    + "    SELECT to_char(r.FECREC, 'Month/yyyy') as transcurso, count(to_char(r.FECREC, 'Month/yyyy')) as cantidad\n"
                    + "    FROM RECLAMACION r\n"
                    + "    group by to_char(r.FECREC, 'Month/yyyy')\n"
                    + ")\n"
                    + "    ORDER BY TO_DATE(transcurso, 'Month/yyyy')";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            List<Number> cantidades = new ArrayList<>();
            List<String> labels = new ArrayList<>();

            while (rs.next()) {
                labels.add(rs.getString(1));
                cantidades.add(rs.getInt(2));
            }

            rs.close();
            ps.close();

            LineChartDataSet dataSet = new LineChartDataSet();
            dataSet.setData(cantidades);
            dataSet.setFill(false);
            dataSet.setLabel("cantidad");
            dataSet.setBorderColor("rgb(75, 192, 192)");

            ChartData data = new ChartData();
            data.addChartDataSet(dataSet);
            data.setLabels(labels);

            linear.setData(data);

            //Options
            LineChartOptions options = new LineChartOptions();
            Title title = new Title();
            title.setDisplay(true);
            title.setText("Reclamaciones");
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
