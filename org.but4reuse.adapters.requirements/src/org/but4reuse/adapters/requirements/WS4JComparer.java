package org.but4reuse.adapters.requirements;

import java.util.ArrayList;
import java.util.Iterator;

import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;

/**
 * Credits: https://bitbucket.org/jnoppen/test-similarity-prototype.git
 * and https://code.google.com/p/ws4j/
 */
public class WS4JComparer {
	
	protected RelatednessCalculator SimCalc;

	WS4JComparer(RelatednessCalculator Calc) {
		SimCalc = Calc;
	}
	
	public final String[] ParseString(String RawString) {
		RawString.replaceAll(",$", " ");
		RawString.replaceAll(".$", " ");
		RawString.replaceAll("!$", " ");
		RawString.replaceAll("\\?$", " ");
		String Split[]= RawString.split("\\s+");
		
		return Split;
	}
	
	public final float CompareText(String Source, String Target) {
		 WS4JConfiguration.getInstance().setMFS(true);
		 
		 String[] SourceSentence = ParseString(Source);
		 String[] TargetSentence = ParseString(Target);
		 
		 return FlattenMatrix(SimCalc.getNormalizedSimilarityMatrix(SourceSentence, TargetSentence));
	}
	
	public final float FlattenMatrix(double[][] SimMatrix) {
		ArrayList<Double> BestMatches = new ArrayList<Double>();		
		int RowNumber = SimMatrix.length;
		int ColumnNumber = -1;
		
		
		for (int i=0;i<SimMatrix.length;i++) {
			double[] CurrentWordResult = SimMatrix[i];
			double BestMatch = -1;
			
			for (int j=0;j<CurrentWordResult.length;j++) {
				double CurrentMatch = CurrentWordResult[j];
				if (CurrentMatch > BestMatch) {
					BestMatch = CurrentMatch;
				}
			}
			
			BestMatches.add(new Double(BestMatch));
			
			if (i==0) {
				ColumnNumber = CurrentWordResult.length;
			}
		}
		
		return FlattenMatchResult(BestMatches, RowNumber, ColumnNumber);
	}
	
	public final float FlattenMatchResult(ArrayList<Double> BestMatches, int RowNumber, int ColumnNumber) {
		int NumberMatched = 0;
		float Accum = 0;
		
		float TresholdNumberMatched = 0;
		float TresholdAccum = 0;
		float Treshold = (float)0.25;
		
		float HighestMatch = 0;
		float LowestMatch = 1;
		
		Iterator<Double> MatchIt = BestMatches.iterator();
		
		while (MatchIt.hasNext()) {
			float CurrentMatch = MatchIt.next().floatValue();
			
			if (CurrentMatch > 0) {
				Accum += CurrentMatch;
				NumberMatched++;
				if (CurrentMatch >= Treshold) {
					TresholdAccum += CurrentMatch;
					TresholdNumberMatched++;
				}
				
				if (CurrentMatch < LowestMatch) {
					LowestMatch = CurrentMatch;
				}
				
				if (CurrentMatch > HighestMatch) {
					HighestMatch = CurrentMatch;
				}
			}
		}
		
		float FractionMatched = (float)NumberMatched / (float)BestMatches.size();
		
		float MatchAverage = Accum / (float)NumberMatched;
		float TresHoldMatchAverage = TresholdAccum / (float)TresholdNumberMatched;
		
		float MatchAmplitude = HighestMatch - LowestMatch;
		
		if (FractionMatched > 0.25) {		// if at least 25% of the source sentence was matched
			if (MatchAmplitude < 0.75) {	// if there is not too much difference in similarity matching
				return MatchAverage;
			}
			else {							// if there is, only look at the ones that made the treshold
				return TresHoldMatchAverage;
			}
		}
		else {
			return 0;
		}
	}

}
