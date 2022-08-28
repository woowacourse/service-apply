import React, { useEffect } from "react";
import { generatePath, Link, useNavigate, useLocation } from "react-router-dom";
import Button from "../../components/@common/Button/Button";
import Container, { CONTAINER_SIZE } from "../../components/@common/Container/Container";
import MessageTextInput from "../../components/@common/MessageTextInput/MessageTextInput";
import Form from "../../components/form/Form/Form";
import { ERROR_MESSAGE } from "../../constants/messages";
import { PATH, PARAM } from "../../constants/path";
import useAuth from "../../hooks/useAuth";
import useLoginForm, { LOGIN_FORM_NAME } from "../../hooks/useLoginForm";
import useTokenContext from "../../hooks/useTokenContext";
import { generateQuery } from "../../utils/route/query";
import styles from "./Login.module.css";

const Login = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const currentRecruitment = location.state?.currentRecruitment;

  const { token } = useTokenContext();
  const { login } = useAuth();
  const { form, errorMessage, handleChanges, handleCapsLockState, isEmpty } = useLoginForm();

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      await login(form);

      const path = currentRecruitment
        ? {
            pathname: generatePath(PATH.APPLICATION_FORM, {
              status: PARAM.APPLICATION_FORM_STATUS.NEW,
            }),
            search: generateQuery({ recruitmentId: currentRecruitment.id }),
            state: {
              currentRecruitment,
            },
          }
        : PATH.RECRUITS;

      navigate(path);
    } catch (error) {
      alert(ERROR_MESSAGE.API.LOGIN_FAILURE);
    }
  };

  useEffect(() => {
    if (token) navigate(PATH.HOME);
  }, [token]);

  return (
    <Container size={CONTAINER_SIZE.NARROW} title="로그인">
      <Form onSubmit={handleSubmit}>
        <MessageTextInput
          name={LOGIN_FORM_NAME.EMAIL}
          type="email"
          label="이메일"
          placeholder="이메일 주소를 입력해 주세요."
          value={form.email}
          onChange={handleChanges[LOGIN_FORM_NAME.EMAIL]}
          required
        />
        <MessageTextInput
          name={LOGIN_FORM_NAME.PASSWORD}
          type="password"
          label="비밀번호"
          placeholder="비밀번호를 입력해 주세요."
          value={form.password}
          onChange={handleChanges[LOGIN_FORM_NAME.PASSWORD]}
          onKeyUp={handleCapsLockState(LOGIN_FORM_NAME.PASSWORD)}
          errorMessage={errorMessage[LOGIN_FORM_NAME.PASSWORD]}
          required
        />
        <div className={styles.buttons}>
          <Button disabled={isEmpty}>로그인</Button>
        </div>
        <Link to={PATH.FIND_PASSWORD} className={styles["find-password"]}>
          비밀번호 찾기
        </Link>
      </Form>
    </Container>
  );
};

export default Login;
