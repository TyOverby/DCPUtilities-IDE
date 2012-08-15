package com.prealpha.dcputil.ide.editor;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Display;

import java.io.File;
import java.io.IOException;

/**
 * User: Ty
 * Date: 8/6/12
 * Time: 2:26 AM
 */
public class EditorTab {
    private final CTabItem tabItem;
    private final Editor editor;

    public EditorTab(CTabFolder parent, int style, File file){
        this.tabItem = new CTabItem(parent,style);

        System.out.println("Opening "+file.getAbsolutePath());
        if(!file.exists()){
            try{
                file.createNewFile();
                System.out.println("file created"+file.getAbsolutePath());
            }
            catch (IOException ioe){
                Status status = new Status(Status.ERROR,"test","Unable to make file",ioe);
                ErrorDialog.openError(Display.getCurrent().getActiveShell(), "IO Error", "The file specified could not be created", status);
            }
        }

        this.editor = new Editor(parent, SWT.NONE, file);
        this.tabItem.setControl(this.editor.textEditor);
        this.tabItem.setText(file.getName());
    }
}
