package mypackage.view;

import java.util.HashMap;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Element;
import org.graphstream.graph.Node;

import mypackage.model.Arc;
import mypackage.model.Petrinet;
import mypackage.model.PetrinetObject;
import mypackage.model.Place;
import mypackage.model.Transition;

/** Klasse für den Graphen zum aktuellen Petrinetz
*/
public class PetriGraph extends MyGraph {
	
	// HashMap für ID - Node Zuweisungen
	private HashMap<String, Element> graph_hash_map = new HashMap<String, Element>(); 
	
    /** Baut den Graphen für das aktuelle Petrinetz auf
     * @param petrinet aktuelles Petrinetz
    */
	public void buildGraph(Petrinet petrinet) {
			
		// Stelle Petrinet als Graph dar
		System.out.println("Geladenes Petrinetz: " + petrinet.getID());		
		
		// Einlesen der Listen aller Teilelemente des Netzes
		List<Place> places = petrinet.getPlaces();
		List<Arc> arcs = petrinet.getArcs();
		List<Transition> trans = petrinet.getTransitions();				
		
		for(Place element : places) {			
			Node node = this.addNode(element.getID());
			node.addAttribute("ui.label", "["+element.getID()+"] "+element.getName() + " <" + element.getTokens() + ">");
			node.addAttribute("xy", element.getX(), -element.getY());
			if(element.getTokens() > 9) {
				node.addAttribute("ui.class", "token10");
			} else {
				node.addAttribute("ui.class", "token"+element.getTokens().toString());
			}
			graph_hash_map.put(element.getID(), node);
		}
		
		for(Transition element : trans) {
			Node node = this.addNode(element.getID());			
			node.addAttribute("ui.label", "["+element.getID()+"] "+element.getName());
			node.addAttribute("xy", element.getX(), -element.getY());
			node.addAttribute("ui.class", "trans");
			graph_hash_map.put(element.getID(), node);
		}
		
		for(Arc element : arcs) {
			Edge edge = this.addEdge(element.getID(), (Node) graph_hash_map.get(element.getSourceID()), (Node) graph_hash_map.get(element.getTargetID()), true);
			edge.addAttribute("ui.label", "["+element.getID()+"]");
		//	Edge edge = this.addEdge(element.getID(), index1, index2, true)
		}
	}
	
    /** Gibt die Hashmap für ID - Element Kombinationen zurück
     * @return this.graph_hash_map die HashMap des Graphen
    */
	public HashMap<String, Element> getHashMap() {
		return this.graph_hash_map;
	}
	
    /** Updated den Graphen auf Basis von Änderungen im Datenmodell
     * @param id ID des Objekts
     * @param object PetrinetObject, welches Basis für Update ist
    */
	public void updateAttribute(String id, PetrinetObject object) {
		Element graph_element = graph_hash_map.get(id);
		if(graph_element instanceof Node) {
			Node node = (Node) graph_element;
			Place place = (Place) object;		
			
			node.addAttribute("ui.label", "["+id+"] "+place.getName() + " <" + place.getTokens() + ">");
			//node.addAttribute("xy", element.getX(), -element.getY());
			if(place.getTokens() > 9) {
				node.addAttribute("ui.class", "token10");
			} else {
				node.addAttribute("ui.class", "token"+place.getTokens().toString());
			}
		}
	}
}
