package com.prealpha.dcputil.ide;

import com.prealpha.dcputil.ide.swt.SWTResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.*;
import swing2swt.layout.BorderLayout;


public class EntryPoint {

	protected Display display;
	protected Shell shell;
	
	private Table memory_table;
	private Table registry_table;
	private Table stackTable;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			EntryPoint window = new EntryPoint();
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
		
		final SashForm  mainSash = new SashForm(shell, SWT.VERTICAL);
		mainSash.setSashWidth(8);
		mainSash.setLayoutData(BorderLayout.CENTER);
		mainSash.setOrientation(SWT.HORIZONTAL);
		
		Composite editorPanel = new Composite(mainSash, SWT.NONE);
		editorPanel.setLayout(new BorderLayout(0, 0));
		
		ToolBar toolBar = new ToolBar(editorPanel, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(BorderLayout.NORTH);
		
		CTabFolder tabFolder = new CTabFolder(editorPanel, SWT.BORDER | SWT.CLOSE | SWT.FLAT);
		tabFolder.setSelectionBackground(SWTResourceManager.getColor(SWT.COLOR_CYAN));
		tabFolder.setTabHeight(25);
		tabFolder.setLayoutData(BorderLayout.CENTER);
		tabFolder.setBackground(new Color(display,255,255,255));
		tabFolder.setSelectionBackground(new Color[] {new Color(display, 255,255,255), new Color(display, 155, 200, 255)}, new int[]{50}, true); 
		tabFolder.setSimple(false);
		
		//EditorTab editorTab = new EditorTab(null,tabFolder, SWT.NONE);
		
		CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE);
		tabItem.setText("New Item");
		
		Button btnNewButton = new Button(tabFolder, SWT.NONE);
		tabItem.setControl(btnNewButton);
		btnNewButton.setText("New Button");
		
		final SashForm infoSash = new SashForm(mainSash, SWT.VERTICAL);
		
		Composite composite = new Composite(infoSash, SWT.NONE);
		composite.setLayout(null);
		
		registry_table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		registry_table.setBounds(0, 22, 106, 182);
		registry_table.setHeaderVisible(true);
		registry_table.setLinesVisible(true);
		
		stackTable = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		stackTable.setBounds(112, 22, 103, 182);
		stackTable.setHeaderVisible(true);
		stackTable.setLinesVisible(true);
		TableColumn stack = new TableColumn(stackTable,SWT.FULL_SELECTION);
		stack.setText("Stack");
		stack.setWidth(stackTable.getSize().x-10);
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
		composite_1.setBounds(221, 22, 248, 182);
		
		memory_table = new Table(infoSash, SWT.FULL_SELECTION);
		memory_table.setHeaderVisible(true);
		memory_table.setLinesVisible(true);
		
		TableColumn address = new TableColumn(memory_table, SWT.CENTER);
		address.setText("Base Address");
		address.setWidth(90);
		mainSash.setWeights(new int[] {1, 1});
	    for(int i=0;i<8;i++){
	    	TableColumn col = new TableColumn(memory_table, SWT.CENTER);
	    	col.setWidth(70);
	    	col.setText(""+i);
	    }
	    
	    
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");
		
		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);
		
		MenuItem mntmNewProject = new MenuItem(menu_1, SWT.NONE);
		mntmNewProject.setText("New Project");
		
		MenuItem mntmEdit = new MenuItem(menu, SWT.CASCADE);
		mntmEdit.setText("Edit");
		
		Menu menu_2 = new Menu(mntmEdit);
		mntmEdit.setMenu(menu_2);
		
		MenuItem mntmView = new MenuItem(menu, SWT.CASCADE);
		mntmView.setText("View");

		Menu menu_3 = new Menu(mntmView);
		mntmView.setMenu(menu_3);
		
		MenuItem mntmToggleAspect = new MenuItem(menu_3, SWT.NONE);
		mntmToggleAspect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				switch(mainSash.getOrientation()){
					case SWT.VERTICAL:
						mainSash.setOrientation(SWT.HORIZONTAL);
						infoSash.setOrientation(SWT.VERTICAL);
						break;
					case SWT.HORIZONTAL:
						mainSash.setOrientation(SWT.VERTICAL);
						infoSash.setOrientation(SWT.HORIZONTAL);
						break;
				}
			}
		});
		mntmToggleAspect.setText("Toggle Aspect");
		
		MenuItem menuItem = new MenuItem(menu_3, SWT.SEPARATOR);
		menuItem.setText("Number Formating ");
		
		MenuItem mntmFormatHex = new MenuItem(menu_3, SWT.RADIO);
		mntmFormatHex.setSelection(true);
		mntmFormatHex.setText("Format Hex");
		
		MenuItem mntmFormatDec = new MenuItem(menu_3, SWT.RADIO);
		mntmFormatDec.setText("Format Dec");
		
		MenuItem mntmFormatBin = new MenuItem(menu_3, SWT.RADIO);
		mntmFormatBin.setText("Format Bin");
		
	}
}