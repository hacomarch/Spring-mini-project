package Miary.miniWeb.diary;
import Miary.miniWeb.MemberManager.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, String>, CustomDiaryRepository {
    List<Diary> findByTitleContaining(String keyword);
    Page<Diary> findByMember(Member member, Pageable pageable);

}
