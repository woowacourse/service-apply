import React from "react";
import { Link, useHistory } from "react-router-dom";
import Button from "../../components/form/Button/Button";
import useTokenContext from "../../hooks/useTokenContext";
import FormProvider from "../../provider/FormProvider/FormProvider";
import InputField from "../../provider/FormProvider/InputField";
import SubmitButton from "../../provider/FormProvider/SubmitButton";
import { validateEmail } from "../../utils/validation/email";
import { validatePassword } from "../../utils/validation/password";
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

      alert("로그인 성공");
      history.push({ pathname: "/recruits", search: "?status=applied" });
    } catch (e) {
      alert(e.response.data.message);
    }

    await fetchLogin({
      email: value.email,
      password: value.password,
    });
  };

  return (
    <div className={styles.login}>
      <FormProvider
        submit={submit}
        validators={{
          email: validateEmail,
          password: validatePassword,
        }}
        footer={
          <Link to="/find" className={styles["find-password"]}>
            비밀번호 찾기
          </Link>
        }
      >
        <h2>내 지원서 보기</h2>
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
      </FormProvider>
    </div>
  );
};

export default Login;
