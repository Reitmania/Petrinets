package mypackage.controller;

import org.graphstream.graph.Graph;

import mypackage.model.Petrinet;
import mypackage.model.PetrinetObject;
import mypackage.model.Place;
import mypackage.model.Arc;
import mypackage.model.PEG;
import mypackage.model.Transition;
import mypackage.view.MyFrame;
import mypackage.view.MyGraph;
import mypackage.view.PEGGraph;
import mypackage.view.PetriGraph;
import mypackage.model.PNMLParser;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

/** Controller-Klasse zur Steuerung der Interaktion
*/
public class Controller {
	
	/** Filename of PNML */
	private String filename = "";
	
	/** File of PNML */
	private File file;
	
	/** Hauptfenster der Anwendung */
	private MyFrame frame;

	/** Petrinet Graph, der angezeigt wird */
	private PetriGraph graph;	
	private PetriGraph graph_default;
	// Erreichbarkeitsgraph
	private PEGGraph graph_peg;
	private PEGGraph graph_peg_default;
	
	// einzelnes Petrinetz
	private Petrinet petrinet;		
	private Petrinet petrinet_default;	
	
	private List<String> path = new ArrayList<String>();
	
	// Liste von Petrinetzen für Abarbeitung mehrerer Dateien
	List<Petrinet> nets = new ArrayList<Petrinet>();
	
	/** Parser zum Einlesen von Petrinetzen */
	private PNMLParser parser;
	
	/** selektiertes Node */
	private String id_selected;
	
	/** Anfangsmarkierung */
	private String id_start;
	
    /** Konstruktor
     * @param frame aktuelles Hauptframe der GUI
    */
	public Controller(MyFrame frame) {
		this.frame = frame;		
		// Erzeugung der Empty Graphs, Defaults für Reset
		graph = new PetriGraph();	
		graph_default = new PetriGraph();
		graph_peg = new PEGGraph();
		graph_peg_default = new PEGGraph();
		this.frame.textareaMessage("Graph initialisiert.");
	}
	
    /** Klick auf ein Element im Petrinetz
     * @param id ID des Elements
    */
	public void clickNodeInGraph(String id) {
		// Ausgabe, welcher Knoten geklickt wurde
		frame.outputText(graph.getOutputText(id));
		
		// Prüfen, ob vorher etwas selektiert war. Wenn ja, dann deselektieren
		if(id_selected != null) {
			graph.toggleNodeHighlight(id_selected);
		}		
		// Selektion erfassen im Controller
		this.id_selected = id;

		
		// Unterscheiden in Place und Transition
		if(petrinet.getPetrinetObject(id) instanceof Transition) {
			
			// Source Marken für die HashMap			
			int[] marken_source = petrinet.getMarker();
			
			// Transition schalten
			petrinet.activateTrans(id);
			Transition trans = (Transition) petrinet.getPetrinetObject(id);
						
			// update Graph
			if(trans.getStatus()) {
				for(String source : trans.getSourceIDs()) {
					graph.updateAttribute(source, petrinet.getPetrinetObject(source));
				}
				for(String source : trans.getTargetIDs()) {
					graph.updateAttribute(source, petrinet.getPetrinetObject(source));
				}
				// update PEG + PEGGraph, füge aktivierte Transition hinzu
				petrinet.getPEG().addTrans(id, petrinet.getMarker());
				graph_peg.insertNode(petrinet.getPEG(), id, petrinet.getMarker(), marken_source);
				//graph_peg.buildPEGGraph(petrinet.getPEG(), id, marken_source);
			}
			this.frame.textareaMessage("Transition " + trans.getID() + " ausgewählt. Status: " + trans.getStatus());	
			if(petrinet.getPEG().getStatus()) {
				this.frame.textareaMessage(" Petrinetz "+ petrinet.getID() + " ist unbeschränkt.");			
			}
			
		}		
		
		// Markierung des Knoten wegnehmen oder Knoten hervorheben.
		graph.toggleNodeHighlight(id);
	}
	
    /** rekursiver Beschränktheitsalgorithmus
    */
	public void recursiveActivation() {

		for(Transition trans : petrinet.getTransitions()) {			
			if(trans.canActivate(petrinet)) {
				//System.out.println("Möglichkeit: " + trans.getID());
				String id = trans.getID();
				//System.out.println("Schalte " + id);
				petrinet.activateTrans(id);
				path.add(id);
				// update pEG
				if(trans.getStatus()) {
					petrinet.getPEG().addTrans(id, petrinet.getMarker());
				}
				if(petrinet.getPEG().getStatus() || trans.getSourceIDs().equals(trans.getTargetIDs())) {
					// unbeschränkt erkannt, Abbruch
					break;
				} else {
					// weiteres Aktivieren
					recursiveActivation();
				}
			}
		}
		// keine aktivierbaren Transitionen mehr auf diesem Pfad
		// Reset zum Status vor der jetzigen ID
		// verbotene (schon gegangene Pfade) hinzufügen
		petrinet.getPEG().getGoneIDs().add(petrinet.getPEG().getTransIDs());
		
		if(path.size() > 0) {
			path.remove(path.size()-1);
			petrinet.resetToID(path);
		}
	}

    /** Wenn visuell gefordert, können hier die Graphen erstellt werden
     * @param visual Bool, ob visuell aktiviert ist oder nicht
    */
	public void buildGraph(boolean visual) {
		// eigentliche Erzeugung des Petrinets
		petrinet = new Petrinet(filename);
		this.frame.textareaMessage("Petrinetz initialisiert.");
		
		// Parser erzeugen und Petrinet bauen		
		parser = new PNMLParser(this.file, this.petrinet);		
		parser.initParser();
		parser.parse();		
		this.frame.textareaMessage("Petrinetz " + filename + " geparsed.");		
		
		petrinet.Arc2Trans();
		// Erstellen des pEG auf Basis der Anfangsmarkierung
		petrinet.buildPEG();		
		
		// Erstellen des Petrinetz-Graphen
		if(visual) {			
			graph.buildGraph(petrinet);
			this.frame.textareaMessage("Graph von Petrinetz "+ petrinet.getID() + " erstellt.");						
			graph_peg.buildPEGGraph(petrinet.getPEG(), "t0", petrinet.getMarker());
			this.frame.textareaMessage("EG von Petrinetz "+ petrinet.getID() + " erstellt.");
		} else {
			
			recursiveActivation();
			for(int[] key : petrinet.getPEG().getPEGHashMarkierung().keySet()) {
				for(int i = 0; i < key.length; i++) {
					System.out.print(key[i]);
				}
				System.out.println();
			}
			System.out.println("Knoten: " + petrinet.getPEG().getPEGHashMarkierung().size());			
		}	
		
		frame.textareaMessage("Der Erreichbarkeitsgraph verfügt über " + petrinet.getPEG().getPEGHashMarkierung().size() + " Knoten.");
		
		if(petrinet.getPEG().getStatus()){
			frame.textareaMessage("Das Netz " + this.filename + " ist unbeschränkt.");
		}
		
		// Startnetz/-graphen abspeichern für Reset
		petrinet_default = petrinet;
		graph_default = graph;
		graph_peg_default = graph_peg;
		
		
	}

	public Graph getGraph() {
		return graph;
	}
	
	public Graph getGraphPEG() {
		return graph_peg;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String name, String example_folder) {
		this.filename = name;
		this.file = new File(example_folder+"/"+this.filename);
	}
	
    /** Wenn eine Stelle selektiert ist, können hier Marker geändert werden
     * @param change Veränderung der Marker
    */
	public void changeMarker(int change) {
		if(id_selected != null) {
			// Änderung im Petrinetz
			int[] marken_source = petrinet.getMarker();
			petrinet.setMarker(id_selected, change);
			// Änderung im Graph
			graph.updateAttribute(id_selected, petrinet.getPetrinetObject(id_selected));
			// die modifizierte Stelle ist neue Anfangsmarkierung!
			id_start = id_selected;
			// Leite neuen PEG Graphen ab
			petrinet.buildPEG();
			
			
			// Source Marken für die HashMap
			//graph_peg.updateNode(marken_source);
			//graph_peg.clear();
			graph_peg.deletePEGGraph();
			graph_peg.buildPEGGraph(petrinet.getPEG(), "t0", petrinet.getMarker());
		}		
	}
	
	public void deletePEG() {
		// lösche PEG

		
		
		// reset des Netzes und des PEG
		
		// Graphen anpassen
	}
	
    /** Lädt das komplette Netz neu
    */
	public void reload() {
		petrinet.reload();    	
		for (String id : petrinet.getHashMap().keySet()) {
    		if(petrinet.getPetrinetObject(id) instanceof Place) {
    			graph.updateAttribute(id, petrinet.getPetrinetObject(id));
    		}
    	}
		graph_peg.deletePEGGraph();
		graph_peg.buildPEGGraph(petrinet.getPEG(), "t0", petrinet.getMarker());
	}
	
    /** Lädt aktuelle Anfangsmarkierung neu
    */
	public void resetDefaults() {
		petrinet.reset();
    	for (String id : petrinet.getHashMap().keySet()) {
    		if(petrinet.getPetrinetObject(id) instanceof Place) {
    			graph.updateAttribute(id, petrinet.getPetrinetObject(id));
    		}
    	}
		
		
		//this.graph.clear();
		//petrinet = petrinet_default;
		//this.graph.clear();
		//this.graph.getHashMap().clear();
		//this.graph.buildGraph(petrinet);
	}
}
