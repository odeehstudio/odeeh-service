package be.odeeh.studio.odeehservice.adapter.out.repository;

import be.odeeh.studio.odeehservice.domain.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AttendanceJpaRepository extends JpaRepository<AttendanceEntity, UUID> {
}
