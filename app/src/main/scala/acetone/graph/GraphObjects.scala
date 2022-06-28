package acetone.graph

import java.util.UUID

  /**
   * Defines a potential path between two goals in the graph
   * @param edgeId id of this edge
   * @param sourceId the goal node this edge comes from
   * @param targetId the goal node this edge can be traversed to
   * @param weight the amount this edge adds to the route (for speedruns this is seconds added to the run). must be grater than 0
   * @param difficulty how hard this route is to follow. used to optimize for different difficulties. must be within the range [1..10]
   * @param edgeName a name for this edge
   * @param edgeDescription description of the edge
   */
  case class RouteEdge(edgeId: UUID, sourceId: UUID, targetId: UUID, weight: Int, difficulty: Int, edgeName: String, edgeDescription: String) {
    require(difficulty < 11 && difficulty > 0)
    require(weight > 0)
  }

  /**
   * Defines a goal along the route.
   * @param nodeId the ID of this node
   * @param edges the outgoing edges from this node
   * @param goalName the name of this goal
   * @param goalDescription a description of this goal
   */
  case class GoalNode(nodeId: UUID, edges: Set[RouteEdge], goalName: String, goalDescription: String)
