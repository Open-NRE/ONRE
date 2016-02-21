/**
 * 
 */
package edu.iitd.cse.open_nre.onre.helper;

import edu.knowitall.tool.parse.graph.DependencyGraph;


/**
 * @author harinder
 *
 */
public class MayIHelpYou {

	public static void runMe(DependencyGraph depGraph) {
		System.out.println(depGraph);
		depGraph.printDependencies();
		OnreGraphHelper.simplifyGraph(depGraph);
		//System.out.println("You are running me :)");
	}
}
