package org.but4reuse.visualisation.visualiser.featurelist;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.visualisation.IVisualisation;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * FeatureSpecificHeatMapVisualisation
 * 
 * @author jabier.martinez
 */
public class FeatureSpecificHeatMapVisualisation implements IVisualisation {
	String[][] matrix;

	@Override
	public void prepare(FeatureList featureList, AdaptedModel adaptedModel, Object extra, IProgressMonitor monitor) {
		matrix = null;
		if (featureList != null) {
			int featuresSize = featureList.getOwnedFeatures().size() + 1;
			int blocksSize = adaptedModel.getOwnedBlocks().size() + 1;
			if (featuresSize > 1 && blocksSize > 1) {
				// initialize matrix sizes
				matrix = new String[featuresSize][];
				for (int i = 0; i < featuresSize; i++) {
					matrix[i] = new String[blocksSize];
				}
				// first row with block names
				for (int i = 1; i < blocksSize; i++) {
					matrix[0][i] = adaptedModel.getOwnedBlocks().get(i - 1).getName();
				}
				// first column with feature names
				for (int i = 1; i < blocksSize; i++) {
					matrix[i][0] = featureList.getOwnedFeatures().get(i - 1).getName();
				}
				// calculate feature-specific heuristic
				for (int r = 1; r < featuresSize; r++) {
					for (int c = 1; c < blocksSize; c++) {
						matrix[r][c] = new Double(percentageOfBlockInFeature(adaptedModel.getOwnedBlocks().get(c - 1),
								featureList.getOwnedFeatures().get(r - 1))).toString();
					}
				}
			}
		}

	}

	@Override
	public void show() {
		// no featurelist then no visualisation
		if (matrix != null) {
			// asyncExec to avoid SWT invalid thread access
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					Display display = Display.getDefault();
					Shell shell = new Shell(display);
					shell.setLayout(new FillLayout());

					Table table = new Table(shell, SWT.BORDER);
					TableColumn[] tableColumns = new TableColumn[matrix[0].length];
					for (int c = 0; c < matrix[0].length; c++) {
						TableColumn column = new TableColumn(table, SWT.NONE);
						tableColumns[c] = column;
					}

					for (int r = 0; r < matrix.length; r++) {
						String[] cells = matrix[r];
						TableItem item = new TableItem(table, SWT.NONE);
						String[] cells2 = new String[cells.length];

						// preparing the text of each row, truncating decimals
						for (int ce = 0; ce < cells.length; ce++) {
							String to = cells[ce];
							if (r != 0 && ce > 0 && to != null && to.length() > 4) {
								to = to.substring(0, 4);
							}
							cells2[ce] = to;
						}

						// set the text of each row
						item.setText(cells2);
						
						// calculating gradient color
						if (r != 0) {
							for (int ce = 1; ce < cells.length; ce++) {
								if (cells[ce] != null) {
									double value = Double.parseDouble(cells[ce]);
									item.setBackground(ce, getGradientColor(value));
								}
							}
						}
					}

					// packs
					for (int c = 0; c < matrix[0].length; c++) {
						tableColumns[c].pack();
					}

					shell.pack();
					shell.setText("Feature-Specific heuristic heatmap");
					shell.open();
				}
			});
		}
	}

	/**
	 * Feature-Specific heuristic
	 * @param block
	 * @param feature
	 * @return
	 */
	public static double percentageOfBlockInFeature(Block block, Feature feature) {
		List<Artefact> artefacts = feature.getImplementedInArtefacts();
		List<Artefact> foundArtefacts = new ArrayList<Artefact>();
		List<BlockElement> blockElements = block.getOwnedBlockElements();
		for (BlockElement be : blockElements) {
			for (ElementWrapper ew : be.getElementWrappers()) {
				AdaptedArtefact aa = (AdaptedArtefact) ew.eContainer();
				for (Artefact a : artefacts) {
					if (aa.getArtefact().equals(a)) {
						if (!foundArtefacts.contains(a)) {
							foundArtefacts.add(a);
						}
					}
				}
			}
		}
		return new Double(foundArtefacts.size()) / new Double(artefacts.size());
	}

	private Color getGradientColor(double percent) {
		// Color color1 = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
		// Color color2 = Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
		// Color color3 = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
		Color color1 = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
		Color color2 = new Color(Display.getCurrent(), 221, 232, 207);
		Color color3 = new Color(Display.getCurrent(), 88, 171, 45);
		if (percent < 0.5) {
			percent = percent / 0.5;
			Double resultRed = color1.getRed() + percent * (color2.getRed() - color1.getRed());
			Double resultGreen = color1.getGreen() + percent * (color2.getGreen() - color1.getGreen());
			Double resultBlue = color1.getBlue() + percent * (color2.getBlue() - color1.getBlue());
			Color newColor = new Color(Display.getCurrent(), resultRed.intValue(), resultGreen.intValue(),
					resultBlue.intValue());
			return newColor;
		} else {
			percent = (percent - 0.5) / 0.5;
			Double resultRed = color2.getRed() + percent * (color3.getRed() - color2.getRed());
			Double resultGreen = color2.getGreen() + percent * (color3.getGreen() - color2.getGreen());
			Double resultBlue = color2.getBlue() + percent * (color3.getBlue() - color2.getBlue());
			Color newColor = new Color(Display.getCurrent(), resultRed.intValue(), resultGreen.intValue(),
					resultBlue.intValue());
			return newColor;
		}
	}

}
