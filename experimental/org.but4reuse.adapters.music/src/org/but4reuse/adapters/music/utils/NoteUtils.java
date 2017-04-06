package org.but4reuse.adapters.music.utils;

import java.util.ArrayList;
import java.util.List;

public class NoteUtils {

	public static void fillExtraInfo(List<Note> song) {
		for (Note note : song) {
			note.setStartRelativeToMeasure(NoteUtils.getStartRelativeToMeasure(song, note));
			note.setDurationRelativeToMeasure(NoteUtils.getDurationRelativeToMeasure(song, note));
		}
	}

	/**
	 * Get the voices of a measure
	 * 
	 * @param song
	 * @param measureNumber
	 * @return
	 */
	public static List<Integer> getMeasureVoices(List<Note> measureNotes) {
		List<Integer> voices = new ArrayList<Integer>();
		for (Note n : measureNotes) {
			if (!voices.contains(n.getVoice())) {
				voices.add(n.getVoice());
			}
		}
		return voices;
	}

	/**
	 * Get the notes of a given measure and a given voice
	 * 
	 * @param song
	 * @param measureNumber
	 * @return
	 */
	public static List<Note> getMeasureNotesOfAGivenVoice(List<Note> song, int measureNumber, int voice) {
		List<Note> notes = new ArrayList<Note>();
		for (Note n : getMeasureNotes(song, measureNumber)) {
			if (n.getVoice().equals(voice)) {
				notes.add(n);
			}
		}
		return notes;
	}

	/**
	 * Get the notes of a given measure
	 * 
	 * @param song
	 * @param measureNumber
	 * @return
	 */
	public static List<Note> getMeasureNotes(List<Note> song, int measureNumber) {
		List<Note> notes = new ArrayList<Note>();
		for (Note n : song) {
			if (n.getMeasure().equals(measureNumber)) {
				notes.add(n);
			}
		}
		return notes;
	}

	public static double getMeasureDuration(List<Note> measureNotes) {
		// Get a voice
		int voice = getMeasureVoices(measureNotes).get(0);
		double totalDuration = 0;
		Note previousNote = null;
		for (Note n : measureNotes) {
			// Only consider one voice
			if (n.getVoice() == voice) {
				if (previousNote != null && previousNote.getStartTime().equals(n.getStartTime())) {
					// do not add
				} else {
					totalDuration += n.getDuration();
				}
				previousNote = n;
			}
		}
		return totalDuration;
	}

	public static double getDurationBeforeStart(List<Note> measureNotes, Note note) {
		// TODO consider liaison...
		double totalDuration = 0;
		Note previousNote = null;
		for (Note n : measureNotes) {
			if (note.getVoice() == n.getVoice()) {
				if (n.equals(note) || n.getStartTime().equals(note.getStartTime())) {
					return totalDuration;
				}
				if (previousNote != null && previousNote.getStartTime().equals(n.getStartTime())) {
					// do not add
				} else {
					totalDuration += n.getDuration();
				}
				previousNote = n;
			}
		}
		return totalDuration;
	}

	/**
	 * Get duration relative to measure
	 * 
	 * @param song
	 * @param note
	 * @return A value from 0 to 1
	 */
	public static double getDurationRelativeToMeasure(List<Note> song, Note note) {
		// TODO consider liaison...
		List<Note> measureNotes = getMeasureNotes(song, note.getMeasure());
		double totalDuration = getMeasureDuration(measureNotes);
		return note.getDuration() / totalDuration;
	}

	/**
	 * Get start relative to measure
	 * 
	 * @param song
	 * @param note
	 * @return A value from 0 to 1
	 */
	public static double getStartRelativeToMeasure(List<Note> song, Note note) {
		// TODO consider liaison...
		List<Note> measureNotes = getMeasureNotes(song, note.getMeasure());
		double totalDuration = getMeasureDuration(measureNotes);
		double beforeStartDuration = getDurationBeforeStart(measureNotes, note);
		return beforeStartDuration / totalDuration;
	}
}
