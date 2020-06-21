package maxzawalo.pcb_design;

public class Main {

	public static void main(String[] args) throws Exception {
		// Цель: Использовать макетку (ардуино) - не меняя/добавляя разъемы и разводку.
		// Подгоняем под нее внешние коннекторы, чтобы не было пересечений.
		// Не надо будет тратить время на ручную (время+нервы)/авто (корявую) разводку.
		// Генерируем сразу код.
		// Экономим время и деньги.

		Chip chip = new Atmega328p_TQFP32();
		chip.PrintPinsByParam("");
		Solver solver = new Solver();
		solver.addChip(chip);

		// Эмпирич.
		// Начинаем соединения с уникальных пинов. Хотя и в противном случае
		// находит...иногда.
		Connector conn;
		conn = solver.addConnector("UART");
		conn.addPin("TXD", "TXD");
		conn.addPin("RXD", "RXD");
		conn.addPin("GND", "GND");

		conn = solver.addConnector("I2C");// резерв для компаса итп
		conn.addPin("SDA", "SDA");
		conn.addPin("SCL", "SCL");

		conn = solver.addConnector("SONAR1");
		conn.addPin("GND", "GND");// Игнорим при комбинаторике
		conn.addPin("Echo", "INT");
		conn.addPin("Trig", "PCINT");
		conn.addPin("VCC", "VCC");// Игнорим при комбинаторике

		conn = solver.addConnector("PWR");
		conn.addPin("LVL", "ADC");
		// conn.addPin("GND", "GND");
		// conn = solver.addConnector("PWR");
		conn.addPin("CUR", "ADC");
		conn.addPin("GND", "GND");

		// TODO: Параметр указывающий, что можно не подряд
		// или вообще раскидать в любые точки - разбивает на несколько коннектров/групп
		// conn = solver.addConnector("DRIVE1");
		// conn.addPin("IN1", "PCINT");
		// conn.addPin("IN2", "PCINT");
		// conn.addPin("LEVEL", "PWM");
		// conn = solver.addConnector("DRIVE2");
		// conn.addPin("IN1", "PCINT");
		// conn.addPin("IN2", "PCINT");
		// conn.addPin("LEVEL", "PWM");

		// Эмпирич.
		// Длинные разъемы вверх
		// Разъем как у L298N. Для шлейфа.
		conn = solver.addConnector("DRIVE");
		conn.addPin("ENA", "PWM");
		conn.addPin("IN1", "PCINT");
		conn.addPin("IN2", "PCINT");
		conn.addPin("IN3", "PCINT");
		conn.addPin("IN4", "PCINT");
		conn.addPin("ENB", "PWM");

		conn = solver.addConnector("CPU");
		conn.addPin("EN", "PCINT");
		conn.addPin("GND", "GND");

		conn = solver.addConnector("BROOM");
		conn.addPin("EN", "PCINT");
		conn.addPin("GND", "GND");

		conn = solver.addConnector("VAC_CLEAN");
		conn.addPin("EN", "PCINT");
		conn.addPin("GND", "GND");

		conn = solver.addConnector("ENCODER_L");
		conn.addPin("IN", "PCINT");
		conn.addPin("GND", "GND");

		conn = solver.addConnector("ENCODER_R");
		conn.addPin("IN", "PCINT");
		conn.addPin("GND", "GND");

		conn = solver.addConnector("SENS_L");
		conn.addPin("IN", "PCINT");
		conn.addPin("GND", "GND");

		conn = solver.addConnector("SENS_R");
		conn.addPin("IN", "PCINT");
		conn.addPin("GND", "GND");

		// ----------------------------------
		solver.addIgnoredPin("GND");
		solver.addIgnoredPin("VCC");

		solver.findFullSolution();

		// Публикуем ранее найденное решение
		// [31, 30, 27, 28, 22, 9, 10, 11, 12, 13, 14, 24, 1, 2, 26, 23, 25, 32, 15, 17]
		// [31, 30, 27, 28, 1, 2, 19, 10, 11, 12, 13, 14, 15, 26, 23, 32, 25, 17, 9, 24]
		// solver.PrintSolution(31, 30, 27, 28, 19, 15, 14, 13, 12, 11, 10, 9, 1, 2, 23,
		// 32, 17, 24, 25, 16);

		// TODO: Ограничения
		// -разъем на стороне Х
	}
}