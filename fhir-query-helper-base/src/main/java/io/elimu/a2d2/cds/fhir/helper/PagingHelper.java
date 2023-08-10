package io.elimu.a2d2.cds.fhir.helper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PagingHelper {

	private static final Logger LOG = LoggerFactory.getLogger(PagingHelper.class);
	
	public static final String PAGE_ID_PARAM = "_getpages";
	public static final String PAGE_OFFSET_PARAM = "_getpagesoffset";
	public static final String PAGE_SIZE_PARAM = "_count";
	
	public static QueryBuilder pageFromUrl(String url) {
		QueryBuilder retval = new QueryBuilder();
		populatePageFromUrl(retval, url);
		return retval;
	}
	
	public static QueryBuilder nextPageFromUrl(String url) {
		QueryBuilder retval = new QueryBuilder();
		populateNextPageFromUrl(retval, url);
		return retval;
	}
	
	public static void populatePageFromUrl(QueryBuilder qb, String url) {
		String ssize = extractParam(url, PAGE_SIZE_PARAM);
		String soffset = extractParam(url, PAGE_OFFSET_PARAM);
		String pageId = extractParam(url, PAGE_ID_PARAM);
		Map<String, List<String>> params = new HashMap<>();
		params.put(PAGE_ID_PARAM, Arrays.asList(pageId));
		params.put(PAGE_OFFSET_PARAM, Arrays.asList(soffset));
		params.put(PAGE_SIZE_PARAM, Arrays.asList(ssize));
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
	
	public static void populatePageFromUrl(QueryBuilder qb, Map<String, List<String>> parameters) {
		String ssize = parameters.getOrDefault(PAGE_SIZE_PARAM, Arrays.asList((String) null)).get(0);
		String soffset = parameters.getOrDefault(PAGE_OFFSET_PARAM, Arrays.asList((String) null)).get(0);
		String pageId = parameters.getOrDefault(PAGE_ID_PARAM, Arrays.asList((String) null)).get(0);
		qb.pageId(pageId);
		if (ssize != null) {
			try {
				qb.count(Integer.valueOf(ssize));
			} catch (Exception e) { 
				LOG.warn("Problem parsing " + PAGE_SIZE_PARAM + " from value " + ssize + ": " + e.getMessage());
			}
		}
		if (ssize != null) {
			try {
				qb.offset(Integer.valueOf(soffset));
			} catch (Exception e) { 
				LOG.warn("Problem parsing " + PAGE_OFFSET_PARAM + " from value " + soffset + ": " + e.getMessage());
			}
		}

	}

	public static void populateNextPageFromUrl(QueryBuilder qb, String url) {
		populatePageFromUrl(qb, url);
		int size = qb.getPageSize();
		int newOffset = qb.getOffset() + size;
		qb.offset(newOffset);
	}
}
