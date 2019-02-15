package org.but4reuse.adapters.ui.xmlgenerator;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.JavaUnderstanding.activator.Activator;
import org.but4reuse.adapters.JavaUnderstanding.adapter.JavaUnderstandingAdapter;
import org.but4reuse.adapters.JavaUnderstanding.adapter.JavaUnderstandingAdapter.Mode;
import org.but4reuse.adapters.JavaUnderstanding.preferences.JavaUnderstandingAdapterPreferencePage;
import org.but4reuse.adapters.JavaUnderstanding.preferences.JavaUnderstandingAdapterPreferencePage.Choice;
import org.but4reuse.adapters.helper.AdaptersHelper;
import org.but4reuse.adapters.ui.AdaptersSelectionDialog;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.eclipse.core.resources.IContainer;

public class SaveDataUtils {

	// calculate the level of reuse for a specific artefact
	private static double levelOfReuse(double NBUniqueElement, double NBTotalElements) {
		double d = (1-(NBUniqueElement/NBTotalElements));
		return d*100;
	}
	
	// calculate the level of reuse of each artefact which shares a same elements with a specific artefact 
	private static HashMap<String, Double> levelofReuseForeachArtefact( double NBUniqueElement, double NBTotalElements, HashMap<String, List<IElement>> listofelements) {
		HashMap<String, Double> results = new HashMap<String, Double>();
		
		double nbElementsShared = NBTotalElements - NBUniqueElement;
		double stat=0;
		for ( String s : listofelements.keySet()) {
			stat= (listofelements.get(s).size()*100)/nbElementsShared;
			results.put(s, stat);
		}
		
		return results;
	}
	
	// calculate statistics
	public static File saveDataInFile(){
		// Get adapted model
		AdaptedModel adaptedModel = AdaptedModelManager.getAdaptedModel();
		
		if (adaptedModel != null) {
			// Get adapters selected
			List<IAdapter> adapters = AdaptersSelectionDialog.getSelectedAdapters();
		
			// Get blocks
			List<Block> blocks = adaptedModel.getOwnedBlocks();
			
			// Get artefacts
			List<AdaptedArtefact> adaptedArtefacts = adaptedModel.getOwnedAdaptedArtefacts();
			
			// Get name of artefacts 
			List<String> artefactsName = new ArrayList<String>();
			for(AdaptedArtefact aa : adaptedModel.getOwnedAdaptedArtefacts()) {
				artefactsName.add(AdaptedModelHelper.getArtefactName(aa.getArtefact()));
			}
			
			// Get name of adapters selected
			List<String> adaptersName = new ArrayList<String>();
			for(IAdapter adapter : adapters) {
				adaptersName.add(AdaptersHelper.getAdapterName(adapter));
			}
			
			// Get configuration of adapter (get the configuration only for JavaUnderstandingAdapter)
			Map<String, String> javaUnderstandingConfig = new HashMap<String, String>();
			Map<String, String> modeConfig = new HashMap<String, String>();
			
			for(IAdapter adapter : adapters) {
				if(adapter instanceof JavaUnderstandingAdapter) {
					Mode mode = ((JavaUnderstandingAdapter)adapter).getMode();
					javaUnderstandingConfig.put("Mode",mode.toString());
					boolean ignorePath = Activator.getDefault()
							 					  .getPreferenceStore()
							 					  .getBoolean(JavaUnderstandingAdapterPreferencePage.IGNORE_PATH);
					javaUnderstandingConfig.put("IgnorePath", String.valueOf(ignorePath));
					
					// get configuration of the mode used
					switch (mode) {
						
						case IMPORTS:
							break;
						case FILES:
							modeConfig.put("files level option",		
									Activator.getDefault()
											 .getPreferenceStore()
											 .getString(JavaUnderstandingAdapterPreferencePage.FILES_OPTION));
							break;
						case INTERFACES:
							break;
						case METHODS:
							modeConfig.put("Compare bodies of methods",
								Choice.valueOf(
									Activator.getDefault()
											 .getPreferenceStore()
											 .getString(JavaUnderstandingAdapterPreferencePage.BODY))
											 .name());
							modeConfig.put("Bodies comparison's method", 
								Choice.valueOf(
									Activator.getDefault()
											 .getPreferenceStore()
											 .getString(JavaUnderstandingAdapterPreferencePage.ORDER_SENSITIVITY))
											 .name());
							modeConfig.put("Bodies similarity pourcentage accurency",
								Activator.getDefault()
										 .getPreferenceStore()
										 .getInt(JavaUnderstandingAdapterPreferencePage.SIMILARITY_LEVEL)+"%");
							modeConfig.put("Compare names of methods",
								Choice.valueOf(
									Activator.getDefault()
											 .getPreferenceStore()
											 .getString(JavaUnderstandingAdapterPreferencePage.NAME_METHOD))
											 .name());
							modeConfig.put("Compare return type of methods",
								Choice.valueOf(
									Activator.getDefault()
											 .getPreferenceStore()
											 .getString(JavaUnderstandingAdapterPreferencePage.RETURNTYPE))
											 .name());
							modeConfig.put("Compare parameters of methods",
								Choice.valueOf(
									Activator.getDefault()
											 .getPreferenceStore()
											 .getString(JavaUnderstandingAdapterPreferencePage.PARAMETERS))
											 .name());
							modeConfig.put("Compare modifiers of methods",
								Choice.valueOf(
									Activator.getDefault()
											 .getPreferenceStore()
											 .getString(JavaUnderstandingAdapterPreferencePage.MODIFIER_METHOD))
											 .name());
							break;
						case SUPERCLASS:
							break;
						case FIELDS:
							modeConfig.put("Compare names of fields",
							Choice.valueOf(
									Activator.getDefault()
											 .getPreferenceStore()
											 .getString(JavaUnderstandingAdapterPreferencePage.NAME_FIELD))
											 .name());
							modeConfig.put("Compare datatypes of fields",
							Choice.valueOf(
									Activator.getDefault()
											 .getPreferenceStore()
											 .getString(JavaUnderstandingAdapterPreferencePage.DATATYPE_FIELD))
											 .name());
							modeConfig.put("Compare modifiers of fields",
								Choice.valueOf(
									Activator.getDefault()
											 .getPreferenceStore()
											 .getString(JavaUnderstandingAdapterPreferencePage.MODIFIER_FIELD))
											 .name());
							modeConfig.put("Compare attributes of fields",
								Choice.valueOf(
									Activator.getDefault()
											 .getPreferenceStore()
											 .getString(JavaUnderstandingAdapterPreferencePage.ATTRIBUTE_FIELD))
											 .name());
							break;
						default:
							break;
					}
						
				}
			}
			
			// Analyse based on the artefacts
			// Calculate the level of reuse of each artefact
			ArrayList<ArtefactStat> stats = new ArrayList<ArtefactStat>();
			
			for(int i=0 ; i<adaptedArtefacts.size(); i++) {
				// total number of elements of this artefact
				int totalNumberOfElements=0;
				
				// list of elements which this artefact doesn't share with other
				List<IElement> listOfUniqueElements = new ArrayList<IElement>();
				
				// list of elements that others artefacts share with this artefact
				HashMap<String, List<IElement>> listElements = new HashMap<>();
				
				// get blocks of this artefact
				List<Block> artefactBlocks = AdaptedModelHelper.getBlocksOfAdaptedArtefact(adaptedArtefacts.get(i));
				// get total number of elements of this artefact
				totalNumberOfElements= AdaptedModelHelper.getElementsOfBlocks(artefactBlocks).size();
				
				for(Block b : artefactBlocks) {
					// number of artefacts sharing this block
					int nbArtefacts=0;
					
					// Compare this artefact with others artefacts
					for( int j = 0 ; j<artefactsName.size();j++) {
						if(j!=i) {
							// if an artefact j shares a same block
							if(AdaptedModelHelper.getBlocksOfAdaptedArtefact(adaptedArtefacts.get(j)).contains(b)) {
								nbArtefacts++;
								// case if the artefact j is already sharing a block with artefact i
								if(listElements.containsKey(artefactsName.get(j))){
									// update the sharing elements
									List<IElement> be = listElements.get(artefactsName.get(j));
									be.addAll(AdaptedModelHelper.getElementsOfBlock(b));
									listElements.put(artefactsName.get(j),be );
								}else {
									// put new artefact to the listes
									listElements.put(artefactsName.get(j),AdaptedModelHelper.getElementsOfBlock(b));
								}
							}
						}
					}
					// if no artecfact shares this block
					if(nbArtefacts==0) {
						listOfUniqueElements.addAll(AdaptedModelHelper.getElementsOfBlock(b));
					}
				}
				
				// save all data of reuse in an object
				ArtefactStat s = new ArtefactStat(artefactsName.get(i),
							 levelOfReuse(listOfUniqueElements.size(), totalNumberOfElements),
							 listElements,
							 levelofReuseForeachArtefact(listOfUniqueElements.size(), totalNumberOfElements, listElements),
							 listOfUniqueElements,
							 totalNumberOfElements - listOfUniqueElements.size());
				stats.add(s);
			}
			
			// Analyse based on the blocks
			
			// blocks reused by several artefacts
			Map<BlockData, List<ArtefactData>> map_block_reuse = new HashMap<BlockData,List<ArtefactData>>(); 
			
			// Unique block in an artefact (List<ArtefactData> content only one object)
			Map<BlockData, List<ArtefactData>> map_block_unique = new HashMap<BlockData,List<ArtefactData>>(); 
			
			for(Block b : blocks) {
				List<ArtefactData> list = new ArrayList<ArtefactData>();
				
				// verify which artefacts contains the block b
				for(int i=0 ; i<adaptedArtefacts.size(); i++) {
					AdaptedArtefact aa = adaptedArtefacts.get(i);
					if( AdaptedModelHelper.getBlocksOfAdaptedArtefact(aa).contains(b) ) {
						
						List<Block> aa_block = AdaptedModelHelper.getBlocksOfAdaptedArtefact(aa);
						list.add(
								new ArtefactData(aa_block.size(),
												 AdaptedModelHelper.getElementsOfBlocks(aa_block).size(),
												 artefactsName.get(i),
												 aa)
						);
					}
				}
				if(list.size()>1) {
					map_block_reuse.put(new BlockData(b.getName(), AdaptedModelHelper.getElementsOfBlock(b)), list);
				}
				else { // if the list content only one object, this block is unique for an artefact
					map_block_unique.put(new BlockData(b.getName(), AdaptedModelHelper.getElementsOfBlock(b)), list);
				}
			}
			
			// calculate the percentage of unique elements of each artefact (default 100%)
			Map<String, Double> map_artefact = new HashMap<String, Double>();
			for(String name : artefactsName) {
				map_artefact.put(name, 100.0);
			}
			// if an artefact exist in the map_block_reuse, this artefact reuses elements of others artefacts
			for(BlockData bd : map_block_reuse.keySet()) {
				List<ArtefactData> list = map_block_reuse.get(bd);
				for(ArtefactData ad : list) {
					// (total number of elements of blocks) / (total number of elements of artefact) 
					// and get the percentage
					double percent = ((bd.getNbElem()*1.0)/ad.getNb_elems())*100;
					String ad_name = ad.getName();
					// subtract the percentage of unique elements of this artefact
					map_artefact.put(ad_name, map_artefact.get(ad_name)-percent);
				}
			}
	
			// Class used for writing data in the XML file giving all calculated data
			XMLFileWriter  xmlgen = new XMLFileWriter(adaptersName, map_block_reuse, map_block_unique, map_artefact, stats, javaUnderstandingConfig, modeConfig);
			
			// Get the path to save the XML file (same project of the file ".artefactmodel")
			IContainer output = AdaptedModelManager.getDefaultOutput();
			File outputFile = WorkbenchUtils.getFileFromIResource(output);
			
			// directory name
			File graphsFolder = new File(outputFile, "reuseAnalysis"); 
			graphsFolder.mkdir();
	
			// Give a name to the xml file with the timestamp
			DateFormat dateFormat=new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			Date currentDate=new Date();
			
			File file = new File(graphsFolder, "result_"+dateFormat.format(currentDate)+".xml");
			xmlgen.generateXmlfile(file);
			
			return file;
		}
		else {
			return null;
		}
	}
}
