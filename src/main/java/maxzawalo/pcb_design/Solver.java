package maxzawalo.pcb_design;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

import maxzawalo.genetic.AlgorithmPB;
import maxzawalo.genetic.ITargetFunction;
import maxzawalo.genetic.Individual;

public class Solver implements ITargetFunction {

	private Chip chip;
	private List<Connector> connectors = new ArrayList<>();

	public void addChip(Chip chip) {
		this.chip = chip;
	}

	public Connector addConnector(String name) {
		Connector conn = new Connector(name);
		connectors.add(conn);
		return conn;
	}

	int SOLUTION_FITNESS = 1000;

	public boolean findFullSolution() {
		System.out.println("Кол-во разъемов: " + connectors.size());

		int genSize = filteredConnectorPins().size();
		System.out.println("Длина гена: " + genSize);

		List<int[]> comb = new ArrayList<>();
		AlgorithmPB genAlgo = new AlgorithmPB();
		genAlgo.minFitness = SOLUTION_FITNESS - 1;//Поднимаем планку, чтобы было к чему стремиться.
		genAlgo.solutionFitness = SOLUTION_FITNESS;
		genAlgo.populationSize = genSize * 2;
		genAlgo.maxGenerationCount = 100000;
		genAlgo.geneSize = genSize;
		AlgorithmPB.posible_values = new int[genSize][];

		long start = System.currentTimeMillis();
		int shuffleCount = 1;
		for (; shuffleCount <= 1000; shuffleCount++) {
			// Волшебный ШУФЛ задает последовательность соединений
			Collections.shuffle(connectors);
			List<String> params = filteredConnectorPins();
			for (int pos = 0; pos < genSize; pos++) {
				String param = params.get(pos);
				// Ограничиваем кол-во вариантов возможными значениями
				// для каждой хромосомы (лапки чипа)
				List<Integer> values = chip.pins.stream()//
						.filter(p -> p.FindByParam(param))//
						.map(p -> p.Num)//
						.collect(Collectors.toList());
				AlgorithmPB.posible_values[pos] = new int[values.size()];
				for (int i = 0; i < values.size(); i++)
					AlgorithmPB.posible_values[pos][i] = values.get(i);

				// // Без фильтрации допустимых значений так же находит
				// // за пару шуфлов и 1М эпох
				// AlgorithmPB.posible_values[pos] = new int[chip.pins.size()];
				// for (int i = 0; i < chip.pins.size(); i++)
				// AlgorithmPB.posible_values[pos][i] = i + 1;
			}
			long variants = 1;
			for (int pos = 0; pos < genSize; pos++)
				variants *= AlgorithmPB.posible_values[pos].length;
			System.out.println("Кол-во вариантов: " + variants);
			AlgorithmPB.tf = this;
			Individual sol = genAlgo.Run();
			if (sol != null)
				comb.add(sol.getGene());

			if (comb.size() != 0)
				break;
		}
		long elapsedTimeMillis = System.currentTimeMillis() - start;

		for (int[] variant : comb)
			System.out.println(Arrays.toString(variant));

		PrintSolution(comb.get(0));

		System.out.println("Кол-во шуфлов: " + shuffleCount);
		System.out
				.println("Затрачено " + elapsedTimeMillis / 1000F + "c | " + elapsedTimeMillis / (60 * 1000F) + "мин");
		return true;
	}

	private List<String> filteredConnectorPins() {
		return connectors.stream()//
				.flatMap(c -> c.pins.stream())//
				.filter(p -> !ignoredPins.contains(p.getValue()))//
				.map(p -> p.getValue())//
				.collect(Collectors.toList());
	}

	/**
	 * Публикуем ранее найденное решение. Зависит от правильной последвательности
	 * connectors.
	 * 
	 * @param sol
	 */
	protected void PrintSolution(int... sol) {
		System.out.println("------Solution-----");
		List<Pin> pins = Pins4Sol();
		Map<Integer, String> sss = new HashMap<>();

		int pos = 0;
		for (Connector conn : connectors) {
			for (KeyValuePair cp : conn.pins) {
				if (ignoredPins.contains(cp.getValue()))
					continue;
				int pinNum = sol[pos];
				Optional<Pin> found = pins.stream()//
						.filter(p -> p.Num == pinNum)//
						.findFirst();

				pins.remove(found.get());
				// System.out.println(pinNum + "\t" + conn.name + "." + cp.getKey() + "\t\t" +
				// found.get());
				sss.put(pinNum, conn.name + "." + cp.getKey());
				pos++;
			}
		}
		// Сортируем по пинам
		TreeSet<Integer> sortSet = new TreeSet<>();
		sortSet.addAll(sss.keySet());
		for (int pinNum : sortSet)
			System.out.println(pinNum + "\t" + sss.get(pinNum));

		System.out.println("Остались свободны на чипе");
		for (Pin pin : pins)
			System.out.println(pin);
	}

	public boolean findSimpleSolution() {
		List<Pin> pins = Pins4Sol();
		List<Wire> wires = new ArrayList<>();

		for (Connector conn : connectors) {
			for (KeyValuePair cp : conn.pins) {
				if (ignoredPins.contains(cp.getValue()))
					continue;

				Optional<Pin> found = pins.stream()//
						.filter(p -> p.FindByParam(cp.getValue()))//
						.findFirst();

				if (found.isPresent()) {
					Wire wire = new Wire(conn, cp.getKey(), found.get());
					wires.add(wire);
					pins.remove(found.get());
				} else {
					System.err.println("Нет подходящего пина для " + cp.getKey() + "-" + cp.getValue());
					return false;
				}
			}
		}

		System.out.println("-------------------");
		for (Wire w : wires)
			System.out.println(w);

		return true;
	}

	private List<Pin> Pins4Sol() {
		List<Pin> pins = new ArrayList<>();
		pins.addAll(chip.pins);

		for (String param : chip.skipPins)
			pins.removeIf(p -> p.FindByParam(param));
		return pins;
	}

	List<String> ignoredPins = new ArrayList<>();

	public void addIgnoredPin(String param) {
		ignoredPins.add(param);
	}

	@Override
	public int check(int[] gen) {
		// Без дубликатов
		int res = (int) (Arrays.stream(gen).distinct().count() - Arrays.stream(gen).count());
		if (res >= 0) {
			List<Pin> pins = Pins4Sol();
			List<Wire> wires = new ArrayList<>();

			int pos = 0;
			for (Connector conn : connectors) {
				List<Wire> connectorsWires = new ArrayList<>();
				// TODO: попробовать переворот(доп хромосомы 0/1)
				for (KeyValuePair cp : conn.pins) {
					if (ignoredPins.contains(cp.getValue()))
						continue;
					int pinNum = gen[pos];
					Optional<Pin> found = pins.stream()//
							.filter(p -> p.Num == pinNum)//
							.filter(p -> p.FindByParam(cp.getValue()))//
							.findFirst();

					if (found.isPresent()) {
						Wire wire = new Wire(conn, cp.getKey(), found.get());
						connectorsWires.add(wire);
						pins.remove(found.get());
					} else {
						// System.err.println("Нет подходящего пина для " + cp.getKey() + "-" +
						// cp.getValue());
						res = gen.length + wires.size();
						return res;
					}
					pos++;
				}
				// Проверяем рядом стоящие пины в коннекторе
				for (int i = 1; i < connectorsWires.size(); i++) {
					Wire prevWire = connectorsWires.get(i - 1);
					Wire wire = connectorsWires.get(i);
					if (Math.abs(prevWire.pin.Num - wire.pin.Num) > 1) {
						// Разрыв в пинах коннектора. Возможно пересечение.
						// Пропускаем GND и VCC - надо же их как то выводить.
						// Хотя XTAL можно ближе к чипу, а коннекторы далее располагать.
						// В atmega328p не актуально - рядом с XTAL только GND и VCC с одной стороны

						// Каждое ограничение снижает кол-во вариантов и увеличивает сложность поиска
						// ген. алгоритмом. Возможно переключаться на перебор.
						res = gen.length + wires.size() + i;// Чем больше соединили в коннекторе - тем выше ЦФ
						return res;
					}
				}
				// Закидываем в общий список
				wires.addAll(connectorsWires);
			}
			// Соединили ВСЕ - решение найдено
			res = SOLUTION_FITNESS;
			return res;
		}
		res += gen.length;
		return res;
	}
}