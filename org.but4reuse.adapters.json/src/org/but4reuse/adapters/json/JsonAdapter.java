package org.but4reuse.adapters.json;

import java.net.URI;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.eclipse.core.runtime.IProgressMonitor;


public class JsonAdapter implements IAdapter{

	@Override
	public boolean isAdaptable(URI uri, IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<IElement> adapt(URI uri, IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void construct(URI uri, List<IElement> elements,
			IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		
	}

}
