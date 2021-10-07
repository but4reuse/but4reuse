package org.but4reuse.feature.location.spectrum;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.Block;

import fk.stardust.localizer.IFaultLocalizer;

import fk.stardust.localizer.sbfl.Ample;
import fk.stardust.localizer.sbfl.Anderberg;
import fk.stardust.localizer.sbfl.ArithmeticMean;
import fk.stardust.localizer.sbfl.Cohen;
import fk.stardust.localizer.sbfl.Dice;
import fk.stardust.localizer.sbfl.Euclid;
import fk.stardust.localizer.sbfl.Fleiss;
import fk.stardust.localizer.sbfl.GeometricMean;
import fk.stardust.localizer.sbfl.Goodman;
import fk.stardust.localizer.sbfl.Hamann;
import fk.stardust.localizer.sbfl.Hamming;
import fk.stardust.localizer.sbfl.HarmonicMean;
import fk.stardust.localizer.sbfl.Jaccard;
import fk.stardust.localizer.sbfl.Kulczynski1;
import fk.stardust.localizer.sbfl.Kulczynski2;
import fk.stardust.localizer.sbfl.M1;
import fk.stardust.localizer.sbfl.M2;
import fk.stardust.localizer.sbfl.Ochiai;
import fk.stardust.localizer.sbfl.Ochiai2;
import fk.stardust.localizer.sbfl.Overlap;
import fk.stardust.localizer.sbfl.RogersTanimoto;
import fk.stardust.localizer.sbfl.Rogot1;
import fk.stardust.localizer.sbfl.Rogot2;
import fk.stardust.localizer.sbfl.RussellRao;
import fk.stardust.localizer.sbfl.Scott;
import fk.stardust.localizer.sbfl.SimpleMatching;
import fk.stardust.localizer.sbfl.Sokal;
import fk.stardust.localizer.sbfl.SorensenDice;
import fk.stardust.localizer.sbfl.Tarantula;
import fk.stardust.localizer.sbfl.Wong1;
import fk.stardust.localizer.sbfl.Wong2;
import fk.stardust.localizer.sbfl.Wong3;
import fk.stardust.localizer.sbfl.Zoltar;

/**
 * Ranking metrics
 * 
 * @author jabier.martinez
 */
public class RankingMetrics {

	public static List<IFaultLocalizer<Block>> getAllRankingMetrics() {
		List<IFaultLocalizer<Block>> rankingMetrics = new ArrayList<>();
		rankingMetrics.add(new Ample<Block>());
		rankingMetrics.add(new Anderberg<Block>());
		rankingMetrics.add(new ArithmeticMean<Block>());
		rankingMetrics.add(new Cohen<Block>());
		rankingMetrics.add(new Dice<Block>());
		rankingMetrics.add(new Euclid<Block>());
		rankingMetrics.add(new Fleiss<Block>());
		rankingMetrics.add(new GeometricMean<Block>());
		rankingMetrics.add(new Goodman<Block>());
		rankingMetrics.add(new Hamann<Block>());
		rankingMetrics.add(new Hamming<Block>());
		rankingMetrics.add(new HarmonicMean<Block>());
		rankingMetrics.add(new Jaccard<Block>());
		rankingMetrics.add(new Kulczynski1<Block>());
		rankingMetrics.add(new Kulczynski2<Block>());
		rankingMetrics.add(new M1<Block>());
		rankingMetrics.add(new M2<Block>());
		rankingMetrics.add(new Ochiai<Block>());
		rankingMetrics.add(new Ochiai2<Block>());
		rankingMetrics.add(new Overlap<Block>());
		rankingMetrics.add(new RogersTanimoto<Block>());
		rankingMetrics.add(new Rogot1<Block>());
		rankingMetrics.add(new Rogot2<Block>());
		rankingMetrics.add(new RussellRao<Block>());
		rankingMetrics.add(new Scott<Block>());
		rankingMetrics.add(new SimpleMatching<Block>());
		rankingMetrics.add(new Sokal<Block>());
		rankingMetrics.add(new SorensenDice<Block>());
		rankingMetrics.add(new Tarantula<Block>());
		rankingMetrics.add(new Wong1<Block>());
		rankingMetrics.add(new Wong2<Block>());
		rankingMetrics.add(new Wong3<Block>());
		rankingMetrics.add(new Zoltar<Block>());
		return rankingMetrics;
	}

	public static IFaultLocalizer<Block> getRankingMetricByName(String rankingMetricName) {
		for(IFaultLocalizer<Block> rankingMetric : getAllRankingMetrics()) {
			if(rankingMetric.getName().equals(rankingMetricName)) {
				return rankingMetric;
			}
		}
		return null;
	}

}
