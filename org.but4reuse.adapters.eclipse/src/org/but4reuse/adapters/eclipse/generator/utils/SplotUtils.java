package org.but4reuse.adapters.eclipse.generator.utils;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.eclipse.benchmark.ActualFeature;
import org.but4reuse.adapters.eclipse.benchmark.FeatureHelper;

public class SplotUtils {

	private static Map<String, String> prefMap;
	private static Map<String, ActualFeature> mapIdWithFeature;
	
	
	public static String getDependencieTree(String r, String id){
		ActualFeature oneFeat = mapIdWithFeature.get(id);
		if(oneFeat==null)return "";
		String sousSplot="";
	//	boolean nbDepOneFeatSup1;
		
		int nbDependences=oneFeat.getRequiredFeatures().size();
		
		/*if(nbDependences>1){
			sousSplot +=":m "+oneFeat.getId()+r+")";
			r+="_0";
			sousSplot+=":g"+r+")[1,*]";
			nbDepOneFeatSup1=true;
		}else{
			if(!inManyDependence) sousSplot += " :m "+oneFeat.getId()+r+")";
			else sousSplot +=":"+oneFeat.getId()+r+")";
			nbDepOneFeatSup1=false;
		}*/
		sousSplot +=" :m "+oneFeat.getId()+r+")";
		
		for(int j=0;j<nbDependences;j++){
			sousSplot+=getDependencieTree(r+"_"+j,oneFeat.getRequiredFeatures().get(j));
		}	

		return sousSplot;
	}
	
	
	
	
	public static void main(String[] args) throws Exception{
		
		prefMap = PreferenceUtils.getPreferencesMap();
		
		String input = prefMap.get(PreferenceUtils.PREF_INPUT);
		URI inputURI = new File(input).toURI();
		List<ActualFeature> allFeatures = FeatureHelper.getFeaturesOfEclipse(inputURI.toString());

		String splotARetourner="";
		
		if (allFeatures == null || allFeatures.isEmpty())
			return;

		
		mapIdWithFeature = new HashMap<>();
		for (ActualFeature oneFeature : allFeatures) mapIdWithFeature.put(oneFeature.getId(), oneFeature);

		System.out.println(mapIdWithFeature.get("org.eclipse.egit.import"));
		
		for(int i=0;i<allFeatures.size();i++){
			splotARetourner+="<feature_model name=\""+allFeatures.get(i).getName()+"\">\n";
			
			splotARetourner+="<meta>\n";
			splotARetourner+="	<data name=\"description\">"+allFeatures.get(i).getDescription()+"</data>\n";
			/* OPTIONNAL */
//			splotARetourner+="	<data name=\"creator\"/>\n";
//			splotARetourner+="	<data name=\"address\"/>\n";
//			splotARetourner+="	<data name=\"email\"/>\n";
//			splotARetourner+="	<data name=\"phone\"/>\n";
//			splotARetourner+="	<data name=\"website\"/>\n";
//			splotARetourner+="	<data name=\"organization\"/>\n";
//			splotARetourner+="	<data name=\"department\"/>\n";
//			splotARetourner+="	<data name=\"date\"/>\n";
//			splotARetourner+="	<data name=\"reference\"/>\n";
			/* END OPTIONNAL */
			splotARetourner+="</meta>\n";
			
			splotARetourner+="<feature_tree>\n";
			splotARetourner+="  :r "+allFeatures.get(i).getName()+"(_r)";
			for(int j=0;j<allFeatures.get(i).getRequiredFeatures().size();j++){
				if(allFeatures.get(i).getRequiredFeatures().get(j)!=null){
					splotARetourner+=getDependencieTree("(_r_"+(j+1),allFeatures.get(i).getRequiredFeatures().get(j));
				}
			}
			
			splotARetourner+="\n";
			splotARetourner+="</feature_tree>\n";
			
			splotARetourner+="-<constraints></constraints>\n";
			
			splotARetourner+="</feature_model>\n";
			System.out.println(splotARetourner);
			splotARetourner="";
		}
	}
}
