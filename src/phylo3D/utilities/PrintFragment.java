package phylo3D.utilities;

/**
 * PrintFragment.java
 *
 * @created Sep 24, 2012
 * @author Pavel Danchenko
 */
public abstract class PrintFragment {

    public abstract void fragment(IndentPrintWriter pw);

    public void printIndented(IndentPrintWriter pw) {
        pw.indent();
        fragment(pw);
        pw.outdent();
    }

    public void printCurvedBrackets(IndentPrintWriter pw) {
        pw.println("{");
        fragment(pw);
        pw.println("}");
    }

    public void printSquareBrackets(IndentPrintWriter pw) {
        pw.println("[");
        fragment(pw);
        pw.println("]");
    }
}
