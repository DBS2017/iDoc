package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import modelo.Requerimiento;

public class RequerimientoImpl extends Conexion implements ICrud<Requerimiento> {

    @Override
    public void registrar(Requerimiento modelo) throws Exception {
        String sql = "INSERT INTO REQUERIMIENTO(NOMREQ, ESTREQ, IDTUP) VALUES(?,?,?)";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, modelo.getNOMREQ().toUpperCase());
            ps.setString(2, "A");
            ps.setInt(3, modelo.getTupa().getIDTUP());
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
    public void editar(Requerimiento modelo) throws Exception {
        String sql = "UPDATE REQUERIMIENTO SET NOMREQ=? WHERE IDREQ=?";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, modelo.getNOMREQ().toUpperCase());
            ps.setInt(2, modelo.getIDREQ());
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
    public void eliminar(Requerimiento modelo) throws Exception {
        String sql = "UPDATE REQUERIMIENTO SET ESTREQ='I' WHERE IDREQ=?";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, modelo.getIDREQ());
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
    public List<Requerimiento> listar() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Requerimiento> listar(Requerimiento modelo) throws Exception {
        List<Requerimiento> listado = new ArrayList<>();
        System.out.println("TUPA-" + modelo.getTupa().getIDTUP());

        try {
            String sql = "SELECT R.IDREQ, R.NOMREQ, R.ESTREQ, R.IDTUP, T.NOMTUP FROM REQUERIMIENTO R"
                    + " INNER JOIN TUPA T on R.IDTUP = T.IDTUP "
                    + " WHERE ESTREQ != 'I' AND T.IDTUP=?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, modelo.getTupa().getIDTUP());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Requerimiento requerimiento = new Requerimiento();

                requerimiento.setIDREQ(rs.getInt(1));
                requerimiento.setNOMREQ(rs.getString(2));
                requerimiento.setESTREQ(rs.getString(3).equals("A") ? "Activo" : "Inactivo");
                requerimiento.getTupa().setIDTUP(rs.getInt(4));
                requerimiento.getTupa().setNOMTUP(rs.getString(5));
                listado.add(requerimiento);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.desconectar();
        }
        return listado;
    }

    @Override
    public Requerimiento obtenerModelo(Requerimiento modelo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
