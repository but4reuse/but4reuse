package org.but4reuse.wordclouds.visualisation;

import java.io.File;
import java.util.ArrayList;

import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.visualisation.helpers.VisualisationsHelper;
import org.but4reuse.wordclouds.activator.Activator;
import org.but4reuse.wordclouds.preferences.WordCloudPreferences;
import org.but4reuse.wordclouds.util.AutomaticRenaming;
import org.but4reuse.wordclouds.util.WordCloudListener;
import org.but4reuse.wordclouds.util.WordCloudUtil;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;

/**
 * @author Arthur, aarkoub This class is an eclipse view.
 */

public class WordCloudView extends ViewPart {

	/**
	 * The current WorldCloudVis.
	 */
	static private WordCloudView singleton = null;

	/**
	 * The combo control in the view.
	 */
	private Combo combo;

	/**
	 * The list control in the view.
	 */
	private List list;

	/**
	 * The Composite control where word clouds are drawn.
	 */
	private Composite cmp;

	/**
	 * The composite control where word clouds are draw using inverse document
	 * frequency
	 */
	private Composite cmpTFIDF;

	/**
	 * The text control in the view
	 */
	private Text text;

	/**
	 * The button named "rename" in the view
	 */
	private Button accept;

	/**
	 * The button named "Rename All Auto"
	 */
	private Button renameAll;

	/**
	 * The button named "Rename Current Auto"
	 */
	private Button renameOne;

	/**
	 * TabFolder from the view.
	 */
	private TabFolder tabFolder;

	/**
	 * Last index used to get the clouds
	 */
	private static int lastIndex = 0;

	/**
	 * Default constructor.
	 */
	public WordCloudView() {

		super();
		singleton = this;
	}

	/**
	 * This method returns the value of singleton attribute.
	 * 
	 * @return The singleton
	 */
	public static WordCloudView getSingleton() {
		return singleton;
	}

	/**
	 * This method returns the combo control from the view.
	 * 
	 * @return The combo control
	 */
	public Combo getCombo() {
		return combo;
	}

	/**
	 * This method returns the list control from the view.
	 * 
	 * @return The list control
	 */
	public List getList() {
		return list;
	}

	/**
	 * This method returns the {@link Composite} control from the view.
	 * 
	 * @return the canvas control
	 */
	public Composite getSComposite() {
		return cmp;
	}

	/**
	 * This method returns the canvas control from the view. It's the canvas
	 * where the IDF is used.
	 * 
	 * @return the {@link Composite} control where is drawn the word cloud using
	 *         the IDF
	 */
	public Composite getSCompositeTFIDF() {
		return cmpTFIDF;
	}

	/**
	 * The method returns the text control from the view
	 * 
	 * @return the text control
	 */
	public Text getText() {
		return text;
	}

	/**
	 * It will return the tabFolder which is in the view.
	 * 
	 * @return the tabFolder
	 */
	public TabFolder getTabFolder() {
		return tabFolder;
	}

	/**
	 * This method will update the singleton. It will set the selected item at
	 * index.\n The method will call WordCloudAction.getClouds() to get cloud at
	 * the index "index" and fill the list control with strings contained in the
	 * cloud.
	 * 
	 * @param index
	 *            Index must be the index of an item from combo control.
	 * @param redraw
	 *            To indicate if you want clear the canvas content.
	 */
	public static void update(int index, boolean redraw) {

		// If features aren't identified, it isn't necessary to update the view
		if (singleton == null || AdaptedModelManager.getAdaptedModel() == null)
			return;
		/*
		 * We clear the content for every control in the view.
		 */
		singleton.getCombo().clearSelection();
		singleton.getCombo().removeAll();
		singleton.getList().removeAll();
		singleton.getText().setText("");

		for (Block b : AdaptedModelManager.getAdaptedModel().getOwnedBlocks())
			singleton.getCombo().add(b.getName());
		singleton.getCombo().select(index);

		Cloud c = null;
		lastIndex = index;
		if (WordCloudView.getSingleton().getTabFolder().getSelectionIndex() == 1)
			c = WordCloudVisualisation.getCloudsTFIDF().get(index);
		else
			c = WordCloudVisualisation.getClouds().get(index);

		for (Tag t : c.tags())
			singleton.getList().add(t.getName() + " - " + String.format("%.2f", t.getNormScore()));

		WordCloudUtil.drawWordCloud(singleton.getSComposite(), c);
		WordCloudUtil.drawWordCloud(singleton.getSCompositeTFIDF(), c);
		addWordCloudListeners(singleton.getSComposite(), index);
		addWordCloudListeners(singleton.getSCompositeTFIDF(), index);
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO use grid layout data with span etc. no absolute positions
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginTop = 5;
		gridLayout.marginRight = 5;
		gridLayout.marginLeft = 5;
		gridLayout.horizontalSpacing = 10;
		parent.setLayout(gridLayout);

		Composite c1 = new Composite(parent, SWT.NONE);
		c1.setLayout(null);
		GridData gd_c1 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_c1.widthHint = 170;
		gd_c1.heightHint = 431;
		c1.setLayoutData(gd_c1);
		c1.setBounds(0, 0, 101, 135);

		Label lblWordList = new Label(c1, SWT.NORMAL | SWT.CENTER);
		lblWordList.setBounds(41, 4, 89, 24);
		lblWordList.setFont(new Font(Display.getCurrent(), "Sylfaen", 12, SWT.BOLD));
		lblWordList.setText(" Word List ");

		combo = new Combo(c1, SWT.NONE | SWT.READ_ONLY);
		combo.setBounds(5, 34, 157, 23);

		list = new List(c1, SWT.BORDER | SWT.V_SCROLL);
		list.setBounds(5, 64, 157, 304);

		renameAll = new Button(c1, SWT.NONE);
		renameAll.setLocation(5, 372);
		renameAll.setSize(157, 25);
		renameAll.setText("Auto Rename All");

		renameOne = new Button(c1, SWT.NONE);
		renameOne.setLocation(5, 403);
		renameOne.setSize(157, 25);
		renameOne.setText("Auto Rename Current");

		tabFolder = new TabFolder(parent, SWT.NONE);
		GridData gd_tabFolder = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
		tabFolder.setLayoutData(gd_tabFolder);

		TabItem tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("Word Cloud");

		final ScrolledComposite sCmp = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		tbtmNewItem.setControl(sCmp);

		TabItem tbtmNewItem_1 = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem_1.setText("Word Cloud TF-IDF");

		final ScrolledComposite sCmpIDF = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		tbtmNewItem_1.setControl(sCmpIDF);

		cmp = new Composite(sCmp, SWT.NORMAL);
		cmpTFIDF = new Composite(sCmpIDF, SWT.NORMAL);
		cmp.setBounds(0, 0, 1000, 2000);
		cmpTFIDF.setBounds(0, 0, 1000, 4000);
		sCmp.getVerticalBar().setIncrement(200);
		sCmp.getHorizontalBar().setIncrement(25);
		sCmpIDF.getVerticalBar().setIncrement(200);
		sCmpIDF.getHorizontalBar().setIncrement(200);
		sCmp.setContent(cmp);
		sCmpIDF.setContent(cmpTFIDF);
		Composite c2 = new Composite(parent, SWT.NONE);
		c2.setBounds(0, 0, 64, 64);
		c2.setLayout(new GridLayout(1, false));

		text = new Text(c2, SWT.BORDER);
		GridData gd_text = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_text.heightHint = 16;
		gd_text.widthHint = 145;
		text.setLayoutData(gd_text);
		text.setBounds(0, 0, 76, 21);

		accept = new Button(c2, SWT.NONE);
		GridData gd_accept = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_accept.heightHint = 25;
		gd_accept.widthHint = 157;
		accept.setLayoutData(gd_accept);
		accept.setBounds(0, 0, 75, 25);
		accept.setText("Rename");

		Composite c3 = new Composite(parent, SWT.NONE);
		GridData gd_c3 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_c3.widthHint = 509;
		c3.setLayoutData(gd_c3);

		Button newWin = new Button(c3, SWT.NONE);
		newWin.setBounds(171, 0, 150, 25);
		newWin.setText("Show in New Window");
		newWin.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				final Shell win = new Shell(Display.getCurrent().getActiveShell(), SWT.TITLE | SWT.CLOSE);
				Cloud c = null;
				int i = WordCloudView.getSingleton().getCombo().getSelectionIndex();
				String name = "Word Cloud " + AdaptedModelManager.getAdaptedModel().getOwnedBlocks().get(i).getName();
				if (WordCloudView.getSingleton().getTabFolder().getSelectionIndex() == 0) {
					win.setSize(1000, 2000);
					c = WordCloudVisualisation.getClouds().get(i);

				} else {
					win.setSize(1000, 4000);
					c = WordCloudVisualisation.getCloudsTFIDF().get(i);
					name += " TFIDF";
				}

				ScrolledComposite cmp = new ScrolledComposite(win, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
				cmp.setBounds(5, 5, win.getSize().x - 10, win.getSize().y - 10);
				Composite comp = new Composite(cmp, SWT.NORMAL);
				comp.setBounds(0, 0, cmp.getBounds().width, cmp.getBounds().height);

				win.setText(name);
				win.open();
				win.update();

				WordCloudUtil.drawWordCloud(comp, c);
				addWordCloudListeners(comp, i);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		Button saveImage = new Button(c3, SWT.NONE);
		saveImage.setBounds(350, 0, 150, 25);
		saveImage.setText("Save as image");
		saveImage.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				Cloud cloud = null;

				if (WordCloudView.getSingleton().getTabFolder().getSelectionIndex() == 1)
					cloud = WordCloudVisualisation.getCloudsTFIDF().get(lastIndex);
				else
					cloud = WordCloudVisualisation.getClouds().get(lastIndex);

				// To select the directory
				DirectoryDialog dirDialog = new DirectoryDialog(Display.getCurrent().getActiveShell());
				dirDialog.setText("Select output directory");

				String selectedDir = dirDialog.open();
				if (selectedDir != null) {
					String path = selectedDir + File.separator + singleton.getCombo().getText();
					if (WordCloudView.getSingleton().getTabFolder().getSelectionIndex() == 1) {
						path += "_tfidf";
					}
					path += ".png";

					// To save the image
					WordCloudUtil.saveCloud(cloud, path);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});

		/*
		 * When you will click on this button you will rename each block with
		 * the most often string saw in the block's word cloud.
		 */
		renameAll.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.Selection:
					// Check if frequency or tfidf is selected
					java.util.List<Cloud> clouds = null;
					if (WordCloudView.getSingleton().getTabFolder().getSelectionIndex() == 0) {
						clouds = WordCloudVisualisation.getClouds();
					} else {
						clouds = WordCloudVisualisation.getCloudsTFIDF();
					}
					// Launch the algorithm
					int kWords = Activator.getDefault().getPreferenceStore()
							.getInt(WordCloudPreferences.AUTORENAME_NB_WORDS);
					java.util.List<String> names = AutomaticRenaming.renameAll(clouds, kWords);
					// Update the names of the blocks
					int i = 0;
					boolean somethingChanged = false;
					for (Block b : AdaptedModelManager.getAdaptedModel().getOwnedBlocks()) {
						String newName = names.get(i);
						boolean keepPreviousName = Activator.getDefault().getPreferenceStore()
								.getBoolean(WordCloudPreferences.AUTORENAME_KEEP_PREVIOUS);
						if (keepPreviousName) {
							newName = b.getName() + " " + newName;
						}
						if (b.getName() != newName) {
							b.setName(newName);
							somethingChanged = true;
						}
						i++;
					}
					// Notify UI
					if (somethingChanged) {
						WordCloudView.update(combo.getSelectionIndex(), true);
						VisualisationsHelper.notifyVisualisations(AdaptedModelManager.getFeatureList(),
								AdaptedModelManager.getAdaptedModel(), null, new NullProgressMonitor());
					}
				}
			}
		});

		/*
		 * Here, we will just rename the selected block with the string which is
		 * the most often saw in the block' word cloud.
		 */
		renameOne.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.Selection:

					int ind = combo.getSelectionIndex();
					Block b = AdaptedModelManager.getAdaptedModel().getOwnedBlocks().get(ind);
					ArrayList<String> names = new ArrayList<String>();
					for (Block bl : AdaptedModelManager.getAdaptedModel().getOwnedBlocks()) {
						if (b == bl)
							continue;
						names.add(bl.getName());
					}
					Cloud c = WordCloudVisualisation.getClouds().get(ind);
					if (WordCloudView.getSingleton().getTabFolder().getSelectionIndex() == 1)
						c = WordCloudVisualisation.getCloudsTFIDF().get(ind);
					String name = WordCloudUtil.rename(names, c);
					b.setName(name);
					WordCloudView.update(combo.getSelectionIndex(), true);
					VisualisationsHelper.notifyVisualisations(AdaptedModelManager.getFeatureList(),
							AdaptedModelManager.getAdaptedModel(), null, new NullProgressMonitor());
					break;

				}
			}
		});

		/*
		 * this one will rename the selected block with the text in the text
		 * control.
		 */
		accept.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.Selection:

					if (text.getText().equals(""))
						return;

					int ind = combo.getSelectionIndex();
					Block b = AdaptedModelManager.getAdaptedModel().getOwnedBlocks().get(ind);
					b.setName(text.getText());
					WordCloudView.update(combo.getSelectionIndex(), true);
					VisualisationsHelper.notifyVisualisations(AdaptedModelManager.getFeatureList(),
							AdaptedModelManager.getAdaptedModel(), null, new NullProgressMonitor());
					break;

				}
			}
		});

		/*
		 * When you choose an item from the combo control it must update the
		 * view. it updates the list control and canvas control.
		 */
		combo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo c = (Combo) e.getSource();
				WordCloudView.update(c.getSelectionIndex(), true);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing
			}
		});

		/*
		 * When you double click on an item from the list it will rename the
		 * selected block with the value of the selected item.
		 */
		list.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				List l = (List) e.getSource();
				Block b = AdaptedModelManager.getAdaptedModel().getOwnedBlocks().get(combo.getSelectionIndex());
				String newName = l.getItem(l.getSelectionIndex());
				int ind = newName.indexOf(" - ");
				b.setName(newName.substring(0, ind));
				WordCloudView.update(combo.getSelectionIndex(), true);
				VisualisationsHelper.notifyVisualisations(AdaptedModelManager.getFeatureList(),
						AdaptedModelManager.getAdaptedModel(), null, new NullProgressMonitor());
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// do nothing
			}

			@Override
			public void mouseUp(MouseEvent e) {
				// do nothing
			}
		});

		tabFolder.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				int ind = ((TabFolder) (e.getSource())).getSelectionIndex();
				int i = WordCloudView.getSingleton().getCombo().getSelectionIndex();
				WordCloudView.getSingleton().getList().removeAll();
				Cloud c = null;
				if (ind == 0) {
					c = WordCloudVisualisation.getClouds().get(i);
					WordCloudUtil.drawWordCloud(cmp, c);
					addWordCloudListeners(cmp, i);
				} else {
					c = WordCloudVisualisation.getCloudsTFIDF().get(i);
					WordCloudUtil.drawWordCloud(cmpTFIDF, c);
					addWordCloudListeners(cmpTFIDF, i);
				}

				for (Tag t : c.tags())
					WordCloudView.getSingleton().getList()
							.add(t.getName() + " - " + String.format("%.2f", t.getNormScore()));

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing
			}
		});

		if (AdaptedModelManager.getAdaptedModel() == null)
			return;

		for (Block b : AdaptedModelManager.getAdaptedModel().getOwnedBlocks())
			combo.add(b.getName());

		if (combo.getItemCount() > 0)
			combo.select(0);
	}

	@Override
	public void setFocus() {

	}

	/**
	 * Add Word Cloud listeners to change names when clicking on one word
	 * 
	 * @param composite
	 *            containing the labels
	 */
	static private void addWordCloudListeners(Composite cloudComposite, int blockIndex) {
		for (final Control c : cloudComposite.getChildren()) {
			c.addMouseListener(new WordCloudListener(blockIndex));
		}
	}
}
