package org.but4reuse.adapters.eclipse.generator.testingPlan;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.but4reuse.adapters.eclipse.generator.VariantsGenerator;
import org.but4reuse.adapters.eclipse.generator.utils.PreferenceUtils;
import org.but4reuse.adapters.eclipse.generator.utils.VariantsUtils;

/**
 * This class generate a lot of variants (following the testing plan
 * in src/resources/TestingPlan.txt), in specific directories.
 * Each line of this file lead a creation of 3 variants, with 
 * the inputs and random specified.
 * <br>/!\ It's important to have exactly the same inputs as TestingPlan.txt   
 */
public class VariantsCreator {

	public static final String TESTING_PLAN = "TestingPlan.txt";
	public static final String input_separator = "_"; // ex: RCP_Java
	private static final int NB_VARIANTS = 3;
	
	public void process(){
		System.out.print("Your directory of Eclipse inputs registered is : ");
		File inputDir;
		try {
			inputDir = new File(PreferenceUtils.getPreferences().get(PreferenceUtils.PREF_INPUT));
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		String input;
		if(VariantsUtils.isEclipseDir(inputDir)) input = inputDir.getParent();
		else input = inputDir.getPath();
		
		System.out.println(input);
		
		Scanner scan = new Scanner(System.in);
		System.out.println("If you want to use it, press enter, else, write the new :");
		String newDir = null;
		while(newDir==null) {
			newDir = scan.nextLine();
			if(newDir.isEmpty()) break;
			else if (!new File(newDir).exists()){
				System.out.println("Your path doesn't exists. Retry...");
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
		
		System.out.print("\nYour directory of Eclipse output registered is : ");
		File outputDir;
		try {
			outputDir = new File(PreferenceUtils.getPreferences().get(PreferenceUtils.PREF_OUTPUT));
		} catch (IOException e1) {
			e1.printStackTrace();
			scan.close();
			return;
		}
		System.out.println(outputDir);
		
		System.out.println("If you want to use it, press enter, else, write the new :");
		String output = outputDir.getAbsolutePath();
		newDir = null;
		while(newDir==null) {
			newDir = scan.nextLine();
			if(newDir.isEmpty()) break;
			else if (!new File(newDir).exists()){
				System.out.println("Your path doesn't exists. Retry...");
				newDir=null;
			} else {
				try {
					PreferenceUtils.savePreferences(null, newDir, null, null);
				} catch (IOException e) {
					e.printStackTrace();
					scan.close();
					return;
				}
				output = newDir; // new output OK
			}
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
		
		System.out.println("\nWhat is your generation variants choice ?\n(Write the number)\n");
		List<String> choices = null;
		int nbChoices;
		try {
			choices = getAllChoices(testingPlan);
			nbChoices = choices.size();
			fstream.getChannel().position(0); // reset at top
		} catch (IOException e) {
			closeAll(testingPlan, scan);
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
				System.out.println("Error, choose an integer between 1 and "+nbChoices);
				continue;
			}
			
			if(choice_int!=-1 && (choice_int<1 || choice_int>nbChoices)){
				System.out.println("Error, choose an integer between 1 and "+nbChoices);
				choice_int = -1;
				continue;
			} else {
				choice = choices.get(choice_int-1).replaceAll("\\D+","");
				break;
			}
		}
		
		List<String> allInputs;
		try {
			allInputs = getAllInputsFrom(testingPlan);
		} catch (IOException e) {
			closeAll(testingPlan, scan);
			e.printStackTrace();
			return;
		}
		
		System.out.println("All your eclipse inputs\n");
		List<String> allExistingInputs = new ArrayList<>();
		for(String one_input : allInputs){

			if(new File(input + one_input).exists()){
				System.out.println(one_input+" : exists");
				allExistingInputs.add(one_input);
			} else {
				System.out.println(one_input+" : not exists");
			}
		}
		
		if(allExistingInputs.isEmpty()){
			System.out.println("There is no inputs ...");
			closeAll(testingPlan, scan);
			return;
		}
		
		
		System.out.println("\nVariants creation start ...");
		for(String existingInput : allExistingInputs){
			try{
				String output_tmp = output;
				if(!output_tmp.endsWith(File.separator)) output_tmp+=File.separator;
				new VariantsGenerator(input+existingInput, output_tmp+existingInput+"-"+choice, 
						NB_VARIANTS, Integer.parseInt(choice)).generate();
			} catch (Exception e){
				System.out.println("Erreur avec la création de "+existingInput+" : "+e);
				e.printStackTrace();
			}
			System.out.println("\n===================================================\n");
		}
		closeAll(testingPlan, scan);
	}
	
	
	public static void main(String[] args) {
		VariantsCreator creator = new VariantsCreator();
		
		try{
			creator.process();
		} catch ( Error | Exception e){
			StackTraceElement[] trace = e.getStackTrace();
			for(StackTraceElement elem : trace){
				System.out.println(elem);
			}
		}
		
	}	
	private void closeAll(Closeable... flux) {
		for(Closeable oneflux : flux){
			try {
				oneflux.close();
			} catch (IOException e) {}
		}
		
	}

	private List<String> getAllChoices(BufferedReader testingPlan) throws IOException{
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
	
	private List<String> getAllInputsFrom(BufferedReader testingPlan) throws IOException{
		if(testingPlan==null) return null;
		
		List<String> list = new ArrayList<String>(21); 
		
		String line = null;
		while((line=testingPlan.readLine())!=null){	// ex: JEE_Testing or #Variants with 10%
			if(line.isEmpty() || line.startsWith("#")){
				continue;
			} else {
				if(!list.contains(line)) list.add(line);
			}
		}
		
		return list;
	}
}
