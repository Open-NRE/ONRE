/**
 * 
 */
package edu.iitd.cse.open_nre.onre.helper;

import edu.iitd.cse.open_nre.onre.OnrePropertiesReader;
import edu.knowitall.tool.parse.graph.DependencyGraph;

/**
 * @author harinder
 *
 */
public class OnreGraphHelper {

	// TODO: check if the function required
	public static DependencyGraph simplifyGraph(DependencyGraph depGraph) {
		DependencyGraph simplifiedDepGraph = depGraph;

		if (OnrePropertiesReader.isCollapseGraph()) {
			simplifiedDepGraph = depGraph.collapse();
		}

		if (OnrePropertiesReader.isSimplifyPostags()) {
			simplifiedDepGraph = depGraph.simplifyPostags();
		}

		if (OnrePropertiesReader.isSimplifyVBPostags()) {
			simplifiedDepGraph = depGraph.simplifyVBPostags();
		}

		return simplifiedDepGraph;
	}
}
