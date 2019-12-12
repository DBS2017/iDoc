package controlador;

import dao.LoginImpl;
import dao.TrabajadorImpl;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import modelo.Login;
import modelo.Trabajador;
import org.primefaces.PrimeFaces;

@Named(value = "trabajadorC")
@SessionScoped
public class TrabajadorC implements Serializable {

    Trabajador trabajador, trabajadorSeleccionado;
    List<Trabajador> listaTrabajador, listaTrabajadorFiltrado;
    TrabajadorImpl daoTrabajador;
    Login login;

    public TrabajadorC() throws Exception {
        trabajador = new Trabajador();
        trabajadorSeleccionado = new Trabajador();
        listaTrabajador = new ArrayList<>();
        daoTrabajador = new TrabajadorImpl();
        login = new Login();
    }

    @PostConstruct
    public void init() {
        try {
            listar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listar() throws Exception {
        try {
            listaTrabajador = daoTrabajador.listar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registrar() throws Exception {
        try {
            if (listaTrabajador.contains(trabajador) == false) {
//                if (servicios.SesionS.getSesion().getTIPLOG().equals("S") == false) {
//                    trabajador.setArea(servicios.SesionS.getSesion().getTrabajador().getArea());
//                }
                daoTrabajador.registrar(trabajador);

                login.setTrabajador(trabajador);
                login.setTIPLOG(login.getTIPLOG() + servicios.SesionS.getSesion().getTIPLOG().charAt(1));
                LoginImpl daoLogin = new LoginImpl();
                daoLogin.registrar(login);
//                trabajador.clear();
//                listar();
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage("Registro exitoso")
                );
                PrimeFaces.current().executeScript("enviar('Trabajador');");
            } else {
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage("El trabajador al que intentó registrar ya lo esá")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cambiarArea() throws Exception {
        try {
            daoTrabajador.cambiarArea(trabajadorSeleccionado);
            PrimeFaces.current().executeScript("enviar('Trabajador');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetearContra() throws Exception {
        try {
            daoTrabajador.resetearContra(trabajadorSeleccionado);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editar() throws Exception {
        try {
            daoTrabajador.editar(trabajadorSeleccionado);
//            listar();
            PrimeFaces.current().executeScript("enviar('Trabajador');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar() throws Exception {
        try {
            daoTrabajador.eliminar(trabajadorSeleccionado);
//            listar();
            PrimeFaces.current().executeScript("enviar('Trabajador');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generarReporte(int IDTRAB) throws Exception {
        TrabajadorImpl report = new TrabajadorImpl();
        try {
            Map<String, Object> parameters = new HashMap(); // Libro de parametros
            parameters.put("IDTRAB", IDTRAB); //Insertamos un parametro
            report.generarReporteIndividual(parameters); //Pido exportar Reporte con los parametros
//            report.exportarPDF2(parameters);
        } catch (Exception e) {
            throw e;
        }
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
    }

    public Trabajador getTrabajadorSeleccionado() {
        return trabajadorSeleccionado;
    }

    public void setTrabajadorSeleccionado(Trabajador trabajadorSeleccionado) {
        this.trabajadorSeleccionado = trabajadorSeleccionado;
    }

    public List<Trabajador> getListaTrabajador() {
        return listaTrabajador;
    }

    public void setListaTrabajador(List<Trabajador> listaTrabajador) {
        this.listaTrabajador = listaTrabajador;
    }

    public List<Trabajador> getListaTrabajadorFiltrado() {
        return listaTrabajadorFiltrado;
    }

    public void setListaTrabajadorFiltrado(List<Trabajador> listaTrabajadorFiltrado) {
        this.listaTrabajadorFiltrado = listaTrabajadorFiltrado;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

}
