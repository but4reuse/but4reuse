package serialization;

import java.lang.reflect.Type;

import org.but4reuse.adapters.emf.EMFAttributeElement;
import org.eclipse.emf.diffmerge.util.ModelImplUtil;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class EMFAttributeElementSerializer implements JsonSerializer<EMFAttributeElement> {

	@Override
	public JsonElement serialize(EMFAttributeElement attributeElement, Type type, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		
		final String ownerString = ModelImplUtil.getXMLID(attributeElement.owner);
		final String nameString = attributeElement.eAttribute.getName();
		final String valueString = attributeElement.value.toString();
		
		final JsonObject attributeJsonObject = new JsonObject();
		
		attributeJsonObject.addProperty("owner", ownerString == null ? "" : ownerString);
		attributeJsonObject.addProperty("name", nameString == null ? "" : nameString);
		attributeJsonObject.addProperty("value", valueString == null ? "" : valueString);
		
		jsonObject.add("attribute", attributeJsonObject);
		
		return jsonObject;
	}
}