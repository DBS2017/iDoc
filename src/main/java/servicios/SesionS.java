package servicios;

import javax.faces.context.FacesContext;
import modelo.Login;

/**
 * @version 0.1
 * @author Martín Samán
 */
public class SesionS {

    /**
     * Obtiene el modelo del login con la cual el usuario ingresó al sistema
     *
     * @return modelo del login de la sesión iniciada
     */
    public static Login getSesion() {
        return (Login) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("login");
    }
}
