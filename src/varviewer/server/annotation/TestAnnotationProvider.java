package varviewer.server.annotation;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import varviewer.shared.variant.Variant;

public class TestAnnotationProvider implements AnnotationProvider {

	List<String> annos = Arrays.asList(new String[]{"some.annotation", "another.annotation"});
	
	public TestAnnotationProvider() {
		//Some test data
//		annoMap.put(1, new Annotation[]{null, new Annotation(5.0)});
//		annoMap.put(2, new Annotation[]{new Annotation("Something2"), new Annotation(17.0)});
//		annoMap.put(6, new Annotation[]{new Annotation("Something6"), new Annotation(32.0)});
//		annoMap.put(8, new Annotation[]{new Annotation("Something8"), new Annotation(14.2)});
//		annoMap.put(11, new Annotation[]{new Annotation("Something11"), null});
	}
	
	@Override
	public Collection<String> getAnnotationsProvided() {
		return annos;
	}

	@Override
	public void annotateVariant(Variant var, AnnotationKeyIndex[] annotationKeys) {
//		Annotation[] annos = getAnnosForPosition(var.getChrom(), var.getPos());
//		if (annos == null) {
//			return;
//		}
//		for(int i=0; i<annotationKeys.length; i++) {
//			if (annotationKeys[i] != null)
//				var.addAnnotation(annotationKeys[i].getKey(), annos[annotationKeys[i].getIndex()]);
//		}
	}
	
	@Override
	public AnnotationKeyIndex[] getKeyIndices(List<String> annotations) {
		AnnotationKeyIndex[] indices = new AnnotationKeyIndex[annotations.size()];
		for(int i=0; i<annotations.size(); i++) {
			int annoIndex = annos.indexOf(annotations.get(i));
			if (annoIndex > -1) {
				indices[i] = new TestKeyIndex(annotations.get(i), annoIndex);
			}
		}
		return indices;
	}

	class TestKeyIndex implements AnnotationKeyIndex {

		final String key;
		final int index;
		
		TestKeyIndex(String key, int index) {
			this.key = key;
			this.index = index;
		}
		
		@Override
		public String getKey() {
			return key;
		}

		@Override
		public int getIndex() {
			return index;
		}

		@Override
		public boolean isNumeric() {
			return false;
		}
		
	}
	

}
