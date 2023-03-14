package serialization;

import java.lang.reflect.Type;

import org.but4reuse.adapters.emf.EMFReferenceElement;
import org.eclipse.emf.diffmerge.util.ModelImplUtil;
import org.eclipse.emf.ecore.EObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * This is a serializer for the gson export of EMFReferenceElements to be registered as TypeAdapter on the GsonBuilder
 */
public class EMFReferenceElementSerializer implements JsonSerializer<EMFReferenceElement> {

	/**
	 * Gson invokes this call-back method during serialization when it encounters a field of the specified type.
	 * In the implementation of this call-back method, you should consider invoking
	 * JsonSerializationContext.serialize(Object, Type) method to create JsonElements for any non-trivial field
	 * of the src object. However, you should never invoke it on the src object itself since that will cause
	 * an infinite loop (Gson will call your call-back method again).
	 *
	 * @param referenceElement to be serialized for the gsonExport; The object that needs to be converted to Json
	 * @param type the actual type (fully genericized version) of the source object
	 * @param context isn't used here
	 * @return a JsonElement corresponding to the specified object
	 */
	@Override
	public JsonElement serialize(EMFReferenceElement referenceElement, Type type, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		
		
		final JsonObject referenceJsonObject = new JsonObject();
		
		referenceJsonObject.addProperty("owner", ModelImplUtil.getXMLID(referenceElement.owner));
		referenceJsonObject.addProperty("name", referenceElement.eReference.getName());
		
		final JsonArray referenced = new JsonArray();
		
		for(final EObject reference : referenceElement.referenced) {
			final String r = ModelImplUtil.getXMLID(reference);
			final JsonPrimitive primitive = new JsonPrimitive(r == null ? "" : r);
			referenced.add(primitive);
		}
		
		referenceJsonObject.add("referenced", referenced);
		
		jsonObject.add("reference", referenceJsonObject);
		
		return jsonObject;
	}
}