package ringle.backend.assignment.repository;
import jakarta.transaction.Transactional;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;
import ringle.backend.assignment.domain.Lecture;
import ringle.backend.assignment.domain.enums.LectureType;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

// *
// * 예약 시나리오 Test * //
// 2개의 스레드가 동시에 실행됨
// 첫번째 트랜잭션 => 1개의 스레드만 예약 요청이 통과되고,
// 2번째 트랜잭션 => 1개의 스레드는 예약 요청이 실패하여야 함 (OptimisticLockExcpetion 발생하여야함)
// *
@SpringBootTest
public class LectureRepositoryTest {
    @Autowired
    private LectureRepository lectureRepository;
    @Transactional
    public void updateLecture(Long id) {
        Lecture lecture = lectureRepository.findById(id).orElseThrow();
        lecture.setAvailable(false);
        lectureRepository.saveAndFlush(lecture);
    }
    @Test
    @Transactional
    public void testOptimisticLock() {

        // 첫 번째 트랜잭션 실행
        new Thread(() -> {
            try {
                updateLecture(1L); // 첫 번째 트랜잭션
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // 두 번째 트랜잭션 실행
        new Thread(() -> {
            try {
                updateLecture(1L); // 두 번째 트랜잭션
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
