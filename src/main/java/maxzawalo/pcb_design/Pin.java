package maxzawalo.pcb_design;

public class Pin {

	public int Num;
	public String[] params;
	public int Side;
	public String port;

	public boolean FindByParam(String param) {
		if (param.isEmpty())
			return true;
		for (String p : params) {
			String pl = p;
			// Убираем цифры
			for (char ch : "0123456789".toCharArray())
				pl = pl.replaceAll("" + ch, "");
			if (pl.equals(param))
				return true;
		}

		return false;
	}

	@Override
	public String toString() {
		return Num + "|" + port + "," + String.join(",", params);
	}

	public void Print() {
		System.out.println(this);
	}

}
