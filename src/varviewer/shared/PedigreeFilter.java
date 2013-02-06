package varviewer.shared;

import java.io.Serializable;
import java.util.List;

/**
 * A type of filter that excludes variants based on their presence and zygosity
 * in another sample. These are typically generated on the client, bundled with a 
 * VariantRequest, and the actual filtering is performed on the server
 * @author brendan
 *
 */
public class PedigreeFilter implements VariantFilter, Serializable {

	private PedigreeSample sample = null;
	private transient HasVariants relVars = null; //Don't try to serialize this
	private transient HasSamples varSource = null; //Dont serialize this, either
	
	public PedigreeFilter() {
		//required no-arg constructor
	}
	
	public PedigreeFilter(PedigreeSample sample) {
		this.sample = sample;
	}
	
	public String getPedSampleID() {
		return sample.getRelId();
	}
	
	public PedigreeSample getPedSample() {
		return sample;
	}
	/**
	 * Set the sample source that will be used to 
	 * @param source
	 */
	public void setVariantSource(HasSamples source) {
		this.varSource = source;
	}
	
	/**
	 * PedigreeFilters can also create a special annotation for Variants 
	 * @param vars
	 */
	public void applyAnnotations(List<Variant> vars) {
		if (relVars == null) {
			initializeRelVars();
		}
		
		for(Variant var : vars) {
			Variant relVar = relVars.getVariant(var.getChrom(), var.getPos());
			String relZyg = null;
			if (relVar != null) {
				relZyg = relVar.getAnnotation("zygosity");
				var.addAnnotation(sample.getRelId() + "-zygosity", relZyg);
			}
			else {
				var.addAnnotation(sample.getRelId() + "-zygosity", "Ref");
			}
		}
	}
	
	@Override
	public boolean variantPasses(Variant var) {
		if (relVars == null) {
			initializeRelVars();
		}
		
		Variant relVar = relVars.getVariant(var.getChrom(), var.getPos());
		String relZyg = null;
		if (relVar != null) {
			relZyg = relVar.getAnnotation("zygosity");
		}
		
		if (sample.getoType() == PedigreeSample.OperationType.EXCLUDE) {
			if (relVar == null)
				return true;
			
			if (sample.getzType() == PedigreeSample.ZygType.ALL) {
				//these are exclusions, so variant does NOT pass if relvar exists, it DOES pass if relvar is null
				return relVar == null;
			}
			
			if (sample.getzType() == PedigreeSample.ZygType.HETS) {
				//We want to exclude Hets, so return false if zygosity is het
				return !relZyg.equalsIgnoreCase("het");
			}
			if (sample.getzType() == PedigreeSample.ZygType.HOMS) {
				return !relZyg.equalsIgnoreCase("hom");
			}
		}
		
		if (sample.getoType() == PedigreeSample.OperationType.INTERSECT) {
			if (relVar == null)
				return false;
			
			if (sample.getzType() == PedigreeSample.ZygType.ALL) {
				//these are intersections, so variant passes only if relvar exists
				return relVar != null;
			}
			
			if (sample.getzType() == PedigreeSample.ZygType.HETS) {
				//Variant passes only if relZyg is a het
				return relZyg.equalsIgnoreCase("het");
			}
			if (sample.getzType() == PedigreeSample.ZygType.HOMS) {
				return relZyg.equalsIgnoreCase("hom");
			}
		}
		
		//When in doubt, it passes
		return true;
	}

	private void initializeRelVars() {
		if (varSource == null)
			throw new IllegalArgumentException("Variant Source not initialized for PedigreeFilter");
		relVars = varSource.getHasVariantsForSample(getPedSampleID());
	}

	public boolean equals(Object o) {
		if (o.getClass() != this.getClass()) {
			return false;
		}
		PedigreeFilter pf = (PedigreeFilter)o;
		return pf.getPedSample().equals(sample);
	}
}