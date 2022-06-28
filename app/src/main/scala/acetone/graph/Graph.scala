package acetone.graph

import com.github.blemale.scaffeine.{Cache, Scaffeine}

import java.util.UUID

object Graph {
  //HERE BEGINNETH THE GLOBAL STATE
  val edgeCache: Cache[UUID, RouteEdge] = Scaffeine().recordStats().build()
  val nodeCache: Cache[UUID, GoalNode] = Scaffeine().recordStats().build()

  var openGraphName: String = ""
  var graphIsOpen: Boolean = false
  var rootNodeId: Option[UUID] = None
  var unsavedChanges: Boolean = false

  //HERE ENDETH THE GLOBAL STATE

  def tryCreateGraph(graphName: String): Boolean = {
    if (graphIsOpen && unsavedChanges) {
      false
    }
    else {
      openGraphName = graphName
      graphIsOpen = true
      rootNodeId = Option(UUID.randomUUID())
      val rootNode = GoalNode(rootNodeId.get, Set.empty, "Start", "The entry point of the route")
      nodeCache.put(rootNode.nodeId, rootNode)
      true
    }
  }
}
