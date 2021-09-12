import { useHistory } from "react-router-dom";
import { fetchPasswordEdit } from "../../api";
import Button from "../../components/form/Button/Button";
import useTokenContext from "../../hooks/useTokenContext";
import FormProvider from "../../provider/FormProvider/FormProvider";
import InputField from "../../provider/FormProvider/InputField";
import SubmitButton from "../../provider/FormProvider/SubmitButton";
import {
  validatePassword,
  validateRePassword,
} from "../../utils/validation/password";
import useForm from "../../hooks/useForm";
import styles from "./PasswordEdit.module.css";
import { Form } from "../../components/form";

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
      alert("비밀번호가 변경되었습니다. 다시 로그인해주세요.");

      resetToken();
      history.push("/login");
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
    </div>
  );
};

export default PasswordEdit;
