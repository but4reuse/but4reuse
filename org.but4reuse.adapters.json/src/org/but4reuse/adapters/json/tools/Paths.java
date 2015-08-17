package org.but4reuse.adapters.json.tools;

import java.util.ArrayList;
import java.util.List;

public class Paths {
	public List<String> absolutePaths;
	public List<String> relativePaths;

	public Paths() {
		this.absolutePaths = new ArrayList<String>();
		this.relativePaths = new ArrayList<String>();
	}

	public Paths(String path) {
		this();
		this.absolutePaths.add(path);
		this.relativePaths.add(path);
	}

	public Paths(Paths paths) {
		this();
		for (String path : paths.absolutePaths)
			this.absolutePaths.add(path);
		for (String path : paths.relativePaths)
			this.relativePaths.add(path);
	}

	public void addAbsolutePaths(List<String> paths) {
		for (String path : paths)
			this.absolutePaths.add(path);
	}

	public void addRelativePaths(List<String> paths) {
		for (String path : paths)
			this.relativePaths.add(path);
	}

	// Extend method extends all the paths with the extensions given. And adds
	// these extensions as new relatives paths.
	public void extend(String extension) {
		List<String> paths;

		paths = new ArrayList<String>();
		for (String path : this.absolutePaths)
			paths.add(path + "_" + extension);
		this.absolutePaths = paths;

		paths = new ArrayList<String>();
		for (String path : this.relativePaths)
			paths.add(path + "_" + extension);
		this.relativePaths = paths;

		this.relativePaths.add(extension);
	}

	public void extend(String extension1, String extension2) {
		List<String> paths;

		paths = new ArrayList<String>();
		for (String path : this.absolutePaths) {
			paths.add(path + "_" + extension1);
			paths.add(path + "_" + extension2);
		}
		this.absolutePaths = paths;

		paths = new ArrayList<String>();
		for (String path : this.relativePaths) {
			paths.add(path + "_" + extension1);
			paths.add(path + "_" + extension2);
		}
		this.relativePaths = paths;

		this.relativePaths.add(extension1);
		this.relativePaths.add(extension2);
	}

	// Tests whether or not one path (absolute or relative) is common to the 2
	// elements compared.
	public boolean matches(Paths paths) {
		for (String path1 : this.absolutePaths)
			for (String path2 : paths.absolutePaths)
				if (path1.compareTo(path2) == 0)
					return true;
		for (String path1 : this.relativePaths)
			for (String path2 : paths.relativePaths)
				if (path1.compareTo(path2) == 0)
					return true;
		return false;
	}

	@Override
	public String toString() {
		String s = this.absolutePaths.size() + " absolute(s) path(s) : \n";
		for (String path : this.absolutePaths)
			s += "\t" + path + "\n";
		s += this.relativePaths.size() + " relative(s) path(s) : \n";
		for (String path : this.relativePaths)
			s += "\t" + path + "\n";
		s += "\n";
		return s;
	}
}
