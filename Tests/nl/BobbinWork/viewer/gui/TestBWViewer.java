package nl.BobbinWork.viewer.gui;

import org.uispec4j.Button;
import org.uispec4j.UISpec4J;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.MainClassAdapter;

public class TestBWViewer extends UISpecTestCase {

	private static final int SECOND = 1000;
    private Button replaceButton;
    private Button deleteButton;

	protected void setUp() throws Exception {
	    super.setUp();
		setAdapter( new MainClassAdapter(
				nl.BobbinWork.viewer.gui.BWViewer.class, 
				new String[] {"nl"} 
		));
	    Window w = getMainWindow();
		this.replaceButton = w.getButton("verwijderen");
	    this.deleteButton = w.getButton("vervangen");
	    UISpec4J.setWindowInterceptionTimeLimit(20*SECOND);
	}	

	public void testInitialAppearance () throws Exception {
		
		assertFalse( replaceButton.isEnabled() );
		assertFalse( deleteButton.isEnabled() );
	}
}
