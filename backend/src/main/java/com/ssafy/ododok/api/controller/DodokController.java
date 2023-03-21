package com.ssafy.ododok.api.controller;

import com.ssafy.ododok.api.request.*;
import com.ssafy.ododok.api.response.DodokInfoRes;
import com.ssafy.ododok.api.service.BookService;
import com.ssafy.ododok.api.service.DodokService;
import com.ssafy.ododok.common.auth.PrincipalDetails;
import com.ssafy.ododok.db.model.*;
import com.ssafy.ododok.db.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dodok")
public class DodokController {
    @Autowired
    BookService bookService;

    @Autowired
    DodokService dodokService;

    @GetMapping({"/",""})
    public String helloDodok(){
        return "hello Dodok";
    }

    /**
     * 도독과 관련 API
     @author 김경삼
     */
    @PostMapping("/addbook")
    public ResponseEntity<String> addBook(@RequestBody BookAddPostReq bookAddPostReq){
        bookService.addBook(bookAddPostReq);
        return new ResponseEntity("책이 추가 됐습니다.",HttpStatus.OK);
    }

    //책 조회
    @GetMapping("/listbook")
    public ResponseEntity<List<Book>> listBooks(){
        List<Book> bookList = bookService.listBooks();
        return new ResponseEntity(bookList,HttpStatus.OK);
    }

    //책 검색
    @GetMapping("/search/{keyword}")
    public ResponseEntity<?> searchBooks(@PathVariable String keyword){
        System.out.println("keyword: "+ keyword);
        List<Book> searchResult = bookService.searchBooks(keyword);
        if(searchResult.size()==0){
            return new ResponseEntity<>("검색 결과가 없습니다.",HttpStatus.OK);
        }else{
            return new ResponseEntity<>(searchResult,HttpStatus.OK);
        }
    }
    //책 선택 ,
//    @PostMapping("/selectbook")
//    public ResponseEntity<String> selectBook(Long bookId){
//        return null;
//    }

    //도독 시작
    @PostMapping("/startdodok")
    public ResponseEntity<String> startDodok(@RequestBody DodokCreateReq dodokCreateReq, Authentication auth) {
        //나중에 auth에서 가져오는 것으로 대체
        PrincipalDetails principal = (PrincipalDetails) auth.getPrincipal();
        User user = principal.getUser();
        System.out.println("user70 = " + user);
        dodokService.startDodok(user,dodokCreateReq);
        return new ResponseEntity("새로운 도독을 생성했습니다.",HttpStatus.OK);
    }


    //도독 종료
    @PutMapping("/end/{dodokId}")
    public ResponseEntity<String> endDodok(@PathVariable Long dodokId){
        ResponseEntity res = dodokService.endDodok(dodokId)==0 ? new ResponseEntity<>("이미 종료 상태입니다.",HttpStatus.OK)
        : new ResponseEntity<>("진행중인 도독을 종료했습니다.",HttpStatus.OK);
        return res;
    }

    //도독 삭제
    @DeleteMapping("/{dodokId}")
    public ResponseEntity<?> deleteDodok(Authentication authentication,@PathVariable Long dodokId) {
        dodokService.deleteDodok(authentication,dodokId);
        return new ResponseEntity<>("책이 삭제됐습니다.",HttpStatus.OK);
    }

    @GetMapping("/lastdodoks")
    public ResponseEntity<List<DodokInfoRes>>showLastAllDodokInfo(Authentication auth){
        //security Authentication에서 User 가져오기.
        PrincipalDetails principal = (PrincipalDetails) auth.getPrincipal();
        User user = principal.getUser();
        List<Dodok> dodokList= dodokService.showLastAllDodoks(user);
        List<DodokInfoRes> dodokInfoResList = new ArrayList<>();
        for(Dodok dodok : dodokList){
            List<ReviewPage> reviewPageList = dodokService.getReviewPageList(dodok);
            List<ReviewEnd> reviewEndList = dodokService.getRivewEndList(dodok);
            DodokInfoRes dodokInfoRes =new DodokInfoRes(dodok,reviewPageList,reviewEndList);
            dodokInfoResList.add(dodokInfoRes);
        }
        return new ResponseEntity(dodokInfoResList,HttpStatus.OK);
    }

    /**
     책 리뷰 관련 API
     @author 김경삼
    */
    //책갈피 조회
    @GetMapping("/pagereview")
    public ResponseEntity<?> listCurPageReviews(Authentication auth){
//        User user = new User();
        PrincipalDetails principal = (PrincipalDetails) auth.getPrincipal();
        User user = principal.getUser();
        System.out.println("user = " + user);
        List<ReviewPage> reviewPageList=dodokService.getCurReviewPageList(user);

        if(reviewPageList==null){
            //진행중인 도독 없는게 아니라, 리뷰페이지만 없는거일수 있음.
            return new ResponseEntity("현재 진행중인 도독이 없습니다.",HttpStatus.OK);
        }else{
            return new ResponseEntity(reviewPageList,HttpStatus.OK);
        }
    }
    //책갈피 입력
    @PostMapping("/pagereview")
    public ResponseEntity<String> writePageReview(@RequestBody PageReviewCreatePostReq pageReviewCreatePostReq,Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        User user = principal.getUser();
        dodokService.writePageReview(pageReviewCreatePostReq,user);
        return new ResponseEntity<>("책갈피를 작성했습니다.",HttpStatus.OK);
    }

    //책갈피 수정
    @PutMapping("/pagereview")
    public ResponseEntity<String> modifyPageReviews(@RequestBody PageReviewPutReq pageReviewPutReq, Authentication authentication){
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        User user = principal.getUser();
        boolean result =dodokService.modifyPageReview(pageReviewPutReq,user);
        if(result){
            return new ResponseEntity<>("수정이 완료됐습니다.",HttpStatus.OK);
        }else{
            return new ResponseEntity<>("권한이 없어 수정에 실패했습니다.",HttpStatus.OK);
        }
    }

    //책갈피 삭제
    @DeleteMapping("/pagereview/{pageReviewId}")
    public ResponseEntity<String> deletePageReviews(@PathVariable Long pageReviewId
    ,Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        User user = principal.getUser();

        boolean result =dodokService.deletePageReview(pageReviewId,user);
        if(result){
            return new ResponseEntity<>("삭제가 완료됐습니다.",HttpStatus.OK);
        }else{
            return new ResponseEntity<>("권한이 없어 삭제에 실패했습니다.",HttpStatus.OK);
        }
    }

    //책 총평 조회
    @GetMapping("/endreview/{dodokId}")
    public ResponseEntity<?> listPageEndReviews(@PathVariable Long dodokId){
        List<ReviewEnd> reviewEndList= dodokService.getRivewEndList(dodokId);
        if(reviewEndList.size()==0){
            return new ResponseEntity<>("책 조회 결과가 없습니다.",HttpStatus.OK);
        }else{
            return new ResponseEntity<>(reviewEndList,HttpStatus.OK);
        }
    }

    //책 총평 입력
    @PostMapping("/endreview")
    public ResponseEntity<String> writePageEndReviews(@RequestBody EndReviewCreatePostReq endReviewCreatePostReq, Authentication authentication){
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        User user = principal.getUser();
        dodokService.writeEndReview(endReviewCreatePostReq,user);

        return new ResponseEntity<>("책 총평을 입력했습니다.",HttpStatus.OK);
    }
    //책 총평 수정
    @PutMapping("/endreview")
    public ResponseEntity<String> modifyPageEndReviews(@RequestBody EndReviewModifyPutReq endReviewModifyPutReq,Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        User user = principal.getUser();

        boolean result = dodokService.modifyEndReview(endReviewModifyPutReq,user);
        if(result){
            return new ResponseEntity<>("책 총평 수정을 완료했습니다.",HttpStatus.OK);
        }else{
            return new ResponseEntity<>("책 총평 수정 권한이 없습니다.",HttpStatus.OK);
        }
    }
    //책 총평 삭제
    @DeleteMapping("/endreview/{endReviewId}")
    public ResponseEntity<String> deletePageEndReviews(@PathVariable Long endReviewId,Authentication authentication){
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        User user = principal.getUser();

        boolean result = dodokService.deleteEndReview(endReviewId,user);
        if(result){
            return new ResponseEntity<>("책 총평을 삭제했습니다.",HttpStatus.OK);
        }
        return new ResponseEntity<>("총평 삭제 권한이 없습니다.",HttpStatus.OK);
    }
}