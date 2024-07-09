import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import Button from "../../components/@common/Button/Button";
import Container, {
  CONTAINER_SIZE,
  TITLE_ALIGN,
} from "../../components/@common/Container/Container";
import MessageTextInput from "../../components/@common/MessageTextInput/MessageTextInput";
import BirthField from "../../components/form/BirthField/BirthField";
import CancelButton from "../../components/form/CancelButton/CancelButton";
import EmailField, { EMAIL_STATUS } from "../../components/form/EmailField/EmailField";
import Form from "../../components/form/Form/Form";
import SummaryCheckField from "../../components/form/SummaryCheckField/SummaryCheckField";
import { FORM } from "../../constants/form";
import { ERROR_MESSAGE } from "../../constants/messages";
import { PATH } from "../../constants/path";
import useSignUpForm, { SIGN_UP_FORM_NAME } from "../../hooks/useSignUpForm";
import useTokenContext from "../../hooks/useTokenContext";
import styles from "./SignUp.module.css";

const SignUp = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { postRegister } = useTokenContext();
  const [agreementContent, setAgreementContent] = useState("");

  const [emailStatus, setEmailStatus] = useState(EMAIL_STATUS.INPUT);
  const {
    form,
    errorMessage,
    handleChanges,
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
      await postRegister(form);
      navigate(PATH.RECRUITS);
    } catch (error) {
      alert(ERROR_MESSAGE.API.JOIN_FAILURE);
    }
  };

  useEffect(() => {
    if (!location?.state?.agreement) {
      navigate(PATH.RECRUITS);
      return;
    }

    setAgreementContent(location.state.agreement?.data?.content);
  }, [location.state]);

  return (
    <Container title="회원가입" titleAlign={TITLE_ALIGN.LEFT} size={CONTAINER_SIZE.NARROW}>
      <Form onSubmit={handleSubmit}>
        <SummaryCheckField
          label="개인정보 수집 및 이용 동의"
          name={SIGN_UP_FORM_NAME.IS_TERM_AGREED}
          checked={form[SIGN_UP_FORM_NAME.IS_TERM_AGREED]}
          onChange={handleChanges[SIGN_UP_FORM_NAME.IS_TERM_AGREED]}
          required
        >
          <p
            className={styles["summary-content"]}
            dangerouslySetInnerHTML={{ __html: agreementContent }}
          />
        </SummaryCheckField>
        <EmailField
          emailValue={form[SIGN_UP_FORM_NAME.EMAIL]}
          emailErrorMessage={errorMessage[SIGN_UP_FORM_NAME.EMAIL]}
          onChangeEmail={handleChanges[SIGN_UP_FORM_NAME.EMAIL]}
          authenticationCodeValue={form[SIGN_UP_FORM_NAME.AUTHENTICATION_CODE]}
          authenticationCodeErrorMessage={errorMessage[SIGN_UP_FORM_NAME.AUTHENTICATION_CODE]}
          onChangeAuthenticationCode={handleChanges[SIGN_UP_FORM_NAME.AUTHENTICATION_CODE]}
          resetAuthenticationCode={() => reset(SIGN_UP_FORM_NAME.AUTHENTICATION_CODE)}
          emailStatus={emailStatus}
          setEmailStatus={setEmailStatus}
          setErrorMessage={setErrorMessage}
        />
        <MessageTextInput
          label="비밀번호"
          placeholder="비밀번호를 입력해 주세요."
          type="password"
          name={SIGN_UP_FORM_NAME.PASSWORD}
          value={form[SIGN_UP_FORM_NAME.PASSWORD]}
          onChange={handleChanges[SIGN_UP_FORM_NAME.PASSWORD]}
          onKeyUp={handleCapsLockState(SIGN_UP_FORM_NAME.PASSWORD)}
          minLength={FORM.PASSWORD_MIN_LENGTH}
          maxLength={FORM.PASSWORD_MAX_LENGTH}
          errorMessage={errorMessage[SIGN_UP_FORM_NAME.PASSWORD]}
          required
        />
        <MessageTextInput
          label="비밀번호 확인"
          placeholder="비밀번호를 다시 한번 입력해 주세요."
          type="password"
          name={SIGN_UP_FORM_NAME.CONFIRM_PASSWORD}
          value={form[SIGN_UP_FORM_NAME.CONFIRM_PASSWORD]}
          onChange={handleChanges[SIGN_UP_FORM_NAME.CONFIRM_PASSWORD]}
          errorMessage={errorMessage[SIGN_UP_FORM_NAME.CONFIRM_PASSWORD]}
          onKeyUp={handleCapsLockState(SIGN_UP_FORM_NAME.CONFIRM_PASSWORD)}
          minLength={FORM.PASSWORD_MIN_LENGTH}
          maxLength={FORM.PASSWORD_MAX_LENGTH}
          required
        />
        <MessageTextInput
          label="이름"
          placeholder="이름을 입력해 주세요."
          name={SIGN_UP_FORM_NAME.NAME}
          value={form[SIGN_UP_FORM_NAME.NAME]}
          onChange={handleChanges[SIGN_UP_FORM_NAME.NAME]}
          maxLength={FORM.NAME_MAX_LENGTH}
          errorMessage={errorMessage[SIGN_UP_FORM_NAME.NAME]}
          required
        />
        <BirthField
          name={SIGN_UP_FORM_NAME.BIRTHDAY}
          value={form[SIGN_UP_FORM_NAME.BIRTHDAY]}
          onChange={handleChanges[SIGN_UP_FORM_NAME.BIRTHDAY]}
          errorMessage={errorMessage[SIGN_UP_FORM_NAME.BIRTHDAY]}
          required
        />
        <MessageTextInput
          label="휴대전화 번호"
          placeholder="연락 가능한 휴대전화 번호를 입력해 주세요."
          type="tel"
          name={SIGN_UP_FORM_NAME.PHONE_NUMBER}
          value={form[SIGN_UP_FORM_NAME.PHONE_NUMBER]}
          onChange={handleChanges[SIGN_UP_FORM_NAME.PHONE_NUMBER]}
          maxLength={FORM.PHONE_NUMBER_MAX_LENGTH}
          errorMessage={errorMessage[SIGN_UP_FORM_NAME.PHONE_NUMBER]}
          className={styles["input-box"]}
          required
        />
        <MessageTextInput
          label="GitHub 사용자 이름"
          placeholder="GitHub 사용자 이름을 입력해 주세요."
          name={SIGN_UP_FORM_NAME.GITHUB_USERNAME}
          value={form[SIGN_UP_FORM_NAME.GITHUB_USERNAME]}
          onChange={handleChanges[SIGN_UP_FORM_NAME.GITHUB_USERNAME]}
          maxLength={FORM.GITHUB_USERNAME_MAX_LENGTH}
          errorMessage={errorMessage[SIGN_UP_FORM_NAME.GITHUB_USERNAME]}
          required
        />
        <div className={styles.buttons}>
          <CancelButton />
          <Button disabled={!isValid || isEmpty}>가입하기</Button>
        </div>
      </Form>
    </Container>
  );
};

export default SignUp;
