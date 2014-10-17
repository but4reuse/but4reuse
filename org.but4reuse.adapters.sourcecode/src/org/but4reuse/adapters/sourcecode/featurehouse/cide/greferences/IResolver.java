package org.but4reuse.adapters.sourcecode.featurehouse.cide.greferences;

import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.ASTNode;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.ISourceFile;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.greferences.IReferenceType;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.greferences.ReferenceResolvingException;

public interface IResolver {

	/**
	 * resolves a reference. returns the target node of the reference or null if
	 * reference could not be resolved
	 * 
	 * @param type
	 * @param source
	 * @return
	 */
	public ASTNode getReferenceTarget(IReferenceType type, ASTNode source) throws ReferenceResolvingException;

	enum CacheRequirement {
		NoCache/* References can be resolved inside a single file */, CacheAll
		/* the whole project needs to be cached before resolving references */
	};

	public CacheRequirement getCacheRequirement();

	/**
	 * called by the framework after parsing a file to cache information
	 * required for resolving references
	 * 
	 * @param file
	 */
	public void cacheSourceFile(ISourceFile ast);

	/**
	 * called by the framework to clear the whole cache
	 */
	public void clearCache();
}
