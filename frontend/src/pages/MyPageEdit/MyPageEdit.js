import React, { useEffect } from "react";
import { useHistory } from "react-router";
import myPageImage from "../../assets/image/myPage.svg";
import Button, { BUTTON_VARIANT } from "../../components/@common/Button/Button";
import Container from "../../components/@common/Container/Container";
import MessageTextInput from "../../components/@common/MessageTextInput/MessageTextInput";
import BirthField from "../../components/form/BirthField/BirthField";
import Form from "../../components/form/Form/Form";
import { ERROR_MESSAGE, SUCCESS_MESSAGE } from "../../constants/messages";
import PATH from "../../constants/path";
import useMyPageEditForm, {
  MY_PAGE_EDIT_FORM,
} from "../../hooks/useMyPageEditForm";
import useUserInfoContext from "../../hooks/useUserInfoContext";
import * as styles from "./MyPageEdit.module.css";

const MyPageEdit = () => {
  const history = useHistory();
  const { userInfo, updateUserInfo } = useUserInfoContext();

  const { form, errorMessage, init, handleChange, isEmpty, isValid } =
    useMyPageEditForm();

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      await updateUserInfo(form);
      alert(SUCCESS_MESSAGE.API.EDIT_MY_PAGE);
      history.push(PATH.MY_PAGE);
    } catch (e) {
      alert(ERROR_MESSAGE.API.EDIT_FAILURE);
    }
  };

  useEffect(() => {
    if (userInfo === null) return;

    init({
      requiredForm: {
        [MY_PAGE_EDIT_FORM.PHONE_NUMBER]: userInfo.phoneNumber,
      },
    });
  }, [userInfo]);

  return (
    <Container title={`${userInfo?.name} 님`}>
      <div className={styles.box}>
        <div className={styles["illust-box"]}>
          <img src={myPageImage} alt="자기소개서 일러스트" />
        </div>
        <Form className={styles["input-box"]} onSubmit={handleSubmit}>
          <MessageTextInput
            label="이메일"
            name={MY_PAGE_EDIT_FORM.EMAIL}
            className={styles.input}
            value={userInfo?.email}
            readOnly
          />
          <MessageTextInput
            label="핸드폰번호"
            type="tel"
            name={MY_PAGE_EDIT_FORM.PHONE_NUMBER}
            value={form[MY_PAGE_EDIT_FORM.PHONE_NUMBER]}
            onChange={handleChange[MY_PAGE_EDIT_FORM.PHONE_NUMBER]}
            errorMessage={errorMessage[MY_PAGE_EDIT_FORM.PHONE_NUMBER]}
            initialValue={userInfo?.phoneNumber}
          />
          <BirthField
            name={MY_PAGE_EDIT_FORM.BIRTHDAY}
            value={userInfo?.birthday}
            readOnly
          />

          <div className={styles.buttons}>
            <Button
              type="button"
              variant={BUTTON_VARIANT.OUTLINED}
              onClick={history.goBack}
            >
              취소
            </Button>
            <Button disabled={!isValid || isEmpty}>확인</Button>
          </div>
        </Form>
      </div>
    </Container>
  );
};

export default MyPageEdit;
