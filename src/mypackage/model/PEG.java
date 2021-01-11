package mypackage.model;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

/** (partieller) Erreichbarkeitsgraph
*/
public class PEG extends PetrinetObject {
	
	private int MAX_SUM = -1;
	private int INIT_SUM = -1;
	
	private boolean unbeschr = false;
	
	// Liste meiner aktivieren Transitions
	private List<String> trans_ids = new ArrayList<String>();
	private List<List<String>> gone_trans_ids = new ArrayList<List<String>>();
	
	// Hashmap der Liste zum jeweiligen Knoten (z. B. t1 - t2 : [1,0,0])
	private HashMap<List<String>, PEGknoten> peg_hash_map = new HashMap<List<String>, PEGknoten>(); 
	// Hashmap der uniquen Markierungen
	private HashMap<int[], PEGknoten> peg_hash_map_markierung = new HashMap<int[], PEGknoten>();
	
	private List<PEGknoten> knoten = new ArrayList<PEGknoten>();		
	
	public PEG(String name) {
		super(name);
	}
	
    /** Konstruktor
     * @param petrinet aktuelles Petrinetz
    */
	public void buildPEG(Petrinet petrinet) {
		// Einlesen der Listen aller Teilelemente des Netzes
		addTrans("t0", petrinet.getMarker());
		
		//str = str.replaceAll("\\D+","");
		//marken = new int[places.size()];
	}
	
	public List<PEGknoten> getKnoten(){
		return this.knoten;
	}
	
    /** Schalten einer Transition
     * @param id ID des Elements
     * @param marken Array der Markierung
    */
	public void addTrans(String id, int[] marken) {
		// check, ob die Trans schon aktiv ist
		
		// Transition hinzufügen und schalten
		this.trans_ids.add(id);		
		List<String> trans_ids_ref = new ArrayList<String>();
		for(String element : trans_ids) {
			trans_ids_ref.add(element);
		}		
		
		PEGknoten new_knoten = new PEGknoten(id, marken, trans_ids_ref);
		peg_hash_map.put(trans_ids_ref, new_knoten);
		if(!peg_hash_map_markierung.containsKey(marken)) {
			peg_hash_map_markierung.put(marken, new_knoten);
		}
		this.knoten.add(new_knoten);
		checkMax(new_knoten);
	}
	
    /** Max-Check für Beschränktheitsalgorithmus
     * @param new_knoten neuer gesetzter PEGknoten
    */
	private void checkMax(PEGknoten new_knoten) {
		for(PEGknoten knoten : this.knoten) {
			if(this.knoten.size() == 1) {
				INIT_SUM = new_knoten.getSum();
			}
			
			if(MAX_SUM == -1 && this.knoten.size() > 1) {
				MAX_SUM = new_knoten.getSum();
			} else if (MAX_SUM < new_knoten.getSum() && this.knoten.size() > 1) {
				this.unbeschr = true;
			}
		}
		
		if(this.unbeschr) {
			System.out.println("unbeschränkt!");
		}
			
	}
	
	public List<List<String>> getGoneIDs(){
		return this.gone_trans_ids;
	}
	
    /** Checke Beschränktheitsstatus
     * @return this.unbeschr Bool für Beschränktheit
    */
	public boolean getStatus() {
		return this.unbeschr;
	}
	
    /** Hole gelaufene IDs
     * @return this.trans_ids Gelaufene IDs
    */
	public List<String> getTransIDs(){
		return this.trans_ids;
	}
	
    /** setze auf vorherige TransIDs
     * @param oldTransIDs vorherige TransIDs
    */
	public void setTransIDs(List<String> oldTransIDs) {
		this.trans_ids = oldTransIDs;
	}
	
    /** reset
    */
	public void reset() {
		MAX_SUM = -1;
		//this.peg_hash_map_markierung.clear();
	}
	
	
	public HashMap<List<String>, PEGknoten> getPEGHash(){
		return peg_hash_map;
	}
	
	public HashMap<int[], PEGknoten> getPEGHashMarkierung(){
		return peg_hash_map_markierung;
	}
	
}
