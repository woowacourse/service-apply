import React from "react";
import { Link, useHistory } from "react-router-dom";

import Container, {
  CONTAINER_SIZE,
} from "../../components/@common/Container/Container";

import useForm from "../../hooks/useForm";
import useTokenContext from "../../hooks/useTokenContext";
import FormProvider from "../../provider/FormProvider";
import { validateEmail } from "../../utils/validation/email";
import { validatePassword } from "../../utils/validation/password";
import { SUCCESS_MESSAGE } from "../../constants/messages";
import PATH from "../../constants/path";

import styles from "./Login.module.css";
import Form from "../../components/form/Form/Form";
import Button from "../../components/@common/Button/Button";
import FormInput from "../../components/form/FormInput/FormInput";
import SubmitButton from "../../components/form/SubmitButton/SubmitButton";

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
        <Form
          onSubmit={handleSubmit}
          footer={
            <Link to="/find" className={styles["find-password"]}>
              비밀번호 찾기
            </Link>
          }
        >
          <FormInput
            name="email"
            type="email"
            label="이메일"
            placeholder="이메일 주소를 입력해 주세요."
            required
          />
          <FormInput
            name="password"
            type="password"
            label="비밀번호"
            placeholder="비밀번호를 입력해 주세요."
            required
          />
          <div className={styles.buttons}>
            <Button type="button" cancel onClick={() => history.goBack()}>
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
