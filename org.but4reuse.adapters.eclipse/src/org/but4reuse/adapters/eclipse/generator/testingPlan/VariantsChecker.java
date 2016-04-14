package org.but4reuse.adapters.eclipse.generator.testingPlan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.but4reuse.adapters.eclipse.generator.utils.PreferenceUtils;

/**
 * This class launch every variants in an output dir (from your preferences.properties), 
 * and if an error occurs, we have to write the error, and a log is write 
 * in a log file (src/resources/logExec.log) 
 */
public class VariantsChecker {

	public static void main(String[] args) throws IOException {
		boolean isWindows = isWindows(System.getProperty("os.name"));
		
		File logFile = new File("src"+File.separator+"resources"+File.separator+"logExec.log");
		if(!logFile.exists()){
			System.out.println("src"+File.separator+"resources"+File.separator+"logExec.log not exists...");
			return;
		}
		
		List<Process> allProcess = new ArrayList<>(3);
		FileWriter log = new FileWriter(logFile, true);
		String output =  PreferenceUtils.getPreferences().get(PreferenceUtils.PREF_OUTPUT);
		Scanner scan = new Scanner(System.in);
		File outputDir = new File(output);
		
		for(File variant : outputDir.listFiles()){
			
			System.out.println("Check variant : "+variant.getName()+"...");
			
			String outputVar = variant.getPath() + File.separator + "eclipse";
			if(isWindows) outputVar += ".exe";
			
			try{
				new File(outputVar).setExecutable(true);
				new File(outputVar).setWritable(true);
				new File(outputVar).setReadable(true);
			} catch (SecurityException exc){
				exc.printStackTrace();
			}

			String error = "";
			ProcessBuilder builder = new ProcessBuilder(outputVar);
			Process process = null;
			try{
				process = builder.start(); // Start the executable
				allProcess.add(process);
			} catch (Exception e){
				System.out.println("Error when we start the executable ... : "+e);
				e.printStackTrace();
				error = e.toString();
			}
			
			if(error.isEmpty()){ // If there is no exception when we start the process
				System.out.println("An error occurs ?\n(Write it here, finish with ';' and press enter,\n"
						+ "else just press enter)");
				do{
					error += scan.nextLine();
				} while ( !error.isEmpty() && !error.endsWith(";") );
			}
			
			if(!error.isEmpty()){ // If we write an error in the console
				File[] propFile = variant.listFiles(new FilenameFilter() {
					
					@Override
					public boolean accept(File dir, String name) {
						// The file inside each generated variants
						return name.equals("VariantParams.properties");
					}
				});

				String percentage;
				String input;
				
				InputStream is = null;
				try {
					is = new FileInputStream(propFile[0]); // There is only one file
					Properties prop = new Properties();
					prop.load(is);

					percentage = prop.getProperty("random"); // See VariantGenerator
					input = prop.getProperty("input");
					
				} catch (FileNotFoundException ex) {
					throw new FileNotFoundException(
							"Does the \"VariantParams.properties\" file exist ?");
				} finally {
					if (is != null) {
						is.close();
					}
				}
				
				String[] splitDate = new Date(System.currentTimeMillis()).toString().split(" ");
				String dateToString = splitDate[2]+ " " + splitDate[1]
						+ " " + splitDate[5] + " " + splitDate[3];
						
				// %%% escape '%'
				log.write(String.format("(%s) %s (%s) with %s%%.\nError : %s\n\n", dateToString, 
						variant.getName(), input , percentage, error.substring(0, error.length()-1)));
				log.flush();
				
				System.out.println("\n");
			}
			
			if(process!=null) process.destroy();
		}
		
		for(Process one_process : allProcess){
			one_process.destroy();
		}
		
		log.close();
		scan.close();
		System.out.println("Finish");
	}
	
	public static boolean isWindows(String osname) {
		return osname.toLowerCase().contains("win");
	}
	
}
