package messages;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class ObjectToDrawEncoder implements Encoder.Text<ObjectToDraw>{

	@Override
	public String encode (ObjectToDraw object) throws EncodeException{
		JsonObject jsonObjectToDraw = Json.createObjectBuilder()
				.add("type", 	  object.getType())
				.add("imageID",   object.getImageID())
				.add("xPosition", object.getObjectPosition().x) 
				.add("yPosition", object.getObjectPosition().y).build(); 
		
				return jsonObjectToDraw.toString(); 
		}
	
	@Override
	public void init(EndpointConfig ec) { }
	
	@Override 
	public void destroy() { }
}
