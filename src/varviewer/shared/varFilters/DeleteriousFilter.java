package varviewer.shared.varFilters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import varviewer.shared.variant.AnnotationIndex;
import varviewer.shared.variant.Variant;
import varviewer.shared.variant.VariantFilter;

public class DeleteriousFilter implements VariantFilter, Serializable {

	double siftMax = 1.0;
	double polyphenMin = 0.0;
	double mutationTasterMin = 0.0;
	double gerpMin = -10.0;
	double phyloPMin = -10.0;
	double combinedMin = 0.0;
	
	boolean siftEnabled = false;
	boolean polyphenEnabled = false;
	boolean mutationTasterEnabled = false;
	boolean gerpEnabled = false;
	boolean phyloPEnabled = false;
	boolean combinedEnabled = false;
	
	private AnnotationIndex index = null;
	private int siftIndex = -1;
	private int polyphenIndex = -1;
	private int mtIndex = -1;
	private int gerpIndex = -1;
	private int phylopIndex = -1;
	private int combinedIndex = -1;
	
	public void setAnnotationIndex(AnnotationIndex index) {
		this.index = index;
		if (index != null) {
			siftIndex = index.getIndexForKey("sift.score");
			polyphenIndex = index.getIndexForKey("pp.score");
			mtIndex = index.getIndexForKey("mt.score");
			gerpIndex = index.getIndexForKey("gerp.score");
			phylopIndex = index.getIndexForKey("phylop.score");
			combinedIndex = index.getIndexForKey("svm.effect.prediction");
		}
	}
	
	public boolean isSiftEnabled() {
		return siftEnabled;
	}

	public void setSiftEnabled(boolean siftEnabled) {
		this.siftEnabled = siftEnabled;
	}

	public boolean isPolyphenEnabled() {
		return polyphenEnabled;
	}

	public void setPolyphenEnabled(boolean polyphenEnabled) {
		this.polyphenEnabled = polyphenEnabled;
	}

	public boolean isMutationTasterEnabled() {
		return mutationTasterEnabled;
	}

	public void setMutationTasterEnabled(boolean mutationTasterEnabled) {
		this.mutationTasterEnabled = mutationTasterEnabled;
	}

	public boolean isGerpEnabled() {
		return gerpEnabled;
	}

	public void setGerpEnabled(boolean gerpEnabled) {
		this.gerpEnabled = gerpEnabled;
	}

	public boolean isPhyloPEnabled() {
		return phyloPEnabled;
	}

	public void setPhyloPEnabled(boolean phyloPEnabled) {
		this.phyloPEnabled = phyloPEnabled;
	}

	public boolean isCombinedEnabled() {
		return combinedEnabled;
	}

	public void setCombinedEnabled(boolean combinedEnabled) {
		this.combinedEnabled = combinedEnabled;
	}

	public double getSiftMax() {
		return siftMax;
	}

	public void setSiftMax(double siftMax) {
		this.siftMax = siftMax;
	}

	public double getPolyphenMin() {
		return polyphenMin;
	}

	public void setPolyphenMin(double polyphenMin) {
		this.polyphenMin = polyphenMin;
	}

	public double getMutationTasterMin() {
		return mutationTasterMin;
	}

	public void setMutationTasterMin(double mutationTasterMin) {
		this.mutationTasterMin = mutationTasterMin;
	}

	public double getGerpMin() {
		return gerpMin;
	}

	public void setGerpMin(double gerpMin) {
		this.gerpMin = gerpMin;
	}

	public double getPhyloPMin() {
		return phyloPMin;
	}

	public void setPhyloPMin(double phyloPMin) {
		this.phyloPMin = phyloPMin;
	}

	public double getCombinedMin() {
		return combinedMin;
	}

	public void setCombinedMin(double combinedMin) {
		this.combinedMin = combinedMin;
	}

	@Override
	public boolean variantPasses(Variant var) {
	
		//SIFT score
		boolean passes = true;
		if (siftEnabled) {
			passes = checkLess(var, siftIndex, siftMax);
			if (passes) {
				return true;
			}
		}
		
		if (polyphenEnabled) {
			passes = checkMore(var, polyphenIndex, polyphenMin);
			if (passes) {
				return true;
			}
		}
		
		if (mutationTasterEnabled) {
			passes = checkMore(var, mtIndex, mutationTasterMin);
			if (passes) {
				return true;
			}
		}
		
		if (gerpEnabled) {
			passes = checkMore(var, gerpIndex, gerpMin);
			if (passes) {
				return true;
			}
		}
		
		if (phyloPEnabled) {
			passes = checkMore(var, phylopIndex, phyloPMin);
			if (passes) {
				return true;
			}
		}
		
		if (combinedEnabled) {
			passes = checkMore(var, combinedIndex, combinedMin);
			if (passes) {
				return true;
			}
		}
		
		//If no filters are enabled (the default) it passes
		//Otherwise it failed every enabled filter, so it fails overall. 
		if (siftEnabled
				|| polyphenEnabled
				|| gerpEnabled
				|| mutationTasterEnabled
				|| phyloPEnabled
				|| combinedEnabled) {
			return false;
		}
		return true;
	}
	
	private static boolean checkLess(Variant var, int index, double max) {
		Double score = var.getAnnotationDouble(index);
		return score != null ? score < max : false; 
	}
	
	private static boolean checkMore(Variant var, int index, double max) {
		Double score = var.getAnnotationDouble(index);
		return score != null ? score > max : false; 
	}

	@Override
	public String getUserDescription() {
		StringBuilder str = new StringBuilder("Variants with ");
		List<String> bits = new ArrayList<String>();
		
		if (isSiftEnabled()) {
			bits.add("SIFT > " + getSiftMax() );
		}
		if (isGerpEnabled()) {
			bits.add("GERP++ < " + getGerpMin());
		}
		if (isPhyloPEnabled()) {
			bits.add("PhyloP < " + getPhyloPMin());
		}
		if (isPolyphenEnabled()) {
			bits.add("Polyphen-2 < " + getPolyphenMin());
		}
		if (isMutationTasterEnabled()) {
			bits.add("MutationTaster < " + getMutationTasterMin());
		}
		
		if (bits.size()==0) {
			return "No filtering by predicted deleteriousness was performed.";
		}
		else {
			str.append(bits.get(0));
			for(int i=1; i<bits.size()-1; i++) {
				str.append(", " + bits.get(i));
			}
			str.append(" and " + bits.get( bits.size()-1));
		}
		str.append(" were excluded.");
		return str.toString();
	}
	
}
