package messages;

import javax.json.Json;

public class ObjectToDrawDecoder implements Decoder.Text<ObjectToDraw> {

	@Override 
	public ObjectToDraw decode(String jsonMessage){
	
		JsonObject jsonObject = Json.createReader(StringReader.jsonMessage()).readObject(); 
	}
}
