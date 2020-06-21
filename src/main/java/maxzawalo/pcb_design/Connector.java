package maxzawalo.pcb_design;

import java.util.ArrayList;
import java.util.List;

public class Connector {

	public String name;

	public Connector(String name) {
		this.name = name;
	}

	public List<KeyValuePair> pins = new ArrayList<>();

	public void addPin(String name, String chipPin) {
		pins.add(new KeyValuePair(name, chipPin));
	}
}