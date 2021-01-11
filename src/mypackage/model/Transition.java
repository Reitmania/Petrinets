package mypackage.model;

import java.util.List;
import java.util.ArrayList;

/** Klasse für Transition
*/
public class Transition extends PetrinetObject {

	private int x;
	private int y;
	
	private List<String> sourceIDs = new ArrayList<String>();
	private List<String> targetIDs = new ArrayList<String>();
	
	private boolean active = false;
	
    /** neue Transition
     * @param name Name des Elements
    */
	public Transition(String name) {
		super(name);
	}
	
    /** setze Position
     * @param x x-Koordinate
     * @param y y-Koordinate
    */
	public void setPosition(Integer x, Integer y) {
		this.x = x;
		this.y = y;
	}
	
    /** Getter für x
     * @return this.x x-Koordinate
    */
	public Integer getX() {
		return this.x;
	}
    /** Getter für y
     * @return this.y y-Koordinate
    */
	public Integer getY() {
		return this.y;
	}
    /** Getter für Status
     * @return this.active Aktivitätsstatus
    */
	public boolean getStatus() {
		return this.active;
	}
	
    /** Prüfung, ob Transition geschaltet werden kann
     * @param petrinet aktuelles Petrinetz
     * @return true Kann geschaltet werden och nicht
    */
	public boolean canActivate(Petrinet petrinet) {
    	for(String source : this.getSourceIDs()) {
    		if(petrinet.getPetrinetObject(source) instanceof Place) {
    			Place place = (Place) petrinet.getPetrinetObject(source);
    			if(!place.hasAtLeastTokens(1)) {
    				return(false);
    			}
    		}
    	}
    	return true;
	}
	
    /** Setze Transition aktiv
     * @param petrinet aktuelles Petrinetz
     * @status Status, auf den gesetzt wird
    */
	public void setActive(boolean status, Petrinet petrinet) {
		this.active = status;
		
		if(status) {
			// Marker der Sources reduzieren
			for(String source : this.getSourceIDs()) {
	    		if(petrinet.getPetrinetObject(source) instanceof Place) {
	    			Place place = (Place) petrinet.getPetrinetObject(source);
	    			place.setToken(place.getTokens() - 1);
	    		}
	    	}
			// Marker der Targets erhöhen
			for(String source : this.getTargetIDs()) {
	    		if(petrinet.getPetrinetObject(source) instanceof Place) {
	    			Place place = (Place) petrinet.getPetrinetObject(source);
	    			place.setToken(place.getTokens() + 1);
	    		}
	    	}
		}
	}
	
    /** füge neues Ziel hinzu
     * @param ID ID des Ziels
    */
	public void addTargetID(String ID) {
		this.targetIDs.add(ID);
	}
    /** füge neue Quelle hinzu
     * @param ID ID der Quelle
    */
	public void addSourceID(String ID) {
		this.sourceIDs.add(ID);
	}
    /** gib Ziele zurück
     * @return this.targetIDs IDs der Ziele
    */
	public List<String> getTargetIDs(){
		return this.targetIDs;
	}
    /** gib Quellen zurück
     * @return this.sourceIDs IDs der Quellen
    */
	public List<String> getSourceIDs(){
		return this.sourceIDs;
	}
}
