package dao;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import modelo.Area;
import modelo.Municipalidad;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.primefaces.model.StreamedContent;

public class AreaImpl extends Conexion implements ICrud<Area>, IReporte<Area> {

    Utils utils = new Utils();

    @Override
    public void registrar(Area modelo) throws Exception {
        try {
            String sql = "INSERT INTO AREA (NOMARE, IDMUN, IDARE_PADR, ESTARE, INARE,CADARE) VALUES (?,?,?,?,?,?)";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, modelo.getNOMARE());
            ps.setInt(2, 1);
            if (modelo.getAreaPadre().getIDARE() == 0) {
                ps.setNull(3, java.sql.Types.NULL);
            } else {
                ps.setInt(3, modelo.getAreaPadre().getIDARE());
            }
            ps.setString(4, "A");
            if (modelo.getINARE() != null) {
                ps.setString(5, modelo.getINARE());
            } else {
                ps.setString(5, utils.obtenerIniciales(modelo.getNOMARE()));
            }
            ps.setString(6, utils.obtenerCadenaArea(modelo.getAreaPadre().getIDARE(), modelo.getNOMARE(), modelo.getINARE()));
            ps.executeUpdate();
            ps.clearParameters();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.desconectar();
        }
    }

    @Override
    public void editar(Area modelo) throws Exception {
        try {
            String sql = "UPDATE AREA SET NOMARE=?, IDMUN=?, IDARE_PADR=?, ESTARE=?, INARE=? WHERE IDARE=?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, modelo.getNOMARE());
            ps.setInt(2, 6);
            if (modelo.getAreaPadre().getIDARE() == 0) {
                ps.setNull(3, java.sql.Types.NULL);
            } else {
                ps.setInt(3, modelo.getAreaPadre().getIDARE());
            }
            ps.setString(4, String.valueOf(modelo.getESTARE().charAt(0)));
            ps.setString(5, modelo.getINARE());
            ps.setInt(6, modelo.getIDARE());
            ps.executeUpdate();
            ps.clearParameters();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.desconectar();
        }
        actualizarCadena(modelo);
    }

    public void actualizarCadena(Area modelo) throws Exception {
        try {
            String sql = "UPDATE AREA SET CADARE= getCadenaArea(?) WHERE IDARE=?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, modelo.getIDARE());
            ps.setInt(2, modelo.getIDARE());
            ps.executeUpdate();
            ps.clearParameters();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.desconectar();
        }
    }

    @Override
    public void eliminar(Area modelo) throws Exception {
        try {
            String sql = "UPDATE AREA SET ESTARE=? WHERE IDARE=?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, "I");
            ps.setInt(2, modelo.getIDARE());
            ps.executeUpdate();
            ps.clearParameters();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.desconectar();
        }
    }

    @Override
    public List<Area> listar() throws Exception {
        List<Area> lista = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT IDARE, NOMARE, ESTARE, INARE, CADARE, IDARE_PADR,\n"
                    + "       SYS_CONNECT_BY_PATH(NOMARE, '/')\n"
                    + "FROM AREA\n"
                    + "start with IDARE_PADR is null\n"
                    + "connect by prior IDARE = IDARE_PADR";
            ps = this.conectar().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Area area = new Area();
                Municipalidad municipalidad = new Municipalidad();

                area.setIDARE(rs.getInt(1));
                area.setNOMARE(rs.getString(2));
                area.setESTARE(rs.getString(3));
                area.setINARE(rs.getString(4));
                area.setCADARE(rs.getString(5));
                area.areaPadre.setIDARE(rs.getInt(6));
                area.areaPadre.setNOMARE(rs.getString(7));

                area.setMunicipalidad(municipalidad);
                lista.add(area);
            }
            ps.closeOnCompletion();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ps.isClosed()) {
                ps.clearParameters();
                rs.close();
                this.desconectar();
            }
        }
        return lista;
    }


    public void generarReporteIndividual(Map parameters) throws Exception {
        conectar();
        File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("Reportes/Area/Area.jasper"));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parameters, this.conectar());
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
       response.addHeader("Content-disposition", "attachment; filename=Area.pdf");
        try (ServletOutputStream stream = response.getOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
            stream.flush();
        }
        FacesContext.getCurrentInstance().responseComplete();
    }

    @Override
    public void generarReporteGeneral(Area modelo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public StreamedContent generarReporteIndividualPrev(Area modelo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public StreamedContent generarReporteGeneralPrev(Area modelo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Area obtenerModelo(Area modelo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Area> listar(Area modelo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void generarReporteIndividual(Area modelo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
