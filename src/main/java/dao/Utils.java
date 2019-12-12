package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

/**
 * Servicios para el dao
 *
 * @version 0.1
 * @author Martín Samán
 */
public class Utils extends Conexion {

    /**
     * Genera la cadena de un documento
     *
     * @param idAreaEmisora Área que manda el documento
     * @param idAreaReceptora Área que recibe el documento
     * @param inicialesJefe (Opcional) Iniciales del jefe al que va dirigido el
     * documento
     * @param numeroDocumento Número de documento
     * @return cadena generada
     * 'MDNI-AÑO-#DOCUMENTO/CADENA_AREA_EMISORA-INICIALES_AREA_RECEPTORA-inicialesJefe'
     * @throws Exception
     */
    public String obtenerCadenaDocumento(int idAreaEmisora, int idAreaReceptora, String inicialesJefe, int numeroDocumento) throws Exception {

        String cadenaArea[] = new String[2];
        try {
            String sql = "SELECT CADARE FROM AREA WHERE IDARE = ?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, idAreaEmisora);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cadenaArea[0] = "MDNI-" + Calendar.getInstance().get(Calendar.YEAR) + "-" + numeroDocumento + "/" + rs.getString(1).trim() + "-";
            }
            ps.clearParameters();

            sql = "SELECT INARE FROM AREA WHERE IDARE = ?";
            ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, idAreaReceptora);
            rs = ps.executeQuery();

            while (rs.next()) {
                cadenaArea[1] = rs.getString(1).trim();
            }

            rs.close();
            ps.clearParameters();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.desconectar();
        }
        if (inicialesJefe != null) {
            return cadenaArea[0] + cadenaArea[1] + "-" + inicialesJefe;
        } else {
            return cadenaArea[0] + cadenaArea[1];
        }
    }

    /**
     * Genera la cadena de un áre al registrarla
     *
     * @param idAreaPadre (Opcional) Área papa
     * @param nombreArea Nombre del área a registrar
     * @param iniciales (Opcional) Iniciales del área
     * @return Cadena del área
     * @throws Exception
     */
    public String obtenerCadenaArea(int idAreaPadre, String nombreArea, String iniciales) throws Exception {
        String cadena = "";
        try {                           // si tiene papa, obtenemos su cadena
            if (idAreaPadre > 0) {
                String sql = "SELECT TRIM(SUBSTR(SYS_CONNECT_BY_PATH(TRIM(INARE),'/'),2)) "
                        + "FROM AREA "
                        + "WHERE IDARE = ? "
                        + "start with IDARE_PADR is null "
                        + "connect by prior IDARE = IDARE_PADR";
                PreparedStatement ps = this.conectar().prepareStatement(sql);
                ps.setInt(1, idAreaPadre);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    cadena = rs.getString(1).trim();
                }

                rs.close();
                ps.clearParameters();
                ps.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            this.desconectar();
        }

        cadena += cadena.equals("") ? "" : "/";

        if (iniciales != null || iniciales.equals("")) {
            cadena += iniciales;
        } else {
            cadena += obtenerIniciales(nombreArea);
        }

        return cadena;
    }

    /**
     * Obtiene las iniciales de un texto
     *
     * @param texto
     * @return iniciales
     */
    public String obtenerIniciales(String texto) {
        String iniciales = "";
        String[] textoSeparado = texto.split(" ");

        for (String separado : textoSeparado) {
            iniciales += separado.charAt(0);
        }
        return iniciales;
    }

    /**
     * Obtiene el siguiente número de expediente, se resetea si cambia el año
     *
     * @return Número de expediente
     * @throws Exception
     */
    public int obtenerNumeroExpediente() throws Exception {
        int numeroExpediente = 0;
        int year = 0;
        int yearActual = Calendar.getInstance().get(Calendar.YEAR);
        try {
            String sql = "SELECT NUMEXP, to_char(FECEXP_EN, 'yyyy') FROM EXPEDIENTE "
                    + "                     WHERE IDEXP in( "
                    + "                         select max(IDEXP) from EXPEDIENTE )";

            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                numeroExpediente = rs.getInt(1);
                year = Integer.valueOf(rs.getString(2));

            }
            rs.close();
            ps.clearParameters();
            ps.close();

            if (yearActual == year) {
                numeroExpediente += 1;
            } else {
                numeroExpediente = 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.desconectar();
        }

        return numeroExpediente;
    }

    /**
     * Obtiene el último número de expediente registrado
     *
     * @return Número de expediente
     * @throws Exception
     */
    public int currentExpediente() throws Exception {
        int currentExpediente = -1;
        try {
            String sql = "SELECT MAX(IDEXP) FROM EXPEDIENTE";

            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                currentExpediente = rs.getInt(1);
            }
            rs.close();
            ps.clearParameters();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.desconectar();
        }
        return currentExpediente;
    }

    /**
     * Obtiene el último número de documento registrado
     *
     * @return
     * @throws Exception
     */
    public int currentDocumento() throws Exception {
        int currentDocumento = -1;
        try {
            String sql = "SELECT MAX(IDDOC) FROM DOCUMENTO";

            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                currentDocumento = rs.getInt(1);
            }
            rs.close();
            ps.clearParameters();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.desconectar();
        }
        return currentDocumento;
    }
}
