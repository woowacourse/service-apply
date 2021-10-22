import classNames from "classnames";
import PropTypes from "prop-types";
import { useEffect } from "react";
import { fetchAuthenticationCode, fetchVerifyAuthenticationCode } from "../../../api";
import FORM from "../../../constants/form";
import { ERROR_MESSAGE } from "../../../constants/messages";
import { SIGN_UP_FORM_NAME } from "../../../hooks/useSignUpForm";
import useTimer from "../../../hooks/useTimer";
import { formatTimerText } from "../../../utils/format/date";
import Button, { BUTTON_VARIANT } from "../../@common/Button/Button";
import Label from "../../@common/Label/Label";
import TextInput from "../../@common/TextInput/TextInput";
import * as styles from "./EmailField.module.css";

export const EMAIL_STATUS = {
  INPUT: "input",
  WAITING_AUTHENTICATION: "waiting for authentication",
  AUTHENTICATED: "authenticated",
};

const AUTHENTICATED_CODE_VALIDITY_SECONDS = 600;

const EmailField = ({
  emailValue,
  emailErrorMessage,
  onChangeEmail,
  authenticationCodeValue,
  authenticationCodeErrorMessage,
  onChangeAuthenticationCode,
  resetAuthenticationCode,
  emailStatus,
  setEmailStatus,
  setErrorMessage,
}) => {
  const { timerSeconds, startTimer, resetTimer } = useTimer(AUTHENTICATED_CODE_VALIDITY_SECONDS);

  const getEmailButton = () => {
    if (emailStatus === EMAIL_STATUS.WAITING_AUTHENTICATION) {
      return null;
    }

    if (emailStatus === EMAIL_STATUS.INPUT) {
      return (
        <Button
          type="button"
          variant={BUTTON_VARIANT.OUTLINED}
          onClick={handleIssueEmailCode}
          className={styles["input-button"]}
          disabled={emailValue === "" || emailErrorMessage !== ""}
        >
          이메일
          <br />
          인증
        </Button>
      );
    }

    if (emailStatus === EMAIL_STATUS.AUTHENTICATED) {
      return (
        <div className={classNames(styles["authenticated"], styles["input-button"])} disabled>
          ✓
        </div>
      );
    }
  };

  const handleChangeEmail = (event) => {
    setEmailStatus(EMAIL_STATUS.INPUT);
    resetAuthenticationCode();
    onChangeEmail(event);
  };

  const handleIssueEmailCode = async () => {
    try {
      await fetchAuthenticationCode(emailValue);

      setEmailStatus(EMAIL_STATUS.WAITING_AUTHENTICATION);
      startTimer();
    } catch (error) {
      alert(ERROR_MESSAGE.API.ALREADY_EXIST_EMAIL);
    }
  };

  const handleAuthenticateEmail = async () => {
    try {
      await fetchVerifyAuthenticationCode({
        email: emailValue,
        authenticationCode: authenticationCodeValue,
      });

      setEmailStatus(EMAIL_STATUS.AUTHENTICATED);
      setErrorMessage(SIGN_UP_FORM_NAME.AUTHENTICATION_CODE, "");
      resetTimer();
    } catch (error) {
      resetAuthenticationCode();
      setErrorMessage(
        SIGN_UP_FORM_NAME.AUTHENTICATION_CODE,
        ERROR_MESSAGE.API.INVALID_AUTHENTICATION_CODE
      );
    }
  };

  useEffect(() => {
    if (timerSeconds > 0) return;

    alert(ERROR_MESSAGE.VALIDATION.TIMEOUT_EMAIL_AUTHENTICATION_CODE);

    resetTimer();

    setEmailStatus(EMAIL_STATUS.INPUT);
    resetAuthenticationCode();
  }, [timerSeconds]);

  return (
    <>
      <div className={styles.box}>
        <div className={styles["text-field"]}>
          <Label className={styles.label} required>
            이메일
          </Label>
          <div className={styles["input-box"]}>
            <TextInput
              type="email"
              placeholder="이메일 주소를 입력해 주세요."
              name={SIGN_UP_FORM_NAME.EMAIL}
              value={emailValue}
              onChange={handleChangeEmail}
              maxLength={FORM.EMAIL_MAX_LENGTH}
              required
            />
            {getEmailButton()}
          </div>
        </div>
        {emailErrorMessage && <p className={styles["rule-field"]}>{emailErrorMessage}</p>}
      </div>

      {emailStatus === EMAIL_STATUS.WAITING_AUTHENTICATION && (
        <div className={styles.box}>
          <div className={styles.timer}>
            인증 코드 유효시간
            <span className={styles["timer-time"]}>{formatTimerText(timerSeconds)}</span>
          </div>
          <div className={styles.box}>
            <div className={styles["text-field"]}>
              <Label className={styles.label} required>
                이메일 인증 코드
              </Label>
              <div className={styles["input-box"]}>
                <TextInput
                  name={SIGN_UP_FORM_NAME.AUTHENTICATION_CODE}
                  value={authenticationCodeValue}
                  onChange={onChangeAuthenticationCode}
                  required
                />
                <Button
                  type="button"
                  onClick={handleAuthenticateEmail}
                  className={styles["input-button"]}
                >
                  인증
                  <br />
                  하기
                </Button>
              </div>
            </div>
          </div>
          {authenticationCodeErrorMessage && (
            <p className={styles["rule-field"]}>{authenticationCodeErrorMessage}</p>
          )}
        </div>
      )}
    </>
  );
};

EmailField.propTypes = {
  emailValue: PropTypes.string.isRequired,
  emailErrorMessage: PropTypes.string,
  onChangeEmail: PropTypes.func.isRequired,
  authenticationCodeValue: PropTypes.string.isRequired,
  authenticationCodeErrorMessage: PropTypes.string,
  onChangeAuthenticationCode: PropTypes.func.isRequired,
  resetAuthenticationCode: PropTypes.func.isRequired,
  emailStatus: PropTypes.string.isRequired,
  setEmailStatus: PropTypes.func.isRequired,
};

export default EmailField;
