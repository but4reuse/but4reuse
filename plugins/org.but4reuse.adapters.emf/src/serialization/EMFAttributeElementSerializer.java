package serialization;

import java.lang.reflect.Type;

import org.but4reuse.adapters.emf.EMFAttributeElement;
import org.eclipse.emf.diffmerge.util.ModelImplUtil;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * This is a serializer for the gson export of EMFAttributeElements to be registered as TypeAdapter on the GsonBuilder
 */
public class EMFAttributeElementSerializer implements JsonSerializer<EMFAttributeElement> {

	/**
	 * Gson invokes this call-back method during serialization when it encounters a field of the specified type.
	 * In the implementation of this call-back method, you should consider invoking
	 * JsonSerializationContext.serialize(Object, Type) method to create JsonElements for any non-trivial field
	 * of the src object. However, you should never invoke it on the src object itself since that will cause
	 * an infinite loop (Gson will call your call-back method again).
	 *
	 * @param attributeElement to be serialized for the gsonExport; The object that needs to be converted to Json
	 * @param type the actual type (fully genericized version) of the source object
	 * @param context isn't used here
	 * @return a JsonElement corresponding to the specified object
	 */
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