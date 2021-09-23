import React from "react";
import { useHistory, useLocation } from "react-router-dom";

import Form from "../../components/form/Form/Form";
import Button from "../../components/form/Button/Button";
import PATH from "../../constants/path";
import Box from "../../components/Box/Box";

import styles from "./PasswordFindResult.module.css";

const PasswordFindResult = () => {
  const {
    state: { email },
  } = useLocation();

  const history = useHistory();

  return (
    <Box size="narrow" title="비밀번호 찾기">
      <Form>
        <p className={styles.text}>
          임시 비밀번호가 <strong>{email}</strong> 으로 발송되었습니다.
        </p>
        <Button type="button" onClick={() => history.push(PATH.HOME)}>
          돌아가기
        </Button>
      </Form>
    </Box>
  );
};

export default PasswordFindResult;
