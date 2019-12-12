package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import modelo.Expediente;

public class ExpedienteImpl extends Conexion implements ICrud<Expediente> {
    
    private Utils utils = new Utils();
    
    @Override
    public void registrar(Expediente modelo) throws Exception {
        try {
            modelo.setNUMEXP(utils.obtenerNumeroExpediente());
            String sql = "INSERT INTO EXPEDIENTE (NUMEXP, FECEXP_EN, ASUEXP, KEYEXP, TIPEXP, IDLOG) VALUES (?, CURRENT_TIMESTAMP, ?, ?, ?, ?)";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, modelo.getNUMEXP());
            ps.setString(2, modelo.getASUEXP());
            ps.setString(3, servicios.EncriptarS.encriptarExpediente(String.valueOf(modelo.getNUMEXP())));
            ps.setString(4, modelo.getTIPEXP());
            ps.setInt(5, servicios.SesionS.getSesion().getIDLOG());
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
    public void editar(Expediente modelo) throws Exception {
        
    }
    
    public void finalizarExpediente(Expediente modelo) throws Exception {
        try {
            String sql = "UPDATE EXPEDIENTE SET FECEXP_SAL= CURRENT_TIMESTAMP, ESTEXP='2' WHERE IDEXP = ?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, modelo.getIDEXP());
            ps.executeUpdate();
            ps.clearParameters();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.desconectar();
        }
    }
    
    public int currentIdExpediente() throws Exception{
        int id = 0;
        try {
            String sql = "select max(IDEXP) from EXPEDIENTE";
            ResultSet rs = this.conectar().prepareStatement(sql).executeQuery();
            while (rs.next()) {                
                id = rs.getInt(1);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            this.desconectar();
        }
        return id;
    }
    
    @Override
    public void eliminar(Expediente modelo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public List<Expediente> listar() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public List<Expediente> listar(Expediente modelo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Expediente obtenerModelo(Expediente modelo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
