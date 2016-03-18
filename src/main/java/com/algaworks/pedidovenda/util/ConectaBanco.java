package com.algaworks.pedidovenda.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectaBanco {
	private static final String URL_MYSQL = "jdbc:mysql://localhost/ccicons_01";
	private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
	private static final String USER = "ccicons_user";
	private static final String PASS = "p3p3r0n1#";

	public static Connection getConnection() {
		System.out.println("Conectando ao Banco de Dados");
		try {
			Class.forName(DRIVER_CLASS);
			return DriverManager.getConnection(URL_MYSQL, USER, PASS);
		} catch (ClassNotFoundException e) {
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

}
