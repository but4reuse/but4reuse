package org.but4reuse.adapters.sourcecode;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.sourcecode.adapter.LanguageConfigurator;
import org.but4reuse.adapters.sourcecode.featurehouse.de.ovgu.cide.fstgen.ast.FSTTerminal;

public class FSTTerminalNodeElement extends FSTNodeElement {

	@Override
	public String getText() {
		return getName() + " " + getType();
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (!(anotherElement instanceof FSTTerminalNodeElement))
			return 0;

		FSTTerminalNodeElement terminalCP = (FSTTerminalNodeElement) anotherElement;

		if (LanguageConfigurator.getLanguage().isMethod(this.getNode())
				|| LanguageConfigurator.getLanguage().isConstructor(this.getNode())) {

			if (LanguageConfigurator.getLanguage().isMethod(terminalCP.getNode())
					|| LanguageConfigurator.getLanguage().isConstructor(terminalCP.getNode())) {
				FSTTerminal sftn1 = (FSTTerminal) this.getNode();
				FSTTerminal sftn2 = (FSTTerminal) terminalCP.getNode();

				if (sftn1.getName().equals(sftn2.getName())) {
					return 1;
				} else {
					return 0;
				}
			}
		}
		if (this.getNode().getName().equals(terminalCP.getNode().getName())
				&& this.getNode().getType().equals(terminalCP.getNode().getType())) {
			return 1;
		} else {
			return 0;
		}
	}

}
