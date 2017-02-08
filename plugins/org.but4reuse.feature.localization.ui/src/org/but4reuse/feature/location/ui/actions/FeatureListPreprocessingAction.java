package org.but4reuse.feature.location.ui.actions;

import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.featurelist.FeatureListFactory;
import org.but4reuse.featurelist.helpers.FeatureListHelper;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Feature List Preprocessing
 * 
 * @author jabier.martinez
 */
public class FeatureListPreprocessingAction implements IObjectActionDelegate {

	FeatureList featureList;
	List<IAdapter> adapters;
	ArtefactModel artefactModel;

	@Override
	public void run(IAction action) {
		if (selection instanceof IStructuredSelection) {
			Object featureListObject = ((IStructuredSelection) selection).getFirstElement();
			if (featureListObject instanceof FeatureList) {
				featureList = ((FeatureList) featureListObject);
				artefactModel = FeatureListHelper.getArtefactModel(featureList);

				Display display = Display.getCurrent();
				Shell shell = new Shell(display);
				shell.setLayout(new GridLayout(1, true));
				shell.setText("Feature List Preprocessing");

				final Button button0 = new Button(shell, SWT.PUSH);
				button0.setText("Add Core Feature if it does not exist");
				button0.addSelectionListener(new SelectionListener() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						for (Feature f : featureList.getOwnedFeatures()) {
							if (FeatureListHelper.isCoreFeature(artefactModel, f)) {
								button0.setEnabled(false);
								return;
							}
						}
						Feature core = FeatureListFactory.eINSTANCE.createFeature();
						core.getImplementedInArtefacts().addAll(artefactModel.getOwnedArtefacts());
						core.setName("Core");
						core.setId("Core");
						// add it at the beginning
						featureList.getOwnedFeatures().add(0, core);
						button0.setEnabled(false);
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
					}
				});

				final Button button1 = new Button(shell, SWT.PUSH);
				button1.setText("Add Feature Negations excluding Features present in all artefacts");
				button1.addSelectionListener(new SelectionListener() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						// Add negations
						List<Feature> negations = FeatureListHelper.getFeatureNegations(featureList.getOwnedFeatures(),
								artefactModel);
						featureList.getOwnedFeatures().addAll(negations);
						button1.setEnabled(false);
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
					}
				});

				final Button button2 = new Button(shell, SWT.PUSH);
				button2.setText("Add 2-wise Feature Interactions existing in the artefacts");
				button2.addSelectionListener(new SelectionListener() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						// Add 2-wise feature interactions
						List<Feature> twoWise = FeatureListHelper.get2WiseFeatureInteractions(
								featureList.getOwnedFeatures(), artefactModel);
						featureList.getOwnedFeatures().addAll(twoWise);
						button2.setEnabled(false);
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
					}
				});

				final Button button_2 = new Button(shell, SWT.PUSH);
				button_2.setText("Add 2-wise and 3-wise Feature Interactions existing in the artefacts");
				button_2.addSelectionListener(new SelectionListener() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						// Add 2-wise feature interactions
						List<Feature> twoWise = FeatureListHelper.get2WiseFeatureInteractions(
								featureList.getOwnedFeatures(), artefactModel);
						// Add 3-wise feature interactions
						List<Feature> threeWise = FeatureListHelper.get3WiseFeatureInteractions(
								featureList.getOwnedFeatures(), artefactModel);
						featureList.getOwnedFeatures().addAll(twoWise);
						featureList.getOwnedFeatures().addAll(threeWise);
						button2.setEnabled(false);
						button_2.setEnabled(false);
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
					}
				});

				final Button button_w = new Button(shell, SWT.PUSH);
				button_w.setText("Add 2-wise, 3-wise and 4-wise Feature Interactions existing in the artefacts");
				button_w.addSelectionListener(new SelectionListener() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						// Add 2-wise feature interactions
						List<Feature> twoWise = FeatureListHelper.get2WiseFeatureInteractions(
								featureList.getOwnedFeatures(), artefactModel);
						// Add 3-wise feature interactions
						List<Feature> threeWise = FeatureListHelper.get3WiseFeatureInteractions(
								featureList.getOwnedFeatures(), artefactModel);
						// Add 3-wise feature interactions
						List<Feature> fourWise = FeatureListHelper.get4WiseFeatureInteractions(
								featureList.getOwnedFeatures(), artefactModel);
						featureList.getOwnedFeatures().addAll(twoWise);
						featureList.getOwnedFeatures().addAll(threeWise);
						featureList.getOwnedFeatures().addAll(fourWise);
						button2.setEnabled(false);
						button_2.setEnabled(false);
						button_w.setEnabled(false);
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
					}
				});

				final Button button3 = new Button(shell, SWT.PUSH);
				button3.setText("Merge Features implemented exactly in the same artefacts");
				button3.addSelectionListener(new SelectionListener() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						// Merge
						FeatureListHelper.mergeFeaturesImplementedInTheSameArtefacts(featureList, artefactModel);
						button3.setEnabled(false);
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
					}
				});

				shell.pack();
				shell.open();
				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
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
