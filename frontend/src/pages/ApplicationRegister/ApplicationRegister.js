import React, { useEffect, useState } from "react";
import { useHistory, useLocation, useParams } from "react-router";
import * as Api from "../../api";
import {
  Button,
  CheckBox,
  Description,
  Field,
  Form,
  Label,
  TextField,
} from "../../components/form";
import RecruitCard from "../../components/RecruitCard/RecruitCard";
import { ALREADY_REGISTER } from "../../constants/messages";
import useRecruitmentContext from "../../hooks/useRecruitmentContext";
import useTokenContext from "../../hooks/useTokenContext";
import { formatDateTime } from "../../utils/date";
import styles from "./ApplicationRegister.module.css";

const ApplicationRegister = () => {
  const history = useHistory();
  const location = useLocation();
  const { token } = useTokenContext();
  const { status } = useParams();

  const recruitmentId = location.search.recruitmentId ?? "";

  const { recruitment: recruitmentApi } = useRecruitmentContext();
  const recruitment = recruitmentApi.findById(recruitmentId) ?? {
    title: "",
    startDateTime: "",
    endDateTime: "",
  };

  const [recruitmentItems, setRecruitmentItems] = useState([]);
  const [formData, setFormData] = useState({
    factCheck: false,
    referenceUrl: "",
    modifiedDateTime: null,
  });
  const [answer, setAnswer] = useState({});
  const [errorMessage, setErrorMessage] = useState({});

  const completedForm =
    Object.keys(answer).every((id) => answer[id] !== "") && formData.factCheck;

  const reset = (recruitmentItems) => {
    const initValue = {};
    recruitmentItems.forEach(({ id }) => (initValue[id] = ""));

    setAnswer(initValue);
    setErrorMessage(initValue);
  };

  const fetchRecruitmentItems = async () => {
    try {
      const { data } = await Api.fetchItems(recruitmentId);

      setRecruitmentItems(data.body.recruitmentItems);

      reset(recruitmentItems);
    } catch (e) {
      alert(e.response.data.message);
      history.replace("/");
    }
  };

  const fillForm = (applicationForm) => {
    setFormData((prev) => ({
      ...prev,
      referenceUrl: applicationForm.referenceUrl,
      modifiedDateTime: formatDateTime(
        new Date(applicationForm.modifiedDateTime)
      ),
    }));

    const nextContents = {};
    applicationForm.answers.map(
      (item) => (nextContents[item.recruitmentItemId] = item.contents)
    );

    setAnswer(nextContents);
  };

  const fetchApplicationForm = async () => {
    try {
      const { data } = await Api.fetchForm({
        token,
        recruitmentId,
      });

      fillForm(data.body);
    } catch (e) {
      alert(e.response.data.message);
      history.replace("/");
    }
  };

  const onChangeAnswer = (id) => (event) => {
    setAnswer((prev) => ({
      ...prev,
      [id]: event.target.value,
    }));
  };

  const onChangeReferenceUrl = (event) => {
    setFormData((prev) => ({ ...prev, referenceUrl: event.target.value }));
  };

  const onReset = () => {
    if (window.confirm("정말 초기화하시겠습니까?")) {
      setFormData({
        factCheck: false,
        referenceUrl: "",
        modifiedDateTime: null,
      });

      reset(recruitmentItems);
    }
  };

  const onChangeFactCheck = (event) => {
    setFormData((prev) => ({ ...prev, factCheck: event.target.checked }));
  };

  const save = async (submitted) => {
    const answers = Object.keys(answer).map((id) => ({
      contents: answer[id],
      recruitmentItemId: id,
    }));

    Api.updateForm({
      token,
      data: {
        recruitmentId,
        referenceUrl: formData.referenceUrl,
        submitted,
        answers,
      },
    });
  };

  const onSaveTemp = async () => {
    try {
      await save(false);
      alert("정상적으로 저장되었습니다.");

      if (status !== "edit") {
        history.replace(
          "/application-forms/edit?recruitmentId=" + recruitmentId
        );
      }

      await fetchApplicationForm();
    } catch (e) {
      alert(e.response.data.message);
      history.replace("/");
    }
  };

  const onSubmit = async (event) => {
    event.preventDefault();

    if (
      window.confirm(
        "제출하신 뒤에는 수정하실 수 없습니다. 정말로 제출하시겠습니까?"
      )
    ) {
      try {
        await save(true);
        alert("정상적으로 제출되었습니다.");
      } catch (e) {
        alert(e.response.data.message);
      } finally {
        history.replace("/");
      }
    }
  };

  useEffect(() => {
    (async () => {
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
        if (error.response.data.message === ALREADY_REGISTER) {
          alert("이미 신청서를 작성했습니다. 로그인 페이지로 이동합니다.");
          history.replace("/login");
        } else {
          alert(error.response.data.message);
          history.replace("/");
        }
      }
    })();
  }, []);

  return (
    <div className={styles["application-register"]}>
      <RecruitCard
        className={styles["recruit-card"]}
        title={recruitment.title}
        startDateTime={recruitment.startDateTime}
        endDateTime={recruitment.endDateTime}
      />
      <Form className={styles["application-form"]} onSubmit={onSubmit}>
        <h2>지원서 작성</h2>
        {status === "edit" && (
          <p className={styles["autosave-indicator"]}>
            {`임시 저장되었습니다. (${formData.modifiedDateTime})`}
          </p>
        )}
        {recruitmentItems.map((item, index) => (
          <div key={item.id}>
            <TextField
              name="recruitment-item"
              type="textarea"
              label={`${index + 1}. ${item.title}`}
              description={item.description}
              placeholder="내용을 입력해 주세요."
              maxLength={item.maximumLength}
              onChange={onChangeAnswer(item.id)}
              value={answer[item.id] ?? ""}
              required
            />
            <p className={styles["rule-field"]}>
              {errorMessage[item.id] ?? ""}
            </p>
          </div>
        ))}
        <TextField
          name="url"
          type="url"
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
          onChange={onChangeReferenceUrl}
          value={formData.referenceUrl}
          placeholder="ex) https://woowacourse.github.io/javable"
        />
        <p className={styles["rule-field"]}></p>
        <Field>
          <Label required>지원서 작성 내용 사실 확인</Label>
          <Description>
            기재한 사실 중 허위사실이 발견되는 즉시, 교육 대상자에서 제외되며
            향후 지원도 불가능합니다.
          </Description>
          <CheckBox label="동의합니다." onChange={onChangeFactCheck} />
        </Field>
        <div className={styles["button-wrapper"]}>
          <Button type="reset" onClick={onReset}>
            초기화
          </Button>
          <Button type="button" onClick={onSaveTemp}>
            임시 저장
          </Button>
          <Button type="submit" disabled={!completedForm}>
            제출
          </Button>
        </div>
      </Form>
    </div>
  );
};

export default ApplicationRegister;
