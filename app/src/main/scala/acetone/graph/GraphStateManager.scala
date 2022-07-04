package acetone.graph

import com.github.blemale.scaffeine.{Cache, Scaffeine}

import java.util.UUID

object GraphStateManager {
  //HERE BEGINNETH THE GLOBAL STATE
  private val edgeCache: Cache[UUID, RouteEdge] = Scaffeine().recordStats().build()
  private val nodeCache: Cache[UUID, GoalNode] = Scaffeine().recordStats().build()

  var openGraphName: String = ""
  var graphIsOpen: Boolean = false
  var rootNodeId: Option[UUID] = None
  var endNodeId: Option[UUID] = None
  var unsavedChanges: Boolean = false

  //HERE ENDETH THE GLOBAL STATE

  def dumpCache: Boolean = {
    edgeCache.invalidateAll()
    nodeCache.invalidateAll()
    true
  }

  def loadGraphObjects(edges: List[RouteEdge], nodes: List[GoalNode], graphName: String): Boolean = {
    edges.map(e => edgeCache.put(e.edgeId, e))
    nodes.map(n => nodeCache.put(n.nodeId, n))
    graphIsOpen = true
    rootNodeId = nodes.find(_.goalName == "Start").map(_.nodeId)
    endNodeId = nodes.find(_.goalName == "End").map(_.nodeId)
    unsavedChanges = false
    true
  }

  def tryCreateGraph(graphName: String): Boolean = {
    if (graphIsOpen && unsavedChanges) {
      false
    }
    else {
      openGraphName = graphName
      graphIsOpen = true
      rootNodeId = Option(UUID.randomUUID())
      endNodeId = Option(UUID.randomUUID())
      val rootNode = GoalNode(rootNodeId.get, Set.empty, "Start", "The entry point of the route")
      val endNode = GoalNode(endNodeId.get, Set.empty, "End", "The exit point of the route")
      nodeCache.put(rootNode.nodeId, rootNode)
      nodeCache.put(endNode.nodeId, endNode)
      true
    }
  }
}
