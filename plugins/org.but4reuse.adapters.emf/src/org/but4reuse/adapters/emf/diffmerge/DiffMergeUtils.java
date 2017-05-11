package org.but4reuse.adapters.emf.diffmerge;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.diffmerge.api.IComparison;
import org.eclipse.emf.diffmerge.api.IDiffPolicy;
import org.eclipse.emf.diffmerge.api.IMatchPolicy;
import org.eclipse.emf.diffmerge.api.IMergePolicy;
import org.eclipse.emf.diffmerge.api.scopes.IEditableModelScope;
import org.eclipse.emf.diffmerge.diffdata.impl.EComparisonImpl;
import org.eclipse.emf.diffmerge.impl.scopes.FilteredModelScope;
import org.eclipse.emf.diffmerge.ui.EMFDiffMergeUIPlugin;
import org.eclipse.emf.diffmerge.ui.specification.IComparisonMethod;
import org.eclipse.emf.diffmerge.ui.specification.IComparisonMethodFactory;
import org.eclipse.emf.diffmerge.ui.specification.ext.DefaultComparisonMethod;
import org.eclipse.emf.diffmerge.ui.specification.ext.EObjectScopeDefinition;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * Diff Merge Utils
 * 
 * @author jabier.martinez
 */
public class DiffMergeUtils {

	/**
	 * Get applicable comparison methods
	 * 
	 * @param leftEObject
	 * @param rightEObject
	 * @return
	 */
	public static List<IComparisonMethod> getApplicableComparisonMethods(EObject leftEObject, EObject rightEObject) {
		List<IComparisonMethod> methods = new ArrayList<IComparisonMethod>();
		EObjectScopeDefinition left = new EObjectScopeDefinition(leftEObject, "left", true);
		EObjectScopeDefinition right = new EObjectScopeDefinition(rightEObject, "right", true);
		List<IComparisonMethodFactory> listcmf = getApplicableComparisonMethodFactories(leftEObject, rightEObject);
		if (listcmf.isEmpty()) {
			methods.add(new DefaultComparisonMethod(left, right, null));
		} else {
			for (IComparisonMethodFactory factory : listcmf) {
				methods.add(factory.createComparisonMethod(left, right, null));
			}
		}
		return methods;
	}

	/**
	 * Get applicable comparison methods
	 * 
	 * @param leftEObject
	 * @param rightEObject
	 * @return
	 */
	public static List<IComparisonMethodFactory> getApplicableComparisonMethodFactories(EObject leftEObject,
			EObject rightEObject) {
		EObjectScopeDefinition left = new EObjectScopeDefinition(leftEObject, "left", true);
		EObjectScopeDefinition right = new EObjectScopeDefinition(rightEObject, "right", true);
		return EMFDiffMergeUIPlugin.getDefault().getSetupManager().getApplicableComparisonMethodFactories(left, right,
				null);
	}

	/**
	 * Is equal EObject
	 * 
	 * @param referenceEObject
	 * @param targetEObject
	 * @return
	 */
	public static boolean isEqualEObject(IComparisonMethod icm, EObject referenceEObject, EObject targetEObject) {

		List<EObject> referenceElements = new ArrayList<EObject>();
		referenceElements.add(referenceEObject);
		IEditableModelScope referenceScope = new FilteredModelScope(referenceElements);

		List<EObject> targetElements = new ArrayList<EObject>();
		targetElements.add(targetEObject);
		IEditableModelScope targetScope = new FilteredModelScope(targetElements);

		// Get the more appropriate comparison method
		IMatchPolicy matchPolicy = icm.getMatchPolicy();
		// Merge policy not needed, but we get it
		IMergePolicy mergePolicy = icm.getMergePolicy();

		// Compare ignoring all structural policies in the diff
		IDiffPolicy diffPolicy = new IgnoreAllStructuralFeaturesDiffPolicy();
		IComparison comparison = new EComparisonImpl(targetScope, referenceScope);
		comparison.compute(matchPolicy, diffPolicy, mergePolicy, new NullProgressMonitor());

		// Collection<IDifference> differences =
		// comparison.getRemainingDifferences();
		// for (IDifference difference : differences) {
		// System.out.println(difference);
		// }

		// Get differences -> no differences means equal
		boolean noDifferences = !comparison.hasRemainingDifferences();
		return noDifferences;
	}

	/**
	 * Is equal EObject Attribute
	 * 
	 * @param referenceEObject
	 * @param referenceEAttribute
	 * @param referenceValue
	 * @param targetEObject
	 * @param targetEAttribute
	 * @param targetValue
	 * @return
	 */
	public static boolean isEqualEObjectAttribute(IComparisonMethod icm, EObject referenceEObject,
			EAttribute referenceEAttribute, Object referenceValue, EObject targetEObject, EAttribute targetEAttribute,
			Object targetValue) {

		// Same attribute
		if (!referenceEAttribute.equals(targetEAttribute)) {
			return false;
		}
		// Same attribute owner
		if (!isEqualEObject(icm, referenceEObject, targetEObject)) {
			return false;
		}

		IDiffPolicy diffPolicy = icm.getDiffPolicy();
		// Consider equal if attribute not covered
		if (!diffPolicy.coverFeature(referenceEAttribute)) {
			return true;
		}
		// Check equal value
		return diffPolicy.considerEqual(referenceValue, targetValue, referenceEAttribute);
	}

	/**
	 * Is equal EObject Reference
	 * 
	 * @param referenceEObject
	 * @param referenceEReference
	 * @param referenceReferenced
	 * @param targetEObject
	 * @param targetEReference
	 * @param targetReferenced
	 * @return
	 */
	public static boolean isEqualEObjectReference(IComparisonMethod icm, EObject referenceEObject,
			EReference referenceEReference, List<EObject> referenceReferenced, EObject targetEObject,
			EReference targetEReference, List<EObject> targetReferenced) {

		// Not the same reference
		if (!referenceEReference.equals(targetEReference)) {
			return false;
		}

		IDiffPolicy diffPolicy = icm.getDiffPolicy();
		// Consider equal if reference not covered
		if (!diffPolicy.coverFeature(referenceEReference)) {
			return true;
		}

		// Not the same size
		if (referenceReferenced.size() != targetReferenced.size()) {
			return false;
		}

		// Not the same owner
		if (!isEqualEObject(icm, referenceEObject, targetEObject)) {
			return false;
		}

		// Check exactly the same references and in the same order
		// Check one by one ordered
		if (referenceEReference.isOrdered()) {
			for (int i = 0; i < referenceReferenced.size(); i++) {
				EObject ref = referenceReferenced.get(i);
				EObject tar = targetReferenced.get(i);
				if (!isEqualEObject(icm, ref, tar)) {
					return false;
				}
			}
		} else {
			// Check no ordered
			boolean found = true;
			for (int i = 0; i < referenceReferenced.size(); i++) {
				if (!found) {
					return false;
				}
				found = false;
				EObject ref = referenceReferenced.get(i);
				for (int x = 0; x < targetReferenced.size(); x++) {
					EObject tar = targetReferenced.get(x);
					if (isEqualEObject(icm, ref, tar)) {
						found = true;
						break;
					}
				}
			}
			// In case of the last one not found
			if (!found) {
				return false;
			}
		}
		// True if all are the same or they were both empty
		return true;
	}
}
