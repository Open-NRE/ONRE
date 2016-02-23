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
			simplifiedDepGraph = simplifiedDepGraph.collapse();
		}

		if (OnrePropertiesReader.isSimplifyPostags()) {
			simplifiedDepGraph = simplifiedDepGraph.simplifyPostags();
		}

		if (OnrePropertiesReader.isSimplifyVBPostags()) {
			simplifiedDepGraph = simplifiedDepGraph.simplifyVBPostags();
		}

		simplifiedDepGraph = simplifiedDepGraph.normalize();
		simplifiedDepGraph = simplifiedDepGraph.collapseNNPOf();
		simplifiedDepGraph = simplifiedDepGraph.collapseWeakLeaves();
		simplifiedDepGraph = simplifiedDepGraph.collapseXNsubj();

		return simplifiedDepGraph;
	}
}
