package com.ap4j.bma.controller.custmerCenter;

import com.ap4j.bma.model.entity.customerCenter.FAQEntity;
import com.ap4j.bma.service.customerCenter.FAQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@SessionAttributes("loginMember")
@Controller
public class FAQController {
    @Autowired
    private FAQService faqService;

    @GetMapping("/faq/list")
    public String searchFaq(Model model,
                            @RequestParam(name = "page", defaultValue = "1") int page,
                            @RequestParam(name = "searchText", required = false) String searchText,
                            @RequestParam(name = "category", required = false) String category) {
        int pageSize = 10; // 한 페이지당 보여줄 게시글 개수
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("id").descending());
        Page<FAQEntity> faqPage;

        if (searchText != null && !searchText.isEmpty()) {
            faqPage = faqService.searchFaqByKeyword(searchText, pageable);

        } else if(category != null){
            faqPage = faqService.findByCategory(category, pageable);
        }else {
            faqPage = faqService.getFAQPage(pageable);
        }

        model.addAttribute("category", category);
        model.addAttribute("searchText", searchText);
        model.addAttribute("faq", faqPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", faqPage.getTotalPages());

        return "customerCenter/FAQBoard/FAQList";
    }

    @GetMapping("/faq/view")
    public String faqView(Model model, Integer id) {

        model.addAttribute("article", faqService.faqView(id));
        model.addAttribute("prevArticle", faqService.getPreArticle(id));
        model.addAttribute("nextArticle", faqService.getNextArticle(id));
        return "customerCenter/FAQBoard/FAQView";
    }
}