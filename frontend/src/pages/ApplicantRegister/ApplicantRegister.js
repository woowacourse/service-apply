import React from "react";
import { generatePath, useHistory, useLocation } from "react-router-dom";
import Button from "../../components/@common/Button/Button";
import Container, {
  CONTAINER_SIZE,
} from "../../components/@common/Container/Container";
import MessageTextInput from "../../components/@common/MessageTextInput/MessageTextInput";
import BirthField from "../../components/form/BirthField/BirthField";
import Form from "../../components/form/Form/Form";
import FormInput from "../../components/form/FormInput/FormInput";
import GenderField from "../../components/form/GenderField/GenderField";
import SubmitButton from "../../components/form/SubmitButton/SubmitButton";
import SummaryCheckField from "../../components/form/SummaryCheckField/SummaryCheckField";
import RecruitmentItem from "../../components/RecruitmentItem/RecruitmentItem";
import { ERROR_MESSAGE } from "../../constants/messages";
import PATH, { PARAM } from "../../constants/path";
import { POLICY_SUMMARY } from "../../constants/policySummary";
import useApplicantRegisterForm from "../../hooks/useApplicantRegisterForm";
import useForm from "../../hooks/useForm";
import useRecruitmentContext from "../../hooks/useRecruitmentContext";
import useTokenContext from "../../hooks/useTokenContext";
import FormProvider from "../../provider/FormProvider";
import { formatBirthday } from "../../utils/format/date";
import { generateQuery } from "../../utils/route/query";
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
import styles from "./ApplicantRegister.module.css";

const ApplicantRegister = () => {
  const location = useLocation();
  const history = useHistory();

  const { recruitmentId } = location.state;
  const { postRegister } = useTokenContext();
  const { recruitment } = useRecruitmentContext();
  const { phoneNumber, handlePhoneNumberChange } = useApplicantRegisterForm();

  const currentRecruitment = recruitment.findById(recruitmentId);

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

      history.push({
        pathname: generatePath(PATH.APPLICATION_FORM, {
          status: PARAM.APPLICATION_FORM_STATUS.NEW,
        }),
        search: generateQuery({ recruitmentId }),
        state: { currentRecruitment },
      });
    } catch (e) {
      alert(ERROR_MESSAGE.API.ALREADY_HAS_APPLICATION);
      history.push(PATH.LOGIN);
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
    <div className={styles.box}>
      {currentRecruitment && (
        <RecruitmentItem
          recruitment={currentRecruitment}
          className={styles.recruitment}
        />
      )}

      <Container title="회원가입" size={CONTAINER_SIZE.NARROW}>
        <FormProvider {...methods}>
          <Form onSubmit={handleSubmit}>
            <div>
              <SummaryCheckField
                name="policy"
                label="개인정보 수집 및 이용 동의"
                required
              >
                <p className={styles.summary}>{POLICY_SUMMARY}</p>
              </SummaryCheckField>
            </div>
            <div>
              <FormInput
                name="name"
                type="text"
                label="이름"
                placeholder="이름을 입력해 주세요."
                required
              />
            </div>
            <div>
              <MessageTextInput
                name="phoneNumber"
                type="tel"
                label="전화번호"
                value={phoneNumber}
                onChange={handlePhoneNumberChange}
                placeholder="연락 가능한 전화번호를 입력해 주세요."
                required
              />
            </div>
            <div>
              <FormInput
                name="email"
                type="email"
                label="이메일"
                placeholder="이메일 주소를 입력해 주세요."
                required
              />
            </div>
            <div>
              <FormInput
                name="password"
                type="password"
                label="비밀번호"
                placeholder="비밀번호를 입력해 주세요."
                required
              />
            </div>
            <div>
              <FormInput
                name="rePassword"
                type="password"
                label="비밀번호 확인"
                placeholder="비밀번호를 다시 한번 입력해 주세요."
                required
              />
            </div>
            <div>
              <BirthField required />
            </div>
            <div>
              <GenderField />
            </div>
            <div className={styles.buttons}>
              <Button cancel type="button">
                취소
              </Button>
              <SubmitButton>다음</SubmitButton>
            </div>
          </Form>
        </FormProvider>
      </Container>
    </div>
  );
};

export default ApplicantRegister;
