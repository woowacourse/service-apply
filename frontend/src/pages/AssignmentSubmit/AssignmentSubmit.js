import Container from "../../components/@common/Container/Container";
import CancelButton from "../../components/form/CancelButton/CancelButton";
import Form from "../../components/form/Form/Form";
import FormInput from "../../components/form/FormInput/FormInput";
import FormTextarea from "../../components/form/FormTextarea/FormTextarea";
import SubmitButton from "../../components/form/SubmitButton/SubmitButton";
import useForm from "../../hooks/useForm";
import FormProvider from "../../provider/FormProvider";
import styles from "./AssignmentSubmit.module.css";

const AssignmentSubmit = () => {
  const submit = () => {};

  const { handleSubmit, ...methods } = useForm({
    validators: {},
    submit,
  });

  return (
    <Container title="프리코스 1차 과제">
      <FormProvider {...methods}>
        <Form onSubmit={handleSubmit} className={styles.form}>
          <FormInput label="GitHub ID" required />
          <FormInput label="Pull Request 주소" required />
          <FormTextarea label="과제 진행 소감" required />
          <p className={styles["info-message"]}>
            작성하신 내용은 과제 체출 마감전까지 수정하실 수 있습니다.
          </p>
          <div className={styles.buttons}>
            <CancelButton />
            <SubmitButton />
          </div>
        </Form>
      </FormProvider>
    </Container>
  );
};

export default AssignmentSubmit;
