package com.parfait.study.starter.namedroutingdatasource.properties;

import org.springframework.util.StringUtils;

import lombok.Data;

import com.zaxxer.hikari.HikariConfig;

@Data
public class NamedRoutingDataSourceTargetProperties {

	private String name;
	private String slaveOf;
	private HikariConfig hikari;

	public boolean isSlave() {
		return !StringUtils.isEmpty(slaveOf);
	}

	public void check() {
		boolean hasName = !StringUtils.isEmpty(name);
		if (hasName == isSlave()) {
			throw new IllegalStateException("name과 slaveOf, 둘 중 하나만 있어야 합니다");
		}
	}
}
