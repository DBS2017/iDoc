
package controlador;

import dao.PersonaImpl;
import dao.ReclamacionImpl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import modelo.Reclamacion;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import modelo.Persona;

@Named(value = "reclamacionC")
@SessionScoped
public class ReclamacionC implements Serializable {
    Reclamacion reclamacion, seleccionado;
    List<Reclamacion> listaReclamacion, listaReclamacionFiltrado;
    ReclamacionImpl daoReclamacion;
    
    Persona persona;
    PersonaImpl daoPersona;
    List<Persona> listaPersona;
    
    public ReclamacionC(){
        reclamacion = new Reclamacion();
        listaReclamacion = new ArrayList<>();
        daoReclamacion = new ReclamacionImpl();
        persona = new Persona();
        daoPersona = new PersonaImpl();
        listaPersona = new ArrayList<>();
    }
    
    @PostConstruct
    public void init(){
        try {
            listar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void registrar() {
        try {
            daoReclamacion.registrar(reclamacion);
            listar();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Agregado Correctamente", null));
        } catch (Exception e) {
            System.out.println("Error" + e);
        }
    }

    public void editar() {
        try {
            daoReclamacion.editar(reclamacion);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Editado Correctamente", null));
        } catch (Exception e) {
            System.out.println("Error" + e);
        }
    }

    public void eliminar() {
        try {
            daoReclamacion.eliminar(reclamacion);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminado Correctamente", null));
        } catch (Exception e) {
            System.out.println("Error" + e);
        }
    }

    public void listar() {
        try {
            listaReclamacion = daoReclamacion.listar(reclamacion);
            List<Reclamacion> listaTemp = new ArrayList<>();
            for (Reclamacion nextReclamacion : listaReclamacion) {
                switch (nextReclamacion.getESTREC()) {
                    case "A":
                        nextReclamacion.setESTREC("Activo");
                        break;
                    case "I":
                        nextReclamacion.setESTREC("Inactivo");
                        break;
                }

                listaTemp.add(nextReclamacion);
            }
            listaReclamacion = listaTemp;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Reclamacion getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(Reclamacion seleccionado) {
        this.seleccionado = seleccionado;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public List<Persona> getListaPersona() {
        return listaPersona;
    }

    public void setListaPersona(List<Persona> listaPersona) {
        this.listaPersona = listaPersona;
    }
    
    

    public Reclamacion getReclamacion() {
        return reclamacion;
    }

    public void setReclamacion(Reclamacion reclamacion) {
        this.reclamacion = reclamacion;
    }

    public List<Reclamacion> getListaReclamacion() {
        return listaReclamacion;
    }

    public void setListaReclamacion(List<Reclamacion> listaReclamacion) {
        this.listaReclamacion = listaReclamacion;
    }

    public List<Reclamacion> getListaReclamacionFiltrado() {
        return listaReclamacionFiltrado;
    }

    public void setListaReclamacionFiltrado(List<Reclamacion> listaReclamacionFiltrado) {
        this.listaReclamacionFiltrado = listaReclamacionFiltrado;
    }
    
    
}
