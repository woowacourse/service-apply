import React from "react";
import Container from "../../components/@common/Container/Container";
import myPageImage from "../../assets/image/myPage.svg";
import * as styles from "./MyPage.module.css";
import Button, { BUTTON_VARIANT } from "../../components/@common/Button/Button";

const MyPage = () => {
  return (
    <Container title="sun@woowa.com 님">
      <div className={styles.box}>
        <div className={styles["illust-box"]}>
          <img src={myPageImage} alt="자기소개서 일러스트" />
        </div>
        <div className={styles["info-box"]}>
          <ul>
            <li className={styles.info}>
              <div className={styles["info-title"]}>이름</div>
              <div className={styles["info-data"]}>권선영</div>
            </li>
            <li className={styles.info}>
              <div className={styles["info-title"]}>전화번호</div>
              <div className={styles["info-data"]}>010-1234-5678</div>
            </li>
            <li className={styles.info}>
              <div className={styles["info-title"]}>생년월일</div>
              <div className={styles["info-data"]}>1995년 01월 01일</div>
            </li>
          </ul>

          <div className={styles.buttons}>
            <Button type="button" variant={BUTTON_VARIANT.OUTLINED}>
              비밀번호 변경
            </Button>
            <Button type="button">수정하기</Button>
          </div>
        </div>
      </div>
    </Container>
  );
};

export default MyPage;
