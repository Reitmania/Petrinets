package mypackage.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** Repräsentiert ein Petrinetz.
 * @author Stefan Reitmann
*/
public class Petrinet extends PetrinetObject {
	
	private HashMap<String, PetrinetObject> hash_map = new HashMap<String, PetrinetObject>(); 
	
	private List<Place> places = new ArrayList<Place>();
	private List<Transition> transitions = new ArrayList<Transition>();
	private List<Arc> arcs = new ArrayList<Arc>();
	
	private PEG peg;
	
	public Petrinet(String name) {
		super(name);
	}
	
    public void buildPEG() {
    	// Leite Erreichbarkeitsgraph ab
    	peg = new PEG("testPEG");
    	peg.buildPEG(this);
    }
	
    /** Fügt ein neues Objekt, abgeleitet von Petrinetobject, in das Netz ein
     * @param o eine Stelle, Transition oder Kante
    */
	public void add(PetrinetObject o) {
		if(o instanceof Place) {
			hash_map.put(o.getID(),(Place) o);
			places.add((Place) o);
		} else if (o instanceof Transition) {
			hash_map.put(o.getID(),(Transition) o);
			transitions.add((Transition) o);
		} else if (o instanceof Arc) {
			hash_map.put(o.getID(),(Arc) o);
			arcs.add((Arc) o);
		}
	}
	
    /** Ändert den Namen eines Elements
     * @param id ID des Elements
     * @param name Name des Elements
    */
	public void setElementName(String id, String name) {
		hash_map.get(id).setName(name);
	}
	
    /** Ändert die Marker eines Elements
     * @param id ID des Elements
     * @param token Anzahl der Marker
    */
	public void setElementTokens(String id, Integer token) {
		if(hash_map.get(id) instanceof Place) {
			Place pl = (Place) hash_map.get(id);
			pl.setInitToken(token);
		}
	}
	
    /** Ändert die Position eines Elements
     * @param id ID des Elements
     * @param x x-Koordinate
     * @param y y-Koordinate
    */
	public void setElementPosition(String id, Integer x, Integer y) {
		if(hash_map.get(id) instanceof Place) {
			Place pl = (Place) hash_map.get(id);
			pl.setPosition(x, y);
		} else if (hash_map.get(id) instanceof Transition) {
			Transition tr = (Transition) hash_map.get(id);
			tr.setPosition(x, y);
		} 
	}
	
	public List<Place> getPlaces() {
        return places;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public List<Arc> getArcs() {
        return arcs;
    }
    
    /** Ändert die Marker eines Elements bei Klick auf + oder -
     * @param id ID des Elements
     * @param marker_change Anzahl der Marker
    */
    public void setMarker(String id, int marker_change) {
    	if(hash_map.get(id) instanceof Place) {
    		Place pl = (Place) hash_map.get(id);
    		//(Place) hash_map.setToken(2);
    		pl.setNewToken(pl.getTokens() + marker_change);
    		//hash_map.replace(id,pl);
    		pl = (Place) hash_map.get(id);
    		System.out.println("Neue Tokens: " + pl.getTokens().toString());    		
    	} else {
    		System.out.println("Achtung: kein Place!");
    	}
    }
    
    /** Leitet aus Kanten die Vorgänger und Nachfolger für Transitionen ab
    */
    public void Arc2Trans() {
    	for(Arc element : arcs) {
    		if(hash_map.get(element.getSourceID()) instanceof Transition) {
    			Transition trans = (Transition) hash_map.get(element.getSourceID());
    			trans.addTargetID(element.getTargetID());
    		} else if (hash_map.get(element.getTargetID()) instanceof Transition) {
    			Transition trans = (Transition) hash_map.get(element.getTargetID());
    			trans.addSourceID(element.getSourceID());
    		}
     	}
    }
    
    /** Liefert ein Element es Netzes
     * @param id ID des Elements
     * @return this.hash_map.get(id) PetrinetObject 
    */
    public PetrinetObject getPetrinetObject(String id) {
    	return this.hash_map.get(id);
    }   
    
    /** Liefert ein Array aller Marker
     * @return marken Array an Marken
    */
    public int[] getMarker() {
		int[] marken = new int[places.size()];
		for(int i = 0; i < places.size(); i++) {
			marken[i] = places.get(i).getTokens();
		}
		return marken;
    }
    
    public PEG getPEG() {
    	return this.peg;
    }
    
    public HashMap<String, PetrinetObject> getHashMap(){
    	return this.hash_map;
    }
    
    /** Schaltet eine Transition
     * @param id ID des Elements
    */
    public void activateTrans(String id) {
    	Transition trans = (Transition) hash_map.get(id);  	
    	trans.setActive(trans.canActivate(this), this);
    }
    
    /** Reset des Netzes auf aktuelle Anfangsmarkierung
    */
    public void reset() {
    	for(Place place : places) {
    		place.reset();
    	}
    	peg.reset();
    	//this.peg.getTransIDs().clear();
    	/*
    	for (PetrinetObject value : hash_map.values()) {
    	    if(value instanceof Place) {
    	    	((Place) value).reset();
    	    }
    	}
    	*/
    }
    
    /** Setzt zu einer bestimmten Schaltsequenz zurück
     * @param path Schaltsequenz
    */
    public void resetToID(List<String> path) {
    	reset();
    	
    	for(String id : path) {
    		Transition trans = (Transition) this.getPetrinetObject(id);
    		// Schalte alle oldtransIDs
    		this.activateTrans(id);
			// update pEG
			if(trans.getStatus()) {
				this.getPEG().addTrans(id, this.getMarker());
			}
    	} 
    	//this.peg.setTransIDs(oldtransIDs);    	
    }
    
    /** Kompletter Reload
    */
    public void reload() {
    	// neuladen des Netzes
    	for(Place place : places) {
    		place.reload();
    	}
    	peg.reset();
    }
    
    
    
    public void sortTrans() {
    	
    }
    
    public String getNextTrans(String id) {
    	// finde die nachfolgende Trans von id
    	
    	for(Transition trans : transitions) {
    		if(trans.getSourceIDs().contains(id)) {
    			return trans.getID();
    		}
    	}
    	return "";
    }
    
    // -------------------------------------------------
	
	
	public Place place(String name) {
		Place p = new Place(name);
        places.add(p);
        return p;
    }
	
    public Place place(String name, int init_token) {
        Place p = new Place(name, init_token);
        places.add(p);
        return p;
    }
    
    public Arc arc(String id, PetrinetObject s, PetrinetObject t) {
        Arc arc = new Arc(id, s, t);
        arcs.add(arc);
        return arc;
    }
    


	
}
