package nl.BobbinWork.bwlib.gui;

import java.awt.Component;

import javax.swing.JSplitPane;

/** A JToolbar with an additional convenience constructor. */
@SuppressWarnings("serial")
public class SplitPane extends JSplitPane {

    public static final int DIVIDER_WIDTH = 8;

    /** Convenience JSplitPane constructor */
    public SplitPane(int dividerposition, int orientation, Component Left, Component Right) {
        super(orientation, Left, Right);
        if (getDividerSize() < DIVIDER_WIDTH) {
            setDividerSize(DIVIDER_WIDTH);
        }
        setBorder(null); // prevent nested borders
        setDividerLocation(dividerposition);
        setOneTouchExpandable(true);
    }
}
