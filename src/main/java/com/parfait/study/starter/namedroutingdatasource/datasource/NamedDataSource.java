package com.parfait.study.starter.namedroutingdatasource.datasource;

import javax.sql.DataSource;

import org.springframework.util.StringUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Delegate;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NamedDataSource implements DataSource {

	private final String name;
	private final String slaveOf;
	@Delegate(types = DataSource.class)
	private final DataSource delegate;

	public static NamedDataSource slaveOf(String slaveOf, String slaveSuffix, DataSource delegate) {
		return new NamedDataSource(slaveOf + slaveSuffix, slaveOf, delegate);
	}

	public static NamedDataSource asMaster(String name, DataSource delegate) {
		return new NamedDataSource(name, null, delegate);
	}

	public boolean isSlave() {
		return !StringUtils.isEmpty(slaveOf);
	}
}
