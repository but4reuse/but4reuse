package org.but4reuse.adaptedmodel.helpers;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.AdaptedModelFactory;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.adapters.IAdapter;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author github.com/readycyr
 */
public class AdaptConcurrently {

	/**
	 * Adapt an artefact model to create the list of elements of each artefact
	 * concurrently
	 * 
	 * @param artefactModel
	 * @param adapters
	 * @param monitor
	 * @return
	 */
	public static AdaptedModel adaptConcurrently(ArtefactModel artefactModel, List<IAdapter> adapters,
			IProgressMonitor monitor) {
		// When we adapt we consider that we are starting a new analysis
		AdaptedModelManager.getElapsedTimeRegistry().clear();
		ExecutorService exec = Executors.newCachedThreadPool();
		List<Future<AdaptedArtefact>> listAdaptedArtefact = new LinkedList<Future<AdaptedArtefact>>();

		long startTime = System.currentTimeMillis();

		AdaptedModel adaptedModel = AdaptedModelFactory.eINSTANCE.createAdaptedModel();

		/* This will respect a FIFA (First In First Add) to the EList */
		for (Artefact artefact : artefactModel.getOwnedArtefacts()) {
			if (artefact.isActive()) {
				listAdaptedArtefact.add(exec.submit(task(artefact, adapters, monitor)));
			}
		}
		for (Future<AdaptedArtefact> futureArtefact : listAdaptedArtefact) {
			AdaptedArtefact aa = null;
			try {
				aa = futureArtefact.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			if (aa != null) {
				adaptedModel.getOwnedAdaptedArtefacts().add(aa);
			}
		}
		exec.shutdown();
		// Add info to the manager
		AdaptedModelManager.registerTime("Adapt all artefacts", System.currentTimeMillis() - startTime);
		AdaptedModelManager.setAdaptedModel(adaptedModel);
		AdaptedModelManager.setAdapters(adapters);
		return adaptedModel;
	}

	public static Callable<AdaptedArtefact> task(final Artefact artefact, final List<IAdapter> adapters,
			final IProgressMonitor monitor) {
		return new Callable<AdaptedArtefact>() {
			@Override
			public AdaptedArtefact call() throws Exception {
				long startTimeArtefact = System.currentTimeMillis();
				AdaptedArtefact adaptedArtefact = AdaptedModelHelper.adapt(artefact, adapters, monitor);
				monitor.worked(1);
				// if (monitor.isCanceled()) { return adaptedModel; }
				AdaptedModelManager.registerTime("Adapt " + artefact.getName(), System.currentTimeMillis()
						- startTimeArtefact);
				return adaptedArtefact;
			}
		};
	}

}
