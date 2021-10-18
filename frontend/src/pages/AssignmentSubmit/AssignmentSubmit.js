import { useEffect, useState } from "react";
import { useHistory, useLocation, useParams } from "react-router";
import {
  fetchAssignment,
  patchAssignment,
  postAssignment,
} from "../../api/recruitments";
import Container from "../../components/@common/Container/Container";
import CancelButton from "../../components/form/CancelButton/CancelButton";
import Form from "../../components/form/Form/Form";
import FormInput from "../../components/form/FormInput/FormInput";
import FormTextarea from "../../components/form/FormTextarea/FormTextarea";
import SubmitButton from "../../components/form/SubmitButton/SubmitButton";
import {
  CONFIRM_MESSAGE,
  ERROR_MESSAGE,
  SUCCESS_MESSAGE,
} from "../../constants/messages";
import PATH, { PARAM } from "../../constants/path";
import useForm from "../../hooks/useForm";
import useTokenContext from "../../hooks/useTokenContext";
import FormProvider from "../../provider/FormProvider";
import styles from "./AssignmentSubmit.module.css";

const AssignmentSubmit = () => {
  const history = useHistory();
  const location = useLocation();
  const { status } = useParams();
  const { token } = useTokenContext();

  const { recruitmentId, currentMission } = location.state;

  const [initialFormData, setInitialFormData] = useState({});

  const handleSubmitError = (e) => {
    if (e.response.status === 401) {
      alert(ERROR_MESSAGE.API.TOKEN_EXPIRED);
      history.push(PATH.LOGIN);
    } else {
      alert(ERROR_MESSAGE.API.SUBMIT_ASSIGNMENT);
    }
  };

  const submit = async (assignmentData) => {
    const payload = {
      recruitmentId,
      missionId: currentMission.id,
      token,
      assignmentData,
    };

    try {
      if (status === PARAM.ASSIGNMENT_STATUS.NEW) {
        await postAssignment(payload);
      }

      if (status === PARAM.APPLICATION_FORM_STATUS.EDIT) {
        await patchAssignment(payload);
      }

      alert(SUCCESS_MESSAGE.API.SUBMIT_ASSIGNMENT);

      history.push(PATH.MY_APPLICATION);
    } catch (e) {
      handleSubmitError(e);
    }
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

  const init = async () => {
    const response = await fetchAssignment({
      recruitmentId,
      missionId: currentMission.id,
      token,
    });

    setInitialFormData(response.data);
  };

  useEffect(() => {
    if (status !== PARAM.ASSIGNMENT_STATUS.EDIT) return;

    init();
  }, []);

  return (
    <Container title={currentMission.title}>
      <FormProvider {...methods}>
        <Form onSubmit={handleSubmit} className={styles.form}>
          <FormInput
            label="GitHub ID"
            name="githubUsername"
            initialValue={initialFormData?.githubUsername}
            required
          />
          <FormInput
            type="url"
            label="Pull Request 주소"
            name="pullRequestUrl"
            initialValue={initialFormData?.pullRequestUrl}
            required
          />
          <FormTextarea
            label="과제 진행 소감"
            name="note"
            initialValue={initialFormData?.note}
            required
          />
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
