import React from "react";
import { useHistory } from "react-router";
import myPageImage from "../../assets/image/myPage.svg";
import Button, { BUTTON_VARIANT } from "../../components/@common/Button/Button";
import Container from "../../components/@common/Container/Container";
import MessageTextInput from "../../components/@common/MessageTextInput/MessageTextInput";
import BirthField from "../../components/form/BirthField/BirthField";
import Form from "../../components/form/Form/Form";
import PATH from "../../constants/path";
import * as styles from "./MyPageEdit.module.css";

const MyPageEdit = () => {
  const history = useHistory();

  const handleCancel = () => {
    history.push({
      pathname: PATH.MY_PAGE,
    });
  };

  return (
    <Container title="sun@woowa.com 님">
      <div className={styles.box}>
        <div className={styles["illust-box"]}>
          <img src={myPageImage} alt="자기소개서 일러스트" />
        </div>
        <Form className={styles["input-box"]}>
          <MessageTextInput label="이름" />
          <MessageTextInput type="tel" label="전화번호" />
          <BirthField />

          <div className={styles.buttons}>
            <Button
              type="button"
              variant={BUTTON_VARIANT.OUTLINED}
              onClick={handleCancel}
            >
              취소
            </Button>
            <Button>확인</Button>
          </div>
        </Form>
      </div>
    </Container>
  );
};

export default MyPageEdit;
