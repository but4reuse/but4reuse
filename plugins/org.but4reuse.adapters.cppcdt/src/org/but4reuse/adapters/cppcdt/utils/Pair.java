package org.but4reuse.adapters.cppcdt.utils;

/**
 * This class contains a basic pair implementation.
 * 
 * @author sandu.postaru
 *
 * @param <F>
 * @param <S>
 */

public class Pair<F, S> {

	public final F first;
	public final S second;

	public Pair(F first, S second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public String toString() {
		return "Pair [first=" + first + ", second=" + second + "]";
	}
}
