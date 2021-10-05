import Container from "../../components/@common/Container/Container";
import Form from "../../components/form/Form/Form";
import FormInput from "../../components/form/FormInput/FormInput";
import FormTextarea from "../../components/form/FormTextarea/FormTextarea";
import SubmitButton from "../../components/form/SubmitButton";
import useForm from "../../hooks/useForm";
import FormProvider from "../../provider/FormProvider";

const AssignmentSubmit = () => {
  const submit = () => {};

  const { handleSubmit, ...methods } = useForm({
    validators: {},
    submit,
  });

  return (
    <Container title="프리코스 1차 과제">
      <FormProvider {...methods}>
        <Form onSubmit={handleSubmit}>
          <FormInput label="GitHub ID" required />
          <FormInput label="Pull Request 주소" required />
          <FormTextarea label="과제 진행 소감" required />
          {/* <CancelButton /> */}
          <SubmitButton />
        </Form>
      </FormProvider>
    </Container>
  );
};

export default AssignmentSubmit;
