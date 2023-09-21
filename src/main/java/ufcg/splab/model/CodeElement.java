package ufcg.splab.model;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"methodName",
"attributeName",
"className",
"packageName",
"details"
})
public class CodeElement {

	@JsonProperty("methodName")
	private String methodName;
	@JsonProperty("attributeName")
	private Object attributeName;
	@JsonProperty("className")
	private String className;
	@JsonProperty("packageName")
	private Object packageName;
	@JsonProperty("details")
	private Object details;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("methodName")
	public String getMethodName() {
		return methodName;
	}

	@JsonProperty("methodName")
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	@JsonProperty("attributeName")
	public Object getAttributeName() {
		return attributeName;
	}

	@JsonProperty("attributeName")
	public void setAttributeName(Object attributeName) {
		this.attributeName = attributeName;
	}

	@JsonProperty("className")
	public String getClassName() {
		return className;
	}

	@JsonProperty("className")
	public void setClassName(String className) {
		this.className = className;
	}

	@JsonProperty("packageName")
	public Object getPackageName() {
		return packageName;
	}

	@JsonProperty("packageName")
	public void setPackageName(Object packageName) {
		this.packageName = packageName;
	}

	@JsonProperty("details")
	public Object getDetails() {
		return details;
	}

	@JsonProperty("details")
	public void setDetails(Object details) {
		this.details = details;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}

