package de.uhd.ifi.se.decision.management.eclipse.extraction.impl;

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.codehaus.jackson.node.ObjectNode;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.persistence.ConfigPersistenceManager;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.ObjectMapper;
import kong.unirest.Unirest;

/**
 * Class to connect to a JIRA project associated with this Eclipse project.
 * Retrieves JIRA issues.
 */
public class JiraClientImpl implements JiraClient {

	private JiraRestClient jiraRestClient;
	private boolean isWorking;
	private String projectKey;

	public JiraClientImpl() {
		boolean isValidUser = this.authenticate();
		this.projectKey = ConfigPersistenceManager.getProjectKey();
		boolean isValidProject = isValidProject(projectKey);
		this.isWorking = isValidUser && isValidProject;
	}

	public JiraClientImpl(URI jiraURI, String username, String password, String projectKey) {
		boolean isValidUser = this.authenticate(jiraURI, username, password);
		this.projectKey = projectKey;
		boolean isValidProject = isValidProject(projectKey);
		this.isWorking = isValidUser && isValidProject;
	}

	@Override
	public boolean authenticate() {
		return authenticate(ConfigPersistenceManager.getJiraUri(), ConfigPersistenceManager.getJiraUser(),
				ConfigPersistenceManager.getJiraPassword());
	}

	@Override
	public boolean authenticate(URI jiraURI, String username, String password) {
		this.jiraRestClient = new AsynchronousJiraRestClientFactory().createWithBasicHttpAuthentication(jiraURI,
				username, password);
		return isValidUser(username);
	}

	private boolean isValidUser(String username) {
		boolean isValidUser = true;
		try {
			this.getJiraRestClient().getUserClient().getUser(username).get().getSelf();
		} catch (ExecutionException | InterruptedException e) {
			isValidUser = false;
			System.err.println("The JIRA username seems to be wrong. Message: " + e.getMessage());
		}

		try {
			this.getJiraRestClient().getSessionClient().getCurrentSession().get().getUserUri().getPath();
		} catch (Exception e) {
			isValidUser = false;
			System.err.println("There is a problem with establishing the session. Message: " + e.getMessage());
		}
		return isValidUser;
	}

	private boolean isValidProject(String projectKey) {
		boolean isValidProject = true;
		try {
			this.getJiraRestClient().getProjectClient().getProject(projectKey);
		} catch (Exception e) {
			isValidProject = false;
			System.err.println("The JIRA project is unknown. Message: " + e.getMessage());
		}
		return isValidProject;
	}

	@Override
	public Set<JiraIssue> getAllJiraIssues() {
		Set<JiraIssue> jiraIssues = new HashSet<JiraIssue>();
		for (BasicIssue jiraIssue : this.getJiraRestClient().getSearchClient()
				.searchJql("project=\"" + projectKey + "\"").claim().getIssues()) {
			jiraIssues.add(JiraIssue.getOrCreate(jiraIssue.getKey(), this));
		}
		return jiraIssues;
	}

	@Override
	public JiraIssue getJiraIssue(String jiraIssueKey) {
		Issue issue = null;
		try {
			issue = this.jiraRestClient.getIssueClient().getIssue(jiraIssueKey).get();
		} catch (InterruptedException | ExecutionException e) {
			System.err.println(jiraIssueKey + ": " + e.getMessage());
		}
		return JiraIssue.getOrCreate(issue);
	}

	@Override
	public Set<JiraIssue> getLinkedJiraIssues(JiraIssue jiraIssue) {
		Set<JiraIssue> linkedJiraIssues = new HashSet<JiraIssue>();

		if (jiraIssue == null) {
			return linkedJiraIssues;
		}

		Set<String> neighborJiraIssueKeys = jiraIssue.getKeysOfLinkedJiraIssues();
		for (String key : neighborJiraIssueKeys) {
			JiraIssue linkedJiraIssue = this.getJiraIssue(key);
			linkedJiraIssues.add(linkedJiraIssue);
		}
		return linkedJiraIssues;
	}

	@Override
	public boolean close() {
		boolean isClosed = false;
		try {
			this.jiraRestClient.close();
			isClosed = true;
		} catch (IOException e) {
			System.err.println("JIRA REST client could not be closed. Message: " + e.getMessage());
		}
		return isClosed;
	}

	@Override
	public boolean isWorking() {
		return isWorking;
	}

	@Override
	public JiraRestClient getJiraRestClient() {
		return this.jiraRestClient;
	}

	@Override
	public void setJiraRestClient(JiraRestClient jiraRestClient) {
		this.jiraRestClient = jiraRestClient;
	}
	
	@Override
	public void createIssue(ObjectNode payload) {
		
		String username = ConfigPersistenceManager.getJiraUser();
		String password = ConfigPersistenceManager.getJiraPassword();

		String uri = ConfigPersistenceManager.getJiraUri().toString();
		if(!uri.endsWith("/")) {
			uri = uri.concat("/");
		}
		uri = uri.concat("/rest/condec/latest/knowledge/createDecisionKnowledgeElement.json");

		// Connect Jackson ObjectMapper to Unirest
		Unirest.config().setObjectMapper(new ObjectMapper() {
			private org.codehaus.jackson.map.ObjectMapper jacksonObjectMapper = new org.codehaus.jackson.map.ObjectMapper();

			public <T> T readValue(String value, Class<T> valueType) {
				try {
					return jacksonObjectMapper.readValue(value, valueType);
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

			public String writeValue(Object value) {
				try {
					return jacksonObjectMapper.writeValueAsString(value);
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});

		// This code sample uses the 'Unirest' library HttpResponse<JsonNode>
		HttpResponse<JsonNode> response;
		response = Unirest.post(uri).basicAuth(username, password)
				.header("Accept", "application/json").header("Content-Type", "application/json")
				.body(payload).asJson();

		System.out.println(payload.toString());
		System.out.println(response.getBody());

		/*String username = ConfigPersistenceManager.getJiraUser(); String password =
				ConfigPersistenceManager.getJiraPassword(); String authHeader = username +
				":" + password;

				String uri = ConfigPersistenceManager.getJiraUri().toString(); if
				(!uri.endsWith("/")) { uri = uri.concat("/"); } uri =
				uri.concat("/rest/api/3/issue/");

				try { Client client = Client.create();

				WebResource webResource = client.resource(uri);

				WebResource.Builder builder = webResource.header("Authorization", "Basic " +
						authHeader) .type("application/json").accept("application/json");

				ClientResponse response = builder.post(ClientResponse.class,
						payload.toString());

				if (response.getStatus() == 401) { throw new
					AuthenticationException("HTTP 401 received: Invalid Username or Password.");
				}

				String jsonResponse = response.getEntity(String.class);
				JSONObject responseJson = new JSONObject(jsonResponse);

				System.out.println(responseJson);

				return jsonResponse; } catch (AuthenticationException e) {
					System.err.println("Login failed. " + e.getMessage()); }

				return null;*/
		 
		
		/*
		 * try { IssueInputBuilder issueBuilder = new IssueInputBuilder();
		 * issueBuilder.setProjectKey(ConfigPersistenceManager.getProjectKey());
		 * //IssueTypeManager issueTypeManager = new IssueTypeManager(); //IssueType
		 * issueType = issueTypeManager.getIssueType(type);
		 * issueBuilder.setIssueTypeId((long) 10000); issueBuilder.setSummary(summary);
		 * issueBuilder.setDescription(description); IssueInput issueInput =
		 * issueBuilder.build();
		 * 
		 * this.getJiraRestClient().getIssueClient().createIssue(issueInput).get(); }
		 * catch (InterruptedException e) { e.printStackTrace(); } catch
		 * (ExecutionException e) { e.printStackTrace(); }
		 */
	}
}