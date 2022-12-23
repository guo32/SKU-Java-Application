package view;

import dao.RentalDao;

public class SystemMain {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SystemFrame s = new SystemFrame(null);
		
		RentalDao rentalDao = new RentalDao();
		rentalDao.updateAllStatus(); // 연체된 도서 상태 업데이트		
	}

}
