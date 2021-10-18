import { useState } from "react";
import { useHistory } from "react-router-dom";
import Button from "../../components/@common/Button/Button";
import Container, {
  CONTAINER_SIZE,
} from "../../components/@common/Container/Container";
import MessageTextInput from "../../components/@common/MessageTextInput/MessageTextInput";
import BirthField from "../../components/form/BirthField/BirthField";
import EmailField, {
  EMAIL_STATUS,
} from "../../components/form/EmailField/EmailField";
import Form from "../../components/form/Form/Form";
import GenderField from "../../components/form/GenderField/GenderField";
import SummaryCheckField from "../../components/form/SummaryCheckField/SummaryCheckField";
import FORM from "../../constants/form";
import { ERROR_MESSAGE } from "../../constants/messages";
import PATH from "../../constants/path";
import { POLICY_SUMMARY } from "../../constants/policySummary";
import useSignUpForm, { SIGN_UP_FORM } from "../../hooks/useSignUpForm";
import useTokenContext from "../../hooks/useTokenContext";
import styles from "./SignUp.module.css";

const Join = () => {
  const history = useHistory();

  const { postRegister } = useTokenContext();

  const [emailStatus, setEmailStatus] = useState(EMAIL_STATUS.INPUT);
  const {
    form,
    errorMessage,
    handleChange,
    handleCapsLockState,
    reset,
    setErrorMessage,
    isValid,
    isEmpty,
  } = useSignUpForm();

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (emailStatus !== EMAIL_STATUS.AUTHENTICATED) {
      alert(ERROR_MESSAGE.API.NOT_AUTHENTICATED);

      return;
    }

    try {
      const {
        email,
        authenticationCode,
        name,
        password,
        confirmPassword,
        phoneNumber,
        birthday,
        gender,
      } = form;

      await postRegister({
        email,
        authenticationCode,
        name,
        password,
        confirmPassword,
        phoneNumber,
        birthday,
        gender,
      });

      history.push(PATH.RECRUITS);
    } catch (e) {
      alert(ERROR_MESSAGE.API.JOIN_FAILURE);
    }
  };

  return (
    <Container title="회원가입" size={CONTAINER_SIZE.NARROW}>
      <Form onSubmit={handleSubmit}>
        <SummaryCheckField
          label="개인정보 수집 및 이용 동의"
          name={SIGN_UP_FORM.IS_TERM_AGREED}
          value={form[SIGN_UP_FORM.IS_TERM_AGREED]}
          onChange={handleChange[SIGN_UP_FORM.IS_TERM_AGREED]}
          required
        >
          <p className={styles["summary-content"]}>{POLICY_SUMMARY}</p>
        </SummaryCheckField>

        <EmailField
          emailValue={form[SIGN_UP_FORM.EMAIL]}
          emailErrorMessage={errorMessage[SIGN_UP_FORM.EMAIL]}
          onChangeEmail={handleChange[SIGN_UP_FORM.EMAIL]}
          authenticationCodeValue={form[SIGN_UP_FORM.AUTHENTICATION_CODE]}
          authenticationCodeErrorMessage={
            errorMessage[SIGN_UP_FORM.AUTHENTICATION_CODE]
          }
          onChangeAuthenticationCode={
            handleChange[SIGN_UP_FORM.AUTHENTICATION_CODE]
          }
          resetAuthenticationCode={() =>
            reset(SIGN_UP_FORM.AUTHENTICATION_CODE)
          }
          emailStatus={emailStatus}
          setEmailStatus={setEmailStatus}
          setErrorMessage={setErrorMessage}
        />

        <MessageTextInput
          label="이름"
          placeholder="이름을 입력해 주세요."
          name={SIGN_UP_FORM.NAME}
          value={form[SIGN_UP_FORM.NAME]}
          onChange={handleChange[SIGN_UP_FORM.NAME]}
          maxLength={FORM.NAME_MAX_LENGTH}
          errorMessage={errorMessage[SIGN_UP_FORM.NAME]}
          required
        />
        <MessageTextInput
          label="핸드폰번호"
          placeholder="연락 가능한 핸드폰번호를 입력해 주세요."
          type="tel"
          name={SIGN_UP_FORM.PHONE_NUMBER}
          value={form[SIGN_UP_FORM.PHONE_NUMBER]}
          onChange={handleChange[SIGN_UP_FORM.PHONE_NUMBER]}
          maxLength={FORM.PHONE_NUMBER_MAX_LENGTH}
          errorMessage={errorMessage[SIGN_UP_FORM.PHONE_NUMBER]}
          className={styles["input-box"]}
          required
        />
        <MessageTextInput
          label="비밀번호"
          placeholder="비밀번호를 입력해 주세요."
          type="password"
          name={SIGN_UP_FORM.PASSWORD}
          value={form[SIGN_UP_FORM.PASSWORD]}
          onChange={handleChange[SIGN_UP_FORM.PASSWORD]}
          onKeyUp={handleCapsLockState(SIGN_UP_FORM.PASSWORD)}
          minLength={FORM.PASSWORD_MIN_LENGTH}
          maxLength={FORM.PASSWORD_MAX_LENGTH}
          errorMessage={errorMessage[SIGN_UP_FORM.PASSWORD]}
          required
        />
        <MessageTextInput
          label="비밀번호 확인"
          placeholder="비밀번호를 다시 한번 입력해 주세요."
          type="password"
          name={SIGN_UP_FORM.CONFIRM_PASSWORD}
          value={form[SIGN_UP_FORM.CONFIRM_PASSWORD]}
          onChange={handleChange[SIGN_UP_FORM.CONFIRM_PASSWORD]}
          errorMessage={errorMessage[SIGN_UP_FORM.CONFIRM_PASSWORD]}
          minLength={FORM.PASSWORD_MIN_LENGTH}
          maxLength={FORM.PASSWORD_MAX_LENGTH}
          required
        />
        <BirthField
          name={SIGN_UP_FORM.BIRTHDAY}
          value={form[SIGN_UP_FORM.BIRTHDAY]}
          onChange={handleChange[SIGN_UP_FORM.BIRTHDAY]}
          required
        />
        <GenderField
          className={styles["input-box"]}
          value={form[SIGN_UP_FORM.GENDER]}
          onChange={handleChange[SIGN_UP_FORM.GENDER]}
          required
        />

        <div className={styles.buttons}>
          <Button cancel type="button">
            취소
          </Button>
          <Button disabled={!isValid || isEmpty}>가입하기</Button>
        </div>
      </Form>
    </Container>
  );
};

export default Join;
