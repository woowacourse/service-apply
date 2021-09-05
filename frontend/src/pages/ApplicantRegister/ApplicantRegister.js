import React from "react";
import { useHistory, useLocation } from "react-router-dom";
import {
  BirthField,
  Button,
  GenderField,
  SummaryCheckField,
} from "../../components/form";
import RecruitCard from "../../components/RecruitCard/RecruitCard";
import { POLICY_SUMMARY } from "../../constants/policySummary";
import useRecruitmentContext from "../../hooks/useRecruitmentContext";
import useTokenContext from "../../hooks/useTokenContext";
import FormProvider from "../../provider/FormProvider/FormProvider";
import InputField from "../../provider/FormProvider/InputField";
import SubmitButton from "../../provider/FormProvider/SubmitButton";
import {
  validateDay,
  validateYear,
  validateMonth,
} from "../../utils/validation/birth";
import { validateEmail } from "../../utils/validation/email";
import { validateName } from "../../utils/validation/name";
import {
  validatePassword,
  validateRePassword,
} from "../../utils/validation/password";
import { validatePhoneNumber } from "../../utils/validation/phoneNumber";
import styles from "./ApplicantRegister.module.css";

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
        birthday: { year, month, day },
      });

      history.push({
        pathname: "/application-forms/new",
        search: `?recruitmentId=${recruitmentId}`,
        state: { currentRecruitment },
      });
    } catch (e) {
      alert("이미 신청서를 작성했습니다. 로그인 페이지로 이동합니다.");
      history.push("/login");
    }
  };

  return (
    <div className={styles["applicant-register"]}>
      {currentRecruitment && (
        <RecruitCard
          title={currentRecruitment.title}
          startDateTime={currentRecruitment.startDateTime}
          endDateTime={currentRecruitment.endDateTime}
        />
      )}
      <FormProvider
        submit={submit}
        validators={{
          email: validateEmail,
          name: validateName,
          password: validatePassword,
          rePassword: validateRePassword,
          phoneNumber: validatePhoneNumber,
          year: validateYear,
          month: validateMonth,
          day: validateDay,
        }}
      >
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
          <InputField
            name="name"
            type="text"
            label="이름"
            placeholder="이름을 입력해 주세요."
            required
          />
        </div>
        <div>
          <InputField
            name="phoneNumber"
            type="text"
            label="전화번호"
            placeholder="연락 가능한 전화번호를 입력해 주세요."
            required
          />
        </div>
        <div>
          <InputField
            name="email"
            type="email"
            label="이메일"
            placeholder="이메일 주소를 입력해 주세요."
            required
          />
        </div>
        <div>
          <InputField
            name="password"
            type="password"
            label="비밀번호"
            placeholder="비밀번호를 입력해 주세요."
            required
          />
        </div>
        <div>
          <InputField
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
      </FormProvider>
    </div>
  );
};

export default ApplicantRegister;
