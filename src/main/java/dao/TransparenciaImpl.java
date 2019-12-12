package dao;

import modelo.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class TransparenciaImpl extends Conexion {

    public List<Transparencia> listarTransferencia(Transparencia t) throws Exception {
        List<Transparencia> lista = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT \n"
                    + "       T.IDTRAN,\n"
                    + "       T.FECINGTRAN,\n"
                    + "       T.FECRECTRAN,\n"
                    + "       T.OBSTRAN,\n"
                    + "       T.OBSDER,\n"
                    + "       A1.NOMARE AS IDARE_EMI,\n"
                    + "       A2.NOMARE AS IDARE_REC,\n"
                    + "       T.ESTTRA,\n"
                    + "       T.IDEXP,\n"
                    + "       E.NUMEXP AS NUMEXP,\n"
                    + "       E.FECEXP_EN,\n"
                    + "       E.FECEXP_SAL,\n"
                    + "       E.ASUEXP,\n"
                    + "       E.KEYEXP,\n"
                    + "       E.TIPEXP,\n"
                    + "       PER.NOMPER AS IDLOGG,\n"
                    + "       E.ESTEXP\n"
                    + "FROM TRANSFERENCIA T\n"
                    + "INNER JOIN AREA A1 on T.IDARE_EMI = A1.IDARE\n"
                    + "INNER JOIN AREA A2 on T.IDARE_REC = A2.IDARE\n"
                    + "INNER JOIN EXPEDIENTE E on T.IDEXP = E.IDEXP\n"
                    + "INNER JOIN LOGIN L on E.IDLOG = L.IDLOG\n"
                    + "INNER JOIN TRABAJADOR T2 on L.IDTRAB = T2.IDTRAB\n"
                    + "INNER JOIN PERSONA PER ON T2.IDPER = PER.IDPER\n"
                    + "WHERE E.NUMEXP = ? AND E.KEYEXP = ? ";
            ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, t.getNUMEXP());
            ps.setString(2, t.getKEYEXP());
            rs = ps.executeQuery();

//            lista = new ArrayList<>();
            Transparencia transparencia;
            while (rs.next()) {

                transparencia = new Transparencia();
                Area emisor = new Area();
                Area receptor = new Area();
                Expediente expediente = new Expediente();
                Persona persona = new Persona();

                transparencia.setIDTRAN(rs.getInt(1));
                transparencia.setFECINGTRAN(rs.getDate(2));
                transparencia.setFECRECTRAN(rs.getDate(3));
                transparencia.setOBSTRAN(rs.getString(4));
                transparencia.setOBSDER(rs.getString(5));
                emisor.setNOMARE(rs.getString(6));
                receptor.setNOMARE(rs.getString(7));
                transparencia.setESTTRA(rs.getString(8));
                expediente.setIDEXP(rs.getInt(9));
                expediente.setNUMEXP(rs.getInt(10));
                expediente.setFECEXP_EN(rs.getDate(11));
                expediente.setFECEXP_SAL(rs.getDate(12));
                expediente.setASUEXP(rs.getString(13));
                expediente.setKEYEXP(rs.getString(14));
                expediente.setTIPEXP(rs.getString(15));
                persona.setNOMPER(rs.getString(16));
                expediente.setESTEXP(rs.getString(17));

                transparencia.setExpediente(expediente);
                transparencia.setEmisor(emisor);
                transparencia.setReceptor(receptor);
                lista.add(transparencia);
            }
//            rs.close();
//            ps.clearParameters();
//            ps.close();
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

    public List<Documento> listarDocumentoTransferencia(Documento d) throws Exception {
        List<Documento> listaDocumentoTransparencia = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT IDDOC,\n"
                    + "       D.NUMFOLDOC,\n"
                    + "       D.FECDOC,\n"
                    + "       D.ASUDOC,\n"
                    + "       D.OBSDOC,\n"
                    + "       D.ESTDOC,\n"
                    + "       D.IDEXP,\n"
                    + "       P.NOMPER AS IDPER,\n"
                    + "       PE.NOMPER AS IDLOG,\n"
                    + "      E.RAZSOCEMP AS IDEMP,\n"
                    + "       TU.NOMTUP AS IDTUP,\n"
                    + "       TP.NOMTIPDOC AS IDTIPDOC,\n"
                    + "        E2.NUMEXP\n"
                    + "FROM DOCUMENTO D\n"
                    + "INNER JOIN PERSONA P ON D.IDPER = P.IDPER\n"
                    + "INNER JOIN LOGIN L ON D.IDLOG = L.IDLOG\n"
                    + "INNER JOIN TRABAJADOR T ON L.IDTRAB = T.IDTRAB\n"
                    + "INNER JOIN PERSONA PE ON T.IDPER = PE.IDPER\n"
                    + "INNER JOIN EMPRESA E ON D.IDEMP = E.IDEMP\n"
                    + "INNER JOIN TUPA TU ON D.IDTUP = TU.IDTUP\n"
                    + "INNER JOIN TIPO_DOCUMENTO TP ON D.IDTIPDOC = TP.IDTIPDOC\n"
                    + "INNER JOIN EXPEDIENTE E2 on D.IDEXP = E2.IDEXP\n"
                    + "WHERE  D.ESTDOC != 'I' AND E2.NUMEXP = ? ORDER BY D.IDDOC DESC";
            ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, d.getNUMEXP());
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
                documentotipo.setNOMTIPDOC(rs.getString("IDTIPDOC"));

                documento.setEmpresa(empresa);
                documento.setTupa(tupa);
                documento.setPersona(persona);
                trabajador.setPersona(persona2);
                login.setTrabajador(trabajador);
                documento.setLogin(login);
                documento.setDocumentotipo(documentotipo);
                documento.setExpediente(expediente);

                listaDocumentoTransparencia.add(documento);
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
        return listaDocumentoTransparencia;
    }

    public List<Tupa> listarTupa() throws Exception {
        TupaImpl daoTupa = new TupaImpl();
        List<Tupa> lista;
        lista = daoTupa.listar();
        return lista;
    }

    public List<Requerimiento> listarRequerimientoTupa() throws Exception {
        List<Requerimiento> listaRequerimientoTupa = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT T.IDTUP, T.NUMTUP,\n"
                    + "       T.NOMTUP, T.PRETUP,\n"
                    + "       T.PLATUP, T.IDARE,\n"
                    + "       area.NOMARE, R.NOMREQ\n"
                    + "FROM REQUERIMIENTO R\n"
                    + "    INNER JOIN TUPA  T ON R.IDTUP = T.IDTUP\n"
                    + "INNER JOIN AREA ON T.IDARE = area.IDARE\n"
                    + "where  ESTTUP != 'I'\n"
                    + "order by T.NOMTUP desc";
            ps = this.conectar().prepareStatement(sql);
            rs = ps.executeQuery();
            Requerimiento requerimiento;
            while (rs.next()) {
                requerimiento = new Requerimiento();
                Area area = new Area();
                Tupa tupa = new Tupa();
                
                tupa.setIDTUP(rs.getInt(1));
                tupa.setNUMTUP(rs.getString(2));
                tupa.setNOMTUP(rs.getString(3));
                tupa.setPRETUP(rs.getDouble(4));
                tupa.setPLATUP(rs.getInt(5));
                area.setIDARE(rs.getInt(6));
                area.setNOMARE(rs.getString(7));
//                req.setIDREQ(rs.getInt(8));
                requerimiento.setNOMREQ(rs.getString(8));

                

                tupa.setArea(area);
                requerimiento.setTupa(tupa);
                
                listaRequerimientoTupa.add(requerimiento);
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
        return listaRequerimientoTupa;
    }

    public List<Tupa> buscarTupa(String campo, List<Tupa> listaTupa) throws Exception {
        List<Tupa> lista = new ArrayList<>();
        try {
            for (Tupa tupa : listaTupa) {
                if (tupa.getNOMTUP().toUpperCase().contains(campo) || tupa.getArea().getNOMARE().toUpperCase().contains(campo)) {
                    lista.add(tupa);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
//  public List<Requerimiento> buscaZrTupa(String campo, List<Requerimiento> listaTupaTemp) throws Exception {
//        List<Requerimiento> lista = new ArrayList<>();
//        try {
//            for (Requerimiento requerimiento : listaTupaTemp) {
//                if (requerimiento.getTupa().getNOMTUP().toUpperCase().contains(campo) || requerimiento.getTupa().getArea().getNOMARE().toUpperCase().contains(campo)) {
//                    lista.add(requerimiento);
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return lista;
//    }

}
