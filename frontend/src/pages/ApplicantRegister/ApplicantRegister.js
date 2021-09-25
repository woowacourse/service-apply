import React from "react";
import { useHistory, useLocation, generatePath } from "react-router-dom";

import RecruitCard from "../../components/RecruitCard/RecruitCard";
import { ERROR_MESSAGE } from "../../constants/messages";
import PATH, { PARAM } from "../../constants/path";
import { POLICY_SUMMARY } from "../../constants/policySummary";
import useForm from "../../hooks/useForm";
import useRecruitmentContext from "../../hooks/useRecruitmentContext";
import useTokenContext from "../../hooks/useTokenContext";
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
import { validatePhoneNumber } from "../../utils/validation/phoneNumber";
import styles from "./ApplicantRegister.module.css";
import { generateQuery } from "../../utils/route/query";
import { formatBirthday } from "../../utils/date";
import Form from "../../components/form/Form/Form";
import BirthField from "../../components/form/BirthField/BirthField";
import GenderField from "../../components/form/GenderField/GenderField";
import Button from "../../components/@common/Button/Button";
import SummaryCheckField from "../../components/form/SummaryCheckField/SummaryCheckField";
import FormInput from "../../components/form/FormInput/FormInput";
import FormProvider from "../../provider/FormProvider";
import SubmitButton from "../../components/form/SubmitButton";

const ApplicantRegister = () => {
  const location = useLocation();
  const history = useHistory();

  const { recruitmentId } = location.state;
  const { postRegister } = useTokenContext();
  const { recruitment } = useRecruitmentContext();

  const currentRecruitment = recruitment.findById(recruitmentId);

  const submit = async ({
    name,
    phoneNumber,
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
      phoneNumber: validatePhoneNumber,
      year: validateYear,
      month: validateMonth,
      day: validateDay,
    },
    submit,
  });

  return (
    <div className={styles["applicant-register"]}>
      {currentRecruitment && (
        <RecruitCard
          title={currentRecruitment.title}
          startDateTime={currentRecruitment.startDateTime}
          endDateTime={currentRecruitment.endDateTime}
        />
      )}
      <FormProvider {...methods}>
        <Form onSubmit={handleSubmit}>
          <h2>지원자 정보</h2>
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
            <FormInput
              name="phoneNumber"
              type="text"
              label="전화번호"
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
            <BirthField />
          </div>
          <div>
            <GenderField />
          </div>
          <div className={styles["button-wrapper"]}>
            <Button cancel type="button">
              취소
            </Button>
            <SubmitButton>다음</SubmitButton>
          </div>
        </Form>
      </FormProvider>
    </div>
  );
};

export default ApplicantRegister;
