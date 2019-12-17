package de.uhd.ifi.se.decision.management.eclipse.persistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.gephi.graph.api.Node;

import de.uhd.ifi.se.decision.management.eclipse.event.NodeUtils;
import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.model.Link;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.KnowledgeGraphImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.LinkImpl;
import de.uhd.ifi.se.decision.management.eclipse.view.KnowledgeGraphView;
import de.uhd.ifi.se.decision.management.eclipse.view.impl.KnowledgeGraphViewImpl;

/**
 * Manages the persistence of knowledge.
 */
public class KnowledgePersistenceManager {
	
	final private static String KNOWLEDGE_LOCATION_FOLDER = "target";
	final private static String KNOWLEDGE_LOCATION_FILE = "knowledge.json";
	final private static String KNOWLEDGE_LOCATION = KNOWLEDGE_LOCATION_FOLDER + "/" + KNOWLEDGE_LOCATION_FILE;
	
	/**
     * Creates a link between the source node and the target node, if sourceNode and targetNode exist.
     *
     * @param sourceNode
	 * 		the source node
	 * @param targetNode
	 * 		the target node
     */
    public static boolean insertLink(Node sourceNode, Node targetNode) {
    	KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance();
    	KnowledgeGraphView knowledgeGraphView = KnowledgeGraphViewImpl.getInstance();
    	
    	if ((targetNode != null) && (sourceNode != null) &&
    			(!knowledgeGraph.linkExists(NodeUtils.convertNode(sourceNode), NodeUtils.convertNode(targetNode)))) {
    		knowledgeGraph.insertLink(NodeUtils.convertNode(sourceNode), NodeUtils.convertNode(targetNode));
    		insertLinkJSON(NodeUtils.convertNode(sourceNode), NodeUtils.convertNode(targetNode));
    	    	
    	    knowledgeGraphView.update(knowledgeGraph);
    	    
    	    return true;
		}
    	
    	return false;
    }
    
    /**
     * Creates a link between the source node and the target node and inserts it into the json file
     * storing the knowledge persistence data.
     * 
     * @param sourceNode
     * 		the source node
     * @param targetNode
     * 		the target node
     */
    private static void insertLinkJSON(de.uhd.ifi.se.decision.management.eclipse.model.Node sourceNode, 
    		de.uhd.ifi.se.decision.management.eclipse.model.Node targetNode) {
    	ObjectMapper mapper = new ObjectMapper();
    	
    	Link link = new LinkImpl(sourceNode, targetNode);
    	
    	openJSONFile();
    	
    	try {
			String jsonString = mapper.writeValueAsString(link);
			mapper.writeValue(new FileOutputStream(KNOWLEDGE_LOCATION), link);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private static void openJSONFile() {
    	
    	File folder = new File(KNOWLEDGE_LOCATION_FOLDER);
    	File file = new File(folder, KNOWLEDGE_LOCATION_FILE);
    	
    	folder.mkdirs();
    		
		if (!file.isFile()) {
    		try {
    			file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	
//		ObjectMapper mapper = new ObjectMapper();
//
//		try {
//			Link link = mapper.readValue(file, LinkImpl.class);
//			return link;
//		} catch (JsonParseException e) {
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
    }
	
}
