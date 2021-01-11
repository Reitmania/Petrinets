package mypackage.model;

/** Klasse für Stellen im Petrinetz
*/
public class Place extends PetrinetObject {
	
	public static final int UNLIMITED = -1;
	
	private int DEFAULT_TOKEN;
	private int DEFAULT_TOKEN_INIT = 0;
	
	private int token = 0;
	private int maxtoken = UNLIMITED;
	
	private int x;
	private int y;
	
	protected Place(String id) {
		super(id);
	}
	
	protected Place(String id, int init_token) {
		this(id);
		this.token = init_token;
		
	}
	
    /** Setze initiale Tokens
     * @param token Anzahl der token/Marker
    */
	public void setInitToken(Integer token) {
		// Konfiguration aus PNML
		this.token = token;
		this.DEFAULT_TOKEN = token;
		this.DEFAULT_TOKEN_INIT = token;
	}
	
    /** Setze neue Token, die neue Anfangsmarkierung werden
     * @param token Anzahl der token/Marker
    */
	public void setNewToken(Integer token) {
		// Anpassen über GUI
		this.token = token;
		this.DEFAULT_TOKEN = token;
	}
	
    /** Setze neue Token
     * @param token Anzahl der token/Marker
    */
	public void setToken(Integer token) {
		this.token = token;
	}
	
    /** Setze Position
     * @param x x-Koordinate
     * @param y y-Koordinate
    */
	public void setPosition(Integer x, Integer y) {
		this.x = x;
		this.y = y;
	}
	
    /** Getter für x
     * @return this.x X-Koordinate
    */
	public Integer getX() {
		return this.x;
	}
	
    /** Getter für y
     * @return this.y Y-Koordinate
    */
	public Integer getY() {
		return this.y;
	}
    /** Getter für Tokens
     * @return this.token Aktuelle Marker
    */
	public Integer getTokens() {
		return this.token;
	}

    /** Test, ob geforderte Marker verfügbar sind
    */
	public boolean hasAtLeastTokens(int grenzwert) {
        return (token >= grenzwert);
    }
	
	public void reset() {
		this.token = this.DEFAULT_TOKEN;
	}
	
	public void reload() {
		this.token = this.DEFAULT_TOKEN_INIT;
	}
	

}

