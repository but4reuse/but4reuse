package org.but4reuse.utils.nlp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class UselessWordsRemover {

	static private final String modelPath ="models"+File.separator+"en-pos-maxent.bin";
	
	public static void removeUselessWords(List<String> words)
	{
		
		InputStream modelIn = null;
	
		try {
			
			URL url = FileLocator.find(Platform.getBundle("org.but4reuse.utils.nlp")
					, new Path(modelPath), null);
			URL fileURL = FileLocator.toFileURL(url);
			
			modelIn = new FileInputStream(new File(fileURL.getPath()));
			POSModel model = new POSModel(modelIn);
			POSTaggerME tagger = new POSTaggerME(model);
			
			String in[] = new String[words.size()];
			for(int i = 0 ;i <words.size();i++)
				in[i] = new String(words.get(i));
			String [] out = tagger.tag(in);
			int cpt = 0;
			for(int i = 0;i< out.length;i++)
			{
				if(out[i].equals("IN") || out[i].equals("CD"))
				{
					words.remove(i-cpt);
					cpt++;
					
				}
			}
		}
		catch (Exception e) {
			// Model loading failed, handle the error
			e.printStackTrace();
		}
		finally {
			if (modelIn != null) {
				try {
						modelIn.close();
					}
					catch (IOException e) {
					}
			}	
		}
		
		
	}
	
	
	
}
