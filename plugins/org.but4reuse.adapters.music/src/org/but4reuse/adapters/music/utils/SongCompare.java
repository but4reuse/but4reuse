package org.but4reuse.adapters.music.utils;

import java.util.ArrayList;
import java.util.List;

public class SongCompare {

	public static List<Note> getCommonNotes(List<Note> song1, List<Note> song2) {
		double SIMILARITY_THRESHOLD = 1;
		List<Note> common = new ArrayList<Note>();
		for (Note n : song1) {
			for (Note n2 : song2) {
				if (similarity(n, n2) >= SIMILARITY_THRESHOLD) {
					common.add(n);
				}
			}
		}
		return common;
	}

	public static double similarity(Note n1, Note n2) {
		if (n1.isRest() && n2.isRest()) {
			return 0;
		}
		if (n2.isRest() && n1.isRest()) {
			return 0;
		}
		if (n1.getPart().equals(n2.getPart()) && n2.getMeasure().equals(n1.getMeasure())) {
			if (n2.getStartRelativeToMeasure() == n1.getStartRelativeToMeasure()
					&& n2.getDurationRelativeToMeasure() == n1.getDurationRelativeToMeasure()
					&& n2.getPitch().equals(n1.getPitch())) {
				if (n2.isRest()) {
					// This is enough for two silences
					return 1;
				} else {
					// Check for two notes
					if (n2.getOctave().equals(n1.getOctave()) && n2.getType().equals(n1.getType())) {
						return 1;
					}
				}
			}
		}
		return 0;
	}

}
