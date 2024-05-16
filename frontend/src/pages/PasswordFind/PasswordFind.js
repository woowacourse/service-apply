import { useNavigate } from "react-router-dom";
import { fetchPasswordFind } from "../../api/members";
import Button from "../../components/@common/Button/Button";
import Container, { CONTAINER_SIZE } from "../../components/@common/Container/Container";
import MessageTextInput from "../../components/@common/MessageTextInput/MessageTextInput";
import BirthField from "../../components/form/BirthField/BirthField";
import CancelButton from "../../components/form/CancelButton/CancelButton";
import Form from "../../components/form/Form/Form";
import { FORM } from "../../constants/form";
import { ERROR_MESSAGE } from "../../constants/messages";
import { PATH } from "../../constants/path";
import usePasswordFindForm, { PASSWORD_FIND_FORM_NAME } from "../../hooks/usePasswordFindForm";
import styles from "./PasswordFind.module.css";

const PasswordFind = () => {
  const navigate = useNavigate();

  const { form, errorMessage, handleChanges, isValid, isEmpty } = usePasswordFindForm();

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      await fetchPasswordFind(form);

      navigate(
        {
          pathname: PATH.FIND_PASSWORD_RESULT,
        },
        {
          state: { email: form[PASSWORD_FIND_FORM_NAME.EMAIL] },
        }
      );
    } catch (error) {
      alert(ERROR_MESSAGE.API.FIND_PASSWORD);
    }
  };

  return (
    <Container size={CONTAINER_SIZE.NARROW} title="비밀번호 찾기">
      <Form onSubmit={handleSubmit}>
        <MessageTextInput
          label="이름"
          placeholder="이름을 입력해 주세요."
          name={PASSWORD_FIND_FORM_NAME.NAME}
          value={form[PASSWORD_FIND_FORM_NAME.NAME]}
          onChange={handleChanges[PASSWORD_FIND_FORM_NAME.NAME]}
          errorMessage={errorMessage[PASSWORD_FIND_FORM_NAME.NAME]}
          maxLength={FORM.NAME_MAX_LENGTH}
          required
        />
        <MessageTextInput
          label="이메일"
          placeholder="이메일 주소를 입력해 주세요."
          name={PASSWORD_FIND_FORM_NAME.EMAIL}
          value={form[PASSWORD_FIND_FORM_NAME.EMAIL]}
          onChange={handleChanges[PASSWORD_FIND_FORM_NAME.EMAIL]}
          errorMessage={errorMessage[PASSWORD_FIND_FORM_NAME.EMAIL]}
          maxLength={FORM.EMAIL_MAX_LENGTH}
          type="email"
          required
        />

        <BirthField
          name={PASSWORD_FIND_FORM_NAME.BIRTHDAY}
          value={form[PASSWORD_FIND_FORM_NAME.BIRTHDAY]}
          onChange={handleChanges[PASSWORD_FIND_FORM_NAME.BIRTHDAY]}
          required
        />
        <div className={styles.buttons}>
          <CancelButton />
          <Button disabled={!isValid || isEmpty}>제출</Button>
        </div>
      </Form>
    </Container>
  );
};

export default PasswordFind;
