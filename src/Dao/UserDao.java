package Dao;

import entity.User;

public interface UserDao {
	//��ѯ���ݿ����Ƿ����ĳ���û�
	public abstract boolean User_check(String username);
	//�û�ע��
	public abstract void User_Login(User user);
}
