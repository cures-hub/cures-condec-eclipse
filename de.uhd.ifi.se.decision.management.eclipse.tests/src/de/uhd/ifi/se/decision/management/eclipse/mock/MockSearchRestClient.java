package de.uhd.ifi.se.decision.management.eclipse.mock;

import java.net.URI;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Function;

import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.Filter;
import com.atlassian.jira.rest.client.api.domain.SearchResult;

import io.atlassian.util.concurrent.Promise;

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
			public Promise<SearchResult> done(Consumer<? super SearchResult> arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Promise<SearchResult> fail(Consumer<Throwable> arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <B> Promise<B> flatMap(Function<? super SearchResult, ? extends Promise<? extends B>> arg0) {
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
			public Promise<SearchResult> then(TryConsumer<? super SearchResult> arg0) {
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
