import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@ServerEndpoint(value = "/", subprotocols = {"graphql-ws"})
public class ClientToProxy {

    private final String TARGET_URL = "ws://10.10.22.67:8081/subscribe/";
    private Session clientSession;

    private static WebSocketContainer webSocketContainer;

    private ProxyToServer proxyToServer;

    @OnOpen
    public void onOpen(Session session) {
        session.setMaxIdleTimeout(Integer.MAX_VALUE);

        proxyToServer = new ProxyToServer(session);

        try {
            clientSession = getWebsocketContainer().connectToServer(proxyToServer, new URI(TARGET_URL));
        } catch (DeploymentException | IOException | URISyntaxException e) {
            System.out.println("Exception connecting to server - " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Opened websocket session - " + session.getId() + " to target server - " + session.getRequestURI() +
                " and Created a new client session to target websocket.");

    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Message received from websocket session - " + session.getId() +
                " from target server:" + session.getRequestURI() + " - " + message);
        proxyToServer.sendMessage(message);
    }

    @OnClose
    public void onClose(CloseReason reason, Session session) {
        System.out.println("Closing the websocket session - " + session.getId() +
                " to target server - " + session.getRequestURI() + " due to " + reason.getCloseCode());
        WSServlet.closeSession(reason, clientSession);
    }

    private static WebSocketContainer getWebsocketContainer() {
        if (webSocketContainer == null) {
            webSocketContainer = ContainerProvider.getWebSocketContainer();
        }
        return webSocketContainer;
    }

}
