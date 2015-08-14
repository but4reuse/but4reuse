package org.but4reuse.adapters.json.tools;

import java.util.ArrayList;
import java.util.List;

public class Paths {
	public List<String> paths;

	public Paths() {
		this.paths = new ArrayList<String>();
	}

	public Paths(String path) {
		this();
		this.paths.add(path);
	}

	public Paths(List<String> paths) {
		this();
		for (String path : paths)
			this.paths.add(path);
	}

	public Paths(Paths paths) {
		this();
		for (String path : paths.paths)
			this.paths.add(path);
	}

	public void add(String path) {
		this.paths.add(path);
	}

	public void extend(String extension) {
		List<String> newPaths = new ArrayList<String>();
		for (String path : this.paths)
			newPaths.add(path + "_" + extension);
		this.paths = newPaths;
	}

	public void extend(String extension1, String extension2) {
		List<String> newPaths = new ArrayList<String>();
		for (String path : this.paths) {
			newPaths.add(path + "_" + extension1);
			newPaths.add(path + "_" + extension2);
		}
		this.paths = newPaths;
	}

	public boolean matches(Paths paths) {
		for (String path1 : this.paths)
			for (String path2 : paths.paths)
				if (path1.compareTo(path2) == 0)
					return true;
		return false;
	}

	@Override
	public String toString() {
		String s = paths.size() + " path(s) : \n";
		for (String path : paths) {
			s += path + "\n";
		}
		return s;
	}
}
