package org.but4reuse.adapters.eclipse.generator.utils;

public interface ISender {
	void addListener(IListener listener);
	void sendToAll(String msg);
	void sendToOne(String msg, IListener listener);
}
