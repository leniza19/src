package mathchem.data;

public class MaterialItem {
	private Long idMtrl;
	private String nameMtrl;
	private String chemF;
	private String noteOfMtrl;
	private boolean observ;
	private boolean interm;
	private boolean source;
	private boolean product;
	private String noteOfReaction;
	
	public Long getIdMtrl() {
		return idMtrl;
	}
	public void setIdMtrl(Long idMtrl) {
		this.idMtrl = idMtrl;
	}
	public String getNameMtrl() {
		return nameMtrl;
	}
	public void setNameMtrl(String nameMtrl) {
		this.nameMtrl = nameMtrl;
	}
	public String getChemF() {
		return chemF;
	}
	public void setChemF(String chemF) {
		this.chemF = chemF;
	}
	public String getNoteOfMtrl() {
		return noteOfMtrl;
	}
	public void setNoteOfMtrl(String noteOfMtrl) {
		this.noteOfMtrl = noteOfMtrl;
	}
	public boolean isObserv() {
		return observ;
	}
	public void setObserv(boolean observ) {
		this.observ = observ;
	}
	public boolean isInterm() {
		return interm;
	}
	public void setInterm(boolean interm) {
		this.interm = interm;
	}
	public boolean isSource() {
		return source;
	}
	public void setSource(boolean source) {
		this.source = source;
	}
	public boolean isProduct() {
		return product;
	}
	public void setProduct(boolean product) {
		this.product = product;
	}
	public String getNoteOfReaction() {
		return noteOfReaction;
	}
	public void setNoteOfReaction(String noteOfReaction) {
		this.noteOfReaction = noteOfReaction;
	}
	
}
