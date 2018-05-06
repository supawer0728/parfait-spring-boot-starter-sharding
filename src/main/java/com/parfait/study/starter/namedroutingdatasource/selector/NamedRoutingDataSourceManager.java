package com.parfait.study.starter.namedroutingdatasource.selector;

import org.springframework.core.NamedThreadLocal;

public class NamedRoutingDataSourceManager {

	private static final ThreadLocal<String> currentDataSourceName = new NamedThreadLocal<>("name of DataSource selected in this thread");

	private NamedRoutingDataSourceManager() {
		throw new UnsupportedOperationException("this class can't be instance");
	}

	public static String getCurrentDataSourceName() {
		return currentDataSourceName.get();
	}

	public static void setCurrentDataSourceName(String name) {
		currentDataSourceName.set(name);
	}

	public static void removeCurrentDataSourceName() {
		currentDataSourceName.remove();
	}
}
