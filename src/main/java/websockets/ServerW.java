package websockets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * Servidor websockets para actualizar la lista de los clientes conectados al
 * sistema
 *
 * @version 0.1
 * @author Martín Samán
 */
@ApplicationScoped
@ServerEndpoint("/ws/server")
public class ServerW {

    List<Session> sesiones = Collections.synchronizedList(new ArrayList<Session>());

    /**
     * Agrega la sesion de un usuario a la lista si es que el usuario no esta en
     * ella
     *
     * @param session la sesión que asigna el servidor al cliente
     */
    @OnOpen
    public void open(Session session) {
        try {
            if (sesiones.contains(session) == false) {
                System.out.println("Sesion abierta");
                sesiones.add(session);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Manda un mensaje que recibe a todos los clientes conectados
     *
     * @param session la sesion del cliente que manda al server
     * @param tipo el mensaje que manda: en este caso el tipo de vista a
     * actualizar
     */
    @OnMessage
    public void handleMessage(Session session, String tipo) {
        try {
            broadcast(session, tipo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Manda un mensaje a todos los clientes conectados
     *
     * @param sesion sesion a la que voy a mandar el mensaje
     * @param tipo tipo de vista que se actualizará
     * @throws IOException
     */
    public void broadcast(Session sesion, String tipo) throws IOException {
        try {
            System.out.println("Haciendo broadcast!");
            for (Session s : sesion.getOpenSessions()) {
                s.getAsyncRemote().sendText(tipo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina la sesión de un usuario de la lista
     *
     * @param session sesión a eliminar
     */
    @OnClose
    public void close(Session session) {
        try {
            sesiones.remove(session);
            System.out.println("Session cerrada");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Imprime en la consola del servidor el error que se presenta
     *
     * @param e error
     */
    @OnError
    public void onError(Throwable e) {
        e.printStackTrace();
    }
}
