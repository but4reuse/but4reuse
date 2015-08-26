package org.but4reuse.adapters.eclipse;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

public class FileElement extends AbstractElement {

	private URI uri;
	private URI relativeURI;

	@Override
	public double similarity(IElement anotherElement) {
		// When they have the same relative URI
		// TODO URIs can reference to the same file... check this
		if (anotherElement instanceof FileElement) {
			FileElement anotherFileElement = ((FileElement) anotherElement);

			// Same URI?
			if (this.getRelativeURI().equals(anotherFileElement.getRelativeURI())) {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public String getText() {
		return getRelativeURI().toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getRelativeURI() == null) ? 0 : getRelativeURI().hashCode());
		return result;
	}

	public URI getRelativeURI() {
		return relativeURI;
	}

	public void setRelativeURI(URI relativeURI) {
		this.relativeURI = relativeURI;
	}

	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

	@Override
	public List<String> getWords() {
		// always empty list
		return new ArrayList<String>();
	}
}
