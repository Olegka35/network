import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.graphicGraph.GraphicNode;
import org.graphstream.ui.spriteManager.*;


public class GraphTest {
    public static void main(String[] args) {
        Graph graph = new SingleGraph("Tutorial 1");
        graph.addNode("A" );
        graph.addNode("B" );
        graph.addNode("C" );
        graph.addNode("D" );
        graph.addNode("E" );
        graph.addNode("F" );
        graph.addNode("G" );
        graph.addNode("H" );
        /*graph.addEdge("AB", "A", "B");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("CA", "C", "A");
        graph.addEdge("HG", "H", "G");
        graph.addEdge("HE", "H", "E");
        graph.addEdge("EF", "F", "E");
        graph.addEdge("ED", "D", "E");
        graph.addEdge("AE", "A", "E");*/

        for (Node node : graph) {
            node.addAttribute("ui.label", "192.168.0.1");
            node.addAttribute("ui.style", "size: 16px; fill-color: green;");
        }
        graph.display();


    }
}
