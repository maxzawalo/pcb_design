package maxzawalo.pcb_design;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Chip {
	protected boolean firstPinClose2Last = false;
	private int currentSide = 0;
	List<Pin> pins = new ArrayList<>();
	private int pinNum = 1;

	public Chip() {
	}

	/**
	 * Новая сторона чипа. Группируем лапки.
	 */
	protected void addSide() {
		currentSide++;
	}

	protected void addPin(String port, String... params) {
		// int pinNum,
		Pin pin = new Pin();
		pin.Side = currentSide;
		pin.port = port;
		pin.Num = pinNum ++;
		pin.params = params;
		pins.add(pin);
	}

	public void PrintPinsByParam(String param) {
		for (Pin pin : pins)
			if (pin.FindByParam(param))
				pin.Print();
	}

	List<String> skipPins = new ArrayList<>();

	protected void addSkipPin(String param) {
		skipPins.add(param);
	}

	public void CheckDuplicates(String... except) throws Exception {
		List<String> exl = Arrays.asList(except);
		List<String> s = new ArrayList<>();
		for (Pin pin : pins)
			for (String p : pin.params)
				if (!exl.contains(p))
					s.add(p);

		List<String> duplicates = s.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))//
				.entrySet().stream()//
				.filter(e -> e.getValue() > 1)//
				.map(e -> e.getKey())//
				.collect(Collectors.toList());

		if (duplicates.size() != 0)
			throw new Exception("В параметрах есть дубликаты: " + duplicates);
	}
}