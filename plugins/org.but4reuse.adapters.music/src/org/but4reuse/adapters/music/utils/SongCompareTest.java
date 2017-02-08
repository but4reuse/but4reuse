package org.but4reuse.adapters.music.utils;

import java.util.List;

public class SongCompareTest {
	public static void main(String[] args) {
		try {
			musicXMLparserDH parser = new musicXMLparserDH("tema.xml");
			musicXMLparserDH parser2 = new musicXMLparserDH("var2.xml");
			parser.parseMusicXML();
			parser2.parseMusicXML();
			List<Note> song1 = parser.getNotesOfSong();
			System.out.println("\n\n-------------SONG 1");
			for (Note n : song1) {
				System.out.println(n);
			}
			System.out.println("\n\n-------------SONG 2");
			List<Note> song2 = parser2.getNotesOfSong();
			for (Note n : song2) {
				System.out.println(n);
			}
			System.out.println("\n\n-------------COMMON NOTES");
			List<Note> common = SongCompare.getCommonNotes(song1, song2);
			for (Note n : common) {
				System.out.println(n);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
