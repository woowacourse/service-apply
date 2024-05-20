import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import myPageImage from "../../assets/image/myPage.svg";
import Button from "../../components/@common/Button/Button";
import Container from "../../components/@common/Container/Container";
import MessageTextInput from "../../components/@common/MessageTextInput/MessageTextInput";
import BirthField from "../../components/form/BirthField/BirthField";
import CancelButton from "../../components/form/CancelButton/CancelButton";
import Form from "../../components/form/Form/Form";
import { FORM } from "../../constants/form";
import { ERROR_MESSAGE, SUCCESS_MESSAGE } from "../../constants/messages";
import { PATH } from "../../constants/path";
import useMyPageEditForm, { MY_PAGE_EDIT_FORM_NAME } from "../../hooks/useMyPageEditForm";
import useMemberInfoContext from "../../hooks/useMemberInfoContext";
import styles from "./MyPageEdit.module.css";

const MyPageEdit = () => {
  const navigate = useNavigate();
  const { memberInfo, updateMemberInfo } = useMemberInfoContext();

  const { form, errorMessage, init, handleChanges, isEmpty, isValid } = useMyPageEditForm();

  const handleSubmitError = (error) => {
    if (!error) return;

    alert(ERROR_MESSAGE.API.EDIT_FAILURE);
    navigate(PATH.MY_PAGE);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      await updateMemberInfo(form);
      alert(SUCCESS_MESSAGE.API.EDIT_MY_PAGE);
      navigate(PATH.MY_PAGE);
    } catch (error) {
      handleSubmitError(error);
    }
  };

  useEffect(() => {
    if (memberInfo === null) return;

    init({
      requiredForm: {
        [MY_PAGE_EDIT_FORM_NAME.PHONE_NUMBER]: memberInfo.phoneNumber,
      },
    });
  }, [memberInfo]);

  return (
    <Container title={`${memberInfo?.name ?? ""} 님`}>
      <div className={styles.box}>
        <div className={styles["illust-box"]}>
          <img src={myPageImage} alt="자기소개서 일러스트" />
        </div>
        <Form className={styles["input-box"]} onSubmit={handleSubmit}>
          <MessageTextInput
            label="이메일"
            name={MY_PAGE_EDIT_FORM_NAME.EMAIL}
            className={styles.input}
            value={memberInfo?.email || ""}
            disabled
          />
          <BirthField
            name={MY_PAGE_EDIT_FORM_NAME.BIRTHDAY}
            value={new Date(memberInfo?.birthday || null)}
            disabled
          />
          <MessageTextInput
            label="휴대전화 번호"
            type="tel"
            name={MY_PAGE_EDIT_FORM_NAME.PHONE_NUMBER}
            value={form[MY_PAGE_EDIT_FORM_NAME.PHONE_NUMBER]}
            onChange={handleChanges[MY_PAGE_EDIT_FORM_NAME.PHONE_NUMBER]}
            errorMessage={errorMessage[MY_PAGE_EDIT_FORM_NAME.PHONE_NUMBER]}
            maxLength={FORM.PHONE_NUMBER_MAX_LENGTH}
          />
          <MessageTextInput
            label="GitHub 사용자 이름"
            name={MY_PAGE_EDIT_FORM_NAME.GITHUB_USERNAME}
            className={styles.input}
            value={memberInfo?.githubUsername || ""}
            disabled
          />
          <div className={styles.buttons}>
            <CancelButton />
            <Button disabled={!isValid || isEmpty}>확인</Button>
          </div>
        </Form>
      </div>
    </Container>
  );
};

export default MyPageEdit;
