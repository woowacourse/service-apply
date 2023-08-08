import { useLocation } from "react-router-dom";
import styles from "./ApplicationRegister.module.css";
import Button, { BUTTON_VARIANT } from "../../components/@common/Button/Button";
import Container, { TITLE_ALIGN } from "../../components/@common/Container/Container";
import Description from "../../components/@common/Description/Description";
import Label from "../../components/@common/Label/Label";
import MessageTextarea from "../../components/@common/MessageTextarea/MessageTextarea";
import MessageTextInput from "../../components/@common/MessageTextInput/MessageTextInput";
import CheckBox from "../../components/form/CheckBox/CheckBox";
import Form from "../../components/form/Form/Form";
import ApplicationPreviewModal from "../../components/ApplicationPreviewModal/ApplicationPreviewModal";
import { FORM } from "../../constants/form";
import useApplicationRegisterForm from "../../hooks/useApplicationRegisterFormFetching";
import useModalContext from "../../hooks/useModalContext";
import useRecruitmentItem from "../../hooks/useRecruitmentItem";
import useApplicationRegisterFormWriting from "../../hooks/useApplicationRegisterFormWriting";
import { parseQuery } from "../../utils/route/query";
import useRecruits from "../../hooks/useRecruits";
import { useMemo } from "react";

const ApplicationRegister = () => {
  const location = useLocation();
  const searchQueries = useMemo(() => parseQuery(location.search), [location]);
  const recruitmentId = useMemo(() => parseInt(searchQueries?.recruitmentId, 10), [searchQueries]);

  const {
    tabs: { filteredRecruitments },
  } = useRecruits();
  const currentRecruitment = useMemo(
    () => filteredRecruitments.find(({ id }) => id === recruitmentId),
    [filteredRecruitments]
  );
  const { recruitmentItems = [] } = useRecruitmentItem(recruitmentId);

  const { Modal } = useModalContext();

  const {
    isEmpty,
    isValid,
    form,
    errorMessage,

    modifiedDateTime,
    setModifiedDateTime,

    reset,
    handleChanges,
  } = useApplicationRegisterForm({
    recruitmentId,
    recruitmentItems,
  });

  const { updateFormAnswers, handleSubmit } = useApplicationRegisterFormWriting({
    form,
    recruitmentId,
    recruitmentItems,
    modifiedDateTime,
    setModifiedDateTime,
  });

  if (!recruitmentId || !currentRecruitment) {
    return;
  }

  return (
    <div className={styles.box}>
      <Container title="지원서 작성" titleAlign={TITLE_ALIGN.LEFT}>
        <h3 className={styles["recruitment-title"]}>{currentRecruitment.title}</h3>
        <Form onSubmit={handleSubmit}>
          <>
            {modifiedDateTime && (
              <p className={styles["autosave-indicator"]}>
                {`임시 저장되었습니다. (${modifiedDateTime})`}
              </p>
            )}
            {recruitmentItems.map((item, index) => (
              <MessageTextarea
                key={index}
                value={form.answers?.[index] ?? ""}
                onChange={handleChanges.answers(index)}
                name={`recruitment-item-${index}`}
                label={`${index + 1}. ${item.title}`}
                description={item.description}
                placeholder="내용을 입력해 주세요."
                maxLength={item.maximumLength}
                className={styles["label-bold"]}
                showCount
                required
              />
            ))}

            <MessageTextInput
              name="url"
              type="url"
              description={
                <div className={styles["description-url"]}>
                  작성한 몰입 경험과 관련된 개인 블로그, GitHub, 그 외 증빙 자료 등이 있다면 입력해
                  주세요.
                  <div className={styles["description-url-small"]}>
                    여러 개가 있는 경우 Notion이나 Google 문서 등을 사용하여 하나로 묶어 주세요.
                    링크를 공유하실 때는 해당 링크가 공개 권한으로 접근 가능한지 확인해 주세요.
                  </div>
                </div>
              }
              value={form.referenceUrl}
              onChange={handleChanges.referenceUrl}
              errorMessage={errorMessage.referenceUrl}
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
                checked={form.isTermAgreed}
                onChange={handleChanges.isTermAgreed}
                required
              />
            </div>

            <div className={styles.buttons}>
              <Button type="reset" variant={BUTTON_VARIANT.OUTLINED} onClick={reset}>
                초기화
              </Button>
              <Button type="button" onClick={() => updateFormAnswers(false)} disabled={!isValid}>
                임시 저장
              </Button>
              <Button disabled={!isValid || isEmpty}>제출</Button>
            </div>
          </>
        </Form>
      </Container>
      <Modal>
        <ApplicationPreviewModal
          recruitmentItems={recruitmentItems}
          answers={form?.answers ?? []}
          referenceUrl={form.referenceUrl}
          onClickConfirmButton={() => updateFormAnswers(true)}
        />
      </Modal>
    </div>
  );
};

export default ApplicationRegister;
