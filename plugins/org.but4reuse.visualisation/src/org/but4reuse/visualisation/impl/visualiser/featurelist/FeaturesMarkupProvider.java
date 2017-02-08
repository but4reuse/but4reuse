package org.but4reuse.visualisation.impl.visualiser.featurelist;

import java.util.HashMap;
import java.util.Map;

import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.eclipse.contribution.visualiser.core.Stripe;
import org.eclipse.contribution.visualiser.interfaces.IMarkupKind;
import org.eclipse.contribution.visualiser.interfaces.IMember;
import org.eclipse.contribution.visualiser.simpleImpl.SimpleMarkupProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

/**
 * Features Markup Provider
 * 
 * @author jabier.martinez
 */
public class FeaturesMarkupProvider extends SimpleMarkupProvider {

	private Map<Feature, IMarkupKind> featuresAndKinds = new HashMap<Feature, IMarkupKind>();

	public void reset() {
		// Remove previous
		resetMarkupsAndKinds();
		getFeaturesAndNames().clear();
	}

	public void update(FeatureList featurelist) {
		// Update
		for (Feature feature : featurelist.getOwnedFeatures()) {
			String featureName = feature.getName();
			FeatureMarkupKind kind = new FeatureMarkupKind(featureName);
			kind.setFeature(feature);
			featuresAndKinds.put(feature, kind);
			addMarkupKind(kind);
		}
	}

	public Map<Feature, IMarkupKind> getFeaturesAndNames() {
		return featuresAndKinds;
	}

	public void setBlocksAndNames(Map<Feature, IMarkupKind> featuresAndNames) {
		this.featuresAndKinds = featuresAndNames;
	}

	@Override
	public boolean processMouseclick(IMember member, Stripe stripe, int buttonClicked) {
		String message = stripe.getToolTip();
		MessageDialog.openInformation(Display.getCurrent().getActiveShell(), member.getName(), message);
		// return false for not to recompute
		return false;
	}

}
