import { useHistory } from "react-router-dom";
import { fetchPasswordEdit } from "../../api";
import Button from "../../components/form/Button/Button";
import TextField from "../../components/form/TextField/TextField";
import useTokenContext from "../../hooks/useTokenContext";
import FormProvider from "../../provider/FormProvider/FormProvider";
import InputField from "../../provider/FormProvider/InputField";
import SubmitButton from "../../provider/FormProvider/SubmitButton";
import {
  validatePassword,
  validateRePassword,
} from "../../utils/validation/password";
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
      alert("비밀번호가 변경되었습니다. 다시 로그인해주세요.");

      resetToken();
      history.push("/login");
    } catch (e) {
      alert(e.response.data.message);
    }
  };

  return (
    <div className={styles["password-edit"]}>
      <FormProvider
        submit={submit}
        validators={{
          password: validatePassword,
          newPassword: validatePassword,
          rePassword: validateRePassword,
        }}
      >
        <h2>비밀번호 변경</h2>
        <InputField
          name="password"
          type="password"
          label="기존 비밀번호"
          placeholder="기존 비밀번호를 입력해 주세요"
          required
        />
        <InputField
          name="newPassword"
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
      </FormProvider>
    </div>
  );
};

export default PasswordEdit;
