package nl.BobbinWork.viewer.gui;
import nl.BobbinWork.viewer.gui.BWViewer;

// unit3 for compatibility with tptp
import junit.framework.*;

public class TestLaunchPerformance extends TestCase {

	public void testEmptyDiagram() throws Exception {
		new BWViewer();
	}
}
