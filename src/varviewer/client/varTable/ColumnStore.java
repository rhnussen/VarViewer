package varviewer.client.varTable;

import java.util.ArrayList;
import java.util.List;

import varviewer.shared.Variant;

import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;

/**
 * Maintains a list of all available Columns that can potentially be used in a ColumnModel
 * and VarTable. 
 * This is a singleton.
 * @author brendan
 *
 */
public class ColumnStore {

	private static List<VarAnnotation<?>> cols = new ArrayList<VarAnnotation<?>>();
	
	private static ColumnStore store;
	
	public static ColumnStore getStore() {
		if (store == null) {
			store = new ColumnStore();
		}
		
		return store;
	}
	
	/**
	 * Private constructor, get access to the store statically through ColumnStore.getStore()
	 */
	private ColumnStore() {
		initialize();
		store = this;
	}
	
	/**
	 * Obtain the column associated with the given key
	 * @param key
	 * @return
	 */
	public VarAnnotation<?> getColumnForID(String key) {
		for(VarAnnotation<?> col : cols) {
			if (col.id.equals(key)) {
				return col;
			}
		}
		return null;
	}
	
	/**
	 * Obtain a reference to a list of all potential columns
	 * @return
	 */
	public List<VarAnnotation<?>> getAllColumns() {
		return cols;
	}
	
	private void addColumn(VarAnnotation<?> col) {
		cols.add(col);
	}
	
	/**
	 * Creates all possible columns and stores them in a list here....
	 */
	private void initialize() {
		addColumn(new VarAnnotation<String>("gene", "Gene", new TextColumn<Variant>() {

			@Override
			public String getValue(Variant var) {
				String val = var.getAnnotation("gene");
				return val != null ? val : "-";
			}
		}, 1.0, false));

		addColumn(new VarAnnotation<String>("contig", "Chr", new TextColumn<Variant>() {

			@Override
			public String getValue(Variant var) {
				return var.getChrom();
			}
		}, 1.0, false));

		addColumn(new VarAnnotation<String>("pos", "Start", new TextColumn<Variant>() {

			@Override
			public String getValue(Variant var) {
				return "" + var.getPos();
			}
		}, 1.0, true));
		
		addColumn(new VarAnnotation<String>("zygosity", "Zygosity", new TextColumn<Variant>() {

			@Override
			public String getValue(Variant var) {
				String val = var.getAnnotation("zygosity");
			
				if (val == null || val.length()<2)
					return "?";
					
				return val;
			}
		}, 1.0, false));


		addColumn(new VarAnnotation<String>("exon.function", "Exon effect", new TextColumn<Variant>() {

			@Override
			public String getValue(Variant var) {
				String val = var.getAnnotation("exon.function");
				if (val == null || val.equals("-")) {
					val = var.getAnnotation("variant.type");
				}
				return val != null ? val : "-";
			}
		}, 2.0, false));

		addColumn(new VarAnnotation<String>("nm.number", "NM Number", new TextColumn<Variant>() {

			@Override
			public String getValue(Variant var) {
				String val = var.getAnnotation("nm.number");
				return val != null ? val : "-";
			}
		}, 2.0, false));

		addColumn(new VarAnnotation<String>("cdot", "c.dot", new TextColumn<Variant>() {

			@Override
			public String getValue(Variant var) {
				String val = var.getAnnotation("cdot");
				return val != null ? val : "-";
			}
		}, 2.0, false));

		addColumn(new VarAnnotation<String>("pdot", "p.dot", new TextColumn<Variant>() {

			@Override
			public String getValue(Variant var) {
				String val = var.getAnnotation("pdot");
				return val != null ? val : "-";
			}
		}, 2.0, false));

		addColumn(new VarAnnotation<String>("ref", "Ref.", new TextColumn<Variant>() {

			@Override
			public String getValue(Variant var) {
				return var.getRef();
			}
		}, 1.0, false));
		
		addColumn(new VarAnnotation<String>("alt", "Alt.", new TextColumn<Variant>() {

			@Override
			public String getValue(Variant var) {
				return var.getAlt();
			}
		}, 1.0, false));
		
		addColumn(new VarAnnotation<String>("quality", "Quality", new TextColumn<Variant>() {

			@Override
			public String getValue(Variant var) {
				String val = var.getAnnotation("quality");
				return val != null ? val : "-";
			}
		}, 1.0, true));

		addColumn(new VarAnnotation<String>("depth", "Depth", new TextColumn<Variant>() {

			@Override
			public String getValue(Variant var) {
				String val = var.getAnnotation("depth");
				return val != null ? val : "-";
			}
		}, 1.0, true));
		
		addColumn(new VarAnnotation<String>("var.freq", "Alt. Freq", new TextColumn<Variant>() {

			@Override
			public String getValue(Variant var) {
				String tot = var.getAnnotation("depth");
				String alt = var.getAnnotation("var.depth");
				if (tot != null && alt != null) {
					try {
						Double t = Double.parseDouble(tot);
						Double a = Double.parseDouble(alt);
						double freq = a / t;
						String freqStr = "" + freq;
						if (freqStr.length() > 5) {
							freqStr = freqStr.substring(0, 4);
						}
						return freqStr;
					}
					catch (NumberFormatException nfe) {
						
					}
					catch (RuntimeException ex) {
						
					}
				}
				return "-";
			}
		}, 1.0, true));

		addColumn(new VarAnnotation<String>("pop.freq", "Pop. Freq.", new TextColumn<Variant>() {

			@Override
			public String getValue(Variant var) {
				String val = var.getAnnotation("pop.freq");
				if (val.equals("-"))
					val = "0";
				return val != null ? val : "0";
			}
		}, 1.0, true));
		
		addColumn(new VarAnnotation<String>("arup.freq", "ARUP Freq.", new TextColumn<Variant>() {

			@Override
			public String getValue(Variant var) {
				String val = var.getAnnotation("ARUP.freq");
				if (val.equals("-"))
					val = "0";
				return val != null ? val : "0";
			}
		}, 2.0, true));

		
		addColumn(new VarAnnotation<String>("sift.score", "SIFT score", new TextColumn<Variant>() {

			@Override
			public String getValue(Variant var) {
				String val = var.getAnnotation("sift.score");
				return val != null ? val : "NA";
			}
		}, 1.0, false));
		
		addColumn(new VarAnnotation<String>("mt.score", "MutationTaster score", new TextColumn<Variant>() {

			@Override
			public String getValue(Variant var) {
				String val = var.getAnnotation("mt.score");
				return val != null ? val : "NA";
			}
		}, 1.0, false));
		
		addColumn(new VarAnnotation<String>("gerp.score", "GERP++ score", new TextColumn<Variant>() {

			@Override
			public String getValue(Variant var) {
				String val = var.getAnnotation("gerp.score");
				return val != null ? val : "NA";
			}
		}, 1.0, false));
		
		addColumn(new VarAnnotation<SafeHtml>("rsnum", "dbSNP #", new Column<Variant, SafeHtml>(new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(Variant var) {
				SafeHtmlBuilder bldr = new SafeHtmlBuilder();
				String val = var.getAnnotation("rsnum");
				if (val == null || val.length() < 2) {
					bldr.appendEscaped("-");
				}
				else {
					bldr.appendHtmlConstant("<a href=\"http://www.ncbi.nlm.nih.gov/projects/SNP/snp_ref.cgi?rs=1063736\" target=\"_blank\">" + val + "</a>" );
				}
				return bldr.toSafeHtml();
			}
			
		}, 1.0, false));
		
		addColumn(new VarAnnotation<String>("pp.score", "PolyPhen-2 score", new TextColumn<Variant>() {

			@Override
			public String getValue(Variant var) {
				String val = var.getAnnotation("pp.score");
				return val != null ? val : "NA";
			}
		}, 1.0, false));
		
		addColumn(new VarAnnotation<String>("omim.num", "OMIM #", new TextColumn<Variant>() {

			@Override
			public String getValue(Variant var) {
				String val = var.getAnnotation("omim.disease.ids");
				return val != null ? val : "0";
			}
		}, 2.0, false));
		
//		addColumn(new VarAnnotation<ImageResource>("omim.disease.pic", "OMIM Disease", new Column<Variant, ImageResource>(new ImageResourceCell()) {
//
//			@Override
//			public ImageResource getValue(Variant var) {
//				String str = var.getAnnotation("omim.disease");
//				if (str != null && str.length() > 3)
//					return resources.omimImage();
//				return null;
//			}
//			
//		}, 1.0, false));
//		
//		addColumn(new VarAnnotation<ImageResource>("dbnsfp.info", "HGMD gene hit", new Column<Variant, ImageResource>(new ImageResourceCell()) {
//
//			@Override
//			public ImageResource getValue(Variant var) {
//				String str = var.getAnnotation("hgmd.info");
//				if (str != null && str.length() > 3)
//					return resources.hgmdImage();
//				return null;
//			}
//			
//		}, 1.0, false));
//		
//		addColumn(new VarAnnotation<ImageResource>("hgmd.exact.match", "HGMD exact hit", new Column<Variant, ImageResource>(new ImageResourceCell()) {
//
//			@Override
//			public ImageResource getValue(Variant var) {
//				String str = var.getAnnotation("hgmd.info");
//				if (str != null && str.length() > 3)
//					return resources.hgmdHitImage();
//				return null;
//			}
//			
//		}, 1.0, false));
		
		addColumn(new VarAnnotation<ImageResource>("disease.pics", "HGMD & OMIM", new Column<Variant, ImageResource>(new ImageResourceCell()) {

			@Override
			public ImageResource getValue(Variant var) {
				String hgmdExact = var.getAnnotation("hgmd.hit");
				String hgmdGeneMatch = var.getAnnotation("hgmd.info");
				String omimGeneMatch = var.getAnnotation("omim.disease");
				
				boolean hasHGMDExact = hgmdExact != null && hgmdExact.length() > 3;
				boolean hasHGMDGene = hgmdGeneMatch != null && hgmdGeneMatch.length() > 3;
				boolean hasOmim = omimGeneMatch != null && omimGeneMatch.length() > 3;
				
				ImageResource img = null;
				if (hasHGMDExact) {
					if (hasOmim) {
						//Has all 3
						img = resources.hgmdHitHgmdOmimImage();
					}
					else {
						//No omim, but has hgmd exact and gene match
						img = resources.hgmdHitHgmdImage();
					}
				}
				else {
					//No exact hit
					if (hasHGMDGene) {
						if (hasOmim) {
							img = resources.hgmdOmimImage();
						}
						else {
							//No OMIM, just hgmd gene match
							img = resources.hgmdOnlyImage();
						}
					}
					else {
						if (hasOmim) {
							img = resources.omimOnlyImage();
						}
						else {
							//nothing, img is null
						}
					}
				}

				return img;
			}
			
		}, 1.0, false));
	}
	
	VarPageResources resources = (VarPageResources) GWT.create(VarPageResources.class);
	
	
	
	
}
