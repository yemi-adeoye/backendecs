package com.playground.api.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.playground.api.enums.LeaveEnum;
import com.playground.api.enums.RecordStatus;
import com.playground.api.model.Employee;
import com.playground.api.model.Leave;

public interface LeaveRepository extends JpaRepository<Leave, Long>{

	@Query("select l from Leave l "
			+ " where l.employee.user.username=?1 "
			+ " AND l.status = ?2 AND l.recordStatus=?3")
	List<Leave> getLeavesByEmployeeUsername(String username,
			LeaveEnum status, RecordStatus recordStatus);

	@Transactional
	@Modifying
	@Query("update Leave l SET l.status=?2 where l.id=?1")
	void updateStatusByid(Long leaveID, LeaveEnum statusVal);

	@Query("select l.employee from Leave l where l.id=?1")
	Employee getEmployeeByLeaveId(Long leaveID);

	@Query("select l from Leave l where l.status=?1 and l.recordStatus=?2")
    List<Leave> getAllByStatusAndRecordStatus(LeaveEnum pending, RecordStatus active);

}
