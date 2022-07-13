import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import myPageImage from "../../assets/image/myPage.svg";
import Button from "../../components/@common/Button/Button";
import Container from "../../components/@common/Container/Container";
import MessageTextInput from "../../components/@common/MessageTextInput/MessageTextInput";
import BirthField from "../../components/form/BirthField/BirthField";
import CancelButton from "../../components/form/CancelButton/CancelButton";
import Form from "../../components/form/Form/Form";
import FORM from "../../constants/form";
import { ERROR_MESSAGE, SUCCESS_MESSAGE } from "../../constants/messages";
import PATH from "../../constants/path";
import useMyPageEditForm, { MY_PAGE_EDIT_FORM_NAME } from "../../hooks/useMyPageEditForm";
import useUserInfoContext from "../../hooks/useUserInfoContext";
import * as styles from "./MyPageEdit.module.css";

const MyPageEdit = () => {
  const navigate = useNavigate();
  const { userInfo, updateUserInfo } = useUserInfoContext();

  const { form, errorMessage, init, handleChanges, isEmpty, isValid } = useMyPageEditForm();

  const handleSubmitError = (error) => {
    if (!error) return;

    alert(ERROR_MESSAGE.API.EDIT_FAILURE);
    navigate(PATH.MY_PAGE);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      await updateUserInfo(form);
      alert(SUCCESS_MESSAGE.API.EDIT_MY_PAGE);
      navigate(PATH.MY_PAGE);
    } catch (error) {
      handleSubmitError(error);
    }
  };

  useEffect(() => {
    if (userInfo === null) return;

    init({
      requiredForm: {
        [MY_PAGE_EDIT_FORM_NAME.PHONE_NUMBER]: userInfo.phoneNumber,
      },
    });
  }, [userInfo]);

  return (
    <Container title={`${userInfo?.name ?? ""} 님`}>
      <div className={styles.box}>
        <div className={styles["illust-box"]}>
          <img src={myPageImage} alt="자기소개서 일러스트" />
        </div>
        <Form className={styles["input-box"]} onSubmit={handleSubmit}>
          <MessageTextInput
            label="이메일"
            name={MY_PAGE_EDIT_FORM_NAME.EMAIL}
            className={styles.input}
            value={userInfo?.email || ""}
            disabled
          />
          <MessageTextInput
            label="휴대폰 번호"
            type="tel"
            name={MY_PAGE_EDIT_FORM_NAME.PHONE_NUMBER}
            value={form[MY_PAGE_EDIT_FORM_NAME.PHONE_NUMBER]}
            onChange={handleChanges[MY_PAGE_EDIT_FORM_NAME.PHONE_NUMBER]}
            errorMessage={errorMessage[MY_PAGE_EDIT_FORM_NAME.PHONE_NUMBER]}
            maxLength={FORM.PHONE_NUMBER_MAX_LENGTH}
          />
          <BirthField
            name={MY_PAGE_EDIT_FORM_NAME.BIRTHDAY}
            value={new Date(userInfo?.birthday || null)}
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
