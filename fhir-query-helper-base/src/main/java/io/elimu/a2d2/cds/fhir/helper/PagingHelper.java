package io.elimu.a2d2.cds.fhir.helper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PagingHelper {

	private static final Logger LOG = LoggerFactory.getLogger(PagingHelper.class);
	
	public static final String PAGE_ID_PARAM_DEFAULT = "_getpages";
	public static final String PAGE_OFFSET_PARAM_DEFAULT = "_getpagesoffset";
	public static final String PAGE_SIZE_PARAM_DEFAULT = "_count";

	public static class Builder {
		private String pageIdParam = PAGE_ID_PARAM_DEFAULT;
		private String pageOffsetParam = PAGE_OFFSET_PARAM_DEFAULT;
		private String pageSizeParam = PAGE_SIZE_PARAM_DEFAULT;
		
		public Builder withPageIdParam(String name) {
			pageIdParam = name;
			return this;
		}
		
		public Builder withPageOffsetParam(String name) {
			pageOffsetParam = name;
			return this;
		}
		
		public Builder withPageSizeParam(String name) {
			pageSizeParam = name;
			return this;
		}
		
		public PagingHelper build() {
			return new PagingHelper(pageIdParam, pageOffsetParam, pageSizeParam);
		}
	}
	
	private final String pageIdParam;
	private final String pageOffsetParam;
	private final String pageSizeParam;
	
	public PagingHelper() {
		this(PAGE_ID_PARAM_DEFAULT, PAGE_OFFSET_PARAM_DEFAULT, PAGE_SIZE_PARAM_DEFAULT);
	}
	
	public PagingHelper(String pageIdParam, String pageOffsetParam, String pageSizeParam) {
		super();
		this.pageIdParam = pageIdParam;
		this.pageOffsetParam = pageOffsetParam;
		this.pageSizeParam = pageSizeParam;
	}

	public QueryBuilder pageFromUrl(String url) {
		QueryBuilder retval = new QueryBuilder();
		populatePageFromUrl(retval, url);
		return retval;
	}
	
	public QueryBuilder nextPageFromUrl(String url) {
		QueryBuilder retval = new QueryBuilder();
		populateNextPageFromUrl(retval, url);
		return retval;
	}
	
	public void populatePageFromUrl(QueryBuilder qb, String url) {
		String ssize = extractParam(url, pageSizeParam);
		String soffset = extractParam(url, pageOffsetParam);
		String pageId = extractParam(url, pageIdParam);
		Map<String, List<String>> params = new HashMap<>();
		params.put(pageIdParam, Arrays.asList(pageId));
		params.put(pageOffsetParam, Arrays.asList(soffset));
		params.put(pageSizeParam, Arrays.asList(ssize));
		populatePageFromUrl(qb, params);
	}

	private static String extractParam(String url, String paramName) {
		if (url != null && url.indexOf("?") >= 0) {
			String queryPart = url.substring(url.indexOf("?") + 1);
			String[] parts = queryPart.split("&");
			for (int index = 0; index < parts.length; index++) {
				String[] keyValue = parts[index].split("=");
				if (paramName.equalsIgnoreCase(keyValue[0]) && keyValue.length > 1) {
					return keyValue[1];
				}
			}
		}
		return null;
	}
	
	public void populatePageFromUrl(QueryBuilder qb, Map<String, List<String>> parameters) {
		String ssize = parameters.getOrDefault(pageSizeParam, Arrays.asList((String) null)).get(0);
		String soffset = parameters.getOrDefault(pageOffsetParam, Arrays.asList((String) null)).get(0);
		String pageId = parameters.getOrDefault(pageIdParam, Arrays.asList((String) null)).get(0);
		qb.pageId(pageId);
		if (ssize != null) {
			try {
				qb.count(Integer.valueOf(ssize));
			} catch (Exception e) { 
				LOG.warn("Problem parsing " + pageSizeParam + " from value " + ssize + ": " + e.getMessage());
			}
		}
		if (ssize != null) {
			try {
				qb.offset(Integer.valueOf(soffset));
			} catch (Exception e) { 
				LOG.warn("Problem parsing " + pageOffsetParam + " from value " + soffset + ": " + e.getMessage());
			}
		}

	}

	public void populateNextPageFromUrl(QueryBuilder qb, String url) {
		populatePageFromUrl(qb, url);
		int size = qb.getPageSize();
		int newOffset = qb.getOffset() + size;
		qb.offset(newOffset);
	}
}
