import React, { useState } from "react";
import { useHistory, useLocation } from "react-router-dom";
import {
  BirthField,
  Button,
  Form,
  GenderField,
  SummaryCheckField,
  TextField,
} from "../../components/form";
import RecruitCard from "../../components/RecruitCard/RecruitCard";
import { ERROR_MESSAGE } from "../../constants/messages";
import { POLICY_SUMMARY } from "../../constants/policySummary";
import useRecruitmentContext from "../../hooks/useRecruitmentContext";
import useTokenContext from "../../hooks/useTokenContext";
import {
  isValidDay,
  isValidMonth,
  isValidYear,
} from "../../utils/validation/birth";
import { isValidEmail } from "../../utils/validation/email";
import { isValidName } from "../../utils/validation/name";
import { isValidPassword } from "../../utils/validation/password";
import { isValidPhoneNumber } from "../../utils/validation/phoneNumber";
import styles from "./ApplicantRegister.module.css";

const ApplicantRegister = () => {
  const location = useLocation();
  const history = useHistory();

  const { recruitmentId } = location.state;
  const { postRegister } = useTokenContext();
  const { recruitment } = useRecruitmentContext();

  const currentRecruitment = recruitment.findById(recruitmentId);

  const [value, setValue] = useState({
    policy: false,
    name: "",
    phoneNumber: "",
    email: "",
    password: "",
    rePassword: "",
    gender: "",
    birthday: {
      year: "",
      month: "",
      day: "",
    },
  });
  const [errorMessage, setErrorMessage] = useState({
    name: "",
    phoneNumber: "",
    email: "",
    password: "",
    rePassword: "",
    gender: "",
    birthday: {
      year: "",
      month: "",
      day: "",
    },
  });

  const onChangePolicy = ({ target }) => {
    setValue((prev) => ({ ...prev, policy: target.checked }));
  };

  const onChangeName = ({ target }) => {
    setValue((prev) => ({ ...prev, name: target.value }));

    if (isValidName(target.value)) {
      setErrorMessage((prev) => ({ ...prev, name: "" }));

      return;
    }

    setErrorMessage((prev) => ({
      ...prev,
      name: ERROR_MESSAGE.VALIDATION.NAME,
    }));
  };

  const onChangeEmail = ({ target }) => {
    setValue((prev) => ({ ...prev, email: target.value }));

    if (isValidEmail(target.value)) {
      setErrorMessage((prev) => ({ ...prev, email: "" }));

      return;
    }

    setErrorMessage((prev) => ({
      ...prev,
      email: ERROR_MESSAGE.VALIDATION.EMAIL,
    }));
  };

  const onChangePhoneNumber = ({ target }) => {
    setValue((prev) => ({ ...prev, phoneNumber: target.value }));

    if (isValidPhoneNumber(target.value)) {
      setErrorMessage((prev) => ({ ...prev, phoneNumber: "" }));

      return;
    }

    setErrorMessage((prev) => ({
      ...prev,
      phoneNumber: ERROR_MESSAGE.VALIDATION.PHONE_NUMBER,
    }));
  };

  const onChangePassword = ({ target }) => {
    setValue((prev) => ({ ...prev, password: target.value }));

    if (value.rePassword !== target.value) {
      setErrorMessage((prev) => ({
        ...prev,
        rePassword: "비밀번호를 확인해주세요.",
      }));
    } else {
      setErrorMessage((prev) => ({ ...prev, rePassword: "" }));
    }

    if (!isValidPassword(target.value)) {
      setErrorMessage((prev) => ({
        ...prev,
        password: ERROR_MESSAGE.VALIDATION.PASSWORD,
      }));

      return;
    }

    setErrorMessage((prev) => ({ ...prev, password: "" }));
  };

  const onChangeRePassword = ({ target }) => {
    setValue((prev) => ({ ...prev, rePassword: target.value }));

    if (target.value !== value.password) {
      setErrorMessage((prev) => ({
        ...prev,
        rePassword: ERROR_MESSAGE.VALIDATION.RE_PASSWORD,
      }));

      return;
    }

    setErrorMessage((prev) => ({ ...prev, rePassword: "" }));
  };

  const onChangeYear = ({ target }) => {
    setValue((prev) => ({
      ...prev,
      birthday: { ...prev.birthday, year: target.value },
    }));

    if (isValidYear(target.value)) {
      setErrorMessage((prev) => ({
        ...prev,
        birthday: { ...prev.birthday, year: "" },
      }));

      return;
    }

    setErrorMessage((prev) => ({
      ...prev,
      birthday: {
        ...prev.birthday,
        year: ERROR_MESSAGE.VALIDATION.YEAR,
      },
    }));
  };

  const onChangeMonth = ({ target }) => {
    setValue((prev) => ({
      ...prev,
      birthday: { ...prev.birthday, month: target.value },
    }));

    if (isValidMonth(target.value)) {
      setErrorMessage((prev) => ({
        ...prev,
        birthday: { ...prev.birthday, month: "" },
      }));

      return;
    }

    setErrorMessage((prev) => ({
      ...prev,
      birthday: {
        ...prev.birthday,
        month: ERROR_MESSAGE.VALIDATION.MONTH,
      },
    }));
  };

  const onChangeDay = ({ target }) => {
    setValue((prev) => ({
      ...prev,
      birthday: { ...prev.birthday, day: target.value },
    }));

    if (isValidDay(target.value)) {
      setErrorMessage((prev) => ({
        ...prev,
        birthday: { ...prev.birthday, day: "" },
      }));

      return;
    }

    setErrorMessage((prev) => ({
      ...prev,
      birthday: {
        ...prev.birthday,
        day: ERROR_MESSAGE.VALIDATION.DAY,
      },
    }));
  };

  const onChangeGender = ({ target }) => {
    setValue((prev) => ({ ...prev, gender: target.value }));
  };

  const completedForm =
    Object.keys(errorMessage).every((key) => {
      if (key === "birthday") {
        return Object.keys(errorMessage[key]).every(
          (birthdayKey) => errorMessage.birthday[birthdayKey] === ""
        );
      }

      return errorMessage[key] === "";
    }) &&
    Object.keys(value).every((key) => {
      if (key === "birthday") {
        return Object.keys(errorMessage[key]).every(
          (birthdayKey) => value.birthday[birthdayKey]
        );
      }

      return value[key];
    });

  const handleSubmit = async (event) => {
    event.preventDefault();

    const { target } = event;
    const data = {
      name: target.name.value,
      phoneNumber: target["phone-number"].value,
      email: target.email.value,
      password: target.password.value,
      gender: target.gender.value.toUpperCase(),
      birthday: new Date(
        target.year.value,
        target.month.value,
        target.day.value
      ),
    };

    await postRegister(data);

    history.push({
      pathname: "/application-forms/new",
      search: `?recruitmentId=${recruitmentId}`,
      state: { currentRecruitment },
    });
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
      <Form onSubmit={handleSubmit}>
        <h2>지원자 정보</h2>
        <div>
          <SummaryCheckField
            name="policy"
            label="개인정보 수집 및 이용 동의"
            onChange={onChangePolicy}
            required
          >
            <p className={styles.summary}>{POLICY_SUMMARY}</p>
          </SummaryCheckField>
        </div>
        <div>
          <TextField
            name="name"
            type="text"
            label="이름"
            placeholder="이름을 입력해 주세요."
            onChange={onChangeName}
            value={value.name}
            required
          />
          <p className={styles["rule-field"]}>{errorMessage.name}</p>
        </div>
        <div>
          <TextField
            name="phone-number"
            type="text"
            label="전화번호"
            placeholder="연락 가능한 전화번호를 입력해 주세요."
            onChange={onChangePhoneNumber}
            value={value.phoneNumber}
            required
          />
          <p className={styles["rule-field"]}>{errorMessage.phoneNumber}</p>
        </div>
        <div>
          <TextField
            name="email"
            type="email"
            label="이메일"
            placeholder="이메일 주소를 입력해 주세요."
            onChange={onChangeEmail}
            value={value.email}
            required
          />
          <p className={styles["rule-field"]}>{errorMessage.email}</p>
        </div>
        <div>
          <TextField
            name="password"
            type="password"
            label="비밀번호"
            placeholder="비밀번호를 입력해 주세요."
            onChange={onChangePassword}
            value={value.password}
            required
          />
          <p className={styles["rule-field"]}>{errorMessage.password}</p>
        </div>
        <div>
          <TextField
            name="re-password"
            type="password"
            label="비밀번호 확인"
            placeholder="비밀번호를 다시 한번 입력해 주세요."
            onChange={onChangeRePassword}
            value={value.rePassword}
            required
          />
          <p className={styles["rule-field"]}>{errorMessage.rePassword}</p>
        </div>
        <div>
          <BirthField
            year={value.birthday.year}
            month={value.birthday.month}
            day={value.birthday.day}
            onChangeYear={onChangeYear}
            onChangeMonth={onChangeMonth}
            onChangeDay={onChangeDay}
            required
          />
          <p className={styles["rule-field"]}>
            {errorMessage.birthday.year ||
              errorMessage.birthday.month ||
              errorMessage.birthday.day}
          </p>
        </div>
        <div>
          <GenderField onChange={onChangeGender} required />
        </div>
        <div className={styles["button-wrapper"]}>
          <Button cancel type="button">
            취소
          </Button>
          <Button type="submit" disabled={!completedForm}>
            다음
          </Button>
        </div>
      </Form>
    </div>
  );
};

export default ApplicantRegister;
