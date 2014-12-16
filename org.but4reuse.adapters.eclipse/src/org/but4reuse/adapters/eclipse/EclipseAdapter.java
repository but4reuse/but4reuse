package org.but4reuse.adapters.eclipse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.jar.JarFile;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
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
	 * Cette méthode permet de définir si l'artefact est adaptable par le EclipseAdapter
	 */
	
	@Override
	public boolean isAdaptable(URI uri, IProgressMonitor monitor) {
		File file = FileUtils.getFile(uri);
		if (file.isDirectory()) {
			File eclipse = new File(file.getAbsolutePath()+"/eclipse.exe");
			if (eclipse.exists()){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

	/**
	 * Cette méthode permet de renvoyer une liste d'éléments atomiques
	 * 
	 * @param uri uri de l'artefact
	 * @param monitor
	 */
	@Override
	public List<IElement> adapt(URI uri, IProgressMonitor monitor) {
		List<IElement> elements = new ArrayList<IElement>();
		File file = FileUtils.getFile(uri);
		if(file != null && file.exists() && file.isDirectory() ){
			elements.addAll(adaptFolder(file.getAbsolutePath()+"/dropins"));
			elements.addAll(adaptFolder(file.getAbsolutePath()+"/plugins"));
			//implémenter l'unicité des éléments de la liste!
			}
		return elements;
	}

	private List<IElement> adaptFolder(String uri) {
		List<IElement> elements = new ArrayList<IElement>();
		File file = new File(uri);
		File[] fichiers = file.listFiles();
		for(int i = 0; i<fichiers.length; i++){
			if(fichiers[i].isDirectory()){
				try{
					IElement e = new PluginElement();
					InputStream ips = new FileInputStream(uri);
					InputStreamReader ipsr = new InputStreamReader(ips);
					BufferedReader br = new BufferedReader(ipsr);
					String ligne;
					while ((ligne = br.readLine()) != null){
						if(ligne.contains("Bundle-SymbolicName: ")){
							ligne = ligne.substring(ligne.indexOf("Bundle-SymbolicName: ") + 21);
							((PluginElement) e).setPluginSymbName(ligne);
						}
						if(ligne.contains("Require-Bundle: ")){
							ligne = ligne.substring(ligne.indexOf("Require-Bundle: ") + 16);
							((PluginElement) e).setRequire_bundle(ligne);
						}
						//voir ce que veux dire %pluginName
						if(ligne.contains("Bundle-Name: ")){
							ligne = ligne.substring(ligne.indexOf("Bundle-Name: ") + 13);
							((PluginElement) e).setPluginName(ligne);
						}
					}
						br.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			if(fichiers[i].getPath().endsWith(".jar")){
				JarFile jar;
				try {
					IElement e = new PluginElement();
					jar = new JarFile(uri);
					((PluginElement) e).setPluginSymbName(jar.getManifest().getMainAttributes().getValue("Bundle-SymbolicName"));
					((PluginElement) e).setPluginName(jar.getManifest().getMainAttributes().getValue("Bundle-Name"));
					((PluginElement) e).setRequire_bundle(jar.getManifest().getMainAttributes().getValue("Require-Bundle"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		return elements;
	}
	
	
	
	
	@Override
	public void construct(URI uri, List<IElement> elements,
			IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		
	}


	
	//TODO à faire lorsqu'on aura compris à quoi ça sert.
//	@Override
//	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {
//		for (IElement element : elements) {
//			// check user cancel for each element
//			if (!monitor.isCanceled()) {
//				// provide user info
//				monitor.subTask(element.getText());
//				if (element instanceof PluginElement) {
//					PluginElement fileElement = (PluginElement) element;
//					try {
//						// Create parent folders structure
//						URI newDirectoryURI = uri.resolve(fileElement.getRelativeURI());
//						File destinationFile = FileUtils.getFile(newDirectoryURI);
//						if (destinationFile!=null && !destinationFile.getParentFile().exists()) {
//							destinationFile.getParentFile().mkdirs();
//						}
//						if (destinationFile!=null && !destinationFile.exists()) {
//							// Copy the content. In the case of a folder, its
//							// content is not copied
//							File file = FileUtils.getFile(fileElement.getUri());
//							Files.copy(file.toPath(), destinationFile.toPath(),
//									StandardCopyOption.REPLACE_EXISTING);
//						}
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//			monitor.worked(1);
//		}
//	}

}
