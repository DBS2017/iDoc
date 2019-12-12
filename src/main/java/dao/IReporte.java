package dao;

import org.primefaces.model.StreamedContent;

/**
 * Generación o presentación de reportes
 *
 * @author Martín Samán
 * @param <T> modelo - POJO
 */
public interface IReporte<T> {

    /**
     * Genera un reporte
     *
     * @param modelo POJO
     * @throws Exception
     */
    public void generarReporteIndividual(T modelo) throws Exception;

    /**
     * Genera un reporte de todos los datos
     *
     * @param modelo POJO
     * @throws Exception
     */
    public void generarReporteGeneral(T modelo) throws Exception;

    /**
     * Abre un popup con el reporte generado
     *
     * @param modelo POJO
     * @return StreamedContent
     * @throws Exception
     */
    public StreamedContent generarReporteIndividualPrev(T modelo) throws Exception;

    /**
     * Abre un popup con el reporte de todos los datos generado
     *
     * @param modelo POJO
     * @return StreamedContent
     * @throws Exception
     */
    public StreamedContent generarReporteGeneralPrev(T modelo) throws Exception;

}
