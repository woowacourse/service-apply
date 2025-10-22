import { useState } from "react";
import { AxiosError } from "axios";
import * as Api from "../../api";

import Container, { CONTAINER_SIZE } from "../../components/@common/Container/Container";
import Description from "../../components/@common/Description/Description";
import Label from "../../components/@common/Label/Label";
import MessageTextInput from "../../components/@common/MessageTextInput/MessageTextInput";
import CheckBox from "../../components/form/CheckBox/CheckBox";
import Button, { BUTTON_VARIANT } from "../../components/@common/Button/Button";
import { ERROR_MESSAGE } from "../../constants/messages";
import styles from "./Withdrawal.module.css";
import useTokenContext from "../../hooks/useTokenContext";
import { useNavigate } from "react-router-dom";
import { PATH } from "../../constants/path";
import { ERROR_CODE } from "../../constants/errorCodes";

const WITHDRAWAL_FORM_NAME = {
  PASSWORD: "password",
  IS_AGREED: "isAgreed",
};

const Withdrawal = () => {
  const [password, setPassword] = useState("");
  const [isAgreed, setIsAgreed] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  const { token, resetToken } = useTokenContext();
  const navigate = useNavigate();
  const clickableForWithdrawal = isAgreed && password;

  const handleChangedPassword = (event: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(event.target.value);
  };

  const handleCapsLockState = (event: React.KeyboardEvent<HTMLInputElement>) => {
    const newErrorMessage = event.getModifierState("CapsLock")
      ? ERROR_MESSAGE.VALIDATION.PASSWORD_CAPS_LOCK
      : "";

    setErrorMessage(newErrorMessage);
  };

  const toggleAgree = () => {
    setIsAgreed(!isAgreed);
  };

  const handleFailedToWithdraw = (error: AxiosError) => {
    if (!error) return;

    switch (error.response?.status) {
      case ERROR_CODE.WITHDRAW.FORBIDDEN:
        alert(ERROR_MESSAGE.API.FAILED_TO_WITHDRAW_BECAUSE_OF_PASSWORD);
        return;
      default:
        alert(ERROR_MESSAGE.API.FAILED_TO_WITHDRAW_OTHER_REASONS);
        return;
    }
  };

  const requestWithdraw = async () => {
    try {
      await Api.fetchWithdraw({
        token,
        password,
      });

      resetToken();
      alert(ERROR_MESSAGE.API.SUCCEED_TO_WITHDRAW);
      navigate(PATH.HOME, { replace: true });
    } catch (error) {
      handleFailedToWithdraw(error as AxiosError);
    }
  };

  return (
    <Container size={CONTAINER_SIZE.NARROW} title="탈퇴하기">
      <MessageTextInput
        label="본인 인증"
        placeholder="비밀번호를 입력해 주세요"
        type="password"
        className={styles["text-input-box"]}
        name={WITHDRAWAL_FORM_NAME.PASSWORD}
        value={password}
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
          checked={isAgreed}
          onChange={toggleAgree}
          className={styles["check-box"]}
          required
        />
      </div>
      <div className={styles["buttons"]}>
        <Button type="button" variant={BUTTON_VARIANT.OUTLINED}>
          취소
        </Button>
        <Button
          type="button"
          variant={BUTTON_VARIANT.CONTAINED}
          disabled={!clickableForWithdrawal}
          onClick={requestWithdraw}
        >
          탈퇴하기
        </Button>
      </div>
    </Container>
  );
};

export default Withdrawal;
