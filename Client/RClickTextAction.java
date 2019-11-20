package com.mycompany.texteditor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPopupMenu;

public class RClickTextAction implements MouseListener {

    JPopupMenu popup;
    
    public RClickTextAction(JPopupMenu popup)
    {
        this.popup = popup;
    }
    
    @Override
    public void mouseClicked(MouseEvent arg0) {
        if(arg0.getButton() == MouseEvent.BUTTON3)
        {
            popup.show(arg0.getComponent(), arg0.getX(), arg0.getY());
        }
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        //System.out.println("Mouse Pressed");
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        //System.out.println("Mouse Released");
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        //System.out.println("Mouse Entered");
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        //System.out.println("Mouse Exited");
    }
    
}
