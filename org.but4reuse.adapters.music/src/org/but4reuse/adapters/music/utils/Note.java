package org.but4reuse.adapters.music.utils;

/**
 * Created by Dorien Herremans on 01/02/15.
 */
public class Note {

	private Integer startTime;
	private Integer duration;
	private String pitch; // Z is for rest
	private Integer octave;
	private Integer voice;
	private String alter;
	private String accidental;
	private Integer counter;
	private String type;
	private String part;
	private Integer measure;
	private double startRelativeToMeasure;
	private double durationRelativeToMeasure;
	private boolean dot;
	private boolean grace;
	private boolean staccato;

	public void setCounter(Integer counter) {
		this.counter = counter;
	}

	public Integer getCounter() {
		return counter;
	}

	public Integer getVoice() {
		return voice;
	}

	public void setVoice(Integer voice) {
		this.voice = voice;
	}

	public Note(Integer startTime) {
		this.startTime = startTime;
		this.accidental = "";

	}

	public void setStartTime(Integer startTime) {
		this.startTime = startTime;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public void setPitch(String pitch) {
		this.pitch = pitch;
	}

	public Integer getStartTime() {
		return startTime;
	}

	public Integer getDuration() {
		return duration;
	}

	public String getPitch() {
		return pitch;
	}

	public void setAccidental(String accidental) {
		this.accidental = accidental;
	}

	public String getAccidental() {
		return accidental;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getOctave() {
		return octave;
	}

	public void setOctave(Integer octave) {
		this.octave = octave;
	}

	public Integer getMeasure() {
		return measure;
	}

	public void setMeasure(Integer measure) {
		this.measure = measure;
	}

	public double getStartRelativeToMeasure() {
		return startRelativeToMeasure;
	}

	public void setStartRelativeToMeasure(double startRelativeToMeasure) {
		this.startRelativeToMeasure = startRelativeToMeasure;
	}

	public double getDurationRelativeToMeasure() {
		return durationRelativeToMeasure;
	}

	public void setDurationRelativeToMeasure(double durationRelativeToMeasure) {
		this.durationRelativeToMeasure = durationRelativeToMeasure;
	}

	public String getPart() {
		return part;
	}

	public void setPart(String part) {
		this.part = part;
	}

	public boolean isRest() {
		return pitch.equals("Z");
	}

	public boolean isDot() {
		return dot;
	}

	public void setDot(boolean dot) {
		this.dot = dot;
	}

	public boolean isGrace() {
		return grace;
	}

	public void setGrace(boolean grace) {
		this.grace = grace;
	}

	public String getAlter() {
		return alter;
	}

	public void setAlter(String alter) {
		this.alter = alter;
	}

	public boolean isStaccato() {
		return staccato;
	}

	public void setStaccato(boolean staccato) {
		this.staccato = staccato;
	}

	@Override
	public String toString() {
		String info = " Silence";
		if (!isRest()) {
			info = " Pitch: " + getPitch() + getAccidental() + " Octave: " + getOctave();
		}
		String grace = "";
		if (isGrace()) {
			grace = " Grace";
		}
		String dot = "";
		if (isDot()) {
			dot = " Dot";
		}
		return "Part: " + getPart() + " Measure: " + getMeasure() + info + " Type: " + getType() + dot + grace
				+ " Duration: " + getDuration() + " Voice: " + getVoice() + " StartRelativeToMeasure: "
				+ getStartRelativeToMeasure() + " DurationRelativeToMeasure: " + getDurationRelativeToMeasure();
	}

}
