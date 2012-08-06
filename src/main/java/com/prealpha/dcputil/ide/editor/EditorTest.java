package com.prealpha.dcputil.ide.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import swing2swt.layout.BorderLayout;

/**
 * User: Ty
 * Date: 8/2/12
 * Time: 5:44 PM
 */
public class EditorTest {
    protected Display display;
    protected Shell shell;

    private Editor editor;

    /**
     * Launch the application.
     * @param args
     */
    public static void main(String[] args) {
        try {
            EditorTest window = new EditorTest();
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open the window.
     */
    public void open() {
        display = Display.getDefault();
        createContents();
        shell.open();
        shell.layout();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {
        shell = new Shell();
        shell.setSize(867, 523);
        shell.setText("DCPUtilities IDE");
        shell.setLayout(new BorderLayout(0, 0));

        editor = new Editor(shell, SWT.NONE);
    }
}
