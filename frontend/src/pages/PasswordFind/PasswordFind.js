import React from "react";
import { useHistory } from "react-router-dom";
import { fetchPasswordFind } from "../../api/applicants";
import Container, {
  CONTAINER_SIZE,
} from "../../components/@common/Container/Container";
import BirthField from "../../components/form/BirthField/BirthField";
import PATH from "../../constants/path";
import useForm from "../../hooks/useForm";
import { formatBirthday } from "../../utils/format/date";
import {
  validateDay,
  validateMonth,
  validateYear,
} from "../../utils/validation/birth";
import { validateEmail } from "../../utils/validation/email";
import { validateName } from "../../utils/validation/name";
import styles from "./PasswordFind.module.css";
import Button from "../../components/@common/Button/Button";
import FormInput from "../../components/form/FormInput/FormInput";
import Form from "../../components/form/Form/Form";
import FormProvider from "../../provider/FormProvider";
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
    <Container size={CONTAINER_SIZE.NARROW} title="비밀번호 찾기">
      <FormProvider {...methods}>
        <Form onSubmit={handleSubmit}>
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
          <BirthField required />
          <div className={styles.buttons}>
            <Button type="button" cancel onClick={() => history.goBack()}>
              이전
            </Button>
            <SubmitButton />
          </div>
        </Form>
      </FormProvider>
    </Container>
  );
};

export default PasswordFind;
