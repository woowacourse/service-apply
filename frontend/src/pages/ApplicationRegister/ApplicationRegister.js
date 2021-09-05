import React, { useEffect, useState } from "react";
import { useHistory, useLocation, useParams } from "react-router-dom";
import * as Api from "../../api";
import {
  Button,
  CheckBox,
  Description,
  Field,
  Label,
} from "../../components/form";
import RecruitCard from "../../components/RecruitCard/RecruitCard";
import { ERROR_MESSAGE } from "../../constants/messages";
import useFormContext from "../../hooks/useFormContext";
import useRecruitmentContext from "../../hooks/useRecruitmentContext";
import useTokenContext from "../../hooks/useTokenContext";
import FormProvider from "../../provider/FormProvider/FormProvider";
import InputField from "../../provider/FormProvider/InputField";
import ResetButton from "../../provider/FormProvider/ResetButton";
import SubmitButton from "../../provider/FormProvider/SubmitButton";
import { formatDateTime } from "../../utils/date";
import parseQuery from "../../utils/route/query";
import { validateURL } from "../../utils/validation/url";
import styles from "./ApplicationRegister.module.css";

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

  useEffect(() => {
    const init = async () => {
      try {
        await fetchRecruitmentItems();

        if (status === "edit") {
          await fetchApplicationForm();
        } else {
          await Api.createForm({
            token,
            recruitmentId,
          });
        }
      } catch (error) {
        console.error(error);

        if (
          error.response.data.message === ERROR_MESSAGE.API.ALREADY_REGISTER
        ) {
          alert("이미 신청서를 작성했습니다. 로그인 페이지로 이동합니다.");
          history.replace("/login");
        } else {
          alert(error.response.data.message);
          history.replace("/");
        }
      }
    };

    init();
  }, [recruitment, recruitmentId]);

  const fetchRecruitmentItems = async () => {
    try {
      const { data } = await Api.fetchItems(recruitmentId);

      setRecruitmentItems(data);
    } catch (e) {
      alert(e.response.data.message);
      history.replace("/");
    }
  };

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

  const fetchApplicationForm = async () => {
    try {
      const { data } = await Api.fetchForm({
        token,
        recruitmentId,
      });

      fillForm(data);
    } catch (e) {
      alert(e.response.data.message);
      history.replace("/");
    }
  };

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
    if (
      window.confirm(
        "제출하신 뒤에는 수정하실 수 없습니다. 정말로 제출하시겠습니까?"
      )
    ) {
      const answers = recruitmentItems.map((item, index) => ({
        contents: value[`recruitment-item-${index}`],
        recruitmentItemId: item.id,
      }));

      try {
        await save(answers, value.url, true);
        alert("정상적으로 제출되었습니다.");
      } catch (e) {
        alert(e.response.data.message);
      } finally {
        history.replace("/");
      }
    }
  };

  const SaveButton = () => {
    const history = useHistory();
    const { value } = useFormContext();

    const answers = recruitmentItems.map((item, index) => ({
      contents: value[`recruitment-item-${index}`],
      recruitmentItemId: item.id,
    }));

    const onSaveTemp = async () => {
      try {
        await save(answers, value.url, false);
        alert("정상적으로 저장되었습니다.");

        if (status !== "edit") {
          history.replace(
            "/application-forms/edit?recruitmentId=" + recruitmentId
          );
        }
      } catch (e) {
        alert(e.response.data.message);
        history.replace("/");
      }
    };

    return <Button onClick={onSaveTemp}>임시 저장</Button>;
  };

  return (
    <div className={styles["application-register"]}>
      {currentRecruitment && (
        <RecruitCard
          className={styles["recruit-card"]}
          title={currentRecruitment.title}
          startDateTime={currentRecruitment.startDateTime}
          endDateTime={currentRecruitment.endDateTime}
        />
      )}
      <FormProvider
        className={styles["application-form"]}
        submit={submit}
        validators={{ url: validateURL }}
      >
        <h2>지원서 작성</h2>
        {status === "edit" && (
          <p className={styles["autosave-indicator"]}>
            {`임시 저장되었습니다. (${modifiedDateTime})`}
          </p>
        )}
        {recruitmentItems.length !== 0 &&
          recruitmentItems.map((item, index) => (
            <div key={item.id}>
              <InputField
                name={`recruitment-item-${index}`}
                type="textarea"
                initialValue={initialFormData[`recruitment-item-${index}`]}
                label={`${index + 1}. ${item.title}`}
                description={item.description}
                placeholder="내용을 입력해 주세요."
                maxLength={item.maximumLength}
                required
              />
            </div>
          ))}
        <InputField
          name="url"
          type="url"
          initialValue={initialFormData.referenceUrl}
          description={
            <>
              자신을 드러낼 수 있는 개인 블로그, GitHub, 포트폴리오 주소 등이
              있다면 입력해 주세요.
              <div className={styles.description}>
                여러 개가 있는 경우 Notion, Google 문서 등을 사용하여 하나로
                묶어 주세요.
              </div>
            </>
          }
          label="URL"
          placeholder="ex) https://woowacourse.github.io/javable"
        />
        <Field>
          <Label required>지원서 작성 내용 사실 확인</Label>
          <Description>
            기재한 사실 중 허위사실이 발견되는 즉시, 교육 대상자에서 제외되며
            향후 지원도 불가능합니다.
          </Description>
          <CheckBox name="agree" label="동의합니다." />
        </Field>
        <div className={styles["button-wrapper"]}>
          <ResetButton>초기화</ResetButton>
          <SaveButton />
          <SubmitButton>제출</SubmitButton>
        </div>
      </FormProvider>
    </div>
  );
};

export default ApplicationRegister;
