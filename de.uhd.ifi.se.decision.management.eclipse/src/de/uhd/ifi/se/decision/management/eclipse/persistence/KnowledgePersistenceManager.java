package de.uhd.ifi.se.decision.management.eclipse.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
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
    	KnowledgeGraphView knowledgeGraphView = KnowledgeGraphViewImpl.getInstance(knowledgeGraph);
    	
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
    	
    	Link newLink = new LinkImpl(sourceNode, targetNode);
    	
    	List<Link> links = openJSONFile();
    	
    	try {
			if (!links.contains(newLink)) {
				File folder = new File(KNOWLEDGE_LOCATION_FOLDER);
		    	File file = new File(folder, KNOWLEDGE_LOCATION_FILE);
		    	
		    	ObjectMapper mapper = new ObjectMapper();
				
				links.add(newLink);
				mapper.writeValue(file, links);
			}
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Reads the list of JSON-objects and converts them to a list of links.
     * If no JSON-file exists, one is created.
     * 
     * @return 
     * 		a list of links in the JSON-file
     */
    private static List<Link> openJSONFile() {
    	
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
    	
		ObjectMapper mapper = new ObjectMapper();

		try {
			if (!FileUtils.readFileToString(file).trim().isEmpty()) {
				List<Link> links = mapper.readValue(file, new TypeReference<List<LinkImpl>>(){});
				return links;
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new ArrayList<Link>();
    }
	
}
