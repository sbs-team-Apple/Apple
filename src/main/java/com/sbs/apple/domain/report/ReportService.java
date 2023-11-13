package com.sbs.apple.domain.report;

import com.sbs.apple.domain.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReportService {
    private final ReportRepository reportRepository;
    public void create(SiteUser siteUser,String subject, String content) {
        Report r = new Report();
        r.setSiteUser(siteUser);
        r.setSubject(subject);
        r.setContent(content);
        r.setReported_user(siteUser.getNickname());
        this.reportRepository.save(r);
    }
    public List<Report> getList() {
        return this.reportRepository.findAll();
    }

}
