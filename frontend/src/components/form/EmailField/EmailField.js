import classNames from "classnames";
import PropTypes from "prop-types";
import useTimer from "../../../hooks/useTimer";
import { formatTimerText } from "../../../utils/format/date";
import Button from "../../@common/Button/Button";
import Label from "../../@common/Label/Label";
import TextInput from "../../@common/TextInput/TextInput";
import * as styles from "./EmailField.module.css";

export const EMAIL_STATUS = {
  INPUT: "input",
  WAITING_AUTHENTICATION: "waiting for authentication",
  AUTHENTICATED: "authenticated",
};

const EmailField = ({
  email,
  onChangeEmail,
  emailErrorMessage,
  emailCode,
  setEmailCode,
  emailStatus,
  setEmailStatus,
}) => {
  const {
    timerSeconds,
    onStart: onStartTimer,
    onStop: onStopTimer,
  } = useTimer(600);

  const getEmailRightButton = () => {
    if (emailStatus === EMAIL_STATUS.WAITING_AUTHENTICATION) {
      return null;
    }

    if (emailStatus === EMAIL_STATUS.INPUT) {
      return (
        <Button
          type="button"
          variant="outlined"
          onClick={handleIssueEmailCode}
          className={styles["input-button"]}
          disabled={email === "" || emailErrorMessage !== null}
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

  const handleIssueEmailCode = () => {
    // TODO: 인증코드 이메일 발송 api 요청(이메일 중복확인 메세지 필요)

    setEmailStatus(EMAIL_STATUS.WAITING_AUTHENTICATION);
    onStartTimer();
  };

  const handleAuthenticateEmail = () => {
    // TODO: 인증코드와 함께 이메일 인증 api 요청

    setEmailStatus(EMAIL_STATUS.AUTHENTICATED);
    onStopTimer();
  };

  return (
    <>
      <div className={styles.box}>
        <div className={styles["text-field"]}>
          <Label className={styles.label} required>
            이메일 인증코드
          </Label>
          <div className={styles["input-box"]}>
            <TextInput
              value={email}
              name="email"
              type="email"
              placeholder="이메일 주소를 입력해 주세요."
              onChange={onChangeEmail}
              required
            />
            {getEmailRightButton()}
          </div>
        </div>
        {emailErrorMessage && (
          <p className={styles["rule-field"]}>{emailErrorMessage}</p>
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
                  value={emailCode}
                  name="email-code"
                  onChange={({ target }) => setEmailCode(target.value)}
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
  email: PropTypes.string.isRequired,
  onChangeEmail: PropTypes.func.isRequired,
  emailErrorMessage: PropTypes.string.isRequired,
  emailCode: PropTypes.string.isRequired,
  setEmailCode: PropTypes.string.isRequired,
  emailStatus: PropTypes.string.isRequired,
  setEmailStatus: PropTypes.string.isRequired,
};

export default EmailField;
