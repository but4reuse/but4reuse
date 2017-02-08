package org.but4reuse.wordclouds.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;

/**
 * Automatic Renaming
 * 
 * @author arthur
 * @author jabier.martinez
 * 
 */
public class AutomaticRenaming {

	/**
	 * Rename all
	 * 
	 * @param clouds
	 * @param k
	 *            is the number of words to use before checking conflicts
	 * @return the list of names
	 */
	public static List<String> renameAll(List<Cloud> clouds, int k) {

		// Create first candidate names using k and calculate name collision set
		List<String> names = new ArrayList<String>();
		List<CloudRanking> rankings = new ArrayList<CloudRanking>();
		Map<Integer, List<Integer>> collisionSet = new HashMap<Integer, List<Integer>>();
		for (int x = 0; x < clouds.size(); x++) {
			Cloud cloud = clouds.get(x);
			CloudRanking cr = new CloudRanking(cloud);
			rankings.add(cr);
			String name = "";
			for (int i = 0; i < k; i++) {
				if (i < cr.getRank().size()) {
					if (i != 0) {
						name += " ";
					}
					name += cr.getRank().get(i).getName();
				}
			}

			// check if collision
			int ind = names.indexOf(name);
			if (ind >= 0) {
				List<Integer> s = collisionSet.get(ind);
				if (s == null) {
					s = new ArrayList<Integer>();
				}
				s.add(x);
				collisionSet.put(ind, s);
			}
			names.add(name);

		}

		// ok, now fix collisions
		while (!collisionSet.isEmpty()) {
			k++;
			Map<Integer, List<Integer>> newCollisionSet = new HashMap<Integer, List<Integer>>();
			for (Entry<Integer, List<Integer>> entry : collisionSet.entrySet()) {
				int number = 1;
				List<Integer> indexes = new ArrayList<Integer>();
				indexes.add(entry.getKey());
				indexes.addAll(entry.getValue());
				for (Integer i : indexes) {
					List<Tag> tags = rankings.get(i).getRank();
					String newName = names.get(i);
					if (k > tags.size()) {
						if (newName.isEmpty()) {
							// special case when the cloud is empty
							newName += number;
						} else {
							newName += " " + number;
						}
						number++;
					} else {
						newName += " " + tags.get(k - 1).getName();
					}
					int ind = names.indexOf(newName);
					if (ind >= 0) {
						List<Integer> s = newCollisionSet.get(ind);
						if (s == null) {
							s = new ArrayList<Integer>();
						}
						s.add(i);
						newCollisionSet.put(ind, s);
					}
					names.set(i, newName);
				}
				collisionSet = newCollisionSet;
			}
		}
		return names;
	}

}
