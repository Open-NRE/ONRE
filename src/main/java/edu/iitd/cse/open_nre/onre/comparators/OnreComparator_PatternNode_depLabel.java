/**
 * 
 */
package edu.iitd.cse.open_nre.onre.comparators;

import java.util.Comparator;

import edu.iitd.cse.open_nre.onre.domain.OnrePatternNode;

/**
 * @author harinder
 *
 */
public class OnreComparator_PatternNode_depLabel implements Comparator<OnrePatternNode> {

	@Override
    public int compare(OnrePatternNode o1, OnrePatternNode o2) {
	    return o1.dependencyLabel.compareTo(o2.dependencyLabel);
    }

}
