package com.orm.core;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public abstract class JdbcWapper {
	
	private Logger logger=LoggerFactory.getLogger(getClass());

	//TODO 1�����������Ƿ���ҪȻ�����ȡ��? 2����װһ������������ת���ɼ���
	private Connection connection;
	protected PreparedStatement statement;
	protected ResultSet resultSet;
	private ComboPooledDataSource dataSource;

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
			String driver = properties.getProperty("jdbc.driver");
			String url = properties.getProperty("jdbc.url");
			String user = properties.getProperty("jdbc.user");
			String password = properties.getProperty("jdbc.password");

			/*
			Class.forName(driver);
			
			// ��ȡ���ݿ����Ӷ���
			connection = DriverManager.getConnection(url,user, password);
			*/
			this.dataSource=new ComboPooledDataSource();
			dataSource.setDriverClass(driver);
			dataSource.setUser(user);
			dataSource.setJdbcUrl(url);
			dataSource.setPassword(password);
			logger.debug("����Դ��ʼ�����");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	protected int executeUpdate(String sql, List<Object> values) throws Exception {
		logger.debug(sql);
		try {
			connection = this.dataSource.getConnection();
			statement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
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
	 * @param sql ִ�е�sql
	 * @param values ��ѯ������ֵ
	 * @return
	 * @throws Exception
	 */
	protected ResultSet executeQurey(String sql, List<Object> values) throws Exception {
		try {
			connection = this.dataSource.getConnection();
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

	/**
	 * �ر����ݿ�����
	 * @throws Exception
	 */
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
		logger.debug("�ͷ����ݿ�������ϣ�");
	}

	/**
	 * �ͷ�����Դ
	 */
	protected void releaseDataSource(){
		if(dataSource!=null){
			dataSource.close();
			logger.debug("�ͷ�����Դ��Դ");
		}
	}
	
}
