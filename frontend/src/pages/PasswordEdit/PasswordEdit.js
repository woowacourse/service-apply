import { useHistory } from "react-router-dom";
import { fetchPasswordEdit } from "../../api/applicants";
import { SUCCESS_MESSAGE } from "../../constants/messages";
import PATH from "../../constants/path";
import useForm from "../../hooks/useForm";
import useTokenContext from "../../hooks/useTokenContext";
import FormProvider from "../../provider/FormProvider/FormProvider";
import FormInput from "../../provider/FormProvider/FormInput";
import SubmitButton from "../../provider/FormProvider/SubmitButton";
import {
  validatePassword,
  validateRePassword,
} from "../../utils/validation/password";
import styles from "./PasswordEdit.module.css";
import Form from "../../components/form/Form/Form";
import Button from "../../components/@common/Button/Button";

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
    <div className={styles["password-edit"]}>
      <FormProvider {...methods}>
        <Form onSubmit={handleSubmit}>
          <h2>비밀번호 변경</h2>
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
            <Button cancel onClick={() => history.goBack()}>
              이전
            </Button>
            <SubmitButton>확인</SubmitButton>
          </div>
        </Form>
      </FormProvider>
    </div>
  );
};

export default PasswordEdit;
