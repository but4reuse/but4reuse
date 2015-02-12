package org.but4reuse.adapters.eclipse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.eclipse.plugin_infos_extractor.utils.DependenciesBuilder;
import org.but4reuse.adapters.eclipse.plugin_infos_extractor.utils.PluginInfosExtractor;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Eclipse adapter
 * 
 * @author Jason CHUMMUN
 * @author Diana MALABARD
 */
public class EclipseAdapter implements IAdapter {

	/**
	 * Cette méthode permet de définir si l'artefact est adaptable par le
	 * EclipseAdapter
	 */

	@Override
	public boolean isAdaptable(URI uri, IProgressMonitor monitor) {
		File file = FileUtils.getFile(uri);
		if (file.isDirectory()) {
			File eclipse = new File(file.getAbsolutePath() + "/eclipse.exe");
			File eclipsemac = new File(file.getAbsolutePath() + "/eclipse.app");
			if (eclipse.exists() || eclipsemac.exists()) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * Provides the atoms (plugins) this distribution is made of
	 * 
	 * @param uri
	 *            URI of the distribution
	 * @param monitor
	 */
	@Override
	public List<IElement> adapt(URI uri, IProgressMonitor monitor) {
		List<IElement> elements = new ArrayList<IElement>();
		File file = FileUtils.getFile(uri);
		if (file != null && file.exists() && file.isDirectory()) {
			elements.addAll(adaptFolder(file.getAbsolutePath() + "/dropins",
					monitor));
			elements.addAll(adaptFolder(file.getAbsolutePath() + "/plugins",
					monitor));
			// For each element, build the dependencies map, depending
			// on the plugins installed in the considered distribution
			// and the values retrieved in its RequiredBundle field
			for (IElement elem : elements) {
				DependenciesBuilder builder = new DependenciesBuilder(
						(PluginElement) elem, elements);
				builder.run();
			}
			// Test
//			for (IElement elem : elements) {
//				PluginElement plugin = (PluginElement) elem;
//				if (plugin.getDependencies().get(
//						AbstractElement.MAIN_DEPENDENCY_ID) == null) {
//					System.out.println("Le plugin "
//							+ plugin.getPluginSymbName()
//							+ " n'a aucune dépendance.");
//				} else {
//					System.out.println("Le plugin "
//							+ plugin.getPluginSymbName()
//							+ " a "
//							+ plugin.getDependencies()
//									.get(AbstractElement.MAIN_DEPENDENCY_ID)
//									.size()+" dépendances.");
//				}
//			}
		}
		return elements;
	}

	/**
	 * Searches for plugins in the given folder
	 * 
	 * @param uri
	 *            URI of an Eclipse folder
	 */
	private List<IElement> adaptFolder(String uri, IProgressMonitor monitor) {
		List<IElement> elements = new ArrayList<IElement>();
		File file = new File(uri);
		File[] fichiers = file.listFiles();

		for (int i = 0; i < fichiers.length; i++) {

			// System.out.println("analyse de l'élément "+fichiers[i].getName());

			if (fichiers[i].isDirectory()) {

				// System.out.println("plugin sous forme de dossier : "+fichiers[i].getAbsolutePath());

				try {

					elements.add(PluginInfosExtractor
							.getPluginInfosFromManifest(fichiers[i]
									.getAbsolutePath()
									+ "/META-INF/MANIFEST.MF"));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (fichiers[i].getPath().endsWith(".jar")) {
				try {
					elements.add(PluginInfosExtractor
							.getPluginInfosFromJar(fichiers[i]
									.getAbsolutePath()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		return elements;
	}

	//TODO à faire lorsqu'on aura compris à quoi ça sert.
	@Override
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {
		 System.out.println("Enter construct");
		   
		    Path dir = FileSystems.getDefault().getPath("/UPMC/M2/GPSTL/fevrier/runtime-EclipseApplication/test/plugins/");
		    Path abs=dir.toAbsolutePath();
		    System.out.println(dir.toString());
		    File dest = new File("C:/UPMC/M2/GPSTL/fevrier/runtime-EclipseApplication/test/plugins/");
		    
			for (IElement element : elements) {
			    System.out.println("************* ENTRE element");

				if (!monitor.isCanceled()) {
					
					monitor.subTask(element.getText());
					if (element instanceof PluginElement) {
						System.out.println("*********PluginELEMENT     **********");

						PluginElement fileElement = (PluginElement) element;
						
					try {
							 // if (dir.getParent() != null) {
									
								//	File file = FileUtils.getFile(fileElement.getAbsolutePath());
					
									String pluginAddr = fileElement.getAbsolutePath();
									System.out.println("plugin : " +  pluginAddr);
								//File f = new File("/UPMC/M2/GPSTL/fevrier/runtime-EclipseApplication/test/plugins/" +fileElement.getPluginSymbName());
									//File source = FileUtils.getFile(URI.create("C:/UPMC/M2/GPSTL/Eclipse versions/eclipse_juno/plugins/org.eclipse.e4.ui.workbench.renderers.swt_0.10.3.v20130124-170312.jar"));
									//copyFile(source, f);
									
									FileUtils.downloadFileFromURL(new URL(pluginAddr), dest);
								
									
							//	Files.copy(file.toPath(), dir,StandardCopyOption.REPLACE_EXISTING);
								//  System.out.println("Copy  *************"+file.toString()+"----->"+dir);

							//	}
								//else{
								//new File("C:\\UPMC\\M2\\GPSTL\\runtime-EclipseApplication\\test3\\plugins").mkdirs();
								//System.out.println("*********DIRECTORY CREATED**********");
								//}

						
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				monitor.worked(1);
			}
	}
	
	private static void copyFile(File source, File dest)throws IOException{
		InputStream input = null;
		OutputStream output = null;
		try{
			input = new FileInputStream(source);
			output = new FileOutputStream(dest);
			byte[] buf = new byte[1024];
			int bytesRead; 
			while((bytesRead = input.read(buf)) > 0){
				output.write(buf,0,bytesRead);
				
			}
		}finally{
				input.close();
				output.close();
		}
	}
}
