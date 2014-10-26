package org.but4reuse.adapters.sourcecode.featurehouse.cide.greferences;

import java.util.HashMap;
import java.util.Set;

import org.but4reuse.adapters.sourcecode.featurehouse.cide.features.IASTColorProvider;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.greferences.IReference;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.greferences.IReferenceType;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.greferences.IValidationErrorCallback;

public interface IValidationRule {
	final static int Ignore = -1; // during handling only
	final static int Warning = 0; // during handling only
	final static int Error = 1;

	int getErrorSeverity();

	String getErrorMessage();

	IReferenceType[] getRequiredReferences();

	void validate(IASTColorProvider colorProvider,
			HashMap<IReferenceType, Set<IReference>> references, IValidationErrorCallback errorCallback);

	String getName();
}
