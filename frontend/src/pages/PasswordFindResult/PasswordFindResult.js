import React from "react";
import { useHistory, useLocation } from "react-router-dom";

import Form from "../../components/form/Form/Form";
import Button from "../../components/form/Button/Button";

import styles from "./PasswordFindResult.module.css";

const PasswordFindResult = () => {
  const {
    state: { email },
  } = useLocation();

  const history = useHistory();

  return (
    <div className={styles["password-find-result"]}>
      <Form>
        <h1>비밀번호 찾기</h1>
        <div>
          임시 비밀번호가 <strong>{email}</strong> 으로 발송되었습니다.
        </div>
        <Button type="button" onClick={() => history.goBack()}>
          돌아가기
        </Button>
      </Form>
    </div>
  );
};

export default PasswordFindResult;
