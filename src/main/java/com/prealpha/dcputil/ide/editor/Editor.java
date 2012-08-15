package com.prealpha.dcputil.ide.editor;

import com.prealpha.dcputil.compiler.lexer.Expression;
import com.prealpha.dcputil.compiler.lexer.Lexer;
import com.prealpha.dcputil.compiler.lexer.Token;
import com.prealpha.dcputil.compiler.parser.TokenType;
import com.prealpha.util.FileUtilities;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


/**
 * User: Ty
 * Date: 8/2/12
 * Time: 6:04 PM
 */
public class Editor{
    private class TextState{
        public final String contents;
        public final int    offset;
        public TextState(String contents, int offset){
            this.contents = contents;
            this.offset = offset;
        }
    }

    public final StyledText textEditor;
    private final Stack<TextState> undoStack = new Stack<TextState>();
    private final Stack<TextState> redoStack = new Stack<TextState>();



    private static final int SPACES_PER_TAB = 4;

    private final File file;

    public Editor(Composite parent, int style, File file) {
        textEditor = new StyledText(parent, style);
        textEditor.setFont(JFaceResources.getFont(JFaceResources.TEXT_FONT));

        textEditor.addKeyListener(new EditorKeyListener());
        textEditor.addLineStyleListener(new EditorLineStyleListener());
        textEditor.addModifyListener(new EditorModifyListener());

        this.file = file;
        this.undoStack.push(new TextState("",0));

        read(this.file, parent.getShell());
    }

    private void read(File file, Shell shell){
        try{
            this.textEditor.setText(FileUtilities.readFile(this.file));
        }
        catch (IOException ioe){
            Status status = new Status(Status.ERROR,"somePluginId","File Unreadable",ioe);
            ErrorDialog.openError(Display.getCurrent().getActiveShell(),"IO Error","The file specified could not be read",status);
            ioe.printStackTrace();
        }
    }

    private void save(File file, Shell shell){
        try{
            FileUtilities.writeFile(file, textEditor.getText());
        }
        catch (IOException ioe){
            Status status = new Status(Status.ERROR,"test","Unable to write to file",ioe);
            ErrorDialog.openError(Display.getCurrent().getActiveShell(),"IO Error","The file specified could not be wrote to.",status);
        }
    }

    private void undo(){
        if(!undoStack.empty()){
            redoStack.push(new TextState(textEditor.getText(),textEditor.getCaretOffset()));

            TextState ts = undoStack.pop();
            textEditor.setText(ts.contents);
            textEditor.setCaretOffset(ts.offset);
            this.ignoreUndo = true;
        }else{
            System.out.println("Undo stack is empty");
        }
    }
    private void redo(){
        if(!redoStack.empty()){
            textEditor.setText(redoStack.pop().contents);
        }else{
            System.out.println("Redo stack is empty");
        }
    }


    private boolean ignoreUndo = false;

    private class EditorModifyListener implements ModifyListener{
        private int lastTime = 0;
        @Override
        public void modifyText(ModifyEvent modifyEvent) {
            if(!ignoreUndo){
                //this.lastTime = modifyEvent.time;
                boolean muchChanged = false;
                if(!undoStack.empty()){
                    muchChanged = Math.abs(textEditor.getText().length() - undoStack.peek().contents.length()) > 10;
                }
                System.out.println("EnterHit: "+enter+"\nmuchChanged"+muchChanged);
                if(muchChanged||enter){
                    if(enter){
                        enter = false;
                    }
                    undoStack.push(new TextState(textEditor.getText(),textEditor.getCaretOffset()));

                }
            }
        }
    }

    private class EditorLineStyleListener implements LineStyleListener{

        @Override
        public void lineGetStyle(LineStyleEvent lineStyleEvent) {
            //System.out.println(lineStyleEvent.lineText);
            Lexer lineLexer = new Lexer();

            List<Expression> expressions = lineLexer.lex(lineStyleEvent.lineText);
            List<StyleRange> styleList  = new ArrayList<StyleRange>();
            for(Expression e: expressions){
                for(Token t:e.tokens){

                    int start = t.charStart;
                    int length   = t.orig.length();
                    Color fore = new Color(Display.getCurrent(),0,0,0);
                    Color back = new Color(Display.getCurrent(),255,255,255);
                    int style = 0;

                    TokenType tt = TokenType.getType(t);


                    switch(tt){
                        case OPERATION:
                            fore = new Color(Display.getCurrent(),0,0,255);
                            style = SWT.BOLD;
                            break;
                        case REGISTER:
                        case STACK_OPERATIONS:
                            style = SWT.BOLD;
                            break;
                        case LABEL:
                            fore = new Color(Display.getCurrent(),150,0,0);
                            break;
                        case LABEL_REF:
                            fore = new Color(Display.getCurrent(),50,0,0);
                            break;
                        case POINTER_REGISTER:
                        case POINTER_LABEL_REF:
                        case POINTER_NEXT:
                        case POINTER_NEXT_PLUS_REGISTER:
                        case POINTER_REGISTER_PLUS_NEXT:
                            back = new Color(Display.getCurrent(),255,255,200);
                            break;
                        default:

                    }

                    styleList.add(new StyleRange(start+lineStyleEvent.lineOffset,length,fore,back,style));
                    //System.out.print("\n"+tt);
                }
                //System.out.println("");
            }

            lineStyleEvent.styles = styleList.toArray(new StyleRange[styleList.size()]);
        }
    }

    private boolean enter=false;
    private class EditorKeyListener implements KeyListener{
        private String toAdd = "";

        @Override
        public void keyPressed(KeyEvent e) {
            this.toAdd = null;
            if(e.stateMask == SWT.CTRL && e.keyCode == 'a'){
                textEditor.selectAll();
            }
            if(e.stateMask==SWT.CTRL && e.keyCode == 'z'){
                undo();
            }
            if(e.keyCode=='\t'){
                // TODO: replace tabs with spaces
            }
            if(e.keyCode=='\r'||e.keyCode=='\n'){
                enter = true;

                // Detect how many tabs or spaces there is in the last line.
                String[] lines = textEditor.getText().split("\n");
                String lastLine = lines[lines.length-1];
                String tabs = "";
                for(int i=0;i<lastLine.length();i++){
                    if(!Character.isWhitespace(lastLine.charAt(i))||i==lastLine.length()-1){
                        tabs = lastLine.substring(0,i);
                        break;
                    }
                }
                this.toAdd = tabs;
            }

            if(this.toAdd !=null && this.toAdd.length()>=1){
                textEditor.insert(this.toAdd);
                textEditor.setCaretOffset(textEditor.getCaretOffset()+toAdd.length());
            }
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {
        }
    }
}
