package org.but4reuse.adapters.eclipse.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

import org.but4reuse.adapters.eclipse.generator.utils.PreferenceUtils;

public class CheckVariants {

	public static void main(String[] args) throws IOException {
		boolean isWindows = isWindows(System.getProperty("os.name"));
		
		File logFile = new File("src"+File.separator+"resources"+File.separator+"logExec.log");
		if(!logFile.exists()){
			System.out.println("Le fichier de log n'existe pas...");
			return;
		}
		PrintWriter log = new PrintWriter(logFile);
		String output =  PreferenceUtils.getPreferencesMap().get(PreferenceUtils.PREF_OUTPUT);
		Scanner scan = new Scanner(System.in);
		File outputDir = new File(output);
		
		for(File variant : outputDir.listFiles()){
			System.out.println("Check variant : "+variant.getName()+"...");
			
			String outputVar = variant.getPath() + File.separator + "eclipse";
			if(isWindows) outputVar += ".exe";

			ProcessBuilder builder = new ProcessBuilder(outputVar);
			Process process;
			try{
				process = builder.start(); // Start the executable
				System.out.println();
			} catch (Exception e){
				System.out.println("Ca a pété ... : "+e);
				e.printStackTrace();
				log.close();
				scan.close();
				return;
			}
			
			System.out.println("Une erreur ? (Appuyez sur entrée si rien, sinon, ecrivez l'erreur)");
			String error = scan.nextLine();
			
			if(!error.isEmpty()){ // Si on écris dans la console une erreur
				File[] propFile = variant.listFiles(new FilenameFilter() {
					
					@Override
					public boolean accept(File dir, String name) {
						return name.equals("VariantParams.properties");
					}
				});

				String percentage;
				String input;
				
				InputStream is = null;
				try {
					is = new FileInputStream(propFile[0]); // Il n'y a qu'un seul fichier
					Properties prop = new Properties();
					prop.load(is);

					percentage = prop.getProperty("random"); // Voir VariantGenerator
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
				
				log.printf("(%s) %s with %s%% -> %s\n", dateToString, input , percentage, error);
				log.flush();
			}
			
			process.destroy();
		}
		
		log.close();
		scan.close();
		System.out.println("Finish");
	}
	
	public static boolean isWindows(String osname) {
		return osname.toLowerCase().contains("win");
	}
	
}
