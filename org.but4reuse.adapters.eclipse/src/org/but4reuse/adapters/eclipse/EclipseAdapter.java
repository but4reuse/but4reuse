package org.but4reuse.adapters.eclipse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IDependencyObject;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.eclipse.plugin_infos_extractor.utils.PluginInfosExtractor;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

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
	 * Provides the atoms (plugins) this distribution is made of
	 * 
	 * @param uri URI of the distribution
	 * @param monitor 
	 */
	@Override
	public List<IElement> adapt(URI uri, IProgressMonitor monitor) {
		List<IElement> elements = new ArrayList<IElement>();
		File file = FileUtils.getFile(uri);
		if(file != null && file.exists() && file.isDirectory() ){
			elements.addAll(adaptFolder(file.getAbsolutePath()+"/dropins", monitor));
			elements.addAll(adaptFolder(file.getAbsolutePath()+"/plugins", monitor));
			//TODO implémenter l'unicité des éléments de la liste!
		}
//		System.out.println("taille des dépendences "+elements.get(0).getDependencies().size());
//		List<IDependencyObject> map = elements.get(0).getDependencies().get(AbstractElement.MAIN_DEPENDENCY_ID);
//		ArrayList<PluginElement> req = ((PluginElement)elements.get(0)).getRequire_Bundles();
//		boolean ok = true;
//		for(IDependencyObject o : map){
//			if(!req.contains(o)){
//				ok=false;
//				break;
//			} 
//		}
//		if( ok ){
//			for(PluginElement p : req){
//				if(!map.contains(p)){
//					ok=false;
//					break;
//				}
//			}
//		}
//		System.out.println(ok);
		return elements;
	}

	/**
	 * Searches for plugins in the given folder
	 * @param uri URI of an Eclipse folder
	 */
	private List<IElement> adaptFolder(String uri, IProgressMonitor monitor) {
		List<IElement> elements = new ArrayList<IElement>();
		File file = new File(uri);
		File[] fichiers = file.listFiles();
		
		for (int i = 0; i<fichiers.length; i++) {
			
//			System.out.println("analyse de l'élément "+fichiers[i].getName());
			
			if (fichiers[i].isDirectory()) {
				
//				System.out.println("plugin sous forme de dossier : "+fichiers[i].getAbsolutePath());
				
				try {
					
					elements.add(PluginInfosExtractor.getPluginInfosFromManifest(fichiers[i].getAbsolutePath()+
							"/META-INF/MANIFEST.MF"));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else
			if(fichiers[i].getPath().endsWith(".jar")){
				try {
					elements.add(PluginInfosExtractor.getPluginInfosFromJar(fichiers[i].getAbsolutePath()));
				} catch (IOException e) {
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
