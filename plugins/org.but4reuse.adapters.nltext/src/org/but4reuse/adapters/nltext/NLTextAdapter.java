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
	public boolean isAdaptable(URI uri, IProgressMonitor monitor) {
		return false;
	}

	@Override
	public List<IElement> adapt(URI uri, IProgressMonitor monitor) {
		List<IElement> elements = new ArrayList<IElement>();
		File file = FileUtils.getFile(uri);
		String text = FileUtils.getStringOfFile(file);
		String[] sentences = SentenceSplitter.split(text);
		for (String s : sentences) {
			SentenceElement se = new SentenceElement();
			se.setSentence(s);
			elements.add(se);
		}
		return elements;
	}

	@Override
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {

	}

}
