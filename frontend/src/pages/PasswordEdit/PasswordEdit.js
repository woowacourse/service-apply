import { useState } from "react";
import { useHistory } from "react-router-dom";
import { fetchPasswordEdit } from "../../api";
import Button from "../../components/form/Button/Button";
import Form from "../../components/form/Form/Form";
import TextField from "../../components/form/TextField/TextField";
import { ERROR_MESSAGE } from "../../constants/messages";
import useTokenContext from "../../hooks/useTokenContext";
import { isValidPassword } from "../../utils/validation/password";
import styles from "./PasswordEdit.module.css";

const validator = {
  password: isValidPassword,
  newPassword: isValidPassword,
  rePassword:
    (rePassword) =>
    ({ newPassword }) =>
      rePassword === newPassword,
};

const message = {
  password: ERROR_MESSAGE.VALIDATION.PASSWORD,
  newPassword: ERROR_MESSAGE.VALIDATION.PASSWORD,
  rePassword: ERROR_MESSAGE.VALIDATION.RE_PASSWORD,
};

const PasswordEdit = () => {
  const [value, setValue] = useState({
    password: "",
    newPassword: "",
    rePassword: "",
  });
  const [errorMessage, setErrorMessage] = useState({
    password: "",
    newPassword: "",
    rePassword: "",
  });
  const { resetToken } = useTokenContext();

  const history = useHistory();

  const disabled =
    Object.values(value).filter(Boolean).length < 3 ||
    Object.values(errorMessage).filter(Boolean).length > 0;

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      await fetchPasswordEdit({
        token: value.token,
        password: value.password,
        newPassword: value.newPassword,
      });
      alert("비밀번호가 변경되었습니다. 다시 로그인해주세요.");

      resetToken();
      history.push("/login");
    } catch (e) {
      alert(e.response.data.message);
    }
  };

  const handleChange = ({ target: { name, value: inputValue } }) => {
    const isValid = validator[name](inputValue);

    if (typeof isValid === "function" ? isValid(value) : isValid) {
      setErrorMessage((errorMessage) => ({
        ...errorMessage,
        [name]: "",
      }));
    } else {
      setErrorMessage((errorMessage) => ({
        ...errorMessage,
        [name]: message[name],
      }));
    }

    setValue((prevValue) => ({ ...prevValue, [name]: inputValue }));
  };

  return (
    <div className={styles["password-edit"]}>
      <Form onSubmit={handleSubmit}>
        <h1>비밀번호 변경</h1>
        <TextField
          name="password"
          type="password"
          label="기존 비밀번호"
          value={value.password}
          placeholder="기존 비밀번호를 입력해 주세요"
          onChange={handleChange}
          required
        />
        <p className={styles["rule-field"]}>{errorMessage.password}</p>
        <TextField
          name="newPassword"
          type="password"
          label="새 비밀번호"
          value={value.newPassword}
          placeholder="비밀번호를 입력해 주세요"
          onChange={handleChange}
          required
        />
        <p className={styles["rule-field"]}>{errorMessage.newPassword}</p>
        <TextField
          name="rePassword"
          type="password"
          label="비밀번호 확인"
          value={value.rePassword}
          placeholder="비밀번호를 다시 한 번 입력해 주세요"
          onChange={handleChange}
          required
        />
        <p className={styles["rule-field"]}>{errorMessage.rePassword}</p>
        <div className={styles.buttons}>
          <Button cancel onClick={() => history.goBack()}>
            이전
          </Button>
          <Button disabled={disabled} type="submit">
            확인
          </Button>
        </div>
      </Form>
    </div>
  );
};

export default PasswordEdit;
