import React, { useCallback, useEffect, useState } from "react";
import {
  useHistory,
  useLocation,
  useParams,
  generatePath,
} from "react-router-dom";
import * as Api from "../../api";

import {
  CONFIRM_MESSAGE,
  ERROR_MESSAGE,
  SUCCESS_MESSAGE,
} from "../../constants/messages";
import PATH, { PARAM } from "../../constants/path";
import useForm from "../../hooks/useForm";
import useRecruitmentContext from "../../hooks/useRecruitmentContext";
import useTokenContext from "../../hooks/useTokenContext";
import FormProvider from "../../provider/FormProvider";
import { generateQuery, parseQuery } from "../../utils/route/query";
import { validateURL } from "../../utils/validation/url";

import styles from "./ApplicationRegister.module.css";
import Label from "../../components/@common/Label/Label";
import Description from "../../components/@common/Description/Description";
import CheckBox from "../../components/form/CheckBox/CheckBox";
import Form from "../../components/form/Form/Form";
import FormInput from "../../components/form/FormInput/FormInput";
import FormTextarea from "../../components/form/FormTextarea/FormTextarea";
import ResetButton from "../../components/form/ResetButton";
import SubmitButton from "../../components/form/SubmitButton";
import TempSaveButton from "../../components/form/TempSaveButton";
import RecruitmentItem from "../../components/RecruitmentItem/RecruitmentItem";
import Container from "../../components/@common/Container/Container";
import { formatDateTime } from "../../utils/format/date";

const pathToEdit = (recruitmentId) =>
  generatePath(PATH.APPLICATION_FORM, {
    status: PARAM.APPLICATION_FORM_STATUS.EDIT,
  }) + generateQuery({ recruitmentId });

const ApplicationRegister = () => {
  const history = useHistory();
  const location = useLocation();
  const { token } = useTokenContext();
  const { status } = useParams();

  const { recruitmentId } = parseQuery(location.search);

  const { recruitment } = useRecruitmentContext();
  const currentRecruitment = recruitment.findById(Number(recruitmentId));

  const [recruitmentItems, setRecruitmentItems] = useState([]);
  const [initialFormData, setInitialFormData] = useState({});
  const [modifiedDateTime, setModifiedDateTime] = useState("");

  const fetchRecruitmentItems = useCallback(async () => {
    try {
      const { data } = await Api.fetchItems(recruitmentId);

      setRecruitmentItems(data);
    } catch (e) {
      alert(e.response.data.message);
      history.replace(PATH.HOME);
    }
  }, [history, recruitmentId]);

  const fillForm = (applicationForm) => {
    setInitialFormData((prev) => {
      const answers = applicationForm.answers.reduce((acc, cur, index) => {
        acc[`recruitment-item-${index}`] = cur.contents;

        return acc;
      }, {});

      return {
        ...prev,
        referenceUrl: applicationForm.referenceUrl,
        ...answers,
      };
    });

    setModifiedDateTime(
      formatDateTime(new Date(applicationForm.modifiedDateTime))
    );
  };

  const fetchApplicationForm = useCallback(async () => {
    try {
      const { data } = await Api.fetchForm({
        token,
        recruitmentId,
      });

      fillForm(data);
    } catch (e) {
      alert(e.response.data.message);
      history.replace(PATH.HOME);
    }
  }, [history, token, recruitmentId]);

  const save = async (answers, referenceUrl, submitted) => {
    Api.updateForm({
      token,
      data: {
        recruitmentId,
        referenceUrl,
        submitted,
        answers,
      },
    });

    setModifiedDateTime(formatDateTime(new Date()));
  };

  const submit = async (value) => {
    if (window.confirm(CONFIRM_MESSAGE.SUBMIT_APPLICATION)) {
      const answers = recruitmentItems.map((item, index) => ({
        contents: value[`recruitment-item-${index}`],
        recruitmentItemId: item.id,
      }));

      try {
        await save(answers, value.url, true);
        alert(SUCCESS_MESSAGE.API.SUBMIT_APPLICATION);
      } catch (e) {
        alert(e.response.data.message);
      } finally {
        history.replace(PATH.HOME);
      }
    }
  };

  const { value, handleSubmit, ...methods } = useForm({
    validators: { url: validateURL },
    submit,
  });

  useEffect(() => {
    const init = async () => {
      try {
        await fetchRecruitmentItems();

        if (status === PARAM.APPLICATION_FORM_STATUS.EDIT) {
          await fetchApplicationForm();
        } else {
          await Api.createForm({
            token,
            recruitmentId,
          });
        }
      } catch (error) {
        console.error(error);

        const isAlreadyRegister =
          error.response.data.message === ERROR_MESSAGE.API.ALREADY_REGISTER;

        if (isAlreadyRegister) {
          await fetchApplicationForm();
        } else {
          alert(error.response.data.message);
          history.replace(PATH.HOME);
        }
      }
    };

    init();
  }, [
    recruitment,
    recruitmentId,
    history,
    status,
    token,
    fetchApplicationForm,
    fetchRecruitmentItems,
  ]);

  const answers = recruitmentItems.map((item, index) => ({
    contents: value[`recruitment-item-${index}`],
    recruitmentItemId: item.id,
  }));

  const onSaveTemp = async () => {
    try {
      await save(answers, value.url, false);

      alert(SUCCESS_MESSAGE.API.SAVE_APPLICATION);

      if (status !== PARAM.APPLICATION_FORM_STATUS.EDIT) {
        history.replace(pathToEdit(recruitmentId));
      }
    } catch (e) {
      alert(e.response.data.message);
      history.replace(PATH.HOME);
    }
  };

  return (
    <div className={styles.box}>
      {currentRecruitment && (
        <RecruitmentItem recruitment={currentRecruitment} />
      )}

      <Container title="지원서 작성">
        <FormProvider value={value} {...methods}>
          <Form onSubmit={handleSubmit}>
            {status === PARAM.APPLICATION_FORM_STATUS.EDIT && (
              <p className={styles["autosave-indicator"]}>
                {`임시 저장되었습니다. (${modifiedDateTime})`}
              </p>
            )}
            {recruitmentItems.length !== 0 &&
              recruitmentItems.map((item, index) => (
                <div key={item.id}>
                  <FormTextarea
                    name={`recruitment-item-${index}`}
                    initialValue={initialFormData[`recruitment-item-${index}`]}
                    label={`${index + 1}. ${item.title}`}
                    description={item.description}
                    placeholder="내용을 입력해 주세요."
                    maxLength={item.maximumLength}
                    required
                  />
                </div>
              ))}
            <FormInput
              name="url"
              type="url"
              initialValue={initialFormData.referenceUrl}
              description={
                <>
                  자신을 드러낼 수 있는 개인 블로그, GitHub, 포트폴리오 주소
                  등이 있다면 입력해 주세요.
                  <div className={styles.description}>
                    여러 개가 있는 경우 Notion, Google 문서 등을 사용하여 하나로
                    묶어 주세요.
                  </div>
                </>
              }
              label="URL"
              placeholder="ex) https://tecoble.techcourse.co.kr/"
            />
            <div>
              <Label required>지원서 작성 내용 사실 확인</Label>
              <Description>
                기재한 사실 중 허위사실이 발견되는 즉시, 교육 대상자에서
                제외되며 향후 지원도 불가능합니다.
              </Description>
              <CheckBox name="agree" label="동의합니다." />
            </div>

            <div className={styles.buttons}>
              <ResetButton />
              <TempSaveButton onSaveTemp={onSaveTemp} />
              <SubmitButton />
            </div>
          </Form>
        </FormProvider>
      </Container>
    </div>
  );
};

export default ApplicationRegister;
