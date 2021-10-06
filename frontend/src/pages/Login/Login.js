import React from "react";
import { generatePath, Link, useHistory, useLocation } from "react-router-dom";
import Button from "../../components/@common/Button/Button";
import Container, {
  CONTAINER_SIZE,
} from "../../components/@common/Container/Container";
import Form from "../../components/form/Form/Form";
import FormInput from "../../components/form/FormInput/FormInput";
import SubmitButton from "../../components/form/SubmitButton";
import { ERROR_MESSAGE, SUCCESS_MESSAGE } from "../../constants/messages";
import PATH, { PARAM } from "../../constants/path";
import useForm from "../../hooks/useForm";
import useTokenContext from "../../hooks/useTokenContext";
import FormProvider from "../../provider/FormProvider";
import { generateQuery } from "../../utils/route/query";
import styles from "./Login.module.css";

const Login = () => {
  const history = useHistory();
  const location = useLocation();
  const currentRecruitment = location.state?.currentRecruitment;

  const { fetchLogin } = useTokenContext();

  const submit = async (value) => {
    try {
      await fetchLogin({
        email: value.email,
        password: value.password,
      });

      alert(SUCCESS_MESSAGE.API.LOGIN);

      if (currentRecruitment) {
        history.push({
          pathname: generatePath(PATH.APPLICATION_FORM, {
            status: PARAM.APPLICATION_FORM_STATUS.NEW,
          }),
          search: generateQuery({ recruitmentId: currentRecruitment.id }),
          state: {
            currentRecruitment,
          },
        });

        return;
      }

      history.push(PATH.RECRUITS);
    } catch (e) {
      alert(ERROR_MESSAGE.API.LOGIN_FAILURE);
    }
  };

  const { handleSubmit, ...methods } = useForm({
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
