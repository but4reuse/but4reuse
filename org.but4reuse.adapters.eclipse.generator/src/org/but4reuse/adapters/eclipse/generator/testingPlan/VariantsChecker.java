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
import org.but4reuse.adapters.eclipse.generator.utils.VariantsUtils;

/**
 * This class launch every variants in an output dir (from your
 * preferences.properties), and if an error occurs, we have to write the error,
 * and a log is write in a log file (src/resources/logExec.log)
 */
public class VariantsChecker {

	public static void main(String[] args) throws IOException {
		boolean isWindows = isWindows(System.getProperty("os.name"));

		File logFile = new File("src" + File.separator + "resources" + File.separator + "logExec.log");
		if (!logFile.exists()) {
			System.out.println("src" + File.separator + "resources" + File.separator + "logExec.log not exists...");
			return;
		}

		List<Process> allProcess = new ArrayList<>(3);
		FileWriter log = new FileWriter(logFile, true);
		Scanner scan = new Scanner(System.in);
		System.out.print("Your directory of Eclipse output registered is : ");

		File outputDir;
		try {
			outputDir = new File(PreferenceUtils.getPreferences().get(PreferenceUtils.PREF_OUTPUT));
		} catch (IOException e1) {
			e1.printStackTrace();
			scan.close();
			log.close();
			return;
		}
		System.out.println(outputDir);

		System.out.println("\nIf you want to use it, press enter, else, write the new :");
		String newDir = null;
		while (newDir == null) {
			newDir = scan.nextLine();
			if (newDir.isEmpty())
				break;
			else if (!new File(newDir).exists()) {
				System.out.println("Your path doesn't exists. Retry...");
				newDir = null;
			} else {
				try {
					PreferenceUtils.savePreferences(null, newDir, null, null);
				} catch (IOException e) {
					e.printStackTrace();
					scan.close();
					log.close();
					return;
				}
				outputDir = new File(newDir);
			}
		}

		List<File> allVariants = getVariants(outputDir);
		for (File variant : allVariants) {

			System.out.println("Check variant : " + variant.getAbsolutePath() + "...");

			String outputVar = variant.getPath() + File.separator + "eclipse";
			if (isWindows)
				outputVar += ".exe";

			try {
				new File(outputVar).setExecutable(true);
				new File(outputVar).setWritable(true);
				new File(outputVar).setReadable(true);
			} catch (SecurityException exc) {
				exc.printStackTrace();
			}

			String error = "";
			ProcessBuilder builder = new ProcessBuilder(outputVar);
			Process process = null;
			try {
				process = builder.start(); // Start the executable
				allProcess.add(process);
			} catch (Exception e) {
				System.out.println("Error when we start the executable ... : " + e);
				e.printStackTrace();
				error = e.toString();
			}

			if (error.isEmpty()) { // If there is no exception when we start the
									// process
				System.out.println("An error occurs ?\n(Write it here, finish with ';' and press enter,\n"
						+ "else just press enter)");
				do {
					error += scan.nextLine();
				} while (!error.isEmpty() && !error.endsWith(";"));
			}

			if (!error.isEmpty()) { // If we write an error in the console
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
					is = new FileInputStream(propFile[0]); // There is only one
															// file
					Properties prop = new Properties();
					prop.load(is);

					percentage = prop.getProperty("random"); // See
																// VariantGenerator
					input = prop.getProperty("input");

				} catch (FileNotFoundException ex) {
					throw new FileNotFoundException("Does the \"VariantParams.properties\" file exist ?");
				} finally {
					if (is != null) {
						is.close();
					}
				}

				String[] splitDate = new Date(System.currentTimeMillis()).toString().split(" ");
				String dateToString = splitDate[2] + " " + splitDate[1] + " " + splitDate[5] + " " + splitDate[3];

				// %%% escape '%'
				log.write(String.format("(%s) %s (%s) with %s%%.\nError : %s\n\n", dateToString, variant.getName(),
						input, percentage, error.substring(0, error.length() - 1)));
				log.flush();

				System.out.println("\n");
			}

			if (process != null)
				process.destroy();
		}

		for (Process one_process : allProcess) {
			one_process.destroy();
		}

		log.close();
		scan.close();
		System.out.println("Variants checking process finished");
	}

	private static List<File> getVariants(File outputDir) {
		List<File> allVariants = new ArrayList<>();
		if (outputDir.listFiles() != null) {
			for (File dir : outputDir.listFiles()) {
				if (VariantsUtils.isEclipseDir(dir)) {
					allVariants.add(dir);
				} else {
					if (outputDir.listFiles() == null)
						return new ArrayList<>(0);
					else {
						List<File> allVariantsInside = getVariants(dir);
						if (allVariantsInside != null)
							allVariants.addAll(allVariantsInside);
					}
				}
			}
		}
		return allVariants;
	}

	public static boolean isWindows(String osname) {
		return osname.toLowerCase().contains("win");
	}

}
