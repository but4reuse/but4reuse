package org.but4reuse.adapters.eclipse.generator.interfaces;

public interface ISender {
	void addListener(IListener listener);

	void sendToAll(String msg);
}
