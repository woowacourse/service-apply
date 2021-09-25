import React from "react";
import { useHistory, useLocation } from "react-router-dom";
import Button from "../../components/@common/Button/Button";
import Form from "../../components/form/Form/Form";
import PATH from "../../constants/path";
import styles from "./PasswordFindResult.module.css";

const PasswordFindResult = () => {
  const {
    state: { email },
  } = useLocation();

  const history = useHistory();

  return (
    <div className={styles["password-find-result"]}>
      <Form>
        <h2>비밀번호 찾기</h2>
        <div>
          임시 비밀번호가 <strong>{email}</strong> 으로 발송되었습니다.
        </div>
        <Button type="button" onClick={() => history.push(PATH.HOME)}>
          돌아가기
        </Button>
      </Form>
    </div>
  );
};

export default PasswordFindResult;
