package de.uhd.ifi.se.decision.management.eclipse.mock;

import java.net.URI;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.Filter;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.util.concurrent.Effect;
import com.atlassian.util.concurrent.Promise;
import com.google.common.base.Function;
import com.google.common.util.concurrent.FutureCallback;

public class MockSearchRestClient implements SearchRestClient {

	@Override
	public Promise<Iterable<Filter>> getFavouriteFilters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Promise<Filter> getFilter(URI arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Promise<Filter> getFilter(long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Promise<SearchResult> searchJql(String arg0) {
		return new Promise<SearchResult>() {

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
			public SearchResult get() throws InterruptedException, ExecutionException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public SearchResult get(long timeout, TimeUnit unit)
					throws InterruptedException, ExecutionException, TimeoutException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public SearchResult claim() {
				return new SearchResult(0, 0, 0, MockIssueRestClient.getAllIssues());
			}

			@Override
			public Promise<SearchResult> done(Effect<SearchResult> arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Promise<SearchResult> fail(Effect<Throwable> arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <B> Promise<B> flatMap(Function<? super SearchResult, Promise<B>> arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <B> Promise<B> fold(Function<Throwable, ? extends B> arg0,
					Function<? super SearchResult, ? extends B> arg1) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <B> Promise<B> map(Function<? super SearchResult, ? extends B> arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Promise<SearchResult> recover(Function<Throwable, ? extends SearchResult> arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Promise<SearchResult> then(FutureCallback<SearchResult> arg0) {
				// TODO Auto-generated method stub
				return null;
			}

		};
	}

	@Override
	public Promise<SearchResult> searchJql(String arg0, Integer arg1, Integer arg2, Set<String> arg3) {
		return searchJql(arg0);
	}

}
