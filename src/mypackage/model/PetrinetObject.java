package mypackage.model;

/** Abstraktes PetrinetObject als Basis für alle Bestandteile der Netze
*/
public class PetrinetObject {
	private String id;
	private String name;
	
    /** Konstruktor
     * @param id ID des Elements
    */
	public PetrinetObject(String id) { 
		super();
		this.id = id;
	}
	
    /** Getter für ID
     * @return id ID des Elements
    */
	public String getID() {
		return id;
	}
	
    /** Setter für ID
     * @param id ID des Elements
    */
	public void setID(String id) {
		this.id = id;
	}
	
	
    /** Setter für Name
     * @param name ID des Elements
    */
	public void setName(String name) {
		this.name = name;
	}
	
    /** Getter für Name
     * @return this.name Name des Elements
    */
	public String getName() {
		return this.name;
	}
}
