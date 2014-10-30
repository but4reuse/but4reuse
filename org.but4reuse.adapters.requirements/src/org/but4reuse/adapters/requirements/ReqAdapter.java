package org.but4reuse.adapters.requirements;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.utils.emf.EMFUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rmf.reqif10.AttributeValue;
import org.eclipse.rmf.reqif10.AttributeValueString;
import org.eclipse.rmf.reqif10.ReqIF;
import org.eclipse.rmf.reqif10.SpecHierarchy;
import org.eclipse.rmf.reqif10.SpecObject;
import org.eclipse.rmf.reqif10.Specification;
import org.eclipse.swt.widgets.Display;

/**
 * Requirements Adapter
 * @author jabier.martinez
 */
public class ReqAdapter implements IAdapter {

	@Override
	/**
	 * is the object a reqIF resource?
	 */
	public boolean isAdaptable(URI uri, IProgressMonitor monitor) {
		EObject eObject = EMFUtils.getEObject(uri);
		if (eObject == null || !(eObject instanceof ReqIF)) {
			return false;
		}
		return true;
	}

	@Override
	/**
	 * Loop through the requirements hierarchy
	 */
	public List<IElement> adapt(URI uri, IProgressMonitor monitor) {
		List<IElement> result = new ArrayList<IElement>();
		EObject eObject = EMFUtils.getEObject(uri);
		if (eObject != null && (eObject instanceof ReqIF)) {
			ReqIF reqif = (ReqIF)eObject;
			List<Specification> specs = reqif.getCoreContent().getSpecifications();
			for(Specification spec : specs){
				List<SpecHierarchy> elems = spec.getChildren();
				for(SpecHierarchy elem : elems){
					SpecObject specObject = elem.getObject();
					List<AttributeValue> attributeValues = specObject.getValues();
					for(AttributeValue av : attributeValues){
						if(av instanceof AttributeValueString){
							ReqElement reqElement = new ReqElement();
							reqElement.setDescription(((AttributeValueString)av).getTheValue());
							result.add(reqElement);
						}
					}
				}
			}
		}
		return result;
	}

	@Override
	/**
	 * Not supported yet
	 */
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {
		// TODO construct ReqIf
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Construction",
						"For the moment, construction is not available for the requirements adapter");
			}
		});
	}

}
