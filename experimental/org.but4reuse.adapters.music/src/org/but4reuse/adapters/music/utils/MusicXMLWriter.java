package org.but4reuse.adapters.music.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.music.NoteElement;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.osgi.framework.Bundle;

/**
 * Only for 2/4, TODO improve divisions TODO no slur supported TODO no time
 * modifications (like mozart var3)
 * 
 * @author jabier.martinez
 */
public class MusicXMLWriter {

	public static Document createEmptyScore(String title, String author) {
		Document doc = null;
		try {
			Bundle bundle = Platform.getBundle("org.but4reuse.adapters.music");
			IPath path = new Path("empty.xml");
			URL setupUrl = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
			File file = new File(FileLocator.toFileURL(setupUrl).toURI());
			String empty = FileUtils.getStringOfFile(file);
			empty = empty.replace("TITLE", title);
			empty = empty.replace("AUTHOR", author);
			doc = Jsoup.parse(empty, "UTF-8", Parser.xmlParser());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}

	// TODO adjust voices and add chords
	public static void write(File file, String title, String author, List<IElement> elements) {
		try {
			// create an empty score
			Document doc = createEmptyScore(title, author);

			// create parts
			List<String> parts = new ArrayList<String>();
			int maxMeasures = Integer.MIN_VALUE;
			for (IElement e : elements) {
				if (e instanceof NoteElement) {
					NoteElement note = (NoteElement) e;
					String part = note.note.getPart();
					if (maxMeasures < note.note.getMeasure()) {
						maxMeasures = note.note.getMeasure();
					}
					// create part if it did not exist yet
					if (!parts.contains(part)) {
						parts.add(part);
						doc.select("part-list")
								.append("<score-part id=\""
										+ part
										+ "\"><part-name print-object=\"no\">Piano</part-name><score-instrument id=\"P1-I1\"><instrument-name>Piano</instrument-name></score-instrument><midi-device id=\"P1-I1\" port=\"1\"></midi-device><midi-instrument id=\"P1-I1\"><midi-channel>1</midi-channel><midi-program>1</midi-program><volume>78.7402</volume><pan>0</pan></midi-instrument></score-part>");
						doc.select("score-partwise").append("<part id=\"" + part + "\"></part>");
					}
				}
			}

			// add measures
			for (int m = 1; m <= maxMeasures; m++) {
				doc.select("part").append("\n<measure number=\"" + m + "\"></measure>");
				// Clef etc
				if (m == 1) {
					// see calculateDuration method
					int DIVISIONS = 16;
					doc.select("measure")
							.get(0)
							.append("<print><system-layout><system-margins><left-margin>0.00</left-margin><right-margin>0.00</right-margin></system-margins><top-system-distance>70.00</top-system-distance></system-layout></print><attributes><divisions>"
									+ DIVISIONS
									+ "</divisions><key><fifths>0</fifths></key><time><beats>2</beats><beat-type>4</beat-type></time><clef><sign>G</sign><line>2</line></clef></attributes>");
				}

				// Get notes of the measure
				List<NoteElement> measureNotes = new ArrayList<NoteElement>();
				for (IElement e : elements) {
					if (e instanceof NoteElement) {
						NoteElement note = (NoteElement) e;
						if (note.note.getMeasure() == m) {
							// Ignore silences
							// TODO take into account grace
							if (!note.note.getPitch().equals("Z") && !note.note.isGrace()) {
								measureNotes.add(note);
							}
						}
					}
				}

				// Assign to voices
				List<List<NoteElement>> voices = assignToVoices(measureNotes);
				System.out.println("Measure " + m + " Voices " + voices.size());

				// Print the voice
				int voiceIndex = 0;
				for (List<NoteElement> voice : voices) {
					voiceIndex++;
					// Prepare the measure
					double increment = 0.0625;
					for (double time = 0; time < 1; time += increment) {
						List<NoteElement> startingNow = new ArrayList<NoteElement>();
						List<NoteElement> playingNow = new ArrayList<NoteElement>();

						for (NoteElement note : voice) {
							if (note.note.getStartRelativeToMeasure() == time) {
								startingNow.add(note);
							}
							if (time >= note.note.getStartRelativeToMeasure()
									&& note.note.getStartRelativeToMeasure() + note.note.getDurationRelativeToMeasure() > time) {
								playingNow.add(note);
							}
						}

						// if it is empty the silence is already there
						if (!voice.isEmpty()) {

							if (playingNow.isEmpty()) {
								printRest(doc, m, voiceIndex);
							}

							boolean isChord = startingNow.size() > 1;
							// but the first note of the chord do not need to
							// have the chord tag
							boolean chord = false;
							for (NoteElement startingNote : startingNow) {
								printNote(doc, startingNote, chord, voiceIndex);
								if (isChord) {
									chord = true;
								}
							}
						}
					}

					// finish of the current voice
					if (voices.size() > 1 && voiceIndex != voices.size()) {
						doc.select("measure").get(m - 1).append("\n\t<backup><duration>64</duration></backup>");
					}
				}

				// Finish barline
				if (m == maxMeasures) {
					doc.select("measure").get(m - 1)
							.append("\n\t<barline location=\"right\"><bar-style>light-heavy</bar-style></barline>");
				}
			}

			// write and close
			// this prevent string problems when loading, for example
			// <sign>G</sign> is recognized but <sign> G </sign> is not
			doc.outputSettings().indentAmount(0).prettyPrint(false);

			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
			writer.write(doc.toString());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Assign to voices
	 * 
	 * @param measureNotes
	 * @return
	 */
	public static List<List<NoteElement>> assignToVoices(List<NoteElement> measureNotes) {
		List<List<NoteElement>> voices = new ArrayList<List<NoteElement>>();
		// add the first voice
		List<NoteElement> voice1 = new ArrayList<NoteElement>();
		voices.add(voice1);
		for (NoteElement note : measureNotes) {
			// Check sequentially (voice 1, 2, 3...) if the note can be stored
			// there
			List<NoteElement> newVoice = null;
			boolean voiceFound = false;
			for (List<NoteElement> voice : voices) {
				if (isCompatible(voice, note)) {
					voice.add(note);
					voiceFound = true;
					break;
				}
			}
			if (!voiceFound) {
				newVoice = new ArrayList<NoteElement>();
				newVoice.add(note);
				voices.add(newVoice);
			}
		}
		return voices;
	}

	/**
	 * Check whether a voice can host a note
	 * 
	 * @param voice
	 * @param note
	 * @return boolean
	 */
	public static boolean isCompatible(List<NoteElement> voice, NoteElement note) {
		// it is empty?
		if (voice.isEmpty()) {
			return true;
		}

		double start = note.note.getStartRelativeToMeasure();
		double end = note.note.getStartRelativeToMeasure() + note.note.getDurationRelativeToMeasure();

		// Can it be inside a chord?
		for (NoteElement noteV : voice) {
			if (noteV.note.getStartRelativeToMeasure() == start
					&& noteV.note.getDurationRelativeToMeasure() == note.note.getDurationRelativeToMeasure()) {
				return true;
			}
		}

		// There is place?
		for (NoteElement noteV : voice) {
			double startV = noteV.note.getStartRelativeToMeasure();
			double endV = noteV.note.getStartRelativeToMeasure() + noteV.note.getDurationRelativeToMeasure();
			if (end <= startV || start >= endV) {
				// before or after is good
			} else {
				// Collision
				return false;
			}
		}

		// No collision found
		return true;
	}

	/**
	 * Print rest of the minimum duration
	 * 
	 * @param doc
	 * @param m
	 * @param voice
	 */
	public static void printRest(Document doc, int measure, int voice) {
		doc.select("measure").get(measure - 1)
				.append("\n\t<note><rest/><duration>1</duration><voice>" + voice + "</voice><type>64th</type></note>");
	}

	/**
	 * Print a note
	 * 
	 * @param doc
	 * @param note
	 * @param ischord
	 * @param voice
	 */
	public static void printNote(Document doc, NoteElement note, boolean ischord, int voice) {
		if (!note.note.getPitch().equals("Z")) {
			String chord = "";
			if (ischord) {
				chord = "<chord/>";
			}
			String grace = "";
			String duration = "<duration>" + calculateDuration(note) + "</duration>";
			if (note.note.isGrace()) {
				grace = "<grace slash=\"yes\"/>";
				// grace has no duration
				duration = "";
			}
			String alter = "";
			if (note.note.getAlter() != null) {
				alter = "<alter>" + note.note.getAlter() + "</alter>";
			}
			String dot = "";
			if (note.note.isDot()) {
				dot = "<dot/>";
			}
			String staccato = "";
			if (note.note.isStaccato()) {
				staccato = "<notations><articulations><staccato/></articulations></notations>";
			}
			String noteString = "\n\t<note>" + chord + grace + "<pitch><step>" + note.note.getPitch() + "</step>"
					+ alter + "<octave>" + note.note.getOctave() + "</octave></pitch>" + duration + "<voice>" + voice
					+ "</voice><type>" + note.note.getType() + "</type>" + dot + staccato + "</note>";
			doc.select("measure").get(note.note.getMeasure() - 1).append(noteString);
		}
	}

	/**
	 * TODO It works only for 2beats 4. The divisions are 16 so 2beatsX16 is the
	 * total duration of a measure
	 **/
	public static int calculateDuration(NoteElement note) {
		int duration = 0;
		String type = note.note.getType();
		if (type.equals("64th")) {
			duration = 1;
		} else if (type.equals("32nd")) {
			duration = 2;
		} else if (type.equals("16th")) {
			duration = 4;
		} else if (type.equals("eighth")) {
			duration = 8;
		} else if (type.equals("quarter")) {
			duration = 16;
		} else if (type.equals("half")) {
			duration = 32;
		}
		if (note.note.isDot()) {
			duration = duration / 2 + duration;
		}
		return duration;
	}

}
