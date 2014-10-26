package org.but4reuse.adapters.sourcecode.featurehouse.cide.languages;

import org.but4reuse.adapters.sourcecode.featurehouse.cide.greferences.IResolver;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.greferences.IValidationRule;

/**
 * interface that bundles everything that is needed to specify a validator.
 * note, a language validator is not necessary for a language extension.
 * however, when a language validator is used, than these methods have to return
 * non-null values.
 * 
 * @author cKaestner
 * 
 */
public interface ILanguageValidator {

	public IResolver getResolver();

	public IValidationRule[] getValidationRules();

}
