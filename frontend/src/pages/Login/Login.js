import React from "react";
import { Link, useHistory } from "react-router-dom";

import Container, {
  CONTAINER_SIZE,
} from "../../components/Container/Container";
import { Form } from "../../components/form";
import Button from "../../components/form/Button/Button";

import useForm from "../../hooks/useForm";
import useTokenContext from "../../hooks/useTokenContext";
import FormProvider from "../../provider/FormProvider/FormProvider";
import InputField from "../../provider/FormProvider/InputField";
import SubmitButton from "../../provider/FormProvider/SubmitButton";
import { validateEmail } from "../../utils/validation/email";
import { validatePassword } from "../../utils/validation/password";
import { SUCCESS_MESSAGE } from "../../constants/messages";
import PATH from "../../constants/path";

import styles from "./Login.module.css";

const Login = () => {
  const { fetchLogin } = useTokenContext();

  const history = useHistory();

  const submit = async (value) => {
    try {
      await fetchLogin({
        email: value.email,
        password: value.password,
      });

      alert(SUCCESS_MESSAGE.API.LOGIN);

      history.push(PATH.RECRUITS);
    } catch (e) {
      alert(e.response.data.message);
    }
  };

  const { handleSubmit, ...methods } = useForm({
    validators: {
      email: validateEmail,
      password: validatePassword,
    },
    submit,
  });

  return (
    <Container size={CONTAINER_SIZE.NARROW} title="로그인">
      <FormProvider {...methods}>
        <Form onSubmit={handleSubmit}>
          <InputField
            name="email"
            type="email"
            label="이메일"
            placeholder="이메일 주소를 입력해 주세요."
            required
          />
          <InputField
            name="password"
            type="password"
            label="비밀번호"
            placeholder="비밀번호를 입력해 주세요."
            required
          />
          <div className={styles.buttons}>
            <Button cancel onClick={() => history.goBack()}>
              이전
            </Button>
            <SubmitButton>확인</SubmitButton>
          </div>
          <Link to={PATH.FIND_PASSWORD} className={styles["find-password"]}>
            비밀번호 찾기
          </Link>
        </Form>
      </FormProvider>
    </Container>
  );
};

export default Login;
