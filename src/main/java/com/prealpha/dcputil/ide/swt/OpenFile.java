package com.prealpha.dcputil.ide.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

/**
 * User: Ty
 * Date: 8/9/12
 * Time: 10:47 PM
 */
public class OpenFile extends SelectionAdapter {

    @Override
    public void widgetSelected(SelectionEvent event) {
        FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
        fd.setText("Open File");
        String[] filterExt = { "*.dcpu16", "*.dcpu" };
        fd.setFilterExtensions(filterExt);

        String selected = fd.open();
        System.out.println(selected);
    }
}
