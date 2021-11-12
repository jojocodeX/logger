package org.bravo.logger.commons.enums;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum SqlCmd {

	UPDATE("UPDATE","更新"),
	INSERT("INSERT","插入"),
	DELETE("DELETE","删除");

	private String key;
	private String value;

	SqlCmd(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public static SqlCmd ofKey(String key) {
		Optional<SqlCmd> cmdOption = Arrays.stream(values())
				.filter(sqlCmd -> sqlCmd.getKey().equalsIgnoreCase(key))
				.findFirst();
		return cmdOption.orElse(null);
	}
}
