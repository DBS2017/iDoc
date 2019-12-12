package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import modelo.Persona;
import modelo.Reclamacion;

public class ReclamacionImpl extends Conexion implements ICrud<Reclamacion> {

    @Override
    public void registrar(Reclamacion modelo) throws Exception {
        String sql = "INSERT INTO RECLAMACION (ASUREC, ESTREC, IDARE, IDPER)VALUES(?,?,?,?)";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, modelo.getASUREC());
            ps.setString(2, modelo.getESTREC());
            ps.setString(3, modelo.getIDARE());
            ps.setString(4, modelo.getIDPER());
            ps.executeUpdate();
            ps.clearParameters();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error" + e);
        } finally {
            this.desconectar();
        }
    }

    @Override
    public void editar(Reclamacion modelo) throws Exception {
        String sql = "UPDATE RECLAMACION SET ASUREC=?, ESTREC=?, IDARE=?, IDPER=?, WHERE IDREC=?";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, modelo.getASUREC());
            ps.setString(2, modelo.getESTREC());
            ps.setString(3, modelo.getIDARE());
            ps.setString(4, modelo.getIDPER());
            ps.setInt(5, modelo.getIDREC());
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
    public void eliminar(Reclamacion modelo) throws Exception {
        String sql = "DELETE ASUREC, ESTREC, IDARE, IDPER FROM RECLAMACION WHERE IDREC=?";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, modelo.getASUREC());
            ps.setString(2, modelo.getESTREC());
            ps.setString(3, modelo.getIDARE());
            ps.setString(4, modelo.getIDPER());
            ps.setInt(5, modelo.getIDREC());
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
    public List<Reclamacion> listar() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Reclamacion> listar(Reclamacion modelo) throws Exception {
        List<Reclamacion> listado = new ArrayList<>();
        Reclamacion reclamacion;
        String sql = "SELECT IDREC, ASUREC, ESTREC, NOMARE, APEPATPER || ' ' ||APEMATPER || ' ' || NOMPER AS IDPER, CORPER, CELPER, FECREC FROM RECLAMACION\n"
                + "    INNER JOIN PERSONA P on RECLAMACION.IDPER = P.IDPER\n"
                + "    INNER JOIN AREA A2 on RECLAMACION.IDARE = A2.IDARE";
        try {
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                reclamacion = new Reclamacion();
                Persona titular = new Persona();
                reclamacion.setIDREC(rs.getInt(1));
                reclamacion.setASUREC(rs.getString(2));
                reclamacion.setESTREC(rs.getString(3).equals("A") ? "Activo" : "Inactivo");
                reclamacion.setIDARE(rs.getString(4));
                reclamacion.setIDPER(rs.getString(5));
                titular.setCORPER(rs.getString(6));
                titular.setCELPER(rs.getString(7));
                reclamacion.setFECREC(rs.getTimestamp(8, Calendar.getInstance(TimeZone.getTimeZone("UTC"))));
                reclamacion.setPersona(titular);
                listado.add(reclamacion);
            }
            st.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listado;
    }

    @Override
    public Reclamacion obtenerModelo(Reclamacion modelo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
