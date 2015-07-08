package org.but4reuse.adapters.music;

import java.io.File;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.music.utils.MusicXMLWriter;
import org.but4reuse.adapters.music.utils.Note;
import org.but4reuse.adapters.music.utils.NoteUtils;
import org.but4reuse.adapters.music.utils.musicXMLparserDH;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Music Adapter
 * 
 * @author jabier.martinez
 */
public class MusicAdapter implements IAdapter {

	@Override
	/**
	 * is it a musicxml file
	 */
	public boolean isAdaptable(URI uri, IProgressMonitor monitor) {
		// xml is a very common extension, lets return false for the moment and
		// put the responsibility on user selection
		return false;
	}

	@Override
	public List<IElement> adapt(URI uri, IProgressMonitor monitor) {
		List<IElement> elements = new ArrayList<IElement>();
		try {
			File file = FileUtils.getFile(uri);
			musicXMLparserDH parser = new musicXMLparserDH(file.getAbsolutePath());
			parser.parseMusicXML();
			List<Note> songSequenceOfNoteObjects = parser.getNotesOfSong();
			for (Note note : songSequenceOfNoteObjects) {
				// ignore silence
				if (!note.getPitch().equals("Z")) {
					NoteElement noteElement = new NoteElement(note);
					elements.add(noteElement);
				}
			}
			NoteUtils.fillExtraInfo(songSequenceOfNoteObjects);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return elements;
	}

	@Override
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {
		try {
			// Use the given file or use a default name if a folder was given
			if (uri.toString().endsWith("/")) {
				uri = new URI(uri.toString() + "score.xml");
			}
			// Create file if it does not exist
			File file = FileUtils.getFile(uri);
			FileUtils.createFile(file);
			String name = AdaptedModelHelper.getName(AdaptedModelManager.getAdaptedModel());
			if (name == null) {
				name = "";
			}
			String author = "Generated " + System.currentTimeMillis();
			MusicXMLWriter.write(file, name, author, elements);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
