package dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import modelo.Area;
import modelo.Documento;
import modelo.DocumentoTipo;
import modelo.Empresa;
import modelo.Expediente;
import modelo.Login;
import modelo.Persona;
import modelo.Trabajador;
import modelo.Transferencia;
import modelo.Tupa;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

public class TransferenciaImpl extends Conexion implements ICrud<Transferencia>, IReporte<Transferencia> {

    Utils utils = new Utils();

    @Override
    public void registrar(Transferencia trans) throws Exception {
        try {
            String sql = "INSERT INTO TRANSFERENCIA (FECINGTRAN,OBSTRAN,IDEXP,IDARE_EMI,IDARE_REC,ESTTRA,FECSALTRAN,IDARE_DER) "
                    + "VALUES(CURRENT_TIMESTAMP,?,?,?,?,?,null,5)";
            PreparedStatement ps = this.conectar().prepareStatement(sql);

            ps.setString(1, trans.getOBSTRAN());
            ps.setInt(2, trans.getExpediente().getIDEXP());
            ps.setInt(3, trans.getAreaEmisora().getIDARE());
            ps.setInt(4, trans.getAreaReceptora().getIDARE());
            ps.setString(5, "0"); // 0	Pendiente
//            ps.setString(6, utils.obtenerCadenaTransferencia(trans.getAreaEmisora().getIDARE(), trans.getAreaReceptora().getIDARE(), trans.getInicialesJefe(), trans.getExpediente().getNUMEXP()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.desconectar();
        }
    }

    //Seleccionar transferencias y actualizar fecha de salida
    public void marcarSalida(List<Transferencia> listaTransferencia) throws Exception {
        try {
            String sql = "UPDATE TRANSFERENCIA SET FECSALTRAN = CURRENT_TIMESTAMP WHERE IDTRAN = ?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);

            for (Transferencia transferencia : listaTransferencia) {
                if (transferencia.getFECSALTRAN() == null) {
                    ps.setInt(1, transferencia.getIDTRAN());
                    ps.executeUpdate();
                }
            }

            ps.clearParameters();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.desconectar();
        }
    }

    public void actualizarDerivacion(Transferencia modelo) throws Exception {
        try {
            String sql = "UPDATE TRANSFERENCIA SET IDARE_DER=? WHERE IDTRAN = ?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, modelo.getAreaReceptora().getIDARE());
            ps.setInt(2, modelo.getIDTRAN());
            ps.executeUpdate();
            ps.clearParameters();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.desconectar();
        }
    }

    public void recibir(Transferencia modelo) throws Exception {
        try {
//            1	Recepcionado
//            2	Rechazado

            String sql = "UPDATE TRANSFERENCIA SET FECRECTRAN=?, ESTTRA=?, OBSDER=? WHERE IDTRAN = ?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setTimestamp(1, new java.sql.Timestamp(new Date().getTime()));
            ps.setString(2, modelo.getESTTRA());
            ps.setString(3, modelo.getOBSDER());
            ps.setInt(4, modelo.getIDTRAN());
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
    public void editar(Transferencia trans) throws Exception {
        try {
            String sql = "UPDATE TRANSFERENCIA SET  OBSDER=?, ESTTRA=? WHERE IDTRAN = ?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, trans.getOBSDER());
            ps.setString(2, trans.getESTTRA());
            ps.setInt(3, trans.getIDTRAN());
            ps.executeUpdate();
            ps.clearParameters();
            ps.close();
            /*            
            3	Atendido
            5	Cancelado
            6	Derivado
            7	Devuelto
            8	finanizar
             */
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.desconectar();
        }
    }

    @Override
    public void eliminar(Transferencia trans) throws Exception {
        try {
            String sql = "UPDATE TRANSFERENCIA SET ESTTRA='I' WHERE IDTRAN = ?";
            PreparedStatement ps = this.conectar().prepareCall(sql);
            ps.setInt(1, trans.getIDTRAN());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.desconectar();
        }
    }

    @Override
    public List<Transferencia> listar() throws Exception {
        List<Transferencia> listaTransferencia = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT "
                    + "       T.IDTRAN, "
                    + "       T.FECRECTRAN, "
                    + "       T.FECSALTRAN, "
                    + "       T.OBSTRAN, "
                    + "       T.ESTTRA, "
                    + "       D.ASUDOC AS IDDOC, "
                    + "       A2.NOMARE AS IDARE_EMI, "
                    + "       A.NOMARE AS IDARE_REC, "
                    + "       T2.PLATUP as plazo,"
                    //+ "diasLaborables(T.FECSALTRAN, T.FECRECTRAN) as habiles, 
                    + " T.CADTRAN as denominacion"
                    + "FROM TRANSFERENCIA T "
                    + "INNER JOIN  DOCUMENTO D on T.IDDOC = D.IDDOC "
                    + "INNER JOIN TUPA T2 on D.IDTUP = T2.IDTUP "
                    + "INNER JOIN AREA A ON T.IDARE_REC = A.IDARE "
                    + "INNER JOIN AREA A2 on T.IDARE_EMI = A2.IDARE "
                    + "WHERE ESTTRA != 'I' ORDER BY IDTRAN DESC";
            ps = this.conectar().prepareStatement(sql);
            rs = ps.executeQuery();
            Transferencia trans;
            while (rs.next()) {
                trans = new Transferencia();
                Area areaEmisora = new Area();
                Area areaReceptora = new Area();
                Documento documento = new Documento();
                Tupa tupa = new Tupa();
                trans.setIDTRAN(rs.getInt("IDTRAN"));
                trans.setFECRECTRAN(rs.getTimestamp("FECRECTRAN", Calendar.getInstance(TimeZone.getTimeZone("UTC"))));
                trans.setFECSALTRAN(rs.getTimestamp("FECSALTRAN", Calendar.getInstance(TimeZone.getTimeZone("UTC"))));
                trans.setOBSTRAN(rs.getString("OBSTRAN"));
                trans.setESTTRA(rs.getString("ESTTRA"));
                documento.setASUDOC(rs.getString("IDDOC"));
                areaEmisora.setNOMARE(rs.getString("IDARE_EMI"));
                areaReceptora.setNOMARE(rs.getString("IDARE_REC"));
                tupa.setPLATUP(rs.getInt("plazo"));
                //trans.setDiasHabiles(rs.getInt("habiles"));
                //trans.setFueraDePlazo(trans.getDiasHabiles() > tupa.getPLATUP());

                trans.setAreaEmisora(areaEmisora);
                trans.setAreaReceptora(areaReceptora);
                documento.setTupa(tupa);
//                trans.setDocumento(documento);

                listaTransferencia.add(trans);
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
        return listaTransferencia;

    }

    public List<Transferencia> listarBandejaEntrada() throws Exception {
        List<Transferencia> listaBandeja = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT \n"
                    + "tran.IDTRAN, tran.FECINGTRAN, tran.FECSALTRAN, tran.FECRECTRAN, tran.OBSTRAN, tran.ESTTRA, tran.OBSDER, \n"
                    + "exp.IDEXP, exp.NUMEXP, exp.ASUEXP, exp.ESTEXP, exp.KEYEXP, exp.TIPEXP, exp.FECEXP_EN, exp.FECEXP_SAL,\n"
                    + "emisor.IDARE, emisor.NOMARE, \n"
                    + "receptor.IDARE, receptor.NOMARE, \n"
                    + "derivado.IDARE, derivado.NOMARE \n"
                    + "FROM TRANSFERENCIA tran\n"
                    + "INNER JOIN AREA emisor\n"
                    + "ON tran.IDARE_EMI = emisor.IDARE\n"
                    + "INNER JOIN AREA receptor\n"
                    + "ON tran.IDARE_REC = receptor.IDARE\n"
                    + "INNER JOIN AREA derivado\n"
                    + "ON tran.IDARE_DER = derivado.IDARE\n"
                    + "INNER JOIN EXPEDIENTE exp\n"
                    + "ON tran.IDEXP = exp.IDEXP\n"
                    + "WHERE receptor.IDARE = ? AND derivado.IDARE = 5 ORDER BY tran.IDTRAN DESC";
            ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, servicios.SesionS.getSesion().getTrabajador().getArea().getIDARE());
//            ps.setInt(1, 2);
            rs = ps.executeQuery();

            while (rs.next()) {
                Transferencia tran = new Transferencia();
                Expediente exp = new Expediente();
                Area emisor = new Area();
                Area receptor = new Area();
                Area derivado = new Area();

                tran.setIDTRAN(rs.getInt(1));
                tran.setFECINGTRAN(rs.getTimestamp(2, Calendar.getInstance(TimeZone.getTimeZone("UTC"))));
                tran.setFECSALTRAN(rs.getTimestamp(3, Calendar.getInstance(TimeZone.getTimeZone("UTC"))));
                tran.setFECRECTRAN(rs.getTimestamp(4, Calendar.getInstance(TimeZone.getTimeZone("UTC"))));
                tran.setOBSTRAN(rs.getString(5));
                tran.setESTTRA(rs.getString(6));
                tran.setEstadoTransferencia(tran.getESTTRA());
                tran.setOBSDER(rs.getString(7));

                exp.setIDEXP(rs.getInt(8));
                exp.setNUMEXP(rs.getInt(9));
                exp.setASUEXP(rs.getString(10));
                exp.setESTEXP(rs.getString(11));
                exp.setKEYEXP(rs.getString(12));
                exp.setTIPEXP(rs.getString(13));
                exp.setFECEXP_EN(rs.getTimestamp(14, Calendar.getInstance(TimeZone.getTimeZone("UTC"))));
                exp.setFECEXP_SAL(rs.getTimestamp(15, Calendar.getInstance(TimeZone.getTimeZone("UTC"))));

                emisor.setIDARE(rs.getInt(16));
                emisor.setNOMARE(rs.getString(17));

                receptor.setIDARE(rs.getInt(18));
                receptor.setNOMARE(rs.getString(19));

                derivado.setIDARE(rs.getInt(20));
                derivado.setNOMARE(rs.getString(21));

                tran.setAreaDerivada(derivado);
                tran.setAreaEmisora(emisor);
                tran.setAreaReceptora(receptor);
                tran.setExpediente(exp);

                listaBandeja.add(tran);
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
        return listaBandeja;
    }

    public List<Transferencia> listarBandejaSalida() throws Exception {
        List<Transferencia> listaBandeja = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT \n"
                    + "tran.IDTRAN, tran.FECINGTRAN, tran.FECSALTRAN, tran.FECRECTRAN, tran.OBSTRAN, tran.ESTTRA, tran.OBSDER, \n"
                    + "exp.IDEXP, exp.NUMEXP, exp.ASUEXP, exp.ESTEXP, exp.KEYEXP, exp.TIPEXP, exp.FECEXP_EN, exp.FECEXP_SAL,\n"
                    + "emisor.IDARE, emisor.NOMARE, \n"
                    + "receptor.IDARE, receptor.NOMARE, \n"
                    + "derivado.IDARE, derivado.NOMARE \n"
                    + "FROM TRANSFERENCIA tran\n"
                    + "INNER JOIN AREA emisor\n"
                    + "ON tran.IDARE_EMI = emisor.IDARE\n"
                    + "INNER JOIN AREA receptor\n"
                    + "ON tran.IDARE_REC = receptor.IDARE\n"
                    + "INNER JOIN AREA derivado\n"
                    + "ON tran.IDARE_DER = derivado.IDARE\n"
                    + "INNER JOIN EXPEDIENTE exp\n"
                    + "ON tran.IDEXP = exp.IDEXP\n"
                    + "WHERE emisor.IDARE = ? ORDER BY tran.IDTRAN DESC";
            ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, servicios.SesionS.getSesion().getTrabajador().getArea().getIDARE());
//            ps.setInt(1, 2);
            rs = ps.executeQuery();

            while (rs.next()) {
                Transferencia tran = new Transferencia();
                Expediente exp = new Expediente();
                Area emisor = new Area();
                Area receptor = new Area();
                Area derivado = new Area();

                tran.setIDTRAN(rs.getInt(1));
                tran.setFECINGTRAN(rs.getTimestamp(2, Calendar.getInstance(TimeZone.getTimeZone("UTC"))));
                tran.setFECSALTRAN(rs.getTimestamp(3, Calendar.getInstance(TimeZone.getTimeZone("UTC"))));
                tran.setFECRECTRAN(rs.getTimestamp(4, Calendar.getInstance(TimeZone.getTimeZone("UTC"))));
                tran.setOBSTRAN(rs.getString(5));
                tran.setESTTRA(rs.getString(6));
                tran.setEstadoTransferencia(tran.getESTTRA());
                tran.setOBSDER(rs.getString(7));

                exp.setIDEXP(rs.getInt(8));
                exp.setNUMEXP(rs.getInt(9));
                exp.setASUEXP(rs.getString(10));
                exp.setESTEXP(rs.getString(11));
                exp.setKEYEXP(rs.getString(12));
                exp.setTIPEXP(rs.getString(13));
                exp.setFECEXP_EN(rs.getTimestamp(14, Calendar.getInstance(TimeZone.getTimeZone("UTC"))));
                exp.setFECEXP_SAL(rs.getTimestamp(15, Calendar.getInstance(TimeZone.getTimeZone("UTC"))));

                emisor.setIDARE(rs.getInt(16));
                emisor.setNOMARE(rs.getString(17));

                receptor.setIDARE(rs.getInt(18));
                receptor.setNOMARE(rs.getString(19));

                derivado.setIDARE(rs.getInt(20));
                derivado.setNOMARE(rs.getString(21));

                tran.setAreaDerivada(derivado);
                tran.setAreaEmisora(emisor);
                tran.setAreaReceptora(receptor);
                tran.setExpediente(exp);

                listaBandeja.add(tran);
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
        return listaBandeja;
    }

    @Override
    public void generarReporteIndividual(Transferencia modelo) throws Exception {
//        File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("Reportes/Transferencia/Transferencia.jasper"));
//        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parameters, this.conectar());
//        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//        response.addHeader("Content-disposition", "attachment; filename=Transferencia.pdf");
//        try (ServletOutputStream stream = response.getOutputStream()) {
//            JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
//            stream.flush();
//        }
//        FacesContext.getCurrentInstance().responseComplete();
    }

    public void generarReporteIndividual(Map parameters) throws JRException, IOException, Exception {
        conectar();
        File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("Reportes/Transferencia/Trazabilidad.jasper"));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parameters, this.conectar());
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.addHeader("Content-disposition", "attachment; filename=Trazabilidad.pdf");
        try (ServletOutputStream stream = response.getOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
            stream.flush();
        }
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void generarReporteIndividual2(Map parameters) throws Exception {
        File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("Reportes/Bandeja/bandeja_R.jasper"));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parameters, this.conectar());
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.addHeader("Content-disposition", "attachment; filename=Bandeja.pdf");
        try (ServletOutputStream stream = response.getOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
            stream.flush();
        }
        FacesContext.getCurrentInstance().responseComplete();
    }

    @Override
    public Transferencia obtenerModelo(Transferencia modelo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void generarReporteGeneral(Transferencia modelo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public StreamedContent generarReporteIndividualPrev(Transferencia modelo) throws Exception {
        InputStream inputStream = null;

        Map parameters = new HashMap();

        parameters.put("IDTRAN", modelo.getIDTRAN());

        try {

            ByteArrayOutputStream salida = new ByteArrayOutputStream();
            File jasperReport = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("Reportes/Transferencia/Transferencia".
                    concat(modelo.getESTTRA().equals("1") ? "A" : "R"
                    )
                    + ".jasper"));

            JasperPrint jPrint = JasperFillManager.fillReport(jasperReport.getPath(), parameters, this.conectar());

            JRExporter exporter = new net.sf.jasperreports.engine.export.JRPdfExporter();

            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, salida);

            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jPrint);

            exporter.setParameter(JRPdfExporterParameter.PDF_JAVASCRIPT, "this.print();");

            exporter.exportReport();

            inputStream = new ByteArrayInputStream(salida.toByteArray());

        } catch (JRException e) {
            e.printStackTrace();
        } finally {
            this.desconectar();
        }

        return new DefaultStreamedContent(inputStream, "application/pdf", "Transferencia_" + modelo.getESTTRA());
    }

    @Override
    public StreamedContent generarReporteGeneralPrev(Transferencia modelo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Transferencia> listar(Transferencia modelo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Documento> listarDocumentoExpediente(Transferencia modelo) throws Exception {
        List<Documento> documentos = new ArrayList<>();
        try {
            String sql = "SELECT IDDOC,\n"
                    + "                           NUMFOLDOC,\n"
                    + "                           FECDOC,\n"
                    + "                           ASUDOC,\n"
                    + "                           OBSDOC,\n"
                    + "                           ESTDOC,\n"
                    + "                           IDEXP,\n"
                    + "                           P.NOMPER AS IDPER,\n"
                    + "                           PE.NOMPER AS IDLOG,\n"
                    + "                          E.RAZSOCEMP AS IDEMP,\n"
                    + "                           TU.NOMTUP AS IDTUP,\n"
                    + "                           TP.NOMTIPDOC AS IDTIPDOC, CADDOC, NUMDOC, area.NOMARE, "
                    + "                           D.TRANSDOC as transcurrido,"
                    + "                           D.OUTPLAZ as fueraPlazo,"
                    + "                           TU.PLATUP as plazoTupa"
                    + "                    FROM DOCUMENTO D\n"
                    + "                    INNER JOIN PERSONA P ON D.IDPER = P.IDPER\n"
                    + "                    INNER JOIN LOGIN L ON D.IDLOG = L.IDLOG\n"
                    + "                    INNER JOIN TRABAJADOR T ON L.IDTRAB = T.IDTRAB\n"
                    + "                    INNER JOIN AREA area ON T.IDARE = area.IDARE\n"
                    + "                    INNER JOIN PERSONA PE ON T.IDPER = PE.IDPER\n"
                    + "                    INNER JOIN EMPRESA E ON D.IDEMP = E.IDEMP\n"
                    + "                    INNER JOIN TUPA TU ON D.IDTUP = TU.IDTUP\n"
                    + "                    INNER JOIN TIPO_DOCUMENTO TP ON D.IDTIPDOC = TP.IDTIPDOC\n"
                    + "                    WHERE  D.ESTDOC != 'I' AND IDEXP = ? ORDER BY D.IDDOC DESC ";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, modelo.getExpediente().getIDEXP());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Documento documento = new Documento();
                Expediente expediente = new Expediente();
                Tupa tupa = new Tupa();
                Login login = new Login();
                Trabajador trabajador = new Trabajador();
                Area area = new Area();
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
                documentotipo.setNOMTIPDOC(rs.getString("IDTIPDOC"));
                documento.setCADDOC(rs.getString("CADDOC"));
                documento.setNUMDOC(rs.getInt("NUMDOC"));
                area.setNOMARE(rs.getString("NOMARE"));
                documento.setTRANSDOC(rs.getInt("transcurrido"));
                documento.setOUTPLAZ(rs.getString("fueraPlazo"));
                tupa.setPLATUP(rs.getInt("plazoTupa"));

                documento.setEmpresa(empresa);
                documento.setTupa(tupa);
                documento.setPersona(persona);
                trabajador.setPersona(persona2);
                trabajador.setArea(area);
                login.setTrabajador(trabajador);
                documento.setLogin(login);
                documento.setDocumentotipo(documentotipo);
                documento.setExpediente(expediente);

                documentos.add(documento);
            }
            rs.clearWarnings();
            rs.close();
            ps.clearParameters();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.desconectar();
        }
        return documentos;
    }

}
