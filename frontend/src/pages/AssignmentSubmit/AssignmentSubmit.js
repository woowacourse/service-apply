import { useEffect } from "react";
import { useHistory, useLocation, useParams } from "react-router";
import { fetchAssignment, patchAssignment, postAssignment } from "../../api/recruitments";
import Button, { BUTTON_VARIANT } from "../../components/@common/Button/Button";
import Container from "../../components/@common/Container/Container";
import MessageTextarea from "../../components/@common/MessageTextarea/MessageTextarea";
import MessageTextInput from "../../components/@common/MessageTextInput/MessageTextInput";
import Form from "../../components/form/Form/Form";
import FORM from "../../constants/form";
import { CONFIRM_MESSAGE, ERROR_MESSAGE, SUCCESS_MESSAGE } from "../../constants/messages";
import PATH, { PARAM } from "../../constants/path";
import useAssignmentForm, { ASSIGNMENT_FORM } from "../../hooks/useAssignmentForm";
import useTokenContext from "../../hooks/useTokenContext";
import styles from "./AssignmentSubmit.module.css";

const AssignmentSubmit = () => {
  const history = useHistory();
  const location = useLocation();
  const { status } = useParams();
  const { token } = useTokenContext();

  const { recruitmentId, currentMission } = location.state;

  const {
    form,
    errorMessage,
    init: initForm,
    handleChange,
    isValid,
    isEmpty,
  } = useAssignmentForm();

  const handleSubmit = async (event) => {
    event.preventDefault();

    const payload = {
      recruitmentId,
      missionId: currentMission.id,
      token,
      assignmentData: form,
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
      console.error(e);
      alert(ERROR_MESSAGE.API.SUBMIT_ASSIGNMENT);
    }
  };

  const handleCancel = () => {
    if (window.confirm(CONFIRM_MESSAGE.CANCEL_ASSIGNMENT_SUBMIT)) {
      history.goBack();
    }
  };

  const init = async () => {
    const response = await fetchAssignment({
      recruitmentId,
      missionId: currentMission.id,
      token,
    });

    initForm({ requiredForm: response.data });
  };

  useEffect(() => {
    if (status !== PARAM.ASSIGNMENT_STATUS.EDIT) return;

    init();
  }, []);

  return (
    <Container title={currentMission.title}>
      <Form onSubmit={handleSubmit} className={styles.form}>
        <MessageTextInput
          label="GitHub ID"
          name={ASSIGNMENT_FORM.GITHUB_USERNAME}
          value={form[ASSIGNMENT_FORM.GITHUB_USERNAME]}
          onChange={handleChange[ASSIGNMENT_FORM.GITHUB_USERNAME]}
          maxLength={FORM.GITHUB_USERNAME_MAX_LENGTH}
          required
        />
        <MessageTextInput
          label="Pull Request 주소"
          type="url"
          name={ASSIGNMENT_FORM.PULL_REQUEST_URL}
          value={form[ASSIGNMENT_FORM.PULL_REQUEST_URL]}
          onChange={handleChange[ASSIGNMENT_FORM.PULL_REQUEST_URL]}
          errorMessage={errorMessage[ASSIGNMENT_FORM.PULL_REQUEST_URL]}
          required
        />
        <MessageTextarea
          label="과제 진행 소감"
          name={ASSIGNMENT_FORM.NOTE}
          value={form[ASSIGNMENT_FORM.NOTE]}
          onChange={handleChange[ASSIGNMENT_FORM.NOTE]}
          maxLength={FORM.NOTE_MAX_LENGTH}
          required
        />
        <p className={styles["info-message"]}>
          작성하신 내용은 과제 제출 마감전까지 수정하실 수 있습니다.
        </p>
        <div className={styles.buttons}>
          <Button type="button" variant={BUTTON_VARIANT.OUTLINED} onClick={handleCancel}>
            취소
          </Button>
          <Button disabled={!isValid || isEmpty}>제출</Button>
        </div>
      </Form>
    </Container>
  );
};

export default AssignmentSubmit;
