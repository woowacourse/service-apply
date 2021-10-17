import React, { useEffect } from "react";
import { useHistory } from "react-router";
import myPageImage from "../../assets/image/myPage.svg";
import Container from "../../components/@common/Container/Container";
import MessageTextInput from "../../components/@common/MessageTextInput/MessageTextInput";
import BirthField from "../../components/form/BirthField/BirthField";
import CancelButton from "../../components/form/CancelButton/CancelButton";
import Form from "../../components/form/Form/Form";
import SubmitButton from "../../components/form/SubmitButton/SubmitButton";
import { ERROR_MESSAGE, SUCCESS_MESSAGE } from "../../constants/messages";
import PATH from "../../constants/path";
import useForm from "../../hooks/useForm";
import usePhoneNumber from "../../hooks/usePhoneNumber";
import useUserInfoContext from "../../hooks/useUserInfoContext";
import FormProvider from "../../provider/FormProvider";
import { PHONE_NUMBER_REGEX_STR } from "../../utils/validation/phoneNumber";
import * as styles from "./MyPageEdit.module.css";

const MyPageEdit = () => {
  const history = useHistory();
  const { userInfo, updateUserInfo } = useUserInfoContext();
  const {
    phoneNumber,
    setPhoneNumber,
    handlePhoneNumberChange,
    phoneNumberErrorMessage,
  } = usePhoneNumber();

  const submit = async (value) => {
    try {
      await updateUserInfo({ ...value, phoneNumber });
      alert(SUCCESS_MESSAGE.API.EDIT_MY_PAGE);
    } catch (e) {
      alert(ERROR_MESSAGE.API.EDIT_FAILURE);
    }

    history.push(PATH.MY_PAGE);
  };

  const { handleSubmit, ...methods } = useForm({ submit });

  useEffect(() => {
    setPhoneNumber(userInfo?.phoneNumber);
  }, [userInfo]);

  return (
    <Container title={`${userInfo?.email} 님`}>
      <div className={styles.box}>
        <div className={styles["illust-box"]}>
          <img src={myPageImage} alt="자기소개서 일러스트" />
        </div>
        <FormProvider {...methods}>
          <Form className={styles["input-box"]} onSubmit={handleSubmit}>
            <MessageTextInput
              name="name"
              label="이름"
              className={styles.input}
              value={userInfo?.name}
              readOnly
            />
            <MessageTextInput
              type="tel"
              name="phoneNumber"
              initialValue={userInfo?.phoneNumber}
              value={phoneNumber}
              onChange={handlePhoneNumberChange}
              errorMessage={phoneNumberErrorMessage}
              label="핸드폰 번호"
              pattern={PHONE_NUMBER_REGEX_STR}
              autoComplete="off"
              required
            />
            <BirthField initialValue={userInfo?.birthday} readOnly />

            <div className={styles.buttons}>
              <CancelButton onClick={history.goBack} />
              <SubmitButton>확인</SubmitButton>
            </div>
          </Form>
        </FormProvider>
      </div>
    </Container>
  );
};

export default MyPageEdit;
