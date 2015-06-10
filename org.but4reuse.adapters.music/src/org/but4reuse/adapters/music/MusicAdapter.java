package org.but4reuse.adapters.music;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
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
		// File file = FileUtils.getFile(uri);
		// if (FileUtils.getExtension(file).equalsIgnoreCase("xml")) {
		// return true;
		// }
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
				NoteElement noteElement = new NoteElement(note);
				elements.add(noteElement);
			}
			NoteUtils.fillExtraInfo(songSequenceOfNoteObjects);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return elements;
	}

	@Override
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {
		// TODO not supported yet
	}

}
