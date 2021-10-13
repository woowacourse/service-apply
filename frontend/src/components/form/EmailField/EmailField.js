import classNames from "classnames";
import PropTypes from "prop-types";
import { useEffect } from "react";
import {
  fetchAuthenticationCode,
  fetchVerifyAuthenticationCode,
} from "../../../api";
import { ERROR_MESSAGE } from "../../../constants/messages";
import useFormContext from "../../../hooks/useFormContext";
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

const INPUT_NAME = {
  EMAIL: "email",
  AUTHENTICATED_CODE: "authenticationCode",
};

const AUTHENTICATED_CODE_VALIDITY_SECONDS = 600;

const EmailField = ({ emailStatus, setEmailStatus }) => {
  const { value, errorMessage, handleChange, reset, register, unRegister } =
    useFormContext();
  const { timerSeconds, setTimerSeconds, startTimer, resetTimer } = useTimer(
    AUTHENTICATED_CODE_VALIDITY_SECONDS
  );

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
          disabled={value.email === "" || errorMessage.email !== null}
        >
          이메일
          <br />
          인증
        </Button>
      );
    }

    if (emailStatus === EMAIL_STATUS.AUTHENTICATED) {
      return (
        <div
          className={classNames(
            styles["authenticated"],
            styles["input-button"]
          )}
          disabled
        >
          ✓
        </div>
      );
    }
  };

  const handleChangeEmail = (event) => {
    setEmailStatus(EMAIL_STATUS.INPUT);
    reset(INPUT_NAME.AUTHENTICATED_CODE);
    handleChange(event);
  };

  const handleIssueEmailCode = async () => {
    try {
      await fetchAuthenticationCode(value.email);

      setEmailStatus(EMAIL_STATUS.WAITING_AUTHENTICATION);
      setTimerSeconds(AUTHENTICATED_CODE_VALIDITY_SECONDS);
      startTimer();
    } catch (error) {
      alert(ERROR_MESSAGE.API.ALREADY_EXIST_EMAIL);
    }
  };

  const handleAuthenticateEmail = async () => {
    try {
      await fetchVerifyAuthenticationCode({
        email: value.email,
        authenticationCode: value.authenticationCode,
      });

      setEmailStatus(EMAIL_STATUS.AUTHENTICATED);
      resetTimer();
    } catch (error) {
      alert(ERROR_MESSAGE.API.INVALID_AUTHENTICATION_CODE);
      reset(INPUT_NAME.AUTHENTICATED_CODE);
    }
  };

  useEffect(() => {
    register(INPUT_NAME.EMAIL, "", true);
    register(INPUT_NAME.AUTHENTICATED_CODE, "", false);

    return () => {
      unRegister(INPUT_NAME.EMAIL);
      unRegister(INPUT_NAME.AUTHENTICATED_CODE);
    };
  }, []);

  useEffect(() => {
    if (timerSeconds > 0) return;

    alert(ERROR_MESSAGE.VALIDATION.TIMEOUT_EMAIL_AUTHENTICATION_CODE);

    resetTimer();

    setEmailStatus(EMAIL_STATUS.INPUT);
    reset(INPUT_NAME.AUTHENTICATED_CODE);
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
              value={value.email}
              name={INPUT_NAME.EMAIL}
              type="email"
              placeholder="이메일 주소를 입력해 주세요."
              onChange={handleChangeEmail}
              required
            />
            {getEmailButton()}
          </div>
        </div>
        {errorMessage.email && (
          <p className={styles["rule-field"]}>{errorMessage.email}</p>
        )}
      </div>

      {emailStatus === EMAIL_STATUS.WAITING_AUTHENTICATION && (
        <div className={styles.box}>
          <div className={styles.timer}>
            인증코드 유효시간
            <span className={styles["timer-time"]}>
              {formatTimerText(timerSeconds)}
            </span>
          </div>
          <div className={styles.box}>
            <div className={styles["text-field"]}>
              <Label className={styles.label} required>
                이메일 인증코드
              </Label>
              <div className={styles["input-box"]}>
                <TextInput
                  value={value.authenticationCode}
                  name={INPUT_NAME.AUTHENTICATED_CODE}
                  onChange={handleChange}
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
        </div>
      )}
    </>
  );
};

EmailField.propTypes = {
  emailStatus: PropTypes.string.isRequired,
  setEmailStatus: PropTypes.func.isRequired,
};

export default EmailField;
