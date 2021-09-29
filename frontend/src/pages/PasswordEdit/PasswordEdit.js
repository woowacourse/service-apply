import { useHistory } from "react-router-dom";

import { fetchPasswordEdit } from "../../api/applicants";

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
import {
  validatePassword,
  validateRePassword,
} from "../../utils/validation/password";
import PATH from "../../constants/path";
import { SUCCESS_MESSAGE } from "../../constants/messages";

import styles from "./PasswordEdit.module.css";

const PasswordEdit = () => {
  const { token, resetToken } = useTokenContext();

  const history = useHistory();

  const submit = async (value) => {
    try {
      await fetchPasswordEdit({
        token,
        password: value.password,
        newPassword: value.newPassword,
      });
      alert(SUCCESS_MESSAGE.API.CHANGE_PASSWORD);

      resetToken();
      history.push(PATH.LOGIN);
    } catch (e) {
      alert(e.response.data.message);
    }
  };

  const { handleSubmit, ...methods } = useForm({
    validators: {
      oldPassword: validatePassword,
      password: validatePassword,
      rePassword: validateRePassword,
    },
    submit,
  });

  return (
    <Container size={CONTAINER_SIZE.NARROW} title="비밀번호 변경">
      <FormProvider {...methods}>
        <Form onSubmit={handleSubmit}>
          <InputField
            name="oldPassword"
            type="password"
            label="기존 비밀번호"
            placeholder="기존 비밀번호를 입력해 주세요"
            required
          />
          <InputField
            name="password"
            type="password"
            label="새 비밀번호"
            placeholder="비밀번호를 입력해 주세요"
            required
          />
          <InputField
            name="rePassword"
            type="password"
            label="비밀번호 확인"
            placeholder="비밀번호를 다시 한 번 입력해 주세요"
            required
          />
          <div className={styles.buttons}>
            <Button cancel onClick={() => history.goBack()}>
              이전
            </Button>
            <SubmitButton>확인</SubmitButton>
          </div>
        </Form>
      </FormProvider>
    </Container>
  );
};

export default PasswordEdit;
