package acetone.database

import acetone.graph.{GoalNode, GraphStateManager, RouteEdge}
import com.github.tototoshi.csv._
import com.github.tototoshi.csv.CSVWriter

import java.io.File
import java.nio.file.{Path, Paths}
object GraphObjectDatabaseReaderWriterConstants{
  val nodeRowType = "node"
  val edgeRowType = "edge"
  val fileExtension = ".csv"
}
trait GraphObjectDatabaseReaderWriter {
  def persistGraphObjects(graphName: String, nodes: Set[GoalNode], edges: Set[RouteEdge], saveDirectory: Path): Unit = {
      val csvWriter = CSVWriter.open(Paths.get(saveDirectory.toString, graphName + GraphObjectDatabaseReaderWriterConstants.fileExtension).toFile)
      val rows = nodes.map(_.asCsv).toSeq ++ edges.map(_.asCSV).toSeq

      csvWriter.writeAll(rows)

      csvWriter.flush()
      csvWriter.close()
  }

  def loadGraph(file: File): Unit = {

    val csvReader = CSVReader.open(file)
    val allRows = csvReader.all()

    val edges = allRows.filter(_(0) == GraphObjectDatabaseReaderWriterConstants.edgeRowType).map(r => RouteEdge(r.tail))
    val nodes = allRows.filter(_(0) == GraphObjectDatabaseReaderWriterConstants.nodeRowType).map(r => GoalNode(r.tail))
    val graphName = file.getName.split(".")(0)
    GraphStateManager.loadGraphObjects(edges, nodes, graphName)
  }
}
