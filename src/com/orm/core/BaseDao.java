package com.orm.core;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.orm.annotation.Column;
import com.orm.annotation.ID;
import com.orm.annotation.Table;

@SuppressWarnings("unchecked")
public abstract class BaseDao<T> extends JdbcWapper {

	/**
	 * �����ݱ��浽���ݿ���
	 * @param t ��������ݵĶ���
	 * @return
	 * @throws Exception
	 */
	public final int saveEntity(T t) throws Exception {
		try {
			// Ŀ���sql:inser into users () values(?,?,?)
			StringBuffer buffer = null;
			Class<T> clazz = (Class<T>) t.getClass();
			// �ж��Ƿ����Tableע��
			boolean flag = clazz.isAnnotationPresent(Table.class);
			if (!flag) {// û��ע�����׳��쳣
				throw new Exception(clazz.getName() + "û�����Tableע��");
			}
			// ��ע��--->StringBuffer����׼��ƴ��sql���
			buffer = new StringBuffer();
			buffer.append("insert into ");
			// ��ȡ���ע��
			Table table = clazz.getAnnotation(Table.class);
			buffer.append(table.name() + "(");
			// ƴ�Ӳ������---->�����Ӧ���Ե�ֵ
			Field[] fields = clazz.getDeclaredFields();
			if (fields != null && fields.length > 0) {
				List<Object> values = new ArrayList<Object>();
				for (Field field : fields) {
					//�ų�static �� final���ε�����
					int modifiers = field.getModifiers();
					if((modifiers&Modifier.FINAL)!=0||(modifiers&Modifier.STATIC)!=0){
						continue;
					}
					// ����Ϊ���Է��ʸ����Ե�ֵ
					field.setAccessible(true);
					// �ж��Ƿ�Ϊ����
					boolean isID = field.isAnnotationPresent(ID.class);
					if (isID) {
						ID id = field.getAnnotation(ID.class);
						if (!id.isAutoIncrement()) {// ��������������Ҫ�ڳ���������и�ֵ
							buffer.append(id.name() + ",");
							// �洢id��ֵ
							values.add(field.get(t));
						}
					} else {// ��ͨ��
						boolean isColumn = field.isAnnotationPresent(Column.class);
						if(isColumn){
							Column column = field.getAnnotation(Column.class);
							buffer.append(column.name() + ",");
							// �洢ֵ
							values.add(field.get(t));
						}
					}
				}
				// ɾ�������","
				buffer.deleteCharAt(buffer.length() - 1).append(") values(");
				// ���������ֵ�ļ���--->����������
				for (int i = 0; i < values.size(); i++) {
					buffer.append("?").append(",");
				}
				// ɾ�����һ������
				buffer.deleteCharAt(buffer.length() - 1).append(")");
				// jdbc����Ĳ���--->�����ݱ��浽���ݿ�
				System.out.println("ִ�е�sql���Ϊ��" + buffer);
				return executeUpdate(buffer.toString(), values);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			closeResouce();
		}
		return 0;
	}

	public final T saveEntityReturnID(T t) throws Exception {
		try {
			// Ŀ���sql:inser into users () values(?,?,?)
			StringBuffer buffer = null;
			Class<T> clazz = (Class<T>) t.getClass();
			// �ж��Ƿ����Tableע��
			boolean flag = clazz.isAnnotationPresent(Table.class);
			if (!flag) {// û��ע�����׳��쳣
				throw new Exception(clazz.getName() + "û�����Tableע��");
			}
			// ��ע��--->StringBuffer����׼��ƴ��sql���
			buffer = new StringBuffer();
			buffer.append("insert into ");
			// ��ȡ���ע��
			Table table = clazz.getAnnotation(Table.class);
			buffer.append(table.name() + "(");
			// ƴ�Ӳ������---->�����Ӧ���Ե�ֵ
			Field[] fields = clazz.getDeclaredFields();
			// �洢id��Ӧ���ֶ�
			Field idField = null;
			if (fields != null && fields.length > 0) {
				List<Object> values = new ArrayList<Object>();
				for (Field field : fields) {
					//�ų�static �� final���ε�����
					int modifiers = field.getModifiers();
					if((modifiers&Modifier.FINAL)!=0||(modifiers&Modifier.STATIC)!=0){
						continue;
					}
					// ����Ϊ���Է��ʸ����Ե�ֵ
					field.setAccessible(true);
					// �ж��Ƿ�Ϊ����
					boolean isID = field.isAnnotationPresent(ID.class);
					if (isID) {
						idField = field;
						ID id = field.getAnnotation(ID.class);
						if (!id.isAutoIncrement()) {// ��������������Ҫ�ڳ���������и�ֵ
							buffer.append(id.name() + ",");
							// �洢id��ֵ
							values.add(field.get(t));
						}
					} else {// ��ͨ��
						boolean isColumn = field.isAnnotationPresent(Column.class);
						if(isColumn){
							Column column = field.getAnnotation(Column.class);
							buffer.append(column.name() + ",");
							// �洢ֵ
							values.add(field.get(t));
						}
					}
				}
				// ɾ�������","
				buffer.deleteCharAt(buffer.length() - 1).append(") values(");
				// ���������ֵ�ļ���--->����������
				for (int i = 0; i < values.size(); i++) {
					buffer.append("?").append(",");
				}
				// ɾ�����һ������
				buffer.deleteCharAt(buffer.length() - 1).append(")");
				// jdbc����Ĳ���--->�����ݱ��浽���ݿ�
				System.out.println("ִ�е�sql���Ϊ��" + buffer);
				// �����ݱ��浽���ݿ���
				this.executeUpdate(buffer.toString(), values);

				// ��������������ֵ
				ID id = idField.getAnnotation(ID.class);
				if (id.isAutoIncrement()) {// ���ֻ��������
					// ��ѯ�õ�id
					String sql = "SELECT LAST_INSERT_ID()";
					resultSet = this.executeQurey(sql, null);
					while (resultSet.next()) {
						Object value = resultSet.getObject(1);
						if (value instanceof BigInteger) {
							value = ((BigInteger) value).intValue();
						}
						idField.setAccessible(true);
						idField.set(t, value);
					}
				}
			}
			return t;
		} catch (Exception e) {
			throw e;
		} finally {
			closeResouce();
		}
	}

	public final int updateEntity(T t) throws Exception {
		try {
			// update users set username=?,address=? where id=?
			StringBuffer buffer = null;
			Class<T> clazz = (Class<T>) t.getClass();
			boolean flag = clazz.isAnnotationPresent(Table.class);
			if (!flag) {
				throw new Exception(clazz.getName() + "û�����Tableע��");
			}
			buffer = new StringBuffer();
			buffer.append("update ");
			// ȷ�����±�
			Table table = clazz.getAnnotation(Table.class);
			buffer.append(table.name()).append(" ");
			// ȷ��������
			// ��ȡ�����е�����
			Field[] fields = clazz.getDeclaredFields();
			List<Object> values = null;
			if (fields != null && fields.length > 0) {
				// ��������洢id����Ӧ���ֶ�
				Field idField = null;

				// ��ʼ�����ݵĴ洢����
				values = new ArrayList<Object>();
				buffer.append("set ");
				for (Field field : fields) {// ������ͨ������---->�Ƿ���ڵ���column����ע�������
					//�ų�static �� final���ε�����
					int modifiers = field.getModifiers();
					if((modifiers&Modifier.FINAL)!=0||(modifiers&Modifier.STATIC)!=0){
						continue;
					}
					field.setAccessible(true);
					boolean isColum = field.isAnnotationPresent(Column.class);
					if (isColum) {// ��ȡֵ ƴ��sql����
						// �Ƿ��ǳ�ʼֵ����
						Object value = field.get(t);
						if (value != null && !isInitValue(value)) {// ---���������������������ͺ͸������͵�����
							Column column = field.getAnnotation(Column.class);
							buffer.append(column.name()).append("=").append("?").append(",");
							// �洢���е�ֵ
							values.add(value);
						}
					} else {
						boolean isID = field.isAnnotationPresent(ID.class);
						if (isID) {
							idField = field;// ����ǰѭ���õ���field��ֵ��
						}
					}
				}
				// ���û����ͨ��--->update users set
				if (values.isEmpty()) {
					throw new Exception(clazz.getName() + "û��ָ�����µ���");
				}
				// ɾ�����һ������
				buffer.deleteCharAt(buffer.length() - 1).append(" ");
				// ���������ֶ�--->��������
				if (idField != null) {// ָ����id����
					idField.setAccessible(true);
					Object value = idField.get(t);
					if (value != null && !isInitValue(value)) {
						buffer.append("where ");
						// �õ�id��ע��
						ID id = idField.getAnnotation(ID.class);
						buffer.append(id.name()).append("=").append("?");
						// ��id��ֵ
						values.add(value);
					} else {// id������ǳ�ʼֵ������
						throw new Exception(clazz.getName() + "û���ƶ�����������ֵ");
					}
				} else {
					throw new Exception(clazz.getName() + "û���ƶ�����������ID����");
				}

				// ��ɸ��²���
				System.out.println(buffer.toString());
				return this.executeUpdate(buffer.toString(), values);
			} else {
				throw new Exception(clazz.getName() + "û������κ�����");
			}
		} catch (Exception e) {
			throw e;
		}finally {
			closeResouce();
		}
	}

	public final int deleteById(T t) throws Exception {
		try {
			// Ŀ��sql��delete from users where id=?
			StringBuffer buffer = null;
			Class<T> clazz = (Class<T>) t.getClass();
			boolean flag = clazz.isAnnotationPresent(Table.class);
			if (!flag) {
				throw new Exception(clazz.getName() + "û�����Tableע��");
			}
			buffer = new StringBuffer();
			buffer.append("delete from ");
			// ��ȡtableע��
			Table table = clazz.getAnnotation(Table.class);
			buffer.append(table.name());
			// ��ȡ����
			Field[] fields = clazz.getDeclaredFields();
			List<Object> values;
			if (fields != null && fields.length > 0) {
				values = new ArrayList<Object>();
				for (int i = 0; i < fields.length; i++) {// ��ʼ��id ����
					Field field = fields[i];
					//�ų�static �� final���ε�����
					int modifiers = field.getModifiers();
					if((modifiers&Modifier.FINAL)!=0||(modifiers&Modifier.STATIC)!=0){
						continue;
					}
					boolean isID = field.isAnnotationPresent(ID.class);
					if (isID) {// ����ɾ��������------>�����ʾΪid�����Բ����ڵ�һ��
						field.setAccessible(true);
						// ��ȡ���Ե�ֵ
						Object value = field.get(t);
						if (value != null && !isInitValue(value)) {// �ų���ʼֵ
							ID id = field.getAnnotation(ID.class);
							buffer.append(" where ").append(id.name()).append("=?");
							values.add(value);
						} else {
							throw new Exception(clazz.getName() + "����IDע�⵫��û�и�id���Խ��и�ֵ");
						}
						// �ж��Ƿ�Ϊ��ʼֵ������
						break;
					} else if (i == fields.length - 1) {// ����forѭ��ִ�е����һ��ѭ��
						throw new Exception(clazz.getName() + "û�����ID��ʾ������");
					}
				}
			} else {
				throw new Exception(clazz.getName() + "û������κ�����");
			}
			System.out.println("ִ�е�sql���Ϊ��" + buffer);
			return executeUpdate(buffer.toString(), values);
		} catch (Exception e) {
			throw e;
		}finally{
			closeResouce();
		}
	}

	/**
	 * ����ID��ѯ����
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public final T findEntityById(Serializable id) throws Exception {// --->�ж�id�����Ƿ���Ч
		// Ŀ��sql��select * from users where id=?
		StringBuffer buffer = null;
		// ��ȡ����ķ��ͻ�����
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();// ���͸���
		Class<T> clazz = (Class<T>) type.getActualTypeArguments()[0];
		boolean flag = clazz.isAnnotationPresent(Table.class);
		if (!flag) {
			throw new Exception(clazz.getName() + "û�����Tableע��");
		}
		buffer = new StringBuffer();
		buffer.append("select * from ");
		// ȷ����ѯ�ı�
		Table table = clazz.getAnnotation(Table.class);
		buffer.append(table.name());

		Field[] fields = clazz.getDeclaredFields();
		if (fields != null && fields.length > 0) {
			// ��ʼƴ��id��ѯ����
			for (int i = 0; i < fields.length; i++) {// �������м���IDע������ԣ�û��
				Field field = fields[i];
				//�ų�static �� final���ε�����
				int modifiers = field.getModifiers();
				if((modifiers&Modifier.FINAL)!=0||(modifiers&Modifier.STATIC)!=0){
					continue;
				}
				boolean isID = field.isAnnotationPresent(ID.class);
				if (isID) {// ��ȡid���Զ�Ӧ�����ݿ��е��ֶ�
					buffer.append(" where ");
					buffer.append(field.getAnnotation(ID.class).name()).append("=").append("?");
					break;
				} else if (i == fields.length - 1) {// �ҵ����û���ҵ�ID�ֶ�
					throw new Exception(clazz.getName() + "û�����ID��ʾ������");
				}
			}
			try {
				System.out.println("ִ�е�sql���Ϊ��" + buffer.toString());
				// ���ݷ�װ��List����
				List<Object> values = new ArrayList<Object>();
				values.add(id);
				// ִ�в�ѯ�õ������
				resultSet = this.executeQurey(buffer.toString(), values);
				List<T> result = convertValues(clazz, fields);
				return result.isEmpty() ? null : result.get(0);
			} catch (Exception e) {
				throw e;
			} finally {// �ͷ���Դ
				this.closeResouce();
			}
		} else {
			throw new Exception(clazz.getName() + "û������κ�����");
		}
	}

	public final List<T> findEntity(T t) throws Exception {
		// Ŀ��sql��select * from users
		StringBuffer buffer = null;
		Class<T> clazz = (Class<T>) t.getClass();
		boolean flag = clazz.isAnnotationPresent(Table.class);
		if (!flag) {
			throw new Exception(clazz.getName() + "û�����Tableע��");
		}
		buffer = new StringBuffer();
		buffer.append("select * from ");
		// ȷ����ѯ�ı�
		Table table = clazz.getAnnotation(Table.class);
		buffer.append(table.name());

		Field[] fields = clazz.getDeclaredFields();
		if (fields != null && fields.length > 0) {
			try {
				System.out.println("ִ�е�sql���Ϊ��" + buffer.toString());
				// ִ�в�ѯ�õ������
				resultSet = this.executeQurey(buffer.toString(), null);
				List<T> result = convertValues(clazz, fields);
				return result.isEmpty() ? null : result;
			} catch (Exception e) {
				throw e;
			} finally {// �ͷ���Դ
				this.closeResouce();
			}
		} else {
			throw new Exception(clazz.getName() + "û������κ�����");
		}
	}

	private List<T> convertValues(Class<T> clazz, Field[] fields)
			throws SQLException, InstantiationException, IllegalAccessException {
		// ��ļ���
		List<T> result = new ArrayList<T>();
		T temp = null;
		// ��������
		while (resultSet.next()) {
			temp = clazz.newInstance();
			// ת��ÿ�е�����
			for (Field field : fields) {
				//�ų�static �� final���ε�����
				int modifiers = field.getModifiers();
				if((modifiers&Modifier.FINAL)!=0||(modifiers&Modifier.STATIC)!=0){
					continue;
				}
				String lable = null;// ����
				// ��ȡ����----> id colum
				boolean isID = field.isAnnotationPresent(ID.class);
				if (isID) {
					lable = field.getAnnotation(ID.class).name();
				} else {
					boolean isColum = field.isAnnotationPresent(Column.class);
					if (isColum) {
						lable = field.getAnnotation(Column.class).name();
					}
				}
				if (lable != null) {// �ҵ�����
					field.setAccessible(true);
					// �õ������������ݿ��е�ֵ
					Object value = resultSet.getObject(lable);
					// ���⴦��
					value = convertData(value);
					field.set(temp, value);
				}
			}
			result.add(temp);
			temp = null;
		}
		return result;
	}

	/**
	 * �������͵��������⴦��
	 * 
	 * @param value
	 * @return
	 */
	private Object convertData(Object value) {
		if (value instanceof BigDecimal) {
			return ((BigDecimal) value).floatValue();
		}
		if(value instanceof BigInteger){
			return ((BigInteger) value).intValue();
		}
		return value;
	}

	/**
	 * ���κ͸������͵�����
	 * 
	 * @param value
	 * @return
	 */
	private boolean isInitValue(Object value) {
		if (value.equals(0) || value.equals(0.0)) {
			return true;
		}
		return false;
	}

}
