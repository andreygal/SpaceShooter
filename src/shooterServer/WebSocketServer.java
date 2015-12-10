package shooterServer;
/*
 * A generic Glassfish server. Needs to be launched prior to running the client application. 
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.websocket.DeploymentException;

import org.glassfish.tyrus.server.*;

public class WebSocketServer {
	
	public static void main(String[] args){
		runServer(); 
	}
	
	public static void runServer() {
		Server server = new Server("localhost", 8025, "/websockets", null, ShooterServerEndpoint.class);
		
		try {
			server.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Press any key to stop the server");
			reader.readLine(); 
		} catch (DeploymentException e) {
			System.out.println("Deployment Exception");
			e.printStackTrace();
		} catch (Exception e){
			System.out.println("WebSocket went wrong");;
		} finally {
			server.stop(); 
		}
	}
}
