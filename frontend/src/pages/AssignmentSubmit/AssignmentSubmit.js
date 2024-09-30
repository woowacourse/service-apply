import { useEffect } from "react";
import { useLocation, useParams } from "react-router";
import { useNavigate } from "react-router-dom";
import { fetchAssignment, patchAssignment, postAssignment } from "../../api/recruitments";
import Button from "../../components/@common/Button/Button";
import Container, { TITLE_ALIGN } from "../../components/@common/Container/Container";
import MessageTextarea from "../../components/@common/MessageTextarea/MessageTextarea";
import MessageTextInput from "../../components/@common/MessageTextInput/MessageTextInput";
import CancelButton from "../../components/form/CancelButton/CancelButton";
import Form from "../../components/form/Form/Form";
import { FORM } from "../../constants/form";
import { CONFIRM_MESSAGE, ERROR_MESSAGE, SUCCESS_MESSAGE } from "../../constants/messages";
import { PATH, PARAM } from "../../constants/path";
import useAssignmentForm, { ASSIGNMENT_FORM_NAME } from "../../hooks/useAssignmentForm";
import useTokenContext from "../../hooks/useTokenContext";
import styles from "./AssignmentSubmit.module.css";
import { MISSION_SUBMISSION_METHOD } from "../../constants/recruitment";

const AssignmentSubmit = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { status } = useParams();
  const { token } = useTokenContext();

  const recruitmentId = location.state?.recruitmentId ?? null;
  const currentMission = location.state?.currentMission ?? null;

  const submissionMethod =
    currentMission?.submissionMethod ?? MISSION_SUBMISSION_METHOD.PUBLIC_PULL_REQUEST;
  const urlInputLabel =
    submissionMethod === MISSION_SUBMISSION_METHOD.PRIVATE_REPOSITORY
      ? "저장소 주소"
      : "풀 리퀘스트 주소";

  const {
    form,
    errorMessage,
    init: initForm,
    handleChanges,
    isValid,
    isEmpty,
  } = useAssignmentForm();

  const handleSubmitError = (error) => {
    if (!error) return;

    alert(ERROR_MESSAGE.API.SUBMIT_ASSIGNMENT);
  };

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

      if (status === PARAM.ASSIGNMENT_STATUS.EDIT) {
        await patchAssignment(payload);
      }

      alert(SUCCESS_MESSAGE.API.SUBMIT_ASSIGNMENT);

      navigate(PATH.MY_APPLICATION);
    } catch (error) {
      handleSubmitError(error);
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
    if (!recruitmentId || !currentMission) {
      navigate(PATH.RECRUITS, { replace: true });
      return;
    }

    if (status !== PARAM.ASSIGNMENT_STATUS.EDIT) return;

    init();
  }, []);

  return (
    <Container title={currentMission?.title ?? ""} titleAlign={TITLE_ALIGN.LEFT}>
      <Form onSubmit={handleSubmit} className={styles.form}>
        <MessageTextInput
          label={urlInputLabel}
          type="url"
          name={ASSIGNMENT_FORM_NAME.URL}
          value={form[ASSIGNMENT_FORM_NAME.URL]}
          onChange={handleChanges[ASSIGNMENT_FORM_NAME.URL]}
          errorMessage={errorMessage[ASSIGNMENT_FORM_NAME.URL]}
          required
        />
        <MessageTextarea
          label="과제 진행 소감"
          name={ASSIGNMENT_FORM_NAME.NOTE}
          value={form[ASSIGNMENT_FORM_NAME.NOTE]}
          onChange={handleChanges[ASSIGNMENT_FORM_NAME.NOTE]}
          maxLength={FORM.NOTE_MAX_LENGTH}
          required
        />
        <p className={styles["info-message"]}>
          작성하신 내용은 과제 제출 마감 전까지 수정할 수 있습니다.
        </p>
        <div className={styles.buttons}>
          <CancelButton confirmMessage={CONFIRM_MESSAGE.CANCEL_ASSIGNMENT_SUBMIT} />
          <Button disabled={!isValid || isEmpty}>제출</Button>
        </div>
      </Form>
    </Container>
  );
};

export default AssignmentSubmit;
