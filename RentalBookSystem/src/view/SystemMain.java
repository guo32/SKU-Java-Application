package view;

import dao.RentalDao;

public class SystemMain {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SystemFrame s = new SystemFrame(null);
		
		RentalDao rentalDao = new RentalDao();
		rentalDao.updateAllStatus(); // ��ü�� ���� ���� ������Ʈ		
	}

}
