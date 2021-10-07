import classNames from "classnames";
import { useState } from "react";
import { useHistory } from "react-router-dom";
import { directive } from "../../../../../../../Library/Caches/typescript/4.4/node_modules/@babel/types/lib/index";
import Button from "../../components/@common/Button/Button";
import Container, {
  CONTAINER_SIZE,
} from "../../components/@common/Container/Container";
import MessageTextInput from "../../components/@common/MessageTextInput/MessageTextInput";
import BirthField from "../../components/form/BirthField/BirthField";
import Form from "../../components/form/Form/Form";
import FormInput from "../../components/form/FormInput/FormInput";
import GenderField from "../../components/form/GenderField/GenderField";
import SubmitButton from "../../components/form/SubmitButton";
import SummaryCheckField from "../../components/form/SummaryCheckField/SummaryCheckField";
import { ERROR_MESSAGE } from "../../constants/messages";
import PATH from "../../constants/path";
import { POLICY_SUMMARY } from "../../constants/policySummary";
import useApplicantRegisterForm from "../../hooks/useApplicantRegisterForm";
import useForm from "../../hooks/useForm";
import useTimer from "../../hooks/useTimer";
import useTokenContext from "../../hooks/useTokenContext";
import FormProvider from "../../provider/FormProvider";
import { formatBirthday, formatTimerText } from "../../utils/format/date";
import {
  validateDay,
  validateMonth,
  validateYear,
} from "../../utils/validation/birth";
import { validateEmail } from "../../utils/validation/email";
import { validateName } from "../../utils/validation/name";
import {
  validatePassword,
  validateRePassword,
} from "../../utils/validation/password";
import styles from "./Join.module.css";

const EMAIL_STATUS = {
  INPUT: "input",
  WAITING_AUTHENTICATION: "waiting for authentication",
  AUTHENTICATED: "authenticated",
};

const Join = () => {
  const history = useHistory();

  const { postRegister } = useTokenContext();
  const { phoneNumber, handlePhoneNumberChange } = useApplicantRegisterForm();

  const [emailStatus, setEmailStatus] = useState(EMAIL_STATUS.INPUT);
  const [emailCode, setEmailCode] = useState("");
  const {
    timerSeconds,
    onStart: onStartTimer,
    onStop: onStopTimer,
  } = useTimer(600);

  const submit = async ({
    name,
    email,
    password,
    gender,
    year,
    month,
    day,
  }) => {
    try {
      await postRegister({
        name,
        phoneNumber,
        email,
        password,
        gender,
        birthday: formatBirthday({ year, month, day }),
      });

      history.push(PATH.RECRUITS);
    } catch (e) {
      alert(ERROR_MESSAGE.API.JOIN_FAILURE);
      history.push(PATH.LOGIN);
    }
  };

  const { handleSubmit, errorMessage, value, ...methods } = useForm({
    validators: {
      email: validateEmail,
      name: validateName,
      password: validatePassword,
      rePassword: validateRePassword,
      year: validateYear,
      month: validateMonth,
      day: validateDay,
    },
    submit,
  });

  const getEmailRightButton = () => {
    if (emailStatus === EMAIL_STATUS.WAITING_AUTHENTICATION) {
      return null;
    }

    if (emailStatus === EMAIL_STATUS.INPUT) {
      return (
        <Button
          type="button"
          variant="outlined"
          onClick={onIssueEmailCode}
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
          type="button"
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

  const onIssueEmailCode = () => {
    // TODO: 인증코드 이메일 발송 api 요청(이메일 중복확인 메세지 필요)

    setEmailStatus(EMAIL_STATUS.WAITING_AUTHENTICATION);
    onStartTimer();
  };

  const onAuthenticateEmail = () => {
    // TODO: 인증코드와 함께 이메일 인증 api 요청

    setEmailStatus(EMAIL_STATUS.AUTHENTICATED);
    onStopTimer();
  };

  return (
    <Container title="회원가입" size={CONTAINER_SIZE.NARROW}>
      <FormProvider errorMessage={errorMessage} value={value} {...methods}>
        <Form onSubmit={handleSubmit}>
          <SummaryCheckField
            name="policy"
            label="개인정보 수집 및 이용 동의"
            required
          >
            <p className={styles["summary-content"]}>{POLICY_SUMMARY}</p>
          </SummaryCheckField>

          <FormInput
            name="email"
            type="email"
            label="이메일"
            placeholder="이메일 주소를 입력해 주세요."
            rightButton={getEmailRightButton()}
            required
          />
          {emailStatus === EMAIL_STATUS.WAITING_AUTHENTICATION && (
            <div className={styles["relative-box"]}>
              <div className={styles.timer}>
                인증코드 유효시간
                <span className={styles["timer-time"]}>
                  {formatTimerText(timerSeconds)}
                </span>
              </div>

              <MessageTextInput
                name="email-code"
                value={emailCode}
                onChange={({ target }) => setEmailCode(target.value)}
                type="text"
                label="이메일 인증코드"
                placeholder="이메일로 발송된 인증코드를 입력해주세요."
                rightButton={
                  <Button
                    type="button"
                    onClick={onAuthenticateEmail}
                    className={styles["input-button"]}
                  >
                    인증
                    <br />
                    하기
                  </Button>
                }
                className={styles["input-box"]}
                required
              />
            </div>
          )}
          <FormInput
            name="name"
            type="text"
            label="이름"
            placeholder="이름을 입력해 주세요."
            required
          />
          <MessageTextInput
            name="phoneNumber"
            type="tel"
            label="전화번호"
            value={phoneNumber}
            onChange={handlePhoneNumberChange}
            placeholder="연락 가능한 전화번호를 입력해 주세요."
            className={styles["input-box"]}
            required
          />
          <FormInput
            name="password"
            type="password"
            label="비밀번호"
            placeholder="비밀번호를 입력해 주세요."
            required
          />
          <FormInput
            name="rePassword"
            type="password"
            label="비밀번호 확인"
            placeholder="비밀번호를 다시 한번 입력해 주세요."
            required
          />
          <BirthField className={styles["input-box"]} required />
          <GenderField className={styles["input-box"]} required />

          <div className={styles.buttons}>
            <Button cancel type="button">
              취소
            </Button>
            <SubmitButton>회원가입</SubmitButton>
          </div>
        </Form>
      </FormProvider>
    </Container>
  );
};

export default Join;
