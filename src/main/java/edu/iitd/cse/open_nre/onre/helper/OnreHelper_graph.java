/**
 * 
 */
package edu.iitd.cse.open_nre.onre.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import scala.collection.JavaConversions;
import edu.iitd.cse.open_nre.onre.OnrePropertiesReader;
import edu.iitd.cse.open_nre.onre.domain.OnrePatternNode;
import edu.iitd.cse.open_nre.onre.domain.OnrePatternTree;
import edu.iitd.cse.open_nre.onre.utils.OnreUtils;
import edu.knowitall.collection.immutable.graph.Graph;
import edu.knowitall.collection.immutable.graph.Graph.Edge;
import edu.knowitall.tool.parse.graph.Dependency;
import edu.knowitall.tool.parse.graph.DependencyGraph;
import edu.knowitall.tool.parse.graph.DependencyNode;

/**
 * @author harinder
 *
 */
public class OnreHelper_graph {

	public static DependencyGraph simplifyGraph(DependencyGraph depGraph) {
		DependencyGraph simplifiedDepGraph = depGraph;

		if (OnrePropertiesReader.isCollapseGraph()) {
			//simplifiedDepGraph = simplifiedDepGraph.collapse(); //TODO: when collapsing, dependencies don't change???
		}

		if (OnrePropertiesReader.isSimplifyPostags()) {
			simplifiedDepGraph = simplifiedDepGraph.simplifyPostags();
		}

		if (OnrePropertiesReader.isSimplifyVBPostags()) {
			simplifiedDepGraph = simplifiedDepGraph.simplifyVBPostags();
		}

		/*simplifiedDepGraph = simplifiedDepGraph.normalize(); //TODO: not sure whether we need these
		simplifiedDepGraph = simplifiedDepGraph.collapseNNPOf();
		simplifiedDepGraph = simplifiedDepGraph.collapseWeakLeaves();
		simplifiedDepGraph = simplifiedDepGraph.collapseXNsubj();*/

		return simplifiedDepGraph;
	}
	
	public static OnrePatternTree convertGraph2PatternTree(DependencyGraph depGraph) {
		Map<DependencyNode, Map<DependencyNode,String>> depMap = getDependencyMap(depGraph);
		DependencyNode start = getVertexWithNoIncoming(depGraph.graph());
		OnrePatternNode onrePatternNode = convertGraph2PatternTree_helper(start, depGraph.graph(), depMap);
		OnreUtils.sortPatternTree(onrePatternNode);
		
		OnrePatternTree onrePatternTree = new OnrePatternTree(depGraph.text(), onrePatternNode);
		return onrePatternTree;
	}
	
	@SuppressWarnings("unchecked")
    private static Map<DependencyNode, Map<DependencyNode,String>> getDependencyMap(DependencyGraph depGraph) {
		Map<DependencyNode, Map<DependencyNode,String>> dependencyMap = new HashMap<DependencyNode, Map<DependencyNode,String>>();
		
		scala.collection.immutable.Set<Dependency> depSet_scala = depGraph.dependencies();
		Set<Dependency> depSet = OnreUtils.scalaSet2JavaSet(depSet_scala);
		if(depSet == null) return dependencyMap;
		
		for (Dependency dependency : depSet) {
			Map<DependencyNode,String> depMap_internal = dependencyMap.get(dependency.source());
			if(depMap_internal == null) depMap_internal = new HashMap<>();
			depMap_internal.put(dependency.dest(), dependency.label());
			dependencyMap.put(dependency.source(), depMap_internal);
		}
		
		return dependencyMap;
	}
	
	@SuppressWarnings("unchecked")
    private static OnrePatternNode convertGraph2PatternTree_helper(DependencyNode depNode, Graph<DependencyNode> graph, Map<DependencyNode, Map<DependencyNode,String>> depMap) {
		
		OnrePatternNode onrePatternNode = new OnrePatternNode(depNode);
		
		Map<DependencyNode, scala.collection.immutable.Set<Edge<DependencyNode>>> map_outgoing = JavaConversions.asJavaMap(graph.outgoing());
		Set<Edge<DependencyNode>> outgoing_set = OnreUtils.scalaSet2JavaSet(map_outgoing.get(depNode));
		if(outgoing_set == null) return onrePatternNode;
		
		for (Edge<DependencyNode> edge : outgoing_set) {
			DependencyNode child_depNode = edge.dest();
			OnrePatternNode child_patternNode = convertGraph2PatternTree_helper(child_depNode, graph, depMap);
			//child_patternNode.parent = onrePatternNode;
			child_patternNode.dependencyLabel = depMap.get(depNode).get(child_depNode);
			onrePatternNode.children.add(child_patternNode);
        }
		
		return onrePatternNode;
	}
	
	@SuppressWarnings("unchecked")
    private static DependencyNode getVertexWithNoIncoming(Graph<DependencyNode> graph) {
		Set<DependencyNode> vertices = JavaConversions.asJavaSet(graph.vertices());
		for (DependencyNode vertex : vertices) {
			Map<DependencyNode, scala.collection.immutable.Set<Edge<DependencyNode>>> map_incoming = JavaConversions.asJavaMap(graph.incoming());
			Set<Edge<DependencyNode>> incoming_set = OnreUtils.scalaSet2JavaSet(map_incoming.get(vertex));
			if(incoming_set==null || incoming_set.isEmpty()) return vertex;
        }
		return null;
	}
	
}
