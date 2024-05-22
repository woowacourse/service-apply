import { useState } from "react";
import Container, { CONTAINER_SIZE } from "../../components/@common/Container/Container";
import Description from "../../components/@common/Description/Description";
import Label from "../../components/@common/Label/Label";
import MessageTextInput from "../../components/@common/MessageTextInput/MessageTextInput";
import CheckBox from "../../components/form/CheckBox/CheckBox";
import { PASSWORD_EDIT_FORM_NAME } from "../../hooks/usePasswordEditForm";
import Button, { BUTTON_VARIANT } from "../../components/@common/Button/Button";
import { ERROR_MESSAGE } from "../../constants/messages";
import styles from "./Withdrawal.module.css";

export const WITHDRAWAL_FORM_NAME = {
  PASSWORD: "password",
  IS_AGREED: "isAgreed",
};

const initialRequiredForm = {
  [WITHDRAWAL_FORM_NAME.PASSWORD]: "",
  [WITHDRAWAL_FORM_NAME.IS_AGREED]: false,
};

const Withdrawal = () => {
  const [requiredForm, setRequiredForm] = useState(initialRequiredForm);
  const [errorMessage, setErrorMessage] = useState("");

  const handleChangedPassword = (event: React.ChangeEvent<HTMLInputElement>) => {
    setRequiredForm({
      ...requiredForm,
      [WITHDRAWAL_FORM_NAME.PASSWORD]: event.target.value,
    });
  };

  const handleCapsLockState = (event: React.KeyboardEvent<HTMLInputElement>) => {
    const newErrorMessage = event.getModifierState("CapsLock")
      ? ERROR_MESSAGE.VALIDATION.PASSWORD_CAPSLOCK
      : "";

    setErrorMessage(newErrorMessage);
  };

  const toggleAgree = () => {
    setRequiredForm({
      ...requiredForm,
      [WITHDRAWAL_FORM_NAME.IS_AGREED]: !requiredForm[WITHDRAWAL_FORM_NAME.IS_AGREED],
    });
  };

  return (
    <Container size={CONTAINER_SIZE.NARROW} title="탈퇴하기">
      <MessageTextInput
        label="본인 인증"
        placeholder="비밀번호를 입력해 주세요"
        type="password"
        className={styles["text-input-box"]}
        name={WITHDRAWAL_FORM_NAME.PASSWORD}
        value={requiredForm[WITHDRAWAL_FORM_NAME.PASSWORD] as string}
        onChange={handleChangedPassword}
        onKeyUp={handleCapsLockState}
        errorMessage={errorMessage}
        required
      />
      <div className={styles["withdrawal-box"]}>
        <Label>탈퇴 유의 사항</Label>
        <Description>
          <ul className={styles["withdrawal-terms"]}>
            <li>탈퇴 시 진행 중인 교육 프로그램 선발 과정에서 제외됩니다.</li>
            <li>탈퇴하면 지원 기록을 포함한 모든 정보가 영구 삭제되며 복구할 수 없습니다. </li>
            <li>동일한 이메일로 재가입하더라도 탈퇴 전 정보는 복구되지 않습니다.</li>
          </ul>
        </Description>
        <CheckBox
          name="agree"
          label="모든 정보를 삭제하는 것에 동의합니다."
          checked={requiredForm[WITHDRAWAL_FORM_NAME.IS_AGREED] as boolean}
          onChange={toggleAgree}
          className={styles["check-box"]}
          required
        />
      </div>
      <div className={styles["buttons"]}>
        <Button type="button" variant={BUTTON_VARIANT.OUTLINED}>
          취소
        </Button>
        <Button type="button" variant={BUTTON_VARIANT.DANGER_CONTAINED}>
          탈퇴하기
        </Button>
      </div>
    </Container>
  );
};

export default Withdrawal;
