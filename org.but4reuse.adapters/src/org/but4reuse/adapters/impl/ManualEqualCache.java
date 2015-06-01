package org.but4reuse.adapters.impl;

import java.util.HashMap;
import java.util.Map;

import org.but4reuse.adapters.IElement;

public class ManualEqualCache {

	public static Map<CacheEntry, Boolean> cacheEntries = new HashMap<CacheEntry, Boolean>();

	public static Boolean check(IElement element1, IElement element2) {
		CacheEntry cacheEntry = new CacheEntry(element1, element2);
		return cacheEntries.get(cacheEntry);
	}

	public static void add(IElement element1, IElement element2, boolean equals) {
		CacheEntry cacheEntry1 = new CacheEntry(element1, element2);
		CacheEntry cacheEntry2 = new CacheEntry(element2, element1);
		cacheEntries.put(cacheEntry1, equals);
		cacheEntries.put(cacheEntry2, equals);
	}

	public static void clearCache() {
		cacheEntries.clear();
	}

	public static class CacheEntry {
		IElement element1;
		IElement element2;

		public CacheEntry(IElement element1, IElement element2) {
			this.element1 = element1;
			this.element2 = element2;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof CacheEntry) {
				CacheEntry ce = (CacheEntry) o;
				if (ce.element1 == element1 && ce.element2 == element2) {
					return true;
				}
			}
			return super.equals(o);
		}

		@Override
		public int hashCode() {
			return element1.hashCode() + element2.hashCode();
		}
	}

}
