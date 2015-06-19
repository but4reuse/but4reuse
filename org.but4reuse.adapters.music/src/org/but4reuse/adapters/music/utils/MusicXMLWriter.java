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

public class MusicXMLWriter {

	public static Document createEmptyScore(String title, String author) {
		Document doc = null;
		try{
			Bundle bundle = Platform.getBundle("org.but4reuse.adapters.music");
			IPath path = new Path("empty.xml");
			URL setupUrl = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
			File file = new File(FileLocator.toFileURL(setupUrl).toURI());
			String empty = FileUtils.getStringOfFile(file);
			empty = empty.replace("TITLE", title);
			empty = empty.replace("AUTHOR", author);
			doc = Jsoup.parse(empty, "UTF-8", Parser.xmlParser());
		} catch(Exception e){
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
				if (m == 1) {
					// Clef etc
					doc.select("measure")
							.get(0)
							.append("<print><system-layout><system-margins><left-margin>0.00</left-margin><right-margin>0.00</right-margin></system-margins><top-system-distance>70.00</top-system-distance></system-layout></print><attributes><divisions>4</divisions><key><fifths>0</fifths></key><time><beats>2</beats><beat-type>4</beat-type></time><clef><sign>G</sign><line>2</line></clef></attributes>");
				}
				if (m == maxMeasures) {
					// Finish barline
					doc.select("measure").get(m - 1)
							.append("\n\t<barline location=\"right\"><bar-style>light-heavy</bar-style></barline>");
				}
			}

			// Add notes
			for (IElement e : elements) {
				if (e instanceof NoteElement) {
					NoteElement note = (NoteElement) e;
					if (!note.note.getPitch().equals("Z")) {
						String grace = "";
						String duration = "<duration>" + note.note.getDuration() + "</duration>";
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
						if (note.note.isStaccato()){
							staccato = "<notations><articulations><staccato/></articulations></notations>";
						}
						String noteString = "\n\t<note>" + grace + "<pitch><step>" + note.note.getPitch() + "</step>"
								+ alter + "<octave>" + note.note.getOctave() + "</octave></pitch>" + duration
								+ "<voice>" + note.note.getVoice()
								+ "</voice><type>" + note.note.getType() + "</type>"
								+ dot + staccato + "</note>";
						doc.select("measure").get(note.note.getMeasure() - 1).append(noteString);
					}
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
	};

}
