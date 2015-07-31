package org.but4reuse.utils.nlp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class UselessWordsRemover {

	static private final String modelPath ="models"+File.separator+"en-pos-maxent.bin";
	
	public static void removeUselessWords(List<String> words)
	{
		
		InputStream modelIn = null;
		
		try {
			modelIn = new FileInputStream(modelPath);
			POSModel model = new POSModel(modelIn);
			POSTaggerME tagger = new POSTaggerME(model);
			
			String [] tab = tagger.tag((String[])(words.toArray()));
			words.clear();
			for(int i = 0;i< tab.length;i++)
			{
				System.out.println("Words["+i+"] : "+tab[i]);
			}
		}
		catch (IOException e) {
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
