package maxzawalo.pcb_design;

public class KeyValuePair {
	private String key;
	private String value;

	public KeyValuePair(String key, String value) {
		this.setKey(key);
		this.setValue(value);
	}

	String getValue() {
		return value;
	}

	private void setValue(String value) {
		this.value = value;
	}

	String getKey() {
		return key;
	}

	private void setKey(String key) {
		this.key = key;
	}
}