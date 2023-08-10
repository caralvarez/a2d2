package io.elimu.a2d2.cds.fhir.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class QueryBuilder {

	private String id;
	private boolean paging = true;
	private Map<String, String> params = new HashMap<>();
	private String resourceType;
	private int count;
	private boolean useCache = true;
	private int retries = Integer.valueOf(System.getProperty("http.invoke.retries", "3"));
	private int delay = Integer.valueOf(System.getProperty("http.invoke.retrydelay", "1000"));
	private String pageId;
	private int offset;
	
	public QueryBuilder withRetries(int retries) {
		this.retries = retries;
		return this;
	}
	
	public QueryBuilder withDelayBetweenRetries(int delay) {
		this.delay = delay;
		return this;
	}
	
	public QueryBuilder paging(boolean paging) {
		this.paging = paging;
		return this;
	}
	
	public QueryBuilder useCache(boolean useCache) {
		this.useCache = useCache;
		return this;
	}
	
	public QueryBuilder withParam(String paramName, String paramValue) {
		this.params.put(paramName, paramValue);
		return this;
	}
	
	public QueryBuilder id(String id) {
		this.id = id;
		return this;
	}
	
	public String getId() {
		return id;
	}
	
	public boolean hasParam(String paramName) {
		return this.params.containsKey(paramName);
	}
	
	public QueryBuilder resourceType(String resourceType) {
		this.resourceType = resourceType;
		return this;
	}
	
	public String getResourceType() {
		return resourceType;
	}
	
	public QueryBuilder count(int count) {
		this.count = count;
		return this;
	}
	
	public QueryBuilder pageId(String pageId) {
		this.pageId = pageId;
		return this;
	}

	public QueryBuilder offset(int offset) {
		this.offset = offset;
		return this;
	}
	
	public String getPageId() {
		return pageId;
	}
	
	public int getPageSize() {
		return count;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public boolean hasPaging() {
		return paging;
	}
	
	public boolean useCache() {
		return useCache;
	}
	
	public int getDelay() {
		return delay;
	}
	
	public int getRetries() {
		return retries;
	}
	
	public String buildQuery(String baseUrl) {
		StringBuilder sb = new StringBuilder();
		if (this.pageId != null) {
			sb.append(baseUrl).append("?_getpages=").append(this.pageId);
			if (this.offset >= 0) {
				sb.append("&_getpagesoffset=").append(offset);
			}
			appendCount(sb);
			sb.append("&_bundletype=searchset");
		}
		sb.append(baseUrl).append('/').append(resourceType);
		sb.append('?');
		appendCount(sb);
		for (String key : params.keySet()) {
			if (!"_count".equals(key)) {
				sb.append('&').append(key).append('=').append(params.get(key));
			}
		}
		return sb.toString();
	}

	private void appendCount(StringBuilder sb) {
		if (count > 0 || params.containsKey("_count")) {
			if (count > 0) {
				sb.append("_count").append('=').append(count);
			} else {
				sb.append("_count").append('=').append(params.get("_count"));
			}
		} else {
			sb.append("_count").append('=').append(50);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(count, delay, id, paging, params, resourceType, retries, useCache, pageId, offset);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QueryBuilder other = (QueryBuilder) obj;
		return Objects.equals(count, other.count) && Objects.equals(delay, other.delay) &&
				Objects.equals(id, other.id) && Objects.equals(paging, other.paging) &&
				Objects.equals(params, other.params) && Objects.equals(resourceType, other.resourceType) &&
				Objects.equals(retries, other.retries) && Objects.equals(useCache, other.useCache) &&
				Objects.equals(pageId, other.pageId) && Objects.equals(offset, other.offset);
	}
}
