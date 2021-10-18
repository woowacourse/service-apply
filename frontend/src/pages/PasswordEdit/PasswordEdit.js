import { useHistory } from "react-router-dom";
import { fetchPasswordEdit } from "../../api";
import Button from "../../components/@common/Button/Button";
import Container, { CONTAINER_SIZE } from "../../components/@common/Container/Container";
import MessageTextInput from "../../components/@common/MessageTextInput/MessageTextInput";
import Form from "../../components/form/Form/Form";
import { ERROR_MESSAGE, SUCCESS_MESSAGE } from "../../constants/messages";
import PATH from "../../constants/path";
import usePasswordEditForm, { PASSWORD_EDIT_FORM } from "../../hooks/usePasswordEditForm";
import useTokenContext from "../../hooks/useTokenContext";
import styles from "./PasswordEdit.module.css";

const PasswordEdit = () => {
  const { token, resetToken } = useTokenContext();
  const history = useHistory();

  const { form, errorMessage, handleChange, handleCapsLockState, isValid, isEmpty } =
    usePasswordEditForm();

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      await fetchPasswordEdit({
        token,
        ...form,
      });

      alert(SUCCESS_MESSAGE.API.CHANGE_PASSWORD);

      resetToken();
      history.push(PATH.LOGIN);
    } catch (e) {
      alert(ERROR_MESSAGE.API.EDIT_PASSWORD);
    }
  };

  return (
    <Container size={CONTAINER_SIZE.NARROW} title="비밀번호 변경">
      <Form onSubmit={handleSubmit}>
        <MessageTextInput
          label="기존 비밀번호"
          placeholder="기존 비밀번호를 입력해 주세요"
          type="password"
          name={PASSWORD_EDIT_FORM.OLD_PASSWORD}
          value={form[PASSWORD_EDIT_FORM.OLD_PASSWORD]}
          onChange={handleChange[PASSWORD_EDIT_FORM.OLD_PASSWORD]}
          onKeyUp={handleCapsLockState(PASSWORD_EDIT_FORM.OLD_PASSWORD)}
          errorMessage={errorMessage[PASSWORD_EDIT_FORM.OLD_PASSWORD]}
          required
        />
        <MessageTextInput
          label="새 비밀번호"
          placeholder="비밀번호를 입력해 주세요"
          type="password"
          name={PASSWORD_EDIT_FORM.PASSWORD}
          value={form[PASSWORD_EDIT_FORM.PASSWORD]}
          onChange={handleChange[PASSWORD_EDIT_FORM.PASSWORD]}
          onKeyUp={handleCapsLockState(PASSWORD_EDIT_FORM.PASSWORD)}
          errorMessage={errorMessage[PASSWORD_EDIT_FORM.PASSWORD]}
          required
        />
        <MessageTextInput
          label="비밀번호 확인"
          placeholder="비밀번호를 다시 한 번 입력해 주세요"
          type="password"
          name={PASSWORD_EDIT_FORM.CONFIRM_PASSWORD}
          value={form[PASSWORD_EDIT_FORM.CONFIRM_PASSWORD]}
          onChange={handleChange[PASSWORD_EDIT_FORM.CONFIRM_PASSWORD]}
          errorMessage={errorMessage[PASSWORD_EDIT_FORM.CONFIRM_PASSWORD]}
          required
        />
        <div className={styles.buttons}>
          <Button type="button" cancel onClick={history.goBack}>
            이전
          </Button>
          <Button disabled={!isValid || isEmpty}>확인</Button>
        </div>
      </Form>
    </Container>
  );
};

export default PasswordEdit;
