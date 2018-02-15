package org.but4reuse.adapters.nltext;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.utils.files.FileUtils;
import org.but4reuse.utils.nlp.SentenceSplitter;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * NLText Adapter
 * 
 * @author jabier.martinez
 * 
 */
public class NLTextAdapter implements IAdapter {

	@Override
	/**
	 * Always return false but the user can select it in the list of adapters
	 */
	public boolean isAdaptable(URI uri, IProgressMonitor monitor) {
		return false;
	}

	@Override
	/**
	 * Split the text in sentences using NLP techniques for sentence splitting
	 */
	public List<IElement> adapt(URI uri, IProgressMonitor monitor) {
		List<IElement> elements = new ArrayList<IElement>();
		File file = FileUtils.getFile(uri);
		String text = FileUtils.getStringOfFile(file);
		String[] sentences = SentenceSplitter.split(text);
		for (String s : sentences) {
			SentenceElement se = new SentenceElement();
			se.sentence = s;
			elements.add(se);
		}
		return elements;
	}

	@Override
	/**
	 * Same construct as the text lines adapter
	 */
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {
		try {
			// Use the given file or use a default name if a folder was given
			if (uri.toString().endsWith("/")) {
				uri = new URI(uri.toString() + "construction.txt");
			}
			// Create file if it does not exist
			File file = FileUtils.getFile(uri);
			FileUtils.createFile(file);

			for (IElement element : elements) {
				FileUtils.appendToFile(file, element.getText());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
