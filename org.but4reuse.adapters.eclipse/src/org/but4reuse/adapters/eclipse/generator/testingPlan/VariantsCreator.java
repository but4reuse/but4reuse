package org.but4reuse.adapters.eclipse.generator.testingPlan;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.but4reuse.adapters.eclipse.generator.utils.PreferenceUtils;

/**
 * This class generate a lot of variants (following the testing plan
 * in src/resources/TestingPlan.txt), in specific directories.
 * Each line of this file lead a creation of 3 variants, with 
 * the inputs and random specified.
 * /!\ It's important to have exactly the same inputs as TestingPlan.txt   
 */
public class VariantsCreator {

	public static final String TESTING_PLAN = "TestingPlan.txt";
	public static final String value_separator = "-"; // input-random
	public static final String input_separator = "_"; //RCP_Java
	
	
	public static void main(String[] args) {
		
		// TODO: traduire en anglais
		
		System.out.print("Votre dossier des input d'Eclipse enregistré est : ");
		File inputDir;
		try {
			inputDir = new File(PreferenceUtils.getPreferences().get(PreferenceUtils.PREF_INPUT));
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		String input = inputDir.getParent();
		System.out.println(input);
		
		Scanner scan = new Scanner(System.in);
		System.out.println("Si vous désirez l'utiliser, appuyez sur enter, sinon, écrivez le nouveau :");
		String newDir = null;
		while(newDir==null) {
			newDir = scan.nextLine();
			if(newDir.isEmpty()) break;
			else if (!new File(newDir).exists()){
				System.out.println("Votre chemin n'existe pas. Ressayez...");
				newDir=null;
			} else {
				try {
					PreferenceUtils.savePreferences(newDir, null, null, null);
				} catch (IOException e) {
					e.printStackTrace();
					scan.close();
					return;
				}
				input = newDir; // new input OK
			}
		}
		
		if(!input.endsWith(File.separator)) input+=File.separator;
		
		File testingPlanFile = new File("src"+File.separator+"resources"+File.separator+TESTING_PLAN);
		if(!testingPlanFile.exists()){
			System.out.println("\nsrc"+File.separator+"resources"+File.separator+TESTING_PLAN+" not exists...");
			scan.close();
			return;
		}
		
		FileInputStream fstream = null;
		BufferedReader testingPlan = null;
		try {
			fstream = new FileInputStream(testingPlanFile);
			testingPlan = new BufferedReader(new InputStreamReader(fstream));
		} catch (IOException e) {
			e.printStackTrace();
			closeAll(scan, fstream, testingPlan);
			return;
		}
		
		
		System.out.println("\nQuel est votre choix de génération de variantes ?\n(Ecrivez le numéro désirez)\n");
		List<String> choices = null;
		int nbChoices;
		try {
			choices = getAllChoices(testingPlan);
			nbChoices = choices.size();
			fstream.getChannel().position(0); // reset at top
		} catch (IOException e) {
			closeAll(testingPlan, fstream, scan);
			e.printStackTrace();
			return;
		}
		
		for(int i=1; i<=nbChoices; i++){
			System.out.println(i+" : "+choices.get(i-1));
		}
		
		String choice = null;
		int choice_int = -1;
		while(choice_int==-1) {
			
			choice = scan.nextLine();
			try{
				choice_int = Integer.parseInt(choice);
			} catch ( NumberFormatException exc ){
				System.out.println("Erreur, choisissez un entier entre 1 et "+nbChoices);
				continue;
			}
			
			if(choice_int!=-1 && (choice_int<1 || choice_int>nbChoices)){
				System.out.println("Erreur, choisissez un entier entre 1 et "+nbChoices);
				choice_int = -1;
				continue;
			} else {
				choice = choices.get(choice_int-1).replaceAll("\\D+","");
				break;
			}
		}
		
		scan.close();
		
		Map<Integer, List<String>> allValues;
		try {
			allValues = getAllValuesFrom(testingPlan, choice);
		} catch (IOException e) {
			closeAll(testingPlan, fstream, scan);
			e.printStackTrace();
			return;
		}
		
		System.out.println("\nLe contenu de "+choice+"%");
		for(Entry<Integer, List<String>> entry : allValues.entrySet()){
			for(String value : entry.getValue()){

				if(new File(input + value).exists()){
					System.out.printf("%s : %s (%s)\n", entry.getKey(), value, "exists");
				} else {
					System.out.printf("%s : %s (%s)\n", entry.getKey(), value, "not exists");
				}
			}
		}
		
		closeAll(testingPlan, fstream, scan);
	}
	
	private static void closeAll(Closeable... flux) {
		for(Closeable oneflux : flux){
			try {
				oneflux.close();
			} catch (IOException e) {}
		}
		
	}

	private static List<String> getAllChoices(BufferedReader testingPlan) throws IOException{
		if(testingPlan==null || !testingPlan.ready()) return null;
		
		List<String> allChoices = new ArrayList<String>(5);
		String line = null;
		while((line=testingPlan.readLine())!=null){
			if(!line.startsWith("#")) continue;
			else {
				allChoices.add(line.substring(1));
			}
		}
		return allChoices;
	}
	
	private static Map<Integer, List<String>> getAllValuesFrom(BufferedReader testingPlan, String tranche) throws IOException{
		if(testingPlan==null) return null;
		
		// keys = 10,20,50,100
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>(4); 
		
		String line = null;
		while((line=testingPlan.readLine())!=null){
			if(line.isEmpty() || line.startsWith("#")) continue;
			else { // ex: JEE_Testing-50
				
				try{
					String[] split = line.split(value_separator);
					if(!split[1].equals(tranche)) continue; // if random != choice
					
					int rand = Integer.parseInt(split[1]);
					if(map.containsKey(rand)){
						map.get(rand).add(split[0]);
					} else {
						List<String> values = new ArrayList<>(4);
						values.add(split[0]);
						map.put(rand, values);
					}
					
				} catch (Exception e){
					System.err.println("Error with line : "+line);
				}
			}
		}
		
		return map;
	}
}
