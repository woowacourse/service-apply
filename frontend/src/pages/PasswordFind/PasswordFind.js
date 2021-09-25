import React from "react";
import { useHistory } from "react-router-dom";
import { fetchPasswordFind } from "../../api/applicants";
import BirthField from "../../components/form/BirthField/BirthField";
import PATH from "../../constants/path";
import useForm from "../../hooks/useForm";
import FormProvider from "../../provider/FormProvider";
import { formatBirthday } from "../../utils/date";
import {
  validateDay,
  validateMonth,
  validateYear,
} from "../../utils/validation/birth";
import { validateEmail } from "../../utils/validation/email";
import { validateName } from "../../utils/validation/name";
import styles from "./PasswordFind.module.css";
import Form from "../../components/form/Form/Form";
import Button from "../../components/@common/Button/Button";
import FormInput from "../../components/form/FormInput/FormInput";
import SubmitButton from "../../components/form/SubmitButton";

const PasswordFind = () => {
  const history = useHistory();

  const submit = async (value) => {
    try {
      await fetchPasswordFind({
        name: value.name,
        email: value.email,
        birthday: formatBirthday({
          year: value.year,
          month: value.month,
          day: value.day,
        }),
      });

      history.push({
        pathname: PATH.FIND_PASSWORD_RESULT,
        state: { email: value.email },
      });
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
          <FormInput
            name="name"
            type="text"
            label="이름"
            placeholder="이름을 입력해 주세요."
            required
          />
          <FormInput
            name="email"
            type="email"
            label="이메일"
            placeholder="이메일 주소를 입력해 주세요."
            required
          />
          <BirthField />
          <div className={styles.buttons}>
            <Button type="button" cancel onClick={() => history.goBack()}>
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
