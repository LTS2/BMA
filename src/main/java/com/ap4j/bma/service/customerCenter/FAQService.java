package com.ap4j.bma.service.customerCenter;

import com.ap4j.bma.model.entity.customerCenter.FAQEntity;
import com.ap4j.bma.model.repository.FAQRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FAQService {
    @Autowired
    private FAQRepository faqRepository;

    public List<FAQEntity> getAllFAQ() {

        return faqRepository.findAll();
    }
    
    //검색
    public Page<FAQEntity> searchFaqByKeyword(String keyword, Pageable pageable) {
        return faqRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable);
    }
    
    public FAQEntity faqView(Integer id) {
        return faqRepository.findById(id).get();
    }

    public Page<FAQEntity> getFAQPage(Pageable pageable) {
        return faqRepository.findAll(pageable);
    }

    public Page<FAQEntity> findByCategory(String category, Pageable pageable) {
        return faqRepository.findByCategory(category, pageable);
    }

    //이전글
    public Object getPreArticle(Integer id) {
        return faqRepository.findTopByIdLessThanOrderByIdDesc(id);
    }
    //다음글
    public Object getNextArticle(Integer id) {
        return faqRepository.findTopByIdGreaterThanOrderByIdAsc(id);
    }

}
