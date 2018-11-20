package org.but4reuse.adapters.javajdt;

import org.eclipse.jdt.core.dom.PackageDeclaration;

/**
 * Id utils
 * 
 * @author jabier.martinez
 */
public class IdUtils {

	public static String getId(PackageDeclaration packageDeclaration){
		if(packageDeclaration == null){
			return "defaultPackage";
		}
		return packageDeclaration.getName().getFullyQualifiedName();
	}
	
}
