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
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.type.TypeReference;
import org.gephi.graph.api.Node;

import de.uhd.ifi.se.decision.management.eclipse.event.NodeUtils;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
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
     * Reads all links contained in the JSON knowledge file and adds them to the knowledge graph.
     * If the source or target node are not contained in the knowledge graph, the link is not added.
     */
	public static void readLinksFromJSON() {
		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance();
		
		List<Link> links = readJSONFile();
		
		for (Link link: links) {
			if (knowledgeGraph.containsVertex(link.getSourceNode()) 
					&& knowledgeGraph.containsVertex(link.getTargetNode())) {
				knowledgeGraph.insertLink(link);
			}
		}
	}
	
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
    	knowledgeGraph.updateWithPersistanceData();
    	KnowledgeGraphView knowledgeGraphView = KnowledgeGraphViewImpl.getInstance(knowledgeGraph);
    	
    	de.uhd.ifi.se.decision.management.eclipse.model.Node sourceKnowledgeNode = NodeUtils.convertNode(sourceNode);
    	de.uhd.ifi.se.decision.management.eclipse.model.Node targetKnowledgeNode = NodeUtils.convertNode(targetNode);
    	
    	
    	if (((sourceNode != null) && targetNode != null) &&
    			(!knowledgeGraph.linkExists(sourceKnowledgeNode, targetKnowledgeNode)) &&
    			!(sourceKnowledgeNode.getNodeId().equals(targetKnowledgeNode.getNodeId()))) {
    		knowledgeGraph.insertLink(sourceKnowledgeNode, targetKnowledgeNode);
    		insertLinkJSON(sourceKnowledgeNode, targetKnowledgeNode);
    	    	
    	    knowledgeGraphView.update(knowledgeGraph);
    	    
    	    return true;
		}
    	
    	return false;
    }
    
    /**
     * Creates a link between the source node and the target node and inserts it into the JSON file
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
    	
    	List<Link> links = readJSONFile();
    	
		if (!links.contains(newLink)) {
			links.add(newLink);
			writeJSONFile(links);
		}
    }
    
    /**
     * Removes the link between the source node and the target node, if sourceNode and targetNode exist.
     *
     * @param sourceNode
	 * 		the source node
	 * @param targetNode
	 * 		the target node
     */
    public static boolean removeLink(Node sourceNode, Node targetNode) {
    	KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance();
    	knowledgeGraph.updateWithPersistanceData();
    	KnowledgeGraphView knowledgeGraphView = KnowledgeGraphViewImpl.getInstance(knowledgeGraph);
    	
    	de.uhd.ifi.se.decision.management.eclipse.model.Node sourceKnowledgeNode = NodeUtils.convertNode(sourceNode);
    	de.uhd.ifi.se.decision.management.eclipse.model.Node targetKnowledgeNode = NodeUtils.convertNode(targetNode);
    	
    	
    	if (((sourceNode != null) && targetNode != null) &&
    			(knowledgeGraph.linkExists(sourceKnowledgeNode, targetKnowledgeNode)) &&
    			!(sourceKnowledgeNode.getNodeId().equals(targetKnowledgeNode.getNodeId()))) {
    		knowledgeGraph.removeLink(sourceKnowledgeNode, targetKnowledgeNode);
    		removeLinkJSON(sourceKnowledgeNode, targetKnowledgeNode);
    	    	
    	    knowledgeGraphView.update(knowledgeGraph);
    	    
    	    return true;
		}
    	
    	return false;
    }
    
    /**
     * Removes the link between the source node and the target node and removes it from the JSON file
     * storing the knowledge persistence data.
     * 
     * @param sourceNode
     * 		the source node
     * @param targetNode
     * 		the target node
     */
    private static void removeLinkJSON(de.uhd.ifi.se.decision.management.eclipse.model.Node sourceNode, 
    		de.uhd.ifi.se.decision.management.eclipse.model.Node targetNode) {
    	Link newLink = new LinkImpl(sourceNode, targetNode);
    	
    	List<Link> links = readJSONFile();
    	
		if (links.contains(newLink)) {
			links.remove(newLink);
			writeJSONFile(links);
		}
    }
    
    /**
     * Reads the list of JSON-objects and converts them to a list of links.
     * If no JSON-file exists, one is created.
     * 
     * @return 
     * 		a list of links in the JSON-file
     */
    private static List<Link> readJSONFile() {
    	File folder = new File(KNOWLEDGE_LOCATION_FOLDER);
    	File file = new File(folder, KNOWLEDGE_LOCATION_FILE);
    	
    	openJSONFile(folder, file);
    	
		ObjectMapper mapper = new ObjectMapper();

		try {
			if (!FileUtils.readFileToString(file).trim().isEmpty()) {
				List<Link> links = mapper.readValue(file, new TypeReference<List<LinkImpl>>(){});
				links = convertLinks(links);
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
    
    /**
     * Writes a list of JSON-objects to a JSON-file.
     * If no JSON-file exists, one is created.
     */
    private static void writeJSONFile(List<Link> links) {
    	File folder = new File(KNOWLEDGE_LOCATION_FOLDER);
    	File file = new File(folder, KNOWLEDGE_LOCATION_FILE);
    	
    	openJSONFile(folder, file);
    	
		ObjectMapper mapper = new ObjectMapper();

		try {
			mapper.writeValue(file, links);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Converts all the links in the list to fully working links by expanding the node ids to full nodes.
     * 
     * @param links
     * 		the list containing the links
     * @return
     * 		the list containing the links with the links expanded
     */
    private static List<Link> convertLinks(List<Link> links) {
    	for (Link link: links) {
    		for (de.uhd.ifi.se.decision.management.eclipse.model.Node node: 
    			de.uhd.ifi.se.decision.management.eclipse.model.Node.nodes.values()) {
    			if (link.getSourceId().equals(node.getNodeId())) {
    				link.setSourceNode(node);
    			}
    			if (link.getTargetId().equals(node.getNodeId())) {
    				link.setTargetNode(node);
    			}
    		}
		}
    	
    	List<Link> convertedLinks = new ArrayList<Link>();
    	
    	for (Link link: links) {
    		if (!(link.getSourceId().equals(link.getTargetId()))) {
    			convertedLinks.add(link);
    		}
		}
    	
    	return convertedLinks;
    }
    
    /**
     * Checks, if a JSON knowledge file exists.
     * If not, one is created.
     * 
     * @param folder
     * 		the path to the folder containing the file
     * @param file
     * 		the path to the file
     */
    private static void openJSONFile(File folder, File file) {
    	folder.mkdirs();
		
		if (!file.isFile()) {
    		try {
    			file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
    
    public static void createDecisionKnowledgeElementInJira(String type, String summary, String description) {
		JiraClient jiraClient = JiraClient.getOrCreate();
		
		JsonNodeFactory jnf = JsonNodeFactory.instance;
		ObjectNode payload = jnf.objectNode();
		{
			payload.put("projectKey", ConfigPersistenceManager.getProjectKey());
			payload.put("type", type);
			payload.put("summary", summary);
			payload.put("description", description);
			payload.put("documentationLocation", "i");
		}
		
		jiraClient.createIssue(payload);
    }
	
}