package com.orm.core;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

public abstract class JdbcWapper {

	//TODO 1�����������Ƿ���ҪȻ�����ȡ��? 2����װһ������������ת���ɼ���
	protected Connection connection;
	protected PreparedStatement statement;
	protected ResultSet resultSet;

	public JdbcWapper() {
		// Ĭ�ϵ������ļ�
		// 1����ǰ��Ŀ�ĸ�Ŀ¼���������ļ�
		InputStream is = JdbcWapper.class.getClassLoader().getResourceAsStream("jdbc.properties");
		if (is == null) {
			is = JdbcWapper.class.getResourceAsStream("jdbc.properties");
		}
		Properties properties = new Properties();
		try {
			properties.load(is);

			// ��������
			Class.forName(properties.getProperty("jdbc.driver"));
			// ��ȡ���ݿ����Ӷ���
			connection = DriverManager.getConnection(properties.getProperty("jdbc.url"),
					properties.getProperty("jdbc.user"), properties.getProperty("jdbc.password"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected int executeUpdate(String sql, List<Object> values) throws Exception {
		try {
			statement = connection.prepareStatement(sql);
			// ������
			for (int i = 1; i <= values.size(); i++) {
				statement.setObject(i, values.get(i - 1));
			}
			return statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * ��ѯ����
	 * 
	 * @param sql
	 *            ִ�е�sql
	 * @param values
	 *            ��ѯ������ֵ
	 * @return
	 * @throws Exception
	 */
	protected ResultSet executeQurey(String sql, List<Object> values) throws Exception {
		try {
			statement = connection.prepareStatement(sql);
			if (values != null) {// �ų�û�в�ѯ����
				// ������
				for (int i = 1; i <= values.size(); i++) {
					statement.setObject(i, values.get(i - 1));
				}
			}
			return statement.executeQuery();
		} catch (Exception e) {
			throw e;
		}

	}

	protected void closeResouce() throws Exception {
		if (resultSet != null) {
			resultSet.close();
		}
		if (statement != null) {
			statement.close();
		}
		if (connection != null) {
			connection.close();
		}
	}

}
