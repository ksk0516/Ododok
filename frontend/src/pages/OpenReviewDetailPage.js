import React, { useState, useEffect } from "react";
import sidestyles from "../styles/Sidebar.module.scss";
import openreviewtyles from "../styles/OpenReviewDetail.module.scss";
import DodokBar from "../components/MyTeam/DodokBar";
import Rating from "@mui/material/Rating";
import { Api } from "../Api";

function OpenReviewDetailPage() {
  const [dodokRecord, setDodokRecord] = useState({
    bookTitle: "",
    bookImg: "",
    dodokStartdate: "",
    dodokEnddate: "",
    pageReviews: [],
    endReviews: [],
    dodokOpen: null,
  });

  useEffect(() => {
    const dodokRecordId = localStorage.getItem("dodokRecordId");
    Api.get(`/dodok/details/${dodokRecordId}`).then((res) => {
      // console.log("도독 상세조회", res.data);
      setDodokRecord({
        ...dodokRecord,
        bookTitle: res.data.dodok.book.bookTitle,
        bookImg: res.data.dodok.book.bookImg,
        dodokStartdate: res.data.dodok.dodokStartdate,
        dodokEnddate: res.data.dodok.dodokEnddate,
        pageReviews: res.data.reviewPageList,
        endReviews: res.data.reviewEndList,
        dodokOpen: res.data.dodok.dodokOpen,
      });
      console.log(dodokRecord);
    });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);
  
  const renderReview = dodokRecord.endReviews.map((review) => {
    return (
      <div key={review.reviewEndId} className={openreviewtyles["wrap-review"]}>
        <div className={openreviewtyles["wrap-user-info"]}>
          <div className={openreviewtyles["user-img-div"]}>
            <img src={review.user.userImage} alt="프로필이미지" />
          </div>
          <p>{review.user.userNickname}</p>
          <Rating
            name="read-only"
            value={review.rating}
            className={openreviewtyles.rating}
            readOnly
          />
        </div>
        <div className={openreviewtyles["review-content"]}>
          {review.reviewEndContent}
        </div>
      </div>
    );
  });

  return (
    <div className={sidestyles["myteam-container"]}>
      <div className={sidestyles.others}>
        <div className={openreviewtyles["wrap-content"]}>
          <div className={openreviewtyles["wrap-book"]}>
            <div>
              {dodokRecord.bookImg !== "tmp" ? (
                <img src={dodokRecord.bookImg} alt="책" />
              ) : (
                <img
                  src="https://cdn.pixabay.com/photo/2018/01/17/18/43/book-3088777__340.png"
                  alt="책"
                />
              )}
            </div>
            <div className={openreviewtyles["wrap-bookinfo"]}>
              <div className={openreviewtyles["book-info"]}>
                <p>도서명</p>
                <p>{dodokRecord.bookTitle}</p>
              </div>
              <div className={openreviewtyles["book-info"]}>
                <p>도독기간</p>
                <p>
                  {dodokRecord.dodokStartdate} ~ {dodokRecord.dodokEnddate}
                </p>
              </div>
            </div>
          </div>
          <DodokBar propPageReviews={dodokRecord.pageReviews} />
          <div className={openreviewtyles["wrap-reviews-title"]}>
            <h3>총평</h3>
          </div>
          {renderReview}
        </div>
      </div>
    </div>
  );
}

export default OpenReviewDetailPage;