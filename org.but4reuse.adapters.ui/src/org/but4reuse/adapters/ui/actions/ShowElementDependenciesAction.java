package org.but4reuse.adapters.ui.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.ui.AdaptersSelectionDialog;
import org.but4reuse.adapters.ui.views.PluginBouchon;
import org.but4reuse.adapters.ui.views.PluginContentProvider;
import org.but4reuse.adapters.ui.views.PluginLabelProvider;
import org.but4reuse.artefactmodel.Artefact;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;

/** 
 * ShowElementDependenciesAction
 * @author Selma Sadouk
 * @author Julia Wisniewski
 */
public class ShowElementDependenciesAction implements IObjectActionDelegate {

	Artefact artefact = null;
	List<IAdapter> adap;
	List<String> text = new ArrayList<String>();

	@Override
	public void run(IAction action) {
		artefact = null;
		if (selection instanceof IStructuredSelection) {
			for (Object art : ((IStructuredSelection) selection).toArray()) {
				if (art instanceof Artefact) {
					artefact = ((Artefact) art);

					// Adapter selection by user
					adap = AdaptersSelectionDialog.show("Show Element Dependencies", artefact);

					if (!adap.isEmpty()) {


						// Launch Progress dialog
						ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(Display.getCurrent()
								.getActiveShell());

						try {
							progressDialog.run(true, true, new IRunnableWithProgress() {
								@Override
								public void run(IProgressMonitor monitor) throws InvocationTargetException,
								InterruptedException {

									int totalWork = 1;
									monitor.beginTask("Calculating dependencies of " + artefact.getArtefactURI(), totalWork);

									text.clear();
									for (IAdapter adapter : adap) {
										/*
										List<IElement> elements = AdaptersHelper.getElements(artefact, adapter);
										for (IElement element : elements) {
											//text.add(element.getText());
											//element.getDependencies(); ??
										}
										*/
										
									String[] elems = {"test1", "test2"};

										
										
									}
									monitor.worked(1);
									monitor.done();
								}
							});
/*
							String sText = "";
							for (String t : text) {
								t = t.replaceAll("\n", " ").replaceAll("\r", "");
								sText = sText + t + "\n";
							}
*/
							String name = artefact.getName();
							if (name == null || name.length() == 0) {
								name = artefact.getArtefactURI();
							}

							/*
							ScrollableMessageDialog dialog = new ScrollableMessageDialog(Display.getCurrent()
									.getActiveShell(), name, text.size() + " Elements", sText);
							dialog.open();
							 */

							//PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("tryEclipseZest.view");

							
							Display.getDefault().syncExec(
									new Runnable(){
										public void run(){

											Display d = Display.getCurrent();
											Shell shell = new Shell();
											shell.setText("Visualization");
											shell.setLayout(new FillLayout(SWT.VERTICAL));
											shell.setSize(400,400);
											/*
											final Graph dependenciesGraph = new Graph(shell, SWT.NONE);
											final GraphNode n1 = new GraphNode(dependenciesGraph,SWT.NONE);
											final GraphNode n2 = new GraphNode(dependenciesGraph,SWT.NONE);
											final GraphConnection c = new GraphConnection(dependenciesGraph,SWT.NONE,n1,n2);
											dependenciesGraph.setSelection(new GraphItem[]{n1,n2,c});
											*/
											GraphViewer viewer = new GraphViewer(shell,SWT.NONE);
											
											
											// ===================================
											PluginContentProvider contentProvider = new PluginContentProvider();
											PluginLabelProvider labelProvider = new PluginLabelProvider();
											viewer.setContentProvider(contentProvider);
											viewer.setLabelProvider(labelProvider);
											
											List<PluginBouchon> input = new ArrayList<PluginBouchon>();
											PluginBouchon p1 = new PluginBouchon("plugin1");
											PluginBouchon p2 = new PluginBouchon("plugin2");
											input.add(p1); input.add(p2);
											viewer.setInput(input); // -> Ajouter ici dependencies but4reuse
											// ===================================
											
											/*
											viewer.setControl(dependenciesGraph);
											n1.setData("1");
											n2.setData("2");
											*/

											//GraphNode noeud = new GraphNode(dependenciesGraph, SWT.NONE);
											//noeud.setText("Un noeud");
											//dependenciesGraph.update();
											shell.open();
											while (!shell.isDisposed()) {
												while (!d.readAndDispatch()) {
													d.sleep();
												}
											}
										} 
									}
									);
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	ISelection selection;

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;

	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

}
