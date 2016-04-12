package org.but4reuse.adapters.eclipse.generator.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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
		sousSplot+="\n";
		for(int k=0;k<r.length();k+=2){
			sousSplot+="	";
		}
		sousSplot +=" :m "+oneFeat.getId()+"("+oneFeat.getName()+")";
		
		for(int j=0;j<nbDependences;j++){
			sousSplot+=getDependencieTree(r+"_"+j,oneFeat.getRequiredFeatures().get(j));
		}	

		return sousSplot;
	}
	
	
	public static File exportToSPLOT(List<ActualFeature> actualFeatures){
		

		if (actualFeatures == null || actualFeatures.isEmpty())
			return null;

		String splotARetourner="";

		mapIdWithFeature = new HashMap<>();
		for (ActualFeature oneFeature : actualFeatures) mapIdWithFeature.put(oneFeature.getId(), oneFeature);

		splotARetourner+="<feature_model name=\"Eclipse Feature\">\n";
		splotARetourner+="<meta>\n";
		/* OPTIONNAL */
//		splotARetourner+="	<data name=\"description\">"+actualFeatures.get(i).getDescription()+"</data>\n";	
//		splotARetourner+="	<data name=\"creator\"/>\n";
//		splotARetourner+="	<data name=\"address\"/>\n";
//		splotARetourner+="	<data name=\"email\"/>\n";
//		splotARetourner+="	<data name=\"phone\"/>\n";
//		splotARetourner+="	<data name=\"website\"/>\n";
//		splotARetourner+="	<data name=\"organization\"/>\n";
//		splotARetourner+="	<data name=\"department\"/>\n";
//		splotARetourner+="	<data name=\"date\"/>\n";
//		splotARetourner+="	<data name=\"reference\"/>\n";
		/* END OPTIONNAL */
		splotARetourner+="</meta>\n";
		splotARetourner+="<feature_tree>\n";
		splotARetourner+="  :r Eclipse Feature(Eclipse Feature)\n";
		for(int i=0;i<actualFeatures.size();i++){
			
			splotARetourner+="		:o "+actualFeatures.get(i).getId()+"("+actualFeatures.get(i).getName()+")";
			List<String> allDependence=null;
			if(actualFeatures.get(i).getRequiredFeatures()!=null)
				allDependence=actualFeatures.get(i).getRequiredFeatures();
			if(actualFeatures.get(i).getIncludedFeatures()!=null)
				allDependence.addAll(actualFeatures.get(i).getIncludedFeatures());
			
			
			for(int j=0;j<allDependence.size();j++){
				if(allDependence.get(j)!=null){
					splotARetourner+=getDependencieTree("(_r_"+(j+1),allDependence.get(j));
				}
			}
			
			splotARetourner+="\n";
			
			System.out.println(i);
		}
		splotARetourner+="</feature_tree>\n";
		splotARetourner+="<constraints></constraints>\n";
		
		splotARetourner+="</feature_model>\n";
		
		PrintWriter writer=null;
		try {
			writer = new PrintWriter("EclipseFeature.xml","UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(writer!=null){
			writer.print(splotARetourner);
			writer.close();
		}
		return null;
		
	}
	
	public static void main(String[] args) throws Exception{
		
		prefMap = PreferenceUtils.getPreferencesMap();
		
		String input = prefMap.get(PreferenceUtils.PREF_INPUT);
		URI inputURI = new File(input).toURI();
		List<ActualFeature> allFeatures = FeatureHelper.getFeaturesOfEclipse(inputURI.toString());

		if (allFeatures == null || allFeatures.isEmpty())
			return;

		exportToSPLOT(allFeatures);
		
//		String splotARetourner="";
//
//		mapIdWithFeature = new HashMap<>();
//		for (ActualFeature oneFeature : allFeatures) mapIdWithFeature.put(oneFeature.getId(), oneFeature);
//
//		
//		for(int i=0;i<allFeatures.size();i++){
//			splotARetourner+="<feature_model name=\""+allFeatures.get(i).getName()+"\">\n";
//			
//			splotARetourner+="<meta>\n";
//			splotARetourner+="	<data name=\"description\">"+allFeatures.get(i).getDescription()+"</data>\n";
//			/* OPTIONNAL */
////			splotARetourner+="	<data name=\"creator\"/>\n";
////			splotARetourner+="	<data name=\"address\"/>\n";
////			splotARetourner+="	<data name=\"email\"/>\n";
////			splotARetourner+="	<data name=\"phone\"/>\n";
////			splotARetourner+="	<data name=\"website\"/>\n";
////			splotARetourner+="	<data name=\"organization\"/>\n";
////			splotARetourner+="	<data name=\"department\"/>\n";
////			splotARetourner+="	<data name=\"date\"/>\n";
////			splotARetourner+="	<data name=\"reference\"/>\n";
//			/* END OPTIONNAL */
//			splotARetourner+="</meta>\n";
//			
//			splotARetourner+="<feature_tree>\n";
//			splotARetourner+="  :r "+allFeatures.get(i).getId()+"(_r)";
//			List<String> allDependence=null;
//			if(allFeatures.get(i).getRequiredFeatures()!=null)
//				allDependence=allFeatures.get(i).getRequiredFeatures();
//			if(allFeatures.get(i).getIncludedFeatures()!=null)
//				allDependence.addAll(allFeatures.get(i).getIncludedFeatures());
//			
//			
//			for(int j=0;j<allDependence.size();j++){
//				if(allDependence.get(j)!=null){
//					splotARetourner+=getDependencieTree("(_r_"+(j+1),allDependence.get(j));
//				}
//			}
//			
//			splotARetourner+="\n";
//			splotARetourner+="</feature_tree>\n";
//			
//			splotARetourner+="<constraints></constraints>\n";
//			
//			splotARetourner+="</feature_model>\n";
//			System.out.println(splotARetourner);
//			if(!(allFeatures.get(i).getName().contains("\\"))){
//				PrintWriter writer= new PrintWriter(allFeatures.get(i).getId()+".xml","UTF-8");
//				writer.print(splotARetourner);
//				writer.close();
//			}
//			splotARetourner="";
//		}
	}
}
