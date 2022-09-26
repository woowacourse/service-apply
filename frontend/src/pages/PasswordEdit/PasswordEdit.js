import { useNavigate } from "react-router-dom";
import { fetchPasswordEdit } from "../../api";
import Button from "../../components/@common/Button/Button";
import Container, { CONTAINER_SIZE } from "../../components/@common/Container/Container";
import MessageTextInput from "../../components/@common/MessageTextInput/MessageTextInput";
import CancelButton from "../../components/form/CancelButton/CancelButton";
import Form from "../../components/form/Form/Form";
import { ERROR_MESSAGE, SUCCESS_MESSAGE } from "../../constants/messages";
import { PATH } from "../../constants/path";
import usePasswordEditForm, { PASSWORD_EDIT_FORM_NAME } from "../../hooks/usePasswordEditForm";
import useTokenContext from "../../hooks/useTokenContext";
import styles from "./PasswordEdit.module.css";

const PasswordEdit = () => {
  const { token, resetToken } = useTokenContext();
  const navigate = useNavigate();

  const { form, errorMessage, handleChanges, handleCapsLockState, isValid, isEmpty } =
    usePasswordEditForm();

  const handleSubmitError = (error) => {
    if (!error) return;

    alert(ERROR_MESSAGE.API.EDIT_PASSWORD);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      await fetchPasswordEdit({ token, ...form });

      alert(SUCCESS_MESSAGE.API.CHANGE_PASSWORD);

      resetToken();
      navigate(PATH.LOGIN);
    } catch (error) {
      handleSubmitError(error);
    }
  };

  return (
    <Container size={CONTAINER_SIZE.NARROW} title="비밀번호 변경">
      <Form onSubmit={handleSubmit}>
        <MessageTextInput
          label="기존 비밀번호"
          placeholder="기존 비밀번호를 입력해 주세요"
          type="password"
          name={PASSWORD_EDIT_FORM_NAME.OLD_PASSWORD}
          value={form[PASSWORD_EDIT_FORM_NAME.OLD_PASSWORD]}
          onChange={handleChanges[PASSWORD_EDIT_FORM_NAME.OLD_PASSWORD]}
          onKeyUp={handleCapsLockState(PASSWORD_EDIT_FORM_NAME.OLD_PASSWORD)}
          errorMessage={errorMessage[PASSWORD_EDIT_FORM_NAME.OLD_PASSWORD]}
          required
        />
        <MessageTextInput
          label="새 비밀번호"
          placeholder="비밀번호를 입력해 주세요"
          type="password"
          name={PASSWORD_EDIT_FORM_NAME.PASSWORD}
          value={form[PASSWORD_EDIT_FORM_NAME.PASSWORD]}
          onChange={handleChanges[PASSWORD_EDIT_FORM_NAME.PASSWORD]}
          onKeyUp={handleCapsLockState(PASSWORD_EDIT_FORM_NAME.PASSWORD)}
          errorMessage={errorMessage[PASSWORD_EDIT_FORM_NAME.PASSWORD]}
          required
        />
        <MessageTextInput
          label="비밀번호 확인"
          placeholder="비밀번호를 다시 한 번 입력해 주세요"
          type="password"
          name={PASSWORD_EDIT_FORM_NAME.CONFIRM_PASSWORD}
          value={form[PASSWORD_EDIT_FORM_NAME.CONFIRM_PASSWORD]}
          onChange={handleChanges[PASSWORD_EDIT_FORM_NAME.CONFIRM_PASSWORD]}
          errorMessage={errorMessage[PASSWORD_EDIT_FORM_NAME.CONFIRM_PASSWORD]}
          required
        />
        <div className={styles.buttons}>
          <CancelButton />
          <Button disabled={!isValid || isEmpty}>확인</Button>
        </div>
      </Form>
    </Container>
  );
};

export default PasswordEdit;
