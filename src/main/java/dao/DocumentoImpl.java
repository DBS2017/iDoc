package dao;

import static dao.Conexion.conectar;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import modelo.Documento;
import modelo.Empresa;
import modelo.Login;
import modelo.Persona;
import modelo.Trabajador;
import modelo.Tupa;
import modelo.DocumentoTipo;
import modelo.Expediente;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.StreamedContent;

public class DocumentoImpl extends Conexion implements ICrud<Documento>, IReporte<Documento> {
    
    @Override
    public void registrar(Documento documento) throws Exception {
        try {
            String sql = "INSERT INTO DOCUMENTO \n"
                    + "(NUMFOLDOC, FECDOC, ASUDOC, OBSDOC, IDEXP, IDPER, IDLOG, IDEMP, IDTUP, IDTIPDOC, CADDOC, NUMDOC, ESTDOC, INIJEF) \n"
                    + "VALUES ( ?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            
            ps.setInt(1, documento.getNUMFOLDOC());
            ps.setString(2, documento.getASUDOC());
            ps.setString(3, documento.getOBSDOC());
            ps.setInt(4, documento.getExpediente().getIDEXP());
            ps.setInt(5, documento.getPersona().getIDPER());
            ps.setInt(6, servicios.SesionS.getSesion().getIDLOG());
            ps.setInt(7, documento.getEmpresa().getIDEMP());
            ps.setInt(8, documento.getTupa().getIDTUP());
            ps.setInt(9, documento.getDocumentotipo().getIDTIPDOC());
            ps.setString(10, documento.getCADDOC());
            ps.setInt(11, documento.getNUMDOC());
            ps.setString(12, "A");
            ps.setString(13, documento.getINIJEF());
            ps.executeUpdate();
            ps.clearParameters();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            
        } finally {
            this.desconectar();
        }
        
    }

//     IDDOC, NUMFOLDOC, FECDOC, ASUDOC, OBSDOC, ESTDOC, IDEXP, IDPER, IDLOG, IDEMP, IDTUP, IDTIPDOC 
    @Override
    public void editar(Documento documento) throws Exception {
        try {
            String sql = "UPDATE DOCUMENTO SET NUMFOLDOC =?, ASUDOC=?, OBSDOC=?, ESTDOC=?, IDEXP=?, IDPER=?, IDEMP=?, IDTUP=?, IDTIPDOC=? WHERE IDDOC LIKE ?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, documento.getNUMFOLDOC());
            ps.setString(2, documento.getASUDOC());
            ps.setString(3, documento.getOBSDOC());
            ps.setString(4, documento.getESTDOC());
            ps.setInt(5, documento.getExpediente().getIDEXP());
            ps.setInt(6, documento.getPersona().getIDPER());
            ps.setInt(7, documento.getEmpresa().getIDEMP());
            ps.setInt(8, documento.getTupa().getIDTUP());
            ps.setInt(9, documento.getDocumentotipo().getIDTIPDOC());
            ps.setInt(10, documento.getIDDOC());
            ps.executeUpdate();
            ps.clearParameters();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            
        } finally {
            this.desconectar();
        }
    }
    
    @Override
    public void eliminar(Documento documento) throws Exception {
        
        try {
            String sql = "UPDATE DOCUMENTO SET ESTDOC='I' WHERE IDDOC LIKE ?";
            PreparedStatement ps = this.conectar().prepareCall(sql);
            ps.setInt(1, documento.getIDDOC());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.desconectar();
        }
    }
    
    public ScheduleModel obtenerCalendario() throws Exception {
        
        ScheduleModel calendario = new DefaultScheduleModel();
        DefaultScheduleEvent evento;
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = " SELECT D.IDDOC,\n"
                    + "                           D.NUMFOLDOC,\n"
                    + "                           D.FECDOC,\n"
                    + "                           D.ASUDOC,\n"
                    + "                           D.OBSDOC,\n"
                    + "                           D.ESTDOC,\n"
                    + "                           D.IDEXP, D.NUMDOC,\n"
                    + "                           P.NOMPER AS IDPER,\n"
                    + "                           PE.NOMPER AS IDLOG,\n"
                    + "                          E.RAZSOCEMP AS IDEMP,\n"
                    + "                           TU.NOMTUP AS IDTUP,\n"
                    + "                           TP.NOMTIPDOC AS IDTIPDOC, "
                    + "                           D.TRANSDOC as transcurrido,"
                    + "                           D.OUTPLAZ as fueraPlazo,"
                    + "                           TU.PLATUP as plazoTupa,"
                    + "                           D.FECDOC +  TU.PLATUP as entrega \n"
                    + "                    FROM DOCUMENTO D\n"
                    + "                    INNER JOIN PERSONA P ON D.IDPER = P.IDPER\n"
                    + "                    INNER JOIN LOGIN L ON D.IDLOG = L.IDLOG\n"
                    + "                    INNER JOIN TRABAJADOR T ON L.IDTRAB = T.IDTRAB\n"
                    + "                    INNER JOIN PERSONA PE ON T.IDPER = PE.IDPER\n"
                    + "                    INNER JOIN EMPRESA E ON D.IDEMP = E.IDEMP\n"
                    + "                    INNER JOIN TUPA TU ON D.IDTUP = TU.IDTUP\n"
                    + "                    INNER JOIN TIPO_DOCUMENTO TP ON D.IDTIPDOC = TP.IDTIPDOC\n"
                    + "                    WHERE  D.ESTDOC != 'I' AND D.IDLOG = ?"
                    + "                    ORDER BY D.IDDOC DESC ";
            ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, servicios.SesionS.getSesion().getIDLOG());
            rs = ps.executeQuery();
            Documento documento;
            while (rs.next()) {
                documento = new Documento();
                Expediente expediente = new Expediente();
                Tupa tupa = new Tupa();
                Login login = new Login();
                Trabajador trabajador = new Trabajador();
                Empresa empresa = new Empresa();
                Persona persona = new Persona();
                Persona persona2 = new Persona();
                DocumentoTipo documentotipo = new DocumentoTipo();
                
                documento.setIDDOC(rs.getInt("IDDOC"));
                documento.setNUMFOLDOC(rs.getInt("NUMFOLDOC"));
                documento.setFECDOC(rs.getTimestamp("FECDOC", Calendar.getInstance(TimeZone.getTimeZone("UTC"))));
                documento.setASUDOC(rs.getString("ASUDOC"));
                documento.setOBSDOC(rs.getString("OBSDOC"));
                documento.setESTDOC(rs.getString("ESTDOC"));
                expediente.setIDEXP(rs.getInt("IDEXP"));
                persona.setNOMPER(rs.getString("IDPER"));
                persona2.setNOMPER(rs.getString("IDLOG"));
                empresa.setRAZSOCEMP(rs.getString("IDEMP"));
                documento.setNUMDOC(rs.getInt("NUMDOC"));
                tupa.setNOMTUP(rs.getString("IDTUP"));
                tupa.setPLATUP(rs.getInt("plazoTupa"));
                documentotipo.setNOMTIPDOC(rs.getString("IDTIPDOC"));
                documento.setTRANSDOC(rs.getInt("transcurrido"));
                documento.setOUTPLAZ(rs.getString("fueraPlazo"));
                documento.setEntrega(rs.getTimestamp("entrega", Calendar.getInstance(TimeZone.getTimeZone("UTC"))));
                
                documento.setEmpresa(empresa);
                documento.setTupa(tupa);
                documento.setPersona(persona);
                trabajador.setPersona(persona2);
                login.setTrabajador(trabajador);
                documento.setLogin(login);
                documento.setDocumentotipo(documentotipo);
                documento.setExpediente(expediente);
                
                evento = new DefaultScheduleEvent(documento.getASUDOC(), documento.getFECDOC(), documento.getEntrega(), documento);
                evento.setDescription(String.valueOf(documento.getNUMDOC()));
                calendario.addEvent(evento);
                
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
        
        return calendario;
    }
    
    @Override
    public List<Documento> listar() throws Exception {
        List<Documento> listaDocumento = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = " SELECT D.IDDOC,\n"
                    + "                           D.NUMFOLDOC,\n"
                    + "                           D.FECDOC,\n"
                    + "                           D.ASUDOC,\n"
                    + "                           D.OBSDOC,\n"
                    + "                           D.ESTDOC,\n"
                    + "                           D.IDEXP,\n"
                    + "                           P.NOMPER AS IDPER,\n"
                    + "                           PE.NOMPER AS IDLOG,\n"
                    + "                          E.RAZSOCEMP AS IDEMP,\n"
                    + "                           TU.NOMTUP AS IDTUP,\n"
                    + "                           TP.NOMTIPDOC AS IDTIPDOC, "
                    + "                           D.TRANSDOC as transcurrido,"
                    + "                           D.OUTPLAZ as fueraPlazo,"
                    + "                           TU.PLATUP as plazoTupa,"
                    + "                           D.FECDOC +  TU.PLATUP as entrega \n"
                    + "                    FROM DOCUMENTO D\n"
                    + "                    INNER JOIN PERSONA P ON D.IDPER = P.IDPER\n"
                    + "                    INNER JOIN LOGIN L ON D.IDLOG = L.IDLOG\n"
                    + "                    INNER JOIN TRABAJADOR T ON L.IDTRAB = T.IDTRAB\n"
                    + "                    INNER JOIN PERSONA PE ON T.IDPER = PE.IDPER\n"
                    + "                    INNER JOIN EMPRESA E ON D.IDEMP = E.IDEMP\n"
                    + "                    INNER JOIN TUPA TU ON D.IDTUP = TU.IDTUP\n"
                    + "                    INNER JOIN TIPO_DOCUMENTO TP ON D.IDTIPDOC = TP.IDTIPDOC\n"
                    + "                    WHERE  D.ESTDOC != 'I' ORDER BY D.IDDOC DESC ";
            ps = this.conectar().prepareStatement(sql);
            rs = ps.executeQuery();
            Documento documento;
            while (rs.next()) {
                documento = new Documento();
                Expediente expediente = new Expediente();
                Tupa tupa = new Tupa();
                Login login = new Login();
                Trabajador trabajador = new Trabajador();
                Empresa empresa = new Empresa();
                Persona persona = new Persona();
                Persona persona2 = new Persona();
                DocumentoTipo documentotipo = new DocumentoTipo();
                
                documento.setIDDOC(rs.getInt("IDDOC"));
                documento.setNUMFOLDOC(rs.getInt("NUMFOLDOC"));
                documento.setFECDOC(rs.getTimestamp("FECDOC", Calendar.getInstance(TimeZone.getTimeZone("UTC"))));
                documento.setASUDOC(rs.getString("ASUDOC"));
                documento.setOBSDOC(rs.getString("OBSDOC"));
                documento.setESTDOC(rs.getString("ESTDOC"));
                expediente.setIDEXP(rs.getInt("IDEXP"));
                persona.setNOMPER(rs.getString("IDPER"));
                persona2.setNOMPER(rs.getString("IDLOG"));
                empresa.setRAZSOCEMP(rs.getString("IDEMP"));
                tupa.setNOMTUP(rs.getString("IDTUP"));
                tupa.setPLATUP(rs.getInt("plazoTupa"));
                documentotipo.setNOMTIPDOC(rs.getString("IDTIPDOC"));
                documento.setTRANSDOC(rs.getInt("transcurrido"));
                documento.setOUTPLAZ(rs.getString("fueraPlazo"));
                documento.setEntrega(rs.getTimestamp("entrega", Calendar.getInstance(TimeZone.getTimeZone("UTC"))));
                
                documento.setEmpresa(empresa);
                documento.setTupa(tupa);
                documento.setPersona(persona);
                trabajador.setPersona(persona2);
                login.setTrabajador(trabajador);
                documento.setLogin(login);
                documento.setDocumentotipo(documentotipo);
                documento.setExpediente(expediente);
                
                listaDocumento.add(documento);
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
        return listaDocumento;
    }
    
    public void generarReporteIndividual(Map parameters) throws Exception {
        conectar();
        File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("Reportes/Documento/Documento.jasper"));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parameters, this.conectar());
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.addHeader("Content-disposition", "attachment; filename=Documento.pdf");
        try (ServletOutputStream stream = response.getOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
            stream.flush();
        }
        FacesContext.getCurrentInstance().responseComplete();
    }
    
    @Override
    public void generarReporteGeneral(Documento modelo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public StreamedContent generarReporteIndividualPrev(Documento modelo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public StreamedContent generarReporteGeneralPrev(Documento modelo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Documento obtenerModelo(Documento modelo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public List<Documento> listar(Documento modelo) throws Exception {
        List<Documento> listaDocumento = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM DOCUMENTO";
            ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, servicios.SesionS.getSesion().getTrabajador().getArea().getIDARE());
            rs = ps.executeQuery();
            Documento documento;
            while (rs.next()) {
                documento = new Documento();
                Tupa tupa = new Tupa();
                Login login = new Login();
                Trabajador trabajador = new Trabajador();
                Empresa empresa = new Empresa();
                Persona persona = new Persona();
                Persona persona2 = new Persona();
                DocumentoTipo documentotipo = new DocumentoTipo();
                
                documento.setEmpresa(empresa);
                documento.setTupa(tupa);
                documento.setPersona(persona);
                trabajador.setPersona(persona2);
                login.setTrabajador(trabajador);
                documento.setLogin(login);
                documento.setDocumentotipo(documentotipo);
                
                listaDocumento.add(documento);
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
        return listaDocumento;
    }
    
    @Override
    public void generarReporteIndividual(Documento modelo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
