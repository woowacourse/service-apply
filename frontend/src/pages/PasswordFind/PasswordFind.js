import React from "react";
import { useHistory } from "react-router-dom";

import Button from "../../components/form/Button/Button";
import { validateName } from "../../utils/validation/name";
import { validateEmail } from "../../utils/validation/email";
import styles from "./PasswordFind.module.css";
import { fetchPasswordFind } from "../../api/applicants";
import BirthField from "../../components/form/BirthField/BirthField";
import {
  validateDay,
  validateMonth,
  validateYear,
} from "../../utils/validation/birth";
import { formatLocalDate } from "../../utils/date";
import SubmitButton from "../../provider/FormProvider/SubmitButton";
import FormProvider from "../../provider/FormProvider/FormProvider";
import InputField from "../../provider/FormProvider/InputField";
import useForm from "../../hooks/useForm";
import { Form } from "../../components/form";

const PasswordFind = () => {
  const history = useHistory();

  const submit = async (value) => {
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

  const { handleSubmit, ...methods } = useForm({
    validators: {
      name: validateName,
      email: validateEmail,
      year: validateYear,
      month: validateMonth,
      day: validateDay,
    },
    submit,
  });

  return (
    <div className={styles["password-find"]}>
      <FormProvider {...methods}>
        <Form onSubmit={handleSubmit}>
          <h2>비밀번호 찾기</h2>
          <InputField
            name="name"
            type="text"
            label="이름"
            placeholder="이름을 입력해 주세요."
            required
          />
          <InputField
            name="email"
            type="email"
            label="이메일"
            placeholder="이메일 주소를 입력해 주세요."
            required
          />
          <BirthField />
          <div className={styles.buttons}>
            <Button cancel onClick={() => history.goBack()}>
              이전
            </Button>
            <SubmitButton>확인</SubmitButton>
          </div>
        </Form>
      </FormProvider>
    </div>
  );
};

export default PasswordFind;
