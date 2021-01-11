package mypackage.view;

import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Element;
import org.graphstream.graph.Node;

import mypackage.model.Petrinet;
import mypackage.model.PEG;
import mypackage.model.PEGknoten;

/** Klasse für den Graphen des partiellen Erreichbarkeitsgraphen
*/
public class PEGGraph extends MyGraph {
	
	private HashMap<List<Integer>, Node> peggraph_hash_map_markierung2 = new HashMap<List<Integer>, Node>();
	private List<EdgePair<List<Integer>,List<Integer>>> EdgeList = new ArrayList<EdgePair<List<Integer>,List<Integer>>>();

    /** baut den Graphen auf, fügt nur Elemente hinzu, die noch nicht da sind
     * @param peg aktueller Erreichbarkeitsgraph
     * @param id ID der geschaltenen Transition
     * @param keylist_source vorherige Markierung
    */
	public void buildPEGGraph(PEG peg, String id, int[] keylist_source) {
		
		// Source Marken für die HashMap	
		
		HashMap<int[], PEGknoten> peg_hash_map_markierung = peg.getPEGHashMarkierung();	
		
		for(int[] keylist : peg_hash_map_markierung.keySet()) {
			
			List<Integer> intList = new ArrayList<Integer>(keylist.length);
			for (int i : keylist)
			{
			    intList.add(i);
			}	
			List<Integer> intListSource = new ArrayList<Integer>(keylist_source.length);
			for (int i : keylist_source)
			{
				intListSource.add(i);
			}				
			
			//MarkierungArray mark = new MarkierungArray(keylist);
			if(!peggraph_hash_map_markierung2.containsKey(intList)) {
				// neuer Knoten wird hinzugefügt, zu dem immer eine Kante gehen muss
				PEGknoten knoten = peg_hash_map_markierung.get(keylist);
				Node node = this.addNode(keylist.toString());
				node.addAttribute("ui.label", Arrays.toString(knoten.getMarken()));
				node.addAttribute("ui.class", "peg");				
				this.peggraph_hash_map_markierung2.put(intList, node);				
				if(peggraph_hash_map_markierung2.size() > 1) {
					// nur beim ersten Knoten gibts keine Edge
					Edge edge = this.addEdge(keylist.toString(), peggraph_hash_map_markierung2.get(intListSource), peggraph_hash_map_markierung2.get(intList));
					System.out.println("Kante von " + intListSource + " bis "+ intList);
					EdgeList.add(new EdgePair(intListSource, intList));
					edge.addAttribute("ui.label", "["+id+"]");
				}
			} 
		}
	}	
	
    /** Fügt ein neues Node hinzu
     * @param peg aktueller Erreichbarkeitsgraph
     * @param id ID der geschaltenen Transition
     * @param keylist aktuelle Markierung
     * @param keylist_source vorherige Markierung
    */
	public void insertNode(PEG peg, String id, int[] keylist, int[] keylist_source) {
		
		HashMap<int[], PEGknoten> peg_hash_map_markierung = peg.getPEGHashMarkierung();
		
		for(int[] keylist2 : peg_hash_map_markierung.keySet()) {
			for(int i = 0; i < keylist2.length; i++) {
				System.out.print(keylist2[i]);
			}
			System.out.println(" ");
		}

		List<Integer> intList = new ArrayList<Integer>(keylist.length);
		for (int i : keylist)
		{
		    intList.add(i);
		}	
		List<Integer> intListSource = new ArrayList<Integer>(keylist_source.length);
		for (int i : keylist_source)
		{
			intListSource.add(i);
		}
		System.out.println("alt: " + intListSource + ", neu: " + intList);
		
		//MarkierungArray mark = new MarkierungArray(keylist);
		if(!peggraph_hash_map_markierung2.containsKey(intList)) {
			// neuer Knoten wird hinzugefügt, zu dem immer eine Kante gehen muss
			//PEGknoten knoten = peg_hash_map_markierung.get(keylist);
			Node node = this.addNode(keylist.toString());
			node.addAttribute("ui.label", Arrays.toString(keylist));
			node.addAttribute("ui.class", "peg");				
			this.peggraph_hash_map_markierung2.put(intList, node);			

			
			if(peggraph_hash_map_markierung2.size() > 1) {
				// nur beim ersten Knoten gibts keine Edge
				Edge edge = this.addEdge(keylist.toString(), peggraph_hash_map_markierung2.get(intListSource), peggraph_hash_map_markierung2.get(intList));
				System.out.println("Kante von " + intListSource + " bis "+ intList);
				EdgeList.add(new EdgePair(intListSource, intList));
				edge.addAttribute("ui.label", "["+id+"]");
			}
			
		} else if (!intListSource.equals(intList)){
			boolean exists = false;
			for(EdgePair pair : EdgeList) {
				if(pair.getL().equals(intListSource)) {
					if(pair.getR().equals(intList)) {
						exists = true;
						System.out.println("Edge gibts schon" + intListSource + " bis "+ intList);
					}
				}
			}			
			
			if(!exists) {
				Edge edge = this.addEdge(keylist.toString(), peggraph_hash_map_markierung2.get(intListSource), peggraph_hash_map_markierung2.get(intList));
				EdgeList.add(new EdgePair(intListSource, intList));
				edge.addAttribute("ui.label", "["+id+"]");	
			}
			
		}
		
	}
	
    /** Updated ein Node auf neue Markierung
     * @param marken aktuelle Markierung
    */
	public void updateNode(int[] marken) {
		List<Integer> intList = new ArrayList<Integer>(marken.length);
		for (int i : marken)
		{
		    intList.add(i);
		}	
		if(peggraph_hash_map_markierung2.containsKey(intList)) {
			Node node = peggraph_hash_map_markierung2.get(intList);
			node.addAttribute("ui.label", intList);
		}
	}
	
    /** Löscht einen PEGGraphen
    */
	public void deletePEGGraph() {
		for(List<Integer> key : peggraph_hash_map_markierung2.keySet()) {
			Node node = peggraph_hash_map_markierung2.get(key);
			this.removeNode(node);
			peggraph_hash_map_markierung2.remove(key);
		}
	}
}
