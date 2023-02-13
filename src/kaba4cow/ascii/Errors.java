package kaba4cow.ascii;

public enum Errors {

	UNKNOWN(0x0000, "Unknown error"), //

	LOAD_ICON(0x0100, "Failed to load icon"), //
	LOAD_IMAGE(0x0101, "Failed to load image"), //

	FILE_CONFIG_LOAD(0x0200, "Failed to load config file"), //
	FILE_CONFIG_READ(0x0201, "Failed to read config file"), //
	FILE_CONFIG_CREATE(0x0202, "Failed to create config file"), //
	FILE_CONFIG_SAVE(0x0203, "Failed to save config file"), //

	FILE_DATA_LOAD(0x0210, "Failed to load data file"), //
	FILE_DATA_READ(0x0211, "Failed to read data file"), //
	FILE_DATA_CREATE(0x0212, "Failed to create data file"), //
	FILE_DATA_SAVE(0x0213, "Failed to save data file"), //

	FILE_TABLE_LOAD(0x0220, "Failed to load table file"), //
	FILE_TABLE_READ(0x0221, "Failed to read table file"), //
	FILE_TABLE_CREATE(0x0222, "Failed to create table file"), //
	FILE_TABLE_SAVE(0x0223, "Failed to save table file");

	private final int code;
	private final String message;

	private Errors(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
