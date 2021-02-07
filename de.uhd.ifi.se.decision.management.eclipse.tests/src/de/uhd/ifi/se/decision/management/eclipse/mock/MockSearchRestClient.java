package de.uhd.ifi.se.decision.management.eclipse.mock;

import java.net.URI;
import java.util.Set;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Promise<SearchResult> searchJql(String arg0, Integer arg1, Integer arg2, Set<String> arg3) {
		// TODO Auto-generated method stub
		return null;
	}

}
