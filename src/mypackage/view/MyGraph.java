package mypackage.view; 

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Element;

/** Klasse für Mygraph als Erweiterung des MultiGraph zur Darstellung von Petrinetz und Erreichbarkeitsgraph
*/
public class MyGraph extends MultiGraph {
	
	private static String CSS_FILE = "url(" + MyGraph.class.getResource("/graph.css") + ")"; // diese Variante der Pfadangabe funktioniert auch aus einem JAR heraus
 	
    /** Konstruktor
    */
	public MyGraph() {
		super("Beispiel");
		// Angabe einer css-Datei für das Layout des Graphen
		this.addAttribute("ui.stylesheet", CSS_FILE);
	}
		
    /** gibt ID einer Node als Text für Label zurück
     * @param id ID des Elements
    */
	public String getOutputText(String id) {
		Node node = this.getNode(id);
		return new String("Der Knoten \"" + node.getAttribute("ui.label") + "\" hat die ID \"" + node.getId() + "\"");
	}
	
	/**
	 * Das Hervorheben des Knotens wegnehmen oder setzen.
	 * 
	 * @param id
	 *            Id des Knotens, bei dem das Hervorheben getauscht werden soll
	 */
	public void toggleNodeHighlight(String id) {
		Node node = this.getNode(id);		
		if (node.hasAttribute("ui.class")) {
			// Prüfen, was in ui.class drinsteckt
			String ui_class = (String) node.getAttribute("ui.class");
						
			if(ui_class.contains("highlight")) {
				node.setAttribute("ui.class", ui_class.replace("highlight", ""));
			} else {
				node.addAttribute("ui.class", ui_class+",highlight");
			}			
		} else {
			node.addAttribute("ui.class");
		}
	}
}
