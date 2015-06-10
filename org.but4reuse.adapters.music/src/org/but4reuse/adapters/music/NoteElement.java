package org.but4reuse.adapters.music;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.adapters.music.utils.Note;

/**
 * Note Element
 * 
 * @author jabier.martinez
 */
public class NoteElement extends AbstractElement {

	Note note;

	public NoteElement(Note note) {
		super();
		this.note = note;
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof NoteElement) {
			NoteElement another = (NoteElement) anotherElement;
			// One is silence and not the other
			if (another.note.isRest() && !note.isRest()) {
				return 0;
			}
			if (!another.note.isRest() && note.isRest()) {
				return 0;
			}
			if (another.note.getPart().equals(note.getPart()) && another.note.getMeasure().equals(note.getMeasure())) {
				if (another.note.getStartRelativeToMeasure() == note.getStartRelativeToMeasure()
						&& another.note.getDurationRelativeToMeasure() == note.getDurationRelativeToMeasure()
						&& another.note.getPitch().equals(note.getPitch())) {
					if (another.note.isRest()) {
						// This is enough for two silences
						return 1;
					} else {
						// Check for two notes
						if (another.note.getOctave().equals(note.getOctave())
								&& another.note.getType().equals(note.getType())) {
							return 1;
						}
					}
				}
			}
		}
		return 0;
	}

	@Override
	public String getText() {
		return note.toString();
	}

}
