package net.sf.bloodball.model.test;

import de.vestrial.util.Range;
import java.awt.Dimension;
import junit.framework.TestCase;
import net.sf.bloodball.model.FieldExtents;

public class FieldExtentsTest extends TestCase {
	public FieldExtentsTest(String name) {
		super(name);
	}

	public void testFieldDimension() {
		assertEquals(new Dimension(28, 15), FieldExtents.SIZE);
	}

	public void testGuestEndZone() {
		assertEquals(new Range(26, 27), FieldExtents.GUEST_END_ZONE);
	}

	public void testGuestSetupZone() {
		assertEquals(new Range(18, 25), FieldExtents.GUEST_SETUP_ZONE);
	}

	public void testHomeEndZone() {
		assertEquals(new Range(0, 1), FieldExtents.HOME_END_ZONE);
	}

	public void testHomeSetupZone() {
		assertEquals(new Range(2, 9), FieldExtents.HOME_SETUP_ZONE);
	}

	public void testVerticalExtents() {
		assertEquals(new Range(0, 14), FieldExtents.VERTICAL_EXTENTS);
	}
}