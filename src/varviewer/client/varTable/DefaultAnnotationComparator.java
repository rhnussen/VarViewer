package varviewer.client.varTable;

import java.util.Comparator;

import varviewer.shared.Variant;

public class DefaultAnnotationComparator implements Comparator<Variant> {
	public final String key;
	
	public DefaultAnnotationComparator(String key) {
		this.key = key;
	}

	@Override
	public int compare(Variant v0, Variant v1) {
		if (v0 == v1) {
			return 0;
		}
		
		String a0 = v0.getAnnotation(key);
		String a1 = v1.getAnnotation(key);
		
		if (a0 == null && a1 == null) {
			return 0;
		}
		if (a0 == null && a1 != null) {
			return 1;
		}
		if (a1 == null && a0 != null) {
			return -1;
		}
		System.out.println("String comparing " + a0 + " to " + a1 + " : " + a0.compareTo(a1));
		return a0.compareTo(a1);
	}
}