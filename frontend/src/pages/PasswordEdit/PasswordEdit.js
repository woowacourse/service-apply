import { useHistory } from "react-router-dom";
import { fetchPasswordEdit } from "../../api";
import Button from "../../components/@common/Button/Button";
import Container, {
  CONTAINER_SIZE,
} from "../../components/@common/Container/Container";
import Form from "../../components/form/Form/Form";
import FormInput from "../../components/form/FormInput/FormInput";
import SubmitButton from "../../components/form/SubmitButton/SubmitButton";
import { ERROR_MESSAGE, SUCCESS_MESSAGE } from "../../constants/messages";
import PATH from "../../constants/path";
import useForm from "../../hooks/useForm";
import useTokenContext from "../../hooks/useTokenContext";
import FormProvider from "../../provider/FormProvider";
import {
  validatePassword,
  validateRePassword,
} from "../../utils/validation/password";
import styles from "./PasswordEdit.module.css";

const PasswordEdit = () => {
  const { token, resetToken } = useTokenContext();
  const history = useHistory();

  const handleSubmitError = (e) => {
    if (e.response.status === 401) {
      alert(ERROR_MESSAGE.API.TOKEN_EXPIRED);
      history.push(PATH.LOGIN);
    } else {
      alert(ERROR_MESSAGE.API.EDIT_PASSWORD);
    }
  };

  const submit = async (value) => {
    try {
      await fetchPasswordEdit({
        token,
        password: value.oldPassword,
        newPassword: value.password,
      });

      alert(SUCCESS_MESSAGE.API.CHANGE_PASSWORD);

      resetToken();
      history.push(PATH.LOGIN);
    } catch (e) {
      handleSubmitError(e);
    }
  };

  const { handleSubmit, ...methods } = useForm({
    validators: {
      password: validatePassword,
      rePassword: validateRePassword,
    },
    submit,
  });

  return (
    <Container size={CONTAINER_SIZE.NARROW} title="비밀번호 변경">
      <FormProvider {...methods}>
        <Form onSubmit={handleSubmit}>
          <FormInput
            name="oldPassword"
            type="password"
            label="기존 비밀번호"
            placeholder="기존 비밀번호를 입력해 주세요"
            required
          />
          <FormInput
            name="password"
            type="password"
            label="새 비밀번호"
            placeholder="비밀번호를 입력해 주세요"
            required
          />
          <FormInput
            name="rePassword"
            type="password"
            label="비밀번호 확인"
            placeholder="비밀번호를 다시 한 번 입력해 주세요"
            required
          />
          <div className={styles.buttons}>
            <Button type="button" cancel onClick={() => history.goBack()}>
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
