package phylo3D.utilities;

import java.io.PrintWriter;
import java.io.Writer;

/**
 * IndentPrintWriter.java
 *
 * @created Sep 23, 2012
 * @author Pavel Danchenko
 */
public class IndentPrintWriter extends PrintWriter {

    private String indent = "\t";
    private boolean indented = false;
    private int level = 0;

    public IndentPrintWriter(Writer out) {
        super(out);
    }

    public void indent() {
        level++;
    }

    public void outdent() {
        level--;
    }

    @Override
    public void print(String s) {
        doIndent();
        super.print(s);
    }

    @Override
    public void println() {
        indented = false;
        super.println();
    }

    private void doIndent() {
        if (!indented) {
            indented = true;
            for ( int i = 0; i < level; i++ ) {
                super.print(indent);
            }
        }
    }

}
