package net.sf.bloodball.model;

import de.vestrial.util.*;
import java.awt.Dimension;

public interface FieldExtents {
	public static final Dimension SIZE = new Dimension(28, 15);
	public static final int END_ZONE_WIDTH = 2;
	public static final int SETUP_ZONE_WIDTH = 8;
	public static final Range HOME_END_ZONE = new Range(0, END_ZONE_WIDTH - 1);
	public static final Range GUEST_END_ZONE = new Range(SIZE.width - END_ZONE_WIDTH, SIZE.width - 1);
	public static final Range HOME_SETUP_ZONE =
		new Range(HOME_END_ZONE.getUpperBound() + 1, HOME_END_ZONE.getUpperBound() + SETUP_ZONE_WIDTH);
	public static final Range GUEST_SETUP_ZONE =
		new Range(GUEST_END_ZONE.getLowerBound() - SETUP_ZONE_WIDTH, GUEST_END_ZONE.getLowerBound() - 1);
	public static final Range VERTICAL_EXTENTS = new Range(0, SIZE.height - 1);
	public static final RangedArea HOME_SUBSTITUTE_AREA =
		new RangedArea(
			new Range(HOME_SETUP_ZONE.getUpperBound() - 3, HOME_SETUP_ZONE.getUpperBound()),
			new Range(SIZE.height - 1, SIZE.height - 1));
  public static final RangedArea GUEST_SUBSTITUTE_AREA =
    new RangedArea(
      new Range(GUEST_SETUP_ZONE.getLowerBound(), GUEST_SETUP_ZONE.getLowerBound() + 3),
      new Range(0, 0));
}