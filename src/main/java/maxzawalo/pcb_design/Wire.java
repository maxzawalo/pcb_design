package maxzawalo.pcb_design;

public class Wire {

	private Connector conn;
	private String connPinName;
	public Pin pin;

	public Wire(Connector conn, String connPinName, Pin pin) {
		this.conn = conn;
		this.connPinName = connPinName;
		this.pin = pin;
	}

	@Override
	public String toString() {
		return pin.Num + "\t" + conn.name + "." + connPinName + "\t\t\t" + "(" + pin + ")";
	}
}
