package org.but4reuse.wordclouds.util;

import java.util.Comparator;
import java.util.List;

import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;

/**
 * Cloud ranking
 * 
 * @author jabier.martinez
 */
public class CloudRanking {

	private List<Tag> rank;

	// constructor to create ranking
	public CloudRanking(Cloud cloud) {
		setRank(cloud.allTags(new Comparator<Tag>() {
			@Override
			public int compare(Tag arg0, Tag arg1) {
				return Double.compare(arg1.getScore(), arg0.getScore());
			}
		}));
	}

	public List<Tag> getRank() {
		return rank;
	}

	public void setRank(List<Tag> rank) {
		this.rank = rank;
	}

}
