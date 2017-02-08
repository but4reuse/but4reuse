package org.but4reuse.adapters.scratch;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.json.JsonAdapter;
import org.but4reuse.adapters.json.tools.Paths;
import org.but4reuse.adapters.scratch.utils.ScratchUtils;
import org.but4reuse.utils.files.FileUtils;
import org.but4reuse.utils.files.ZipUtils;
import org.eclipse.core.runtime.IProgressMonitor;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.WriterConfig;

public class ScratchAdapter implements IAdapter {
	private JsonAdapter jsonAdapter;

	public ScratchAdapter() {
		super();
		jsonAdapter = new JsonAdapter();
	}

	@Override
	public boolean isAdaptable(URI uri, IProgressMonitor monitor) {
		return jsonAdapter.isAdaptable(uri, monitor);
	}

	@Override
	public List<IElement> adapt(URI uri, IProgressMonitor monitor) {
		Paths pathsToIgnore = new Paths();
		pathsToIgnore.absolutePaths.add("info");
		pathsToIgnore.relativePaths.add("baseLayerID");
		pathsToIgnore.relativePaths.add("soundID");
		pathsToIgnore.relativePaths.add("currentCostumeIndex");

		Paths pathsUnsplittable = new Paths();
		pathsUnsplittable.absolutePaths.add("children_[]_{}");

		List<IElement> jsonElementList = jsonAdapter.adapt(uri, monitor, pathsToIgnore, pathsUnsplittable);
		List<IElement> scratchElementList = new ArrayList<IElement>();

		for (IElement elt : jsonElementList)
			scratchElementList.add(new ScratchElement(elt));

		return scratchElementList;
	}

	@Override
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {
		try {
			if (uri.toString().endsWith("/")) {
				uri = new URI(uri.toString() + "scratchConstruction.sb2");
			} else if (!uri.toString().endsWith(".zip")) {
				uri = new URI(uri.toString() + ".sb2");
			}

			List<IElement> jsonElements = new ArrayList<IElement>();
			for (IElement element : elements)
				jsonElements.add(((ScratchElement) element).element);

			JsonObject root = jsonAdapter.construct(jsonElements, monitor);
			ZipOutputStream zipOut = ZipUtils.constructZIP(FileUtils.getFile(uri).toPath().toString());

			String md5 = root.get("penLayerMD5").toString().replace("\"", "");
			File file = new File("tmp");
			ScratchUtils.downloadFile(md5, file);
			ZipUtils.addFileToZip(zipOut, file, "0.png");
			file.delete();
			root.set("penLayerMD5", 0);

			int id = 1;

			if (root.get("costumes") != null) {
				for (JsonValue value : root.get("costumes").asArray().values()) {
					JsonObject costume = value.asObject();
					ScratchUtils.costume(zipOut, costume, id);
					id++;
				}
			}
			if (root.get("sounds") != null) {
				for (JsonValue value : root.get("sounds").asArray().values()) {
					JsonObject sound = value.asObject();
					ScratchUtils.sound(zipOut, sound, id);
					id++;
				}
			}
			if (root.get("children") != null) {
				for (JsonValue value : root.get("children").asArray().values()) {
					JsonObject children = value.asObject();
					if (children.get("costumes") != null) {
						for (JsonValue valueBis : children.get("costumes").asArray().values()) {
							JsonObject costume = valueBis.asObject();
							ScratchUtils.costume(zipOut, costume, id);
							id++;
						}
					}
					if (children.get("sounds") != null) {
						for (JsonValue valueBis : children.get("sounds").asArray().values()) {
							JsonObject sound = valueBis.asObject();
							ScratchUtils.sound(zipOut, sound, id);
							id++;
						}
					}
				}
			}

			file = new File("tmp");
			FileUtils.appendToFile(file, root.toString(WriterConfig.PRETTY_PRINT));
			ZipUtils.addFileToZip(zipOut, file, "project.json");
			file.delete();

			zipOut.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
