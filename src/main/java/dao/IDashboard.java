package dao;

import java.util.List;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.charts.hbar.HorizontalBarChartModel;
import org.primefaces.model.charts.line.LineChartModel;

/**
 * Creación de dashboards
 *
 * @author Martín Samán
 * @param <T> modelo
 */
public interface IDashboard<T> {

    /**
     * Genera un Bar a partir de un modelo
     *
     * @param modelo POJO
     * @return BarChartModel
     * @throws Exception
     */
    public BarChartModel generarBar(T modelo) throws Exception;

    /**
     * Genera un Bar a partir de varios modelos
     *
     * @param modelo POJO
     * @return BarChartModel
     * @throws Exception
     */
    public BarChartModel generarBar(List<T> modelo) throws Exception;

    /**
     * Lista todas las cantidades
     *
     * @return
     * @throws Exception
     */
    public List<T> listarCantidades() throws Exception;

    /**
     * Lista las cantidades dependiendo del modelo
     *
     * @param modelo POJO
     * @return
     * @throws Exception
     */
    public List<T> listarCantidades(T modelo) throws Exception;
    
    /**
     * Crea un linear model
     * @return LineChartModel
     * @throws Exception 
     */
    public LineChartModel generarLinear() throws Exception;
    
    /**
     * Crea un pie
     * @return HorizontalBarChartModel
     * @throws Exception 
     */
    public HorizontalBarChartModel generarBarHorizontal() throws Exception;
}
