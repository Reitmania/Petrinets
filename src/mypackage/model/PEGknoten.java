package mypackage.model;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/** Knoten des partiellen Erreichbarkeitsgraphen
*/
public class PEGknoten extends PetrinetObject {
	
	private int[] marken;
	
	private List<String> trans_ids = new ArrayList<String>();
	
	// Zuweisung von ID zu Markern zu einem bestimmten Schaltzeitpunkt
	private HashMap<String, Integer> peg_trans_path = new HashMap<String, Integer>(); 
	
    /** Konstruktor
     * @param name Name des Knoten
     * @param marken Array der Markierung
     * @param trans_ids geschaltete IDs bisher
    */
	public PEGknoten (String name, int[] marken, List<String> trans_ids) {
		super(name);
		this.trans_ids = trans_ids;
		this.marken = marken;
	}
	
    /** Hole Markierung von Knoten
     * @return this.marken Array der Markierung
    */
	public int[] getMarken() {
		return this.marken;
	}
	
    /** Hole die Quersumme
     * @return sum Quersumme der Markierung
    */
	public int getSum() {
	    int sum = 0;
	    for (int value : this.marken) {
	        sum += value;
	    }
	    return sum;
	}

}
