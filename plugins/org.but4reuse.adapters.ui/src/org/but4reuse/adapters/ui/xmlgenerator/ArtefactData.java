package org.but4reuse.adapters.ui.xmlgenerator;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
/**
 * The contents of an artefact
 * @author lin_chao
 *
 */
public class ArtefactData {
	// the number of blocks this artefact is divided
	private int nb_blocks;
	// the number of elements of this artefact
	private int nb_elems;
	// name of the artefact
	private String name;
	// the Object representing the artefact
	private AdaptedArtefact aa;
	
	public ArtefactData(int nb_blocks, int nb_elems, String name, AdaptedArtefact aa) {
		this.nb_blocks = nb_blocks;
		this.nb_elems = nb_elems;
		this.name = name;
		this.aa = aa;
	}

	public int getNb_blocks() {
		return nb_blocks;
	}

	public void setNb_blocks(int nb_blocks) {
		this.nb_blocks = nb_blocks;
	}

	public int getNb_elems() {
		return nb_elems;
	}

	public void setNb_elems(int nb_elems) {
		this.nb_elems = nb_elems;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AdaptedArtefact getAa() {
		return aa;
	}

	public void setAa(AdaptedArtefact aa) {
		this.aa = aa;
	}
	
}
