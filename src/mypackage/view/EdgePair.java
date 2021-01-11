package mypackage.view;

/** Generischer Datentyp für eine Liste aus Listen
*/
public class EdgePair<L, R> {
    private L l;
    private R r;
    /** Konstruktor
     * @param l linke Liste
     * @param r rechte Liste
    */
    public EdgePair(L l, R r){
        this.l = l;
        this.r = r;
    }
    public L getL(){ return l; }
    public R getR(){ return r; }
    public void setL(L l){ this.l = l; }
    public void setR(R r){ this.r = r; }
}
