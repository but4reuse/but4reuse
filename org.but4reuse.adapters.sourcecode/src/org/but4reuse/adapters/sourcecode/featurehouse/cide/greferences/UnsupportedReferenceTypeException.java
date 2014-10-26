package org.but4reuse.adapters.sourcecode.featurehouse.cide.greferences;

import org.but4reuse.adapters.sourcecode.featurehouse.cide.greferences.IReferenceType;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.greferences.ReferenceResolvingException;

public class UnsupportedReferenceTypeException extends
		ReferenceResolvingException {

	public UnsupportedReferenceTypeException(IReferenceType type) {
		super("Unsupported Reference Type: " + type);
	}

}
