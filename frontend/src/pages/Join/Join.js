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
import FormInput from "../../components/form/FormInput/FormInput";
import GenderField from "../../components/form/GenderField/GenderField";
import SubmitButton from "../../components/form/SubmitButton/SubmitButton";
import SummaryCheckField from "../../components/form/SummaryCheckField/SummaryCheckField";
import { ERROR_MESSAGE } from "../../constants/messages";
import PATH from "../../constants/path";
import { POLICY_SUMMARY } from "../../constants/policySummary";
import usePhoneNumber from "../../hooks/usePhoneNumber";
import useForm from "../../hooks/useForm";
import useTokenContext from "../../hooks/useTokenContext";
import FormProvider from "../../provider/FormProvider";
import { formatBirthday } from "../../utils/format/date";
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

const Join = () => {
  const history = useHistory();

  const { postRegister } = useTokenContext();
  const { phoneNumber, handlePhoneNumberChange } = usePhoneNumber();

  const [emailStatus, setEmailStatus] = useState(EMAIL_STATUS.INPUT);

  const submit = async ({
    name,
    email,
    authenticationCode,
    password,
    gender,
    year,
    month,
    day,
  }) => {
    if (emailStatus !== EMAIL_STATUS.AUTHENTICATED) {
      alert(ERROR_MESSAGE.API.NOT_AUTHENTICATED);

      return;
    }

    try {
      await postRegister({
        name,
        phoneNumber,
        email,
        authenticationCode,
        password,
        gender,
        birthday: formatBirthday({ year, month, day }),
      });

      history.push(PATH.RECRUITS);
    } catch (e) {
      alert(ERROR_MESSAGE.API.JOIN_FAILURE);
    }
  };

  const { handleSubmit, ...methods } = useForm({
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

  return (
    <Container title="회원가입" size={CONTAINER_SIZE.NARROW}>
      <FormProvider {...methods}>
        <Form onSubmit={handleSubmit}>
          <SummaryCheckField
            name="policy"
            label="개인정보 수집 및 이용 동의"
            required
          >
            <p className={styles["summary-content"]}>{POLICY_SUMMARY}</p>
          </SummaryCheckField>

          <EmailField
            emailStatus={emailStatus}
            setEmailStatus={setEmailStatus}
          />

          <FormInput
            name="name"
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
            <SubmitButton>가입하기</SubmitButton>
          </div>
        </Form>
      </FormProvider>
    </Container>
  );
};

export default Join;
