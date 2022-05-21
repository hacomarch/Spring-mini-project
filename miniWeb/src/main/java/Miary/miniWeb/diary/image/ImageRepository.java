package Miary.miniWeb.diary.image;

import Miary.miniWeb.diary.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> , CustomImageRepository{

}
