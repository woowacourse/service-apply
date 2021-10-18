import React from "react";
import { generatePath, Link, useHistory, useLocation } from "react-router-dom";
import Button from "../../components/@common/Button/Button";
import Container, { CONTAINER_SIZE } from "../../components/@common/Container/Container";
import MessageTextInput from "../../components/@common/MessageTextInput/MessageTextInput";
import Form from "../../components/form/Form/Form";
import { ERROR_MESSAGE } from "../../constants/messages";
import PATH, { PARAM } from "../../constants/path";
import useAuth from "../../hooks/useAuth";
import useLoginForm, { LOGIN_FORM } from "../../hooks/useLoginForm";
import { generateQuery } from "../../utils/route/query";
import styles from "./Login.module.css";

const Login = () => {
  const history = useHistory();
  const location = useLocation();
  const currentRecruitment = location.state?.currentRecruitment;

  const { login } = useAuth();
  const { form, handleChange, isEmpty } = useLoginForm();

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

      history.push(path);
    } catch (e) {
      alert(ERROR_MESSAGE.API.LOGIN_FAILURE);
    }
  };

  return (
    <Container size={CONTAINER_SIZE.NARROW} title="로그인">
      <Form onSubmit={handleSubmit}>
        <MessageTextInput
          name={LOGIN_FORM.EMAIL}
          type="email"
          label="이메일"
          placeholder="이메일 주소를 입력해 주세요."
          value={form.email}
          onChange={handleChange[LOGIN_FORM.EMAIL]}
          required
        />
        <MessageTextInput
          name={LOGIN_FORM.PASSWORD}
          type="password"
          label="비밀번호"
          placeholder="비밀번호를 입력해 주세요."
          value={form.password}
          onChange={handleChange[LOGIN_FORM.PASSWORD]}
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
