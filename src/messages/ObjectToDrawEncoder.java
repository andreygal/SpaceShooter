package messages;


public class ObjectToDrawEncoder implements Encoder.Text<ObjectToDraw>{

	@Override
	public String encode (ObjectToDraw object) throws EncodeException{
		JsonObject jsonObject = JsonObject.createObjectBuilder()
				.add("type", object.getType())
				.add("imageID", object.getImageID().toString())
				.add("xPosition", object.getObjectPosition().toString())
				.add("yPosition", object.getObjectPosition().toString()).build(); 
		}
	
	@Override
	public void init(EndpointConfig ec) { }
}
