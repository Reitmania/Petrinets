package mypackage.model;

/** Klasse für Kanten
*/
public class Arc extends PetrinetObject {
	
	private PetrinetObject source;
	private PetrinetObject target;
	
	private String sourceID;
	private String targetID;
	
	protected Arc(String id) {
		super(id);
	}
	
    /** Konstruktor
     * @param id ID des Elements
     * @param source Vorgänger ID
     * @param target Nachfolger ID
    */
	protected Arc(String id, String source, String target) { 
		this(id);
		this.sourceID = source;
		this.targetID = target;
	} 
	
    /** Konstruktor
     * @param id ID des Elements
     * @param source Vorgängerobject
     * @param target Nachfolgerobject
    */
	protected Arc(String id, PetrinetObject source, PetrinetObject target) { 
		this(id);
		if(source instanceof Place) {
			this.source = (Place) source;
		} else if (source instanceof Transition) {
			this.source = (Transition) source;
		}
		if(target instanceof Place) {
			this.target = (Place) target;
		} else if (target instanceof Transition) {
			this.target = (Transition) target;
		}
	} 
    /** gib Ziel zurück
     * @return sourceID ID des Ziels
    */
	public String getSourceID() {
		return sourceID;
	}
    /** gib Ziele zurück
     * @return targetID ID des Ziels
    */
	public String getTargetID(){
		return targetID;
	}	
    /** gib Quellobjekt zurück
     * @return source Quellobjekt
    */
	public PetrinetObject getSource() {
		return source;
	}
    /** gib Zielobjekt zurück
     * @return target Zielobjekt
    */
	public PetrinetObject getTarget() {
		return target;
	}

}
