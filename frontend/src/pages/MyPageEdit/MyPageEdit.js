import React from "react";
import { useHistory } from "react-router";
import myPageImage from "../../assets/image/myPage.svg";
import Button, { BUTTON_VARIANT } from "../../components/@common/Button/Button";
import Container from "../../components/@common/Container/Container";
import MessageTextInput from "../../components/@common/MessageTextInput/MessageTextInput";
import TextInput from "../../components/@common/TextInput/TextInput";
import BirthField from "../../components/form/BirthField/BirthField";
import Form from "../../components/form/Form/Form";
import FormInput from "../../components/form/FormInput/FormInput";
import useForm from "../../hooks/useForm";
import useUserInfoContext from "../../hooks/useUserInfoContext";
import FormProvider from "../../provider/FormProvider";
import * as styles from "./MyPageEdit.module.css";

const MyPageEdit = () => {
  const history = useHistory();
  const { userInfo, setUserInfo } = useUserInfoContext();

  const submit = async (value) => {
    await setUserInfo(value);
    alert("정보 변경에 성공했습니다.");
  };

  const { handleSubmit, ...methods } = useForm({
    submit,
  });

  return (
    <Container title={`${userInfo?.email} 님`}>
      <div className={styles.box}>
        <div className={styles["illust-box"]}>
          <img src={myPageImage} alt="자기소개서 일러스트" />
        </div>
        <FormProvider {...methods}>
          <Form className={styles["input-box"]}>
            <MessageTextInput
              label="이름"
              className={styles.input}
              value={userInfo?.name}
              readOnly
            />
            <FormInput
              name="phoneNumber"
              initialValue={userInfo?.phoneNumber}
              type="tel"
              label="전화번호"
            />
            <BirthField initialValue={userInfo?.birthday} readOnly />

            <div className={styles.buttons}>
              <Button
                type="button"
                variant={BUTTON_VARIANT.OUTLINED}
                onClick={() => history.goBack()}
              >
                취소
              </Button>
              <Button>확인</Button>
            </div>
          </Form>
        </FormProvider>
      </div>
    </Container>
  );
};

export default MyPageEdit;
