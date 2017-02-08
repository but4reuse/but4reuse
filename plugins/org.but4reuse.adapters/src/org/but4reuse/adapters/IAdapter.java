package org.but4reuse.adapters;

import java.net.URI;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * IAdapter that converts an artefact to a set of IElements. Also used to
 * construct a set of IElements to an artefact.
 * 
 * @author jabier.martinez
 */
public interface IAdapter {

	/**
	 * Whether an artefact is adaptable by this adapter
	 * 
	 * @param uri
	 *            of the artefact
	 * @param monitor
	 *            (optionally use monitor.subTask method to provide information
	 *            to the monitor)
	 * @return true or false if the artefact is adaptable by this adapter
	 */
	public boolean isAdaptable(URI uri, IProgressMonitor monitor);

	/**
	 * If isAdaptable returned true, then this method should convert the
	 * artefact to a set of elements. The IElement ownership relations can be
	 * defined also during this process.
	 * 
	 * @param uri
	 *            of the artefact
	 * @param monitor
	 *            (optionally use monitor.subTask method to provide information
	 *            to the monitor)
	 * @return true or false if the artefact is adaptable by this adapter
	 */
	public List<IElement> adapt(URI uri, IProgressMonitor monitor);

	/**
	 * Given an output URI, this method should construct an artefact containing
	 * a set of IElements
	 * 
	 * @param uri
	 * @param elements
	 * @param monitor
	 *            (optionally use monitor.subTask method to provide information
	 *            to the monitor)
	 */
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor);
}
