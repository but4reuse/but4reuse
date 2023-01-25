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

public class EMFReferenceElementSerializer implements JsonSerializer<EMFReferenceElement> {

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