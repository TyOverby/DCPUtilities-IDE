package com.prealpha.dcputil.ide.editor;

import com.prealpha.dcputil.compiler.lexer.Expression;
import com.prealpha.dcputil.compiler.lexer.Lexer;
import com.prealpha.dcputil.compiler.lexer.Token;
import com.prealpha.dcputil.compiler.parser.TokenType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import java.util.ArrayList;
import java.util.List;


/**
 * User: Ty
 * Date: 8/2/12
 * Time: 6:04 PM
 */
public class Editor{
    public final StyledText textEditor;



    private static final int SPACES_PER_TAB = 4;

    public Editor(Composite parent, int style) {
        textEditor = new StyledText(parent, style);

        textEditor.addKeyListener(new EditorKeyListener());
        textEditor.addLineStyleListener(new EditorLineStyleListener());
    }


    private class EditorLineStyleListener implements LineStyleListener{

        @Override
        public void lineGetStyle(LineStyleEvent lineStyleEvent) {
            System.out.println(lineStyleEvent.lineText);
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
                        case POINTER_REGISTER:
                        case POINTER_LABEL_REF:
                        case POINTER_NEXT:
                        case POINTER_NEXT_PLUS_REGISTER:
                        case POINTER_REGISTER_PLUS_NEXT:
                            back = new Color(Display.getCurrent(),255,255,100);
                            break;
                        default:

                    }

                    styleList.add(new StyleRange(start+lineStyleEvent.lineOffset,length,fore,back,style));
                }
            }

            lineStyleEvent.styles = styleList.toArray(new StyleRange[styleList.size()]);
        }
    }

    private class EditorKeyListener implements KeyListener{

        @Override
        public void keyPressed(KeyEvent e) {
            if(e.stateMask == SWT.CTRL && e.keyCode == 'a'){
                textEditor.selectAll();
            }
            if(e.keyCode=='\t'){
                // TODO: replace tabs with spaces
            }

        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {
        }
    }
}
