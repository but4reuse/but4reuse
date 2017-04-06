package org.but4reuse.adapters.music.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

/**
 * Created by Dorien Herremans on 02/02/15.
 */
public class musicXMLparserDH {

	public org.jsoup.nodes.Document doc;
	HashMap<Integer, Integer> divMultiplier;
	private ArrayList<Note> notesOfSong;

	public musicXMLparserDH(String filename) throws IOException {

		File input = new File(filename);
		doc = Jsoup.parse(input, "UTF-8", filename);

		// check if characterset is ok

		// OutputStreamWriter writer = new OutputStreamWriter(new
		// FileOutputStream(input), "UTF-8");

		if (doc.getElementsByTag("note").isEmpty()) {
			doc = Jsoup.parse(input, "UTF-16", filename);
			if (doc.getElementsByTag("note").isEmpty()) {
				System.out.println("Please check that your file is encoded in UTF-8 or UTF-16 and contains notes.");
			}
		}

	}

	public String[] parseMusicXML() {
		// read in a MusicXNL file

		// check part-list for divisions
		// for each part
		// for each staff and voice
		// rests

		ArrayList<Integer> voicesIndex = new ArrayList<Integer>(); // list of
																	// unique
																	// voices
		ArrayList<String> song = new ArrayList<String>();
		notesOfSong = new ArrayList<Note>();

		String[] flatSong = null;

		// get the number of divisions and the change throughout the piece

		ArrayList<Integer> divisions = new ArrayList<Integer>();

		for (Element thisdiv : this.doc.getElementsByTag("divisions")) {
			divisions.add(Integer.valueOf(thisdiv.text()));
		}
		divMultiplier = new HashMap<Integer, Integer>();
		// System.out.print(this.doc.text());

		if (!this.doc.getElementsByTag("divisions").isEmpty()) {

			// System.out.println("divisions; " + lcm(divisions));
			Integer lcm = lcm(divisions);

			// set the multiplier for each division.
			for (Integer i : divisions) {
				divMultiplier.put(i, (int) lcm / i);
			}
		} else {

			divMultiplier.put(1, 1);
		}
		// voicesIndex = getNumberOfVoices();

		notesOfSong = getAllNotes();

		flatSong = setSongArrayOfStrings(notesOfSong);

		return flatSong;

	}

	public ArrayList<Note> getNotesOfSong() {
		return notesOfSong;
	}

	private String[] setSongArrayOfStrings(ArrayList<Note> notesOfSong) {

		String[] flatSong;
		// find total duration
		Integer numberOfSlices = 0;
		for (Note inote : notesOfSong) {
			if (inote.getDuration() + inote.getStartTime() > numberOfSlices) {
				numberOfSlices = inote.getDuration() + inote.getStartTime();
			}
			// System.out.print("starters: " + inote.getStartTime() + " ");
		}
		// System.out.print("slices: " + numberOfSlices);

		// array in elaine's string format

		flatSong = new String[numberOfSlices];
		for (Note inote : notesOfSong) {
			// System.out.print(inote.getDuration() );
			if (inote.getPitch() != "Z") {
				for (int i = 0; i < inote.getDuration(); i++) {
					if (flatSong[i + inote.getStartTime()] == null) {
						flatSong[i + inote.getStartTime()] = "";
					} else {
						flatSong[i + inote.getStartTime()] += " ";

					}

					flatSong[i + inote.getStartTime()] += inote.getPitch() + inote.getAccidental();
				}
			}
		}

		// remove lines with only rests

		int count = 0;
		for (int i = 0; i < flatSong.length; i++) {
			if (flatSong[i] == null) {
				count++;
			}
		}

		// System.out.println("full rests: " + count + "end");
		// System.out.println("length: " + flatSong.length + "end");
		String[] newFlatSong = new String[flatSong.length - count];

		count = 0;
		for (int i = 0; i < flatSong.length; i++) {
			if (flatSong[i] == null) {
				count++;
			} else {
				newFlatSong[i - count] = flatSong[i];

			}
		}

		// System.out.print("test"+flatSong.length) ;

		return newFlatSong;
	}

	private ArrayList<Integer> getNumberOfVoices() {
		// detect number of voices

		ArrayList<Integer> voices = new ArrayList<Integer>();
		ArrayList<Integer> voicesIndex = new ArrayList<Integer>(); // list of
																	// unique
																	// voices

		voices.clear();

		for (Element note : this.doc.getElementsByTag("Note")) {

			// System.out.print("voice: "+
			// note.getElementsByTag("voice").text());
			voices.add(Integer.valueOf(note.getElementsByTag("voice").text()));

		}

		// System.out.print("size: " + voices.size());

		// select only unique voices

		Collections.sort(voices);
		voicesIndex.clear();
		voicesIndex.add(voices.get(0));
		if (voices.size() > 1) {
			for (int i = 1; i < voices.size(); i++) {
				if (voices.get(i) != voices.get(i - 1)) {
					voicesIndex.add(voices.get(i));
				}
			}
		}

		return voicesIndex;
	}

	private ArrayList<Note> getAllNotes() {

		ArrayList<Note> notesOfSong = new ArrayList<Note>();
		Integer currentVoice = 0;

		Integer duration;
		// HashMap<Integer, Integer> positionOfVoice = new HashMap();
		// HashMap<Integer, Integer> lastDurationsOfVoices = new HashMap();

		notesOfSong.clear();
		// positionOfVoice.clear();
		String[] flatSong;
		Integer position = 0;
		Integer lastDuration = 0;
		Integer counter = 0;

		// //keep the last duration for each voice
		// for (Integer voice : voicesIndex) {
		// lastDurationsOfVoices.put(voice, 0);
		// }
		//
		// //keep a start duration for each voice
		// for (Integer voice : voicesIndex) {
		// positionOfVoice.put(voice, 0);
		// }

		for (Element thisPart : this.doc.select("part")) {

			String partId = thisPart.attr("id");
			Integer divisions = 1;

			// get all notes
			// for (Element thiselement : thisPart.children()) {
			// for (Element thisnote : thisPart.getElementsByTag("Note")) {
			// go through all the elements, and only if note and forward
			// backward
			for (Element thismeasure : thisPart.getElementsByTag("measure")) {
				int measureNumber = Integer.valueOf(thismeasure.attr("number"));

				if (!thismeasure.getElementsByTag("divisions").isEmpty()) {
					divisions = Integer.valueOf(thismeasure.getElementsByTag("divisions").text());
				}

				for (Element thisnote : thismeasure.children()) {
					// thiselement.getElementsByTag("Note")

					// System.out.println(thisnote.tagName());
					if (thisnote.tagName().equals("note")) {

						Note note = new Note(0);
						note.setMeasure(measureNumber);
						note.setPart(partId);
						counter++;

						// System.out.println("\n note:");

						// System.out.print("DIVISIONS: " + divisions);

						// get current voice
						currentVoice = Integer.valueOf(thisnote.getElementsByTag("voice").text());
						note.setVoice(currentVoice);

						// get the pitch
						if (!thisnote.getElementsByTag("pitch").isEmpty()) {
							for (Element thispitch : thisnote.getElementsByTag("pitch")) {
								// System.out.print(thispitch.getElementsByTag("step").text());
								note.setPitch(thispitch.getElementsByTag("step").text());
								if (thispitch.getElementsByTag("octave") != null) {
									note.setOctave(Integer.valueOf(thispitch.getElementsByTag("octave").text()));
								}
								// System.out.print("alter: " +
								// thispitch.getElementsByTag("alter").text());
								String alter = String.valueOf(thispitch.getElementsByTag("alter").text());
								if (!thispitch.getElementsByTag("alter").isEmpty()) {
									note.setAlter(alter);
									if (alter.equals("1")) {
										note.setAccidental("#");
									} else if (alter.equals("-1")) {
										note.setAccidental("b");
									} else if (alter.equals("2")) {
										note.setAccidental("##");
									} else if (alter.equals("-2")) {
										note.setAccidental("bb");
									}
								}
							}

						} else { // if (note.getElementsByTag("rest")!= null){
							// getting other elements like rests is important
							// for determining start onsets of the next note
							// System.out.println(note.getElementsByTag("rest").text());

							note.setPitch("Z");

						}
						if (!thisnote.getElementsByTag("type").isEmpty()) {
							String type = String.valueOf(thisnote.getElementsByTag("type").text());
							note.setType(type);
						}

						// System.out.print("\n" + note.getPitch() + " ");
						// System.out.print(" for " +
						// thisnote.getElementsByTag("duration").text() + ", ");

						// System.out.println(thisnote.text()+"ok"+ divisions);

						if (thisnote.getElementsByTag("duration").text().isEmpty()) {
							duration = 0; // todo ornamentals
						} else {
							duration = Integer.valueOf(thisnote.getElementsByTag("duration").text())
									* divMultiplier.get(divisions);

						}

						note.setDuration(duration);
						// check if dot
						if (!thisnote.getElementsByTag("dot").isEmpty()) {
							note.setDot(true);
						}

						if (!thisnote.getElementsByTag("grace").isEmpty()) {
							note.setGrace(true);
						}

						if (!thisnote.getElementsByTag("staccato").isEmpty()) {
							note.setStaccato(true);
						}

						// System.out.print(" dur: " + duration);
						// System.out.print("voice: " + currentVoice);

						// now check if it is a chord
						if (!thisnote.getElementsByTag("chord").isEmpty()) {

							note.setStartTime(position);

							// retract previous duration
							note.setStartTime(position - lastDuration);
							// System.out.print(" start: " +
							// note.getStartTime());

							// System.out.print("chord");

						} else {
							// increment start time of the current voice
							// System.out.print(" start: " + position);
							note.setStartTime(position);
							position = position + duration;

						}

						lastDuration = duration;
						note.setCounter(counter);

						notesOfSong.add(note);

					} else if (thisnote.tagName().equals("forward")) {
						position = position + Integer.valueOf(thisnote.getElementsByTag("duration").text())
								* divMultiplier.get(divisions);

					} else if (thisnote.tagName().equals("backup")) {
						// System.out.println("BACKUP" +
						// Integer.valueOf(thisnote.getElementsByTag("duration").text())
						// * divMultiplier.get(divisions));
						position = position - Integer.valueOf(thisnote.getElementsByTag("duration").text())
								* divMultiplier.get(divisions);

					}
				}

			}
		}

		NoteUtils.fillExtraInfo(notesOfSong);

		return notesOfSong;
	}

	/**
	 * get greatest common denominator
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private static long gcd(long a, long b) {
		while (b > 0) {
			long temp = b;
			b = a % b; // % is remainder
			a = temp;
		}
		return a;
	}

	private static long gcd(long[] input) {
		long result = input[0];
		for (int i = 1; i < input.length; i++)
			result = gcd(result, input[i]);
		return result;
	}

	/**
	 * get least common multiplier
	 */
	private static long lcm(long a, long b) {
		return a * (b / gcd(a, b));
	}

	/**
	 * get least common multiplier of a list
	 */
	private static Integer lcm(ArrayList<Integer> inputInt) {
		long[] input = new long[inputInt.size()];

		for (int i = 0; i < inputInt.size(); i++) {
			input[i] = inputInt.get(i);

		}
		long result = input[0];
		for (int i = 1; i < input.length; i++)
			result = lcm(result, input[i]);
		return ((int) result);
	}

}
