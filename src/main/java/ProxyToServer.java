import javax.websocket.*;
import java.io.IOException;

@ClientEndpoint(subprotocols = {"graphql-ws"})
public class ProxyToServer {
    private Session session;
    private Session socketSession;

    ProxyToServer(Session socketSession) {
        this.socketSession = socketSession;
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Opened websocket session - " + session.getId() +
                " to target server - " + session.getRequestURI());
        this.session = session;
        this.session.setMaxIdleTimeout(Integer.MAX_VALUE);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Message received from websocket session - " + session.getId() +
                " from target server:" + session.getRequestURI() + " - " + message);
        WSServlet.sendMessage(message, socketSession, session);
    }

    @OnClose
    public void onClose(CloseReason reason, Session session) {
        System.out.println("Closing the websocket session - " + session.getId() +
                " to target server - " + session.getRequestURI() + " due to " + reason.getCloseCode());
        WSServlet.closeSession(reason, socketSession);
    }

    public void sendMessage(String str) {
        try {
            session.getBasicRemote().sendText(str);
        } catch (IOException e) {
            System.out.println("Failed to send a message to target server - " + e);
            WSServlet.closeSession(WSServlet.toCloseReason(e), socketSession);
            WSServlet.closeSession(WSServlet.toCloseReason(e), session);
        }
    }
}
