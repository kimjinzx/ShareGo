package com.java501.S20230401.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.java501.S20230401.model.Article;
import com.java501.S20230401.model.Comm;
import com.java501.S20230401.model.Region;
import com.java501.S20230401.service.ArticleService;
import com.java501.S20230401.service.Paging;

import lombok.RequiredArgsConstructor;

// 함께해요 페이지 계열 컨트롤러 : 임동빈
@Controller
@RequiredArgsConstructor
public class TogetherController {

	private final ArticleService as;

	@RequestMapping(value = "/board/together")
	public String articleList(Article article, int category, String currentPage, Model model) {
		System.out.println("articleList controller Start");
		article.setBrd_id(category);
		int number = article.getBrd_id();
		System.out.println("article.getBrd_id() = " + number);
		System.out.println("articleList controller getBrd_id->" + article.getBrd_id());

		// 전체 게시글 개수 Count
		int totalArticle = as.totalArticle(article);
		System.out.println("ArticleController totalArticle => " + totalArticle);

		// Paging 작업
		Paging page = new Paging(totalArticle, currentPage);
		article.setStart(page.getStart()); // 시작시 1
		article.setEnd  (page.getEnd());

		// 게시글 리스트 작업
		List<Article> listArticle = as.listArticle(article); 

		model.addAttribute("article", article);
		model.addAttribute("totalArticle", totalArticle);
		model.addAttribute("listArticle", listArticle);
		model.addAttribute("category", number);
		model.addAttribute("page", page);

		return "together/listArticle";
	}

	@RequestMapping(value = "/board/detailArticle")
	public String detailArticle(Article article, Model model) {
		System.out.println("ArticleController Start detailArticle...");

		// 상세게시글 요소 구현
		Article detailArticle = as.detailArticle(article);
		System.out.println("article =>" + article);
		System.out.println("detailArticle => " + detailArticle);
		model.addAttribute("detailArticle", detailArticle);

		// 게시글 별 댓글 리스트
		List<Article> replyList = as.replyList(article);
		model.addAttribute("replyList", replyList);

		return "together/detailArticle";
	}

	@RequestMapping(value = "/board/writeFormArticle")
	public String writeFormArticle(Model model) {
		System.out.println("ArticleController Start writeFormArticle...");

		// 카테고리별 콤보박스
		List<Comm> categoryList = as.categoryName();
		System.out.println("ArticleController category => " + categoryList.size());
		model.addAttribute("categories", categoryList);

		// 지역별 콤보박스
		List<Region> regionList = as.regionName();
		System.out.println("ArticleController category => " + regionList.size());
		model.addAttribute("regions", regionList);

		return "together/writeFormArticle";
	}

	@RequestMapping(value = "/board/writeArticle")
	public String writeArticle(Article article, Model model) {
		System.out.println("ArticleController Start writeEmp...");

		// 프로시저 Insert_Article 이용 => 게시글 작성
		as.writeArticle(article);
		int insertResult = article.getInsert_result();
		System.out.println("article.getInsert_result() =>" + insertResult);

		model.addAttribute("insertResult", insertResult);
		model.addAttribute("article", article);
		if (insertResult > 0) {
			return "redirect:/board/together?category=1000";
		} else {
			model.addAttribute("msg", "입력실패");
			return "forward:/board/writeFormArticle";
		}
	}

	@RequestMapping(value = "/board/deleteArticle")
	public String deleteArticle(Article article, Model model) {
		System.out.println("ArticleController Start delete...");

		// 게시글 삭제 (isdelete = 0 => 1)
		int result = as.deleteArticle(article);

		model.addAttribute("result", result);
		return "redirect:/board/together?category=1000";
	}

	@RequestMapping(value = "/board/updateFormArticle")
	public String updateFormArticle(Article article, Model model) {
		System.out.println("ArticleController Start writeFormArticle...");

		// 게시글 수정 양식 (상세 게시글 값 가져오기)
		Article updateFormArticle = as.detailArticle(article);
		System.out.println("article =>" + article);
		System.out.println("updateArticle => " + updateFormArticle);
		model.addAttribute("article", updateFormArticle);
		
		// 카테고리별 콤보박스
		List<Comm> categoryList = as.categoryName();
		System.out.println("ArticleController category => " + categoryList.size());
		model.addAttribute("categories", categoryList);

		// 지역별 콤보박스
		List<Region> regionList = as.regionName();
		System.out.println("ArticleController category => " + regionList.size());
		model.addAttribute("regions", regionList);

		return "together/updateFormArticle";
	}
	
	@RequestMapping(value = "/board/updateArticle")
	public String updateArticle(Article article, Model model) {
		System.out.println("ArticleController Start updateArticle...");

		// 게시글 수정 (프로시저 사용 => Update_Article)
		int updateArticle = as.updateArticle(article);
		model.addAttribute("article", article);

		if (updateArticle > 0) {
			return "redirect:/board/together?category=1000";
		} else {
			model.addAttribute("msg", "입력실패");
			return "forward:/board/updateFormArticle";
		}
	}
}