import React, { useState } from "react";
import { Link, useHistory } from "react-router-dom";

import Form from "../../components/form/Form/Form";
import TextField from "../../components/form/TextField/TextField";
import Button from "../../components/form/Button/Button";
import {
  isValid as isValidEmail,
  MESSAGE as EMAIL_MESSAGAE,
} from "../../utils/validation/email";
import {
  isValid as isValidPassword,
  MESSAGE as PASSWORD_MESSAGE,
} from "../../utils/validation/password";
import useTokenContext from "../../hooks/useTokenContext";
import styles from "./Login.module.css";

const validator = {
  email: isValidEmail,
  password: isValidPassword,
};

const message = {
  email: EMAIL_MESSAGAE,
  password: PASSWORD_MESSAGE,
};

const Login = () => {
  const [value, setValue] = useState({});
  const [errorMessage, setErrorMessage] = useState({});
  const { fetchLogin } = useTokenContext();

  const history = useHistory();

  const disabled =
    Object.values(value).filter(Boolean).length < 2 ||
    Object.values(errorMessage).filter(Boolean).length > 0;

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      await fetchLogin({
        email: value.email,
        password: value.password,
      });
      alert("로그인 성공");
      history.push({ pathname: "/recruits", state: { status: "applied" } });
    } catch (e) {
      alert(e.response.data.message);
    }
  };

  const handleChange = ({ target: { name, value } }) => {
    const isValid = validator[name](value);

    if (isValid) {
      setErrorMessage((errorMessage) => ({
        ...errorMessage,
        [name]: "",
      }));
    } else {
      setErrorMessage((errorMessage) => ({
        ...errorMessage,
        [name]: message[name],
      }));
    }

    setValue((prevValue) => ({ ...prevValue, [name]: value }));
  };

  return (
    <div className={styles.Login}>
      <Form onSubmit={handleSubmit}>
        <h1>내 지원서 보기</h1>
        <TextField
          v-model="email"
          name="email"
          type="email"
          label="이메일"
          value={value.email}
          placeholder="이메일 주소를 입력해 주세요."
          onChange={handleChange}
          required
        />
        <p className={styles["rule-field"]}>{errorMessage.email}</p>
        <TextField
          name="password"
          type="password"
          label="비밀번호"
          value={value.password}
          placeholder="비밀번호를 입력해 주세요."
          onChange={handleChange}
          required
        />
        <p className={styles["rule-field"]}>{errorMessage.password}</p>
        <Button onClick={() => history.goBack()}>이전</Button>
        <Button type="submit" disabled={disabled}>
          확인
        </Button>
      </Form>
      <Link to="/find" className={styles["find-password"]}>
        비밀번호 찾기
      </Link>
    </div>
  );
};

export default Login;
