package serialization;

import java.lang.reflect.Type;

import org.but4reuse.adapters.emf.EMFClassElement;
import org.eclipse.emf.diffmerge.util.ModelImplUtil;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class EMFClassElementSerializer implements JsonSerializer<EMFClassElement> {

	@Override
	public JsonElement serialize(EMFClassElement classElement, Type type, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		
		final String ownerString = ModelImplUtil.getXMLID(classElement.owner);
		final String eObjectString = ModelImplUtil.getXMLID(classElement.eObject);
		
		final JsonObject classJsonObject = new JsonObject();
		
		classJsonObject.addProperty("owner", ownerString == null ? "" : ownerString);
		classJsonObject.addProperty("eObject", eObjectString == null ? "" : eObjectString);
		
		jsonObject.add("class", classJsonObject);
		
		return jsonObject;
	}
}