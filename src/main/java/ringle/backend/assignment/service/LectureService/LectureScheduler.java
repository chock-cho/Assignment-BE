package ringle.backend.assignment.service.LectureService;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ringle.backend.assignment.repository.LectureRepository;

@Component
@RequiredArgsConstructor

public class LectureScheduler {
    private final LectureRepository lectureRepository;

    /**
     * 예약되지 않은 수업 정보 삭제: 매일 자정 실행
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteUnreservedLectures() {
        int deletedCount = lectureRepository.deleteUnreservedLectures();
        System.out.println("자정 스케줄 실행: " + deletedCount + "개의 예약되지 않은 수업 삭제 완료");
    }
}
