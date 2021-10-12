import { useHistory, useLocation } from "react-router";
import { postAssignment } from "../../api/recruitments";
import Container from "../../components/@common/Container/Container";
import CancelButton from "../../components/form/CancelButton/CancelButton";
import Form from "../../components/form/Form/Form";
import FormInput from "../../components/form/FormInput/FormInput";
import FormTextarea from "../../components/form/FormTextarea/FormTextarea";
import SubmitButton from "../../components/form/SubmitButton/SubmitButton";
import { CONFIRM_MESSAGE } from "../../constants/messages";
import PATH from "../../constants/path";
import useForm from "../../hooks/useForm";
import useTokenContext from "../../hooks/useTokenContext";
import FormProvider from "../../provider/FormProvider";
import styles from "./AssignmentSubmit.module.css";

const AssignmentSubmit = () => {
  const history = useHistory();
  const location = useLocation();
  const { token } = useTokenContext();

  const { recruitmentId, currentMission } = location.state;

  const submit = async (assignmentData) => {
    try {
      await postAssignment({
        recruitmentId,
        missionId: currentMission.id,
        token,
        assignmentData,
      });

      history.push(PATH.MY_APPLICATION);
    } catch (e) {}
  };

  const handleCancel = () => {
    if (window.confirm(CONFIRM_MESSAGE.CANCEL_ASSIGNMENT_SUBMIT)) {
      history.goBack();
    }
  };

  const { handleSubmit, ...methods } = useForm({
    validators: {},
    submit,
  });

  return (
    <Container title={currentMission.title}>
      <FormProvider {...methods}>
        <Form onSubmit={handleSubmit} className={styles.form}>
          <FormInput label="GitHub ID" name="githubUsername" required />
          <FormInput
            type="url"
            label="Pull Request 주소"
            name="pullRequestUrl"
            required
          />
          <FormTextarea label="과제 진행 소감" name="note" required />
          <p className={styles["info-message"]}>
            작성하신 내용은 과제 제출 마감전까지 수정하실 수 있습니다.
          </p>
          <div className={styles.buttons}>
            <CancelButton onClick={handleCancel} />
            <SubmitButton />
          </div>
        </Form>
      </FormProvider>
    </Container>
  );
};

export default AssignmentSubmit;
