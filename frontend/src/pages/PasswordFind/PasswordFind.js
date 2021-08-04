import React, { useState } from "react";
import { Link, useHistory } from "react-router-dom";

import Form from "../../components/form/Form/Form";
import TextField from "../../components/form/TextField/TextField";
import Button from "../../components/form/Button/Button";
import {
  isValid as isValidName,
  MESSAGE as NAME_MESSAGE,
} from "../../utils/validation/name";
import {
  isValid as isValidEmail,
  MESSAGE as EMAIL_MESSAGAE,
} from "../../utils/validation/email";
import styles from "./PasswordFind.module.css";
import { fetchPasswordFind } from "../../api/applicants";
import BirthField from "../../components/form/BirthField/BirthField";
import {
  DAY_MESSAGE,
  isDayValid,
  isMonthValid,
  isYearValid,
  MONTH_MESSAGE,
  YEAR_MESSAGE,
} from "../../utils/validation/birth";
import { formatLocalDate } from "../../utils/date";

const validator = {
  name: isValidName,
  email: isValidEmail,
  year: isYearValid,
  month: isMonthValid,
  day: isDayValid,
};

const message = {
  name: NAME_MESSAGE,
  email: EMAIL_MESSAGAE,
  year: YEAR_MESSAGE,
  month: MONTH_MESSAGE,
  day: DAY_MESSAGE,
};

const PasswordFind = () => {
  const [value, setValue] = useState({});
  const [errorMessage, setErrorMessage] = useState({});

  const history = useHistory();

  const disabled =
    Object.values(value).filter(Boolean).length < 5 ||
    Object.values(errorMessage).filter(Boolean).length > 0;

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      await fetchPasswordFind({
        name: value.name,
        email: value.email,
        birthday: formatLocalDate({
          year: value.year,
          month: value.month,
          day: value.day,
        }),
      });
      history.push({ path: `/find/result`, state: { email: value.email } });
    } catch (e) {
      alert(e.response.data.message);
    }
  };

  const handleChange = ({ target: { name, value } }) => {
    const isValid = validator[name](value);

    if (isValid) {
      setErrorMessage((errorMessage) => ({
        ...errorMessage,
        [name]: "",
      }));
    } else {
      setErrorMessage((errorMessage) => ({
        ...errorMessage,
        [name]: message[name],
      }));
    }

    setValue((prevValue) => ({ ...prevValue, [name]: value }));
  };

  return (
    <div className={styles.Login}>
      <Form onSubmit={handleSubmit}>
        <h1>비밀번호 찾기</h1>
        <TextField
          name="name"
          type="text"
          label="이름"
          value={value.name}
          placeholder="이름을 입력해 주세요."
          onChange={handleChange}
          required
        />
        <p className={styles["rule-field"]}>{errorMessage.name}</p>
        <TextField
          name="email"
          type="email"
          label="이메일"
          value={value.email}
          placeholder="이메일 주소를 입력해 주세요."
          onChange={handleChange}
          required
        />
        <p className={styles["rule-field"]}>{errorMessage.email}</p>
        <BirthField value={value} onChange={handleChange} required />
        <p className={styles["rule-field"]}>
          {errorMessage.year || errorMessage.month || errorMessage.day}
        </p>
        <div className={styles.buttons}>
          <Button cancel onClick={() => history.goBack()}>
            이전
          </Button>
          <Button disabled={disabled} type="submit">
            확인
          </Button>
        </div>
      </Form>
      <Link to="/find" className={styles["find-password"]}>
        비밀번호 찾기
      </Link>
    </div>
  );
};

export default PasswordFind;
