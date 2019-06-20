package de.uhd.ifi.se.decision.management.eclipse.mock;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.atlassian.jira.rest.client.api.GetCreateIssueMetadataOptions;
import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.BulkOperationResult;
import com.atlassian.jira.rest.client.api.domain.CimProject;
import com.atlassian.jira.rest.client.api.domain.Comment;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueLink;
import com.atlassian.jira.rest.client.api.domain.Transition;
import com.atlassian.jira.rest.client.api.domain.Votes;
import com.atlassian.jira.rest.client.api.domain.Watchers;
import com.atlassian.jira.rest.client.api.domain.input.AttachmentInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.LinkIssuesInput;
import com.atlassian.jira.rest.client.api.domain.input.TransitionInput;
import com.atlassian.jira.rest.client.api.domain.input.WorklogInput;
import com.atlassian.util.concurrent.Effect;
import com.atlassian.util.concurrent.Promise;
import com.google.common.base.Function;
import com.google.common.util.concurrent.FutureCallback;

public class MockIssueRestClient implements IssueRestClient {

	@Override
	public Promise<Void> addAttachment(URI arg0, InputStream arg1, String arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Promise<Void> addAttachments(URI arg0, AttachmentInput... arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Promise<Void> addAttachments(URI arg0, File... arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Promise<Void> addComment(URI arg0, Comment arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Promise<Void> addWatcher(URI arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Promise<Void> addWorklog(URI arg0, WorklogInput arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Promise<BasicIssue> createIssue(IssueInput arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Promise<BulkOperationResult<BasicIssue>> createIssues(Collection<IssueInput> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Promise<Void> deleteIssue(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Promise<InputStream> getAttachment(URI arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Promise<Iterable<CimProject>> getCreateIssueMetadata(GetCreateIssueMetadataOptions arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Promise<Issue> getIssue(String key) {
		Promise<Issue> promise = new Promise<Issue>() {

			@Override
			public void addListener(Runnable arg0, Executor arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public boolean cancel(boolean mayInterruptIfRunning) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isCancelled() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isDone() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public Issue get() throws InterruptedException, ExecutionException {
				List<IssueLink> issueLinks = new ArrayList<IssueLink>();
				switch (key) {
				case "ECONDEC-1":
					issueLinks.add(new IssueLink("ECONDEC-5", null, null));
					return createIssue("WI: Create empty Eclipse plugin", key, issueLinks);
					
				case "ECONDEC-5":
					issueLinks.add(new IssueLink("ECONDEC-1", null, null));
					return createIssue("SF: Show knowledge graph", "ECONDEC-5", issueLinks);

				default:
					return null;
				}
			}

			@Override
			public Issue get(long timeout, TimeUnit unit)
					throws InterruptedException, ExecutionException, TimeoutException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Issue claim() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Promise<Issue> done(Effect<Issue> arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Promise<Issue> fail(Effect<Throwable> arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <B> Promise<B> flatMap(Function<? super Issue, Promise<B>> arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <B> Promise<B> fold(Function<Throwable, ? extends B> arg0,
					Function<? super Issue, ? extends B> arg1) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <B> Promise<B> map(Function<? super Issue, ? extends B> arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Promise<Issue> recover(Function<Throwable, ? extends Issue> arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Promise<Issue> then(FutureCallback<Issue> arg0) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		return promise;
	}

	@Override
	public Promise<Issue> getIssue(String arg0, Iterable<Expandos> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Promise<Iterable<Transition>> getTransitions(URI arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Promise<Iterable<Transition>> getTransitions(Issue arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Promise<Votes> getVotes(URI arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Promise<Watchers> getWatchers(URI arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Promise<Void> linkIssue(LinkIssuesInput arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Promise<Void> removeWatcher(URI arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Promise<Void> transition(URI arg0, TransitionInput arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Promise<Void> transition(Issue arg0, TransitionInput arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Promise<Void> unvote(URI arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Promise<Void> unwatch(URI arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Promise<Void> updateIssue(String arg0, IssueInput arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Promise<Void> vote(URI arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Promise<Void> watch(URI arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Issue createIssue(String summary, String key, List<IssueLink> issueLinks) {
		return new Issue(summary, URI.create("https://my-raspberry.pi/rest/" + key), key, null,
				new BasicProject(null, "ECONDEC", null, "Eclipse ConDec"), null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, issueLinks, null, null, null, null,
				null, null, null, null);
	}
}
