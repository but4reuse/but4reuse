package org.but4reuse.adapters.ui.actions;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.but4reuse.adapters.ui.xmlgenerator.SaveDataUtils;
import org.but4reuse.utils.ui.dialogs.ScrollableMessageDialog;
import org.eclipse.contribution.visualiser.views.Menu;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class ConstructXMLAction implements IViewActionDelegate {

	Menu menu;

	@Override
	public void run(IAction action) {
		File file = SaveDataUtils.saveDataInFile();
		try {
			String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), "UTF-8");
			// Show in dialog xml file content
			ScrollableMessageDialog dialog = new ScrollableMessageDialog(Display.getCurrent().getActiveShell(),
					"Reuse Analysis: reuseAnalysis/"+file.getName(), "", content);
			dialog.open();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {

	}

	@Override
	public void init(IViewPart view) {
		menu = (Menu) view;
	}
	
}
