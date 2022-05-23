package Miary.miniWeb.service;

import Miary.miniWeb.MemberManager.profile.Profile;
import Miary.miniWeb.MemberManager.profile.ProfileRepository;
import Miary.miniWeb.MemberManager.profile.profileImage.ProfileImage;
import Miary.miniWeb.MemberManager.profile.profileImage.ProfileImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class ProfileImageService {

    @Autowired
    ProfileImageRepository profileImageRepository;
    @Autowired
    ProfileRepository profileRepository;

    @Value("${file.dir}")
    private String fileDir;

    public List<ProfileImage> findProfile(Profile profile) {
        return profileImageRepository.findProfile(profile);
    }

    @Transactional
    public Long deleteProfileImage(Profile profile) {
        List<ProfileImage> profile1 = profileImageRepository.findProfile(profile);
        if(!profile1.isEmpty()){
            ProfileImage remove = profile1.remove(0);
            profileImageRepository.delete(remove);
        }
        return profile.getProfileIdx();
    }

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public List<ProfileImage> storeFiles(List<MultipartFile> multipartFiles, Profile profile) throws IOException {
        List<ProfileImage> storeFileResult = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile, profile));
            }
        }
        return storeFileResult;
    }

    private ProfileImage storeFile(MultipartFile multipartFile, Profile profile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        ProfileImage profileImage = new ProfileImage();
        profileImage.setUploadFileName(originalFilename);
        profileImage.setStoreFileName(storeFileName);
        profileRepository.save(profile);
        profileImage.setProfileImage(profile);

        profileImageRepository.save(profileImage);

        return profileImage;
    }


    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;

    }

    //확장자를 별도로 추출해 서버 내부에서 관리하는 파일명에도 붙여준다.
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
