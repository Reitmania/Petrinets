package mypackage.model;

import java.io.File;

/** PNML Parser Ableitung
*/
public class PNMLParser extends PNMLWopedParser {
	
	private Petrinet petrinet;
	
    /** Konstruktor
     * @param pnml aktuelles pnml
     * @param petrinet aktuelles Petrinetz
    */
	public PNMLParser(final File pnml, Petrinet petrinet) {
		super(pnml);
		this.petrinet = petrinet;
	}
    /** neue Transition
     * @param id ID des Elements
    */
	@Override
	public void newTransition(final String id) {
		System.out.println("Transition mit id " + id + " wurde gefunden.");
		petrinet.add(new Transition(id));
	}
    /** neue Stelle
     * @param id ID des Elements
    */
	@Override
	public void newPlace(final String id) {
		System.out.println("Stelle mit id " + id + " wurde gefunden.");
		petrinet.add(new Place(id));
	}
    /** neue Kante
     * @param id ID des Elements
    */
	@Override
	public void newArc(final String id, final String source, final String target) {
		System.out.println("Kante mit id " + id + " von " + source + " nach " + target + " wurde gefunden.");
		petrinet.add(new Arc(id, source, target));
	}
    /** setze Position
     * @param id ID des Elements
     * @param x x-Koordinate
     * @param y y-Koordinate
    */
	@Override
	public void setPosition(final String id, final String x, final String y) {
		System.out.println("Setze die Position des Elements " + id + " auf (" + x + ", " + y + ")");
		petrinet.setElementPosition(id, Integer.valueOf(x), Integer.valueOf(y));
	}
    /** Setze Namen
     * @param id ID des Elements
     * @param name Name des Elements
    */
	@Override
	public void setName(final String id, final String name) {
		System.out.println("Setze den Namen des Elements " + id + " auf " + name);
		petrinet.setElementName(id, name);
	}
    /** Setze Marker
     * @param id ID des Elements
     * @param tokens neue Markeranzahl
    */
	@Override
	public void setTokens(final String id, final String tokens) {
		System.out.println("Setze die Markenanzahl des Elements " + id + " auf " + tokens);
		petrinet.setElementTokens(id, Integer.valueOf(tokens));
	}	
	
}
