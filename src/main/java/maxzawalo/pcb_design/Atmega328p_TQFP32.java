package maxzawalo.pcb_design;

public class Atmega328p_TQFP32 extends Chip {
	public Atmega328p_TQFP32() throws Exception {
		super();
		firstPinClose2Last = true;

		addSide();
		addPin("PD3", "OC2B", "PWM", "INT1", "PCINT19");
		addPin("PD4", "T0", "XCK", "PCINT20");
		addPin("", "GND");// OR
		addPin("", "VCC");// OR
		addPin("", "GND");
		addPin("", "VCC");
		addPin("PB6", "OSC1", "XTAL1", "PCINT6");
		addPin("PB7", "OSC2", "XTAL2", "PCINT7");

		addSide();
		addPin("PD5", "OC0B", "PWM", "T1", "PCINT21");
		addPin("PD6", "OC0A", "PWM", "AIN0", "PCINT22");
		addPin("PD7", "AIN1", "PCINT23");
		addPin("PB0", "ACP1", "CLKO", "PCINT0");// CLKO|0?
		addPin("PB1", "PWM", "OC1A", "PCINT1");
		addPin("PB2", "SS", "PWM", "OC1B", "PCINT2");
		addPin("PB3", "MOSI", "PWM", "OC2A", "PCINT3");
		addPin("PB4", "MISO", "PCINT4");

		addSide();
		addPin("PB5", "SCK", "PCINT5");
		addPin("", "VCC");
		addPin("", "ADC6");
		addPin("", "AREF");
		addPin("", "GND");
		addPin("", "ADC7");
		addPin("PC0", "ADC0", "PCINT8");
		addPin("PC1", "ADC1", "PCINT9");

		addSide();
		addPin("PC2", "ADC2", "PCINT10");
		addPin("PC3", "ADC3", "PCINT11");
		addPin("PC4", "SDA", "ADC4", "PCINT12");
		addPin("PC5", "SCL", "ADC5", "PCINT13");
		addPin("PC6", "RESET", "PCINT14");// PCINT5
		addPin("PD0", "RXD", "PCINT16");
		addPin("PD1", "TXD", "PCINT17");
		addPin("PD2", "INT0", "PCINT18");

		addSkipPin("XTAL");

		// addSkipPin("MOSI");
		// addSkipPin("MISO");
		// addSkipPin("SCK");
		addSkipPin("RESET");

		CheckDuplicates("VCC", "GND", "PWM");
	}
}