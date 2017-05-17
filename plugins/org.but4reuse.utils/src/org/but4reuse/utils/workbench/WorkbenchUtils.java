package org.but4reuse.utils.workbench;

import java.io.File;
import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;

/**
 * Workbench Utils
 * 
 * @author jabier.martinez
 */
public class WorkbenchUtils {

	/**
	 * Get active editor of a given id
	 * 
	 * @param editorId
	 * @return the editor or null if not found
	 */
	public static IEditorPart getActiveEditorOfAGivenId(String editorId) {
		IEditorReference[] editorReferences = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getEditorReferences();
		for (int i = 0; i < editorReferences.length; i++) {
			IEditorReference iEditorReference = editorReferences[i];
			if (iEditorReference.getId().equals(editorId)) {
				IEditorPart editorPart = iEditorReference.getEditor(true);
				IWorkbenchPage iwpage = editorPart.getSite().getPage();
				if (iwpage.isPartVisible(editorPart)) {
					return iEditorReference.getEditor(true);
				}
			}
		}
		return null;
	}

	/**
	 * Try to force to show a view If not UI-thread, use
	 * Display.getDefault().asyncExec(new Runnable() {
	 * 
	 * @param viewId
	 * @return
	 */
	public static IViewPart forceShowView(String viewId) {
		try {
			IWorkbenchWindow ww = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			if (ww != null && ww.getActivePage() != null) {
				return ww.getActivePage().showView(viewId);
			}
			return null;
		} catch (PartInitException e) {
			return null;
		}
	}

	/**
	 * Refresh all workspace
	 */
	public static void refreshAllWorkspace() {
		try {
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Refresh resource
	 */
	public static void refreshIResource(IResource resource) {
		try {
			resource.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open a file in its default editor
	 * 
	 * @param file
	 */
	public static void openInEditor(IFile file) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(file.getName());
		try {
			page.openEditor(new FileEditorInput(file), desc.getId());
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	/**
	 * try to get the ifile from a file that is supposed to be in the workspace
	 * 
	 * @param ifile
	 * @return
	 */
	public static IFile getIFileFromFile(File file) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IPath location = Path.fromOSString(file.getAbsolutePath());
		IFile ifile = workspace.getRoot().getFileForLocation(location);
		return ifile;
	}

	/**
	 * get File from IResource
	 * 
	 * @param iresource
	 *            (including IFile)
	 * @return File
	 */
	public static File getFileFromIResource(IResource resource) {
		if (resource instanceof IProject) {
			// for some reason rawlocation in projects return null
			IProject project = (IProject) resource;
			if (!project.exists()) {
				return null;
			}
			return project.getLocation().makeAbsolute().toFile();
		}
		if (resource.getRawLocation() != null) {
			return resource.getRawLocation().makeAbsolute().toFile();
		}
		return null;
	}

	/**
	 * Open the editor at the location of the given marker.
	 * 
	 * @param marker
	 */
	public static void openInEditor(IMarker marker) {
		IWorkbenchPage page = getActivePage();
		try {
			IDE.openEditor(page, marker);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	public static IWorkbenchPage getActivePage() {
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		return win.getActivePage();
	}

	/**
	 * Convert IResource to URI
	 * 
	 * @param platform
	 *            uri, for example platform:/resource/projectName/myFile
	 * @return the iresource or null
	 */
	public static IResource getIResourceFromURI(URI uri) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		URI rootUri = root.getLocationURI();
		uri = rootUri.relativize(uri);
		IPath path = new Path(uri.getPath());
		path = path.removeFirstSegments(1);
		// if the segment count is 1 then it is an IProject
		if (path.segmentCount() == 1) {
			return root.getProject(path.segment(0));
		}
		return root.getFile(path);
	}
	
	public static URI getURIFromIResource(IResource resource) {
		String projectName = org.eclipse.emf.common.util.URI.encodeSegment(resource.getProject().getName(), false);
		String uriString = "platform:/resource/" + projectName;
		String[] pathSegments = resource.getProjectRelativePath().segments();
		for(String seg : pathSegments){
			uriString += '/' + org.eclipse.emf.common.util.URI.encodeSegment(seg, false);
		}
		return URI.create(uriString);
	}

	/**
	 * Error reporting
	 * 
	 * @param resource
	 * @param line
	 * @param msg
	 */
	public static void reportError(IResource resource, int line, String msg) {
		try {
			IMarker m = resource.createMarker(IMarker.PROBLEM);
			m.setAttribute(IMarker.LINE_NUMBER, line);
			m.setAttribute(IMarker.MESSAGE, msg);
			m.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
			m.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			forceShowView("org.eclipse.ui.views.ProblemView");
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

}
