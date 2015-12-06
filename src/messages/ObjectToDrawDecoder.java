package messages;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig; 

public class ObjectToDrawDecoder implements Decoder.Text<ObjectToDraw> {

	@Override 
	public ObjectToDraw decode(String jsonMessage) throws DecodeException {
	
		JsonObject jsonObject = Json.createReader(new StringReader(jsonMessage)).readObject(); 
	
		ObjectToDraw objectToDraw = new ObjectToDraw((jsonObject.getString("type")), 
                                  Integer.parseInt(jsonObject.getString("imageID")),
                                Integer.parseInt(jsonObject.getString("xPosition")),
                                Integer.parseInt(jsonObject.getString("yPosition")));
								 
		return objectToDraw; 
	}
	
	@Override
	public boolean willDecode(String jsonMessage) {
		return true; 
	}
	
	@Override 
	public void destroy() {
		
	}
	
	@Override
	public void init(EndpointConfig arg0) {
		
	}
}
