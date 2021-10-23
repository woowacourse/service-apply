import React from "react";
import { generatePath, useHistory } from "react-router";
import { useLocation, useParams } from "react-router-dom";
import * as Api from "../../api";
import Button, { BUTTON_VARIANT } from "../../components/@common/Button/Button";
import Container from "../../components/@common/Container/Container";
import Description from "../../components/@common/Description/Description";
import Label from "../../components/@common/Label/Label";
import MessageTextarea from "../../components/@common/MessageTextarea/MessageTextarea";
import MessageTextInput from "../../components/@common/MessageTextInput/MessageTextInput";
import CheckBox from "../../components/form/CheckBox/CheckBox";
import Form from "../../components/form/Form/Form";
import RecruitmentItem from "../../components/RecruitmentItem/RecruitmentItem";
import FORM from "../../constants/form";
import { CONFIRM_MESSAGE, SUCCESS_MESSAGE } from "../../constants/messages";
import PATH, { PARAM } from "../../constants/path";
import useApplicationRegisterForm, {
  APPLICATION_REGISTER_FORM_NAME,
} from "../../hooks/useApplicationRegisterForm";
import useRecruitmentItem from "../../hooks/useRecruitmentItem";
import useTokenContext from "../../hooks/useTokenContext";
import { formatDateTime } from "../../utils/format/date";
import { generateQuery, parseQuery } from "../../utils/route/query";
import styles from "./ApplicationRegister.module.css";

const ApplicationRegister = () => {
  const location = useLocation();
  const history = useHistory();
  const { status } = useParams();
  const { token } = useTokenContext();

  const { recruitmentId = null } = parseQuery(location.search);
  const currentRecruitment = location.state?.currentRecruitment ?? null;
  const { recruitmentItems = [] } = useRecruitmentItem(recruitmentId);

  const {
    form,
    handleChanges,
    errorMessage,
    modifiedDateTime,
    setModifiedDateTime,
    isEmpty,
    isValid,
    reset,
  } = useApplicationRegisterForm({
    recruitmentId,
    recruitmentItems,
    currentRecruitment,
    status,
  });

  const combineAnswers = (answers) =>
    recruitmentItems.map((item, index) => ({
      contents: answers[index] ?? "",
      recruitmentItemId: item.id,
    }));

  const handleSaveError = (error) => {
    if (!error) return;

    // TODO: 서버 에러응답을 클라이언트에서 분기처리하여 메시지 표시한다.
    alert(error.response.data.message);
  };

  const save = async ({ referenceUrl, answers }) => {
    try {
      await Api.updateForm({
        token,
        data: {
          recruitmentId,
          referenceUrl,
          answers: combineAnswers(answers),
          submitted: true,
        },
      });

      setModifiedDateTime(formatDateTime(new Date()));
      alert(SUCCESS_MESSAGE.API.SUBMIT_APPLICATION);
      history.replace(PATH.HOME);
    } catch (error) {
      handleSaveError(error);
    }
  };

  const tempSave = async ({ referenceUrl, answers }) => {
    try {
      await Api.updateForm({
        token,
        data: {
          recruitmentId,
          referenceUrl,
          answers: combineAnswers(answers),
          submitted: false,
        },
      });

      setModifiedDateTime(formatDateTime(new Date()));
      alert(SUCCESS_MESSAGE.API.SAVE_APPLICATION);

      if (status === PARAM.APPLICATION_FORM_STATUS.EDIT) return;

      const path = {
        pathname: generatePath(PATH.APPLICATION_FORM, {
          status: PARAM.APPLICATION_FORM_STATUS.EDIT,
        }),
        state: { currentRecruitment },
        search: generateQuery({ recruitmentId }),
      };

      history.replace(path);
    } catch (error) {
      handleSaveError(error);
    }
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    if (!window.confirm(CONFIRM_MESSAGE.SUBMIT_APPLICATION)) return;

    save(form);
  };

  return (
    <div className={styles.box}>
      {currentRecruitment && (
        <RecruitmentItem className={styles["recruitment-item"]} recruitment={currentRecruitment} />
      )}

      <Container title="지원서 작성">
        <Form onSubmit={handleSubmit}>
          {status === PARAM.APPLICATION_FORM_STATUS.EDIT && (
            <p className={styles["autosave-indicator"]}>
              {`임시 저장되었습니다. (${modifiedDateTime})`}
            </p>
          )}
          {recruitmentItems.map((item, index) => (
            <MessageTextarea
              key={index}
              value={form[APPLICATION_REGISTER_FORM_NAME.ANSWERS][index]}
              onChange={handleChanges[APPLICATION_REGISTER_FORM_NAME.ANSWERS](index)}
              name={`recruitment-item-${index}`}
              label={`${index + 1}. ${item.title}`}
              description={item.description}
              placeholder="내용을 입력해 주세요."
              maxLength={item.maximumLength}
              className={styles["label-bold"]}
              required
            />
          ))}

          <MessageTextInput
            name="url"
            type="url"
            description={
              <div className={styles["description-url"]}>
                자신을 드러낼 수 있는 개인 블로그, GitHub, 포트폴리오 주소 등이 있다면 입력해
                주세요.
                <div className={styles["description-url-small"]}>
                  여러 개가 있는 경우 Notion, Google 문서 등을 사용하여 하나로 묶어 주세요.
                </div>
              </div>
            }
            value={form[APPLICATION_REGISTER_FORM_NAME.REFERENCE_URL]}
            onChange={handleChanges[APPLICATION_REGISTER_FORM_NAME.REFERENCE_URL]}
            errorMessage={errorMessage[APPLICATION_REGISTER_FORM_NAME.REFERENCE_URL]}
            label="URL"
            className={styles["label-bold"]}
            maxLength={FORM.REFERENCE_URL_MAX_LENGTH}
            placeholder="ex) https://tecoble.techcourse.co.kr/"
          />

          <div className={styles["box-agree"]}>
            <Label className={styles["text-bold"]} required>
              지원서 작성 내용 사실 확인
            </Label>
            <Description className={styles["description-agree"]}>
              기재한 사실 중 허위사실이 발견되는 즉시, 교육 대상자에서 제외되며 향후 지원도
              불가능합니다.
            </Description>
            <CheckBox
              name="agree"
              label="동의합니다."
              checked={form[APPLICATION_REGISTER_FORM_NAME.IS_TERM_AGREED]}
              onChange={handleChanges[APPLICATION_REGISTER_FORM_NAME.IS_TERM_AGREED]}
              required
            />
          </div>

          <div className={styles.buttons}>
            <Button type="reset" variant={BUTTON_VARIANT.OUTLINED} onClick={reset}>
              초기화
            </Button>
            <Button type="button" onClick={() => tempSave(form)} disabled={!isValid}>
              임시 저장
            </Button>
            <Button disabled={!isValid || isEmpty}>제출</Button>
          </div>
        </Form>
      </Container>
    </div>
  );
};

export default ApplicationRegister;
