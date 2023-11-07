
import javax.servlet.http.HttpServlet;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;

public class WSServlet extends HttpServlet {

    public static final String WS_ENDPOINT = "/ws/subscribe/";

    @Override
    public void init() {
        try {
            ServerContainer container = (ServerContainer) getServletContext().getAttribute(ServerContainer.class.getName());
            container.addEndpoint(
                    ServerEndpointConfig.Builder.create(ClientToProxy.class, WS_ENDPOINT).build());
        } catch (DeploymentException e) {
            System.out.println("Exception when initializing websocket server - " + e.getMessage());
            e.printStackTrace();
        }

    }


    public static void sendMessage(String message, Session socketSession, Session clientSession) {
        try {
            socketSession.getBasicRemote().sendText(message);
        } catch (IOException e) {
            System.out.println("failed to send a message - " + e.getMessage());
            closeSession(toCloseReason(e), clientSession);
            closeSession(toCloseReason(e), socketSession);
        }
    }

    public static void closeSession(CloseReason closeReason, Session socketSession) {
        try {
            if (socketSession != null && socketSession.isOpen()) {
                socketSession.close(closeReason);
            }
        } catch (IOException e) {
            System.out.println("Failed to close websocket session - " + e.getMessage());
        }
    }

    public static CloseReason toCloseReason(Throwable e) {
        String msg = "Websocket error: " + e.getClass().getSimpleName() + " " + e.getMessage();
        if (e.getCause() != null) {
            msg += " Caused by: " + e.getCause().getClass().getSimpleName() + " " + e.getCause().getMessage();
        }
        return new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, msg);
    }


}
