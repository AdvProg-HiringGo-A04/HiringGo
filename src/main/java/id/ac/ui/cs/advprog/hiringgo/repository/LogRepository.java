package id.ac.ui.cs.advprog.hiringgo.repository;

import id.ac.ui.cs.advprog.hiringgo.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface LogRepository extends JpaRepository<Log, String> {

    List<Log> findByMahasiswaNPM(String NPM);

    List<Log> findByTanggalLogBetweenAndMahasiswaNPM(Date startDate, Date endDate, String npm);
}
