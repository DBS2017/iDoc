/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import modelo.Dashboard;
import modelo.Transferencia;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.charts.hbar.HorizontalBarChartModel;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.pie.PieChartModel;

/**
 *
 * @author josefernandolevanoarbizu
 */
public class BandejaDashboardImpl extends Conexion implements IDashboard<Dashboard> {

    @Override
    public BarChartModel generarBar(Dashboard modelo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Dashboard> listarCantidades() throws Exception {
        List<Dashboard> lista = new ArrayList<>();
        try {
            String sql = "select\n"
                    + "       count(*) as Transferenciaban\n"
                    + "from transferencia\n"
                    + "where idare_rec =?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, servicios.SesionS.getSesion().getTrabajador().getArea().getIDARE());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Dashboard dashboard = new Dashboard();
                Transferencia transferencia = new Transferencia();
                dashboard.setCantidadban(rs.getInt(1));
                dashboard.setTransferencia(transferencia);
                lista.add(dashboard);
            }
            rs.clearWarnings();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.desconectar();
        }
        return lista;
    }

    @Override
    public BarChartModel generarBar(List<Dashboard> modelo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Dashboard> listarCantidades(Dashboard modelo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LineChartModel generarLinear() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HorizontalBarChartModel generarBarHorizontal() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
