import React from "react";
import { useNavigate } from "react-router";
import myPageImage from "../../assets/image/myPage.svg";
import Button, { BUTTON_VARIANT } from "../../components/@common/Button/Button";
import Container from "../../components/@common/Container/Container";
import { PATH } from "../../constants/path";
import useMemberInfoContext from "../../hooks/useMemberInfoContext";
import styles from "./MyPage.module.css";

const MyPage = () => {
  const navigate = useNavigate();

  const { memberInfo } = useMemberInfoContext();

  const routeToPasswordEdit = () => {
    navigate(PATH.EDIT_PASSWORD);
  };

  const routeToMyPageEdit = () => {
    navigate(PATH.EDIT_MY_PAGE);
  };

  const routeToWithdrawal = () => {
    navigate(PATH.WITHDRAWAL_PAGE);
  };

  return (
    <Container title={`${memberInfo?.name ?? ""} 님`}>
      <div className={styles.box}>
        <div className={styles["illust-box"]}>
          <img src={myPageImage} alt="자기소개서 일러스트" />
        </div>
        <div className={styles["info-box"]}>
          <ul>
            <li className={styles.info}>
              <div className={styles["info-title"]}>이메일</div>
              <div className={styles["info-data"]}>{memberInfo?.email || ""}</div>
            </li>
            <li className={styles.info}>
              <div className={styles["info-title"]}>생년월일</div>
              <div className={styles["info-data"]}>{memberInfo?.birthday || ""}</div>
            </li>
            <li className={styles.info}>
              <div className={styles["info-title"]}>
                휴대전화 <br />
                번호
              </div>
              <div className={styles["info-data"]}>{memberInfo?.phoneNumber || ""}</div>
            </li>
            <li className={styles.info}>
              <div className={styles["info-title"]}>
                GitHub <br />
                사용자 이름
              </div>
              <div className={styles["info-data"]}>{memberInfo?.githubUsername || ""}</div>
            </li>
          </ul>

          <div className={styles.buttons}>
            <Button type="button" variant={BUTTON_VARIANT.OUTLINED} onClick={routeToPasswordEdit}>
              비밀번호 변경
            </Button>
            <Button type="button" onClick={routeToMyPageEdit}>
              내 정보 수정
            </Button>
            <Button
              type="button"
              variant={BUTTON_VARIANT.DANGER_CONTAINED}
              onClick={routeToWithdrawal}
            >
              회원 탈퇴
            </Button>
          </div>
        </div>
      </div>
    </Container>
  );
};

export default MyPage;
