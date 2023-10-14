package com.sbs.apple.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReportService {
    private final ReportRepository reportRepository;
    public void create(String subject, String content) {
        Report r = new Report();
        r.setSubject(subject);
        r.setContent(content);
        this.reportRepository.save(r);
    }
}
